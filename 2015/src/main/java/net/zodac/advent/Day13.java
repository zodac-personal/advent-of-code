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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.zodac.advent.pojo.Route;
import net.zodac.advent.search.DistanceCalculator;

/**
 * Solution for 2015, Day 13.
 *
 * @see <a href="https://adventofcode.com/2015/day/13">AoC 2015, Day 13</a>
 */
public final class Day13 {

    private static final String SELF_SOURCE_NAME = "me";
    private static final int SELF_DISTANCE_VALUE = 0;

    private Day13() {

    }

    /**
     * Calculates the longest path along all locations defined by the provided {@link Route}s.
     *
     * @param routes the {@link Route}s
     * @return the distance of the longest path between all locations
     * @see DistanceCalculator
     */
    public static long greatestChangeInHappiness(final Collection<Route> routes) {
        final DistanceCalculator distanceCalculator = DistanceCalculator.createWithAllOptions(routes);
        return distanceCalculator.distanceOfLongestPath();
    }

    /**
     * Calculates the longest path along all locations defined by the provided {@link Route}s. For all {@link Route}s, we also add ourselves
     * (default user) to each of the sources.
     *
     * @param routes the {@link Route}s
     * @return the distance of the longest path between all locations
     * @see DistanceCalculator
     */
    public static long greatestChangeInHappinessIncludingSelf(final Collection<Route> routes) {
        final Collection<Route> routesIncludingSelf = updateRoutesWithSelf(routes);
        final DistanceCalculator distanceCalculator = DistanceCalculator.createWithAllOptions(routesIncludingSelf);
        return distanceCalculator.distanceOfLongestPath();
    }

    private static Collection<Route> updateRoutesWithSelf(final Collection<Route> routes) {
        final Collection<Route> routesIncludingSelf = new ArrayList<>(routes);
        final Set<String> sources = getAllSources(routes);

        for (final String source : sources) {
            routesIncludingSelf.add(Route.create(SELF_SOURCE_NAME, source, SELF_DISTANCE_VALUE));
            routesIncludingSelf.add(Route.create(source, SELF_SOURCE_NAME, SELF_DISTANCE_VALUE));
        }

        return routesIncludingSelf;
    }

    private static Set<String> getAllSources(final Collection<Route> routes) {
        final Set<String> sources = new HashSet<>();
        for (final Route route : routes) {
            sources.add(route.from());
            sources.add(route.to());
        }
        return sources;
    }
}
