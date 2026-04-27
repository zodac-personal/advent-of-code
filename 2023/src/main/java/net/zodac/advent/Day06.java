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
import java.util.List;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2023, Day 6.
 *
 * @see <a href="https://adventofcode.com/2023/day/6">[2023: 06] Wait For It</a>
 */
public final class Day06 {

    private Day06() {

    }

    /**
     * We are given a {@link List} of {@link String}s, representing a number of {@link Race}s. For each {@link Race} we can 'charge' up our velocity,
     * and use the remaining time to complete the required distance. We count the number of ways to win the {@link Race}s, then multiply them up.
     *
     * <p>
     * if {@code isMultipleRaces} is {@code true}, each column of the input is referring to a separate {@link Race}, otherwise all columns should be
     * concatenated to create one large {@link Race}.
     *
     * @param values          the input {@link Race}s
     * @param isMultipleRaces whether the input is multiple {@link Race}s or a single large {@link Race}
     * @return the number of ways to win the supplied{@link Race}s
     */
    public static long countNumberOfWaysToWin(final List<String> values, final boolean isMultipleRaces) {
        return parseRaces(values, isMultipleRaces)
            .stream()
            .mapToLong(Day06::countWaysToWinRace)
            .reduce(1L, (first, second) -> first * second);
    }

    private static Collection<Race> parseRaces(final List<String> values, final boolean isMultipleRaces) {
        final List<Long> times = StringUtils.collectNumbersInOrder(values.getFirst());
        final List<Long> distances = StringUtils.collectNumbersInOrder(values.getLast());
        final Collection<Race> races = new ArrayList<>();
        final int numberOfTimes = times.size();

        if (isMultipleRaces) {
            for (int i = 0; i < numberOfTimes; i++) {
                races.add(new Race(times.get(i), distances.get(i)));
            }
        } else {
            // Concatenate all time values and distance values to make one large race definition
            final StringBuilder timeBuilder = new StringBuilder();
            final StringBuilder distanceBuilder = new StringBuilder();
            for (int i = 0; i < numberOfTimes; i++) {
                timeBuilder.append(times.get(i));
                distanceBuilder.append(distances.get(i));
            }

            races.add(new Race(Long.parseLong(timeBuilder.toString()), Long.parseLong(distanceBuilder.toString())));
        }

        return races;
    }

    private static long countWaysToWinRace(final Race race) {
        int count = 0;
        for (int timeToHold = 1; timeToHold < race.time - 1; timeToHold++) {
            final long remainingTime = race.time - timeToHold;
            final long travelledDistance = timeToHold * remainingTime;

            if (travelledDistance > race.distance) {
                count++;
            }
        }
        return count;
    }

    /**
     * Simple record defining a race.
     *
     * @param time     the time available
     * @param distance the distance to travel
     */
    private record Race(long time, long distance) {

    }
}
