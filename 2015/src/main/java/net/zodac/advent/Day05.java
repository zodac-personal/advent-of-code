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

import java.util.Collection;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2015, Day 5.
 *
 * @see <a href="https://adventofcode.com/2015/day/5">AoC 2015, Day 5</a>
 */
public final class Day05 {

    private static final String[] INVALID_SUBSTRINGS = {"ab", "cd", "pq", "xy"};
    private static final long MINIMUM_NUMBER_OF_VOWELS_REQUIRED = 3L;

    private Day05() {

    }

    /**
     * Takes a {@link Collection} of {@link String}s and validates them based on the following rules:
     * <ul>
     *     <li>Cannot contain any of the substrings: "ab", "cd", "pq", "xy"</li>
     *     <li>Must have at least 3 vowels</li>
     *     <li>Must have at least one character repeating itself</li>
     * </ul>
     *
     * @param strings the {@link String}s to validate
     * @return the number of valid {@link String}s
     */
    public static long countValidStringsPartOne(final Collection<String> strings) {
        return strings
            .stream()
            .filter(Day05::isValidForPartOne)
            .count();
    }

    private static boolean isValidForPartOne(final String input) {
        return !StringUtils.containsAny(input, INVALID_SUBSTRINGS)
            && StringUtils.countVowels(input) >= MINIMUM_NUMBER_OF_VOWELS_REQUIRED
            && StringUtils.hasRepeatedCharacterInOrder(input);
    }

    /**
     * Takes a {@link Collection} of {@link String}s and validates them based on the following rules:
     * <ul>
     *     <li>Must have a repeated character, with a character in between</li>
     *     <li>Must have a repeated pair of characters, with no overlap between the pairs</li>
     * </ul>
     *
     * @param strings the {@link String}s to validate
     * @return the number of valid {@link String}s
     */
    public static long countValidStringsPartTwo(final Collection<String> strings) {
        return strings
            .stream()
            .filter(Day05::isValidForPartTwo)
            .count();
    }

    private static boolean isValidForPartTwo(final String input) {
        return hasSandwichCharacters(input) && hasRepeatedCharacterPairWithNoOverlap(input);
    }

    private static boolean hasSandwichCharacters(final String input) {
        if (input.isBlank()) {
            return false;
        }

        final int stringLength = input.length();
        for (int i = 2; i < stringLength; i++) {
            if (input.charAt(i - 2) == input.charAt(i)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasRepeatedCharacterPairWithNoOverlap(final String input) {
        if (input.isBlank()) {
            return false;
        }

        final int stringLength = input.length();
        for (int i = 1; i < stringLength; i++) {
            final String remainderOfInput = input.substring(i + 1);
            final String pair = String.format("%c%c", input.charAt(i - 1), input.charAt(i));

            if (remainderOfInput.contains(pair)) {
                return true;
            }
        }

        return false;
    }
}
