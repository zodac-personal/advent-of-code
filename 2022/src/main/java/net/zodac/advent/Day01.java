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

/**
 * Solution for 2022, Day 1.
 *
 * @see <a href="https://adventofcode.com/2022/day/1">AoC 2022, Day 1</a>
 */
public final class Day01 {

    private Day01() {

    }

    /**
     * Given an input of calories, each group of calories is delimited by a blank line. For example:
     *
     * <pre>
     *     100
     *     224
     *     500
     *
     *     65
     *
     *     123
     *     1098
     *     738
     * </pre>
     *
     * <p>
     * Given these groups, we find the group with the most calories by value and return that sum. For the above example, it would be group 3 with a
     * value of <b>1,959</b>.
     *
     * @param values the input calorie values
     * @return the sum of the calories in the largest group (by value, not size)
     */
    public static long valueOfLargestGroupOfCalories(final Collection<String> values) {
        return getCalorieTotals(values)
            .stream()
            .mapToLong(l -> l)
            .max()
            .orElse(0L);
    }

    /**
     * Given an input of calories, each group of calories is delimited by a blank line. For example:
     *
     * <pre>
     *     100
     *     224
     *     500
     *
     *     65
     *
     *     123
     *     1098
     *     738
     * </pre>
     *
     * <p>
     * Given these groups, we find the top groups with the most calories by value and return the sum of those groups. For the above example, given a
     * {@code numberOfGroups} value of 2, it would be group 1 and 3 with a value of <b>2,783</b>.
     *
     * @param values         the input calorie values
     * @param numberOfGroups the number of the largest groups to count
     * @return the sum of the calories in the largest {@code numberOfGroups} groups (by value, not size)
     */
    public static long valueOfLargestGroupsOfCalories(final Collection<String> values, final int numberOfGroups) {
        return getCalorieTotals(values)
            .stream()
            .sorted(Collections.reverseOrder())
            .mapToLong(l -> l)
            .limit(numberOfGroups)
            .sum();
    }

    private static Collection<Long> getCalorieTotals(final Collection<String> values) {
        final Collection<Long> elves = new ArrayList<>();

        long currentTotal = 0L;
        for (final String value : values) {
            if (value.isBlank()) {
                elves.add(currentTotal);
                currentTotal = 0L;
            } else {
                currentTotal += Integer.parseInt(value);
            }
        }

        // Don't miss the last group!
        elves.add(currentTotal);

        return elves;
    }
}
