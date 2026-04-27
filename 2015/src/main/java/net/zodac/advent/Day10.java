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

/**
 * Solution for 2015, Day 10.
 *
 * @see <a href="https://adventofcode.com/2015/day/10">AoC 2015, Day 10</a>
 */
public final class Day10 {

    private Day10() {

    }

    /**
     * Performs the 'lookAndSay' sequence on the input {@code numberOfExecutions} times, and returns the length of the result.
     *
     * @param input              the {@link String} to perform the sequence on
     * @param numberOfExecutions the number of times to perform the sequence
     * @return the length of the end result after the sequence has been applied {@code numberOfExecutions} times
     */
    public static long performLookAndSaySequenceAndReturnLength(final String input, final int numberOfExecutions) {
        String output = input;
        for (int i = 0; i < numberOfExecutions; i++) {
            output = lookAndSay(output);
        }
        return output.length();
    }

    private static String lookAndSay(final String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be blank");
        }

        final StringBuilder output = new StringBuilder();

        final int inputLength = input.length();
        for (int i = 0; i < inputLength; i++) {
            final char currentChar = input.charAt(i);

            if (!Character.isDigit(currentChar)) {
                throw new IllegalArgumentException(String.format("Character '%s' is not a valid integer", currentChar));
            }

            int count = 1;
            while (i + 1 < inputLength && input.charAt(i + 1) == currentChar) {
                i++; // If next char is same as current, move the pointer to the next char
                count++;
            }

            output.append(count).append(currentChar);
        }
        return output.toString();
    }
}
