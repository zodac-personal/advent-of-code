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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.AdjacentPointsSelector;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.tuple.Pair;
import net.zodac.advent.search.PathFinder;

/**
 * Solution for 2024, Day 10.
 *
 * @see <a href="https://adventofcode.com/2024/day/10">[2024: 10] Hoof It</a>
 */
public final class Day10 {

    private static final int START_POINT_VALUE = 0;
    private static final int END_POINT_VALUE = 9;

    private Day10() {

    }

    /**
     * Given an {@link Integer} {@link Grid}, where a {@value #START_POINT_VALUE} value is the start of a trail and a {@value #END_POINT_VALUE} value
     * is the end of a trail, calculate the 'value' of each start {@link Point}. This is done by traversing from each possible start {@link Point} and
     * attempting to find a route to the end {@link Point}. Only cardinal directions (UP, DOWN, LEFT, RIGHT) may be used, and a {@link Point} is only
     * valid if the value is exactly <b>1</b> greater than the current value.
     *
     * <p>
     * Also, a rating for each path can be calculated, which is the number of unique paths between a valid start/end {@link Point} route. Otherwise,
     * the value is <b>1</b> per unique start/end {@link Point} route.
     *
     * @param integerGrid     the input {@link Integer} {@link Grid}
     * @param calculateRating whether to calculate the rating for each route
     * @return the value of valid paths
     */
    public static long calculateValueOfValidPaths(final Grid<Integer> integerGrid, final boolean calculateRating) {
        final Collection<Point> startPoints = integerGrid
            .findValue(integer -> integer == START_POINT_VALUE)
            .toList();

        long total = 0L;
        for (final Point startPoint : startPoints) {
            Set<Point> nextPoints = getNextPoints(integerGrid, startPoint);

            while (!nextPoints.isEmpty()) {
                final Pair<Long, Set<Point>> valueAndNextPoints = calculateValueAndNextPoints(integerGrid, calculateRating, startPoint, nextPoints);
                total += valueAndNextPoints.first();
                nextPoints = valueAndNextPoints.second();
            }
        }

        return total;
    }

    private static Pair<Long, Set<Point>> calculateValueAndNextPoints(final Grid<Integer> integerGrid,
                                                                      final boolean calculateRating,
                                                                      final Point startPoint,
                                                                      final Collection<Point> nextPoints
    ) {
        long value = 0L;
        final Set<Point> pointsToCheck = new HashSet<>();

        for (final Point nextPoint : nextPoints) {
            if (integerGrid.at(nextPoint) == END_POINT_VALUE) {
                if (calculateRating) {
                    final List<List<Point>> allPaths = PathFinder.all(integerGrid, startPoint, nextPoint, AdjacentDirection.CARDINAL,
                        (currentPoint, potentialNextPoint) -> integerGrid.at(currentPoint) + 1 == integerGrid.at(potentialNextPoint)
                    );
                    value += allPaths.size();
                } else {
                    value++;
                }
            } else {
                pointsToCheck.addAll(getNextPoints(integerGrid, nextPoint));
            }
        }
        return Pair.of(value, pointsToCheck);
    }

    private static Set<Point> getNextPoints(final Grid<Integer> integerGrid, final Point startPoint) {
        final int currentValue = integerGrid.at(startPoint);
        final AdjacentPointsSelector adjacentPointsSelector = AdjacentPointsSelector.bounded(false, AdjacentDirection.CARDINAL, integerGrid.size());

        return startPoint
            .getAdjacentPoints(adjacentPointsSelector)
            .filter(point -> integerGrid.at(point) == currentValue + 1)
            .collect(Collectors.toSet());
    }
}
