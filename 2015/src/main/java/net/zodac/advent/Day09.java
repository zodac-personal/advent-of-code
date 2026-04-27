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
import net.zodac.advent.pojo.Route;
import net.zodac.advent.search.DistanceCalculator;

/**
 * Solution for 2015, Day 9.
 *
 * @see <a href="https://adventofcode.com/2015/day/9">AoC 2015, Day 9</a>
 */
public final class Day09 {

    private Day09() {

    }

    /**
     * Calculates the shortest path along all locations defined by the provided {@link Route}s.
     *
     * @param routes the {@link Route}s
     * @return the distance of the shortest path between all locations
     * @see DistanceCalculator
     */
    public static long distanceOfShortestPath(final Collection<Route> routes) {
        final DistanceCalculator distanceCalculator = DistanceCalculator.create(routes);
        return distanceCalculator.distanceOfShortestPath();
    }

    /**
     * Calculates the longest path along all locations defined by the provided {@link Route}s.
     *
     * @param routes the {@link Route}s
     * @return the distance of the longest path between all locations
     * @see DistanceCalculator
     */
    public static long distanceOfLongestPath(final Collection<Route> routes) {
        final DistanceCalculator distanceCalculator = DistanceCalculator.create(routes);
        return distanceCalculator.distanceOfLongestPath();
    }
}
