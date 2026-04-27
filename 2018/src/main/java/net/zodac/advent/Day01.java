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

/**
 * Solution for 2018, Day 1.
 *
 * @see <a href="https://adventofcode.com/2018/day/1">AoC 2018, Day 1</a>
 */
public final class Day01 {

    // Not defined by the puzzle, but to avoid it running forever
    private static final int MAXIMUM_LIST_CYCLES = 1_000;

    private Day01() {

    }

    /**
     * Given a {@link Collection} of {@link Integer} frequencies, sum the values up.
     *
     * @param frequencies the input {@link Integer} frequencies
     * @return the sum of frequencies
     */
    public static long sumOfFrequencies(final Collection<Integer> frequencies) {
        return frequencies
            .stream()
            .mapToInt(Integer::intValue)
            .sum();
    }

    /**
     * Given a {@link Collection} of {@link Integer} frequencies, sum the values one by one. When a new frequency that has already been reached is
     * reached a second time, return that frequency.
     *
     * <p>
     * If a frequency is not reach twice, we will start the frequencies again. We will re-use the input frequencies a maximum of
     * {@value #MAXIMUM_LIST_CYCLES} times.
     *
     * @param frequencies the input {@link Integer} frequencies
     * @return the first frequency reached twice
     * @throws IllegalArgumentException thrown if the provided input does not have any frequency that is reached twice
     */
    public static long findFirstFrequencyReachedTwice(final Collection<Integer> frequencies) {
        final Collection<Integer> reachedFrequencies = new HashSet<>();
        int currentFrequency = 0;
        reachedFrequencies.add(currentFrequency);

        int timesInputHasBeenUsed = 0;
        while (timesInputHasBeenUsed < MAXIMUM_LIST_CYCLES) {
            for (final int frequency : frequencies) {
                currentFrequency += frequency;

                if (reachedFrequencies.contains(currentFrequency)) {
                    return currentFrequency;
                }

                reachedFrequencies.add(currentFrequency);
            }
            timesInputHasBeenUsed++;
        }

        throw new IllegalArgumentException("No frequency reached twice for given input");
    }
}
