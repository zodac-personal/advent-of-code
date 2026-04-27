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

package net.zodac.advent.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.AdjacentPointsSelector;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;

/**
 * Utility class to find all groups in a {@link Grid}. A group is defined as any set of connected {@link Point}s (connected by cardinal direction)
 * which have the same value.
 */
public final class GroupSearcher {

    private GroupSearcher() {

    }

    /**
     * Searches the input {@link Grid} for all grouped {@link Point}s. There can be multiple groups of the same value (but not connected to each
     * other).
     *
     * @param grid the {@link Grid} to search
     * @param <E>  the type of the {@link Grid}
     * @return the groups, keyed by value
     */
    public static <E> Map<E, Set<Set<Point>>> findGroups(final Grid<? extends E> grid) {
        // Map to store the result
        final Map<E, Set<Set<Point>>> groupedPointsByValue = new HashMap<>();
        final Set<Point> visited = new HashSet<>();
        final int numberOfRows = grid.numberOfRows();
        final int numberOfColumns = grid.numberOfColumns();

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                final Point currentPoint = Point.of(i, j);
                if (visited.contains(currentPoint)) {
                    continue;
                }

                // Perform DFS to find all connected points, and update the input collection
                final Set<Point> groupedPoints = new HashSet<>();
                final E currentValue = grid.at(currentPoint);
                dfs(currentPoint, grid, visited, groupedPoints, point -> grid.at(point).equals(currentValue));

                // Add the component to the map
                groupedPointsByValue
                    .computeIfAbsent(currentValue, _ -> new HashSet<>())
                    .add(groupedPoints);
            }
        }

        return groupedPointsByValue;
    }

    private static <E> void dfs(final Point pointToCheck,
                                final Grid<E> grid,
                                final Set<? super Point> visited,
                                final Set<? super Point> currentGroup,
                                final Predicate<? super Point> predicate
    ) {
        // Check if the point has already been checked, out of bounds or already visited or not meeting the predicate
        if (visited.contains(pointToCheck) || !grid.exists(pointToCheck) || !predicate.test(pointToCheck)) {
            return;
        }

        // Add the current point to the group and mark as visited to skip in future iterations
        currentGroup.add(pointToCheck);
        visited.add(pointToCheck);

        final AdjacentPointsSelector adjacentPointsSelector = AdjacentPointsSelector.bounded(false, AdjacentDirection.CARDINAL, grid.size());
        final List<Point> neighbourPoints = pointToCheck.getAdjacentPoints(adjacentPointsSelector).toList();

        // Recursively check all neighbours to update current group
        for (final Point neighbourPoint : neighbourPoints) {
            dfs(neighbourPoint, grid, visited, currentGroup, predicate);
        }
    }
}
