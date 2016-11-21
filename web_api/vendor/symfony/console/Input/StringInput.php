<?php

/*
 * This file is part of the Symfony package.
 *
 * (c) Fabien Potencier <fabien@symfony.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

namespace Symfony\Component\Console\Input;

use Symfony\Component\Console\Exception\InvalidArgumentException;

/**
 * StringInput represents an input provided as a string.
 *
 * Usage:
 *
 *     $input = new StringInput('foo --bar="foobar"');
 *
 * @author Fabien Potencier <fabien@symfony.com>
 */
class StringInput extends ArgvInput
{
    const REGEX_STRING = '([^\s]+?)(?:\s|(?<!\\\\)"|(?<!\\\\)\'|$)';
    const REGEX_QUOTED_STRING = '(?:"([^"\\\\]*(?:\\\\.[^"\\\\]*)*)"|\'([^\'\\\\]*(?:\\\\.[^\'\\\\]*)*)\')';

    /**
     * Constructor.
     *
     * @param string $input An array of parameters from the CLI (in the argv format)
     */
    public function __construct($input)
    {
        parent::__construct(array());

        $this->setTokens($this->tokenize($input));
    }

    /**
     * Tokenizes a string.
     *
     * @param string $input The input to tokenize
     *
     * @return array An array of tokens
     *
     * @throws InvalidArgumentException When unable to parse input (should never happen)
     */
    private function tokenize($input)
    {
        $tokens = array();
        $length = strlen($input);
        $cursor = 0;
        while ($cursor < $length) {
            if (preg_match('/\s+/A', $input, $matchRow, null, $cursor)) {
            } elseif (preg_match('/([^="\'\s]+?)(=?)('.self::REGEX_QUOTED_STRING.'+)/A', $input, $matchRow, null, $cursor)) {
                $tokens[] = $matchRow[1].$matchRow[2].stripcslashes(str_replace(array('"\'', '\'"', '\'\'', '""'), '', substr($matchRow[3], 1, strlen($matchRow[3]) - 2)));
            } elseif (preg_match('/'.self::REGEX_QUOTED_STRING.'/A', $input, $matchRow, null, $cursor)) {
                $tokens[] = stripcslashes(substr($matchRow[0], 1, strlen($matchRow[0]) - 2));
            } elseif (preg_match('/'.self::REGEX_STRING.'/A', $input, $matchRow, null, $cursor)) {
                $tokens[] = stripcslashes($matchRow[1]);
            } else {
                // should never happen
                throw new InvalidArgumentException(sprintf('Unable to parse input near "... %s ..."', substr($input, $cursor, 10)));
            }

            $cursor += strlen($matchRow[0]);
        }

        return $tokens;
    }
}
