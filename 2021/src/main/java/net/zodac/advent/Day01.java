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

import java.util.List;
import java.util.SequencedCollection;

/**
 * Solution for 2021, Day 1.
 *
 * @see <a href="https://adventofcode.com/2021/day/1">AoC 2021, Day 1</a>
 */
public final class Day01 {

    private Day01() {

    }

    /**
     * Iterates through the supplied {@code values} and compares each entry to the one before it. If the new value is greater than the previous one,
     * the counter is updated.
     *
     * @param values the {@link SequencedCollection} of {@link Integer}s to be checked
     * @return the count of the values higher than their predecessor
     */
    public static long countValuesHigherThanPreviousValue(final SequencedCollection<Integer> values) {
        if (values.isEmpty()) {
            return 0;
        }

        long count = 0L;

        // Initialise with first value, rather than assuming the value cannot be negative
        int currentValue = values.getFirst();

        for (final int nextValue : values) {
            if (nextValue > currentValue) {
                count++;
            }

            currentValue = nextValue;
        }

        return count;
    }

    /**
     * Iterates through the supplied {@code values} and groups each {@code windowSize} entries into a 'window' for comparison. These values are summed
     * together as the first window. Then we iterate down one value (for a {@code windowSize} of <b>three</b>, for example) the second and third
     * values of the first window are reused to calculate the second window, and so on. If the new windows summed value is greater than the previous
     * one, the counter is updated.
     *
     * @param windowSize the size of the 'window' to be calculated
     * @param values     the {@link List} of {@link Integer}s to be checked
     * @return the count of the windows with a higher summed value than their predecessor
     */
    public static long countWindowValueHigherThanPreviousValue(final int windowSize, final List<Integer> values) {
        if (values.size() < windowSize) {
            return 0;
        }

        long count = 0L;

        // Initialise with first value, rather than assuming the value cannot be negative
        int currentValue = 0;
        for (int i = 0; i < windowSize; i++) {
            currentValue += values.get(i);
        }

        // We want to stop iterating 'windowSize - 1' entries before the end of the values, to avoid an IndexOutOfBoundsException
        final int iterationBoundary = values.size() - (windowSize - 1);

        for (int i = 0; i < iterationBoundary; i++) {
            int nextValue = 0;
            for (int j = 0; j < windowSize; j++) {
                nextValue += values.get(i + j);
            }

            if (nextValue > currentValue) {
                count++;
            }

            currentValue = nextValue;
        }

        return count;
    }
}
