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

package net.zodac.advent.pojo;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple class representing a reindeer that can fly at a known velocity (in km/s) for a given {@link Duration}, then must rest (and not fly) for
 * another {@link Duration}.
 *
 * @param flyingVelocity the velocity of the {@link Reindeer} in km/s, when not resting
 * @param flyingDuration the {@link Duration} that the {@link Reindeer} can fly before resting
 * @param restDuration   the {@link Duration} that the {@link Reindeer} must rest before flying again
 */
public record Reindeer(int flyingVelocity, Duration flyingDuration, Duration restDuration) implements Comparable<Reindeer> {

    // Regex pattern with named capture group
    private static final Pattern REINDEER_PATTERN = Pattern.compile(
        ".* can fly (?<velocity>\\d+) km/s for (?<flyingDuration>\\d+) seconds, but then must rest for (?<restDuration>\\d+) seconds.");

    /**
     * Creates a {@link Reindeer} from a {@link CharSequence} in the format:
     * <pre>
     *     [name] can fly [flyingVelocity] km/s for [flyingDuration] seconds, but then must rest for [restDuration] seconds.
     * </pre>
     *
     * @param input the {@link CharSequence} to parse
     * @return the {@link Reindeer}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static Reindeer parse(final CharSequence input) {
        final Matcher matcher = REINDEER_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + input);
        }

        final int flyingVelocity = Integer.parseInt(matcher.group("velocity"));
        final Duration flyingDuration = Duration.ofSeconds(Integer.parseInt(matcher.group("flyingDuration")));
        final Duration restDuration = Duration.ofSeconds(Integer.parseInt(matcher.group("restDuration")));

        return new Reindeer(flyingVelocity, flyingDuration, restDuration);
    }

    /**
     * Calculates the distance that the {@link Reindeer} can travel in the provided {@link Duration} of time, in <b>km</b>.
     *
     * @param travelDuration the {@link Duration} the {@link Reindeer} will travel
     * @return the total distance covered, in <b>km</b>
     */
    public long calculateDistance(final Duration travelDuration) {
        final long fullCycleDuration = flyingDuration.toSeconds() + restDuration.toSeconds();
        final long numberOfFullCycles = travelDuration.toSeconds() / fullCycleDuration;
        final long remainingSeconds = travelDuration.toSeconds() % fullCycleDuration;

        final long totalDistanceOfFullCycles = numberOfFullCycles * flyingVelocity * flyingDuration.toSeconds();

        // Remaining time might be a single second, might be almost the full flyingDuration and restDuration
        // Since we know it is only a partial cycle, it will only fly a maximum of flyingDuration
        final long remainingFlyingTime = Math.min(remainingSeconds, flyingDuration.toSeconds());
        final long distanceOfLastCycle = flyingVelocity * remainingFlyingTime;

        return totalDistanceOfFullCycles + distanceOfLastCycle;
    }

    @Override
    public int compareTo(final Reindeer other) {
        final int compareFlyingVelocity = Integer.compare(flyingVelocity, other.flyingVelocity);
        if (compareFlyingVelocity != 0) {
            return compareFlyingVelocity;
        }

        final int compareFlyingDuration = flyingDuration.compareTo(other.flyingDuration);
        if (compareFlyingDuration != 0) {
            return compareFlyingDuration;
        }

        return restDuration.compareTo(other.restDuration);
    }
}
