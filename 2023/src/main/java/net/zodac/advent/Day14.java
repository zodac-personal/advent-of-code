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

import net.zodac.advent.grid.Grid;
import net.zodac.advent.pojo.RotationDirection;
import net.zodac.advent.search.Cycle;
import net.zodac.advent.search.CycleFinder;

/**
 * Solution for 2023, Day 14.
 *
 * @see <a href="https://adventofcode.com/2023/day/14">[2023: 14] Parabolic Reflector Dish</a>
 */
public final class Day14 {

    private static final char EMPTY_SYMBOL = '.';
    private static final char ROCK_SYMBOL = 'O';

    private Day14() {

    }

    /**
     * Given a {@link Character} {@link Grid}, containing rocks ({@link #ROCK_SYMBOL}), we migrate all rocks north, until they no longer find an
     * existing empty space ({@link #EMPTY_SYMBOL}), at which point they come to rest.
     *
     * <p>
     * Once the rocks are settled, we calculate the load of the full {@link Grid}, which is calculated as the sum of the load for each row. And each
     * row's load is calculated as {@code numberOfRocks} * {@code rowMultiplier}. The {@code rowMultiplier} starts at <b>1</b> for the final row of
     * the {@link Grid}, and increments by <b>1</b> as iterate up the {@link Grid}.
     *
     * @param grid the {@link Character} {@link Grid}
     * @return the load after a single migration north
     */
    public static long countLoadAfterSingleNorthTurn(final Grid<Character> grid) {
        final Grid<Character> northTurn = sendRocksNorth(grid);
        return calculateLoad(northTurn);
    }

    /**
     * Given a {@link Character} {@link Grid}, containing rocks ({@link #ROCK_SYMBOL}), we migrate all rocks for a full rotation: north -> west ->
     * south -> east, in that order. Each rock will move in the given direction until they no longer find an existing empty space
     * ({@link #EMPTY_SYMBOL}), at which point they come to rest. This will be repeated for {@code numberOfCycles} cycles.
     *
     * <p>
     * Once the rocks are settled, we calculate the load of the full {@link Grid}, which is calculated as the sum of the load for each row. And each
     * row's load is calculated as {@code numberOfRocks} * {@code rowMultiplier}. The {@code rowMultiplier} starts at <b>1</b> for the final row of
     * the {@link Grid}, and increments by <b>1</b> as iterate up the {@link Grid}.
     *
     * <p>
     * Rather than moving the {@link Grid} elements in all four directions, we will only move them north, rotating the {@link Grid}
     * {@link RotationDirection#CLOCKWISE} between each direction.
     *
     * <p>
     * Similarly, In order to avoid executing the cycle {@code numberOfCycles} times, we will attempt to search for a {@link Cycle}. If one is found,
     * we will be able to 'jump-ahead' to the result of {@code numberOfCycles} without having to execute each iteration.
     *
     * @param grid           the {@link Character} {@link Grid}
     * @param numberOfCycles the number of rotations to perform on the {@link Grid}
     * @return the load after {@code numberOfCycles} full rotations
     * @see CycleFinder
     */
    public static long calculateLoadAfterFullCycleOfTurns(final Grid<Character> grid, final int numberOfCycles) {
        final Cycle<Grid<Character>> cycle = CycleFinder.findCycle(grid, Day14::performFullRotation, numberOfCycles);

        if (!cycle.doesCycleExist()) {
            throw new IllegalStateException(String.format("Unable to find cycle in %s iterations", numberOfCycles));
        }

        return calculateLoad(cycle.cycleValue());
    }

    private static Grid<Character> performFullRotation(final Grid<Character> startGrid) {
        final Grid<Character> northTurn = sendRocksNorth(startGrid);
        final Grid<Character> westTurn = sendRocksNorth(northTurn.rotate(RotationDirection.CLOCKWISE));
        final Grid<Character> southTurn = sendRocksNorth(westTurn.rotate(RotationDirection.CLOCKWISE));
        final Grid<Character> eastTurn = sendRocksNorth(southTurn.rotate(RotationDirection.CLOCKWISE));
        return eastTurn.rotate(RotationDirection.CLOCKWISE);
    }

    private static long calculateLoad(final Grid<Character> grid) {
        int multiplier = 1;

        final int numberOfRowsExceptLast = grid.numberOfRows() - 1;
        long total = 0;
        for (int i = numberOfRowsExceptLast; i >= 0; i--) {
            final Character[] row = grid.rowAt(i);
            int rocks = 0;

            for (final char c : row) {
                if (c == ROCK_SYMBOL) {
                    rocks++;
                }
            }
            total += (long) rocks * multiplier;
            multiplier++;
        }

        return total;
    }

    private static Grid<Character> sendRocksNorth(final Grid<Character> grid) {
        final Character[][] internalGrid = grid.getInternalGrid();
        final Character[][] newGrid = new Character[internalGrid.length][internalGrid[0].length];
        System.arraycopy(internalGrid[0], 0, newGrid[0], 0, internalGrid[0].length);

        for (int row = 1; row < internalGrid.length; row++) {
            for (int column = 0; column < internalGrid[0].length; column++) {
                final char current = internalGrid[row][column];

                if (current == ROCK_SYMBOL) {
                    final int newRow = findFirstEmptyRowGoingNorth(newGrid, row, column);
                    newGrid[row][column] = EMPTY_SYMBOL;
                    newGrid[newRow][column] = ROCK_SYMBOL;
                } else {
                    newGrid[row][column] = current;
                }
            }
        }

        return new Grid<>(newGrid);
    }

    private static int findFirstEmptyRowGoingNorth(final Character[][] newGrid, final int startRow, final int column) {
        int newRow = startRow;
        for (int i = startRow - 1; i >= 0; i--) {
            if (newGrid[i][column] != EMPTY_SYMBOL) {
                break;
            }
            newRow--;
        }

        return newRow;
    }
}
