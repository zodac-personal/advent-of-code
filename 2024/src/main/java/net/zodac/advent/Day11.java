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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.zodac.advent.pojo.tuple.Pair;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2024, Day 11.
 *
 * @see <a href="https://adventofcode.com/2024/day/11">[2024: 11] Plutonian Pebbles</a>
 */
public final class Day11 {

    private static final String CACHE_KEY_FORMAT = "%s-%s";
    private static final long DEFAULT_MULTIPLICATION_VALUE = 2_024L;

    private Day11() {

    }

    /**
     * Given an input {@link String} defining a {@link List} of stone values, we blink {@code numberOfBlinks} times. After each blink the stones will
     * change based on the following rules. Only one of the following will be performed, in order of priority:
     *
     * <ol>
     *     <li>If the value is <b>0</b>, replace with a value of <b>1</b></li>
     *     <li>If the value has an even number of digits, replace with two values (both halves of the existing value)</li>
     *     <li>Multiply the value by {@value #DEFAULT_MULTIPLICATION_VALUE}</li>
     * </ol>
     *
     * <p>
     * When the blinks are finished, count the number of stones.
     *
     * @param inputStoneValues the input value of the stones
     * @param numberOfBlinks   the number of times to blink
     * @return the final number of stones
     */
    // TODO: Stop using CharSequence, for God's sake
    public static long countStonesAfterBlinks(final CharSequence inputStoneValues, final int numberOfBlinks) {
        final List<Long> stoneValues = StringUtils.collectNumbersInOrder(inputStoneValues);
        final Map<String, Long> cache = new HashMap<>();

        long count = 0L;
        for (final Long stoneValue : stoneValues) {
            count += updateStonesOnBlinks(cache, numberOfBlinks, stoneValue);
        }

        return count;
    }

    private static long updateStonesOnBlinks(final Map<? super String, Long> cache, final int numberOfBlinksRemaining, final long stoneValue) {
        final String cacheKey = String.format(CACHE_KEY_FORMAT, numberOfBlinksRemaining, stoneValue);

        // Check cache if value has already been calculated
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        // Base case for recursive call
        if (numberOfBlinksRemaining == 0) {
            return 1L;
        }

        final long result = updateStone(cache, numberOfBlinksRemaining, stoneValue);

        cache.put(cacheKey, result);
        return result;
    }

    private static long updateStone(final Map<? super String, Long> cache, final int numberOfBlinksRemaining, final long stoneValue) {
        // Below are the three problem conditions, recursively calling the function and caching the result
        if (stoneValue == 0) {
            return updateStonesOnBlinks(cache, numberOfBlinksRemaining - 1, 1);
        }

        if (String.valueOf(stoneValue).length() % 2 == 0) {
            final Pair<String, String> split = StringUtils.bisect(String.valueOf(stoneValue));
            final long first = Long.parseLong(split.first());
            final long second = Long.parseLong(split.second());

            return updateStonesOnBlinks(cache, numberOfBlinksRemaining - 1, first) + updateStonesOnBlinks(cache, numberOfBlinksRemaining - 1, second);
        }

        return updateStonesOnBlinks(cache, numberOfBlinksRemaining - 1, stoneValue * DEFAULT_MULTIPLICATION_VALUE);
    }
}
