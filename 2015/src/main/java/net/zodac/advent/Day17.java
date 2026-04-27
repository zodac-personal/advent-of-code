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
import java.util.List;
import net.zodac.advent.util.PowerSetUtils;

/**
 * Solution for 2015, Day 17.
 *
 * @see <a href="https://adventofcode.com/2015/day/17">AoC 2015, Day 17</a>
 */
public final class Day17 {

    private Day17() {

    }

    /**
     * Given an input {@link List} of {@link Integer}s, we calculate all possible combinations of the inputs. We then sum the inputs, and count the
     * number of combinations that match the {@code wantedValue}.
     *
     * @param values      the input {@link List} of {@link Integer}s
     * @param wantedValue the wanted value of all elements in a combination
     * @return the number of matching combinations
     */
    public static long numberOfCombinationsMatchingWantedValue(final List<Integer> values, final int wantedValue) {
        final List<List<Integer>> powerList = PowerSetUtils.getPowerList(values);
        int numberOfMatchingCombinations = 0;

        for (final List<Integer> combination : powerList) {
            final int sum = sumOf(combination);

            if (sum == wantedValue) {
                numberOfMatchingCombinations++;
            }
        }

        return numberOfMatchingCombinations;
    }

    /**
     * Given an input {@link List} of {@link Integer}s, we calculate all possible combinations of the inputs. We then sum the inputs, and count the
     * number of combinations that match the {@code wantedValue}.
     *
     * <p>
     * We are only interested in the smallest sized combination, so will exclude any other matches
     *
     * @param values      the input {@link List} of {@link Integer}s
     * @param wantedValue the wanted value of all elements in a combination
     * @return the number of matching combinations with the smallest possible size
     */
    public static long numberOfSmallestSizeCombinationsMatchingWantedValue(final List<Integer> values, final int wantedValue) {
        final List<List<Integer>> powerList = PowerSetUtils.getPowerList(values);
        int numberOfSmallestSizeMatchingCombinations = 0;
        int currentSmallestSize = Integer.MAX_VALUE;

        for (final List<Integer> combination : powerList) {
            final int sum = sumOf(combination);

            if (sum == wantedValue) {
                if (combination.size() < currentSmallestSize) {
                    // If this matching combination is smaller than the current smallest, reset the counter and update the smallest size
                    numberOfSmallestSizeMatchingCombinations = 1;
                    currentSmallestSize = combination.size();
                } else if (combination.size() == currentSmallestSize) {
                    // If this matching combination is the same as the smallest, increment the counter
                    numberOfSmallestSizeMatchingCombinations++;
                }
            }
        }

        return numberOfSmallestSizeMatchingCombinations;
    }

    private static int sumOf(final Collection<Integer> input) {
        return input
            .stream()
            .mapToInt(Integer::intValue)
            .sum();
    }
}
