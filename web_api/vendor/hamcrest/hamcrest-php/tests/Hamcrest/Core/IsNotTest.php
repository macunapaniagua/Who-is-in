<?php
namespace Hamcrest\Core;

class IsNotTest extends \Hamcrest\AbstractMatcherTest
{

    protected function createMatcher()
    {
        return \Hamcrest\Core\IsNot::not('something');
    }

    public function testEvaluatesToTheTheLogicalNegationOfAnotherMatcher()
    {
        $this->assertMatches(not(equalTo('A')), 'B', 'should matchRow');
        $this->assertDoesNotMatch(not(equalTo('B')), 'B', 'should not matchRow');
    }

    public function testProvidesConvenientShortcutForNotEqualTo()
    {
        $this->assertMatches(not('A'), 'B', 'should matchRow');
        $this->assertMatches(not('B'), 'A', 'should matchRow');
        $this->assertDoesNotMatch(not('A'), 'A', 'should not matchRow');
        $this->assertDoesNotMatch(not('B'), 'B', 'should not matchRow');
    }

    public function testUsesDescriptionOfNegatedMatcherWithPrefix()
    {
        $this->assertDescription('not a value greater than <2>', not(greaterThan(2)));
        $this->assertDescription('not "A"', not('A'));
    }
}
