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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.search.PathFinder;

/**
 * Solution for 2024, Day 20.
 *
 * @see <a href="https://adventofcode.com/2024/day/20">[2024: 20] Race Condition</a>
 */
public final class Day20 {

    private static final char START_SYMBOL = 'S';
    private static final char END_SYMBOL = 'E';
    private static final char OBSTACLE_SYMBOL = '#';

    private Day20() {

    }

    /**
     * Given a {@link Character} {@link Grid} with a known start {@link Point} and end {@link Point}, find the shortest path between them avoiding any
     * {@value #OBSTACLE_SYMBOL}s. We can assume there is only <b>one</b> valid path between these two {@link Point}s.
     *
     * <p>
     * For {@code maximumDistanceBetweenPoints}, we are permitted to 'cheat' and to ignore the {@value #OBSTACLE_SYMBOL}s and simply pass through them
     * and traverse towards {@value #END_SYMBOL}. A cheat is considered as any pair of two {@link Point}, regardless of what path they take, as long
     * as the first and last {@link Point}s of the cheat are the same. We need to find all cheats that can save at least {@code minimumSavings} steps
     * from the shortest path found earlier.
     *
     * <p>
     * We can determine this by doing the following. For each {@link Point} on the known shortest path:
     * <ol>
     *     <li>Find all {@link Point}s remaining on the path with a Manhattan distance less that {@code maximumDistanceBetweenPoints}</li>
     *     <li>
     *         For that pair of first and second {@link Point}s, calculate the potential savings by performing a cheat.
     *         The saving is calculated as the distance between the two {@link Point}s (distance using the cheat), <b>minus</b> the distance between
     *         the {@link Point}s along the shortest path (can be retrieved by the index of the {@link Point}s within an ordered {@link List} of the
     *         shortest path).
     *     </li>
     *     <li>If the saving is greater than {@code minimumSavings}, it is a valid cheat</li>
     * </ol>
     *
     * @param characterGrid                the input {@link Character} {@link Grid}
     * @param maximumDistanceBetweenPoints the distance between {@link Point}s to consider for the cheat
     * @param minimumSavings               the minimum saving required for a cheat to be valid
     * @return the number of cheats that save at least {@code minimumSavings}
     * @see PathFinder#shortest(Grid, Point, Point, AdjacentDirection, BiPredicate)
     * @see Point#distanceTo(Point)
     */
    public static long findNumberOfCheats(final Grid<Character> characterGrid, final int maximumDistanceBetweenPoints, final int minimumSavings) {
        final Point start = characterGrid.findValue(c -> c == START_SYMBOL).toList().getFirst();
        final Point end = characterGrid.findValue(c -> c == END_SYMBOL).toList().getFirst();
        final List<Point> shortestPath = findShortestPath(characterGrid, start, end);

        long total = 0;
        for (final Point point : shortestPath) {
            final int index = shortestPath.indexOf(point);

            total += shortestPath.subList(index, shortestPath.size()) // Only look in forward direction
                .parallelStream()
                .filter(otherPoint -> point.distanceTo(otherPoint) <= maximumDistanceBetweenPoints)
                .map(potentialCheat -> calculateSaving(shortestPath, point, potentialCheat, index))
                .filter(saving -> saving >= minimumSavings)
                .count();
        }

        return total;
    }

    private static List<Point> findShortestPath(final Grid<Character> characterGrid, final Point start, final Point end) {
        return new ArrayList<>(PathFinder.shortest(characterGrid, start, end, AdjacentDirection.CARDINAL,
            (_, nextPoint) -> characterGrid.at(nextPoint) != OBSTACLE_SYMBOL));
    }

    private static long calculateSaving(final List<Point> path, final Point first, final Point second, final int firstIndex) {
        final long distanceWithCheat = first.distanceTo(second);
        final long distanceWithoutCheat = Math.abs(firstIndex - path.indexOf(second));

        return Math.abs(distanceWithoutCheat - distanceWithCheat);
    }
}
