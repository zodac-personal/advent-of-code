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
import java.util.Set;
import java.util.function.BiPredicate;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.GridFactory;
import net.zodac.advent.grid.Point;
import net.zodac.advent.search.PathFinder;

/**
 * Solution for 2024, Day 18.
 *
 * @see <a href="https://adventofcode.com/2024/day/18">[2024: 18] RAM Run</a>
 */
public final class Day18 {

    private static final char OBSTACLE_SYMBOL = '#';

    private Day18() {

    }

    /**
     * Creates a {@link Grid} populated with obstacles defined by the input {@link Point}s. While many {@link Point}s are provided, only
     * {@code pointsToCheck} {@link Point}s should be considered.
     *
     * <p>
     * Once the obstacles have been populated, find the shortest path between the top-left {@link Point} to the bottom-right {@link Point}. Return
     * the number of steps required (size of path - <b>1</b>).
     *
     * @param obstaclePoints the input {@link Point}s defining the obstacles on the {@link Grid}
     * @param gridSize       the size of the {@link Grid}
     * @param pointsToCheck  the number of obstacle {@link Point}s to consider
     * @return the smallest number of steps between the start and end {@link Point}
     */
    public static long smallestStepsBetweenStartAndEnd(final List<Point> obstaclePoints, final int gridSize, final int pointsToCheck) {
        final Point startPoint = Point.atOrigin();
        final Point endPoint = Point.of(gridSize - 1, gridSize - 1);
        final Grid<Character> grid = createPopulatedGrid(obstaclePoints, gridSize, pointsToCheck);

        // Ignore first point, since we're looking for number of steps
        final BiPredicate<Point, Point> filter = (_, nextPoint) -> grid.at(nextPoint) != OBSTACLE_SYMBOL;
        return PathFinder.shortest(grid, startPoint, endPoint, AdjacentDirection.CARDINAL, filter).size() - 1L;
    }

    /**
     * Having previously found the shortest path from start {@link Point} to end {@link Point}, we continue adding obstacles from the input
     * {@link Point}s supplied. We can start from a later index, defined by {@code previouslyCheckedPoints} to speed up our analysis.
     *
     * <p>
     * We keep adding new {@link Point}s until we find one that prevents any valid path between the start {@link Point} and the end {@link Point}. We
     * then return this {@link Point} as a {@link String} in the format:
     * <pre>
     *     x,y
     * </pre>
     *
     * @param obstaclePoints          the input {@link Point}s defining the obstacles on the {@link Grid}
     * @param gridSize                the size of the {@link Grid}
     * @param previouslyCheckedPoints the starting value to continue checking subsequent {@link Point}s
     * @return the {@link Point} that blocks the start {@link Point} from the end {@link Point}, as a {@link String}
     */
    public static String findPointWhichBlocksPath(final List<Point> obstaclePoints, final int gridSize, final int previouslyCheckedPoints) {
        final Point startPoint = Point.atOrigin();
        final Point endPoint = Point.of(gridSize - 1, gridSize - 1);
        Grid<Character> grid = createPopulatedGrid(obstaclePoints, gridSize, previouslyCheckedPoints);

        final int numberOfObstaclePoints = obstaclePoints.size();
        for (int i = previouslyCheckedPoints; i <= numberOfObstaclePoints; i++) {
            final Point point = obstaclePoints.get(i);

            grid = grid.updateAt(point, OBSTACLE_SYMBOL);
            final Grid<Character> updatedGrid = grid; // Stupid workaround for lambda function
            final BiPredicate<Point, Point> filter = (_, nextPoint) -> updatedGrid.at(nextPoint) != OBSTACLE_SYMBOL;

            final Set<Point> shortestPath = PathFinder.shortest(updatedGrid, startPoint, endPoint, AdjacentDirection.CARDINAL, filter);
            if (shortestPath.isEmpty()) {
                return String.format("%s,%s", point.x(), point.y());
            }
        }
        throw new IllegalStateException("Unable to find any Point to block the path");
    }

    private static Grid<Character> createPopulatedGrid(final List<Point> values, final int gridSize, final int bytesToCheck) {
        Grid<Character> grid = GridFactory.ofCharactersWithSize(gridSize);

        for (int i = 0; i < bytesToCheck; i++) {
            final Point point = values.get(i);
            grid = grid.updateAt(point, OBSTACLE_SYMBOL);
        }
        return grid;
    }
}
