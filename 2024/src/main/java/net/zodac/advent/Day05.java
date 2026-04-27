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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2024, Day 5.
 *
 * @see <a href="https://adventofcode.com/2024/day/5">[2024: 05] Print Queue</a>
 */
public final class Day05 {

    private Day05() {

    }

    /**
     * We are given a set of page rules in the format:
     * <pre>
     *     firstNumber|secondNumber
     * </pre>
     *
     * <p>
     * And a set of page numbers in the format:
     * <pre>
     *     firstNumber,secondNumber,thirdNumber
     * </pre>
     *
     * <p>
     * Based on this, we must determine the {@code inputPageNumbers} that are <b>valid</b>. For the valid ones, find the middle value and sum them.
     *
     * @param pageRules        the rules determining the orders the pages must be in
     * @param inputPageNumbers the page numbers to be checked
     * @return the sum of all middle values of valid {@code inputPageNumbers}
     */
    public static long sumMiddleValuesOfSortedPageNumbers(final Collection<String> pageRules, final Collection<String> inputPageNumbers) {
        final Map<Long, Set<Long>> beforeRulesByPageNumber = parseBeforeRules(pageRules);

        return inputPageNumbers
            .stream()
            .map(StringUtils::collectNumbersInOrder)
            .filter(pageNumber -> isSorted(pageNumber, beforeRulesByPageNumber))
            .mapToLong(Day05::getMiddleValue)
            .sum();
    }

    /**
     * We are given a set of page rules in the format:
     * <pre>
     *     firstNumber|secondNumber
     * </pre>
     *
     * <p>
     * And a set of page numbers in the format:
     * <pre>
     *     firstNumber,secondNumber,thirdNumber
     * </pre>
     *
     * <p>
     * Based on this, we must determine the {@code inputPageNumbers} that are <b>invalid</b>. For the invalid ones, sort them so they are valid, then
     * find the middle value and sum these values.
     *
     * @param pageRules        the rules determining the orders the pages must be in
     * @param inputPageNumbers the page numbers to be checked
     * @return the sum of all middle values of re-sorted {@code inputPageNumbers}
     */
    public static long sumMiddleValuesOfReSortedPageNumbers(final Collection<String> pageRules, final Collection<String> inputPageNumbers) {
        final Map<Long, Set<Long>> beforeRulesByPageNumber = parseBeforeRules(pageRules);

        return inputPageNumbers
            .stream()
            .map(StringUtils::collectNumbersInOrder)
            .filter(pageNumber -> !isSorted(pageNumber, beforeRulesByPageNumber))
            .map(unsortedPageNumbers -> sort(unsortedPageNumbers, beforeRulesByPageNumber))
            .mapToLong(Day05::getMiddleValue)
            .sum();
    }

    private static Map<Long, Set<Long>> parseBeforeRules(final Collection<String> pageRules) {
        final Map<Long, Set<Long>> beforeRules = new HashMap<>();
        for (final String pageRule : pageRules) {
            final List<Long> numbers = StringUtils.collectNumbersInOrder(pageRule);
            final long first = numbers.getFirst();
            final long second = numbers.getLast();

            final Set<Long> secondBefore = beforeRules.getOrDefault(second, new HashSet<>());
            secondBefore.add(first);
            beforeRules.put(second, secondBefore);
        }
        return beforeRules;
    }

    // No need for us to check the values, just sort the numbers based on the page rules, then compare to the input
    private static boolean isSorted(final Collection<Long> pageNumbers, final Map<Long, Set<Long>> beforeRulesByPageNumber) {
        final List<Long> sortedPageNumbers = sort(pageNumbers, beforeRulesByPageNumber);
        return pageNumbers.equals(sortedPageNumbers);
    }

    private static long getMiddleValue(final List<Long> pageNumbers) {
        final int middleIndex = pageNumbers.size() / 2;
        return pageNumbers.get(middleIndex);
    }

    // We can sort the set of page numbers based on only the before rules.
    // We iterate through the values and apply the rules, applying either a before/after binary decision (we can assume there are no equal values)
    private static List<Long> sort(final Collection<Long> pageNumbers, final Map<Long, Set<Long>> beforeRulesByPageNumber) {
        return pageNumbers
            .stream()
            .sorted((first, second) -> beforeRulesByPageNumber.getOrDefault(first, new HashSet<>()).contains(second) ? 1 : -1)
            .toList();
    }
}
