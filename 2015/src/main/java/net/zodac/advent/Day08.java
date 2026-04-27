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
import java.util.regex.Pattern;

/**
 * Solution for 2015, Day 8.
 *
 * @see <a href="https://adventofcode.com/2015/day/8">AoC 2015, Day 8</a>
 */
public final class Day08 {

    private static final Pattern TWO_BACKSLASHES = Pattern.compile("\\\\{2}");
    private static final Pattern BACKSLASH_AND_DOUBLE_QUOTATION = Pattern.compile("\\\\\"");
    private static final Pattern BACKSLASH_X_AND_TWO_HEXADECIMAL_CHARACTERS = Pattern.compile("\\\\x[A-Za-z0-9]{2}");
    private static final String SINGLE_CHARACTER = "+";

    private Day08() {

    }

    /**
     * Calculates the different between the length of the literal {@link String}s provided, and the in-memory length of those {@link String}s. We can
     * assume there are only three escape scenarios to consider:
     * <ul>
     *     <li>\\</li>
     *     <li>\"</li>
     *     <li>\xAB, where 'AB' can be any two hexadecimal characters</li>
     * </ul>
     *
     * @param values the {@link String}s
     * @return the difference between the length of the literal and in-memory {@link String}s
     */
    public static long calculateDiffOfLiteralAndInMemoryLength(final Collection<? extends CharSequence> values) {
        long totalLiteralLength = 0L;
        long totalInMemoryLength = 0L;

        for (final CharSequence value : values) {
            totalLiteralLength += value.length();
            totalInMemoryLength += calculateInMemoryLength(value);
        }

        return totalLiteralLength - totalInMemoryLength;
    }

    private static int calculateInMemoryLength(final CharSequence value) {
        // Two backslashes (\\) can be considered 1 character
        final String first = TWO_BACKSLASHES.matcher(value).replaceAll(SINGLE_CHARACTER);
        // A backslash and a double quotation mark (\") can be considered 1 character
        final String second = BACKSLASH_AND_DOUBLE_QUOTATION.matcher(first).replaceAll(SINGLE_CHARACTER);
        // A backslash, 'x' literal and two hexadecimal characters (\xAB) can be considered 1 character
        final String third = BACKSLASH_X_AND_TWO_HEXADECIMAL_CHARACTERS.matcher(second).replaceAll(SINGLE_CHARACTER);

        // Remove length of the surrounding quotation marks
        return third.length() - 2;
    }

    /**
     * Calculates the different between the length of the fully-escaped version of the {@link String}s provided, and the length of those literal
     * {@link String}s. We can assume the following characters are to be escaped:
     * <ul>
     *     <li>A " becomes \"</li>
     *     <li>A \ becomes \\</li>
     * </ul>
     *
     * <p>
     * Also remember that the escaped {@link String} must still be surrounded by non-escaped " marks when complete.
     *
     * @param values the {@link String}s
     * @return the difference between the length of the escaped and literal {@link String}s
     */
    public static long calculateDiffOfEscapedAndLiteralLength(final Collection<String> values) {
        long totalEscapedLength = 0L;
        long totalLiteralLength = 0L;

        for (final String value : values) {
            totalEscapedLength += calculateEscapedLength(value);
            totalLiteralLength += value.length();
        }

        return totalEscapedLength - totalLiteralLength;
    }

    private static int calculateEscapedLength(final String value) {
        int length = 0;
        for (final Character c : value.toCharArray()) {
            if (c == '"' || c == '\\') {
                length += 2;
            } else {
                length++;
            }
        }

        // Add length of the surrounding quotation marks
        return length + 2;
    }
}
