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

import java.util.Set;
import java.util.function.ToIntFunction;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.AdjacentPointsSelector;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;

/**
 * Solution for 2015, Day 18.
 *
 * @see <a href="https://adventofcode.com/2015/day/18">AoC 2015, Day 18</a>
 */
public final class Day18 {

    private static final Set<Integer> NEIGHBOUR_VALUES_FOR_OFF_POINT = Set.of(3);
    private static final Set<Integer> NEIGHBOUR_VALUES_FOR_ON_POINT = Set.of(2, 3);
    private static final ToIntFunction<Boolean> EVALUATOR = booleanValue -> Boolean.TRUE.equals(booleanValue) ? 1 : 0;

    private Day18() {

    }

    /**
     * Given a {@link Boolean} {@link Grid}, plays <a href="https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life">Conway's Game of Life</a> a number
     * of times.
     *
     * @param initialState       the initial state of the {@link Boolean} {@link Grid}
     * @param numberOfIterations the number of times to execute the 'Game of Life'
     * @param cornersAlwaysOn    if {@code true}, the value of the corners of the {@link Grid} must always be set to <b>true</b>
     * @return the number of {@link Point}s that have been turned on
     */
    public static long playGameOfLife(final Grid<Boolean> initialState, final int numberOfIterations, final boolean cornersAlwaysOn) {
        Grid<Boolean> booleanGrid = initialState;
        if (cornersAlwaysOn) {
            booleanGrid = initialState.updateCorners(true);
        }

        for (int i = 0; i < numberOfIterations; i++) {
            booleanGrid = playGameOfLife(booleanGrid, cornersAlwaysOn);
        }

        return booleanGrid.sumValues(EVALUATOR);
    }

    private static Grid<Boolean> playGameOfLife(final Grid<Boolean> booleanGrid, final boolean cornersAlwaysOn) {
        final int numberOfRows = booleanGrid.numberOfRows();
        final int numberOfColumns = booleanGrid.numberOfColumns();
        final Boolean[][] newGrid = new Boolean[numberOfRows][numberOfColumns];

        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                if (cornersAlwaysOn && booleanGrid.isCorner(row, column)) {
                    newGrid[row][column] = true;
                    continue;
                }

                final Point currentPoint = Point.of(row, column);
                final boolean isSet = booleanGrid.at(currentPoint);

                final AdjacentPointsSelector adjacentPointsSelector = AdjacentPointsSelector.bounded(false, AdjacentDirection.ALL, numberOfRows);
                final int valueOfSetNeighbours = currentPoint.getAdjacentPoints(adjacentPointsSelector)
                    .mapToInt(point -> EVALUATOR.applyAsInt(booleanGrid.at(point)))
                    .sum();

                if (isSet) {
                    newGrid[row][column] = NEIGHBOUR_VALUES_FOR_ON_POINT.contains(valueOfSetNeighbours);
                } else {
                    newGrid[row][column] = NEIGHBOUR_VALUES_FOR_OFF_POINT.contains(valueOfSetNeighbours);
                }
            }
        }

        return new Grid<>(newGrid);
    }
}
