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

import java.util.List;
import net.zodac.advent.util.CollectionUtils;

/**
 * Simple POJO defining a report of numbers.
 *
 * @param inputs the {@link Long} numbers representing the report
 */
public record Report(List<Long> inputs) {

    /**
     * Create an instance of a {@link Report}.
     *
     * @param inputs the values of the {@link Report}
     * @return the created {@link Report}
     */
    public static Report create(final List<Long> inputs) {
        return new Report(inputs);
    }

    /**
     * Checks if the {@link Report} is 'safe', if it abides by the following rules:
     *
     * <ul>
     *      <li>The values in the report are all increasing or all decreasing</li>
     *      <li>The difference between any two adjacent values is at most {@code endRangeInclusive}</li>
     *  </ul>
     *
     * @param endRangeInclusive the maximum difference allowed between two adjacent values in the report
     * @return {@code true} if the {@link Report} is safe
     */
    public boolean isSafe(final int endRangeInclusive) {
        return (CollectionUtils.isStrictlyIncreasing(inputs) || CollectionUtils.isStrictlyDecreasing(inputs))
            && isMaxAdjacentDiffInRange(endRangeInclusive);
    }

    private boolean isMaxAdjacentDiffInRange(final int endRangeInclusive) {
        final int size = inputs.size();
        for (int i = 0; i < size - 1; i++) {
            final long curr = inputs.get(i);
            final long next = inputs.get(i + 1);

            final long diff = Math.abs(curr - next);
            if (diff > endRangeInclusive) {
                return false;
            }
        }

        return true;
    }
}
