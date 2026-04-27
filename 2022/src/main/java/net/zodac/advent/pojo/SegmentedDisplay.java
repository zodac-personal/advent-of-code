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

package net.zodac.advent.pojo;

import net.zodac.advent.util.ArrayUtils;

/**
 * Defined a segmented display of letters. Each letter has a height of {@value #DEFAULT_HEIGHT}, and a width of
 * {@value #COLUMNS_NEEDED_PER_CHARACTER}, allowing for a full column of blank spaces to space the different letters. For example, a display of 6x15
 * characters would display something like this:
 *
 * <pre>
 *     # # # . . # . . . . # # # # .
 *     # . . # . # . . . . # . . . .
 *     # . . # . # . . . . # # # . .
 *     # # # . . # . . . . # . . . .
 *     # . . . . # . . . . # . . . .
 *     # . . . . # # # # . # # # # .
 * </pre>
 */
public final class SegmentedDisplay {

    private static final int COLUMNS_NEEDED_PER_CHARACTER = 5; // Includes one column of blank spaces
    private static final int DEFAULT_HEIGHT = 6;

    private final Boolean[][] array;

    private SegmentedDisplay(final Boolean[][] array) {
        this.array = ArrayUtils.deepCopy(array);
    }

    /**
     * Creates a {@link SegmentedDisplay} for {@code numberOfCharacters} characters.
     *
     * @param numberOfCharacters the number of characters on the {@link SegmentedDisplay}
     * @return the {@link SegmentedDisplay}
     */
    public static SegmentedDisplay create(final int numberOfCharacters) {
        final Boolean[][] array = new Boolean[DEFAULT_HEIGHT][numberOfCharacters * COLUMNS_NEEDED_PER_CHARACTER];
        final Boolean[][] filledArray = ArrayUtils.deepFill(array, false);
        return new SegmentedDisplay(filledArray);
    }

    /**
     * The horizontal length of the {@link SegmentedDisplay}.
     *
     * @return the length
     */
    public int length() {
        return array[0].length;
    }

    /**
     * Switch on the specified cell on the {@link SegmentedDisplay}.
     *
     * @param row    the row of the cell
     * @param column the column of the cell
     */
    public void turnOn(final int row, final int column) {
        array[row][column] = true;
    }

    /**
     * Parses the {@link SegmentedDisplay} and returns the characters listed as a {@link String}.
     *
     * @return the {@link SegmentedDisplay} characters as a {@link String}
     * @see SegmentedDisplayAlphabetMapper
     */
    public String getCharacters() {
        final boolean[][][] chars = chunk();
        final StringBuilder stringBuilder = new StringBuilder();

        for (final boolean[][] charValues : chars) {
            stringBuilder.append(SegmentedDisplayAlphabetMapper.getOrEmpty(charValues));
        }

        return stringBuilder.toString();
    }

    private boolean[][][] chunk() {
        final int numChars = length() / COLUMNS_NEEDED_PER_CHARACTER;
        final int columnsNeeded = COLUMNS_NEEDED_PER_CHARACTER - 1; // Ignore blank column
        final boolean[][][] chars = new boolean[numChars][DEFAULT_HEIGHT][columnsNeeded];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                final int charNumber = j / 5;
                final int col = j % 5;

                // Skip the empty column
                if (col != columnsNeeded) {
                    chars[charNumber][i][col] = array[i][j];
                }
            }
        }

        return chars;
    }
}
