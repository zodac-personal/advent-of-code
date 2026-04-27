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

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import net.zodac.advent.pojo.Reindeer;

/**
 * Solution for 2015, Day 14.
 *
 * @see <a href="https://adventofcode.com/2015/day/14">AoC 2015, Day 14</a>
 */
public final class Day14 {

    private static final int STARTING_POINTS = 0;
    private static final int POINTS_FOR_BEING_IN_THE_LEAD = 1;

    private Day14() {

    }

    /**
     * Given a {@link Collection} of {@link Reindeer}, it calculates the distance each one travels in the provided {@link Duration}, then returns the
     * furthest distance travelled.
     *
     * @param reindeers      the travelling {@link Reindeer}
     * @param travelDuration the {@link Duration} of travel
     * @return the distance of the furthest-travelled {@link Reindeer}
     */
    public static long distanceOfFurthestTravelledReindeer(final Collection<Reindeer> reindeers, final Duration travelDuration) {
        return reindeers
            .stream()
            .map(reindeer -> reindeer.calculateDistance(travelDuration))
            .mapToLong(Long::longValue)
            .max()
            .orElseThrow(() -> new IllegalStateException("Could not find a max value for input: " + reindeers));
    }

    /**
     * Given {@link Collection} of {@link Reindeer}, it calculates the distance each one travels for every second of the provided {@link Duration}. At
     * the end of each second, the {@link Reindeer} that has travelled the furthest is given {@value #POINTS_FOR_BEING_IN_THE_LEAD} points. The value
     * returned is the highest score out of all the {@link Reindeer} after the {@code travelDuration} is complete.
     *
     * @param reindeers      the travelling {@link Reindeer}
     * @param travelDuration the {@link Duration} of travel
     * @return the score of the highest-scoring {@link Reindeer}
     */
    public static long calculateTheHighestScore(final Collection<Reindeer> reindeers, final Duration travelDuration) {
        final long secondsToTravel = travelDuration.toSeconds();

        final Map<Reindeer, Integer> scoresByReindeer = HashMap.newHashMap(reindeers.size());
        for (final Reindeer reindeer : reindeers) {
            scoresByReindeer.put(reindeer, STARTING_POINTS);
        }

        for (int travelTime = 1; travelTime <= secondsToTravel; travelTime++) {
            final Collection<Reindeer> furthestReindeers = getFurthestReindeerForTime(reindeers, travelTime);

            for (final Reindeer furthestReindeer : furthestReindeers) {
                scoresByReindeer.computeIfPresent(furthestReindeer, (key, value) -> value + POINTS_FOR_BEING_IN_THE_LEAD);
            }
        }

        return Collections.max(scoresByReindeer.values());
    }

    private static Collection<Reindeer> getFurthestReindeerForTime(final Collection<Reindeer> reindeers, final int travelTime) {
        final Collection<Reindeer> furthestReindeers = new HashSet<>();
        long distanceOfFurthestReindeer = Long.MIN_VALUE;

        for (final Reindeer reindeer : reindeers) {
            final long distanceAtTime = reindeer.calculateDistance(Duration.ofSeconds(travelTime));

            if (distanceAtTime > distanceOfFurthestReindeer) {
                distanceOfFurthestReindeer = distanceAtTime;
                furthestReindeers.clear();
                furthestReindeers.add(reindeer);
            } else if (distanceAtTime == distanceOfFurthestReindeer) {
                furthestReindeers.add(reindeer);
            }
        }
        return furthestReindeers;
    }
}
