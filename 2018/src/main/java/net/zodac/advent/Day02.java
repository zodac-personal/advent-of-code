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
import java.util.List;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2018, Day 2.
 *
 * @see <a href="https://adventofcode.com/2018/day/2">AoC 2018, Day 2</a>
 */
public final class Day02 {

    private Day02() {

    }

    /**
     * Given some box IDs in the form of {@link String}s, we calculate the checksum by counting the number of box IDs with:
     * <ul>
     *     <li>A character occurring exactly <b>2</b> times</li>
     *     <li>A character occurring exactly <b>3</b> times</li>
     * </ul>
     *
     * <p>
     * These counts are then multiplied to return the checksum of the provided box IDs.
     *
     * @param boxIds the {@link String}s of the box IDs
     * @return the checksum of the box IDs
     */
    public static long checksumOfBoxIds(final Collection<String> boxIds) {
        int numberOfDoubleCharacters = 0;
        int numberOfTripleCharacters = 0;

        for (final String boxId : boxIds) {
            if (hasAtLeastOneCharacterThatOccursExactlyTwoTimes(boxId)) {
                numberOfDoubleCharacters++;
            }

            if (hasAtLeastOneCharacterThatOccursExactlyThreeTimes(boxId)) {
                numberOfTripleCharacters++;
            }
        }

        return (long) numberOfDoubleCharacters * numberOfTripleCharacters;
    }

    /**
     * Given a {@link List} of box IDs in the form of {@link String}s, we know there will be one pair different by only <b>1</b> character. We find
     * this pair of box IDs, remove the different character, then return the common letters for the pair.
     *
     * @param boxIds the {@link String}s of the box IDs
     * @return the common characters of the valid box ID pair
     * @throws IllegalStateException thrown if a valid pair of box IDs could not be found
     */
    public static String findCommonCharactersForValidBoxIds(final List<String> boxIds) {
        final int expectedLengthAfterDifferencesRemoved = boxIds.getFirst().length() - 1;
        final int numberOfBoxIds = boxIds.size();

        for (int i = 0; i < numberOfBoxIds; i++) {
            final String currentBoxId = boxIds.get(i);

            for (int j = i + 1; j < numberOfBoxIds; j++) {
                final String nextBoxId = boxIds.get(j);

                final String differentCharactersRemoved = StringUtils.removeDifferentCharacters(currentBoxId, nextBoxId);
                if (differentCharactersRemoved.length() == expectedLengthAfterDifferencesRemoved) {
                    return differentCharactersRemoved;
                }
            }
        }

        throw new IllegalStateException("Could not find a valid pair of box IDs");
    }

    private static boolean hasAtLeastOneCharacterThatOccursExactlyTwoTimes(final CharSequence input) {
        return hasAtLeastOneCharacterThatOccursExactly(input, 2);
    }

    private static boolean hasAtLeastOneCharacterThatOccursExactlyThreeTimes(final CharSequence input) {
        return hasAtLeastOneCharacterThatOccursExactly(input, 3);
    }

    private static boolean hasAtLeastOneCharacterThatOccursExactly(final CharSequence input, final int times) {
        final int stringLength = input.length();
        final Collection<Character> checkedCharacters = new HashSet<>();

        for (int i = 0; i < stringLength - 1; i++) {
            final char currentChar = input.charAt(i);
            int count = 1; // Start count at 1 as we count the current occurrence

            if (checkedCharacters.contains(currentChar)) {
                continue;
            }

            for (int j = i + 1; j < stringLength; j++) {
                if (input.charAt(j) == currentChar) {
                    count++;
                }
            }

            if (count == times) {
                return true;
            }

            checkedCharacters.add(currentChar);
        }

        return false;
    }
}
