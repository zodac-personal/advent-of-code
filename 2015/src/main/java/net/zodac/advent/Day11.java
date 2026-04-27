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
import java.util.HashSet;

/**
 * Solution for 2015, Day 11.
 *
 * @see <a href="https://adventofcode.com/2015/day/11">AoC 2015, Day 11</a>
 */
public final class Day11 {

    // Excludes 'i', 'l' and 'o'
    private static final String POSSIBLE_CHARACTERS = "abcdefghjkmnpqrstuvwxyz";

    private Day11() {

    }

    /**
     * Takes the input {@link String} and increments it until a valid password is found.
     *
     * <p>
     * The increment works by taking the last character of the {@link String} and getting the next character from {@value #POSSIBLE_CHARACTERS}. If
     * the last character is the last possible value, it rolls over to the first possible value, and we begin incrementing the next-left character of
     * the {@link String}.
     *
     * <p>
     * A {@link String} is considered a valid password if it meets these three criteria:
     * <ul>
     *     <li>It does not contain the characters <b>i</b>, <b>l</b> or <b>o</b></li>
     *     <li>It contains two distinct pairs of characters (<b>abcddee</b> is valid while <b>aabcdaa</b> is not)</li>
     *     <li>It contains at least one run of three characters in ascending order (<b>ablmnp</b> is valid)</li>
     * </ul>
     *
     * @param input the input password to be incremented
     * @return the next valid password
     */
    public static String findNextValidPassword(final String input) {
        String outputPassword = incrementPassword(input);

        while (!isValidPassword(outputPassword)) {
            outputPassword = incrementPassword(outputPassword);
        }

        return outputPassword;
    }

    private static String incrementPassword(final String input) {
        final StringBuilder mutableInput = new StringBuilder(input);
        int index = mutableInput.length() - 1;

        while (index >= 0) {
            final char currentChar = mutableInput.charAt(index);

            // If current character is last in possible set, rollover to the first possible character, and start incrementing the next character
            if (currentChar == POSSIBLE_CHARACTERS.charAt(POSSIBLE_CHARACTERS.length() - 1)) {
                mutableInput.setCharAt(index, POSSIBLE_CHARACTERS.charAt(0));
                index--;
            } else {
                mutableInput.setCharAt(index, getNextCharacter(currentChar));
                return mutableInput.toString();
            }
        }

        throw new IllegalStateException(String.format("Cannot update input '%s' from current state '%s'", input, mutableInput));
    }

    private static char getNextCharacter(final char currentChar) {
        final int indexOfChar = POSSIBLE_CHARACTERS.indexOf(currentChar);
        return POSSIBLE_CHARACTERS.charAt(indexOfChar + 1);
    }

    private static boolean isValidPassword(final CharSequence inputPassword) {
        return containsThreeCharactersInAscendingOrder(inputPassword) && containsTwoDifferentPairs(inputPassword);
    }

    private static boolean containsTwoDifferentPairs(final CharSequence inputPassword) {
        final Collection<Character> pairs = new HashSet<>();
        final int inputPasswordLengthExceptLast = inputPassword.length() - 1;
        for (int i = 0; i < inputPasswordLengthExceptLast; i++) {
            final char currentChar = inputPassword.charAt(i);
            final char nextChar = inputPassword.charAt(i + 1);

            // We could shortcut this loop by checking if another pair already exists, but as the String is only 8 chars long, probably not worth it
            if (currentChar == nextChar) {
                pairs.add(currentChar);
                i++; // Skip the next character since we know it is a match for the current character
            }
        }
        return pairs.size() > 1;
    }

    private static boolean containsThreeCharactersInAscendingOrder(final CharSequence inputPassword) {
        final int inputPasswordLengthExceptLastTwo = inputPassword.length() - 2;
        for (int i = 0; i < inputPasswordLengthExceptLastTwo; i++) {
            final char currentChar = inputPassword.charAt(i);
            final char nextChar = inputPassword.charAt(i + 1);
            final char nextNextChar = inputPassword.charAt(i + 2);

            // Step back nextChar and nextNextChar to see if they match currentChar
            if (currentChar == (nextChar - 1) && currentChar == (nextNextChar - 2)) {
                return true;
            }
        }

        return false;
    }
}
