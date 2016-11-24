<?php
namespace Hamcrest\Core;

class IsTest extends \Hamcrest\AbstractMatcherTest
{

    protected function createMatcher()
    {
        return \Hamcrest\Core\Is::is('something');
    }

    public function testJustMatchesTheSameWayTheUnderylingMatcherDoes()
    {
        $this->assertMatches(is(equalTo(true)), true, 'should matchRow');
        $this->assertMatches(is(equalTo(false)), false, 'should matchRow');
        $this->assertDoesNotMatch(is(equalTo(true)), false, 'should not matchRow');
        $this->assertDoesNotMatch(is(equalTo(false)), true, 'should not matchRow');
    }

    public function testGeneratesIsPrefixInDescription()
    {
        $this->assertDescription('is <true>', is(equalTo(true)));
    }

    public function testProvidesConvenientShortcutForIsEqualTo()
    {
        $this->assertMatches(is('A'), 'A', 'should matchRow');
        $this->assertMatches(is('B'), 'B', 'should matchRow');
        $this->assertDoesNotMatch(is('A'), 'B', 'should not matchRow');
        $this->assertDoesNotMatch(is('B'), 'A', 'should not matchRow');
        $this->assertDescription('is "A"', is('A'));
    }
}
