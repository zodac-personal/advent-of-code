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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.AdjacentPointsSelector;
import net.zodac.advent.grid.Point;
import net.zodac.advent.search.BreadthFirstSearcher;

/**
 * Solution for 2022, Day 12.
 *
 * @see <a href="https://adventofcode.com/2022/day/12">AoC 2022, Day 12</a>
 */
public final class Day12 {

    // Add a meaningless first character, so we can use a String#indexOf() call without any offset
    private static final String VALUES = "*abcdefghijklmnopqrstuvwxyz";
    private static final int DEFAULT_HEIGHT = 0;
    private static final char START_CHARACTER = 'S';
    private static final char END_CHARACTER = 'E';

    private Day12() {

    }

    /**
     * Given a list of {@link String}s representing a 2D array of chars, we find the shortest path from any of the {@code startChar}s or
     * {@link #START_CHARACTER} to {@link #END_CHARACTER}. We assume that while any {@code startChar} may be provided, {@value #START_CHARACTER} is
     * also always considered a valid start point for the path.
     *
     * <p>
     * Each step in the path will have a weight of <b>1</b>, but if the char value is more than 1 value greater than the current location, it is not
     * considered a valid option. A value less than 1 is acceptable (you may descend as much as possible, but only ascend 1 height difference per
     * step).
     *
     * @param values    the input {@link String}s representing a 2D array of chars
     * @param startChar the wanted starting characters (in addition to {@link #START_CHARACTER})
     * @return the shortest path from any start point to the end point
     * @see BreadthFirstSearcher
     */
    public static long findShortestPathFromPossibleStartsToEnd(final List<String> values, final char startChar) {
        final Map<Point, Integer> heightsByPoint = new HashMap<>();

        final Collection<Point> startPoints = new HashSet<>();
        final Collection<Point> endPoints = new HashSet<>();
        final int numberOfValues = values.size();

        for (int i = 0; i < numberOfValues; i++) {
            final String value = values.get(i);
            final int length = value.length();

            for (int j = 0; j < length; j++) {
                final char currentChar = value.charAt(j);
                final Point currentPoint = Point.of(i, j);

                // Always add possible start characters to the starting points list, then perform next checks
                if (currentChar == startChar) {
                    startPoints.add(currentPoint);
                }

                switch (currentChar) {
                    case START_CHARACTER -> {
                        startPoints.add(currentPoint);
                        heightsByPoint.put(currentPoint, DEFAULT_HEIGHT);
                    }
                    case END_CHARACTER -> {
                        endPoints.add(currentPoint);
                        heightsByPoint.put(currentPoint, VALUES.length());
                    }
                    default -> heightsByPoint.put(currentPoint, VALUES.indexOf(currentChar));
                }
            }
        }

        // Calculate BFS distance for each possible starting point, and return the smallest
        long minDistance = Long.MAX_VALUE;
        for (final Point startingPoint : startPoints) {
            final long shortestDistance = BreadthFirstSearcher.findShortestDistance(startingPoint, endPoints, heightsByPoint,
                point -> point.getAdjacentPoints(AdjacentPointsSelector.unbounded(false, AdjacentDirection.CARDINAL))
                    .collect(Collectors.toSet()));
            minDistance = Math.min(minDistance, shortestDistance);
        }
        return minDistance;
    }
}
