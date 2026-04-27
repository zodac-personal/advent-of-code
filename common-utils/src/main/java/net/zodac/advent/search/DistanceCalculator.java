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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.zodac.advent.pojo.Route;
import net.zodac.advent.util.PermutationUtils;

/**
 * Calculator that can calculate the shortest or longest path across a {@link List} of {@link Route}s, while visiting all locations once.
 */
public final class DistanceCalculator {

    private final Map<String, Integer> distancesBySourceAndDestination;
    private final List<? extends List<String>> permutations;
    private final Set<Option> options;

    private DistanceCalculator(final Map<String, Integer> distancesBySourceAndDestination,
                               final List<? extends List<String>> permutations,
                               final Option... options) {
        this.distancesBySourceAndDestination = Collections.unmodifiableMap(distancesBySourceAndDestination);
        this.permutations = Collections.unmodifiableList(permutations);
        this.options = options.length > 0 ? EnumSet.copyOf(Arrays.asList(options)) : EnumSet.noneOf(Option.class);
    }

    /**
     * Defines the various options for {@link DistanceCalculator} when calculating distances for {@link Route}s.
     */
    public enum Option {

        /**
         * When calculating the distances for a {@link Route}, we consider the value of both a -> b and b -> a.
         */
        CALCULATE_BOTH_DIRECTIONS,

        /**
         * When generating permutations, the last entry must also loop back to the first entry.
         */
        LOOPS_TO_START,

        /**
         * The distance from (a -> b) != (b -> a), so we cannot simply find the reverse direction when calculating distance.
         */
        UNI_DIRECTIONAL_DISTANCES
    }

    /**
     * Creates an instance of {@link DistanceCalculator}. We use the provided {@link Route}s to create a map of all distances by location, and also
     * all available permutations of the locations.
     *
     * <p>
     * Sets all available {@link DistanceCalculator.Option}s.
     *
     * @param routes the {@link Route}s
     * @return the created {@link DistanceCalculator}
     * @see PermutationUtils#generateAll(List)
     * @see #createWithOptions(Collection, Option...)
     */
    public static DistanceCalculator createWithAllOptions(final Collection<Route> routes) {
        return createWithOptions(routes, Option.values());
    }

    /**
     * Creates an instance of {@link DistanceCalculator}. We use the provided {@link Route}s to create a map of all distances by location, and also
     * all available permutations of the locations.
     *
     * <p>
     * Does not set any {@link DistanceCalculator.Option}s.
     *
     * @param routes the {@link Route}s
     * @return the created {@link DistanceCalculator}
     * @see PermutationUtils#generateAll(List)
     * @see #createWithOptions(Collection, Option...)
     */
    public static DistanceCalculator create(final Collection<Route> routes) {
        return createWithOptions(routes);
    }

    /**
     * Creates an instance of {@link DistanceCalculator}. We use the provided {@link Route}s to create a map of all distances by location, and also
     * all available permutations of the locations.
     *
     * @param routes  the {@link Route}s
     * @param options the {@link DistanceCalculator.Option}s to be enabled
     * @return the created {@link DistanceCalculator}
     * @see PermutationUtils#generateAll(List)
     */
    public static DistanceCalculator createWithOptions(final Collection<Route> routes, final Option... options) {
        final Map<String, Integer> distancesBySourceAndDestination = HashMap.newHashMap(routes.size());

        final Set<String> sources = new HashSet<>();
        for (final Route route : routes) {
            sources.add(route.from());
            sources.add(route.to());

            final String key = route.from() + route.to();
            distancesBySourceAndDestination.put(key, route.value());
        }

        final List<List<String>> permutations = PermutationUtils.generateAll(new ArrayList<>(sources))
            .stream()
            .toList();

        return new DistanceCalculator(distancesBySourceAndDestination, permutations, options);
    }

    /**
     * Returns the distance of the shortest path that visits all locations in the provided {@link Route}s.
     *
     * @return the distance of the shortest path
     */
    public int distanceOfShortestPath() {
        int minimumDistance = calculateDistance(permutations.getFirst());

        // First entry is used as 'baseline' distance, compare remaining entries in the subList to this value
        for (final List<String> permutation : permutations.subList(1, permutations.size())) {
            final int distanceOfPath = calculateDistance(permutation);
            minimumDistance = Math.min(minimumDistance, distanceOfPath);
        }

        return minimumDistance;
    }

    /**
     * Returns the distance of the longest path that visits all locations in the provided {@link Route}s.
     *
     * @return the distance of the longest path
     */
    public int distanceOfLongestPath() {
        int maximumDistance = calculateDistance(permutations.getFirst());

        // First entry is used as 'baseline' distance, compare remaining entries in the subList to this value
        for (final List<String> permutation : permutations.subList(1, permutations.size())) {
            final List<String> newPermutation = new ArrayList<>(permutation);

            if (isEnabled(Option.LOOPS_TO_START)) {
                newPermutation.add(permutation.getFirst());
            }

            final int distanceOfPath = calculateDistance(newPermutation);
            maximumDistance = Math.max(maximumDistance, distanceOfPath);
        }

        return maximumDistance;
    }

    private int calculateDistance(final List<String> permutation) {
        final int permutationLengthExceptLast = permutation.size() - 1;
        int totalDistance = 0;
        for (int i = 0; i < permutationLengthExceptLast; i++) {
            final String from = permutation.get(i);
            final String to = permutation.get(i + 1);
            totalDistance += getValue(from, to);

            if (isEnabled(Option.CALCULATE_BOTH_DIRECTIONS)) {
                totalDistance += getValue(to, from);
            }
        }

        return totalDistance;
    }

    private int getValue(final String from, final String to) {
        final String key = from + to;
        if (distancesBySourceAndDestination.containsKey(key)) {
            return distancesBySourceAndDestination.get(key);
        }

        if (isEnabled(Option.UNI_DIRECTIONAL_DISTANCES)) {
            throw new IllegalArgumentException(
                String.format("Unable to find a value for %s in saved values %s", key, distancesBySourceAndDestination));
        }

        final String reverseKey = to + from;
        if (distancesBySourceAndDestination.containsKey(reverseKey)) {
            return distancesBySourceAndDestination.get(reverseKey);
        }

        throw new IllegalArgumentException(
            String.format("Unable to find a value for %s or %s in saved values %s", key, reverseKey, distancesBySourceAndDestination));
    }

    private boolean isEnabled(final Option option) {
        return options.contains(option);
    }
}
