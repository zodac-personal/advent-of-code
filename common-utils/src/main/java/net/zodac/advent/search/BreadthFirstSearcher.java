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
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.zodac.advent.grid.Point;

/**
 * An implementation of Breadth-First Search to find the shortest distance between two nodes, where all edges between nodes have the same cost/weight.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Breadth-first_search">Breadth-First Search</a>
 */
// TODO: Would be nice to combine this with Dijkstra and even A* for a flexible all-in-one utility class...
public final class BreadthFirstSearcher {

    private static final long START_NODE_DISTANCE = 0L;

    private BreadthFirstSearcher() {

    }

    /**
     * Finds the shortest weighted distance from the provided {@link Point} to one of the end {@link Point}s.
     *
     * @param startPoint             the starting {@link Point} to begin our search
     * @param endPoints              the possible end {@link Point}s to finish our search
     * @param valuesByPoint          the values of all available {@link Point}s
     * @param pointNeighbourFunction a {@link Function} defining how to retrieve the neighbours of the current {@link Point}
     * @return the shortest distance from the provided {@link Point} to any end {@link Point}, or {@link Long#MAX_VALUE} if none can be found
     */
    public static long findShortestDistance(
        final Point startPoint,
        final Collection<Point> endPoints,
        final Map<Point, Integer> valuesByPoint,
        final Function<? super Point, ? extends Set<Point>> pointNeighbourFunction // TODO: Keep using super/extends, or remove to simplify?
    ) {
        final Map<Point, Long> distanceByNeighbourNode = new HashMap<>();
        distanceByNeighbourNode.put(startPoint, START_NODE_DISTANCE);

        final Deque<Point> neighbourNodes = new LinkedList<>();
        neighbourNodes.add(startPoint);

        final Map<Point, Point> neighbourNodeByParentNode = new HashMap<>();

        while (!neighbourNodes.isEmpty()) {
            Point currentPoint = neighbourNodes.poll();

            if (endPoints.contains(currentPoint)) {
                final Collection<Point> path = new ArrayList<>();
                while (neighbourNodeByParentNode.containsKey(currentPoint)) {
                    path.add(currentPoint);
                    currentPoint = neighbourNodeByParentNode.get(currentPoint);
                }
                return path.size();
            }

            for (final Point neighbourPoint : pointNeighbourFunction.apply(currentPoint)) {
                if (isInvalidPoint(valuesByPoint, neighbourPoint, currentPoint)) {
                    continue;
                }

                final long distanceToNeighbour = distanceByNeighbourNode.getOrDefault(currentPoint, Long.MIN_VALUE);

                if (distanceToNeighbour < distanceByNeighbourNode.getOrDefault(neighbourPoint, Long.MAX_VALUE)) {
                    distanceByNeighbourNode.put(neighbourPoint, distanceToNeighbour);
                    neighbourNodeByParentNode.put(neighbourPoint, currentPoint);
                    neighbourNodes.add(neighbourPoint);
                }
            }
        }

        // No need to throw an exception, just return a large distance
        return Long.MAX_VALUE;
    }

    private static boolean isInvalidPoint(final Map<Point, Integer> valuesByPoint, final Point neighbourPoint, final Point currentPoint) {
        return !valuesByPoint.containsKey(neighbourPoint)
            || valuesByPoint.getOrDefault(neighbourPoint, Integer.MIN_VALUE) > valuesByPoint.getOrDefault(currentPoint, Integer.MIN_VALUE) + 1;
    }
}
