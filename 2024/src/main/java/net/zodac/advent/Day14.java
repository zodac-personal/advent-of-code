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
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * Solution for 2024, Day 14.
 *
 * @see <a href="https://adventofcode.com/2024/day/14">[2024: 14] Restroom Redoubt</a>
 */
public final class Day14 {

    private static final int SECONDS_TO_MOVE = 100;
    private static final int MAXIMUM_SECONDS_TO_FIND_CHRISTMAS_TREE = 10_000;

    private Day14() {

    }

    /**
     * Given a {@link Collection} of {@link Point}s and their velocity in the format:
     * <pre>
     *     p=x,y v=deltaX,deltaY
     * </pre>
     *
     * <p>
     * We can move each {@link Point} around the supplied {@link Integer} {@link Grid}. If the {@link Point} is going out of bounds, it should
     * {@link Point#wrapAround(int, int, int, int, int)} the {@link Grid} to the other side. The {@link Point} should be moved
     * {@value #SECONDS_TO_MOVE} seconds.
     *
     * <p>
     * When complete, the {@link Grid} should be split into quadrants. For each quadrant, count the number of {@link Point}s. If multiple
     * {@link Point}s exist in the same location, they should each be counted. The four quadrant values should then be multiplied together to find the
     * 'safety rating' of the {@link Grid}.
     *
     * @param values      the input {@link Point}s and their velocities
     * @param integerGrid the {@link Grid} that the {@link Point}s should move around
     * @return the safety rating of the {@link Grid} after {@value #SECONDS_TO_MOVE} seconds
     */
    public static long calculateSafetyRating(final Collection<String> values, final Grid<Integer> integerGrid) {
        final Grid<Integer> grid = movePointsAroundGrid(values, integerGrid, SECONDS_TO_MOVE);
        return getSafetyRatingOfGrid(grid);
    }

    /**
     * Given a {@link Collection} of {@link Point}s and their velocity in the format:
     * <pre>
     *     p=x,y v=deltaX,deltaY
     * </pre>
     *
     * <p>
     * We can move each {@link Point} around the supplied {@link Integer} {@link Grid}. If the {@link Point} is going out of bounds, it should
     * {@link Point#wrapAround(int, int, int, int, int)} the {@link Grid} to the other side.
     *
     * <p>
     * It is possible to find a Christmas Tree in the output of one of the grids after an unknown number of seconds. We iterate through the
     * {@link Grid}s after every second (up to a maximum of {@value #MAXIMUM_SECONDS_TO_FIND_CHRISTMAS_TREE}).
     *
     * <p>
     * We determine that a Christmas Tree exists for a {@link Grid} where the 'safety rating' is lowest. This is assumed since a Christmas Tree would
     * require most points in a single quadrant (or two), resulting in a lower rating. We iterate through all possible options then return the second
     * that returns the lowest rating.
     *
     * @param values      the input {@link Point}s and their velocities
     * @param integerGrid the {@link Grid} that the {@link Point}s should move around
     * @return the number of seconds to move until a Christmas Tree appears
     */
    public static long findSecondsUntilChrismasTree(final Collection<String> values, final Grid<Integer> integerGrid) {
        return IntStream.rangeClosed(1, MAXIMUM_SECONDS_TO_FIND_CHRISTMAS_TREE)
            .parallel()
            .mapToObj(i -> {
                final Grid<Integer> grid = movePointsAroundGrid(values, integerGrid, i);
                final long safetyRatingForIteration = getSafetyRatingOfGrid(grid);
                return Pair.of(i, safetyRatingForIteration);
            })
            .min(Comparator.comparingLong(Pair::second))
            .map(Pair::first)
            .orElseThrow(() -> new IllegalStateException("No minimum iteration found"));
    }

    private static Grid<Integer> movePointsAroundGrid(final Collection<String> values, final Grid<Integer> integerGrid, final int numberOfMovements) {
        final Collection<Point> endPoints = values
            .parallelStream()
            .map(Point::ofMany)
            .map(pair -> moveToEndPoint(pair.getFirst().flip(), pair.getLast().flip(), integerGrid, numberOfMovements))
            .toList();

        return createUpdatedGrid(integerGrid, endPoints);
    }

    private static long calculateValueOfQuadrant(final Grid<Integer> quadrant) {
        long total = 0L;

        final Integer[][] internalGrid = quadrant.getInternalGrid();
        for (final Integer[] rows : internalGrid) {
            for (final Integer value : rows) {
                total += value;
            }
        }

        return total;
    }

    private static long getSafetyRatingOfGrid(final Grid<Integer> grid) {
        return splitIntoQuadrants(grid)
            .stream()
            .mapToLong(Day14::calculateValueOfQuadrant)
            .reduce(1L, (first, second) -> first * second);
    }

    private static Point moveToEndPoint(final Point point, final Point velocity, final Grid<Integer> integerGrid, final int numMoves) {
        return point.wrapAround(velocity.x(), velocity.y(), numMoves, integerGrid.numberOfRows(), integerGrid.numberOfColumns());
    }

    private static Grid<Integer> createUpdatedGrid(final Grid<Integer> integerGrid, final Collection<Point> endPoints) {
        final Integer[][] internalGrid = integerGrid.getInternalGrid();
        for (final Point endPoint : endPoints) {
            internalGrid[endPoint.x()][endPoint.y()] = internalGrid[endPoint.x()][endPoint.y()] + 1;
        }

        return new Grid<>(internalGrid);
    }

    private static List<Grid<Integer>> splitIntoQuadrants(final Grid<Integer> grid) {
        final Integer[][] internalGrid = grid.getInternalGrid();
        final int midRow = internalGrid.length / 2;
        final int midCol = internalGrid[0].length / 2;

        final Integer[][] topLeft = new Integer[midRow][midCol];
        final Integer[][] topRight = new Integer[midRow][midCol];
        final Integer[][] bottomLeft = new Integer[midRow][midCol];
        final Integer[][] bottomRight = new Integer[midRow][midCol];

        // Populate quadrants
        for (int i = 0; i < midRow; i++) {
            for (int j = 0; j < midCol; j++) {
                topLeft[i][j] = internalGrid[i][j];
                topRight[i][j] = internalGrid[i][j + midCol + 1];
                bottomLeft[i][j] = internalGrid[i + midRow + 1][j];
                bottomRight[i][j] = internalGrid[i + midRow + 1][j + midCol + 1];
            }
        }

        return List.of(
            new Grid<>(topLeft),
            new Grid<>(topRight),
            new Grid<>(bottomLeft),
            new Grid<>(bottomRight)
        );
    }
}
