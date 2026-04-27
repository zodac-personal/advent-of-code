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
import java.util.List;

/**
 * Solution for 2015, Day 1.
 *
 * @see <a href="https://adventofcode.com/2015/day/1">AoC 2015, Day 1</a>
 */
public final class Day01 {

    private static final char DECREMENT_CHARACTER = ')';
    private static final char INCREMENT_CHARACTER = '(';

    private Day01() {

    }

    /**
     * Takes the provided {@code values}. For each entry we will go up or down a floor. The behaviour is:
     * <ul>
     *     <li>If the character is {@value #INCREMENT_CHARACTER}, we go up 1 floor</li>
     *     <li>If the character is {@value #DECREMENT_CHARACTER}, we go down 1 floor</li>
     * </ul>
     *
     * <p>
     * We start from floor <b>0</b>.
     *
     * @param values the input defining whether to go up or down
     * @return the final floor
     */
    public static long findResultFloor(final Collection<Character> values) {
        long currentFloor = 0L;

        for (final char character : values) {
            if (character == INCREMENT_CHARACTER) {
                currentFloor++;
            } else if (character == DECREMENT_CHARACTER) {
                currentFloor--;
            }
        }

        return currentFloor;
    }

    /**
     * Takes the provided {@code values}, and searches for when we end up on a specified floor. For each entry we will go up or down a floor.
     * The behaviour is:
     * <ul>
     *     <li>If the character is {@value #INCREMENT_CHARACTER}, we go up 1 floor</li>
     *     <li>If the character is {@value #DECREMENT_CHARACTER}, we go down 1 floor</li>
     * </ul>
     *
     * <p>
     * We start from floor <b>0</b>.
     *
     * @param values      the input defining whether to go up or down
     * @param floorToFind the floor we want to find the index of
     * @return the index of {@code values} when we arrived at the wanted floor
     */
    public static long findIndexOfSpecificFloor(final List<Character> values, final int floorToFind) {
        long currentFloor = 0L;

        final int numberOfValues = values.size();
        for (int i = 0; i < numberOfValues; i++) {
            final char character = values.get(i);

            if (character == INCREMENT_CHARACTER) {
                currentFloor++;
            } else if (character == DECREMENT_CHARACTER) {
                currentFloor--;
            }

            if (currentFloor == floorToFind) {
                return i + 1L; // Expected index starts at 1, not 0
            }
        }

        throw new IllegalStateException("Input values never reached floor: " + floorToFind);
    }
}
