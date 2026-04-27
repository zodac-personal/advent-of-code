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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SequencedSet;
import java.util.Set;
import java.util.function.BiPredicate;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.AdjacentPointsSelector;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;

/**
 * Utility class used to perform different types of path-finding algorithms on {@link Point}s within a {@link Grid}.
 */
public final class PathFinder {

    private PathFinder() {

    }

    /**
     * Finds the shortest possible path between the start {@link Point} and the end {@link Point} using Breadth-First-Search. Retrieves all neighbour
     * {@link Point}s based off the {@link AdjacentDirection}. The potential neighbours are filtered by the {@link BiPredicate} for the current and
     * next {@link Point} on the {@link Grid}.
     *
     * @param grid                the {@link Grid} in which to search
     * @param startPoint          the start {@link Point}
     * @param endPoint            the end {@link Point}
     * @param adjacentDirection   the {@link AdjacentDirection} to select neighbour {@link Point}s
     * @param adjacentPointFilter the {@link BiPredicate} to filter potential neighbour {@link Point}s
     * @param <E>                 the type of the {@link Grid}
     * @return the shortest paths from the start {@link Point} to the end {@link Point} as a {@link SequencedSet}
     * @see AdjacentPointsSelector
     */
    public static <E> SequencedSet<Point> shortest(final Grid<E> grid, final Point startPoint, final Point endPoint,
                                                   final AdjacentDirection adjacentDirection,
                                                   final BiPredicate<? super Point, ? super Point> adjacentPointFilter) {
        final Map<Point, Point> previousPointsInPath = new HashMap<>();
        final Queue<Point> queue = new LinkedList<>();
        queue.add(startPoint);

        // Set to track visited points
        final Collection<Point> visited = new HashSet<>();
        visited.add(startPoint);

        while (!queue.isEmpty()) {
            final Point currentPoint = queue.poll();

            // If we reach the destination, reconstruct and return the path
            if (currentPoint.equals(endPoint)) {
                return rebuildPath(previousPointsInPath, endPoint);
            }

            final List<Point> validNeighbours = currentPoint
                .getAdjacentPoints(AdjacentPointsSelector.bounded(false, adjacentDirection, grid.size()))
                .filter(nextPoint -> adjacentPointFilter.test(currentPoint, nextPoint) && !visited.contains(nextPoint))
                .toList();

            for (final Point validNeighbour : validNeighbours) {
                queue.add(validNeighbour);
                visited.add(validNeighbour);
                previousPointsInPath.put(validNeighbour, currentPoint);
            }
        }

        // If no path is found, return an empty list
        return new LinkedHashSet<>();
    }

    private static SequencedSet<Point> rebuildPath(final Map<Point, Point> previousPointsInPath, final Point latestPointInPath) {
        final SequencedSet<Point> path = new LinkedHashSet<>();
        Point current = latestPointInPath;

        while (current != null) {
            path.add(current);
            current = previousPointsInPath.get(current);
        }

        return path.reversed();
    }

    /**
     * Finds all possible paths between the start {@link Point} and the end {@link Point}. Retrieves all neighbour {@link Point}s based off the
     * {@link AdjacentDirection}. The potential neighbours are filtered by the {@link BiPredicate} for the current and next {@link Point} on the
     * {@link Grid}.
     *
     * @param grid                the {@link Grid} in which to search
     * @param startPoint          the start {@link Point}
     * @param endPoint            the end {@link Point}
     * @param adjacentDirection   the {@link AdjacentDirection} to select neighbour {@link Point}s
     * @param adjacentPointFilter the {@link BiPredicate} to filter potential neighbour {@link Point}s
     * @param <E>                 the type of the {@link Grid}
     * @return all valid paths from the start {@link Point} to the end {@link Point}
     * @see AdjacentPointsSelector
     */
    public static <E> List<List<Point>> all(final Grid<E> grid, final Point startPoint, final Point endPoint,
                                            final AdjacentDirection adjacentDirection,
                                            final BiPredicate<? super Point, ? super Point> adjacentPointFilter) {
        final List<List<Point>> paths = new ArrayList<>();
        final List<Point> currentPath = new ArrayList<>();
        final Set<Point> visited = new HashSet<>();

        dfs(grid, startPoint, endPoint, currentPath, paths, visited, adjacentDirection, adjacentPointFilter);
        return paths;
    }

    private static <E> void dfs(final Grid<E> grid, final Point current,
                                final Point end,
                                final List<Point> currentPath,
                                final List<? super List<Point>> paths,
                                final Set<? super Point> visited,
                                final AdjacentDirection adjacentDirection,
                                final BiPredicate<? super Point, ? super Point> adjacentPointFilter
    ) {
        currentPath.add(current);
        visited.add(current);

        // If the end point is reached, add the current path to the routes
        if (current.equals(end)) {
            paths.add(new ArrayList<>(currentPath));
        } else {
            current.getAdjacentPoints(AdjacentPointsSelector.bounded(false, adjacentDirection, grid.size()))
                .filter(nextPoint -> adjacentPointFilter.test(current, nextPoint) && !visited.contains(nextPoint))
                .forEach(neighbour -> dfs(grid, neighbour, end, currentPath, paths, visited, adjacentDirection, adjacentPointFilter));
        }

        // Remove current point from the path and unmark as visited
        currentPath.removeLast();
        visited.remove(current);
    }
}
