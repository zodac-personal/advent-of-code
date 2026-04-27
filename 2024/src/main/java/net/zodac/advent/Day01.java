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
import java.util.Collections;
import java.util.List;
import net.zodac.advent.util.CollectionUtils;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2024, Day 1.
 *
 * @see <a href="https://adventofcode.com/2024/day/1">[2024: 01] Historian Hysteria</a>
 */
public final class Day01 {

    private Day01() {

    }

    /**
     * Given a {@link List} of {@link String}s, where each row has two numbers, extract the values and create two {@link List}s of {@link Long}s. Sort
     * the two {@link List}s, then compare the {@link Long}s at each index. Take the {@link Math#abs(long)} difference between the values, and sum up
     * all differences.
     *
     * @param values the input {@link String}s
     * @return the total difference between the values of the two {@link List}s
     */
    public static long totalDifferenceBetweenLists(final Collection<String> values) {
        final List<Long> first = new ArrayList<>();
        final List<Long> second = new ArrayList<>();

        for (final String input : values) {
            final List<Long> numbers = StringUtils.collectNumbersInOrder(input);
            first.add(numbers.getFirst());
            second.add(numbers.getLast());
        }

        Collections.sort(first);
        Collections.sort(second);

        long diff = 0L;
        final int size = first.size();
        for (int i = 0; i < size; i++) {
            diff += Math.abs(first.get(i) - second.get(i));
        }

        return diff;
    }

    /**
     * Given a {@link List} of {@link String}s, where each row has two numbers, extract the values and create two {@link List}s of {@link Long}s. For
     * each index in the first {@link List}, count the number of occurrences in the second {@link List}. The index's 'match value' is calculated as:
     * <pre>
     *     (number of occurrences) * (the value)
     * </pre>
     *
     * <p>
     * Sum all the match values for all values in the first {@link List}.
     *
     * @param values the input {@link String}s
     * @return the total of all match values
     */
    public static long sumOfMatchValues(final Collection<String> values) {
        final Collection<Long> first = new ArrayList<>();
        final Collection<Long> second = new ArrayList<>();

        for (final String input : values) {
            final List<Long> numbers = StringUtils.collectNumbersInOrder(input);
            first.add(numbers.getFirst());
            second.add(numbers.getLast());
        }

        long matchValues = 0L;
        for (final Long value : first) {
            final long matchValue = value * CollectionUtils.countMatches(second, value);
            matchValues += matchValue;
        }

        return matchValues;
    }
}
