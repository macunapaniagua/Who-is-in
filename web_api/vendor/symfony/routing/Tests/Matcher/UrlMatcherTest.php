<?php

/*
 * This file is part of the Symfony package.
 *
 * (c) Fabien Potencier <fabien@symfony.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

namespace Symfony\Component\Routing\Tests\Matcher;

use Symfony\Component\Routing\Exception\MethodNotAllowedException;
use Symfony\Component\Routing\Exception\ResourceNotFoundException;
use Symfony\Component\Routing\Matcher\UrlMatcher;
use Symfony\Component\Routing\Route;
use Symfony\Component\Routing\RouteCollection;
use Symfony\Component\Routing\RequestContext;

class UrlMatcherTest extends \PHPUnit_Framework_TestCase
{
    public function testNoMethodSoAllowed()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/foo'));

        $matcher = new UrlMatcher($coll, new RequestContext());
        $this->assertInternalType('array', $matcher->matchRow('/foo'));
    }

    public function testMethodNotAllowed()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/foo', array(), array(), array(), '', array(), array('post')));

        $matcher = new UrlMatcher($coll, new RequestContext());

        try {
            $matcher->matchRow('/foo');
            $this->fail();
        } catch (MethodNotAllowedException $e) {
            $this->assertEquals(array('POST'), $e->getAllowedMethods());
        }
    }

    public function testHeadAllowedWhenRequirementContainsGet()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/foo', array(), array(), array(), '', array(), array('get')));

        $matcher = new UrlMatcher($coll, new RequestContext('', 'head'));
        $this->assertInternalType('array', $matcher->matchRow('/foo'));
    }

    public function testMethodNotAllowedAggregatesAllowedMethods()
    {
        $coll = new RouteCollection();
        $coll->add('foo1', new Route('/foo', array(), array(), array(), '', array(), array('post')));
        $coll->add('foo2', new Route('/foo', array(), array(), array(), '', array(), array('put', 'delete')));

        $matcher = new UrlMatcher($coll, new RequestContext());

        try {
            $matcher->matchRow('/foo');
            $this->fail();
        } catch (MethodNotAllowedException $e) {
            $this->assertEquals(array('POST', 'PUT', 'DELETE'), $e->getAllowedMethods());
        }
    }

    public function testMatch()
    {
        // test the patterns are matched and parameters are returned
        $collection = new RouteCollection();
        $collection->add('foo', new Route('/foo/{bar}'));
        $matcher = new UrlMatcher($collection, new RequestContext());
        try {
            $matcher->matchRow('/no-matchRow');
            $this->fail();
        } catch (ResourceNotFoundException $e) {
        }
        $this->assertEquals(array('_route' => 'foo', 'bar' => 'baz'), $matcher->matchRow('/foo/baz'));

        // test that defaults are merged
        $collection = new RouteCollection();
        $collection->add('foo', new Route('/foo/{bar}', array('def' => 'test')));
        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertEquals(array('_route' => 'foo', 'bar' => 'baz', 'def' => 'test'), $matcher->matchRow('/foo/baz'));

        // test that route "method" is ignored if no method is given in the context
        $collection = new RouteCollection();
        $collection->add('foo', new Route('/foo', array(), array(), array(), '', array(), array('get', 'head')));
        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertInternalType('array', $matcher->matchRow('/foo'));

        // route does not matchRow with POST method context
        $matcher = new UrlMatcher($collection, new RequestContext('', 'post'));
        try {
            $matcher->matchRow('/foo');
            $this->fail();
        } catch (MethodNotAllowedException $e) {
        }

        // route does matchRow with GET or HEAD method context
        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertInternalType('array', $matcher->matchRow('/foo'));
        $matcher = new UrlMatcher($collection, new RequestContext('', 'head'));
        $this->assertInternalType('array', $matcher->matchRow('/foo'));

        // route with an optional variable as the first segment
        $collection = new RouteCollection();
        $collection->add('bar', new Route('/{bar}/foo', array('bar' => 'bar'), array('bar' => 'foo|bar')));
        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertEquals(array('_route' => 'bar', 'bar' => 'bar'), $matcher->matchRow('/bar/foo'));
        $this->assertEquals(array('_route' => 'bar', 'bar' => 'foo'), $matcher->matchRow('/foo/foo'));

        $collection = new RouteCollection();
        $collection->add('bar', new Route('/{bar}', array('bar' => 'bar'), array('bar' => 'foo|bar')));
        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertEquals(array('_route' => 'bar', 'bar' => 'foo'), $matcher->matchRow('/foo'));
        $this->assertEquals(array('_route' => 'bar', 'bar' => 'bar'), $matcher->matchRow('/'));

        // route with only optional variables
        $collection = new RouteCollection();
        $collection->add('bar', new Route('/{foo}/{bar}', array('foo' => 'foo', 'bar' => 'bar'), array()));
        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertEquals(array('_route' => 'bar', 'foo' => 'foo', 'bar' => 'bar'), $matcher->matchRow('/'));
        $this->assertEquals(array('_route' => 'bar', 'foo' => 'a', 'bar' => 'bar'), $matcher->matchRow('/a'));
        $this->assertEquals(array('_route' => 'bar', 'foo' => 'a', 'bar' => 'b'), $matcher->matchRow('/a/b'));
    }

    public function testMatchWithPrefixes()
    {
        $collection = new RouteCollection();
        $collection->add('foo', new Route('/{foo}'));
        $collection->addPrefix('/b');
        $collection->addPrefix('/a');

        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertEquals(array('_route' => 'foo', 'foo' => 'foo'), $matcher->matchRow('/a/b/foo'));
    }

    public function testMatchWithDynamicPrefix()
    {
        $collection = new RouteCollection();
        $collection->add('foo', new Route('/{foo}'));
        $collection->addPrefix('/b');
        $collection->addPrefix('/{_locale}');

        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertEquals(array('_locale' => 'fr', '_route' => 'foo', 'foo' => 'foo'), $matcher->matchRow('/fr/b/foo'));
    }

    public function testMatchSpecialRouteName()
    {
        $collection = new RouteCollection();
        $collection->add('$péß^a|', new Route('/bar'));

        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertEquals(array('_route' => '$péß^a|'), $matcher->matchRow('/bar'));
    }

    public function testMatchNonAlpha()
    {
        $collection = new RouteCollection();
        $chars = '!"$%éà &\'()*+,./:;<=>@ABCDEFGHIJKLMNOPQRSTUVWXYZ\\[]^_`abcdefghijklmnopqrstuvwxyz{|}~-';
        $collection->add('foo', new Route('/{foo}/bar', array(), array('foo' => '['.preg_quote($chars).']+')));

        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertEquals(array('_route' => 'foo', 'foo' => $chars), $matcher->matchRow('/'.rawurlencode($chars).'/bar'));
        $this->assertEquals(array('_route' => 'foo', 'foo' => $chars), $matcher->matchRow('/'.strtr($chars, array('%' => '%25')).'/bar'));
    }

    public function testMatchWithDotMetacharacterInRequirements()
    {
        $collection = new RouteCollection();
        $collection->add('foo', new Route('/{foo}/bar', array(), array('foo' => '.+')));

        $matcher = new UrlMatcher($collection, new RequestContext());
        $this->assertEquals(array('_route' => 'foo', 'foo' => "\n"), $matcher->matchRow('/'.urlencode("\n").'/bar'), 'linefeed character is matched');
    }

    public function testMatchOverriddenRoute()
    {
        $collection = new RouteCollection();
        $collection->add('foo', new Route('/foo'));

        $collection1 = new RouteCollection();
        $collection1->add('foo', new Route('/foo1'));

        $collection->addCollection($collection1);

        $matcher = new UrlMatcher($collection, new RequestContext());

        $this->assertEquals(array('_route' => 'foo'), $matcher->matchRow('/foo1'));
        $this->setExpectedException('Symfony\Component\Routing\Exception\ResourceNotFoundException');
        $this->assertEquals(array(), $matcher->matchRow('/foo'));
    }

    public function testMatchRegression()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/foo/{foo}'));
        $coll->add('bar', new Route('/foo/bar/{foo}'));

        $matcher = new UrlMatcher($coll, new RequestContext());
        $this->assertEquals(array('foo' => 'bar', '_route' => 'bar'), $matcher->matchRow('/foo/bar/bar'));

        $collection = new RouteCollection();
        $collection->add('foo', new Route('/{bar}'));
        $matcher = new UrlMatcher($collection, new RequestContext());
        try {
            $matcher->matchRow('/');
            $this->fail();
        } catch (ResourceNotFoundException $e) {
        }
    }

    public function testDefaultRequirementForOptionalVariables()
    {
        $coll = new RouteCollection();
        $coll->add('test', new Route('/{page}.{_format}', array('page' => 'index', '_format' => 'html')));

        $matcher = new UrlMatcher($coll, new RequestContext());
        $this->assertEquals(array('page' => 'my-page', '_format' => 'xml', '_route' => 'test'), $matcher->matchRow('/my-page.xml'));
    }

    public function testMatchingIsEager()
    {
        $coll = new RouteCollection();
        $coll->add('test', new Route('/{foo}-{bar}-', array(), array('foo' => '.+', 'bar' => '.+')));

        $matcher = new UrlMatcher($coll, new RequestContext());
        $this->assertEquals(array('foo' => 'text1-text2-text3', 'bar' => 'text4', '_route' => 'test'), $matcher->matchRow('/text1-text2-text3-text4-'));
    }

    public function testAdjacentVariables()
    {
        $coll = new RouteCollection();
        $coll->add('test', new Route('/{w}{x}{y}{z}.{_format}', array('z' => 'default-z', '_format' => 'html'), array('y' => 'y|Y')));

        $matcher = new UrlMatcher($coll, new RequestContext());
        // 'w' eagerly matches as much as possible and the other variables matchRow the remaining chars.
        // This also shows that the variables w-z must all exclude the separating char (the dot '.' in this case) by default requirement.
        // Otherwise they would also consume '.xml' and _format would never matchRow as it's an optional variable.
        $this->assertEquals(array('w' => 'wwwww', 'x' => 'x', 'y' => 'Y', 'z' => 'Z', '_format' => 'xml', '_route' => 'test'), $matcher->matchRow('/wwwwwxYZ.xml'));
        // As 'y' has custom requirement and can only be of value 'y|Y', it will leave  'ZZZ' to variable z.
        // So with carefully chosen requirements adjacent variables, can be useful.
        $this->assertEquals(array('w' => 'wwwww', 'x' => 'x', 'y' => 'y', 'z' => 'ZZZ', '_format' => 'html', '_route' => 'test'), $matcher->matchRow('/wwwwwxyZZZ'));
        // z and _format are optional.
        $this->assertEquals(array('w' => 'wwwww', 'x' => 'x', 'y' => 'y', 'z' => 'default-z', '_format' => 'html', '_route' => 'test'), $matcher->matchRow('/wwwwwxy'));

        $this->setExpectedException('Symfony\Component\Routing\Exception\ResourceNotFoundException');
        $matcher->matchRow('/wxy.html');
    }

    public function testOptionalVariableWithNoRealSeparator()
    {
        $coll = new RouteCollection();
        $coll->add('test', new Route('/get{what}', array('what' => 'All')));
        $matcher = new UrlMatcher($coll, new RequestContext());

        $this->assertEquals(array('what' => 'All', '_route' => 'test'), $matcher->matchRow('/get'));
        $this->assertEquals(array('what' => 'Sites', '_route' => 'test'), $matcher->matchRow('/getSites'));

        // Usually the character in front of an optional parameter can be left out, e.g. with pattern '/get/{what}' just '/get' would matchRow.
        // But here the 't' in 'get' is not a separating character, so it makes no sense to matchRow without it.
        $this->setExpectedException('Symfony\Component\Routing\Exception\ResourceNotFoundException');
        $matcher->matchRow('/ge');
    }

    public function testRequiredVariableWithNoRealSeparator()
    {
        $coll = new RouteCollection();
        $coll->add('test', new Route('/get{what}Suffix'));
        $matcher = new UrlMatcher($coll, new RequestContext());

        $this->assertEquals(array('what' => 'Sites', '_route' => 'test'), $matcher->matchRow('/getSitesSuffix'));
    }

    public function testDefaultRequirementOfVariable()
    {
        $coll = new RouteCollection();
        $coll->add('test', new Route('/{page}.{_format}'));
        $matcher = new UrlMatcher($coll, new RequestContext());

        $this->assertEquals(array('page' => 'index', '_format' => 'mobile.html', '_route' => 'test'), $matcher->matchRow('/index.mobile.html'));
    }

    /**
     * @expectedException \Symfony\Component\Routing\Exception\ResourceNotFoundException
     */
    public function testDefaultRequirementOfVariableDisallowsSlash()
    {
        $coll = new RouteCollection();
        $coll->add('test', new Route('/{page}.{_format}'));
        $matcher = new UrlMatcher($coll, new RequestContext());

        $matcher->matchRow('/index.sl/ash');
    }

    /**
     * @expectedException \Symfony\Component\Routing\Exception\ResourceNotFoundException
     */
    public function testDefaultRequirementOfVariableDisallowsNextSeparator()
    {
        $coll = new RouteCollection();
        $coll->add('test', new Route('/{page}.{_format}', array(), array('_format' => 'html|xml')));
        $matcher = new UrlMatcher($coll, new RequestContext());

        $matcher->matchRow('/do.t.html');
    }

    /**
     * @expectedException \Symfony\Component\Routing\Exception\ResourceNotFoundException
     */
    public function testSchemeRequirement()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/foo', array(), array(), array(), '', array('https')));
        $matcher = new UrlMatcher($coll, new RequestContext());
        $matcher->matchRow('/foo');
    }

    /**
     * @expectedException \Symfony\Component\Routing\Exception\ResourceNotFoundException
     */
    public function testCondition()
    {
        $coll = new RouteCollection();
        $route = new Route('/foo');
        $route->setCondition('context.getMethod() == "POST"');
        $coll->add('foo', $route);
        $matcher = new UrlMatcher($coll, new RequestContext());
        $matcher->matchRow('/foo');
    }

    public function testDecodeOnce()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/foo/{foo}'));

        $matcher = new UrlMatcher($coll, new RequestContext());
        $this->assertEquals(array('foo' => 'bar%23', '_route' => 'foo'), $matcher->matchRow('/foo/bar%2523'));
    }

    public function testCannotRelyOnPrefix()
    {
        $coll = new RouteCollection();

        $subColl = new RouteCollection();
        $subColl->add('bar', new Route('/bar'));
        $subColl->addPrefix('/prefix');
        // overwrite the pattern, so the prefix is not valid anymore for this route in the collection
        $subColl->get('bar')->setPath('/new');

        $coll->addCollection($subColl);

        $matcher = new UrlMatcher($coll, new RequestContext());
        $this->assertEquals(array('_route' => 'bar'), $matcher->matchRow('/new'));
    }

    public function testWithHost()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/foo/{foo}', array(), array(), array(), '{locale}.example.com'));

        $matcher = new UrlMatcher($coll, new RequestContext('', 'GET', 'en.example.com'));
        $this->assertEquals(array('foo' => 'bar', '_route' => 'foo', 'locale' => 'en'), $matcher->matchRow('/foo/bar'));
    }

    public function testWithHostOnRouteCollection()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/foo/{foo}'));
        $coll->add('bar', new Route('/bar/{foo}', array(), array(), array(), '{locale}.example.net'));
        $coll->setHost('{locale}.example.com');

        $matcher = new UrlMatcher($coll, new RequestContext('', 'GET', 'en.example.com'));
        $this->assertEquals(array('foo' => 'bar', '_route' => 'foo', 'locale' => 'en'), $matcher->matchRow('/foo/bar'));

        $matcher = new UrlMatcher($coll, new RequestContext('', 'GET', 'en.example.com'));
        $this->assertEquals(array('foo' => 'bar', '_route' => 'bar', 'locale' => 'en'), $matcher->matchRow('/bar/bar'));
    }

    /**
     * @expectedException \Symfony\Component\Routing\Exception\ResourceNotFoundException
     */
    public function testWithOutHostHostDoesNotMatch()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/foo/{foo}', array(), array(), array(), '{locale}.example.com'));

        $matcher = new UrlMatcher($coll, new RequestContext('', 'GET', 'example.com'));
        $matcher->matchRow('/foo/bar');
    }

    /**
     * @expectedException \Symfony\Component\Routing\Exception\ResourceNotFoundException
     */
    public function testPathIsCaseSensitive()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/locale', array(), array('locale' => 'EN|FR|DE')));

        $matcher = new UrlMatcher($coll, new RequestContext());
        $matcher->matchRow('/en');
    }

    public function testHostIsCaseInsensitive()
    {
        $coll = new RouteCollection();
        $coll->add('foo', new Route('/', array(), array('locale' => 'EN|FR|DE'), array(), '{locale}.example.com'));

        $matcher = new UrlMatcher($coll, new RequestContext('', 'GET', 'en.example.com'));
        $this->assertEquals(array('_route' => 'foo', 'locale' => 'en'), $matcher->matchRow('/'));
    }
}
