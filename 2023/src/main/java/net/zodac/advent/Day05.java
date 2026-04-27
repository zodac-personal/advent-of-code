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
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.zodac.advent.pojo.Interval;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2023, Day 5.
 *
 * @see <a href="https://adventofcode.com/2023/day/5">[2023: 05] If You Give A Seed A Fertilizer</a>
 */
public final class Day05 {

    private static final String MAPPING_IDENTIFIER = "map";

    private Day05() {

    }

    /**
     * Given an {@code input} containing a definition include <b>seed</b> values, and {@link Mapping} values, we must determine the <b>seed</b> value
     * that results in the lowest <b>location</b> value. The first {@link String} in {@code values} will be the space-separated <b>seed</b> numbers,
     * with an identifier.
     *
     * <p>
     * For each <b>seed</b>, we attempt to find the matching <b>soil</b> value. This carries on with the available {@link Mapping}s, until we find the
     * value of {@link Mapping#HUMIDITY_TO_LOCATION}.
     *
     * <p>
     * Each value can be considered an {@link Interval}, which can then be used to find the correct {@link Mapping} {@link Interval}, which we use to
     * determine the <b>location</b> for a <b>seed</b>.
     *
     * @param values the input <b>seed</b>> numbers and {@link Mapping} values
     * @return the lowest <b>location</b> value for the provided <b>seed</b> numbers
     */
    public static long findLowestLocationForSeedNumbers(final List<String> values) {
        final Map<Mapping, List<String>> mappingsByName = loadMappings(values.subList(2, values.size()));
        final List<Long> seedDefinitions = StringUtils.collectNumbersInOrder(values.getFirst());
        final Collection<Interval> seedNumbers = new ArrayList<>();

        for (final Long seedDefinition : seedDefinitions) {
            seedNumbers.add(Interval.singular(seedDefinition));
        }

        return findMinimumLocationForSeed(mappingsByName, seedNumbers);
    }

    /**
     * Given an {@code input} containing a definition include <b>seed</b> values, and {@link Mapping} values, we must determine the <b>seed</b> value
     * that results in the lowest <b>location</b> value. This is similar to {@link #findLowestLocationForSeedNumbers(List)}, but instead of the first
     * {@link String} in {@code values} listing all <b>seed</b> numbers, each pair of numbers is the seed start number, and the size of its
     * {@link Interval}.
     *
     * <p>
     * Each value can be considered an {@link Interval}, which can then be used to find the correct {@link Mapping} {@link Interval}, which we use to
     * determine the <b>location</b> for a <b>seed</b>.
     *
     * <p>
     * For each <b>seed</b>, we attempt to find the matching <b>soil</b> value. This carries on with the available {@link Mapping}s, until we find the
     * value of {@link Mapping#HUMIDITY_TO_LOCATION}.
     *
     * @param values the input <b>seed</b>> numbers and {@link Mapping} values
     * @return the lowest <b>location</b> value for the provided <b>seed</b> {@link Interval}s
     */
    public static long findLowestLocationForSeedNumberIntervals(final List<String> values) {
        final Map<Mapping, List<String>> mappingsByName = loadMappings(values.subList(2, values.size()));
        final List<Long> seedDefinitions = StringUtils.collectNumbersInOrder(values.getFirst());
        final Collection<Interval> seedIntervals = new ArrayList<>();

        final int numberOfSeedDefinitionsWhenIteratingByTwo = seedDefinitions.size() / 2;
        for (int i = 0; i <= numberOfSeedDefinitionsWhenIteratingByTwo; i += 2) {
            seedIntervals.add(Interval.openInterval(seedDefinitions.get(i), seedDefinitions.get(i) + seedDefinitions.get(i + 1)));
        }

        return findMinimumLocationForSeed(mappingsByName, seedIntervals);
    }

    private static Map<Mapping, List<String>> loadMappings(final List<String> values) {
        final Map<Mapping, List<String>> mappingsByName = new EnumMap<>(Mapping.class);
        Mapping currentMapping = Mapping.SEED_TO_SOIL; // Default value, expected to be overridden

        for (final String value : values.subList(2, values.size())) {
            if (value.isBlank()) {
                continue;
            }

            if (value.contains(MAPPING_IDENTIFIER)) {
                currentMapping = Mapping.get(value);
                continue;
            }

            final List<String> currentMappings = mappingsByName.getOrDefault(currentMapping, new ArrayList<>());
            currentMappings.add(value);
            mappingsByName.put(currentMapping, currentMappings);
        }
        return mappingsByName;
    }

    private static long findMinimumLocationForSeed(final Map<Mapping, ? extends List<String>> mappingsByName, final Collection<Interval> intervals) {
        long minimumLocation = Long.MAX_VALUE;

        for (final Interval seedInterval : intervals) {
            final Interval soilInterval = findNextValue(seedInterval, mappingsByName, Mapping.SEED_TO_SOIL);
            final Interval fertilizerInterval = findNextValue(soilInterval, mappingsByName, Mapping.SOIL_TO_FERTILIZER);
            final Interval waterInterval = findNextValue(fertilizerInterval, mappingsByName, Mapping.FERTILIZER_TO_WATER);
            final Interval lightInterval = findNextValue(waterInterval, mappingsByName, Mapping.WATER_TO_LIGHT);
            final Interval temperatureInterval = findNextValue(lightInterval, mappingsByName, Mapping.LIGHT_TO_TEMPERATURE);
            final Interval humidityInterval = findNextValue(temperatureInterval, mappingsByName, Mapping.TEMPERATURE_TO_HUMIDITY);
            final Interval locationInterval = findNextValue(humidityInterval, mappingsByName, Mapping.HUMIDITY_TO_LOCATION);

            if (locationInterval.start() < minimumLocation) {
                minimumLocation = locationInterval.start();
            }
        }

        return minimumLocation;
    }

    private static Interval findNextValue(final Interval interval, final Map<Mapping, ? extends List<String>> mappingsByName, final Mapping mapping) {
        if (!mappingsByName.containsKey(mapping)) {
            throw new IllegalStateException("Unable to find mapping for key: " + mapping);
        }

        final List<String> possibleMappings = mappingsByName.get(mapping);
        for (final String possibleMapping : possibleMappings) {
            final List<Long> mappingValues = StringUtils.collectNumbersInOrder(possibleMapping);
            final Interval mappingInterval = new Interval(mappingValues.get(1), mappingValues.get(1) + mappingValues.get(2));
            final Interval intersection = interval.intersect(mappingInterval);

            if (!intersection.isEmpty()) {
                final long offset = mappingValues.get(0) - mappingValues.get(1);
                return Interval.openInterval(intersection.start() + offset, intersection.end() + offset);
            }
        }
        return interval;
    }

    private enum Mapping {

        SEED_TO_SOIL("seed-to-soil"),
        SOIL_TO_FERTILIZER("soil-to-fertilizer"),
        FERTILIZER_TO_WATER("fertilizer-to-water"),
        WATER_TO_LIGHT("water-to-light"),
        LIGHT_TO_TEMPERATURE("light-to-temperature"),
        TEMPERATURE_TO_HUMIDITY("temperature-to-humidity"),
        HUMIDITY_TO_LOCATION("humidity-to-location");

        private final String mappingName;

        Mapping(final String mappingName) {
            this.mappingName = mappingName;
        }

        private static Mapping get(final String input) {
            return Arrays.stream(values())
                .filter(mapping -> input.contains(mapping.mappingName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Unable to find %s for '%s'", Mapping.class.getSimpleName(), input)));
        }
    }
}

