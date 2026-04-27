/*
 * BSD Zero Clause License
 *
 * Copyright (c) 2021-2026 zodac.net
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package net.zodac.advent;

import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2022, Day 6.
 *
 * @see <a href="https://adventofcode.com/2022/day/6">AoC 2022, Day 6</a>
 */
public final class Day06 {

    private Day06() {

    }

    /**
     * Given a {@link String} input, we look for the first sequence of {@code numberOfUniqueCharactersNeeded} unique characters, beginning at the
     * start of the {@link String}. We then return the location (index + 1) of the {@code numberOfUniqueCharactersNeeded}-th character in that
     * sequence.
     *
     * <p>
     * For example, given the {@link String} {@code nppdvjthqldpwncqszvftbrmjlhg}, the first sequence of <b>4</b> unique characters it at index 5, so
     * we return the value of the location, 6.
     *
     * @param input                          the input {@link String}
     * @param numberOfUniqueCharactersNeeded the number of unique characters in sequence to be found
     * @return the first location (index + 1) signifying at least {@code numberOfUniqueCharactersNeeded} unique characters exist in a sequence
     * @throws IllegalArgumentException if {@code numberOfUniqueCharactersNeeded} unique characters cannot be found in sequence
     */
    public static long findSequenceOfUniqueCharactersAndReturnLastIndex(final String input, final int numberOfUniqueCharactersNeeded) {
        final int range = input.length() - numberOfUniqueCharactersNeeded;
        for (int i = 0; i < range; i++) {

            // 'Window' the string from index to the wanted number of unique characters, then check for duplicates
            final String subStringToCheck = input.substring(i, i + numberOfUniqueCharactersNeeded);

            if (!StringUtils.containsDuplicates(subStringToCheck)) {
                return (long) i + numberOfUniqueCharactersNeeded;
            }
        }

        throw new IllegalArgumentException("Could not find %s unique characters in sequence for input: " + input);
    }
}
