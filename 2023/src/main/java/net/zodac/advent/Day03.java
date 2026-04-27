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

import java.util.List;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.AdjacentPointsSelector;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;

/**
 * Solution for 2023, Day 3.
 *
 * @see <a href="https://adventofcode.com/2023/day/3">[2023: 03] Gear Ratios</a>
 */
public final class Day03 {

    private static final char DEFAULT_SYMBOL = '.';
    private static final char GEAR_SYMBOL = '*';
    private static final int EXPECTED_NUMBER_OF_NUMBERS_FOR_GEAR_RATIO = 2;

    private Day03() {

    }

    /**
     * Given a {@link Character} {@link Grid}, any non-digit {@link Character} that is not {@link #DEFAULT_SYMBOL} is considered an engine part.
     * If an engine part is touching a number (meaning any of the 8 adjacent points overlaps any digit), then that number is considered an engine part
     * number. This method retrieves all those part numbers, then sums their values.
     *
     * @param characterGrid the {@link Grid}
     * @return the sum of all engine part numbers
     * @see Point#getAdjacentPoints(AdjacentPointsSelector)
     */
    public static long sumOfAllPartNumbers(final Grid<Character> characterGrid) {
        final int numberOfRows = characterGrid.numberOfRows();
        final int numberOfColumns = characterGrid.numberOfColumns();
        long total = 0L;

        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                final Point partPoint = Point.of(row, column);
                final char character = characterGrid.at(partPoint);

                if (isNotEnginePart(character)) {
                    continue;
                }

                total += findSumOfNumbersForPart(characterGrid, partPoint);
            }
        }

        return total;
    }

    private static boolean isNotEnginePart(final char character) {
        return character == DEFAULT_SYMBOL || Character.isDigit(character);
    }

    private static long findSumOfNumbersForPart(final Grid<Character> characterGrid, final Point partPoint) {
        final AdjacentPointsSelector adjacentPointsSelector = AdjacentPointsSelector.bounded(false, AdjacentDirection.ALL, characterGrid.size());

        return partPoint
            .getAdjacentPoints(adjacentPointsSelector)
            .filter(point -> Character.isDigit(characterGrid.at(point)))
            .mapToInt(point -> findNumberInRow(characterGrid, point))
            .distinct()
            .sum();
    }

    private static int findNumberInRow(final Grid<Character> characterGrid, final Point pointOfKnownDigit) {
        final int yCoordinate = pointOfKnownDigit.y();
        int startColumn;
        int endColumn;

        for (startColumn = yCoordinate; startColumn > 0; startColumn--) {
            if (!Character.isDigit(characterGrid.at(pointOfKnownDigit.x(), startColumn - 1))) {
                break;
            }
        }

        final int numberOfColumnsExceptLast = characterGrid.numberOfColumns() - 1;
        for (endColumn = yCoordinate; endColumn < numberOfColumnsExceptLast; endColumn++) {
            if (!Character.isDigit(characterGrid.rowAt(pointOfKnownDigit.x())[endColumn + 1])) {
                break;
            }
        }

        final StringBuilder stringBuilder = new StringBuilder();
        for (int column = startColumn; column <= endColumn; column++) {
            stringBuilder.append(characterGrid.at(pointOfKnownDigit.x(), column));
        }

        return Integer.parseInt(stringBuilder.toString());
    }

    /**
     * Given a {@link Character} {@link Grid}, any {@link #GEAR_SYMBOL} is considered an engine gear. If that gear has exactly 2 touching numbers
     * (meaning 2 of the 8 adjacent points overlaps any digit), then we can calculate a gear ratio for that gear by calculating the product of the 2
     * numbers touching the gear. These gear ratios are then summed and returned.
     *
     * @param characterGrid the {@link Character} {@link Grid}
     * @return the sum of all gear ratios
     * @see Point#getAdjacentPoints(AdjacentPointsSelector)
     */
    public static long sumOfAllGearRatios(final Grid<Character> characterGrid) {
        final int numberOfRows = characterGrid.numberOfRows();
        final int numberOfColumns = characterGrid.numberOfColumns();
        long total = 0L;

        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                final Point gearPoint = Point.of(row, column);
                final char character = characterGrid.at(gearPoint);

                if (isNotGear(character)) {
                    continue;
                }

                total += findSumOfGearRatios(characterGrid, gearPoint);
            }
        }
        return total;
    }

    private static boolean isNotGear(final char character) {
        return character != GEAR_SYMBOL;
    }

    private static long findSumOfGearRatios(final Grid<Character> characterGrid, final Point gearPoint) {
        final int size = characterGrid.numberOfRows();
        final AdjacentPointsSelector adjacentPointsSelector = AdjacentPointsSelector.bounded(false, AdjacentDirection.ALL, size);

        final List<Integer> gearRatioValues = gearPoint
            .getAdjacentPoints(adjacentPointsSelector)
            .filter(point -> Character.isDigit(characterGrid.at(point)))
            .map(point -> findNumberInRow(characterGrid, point))
            .distinct()
            .toList();

        return gearRatioValues.size() == EXPECTED_NUMBER_OF_NUMBERS_FOR_GEAR_RATIO
            ? (long) gearRatioValues.getFirst() * gearRatioValues.getLast()
            : 0L;
    }
}
