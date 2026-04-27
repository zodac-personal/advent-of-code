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

package net.zodac.advent.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for generating the power-set of an input {@link java.util.Collection}.
 */
public final class PowerSetUtils {

    private PowerSetUtils() {

    }

    /**
     * Generates a power-list of the input with duplicates, then filters based on the provided {@link PowerSetFilter}.
     *
     * <p>
     * For an example, take the input {@link List}: [1, 5, 25, 500]. Suppose we wanted a power-list, but only combinations where the sum of numbers
     * less than <b>10</b> are equal to <b>6</b>. The full power-list would be:
     *
     * <pre>
     *     {
     *       {}
     *       {1}
     *       {5}
     *       {25}
     *       {500}
     *       {1, 5}
     *       {1, 25}
     *       {1, 500}
     *       {5, 25}
     *       {5, 500}
     *       {25, 500}
     *       {1, 5, 25}
     *       {1, 5, 500}
     *       {1, 25, 500}
     *       {5, 25, 500}
     *       {1, 5, 25, 500}
     *     }
     * </pre>
     *
     * <p>
     * Of these, the valid ones would be:
     *
     * <pre>
     *     {
     *       {1, 5}
     *       {1, 5, 25}
     *       {1, 5, 500}
     *       {1, 5, 25, 500}
     *     }
     * </pre>
     *
     * <p>
     * We would define a {@link PowerSetFilter} to represent this as follows:
     *
     * <pre>
     *     {@code
     *     // Group combination values into units/tens/hundreds, and only allow combos where units equal 6 (contain 5 and 1)
     *     final PowerSetFilter<Integer, Integer> powerSetFilter = new PowerSetFilter<>(
     *         i -> i / 10,
     *         Map.of(
     *             0, zeros -> zeros != null && zeros.stream().mapToInt(i -> i).sum() == 6
     *         )
     *     );
     *     }
     * </pre>
     *
     * @param input          the input {@link List}
     * @param powerSetFilter the {@link PowerSetFilter}s to filter the available combinations
     * @param <T>            the type of the {@link List}
     * @param <G>            the key type of the groups of the input {@link List}
     * @return the filtered power-list of the input
     * @see #getPowerList(List)
     */
    public static <T, G> List<List<T>> getFilteredPowerList(final List<T> input, final PowerSetFilter<G, ? super T> powerSetFilter) {
        return getPowerList(input)
            .stream()
            .filter(combinations -> isValidCombination(combinations, powerSetFilter))
            .toList();
    }

    /**
     * Generates a power-set of the input with duplicates, then filters based on the provided {@link PowerSetFilter}.
     *
     * <p>
     * For an example, take the input {@link Set}: [1, 5, 25, 500]. Suppose we wanted a power-set, but only combinations where the sum of numbers
     * less than <b>10</b> are equal to <b>6</b>. The full power-set would be:
     *
     * <pre>
     *     {
     *       {}
     *       {1}
     *       {5}
     *       {25}
     *       {500}
     *       {1, 5}
     *       {1, 25}
     *       {1, 500}
     *       {5, 25}
     *       {5, 500}
     *       {25, 500}
     *       {1, 5, 25}
     *       {1, 5, 500}
     *       {1, 25, 500}
     *       {5, 25, 500}
     *       {1, 5, 25, 500}
     *     }
     * </pre>
     *
     * <p>
     * Of these, the valid ones would be:
     *
     * <pre>
     *     {
     *       {1, 5}
     *       {1, 5, 25}
     *       {1, 5, 500}
     *       {1, 5, 25, 500}
     *     }
     * </pre>
     *
     * <p>
     * We would define a {@link PowerSetFilter} to represent this as follows:
     *
     * <pre>
     *     {@code
     *     // Group combination values into units/tens/hundreds, and only allow combos where units equal 6 (contain 5 and 1)
     *     final PowerSetFilter<Integer, Integer> powerSetFilter = new PowerSetFilter<>(
     *         i -> i / 10,
     *         Map.of(
     *             0, zeros -> zeros != null && zeros.stream().mapToInt(i -> i).sum() == 6
     *         )
     *     );
     *     }
     * </pre>
     *
     * @param input          the input {@link List}
     * @param powerSetFilter the {@link PowerSetFilter}s to filter the available combinations
     * @param <T>            the type of the {@link List}
     * @param <G>            the key type of the groups of the input {@link List}
     * @return the filtered power-list of the input
     * @see #getPowerList(List)
     */
    public static <T, G> Set<Set<T>> getFilteredPowerSet(final Set<T> input, final PowerSetFilter<G, ? super T> powerSetFilter) {
        return getPowerSet(input)
            .stream()
            .filter(combinations -> isValidCombination(combinations, powerSetFilter))
            .collect(Collectors.toSet());
    }

    /**
     * Generates the power-list of the input with duplicates. The power-list is a made up term for the {@link Set} that contains all possible
     * combinations of the input {@link List}. Since a {@link Set} cannot contain duplicates, we will use {@link List}s as inputs and
     * outputs instead.
     *
     * <p>
     * For example, given an input of {@code {1, 1, 3}}, the power-list would be:
     * <pre>
     *     {
     *       {},
     *       {1}
     *       {1}
     *       {3}
     *       {1, 1}
     *       {1, 3}
     *       {1, 3}
     *       {1, 1, 3}
     *     }
     * </pre>
     *
     * @param input the input {@link List}
     * @param <T>   the type of the {@link List}
     * @return the power-list of the input, with 2^n entries
     * @see #getPowerSet(Set)
     */
    public static <T> List<List<T>> getPowerList(final List<? extends T> input) {
        final List<List<T>> powerList = new ArrayList<>();
        if (input.isEmpty()) {
            powerList.add(new ArrayList<>(0));
            return powerList;
        }

        final List<T> list = new ArrayList<>(input);
        final T head = list.removeFirst();
        final List<T> remainder = new ArrayList<>(list);

        for (final List<T> subList : getPowerList(remainder)) {
            final List<T> newList = new ArrayList<>();
            newList.add(head);
            newList.addAll(subList);
            powerList.add(newList);
            powerList.add(subList);
        }
        return powerList;
    }

    /**
     * Generates the power-set of the input {@link Set}. The power-set is the {@link Set} that contains all possible combinations of the input
     * {@link Set}.
     *
     * <p>
     * For example, given an input of {@code {1, 2, 3}}, the power-set would be:
     * <pre>
     *     {
     *       {}
     *       {1}
     *       {2}
     *       {3}
     *       {1, 2}
     *       {1, 3}
     *       {2, 3}
     *       {1, 2, 3}
     *     }
     * </pre>
     *
     * @param input the input {@link Set}
     * @param <T>   the type of the {@link Set}
     * @return the power-set of the input, with 2^n entries
     * @see #getPowerList(List)
     */
    public static <T> Set<Set<T>> getPowerSet(final Set<? extends T> input) {
        final Set<Set<T>> powerSet = new HashSet<>();
        if (input.isEmpty()) {
            powerSet.add(HashSet.newHashSet(0));
            return powerSet;
        }

        final List<T> list = new ArrayList<>(input);
        final T head = list.removeFirst();
        final Set<T> remainder = new HashSet<>(list);

        for (final Set<T> subSet : getPowerSet(remainder)) {
            final Set<T> newSet = new HashSet<>();
            newSet.add(head);
            newSet.addAll(subSet);
            powerSet.add(newSet);
            powerSet.add(subSet);
        }
        return powerSet;
    }

    private static <T, G> boolean isValidCombination(final Collection<? extends T> powerSetCombinations, final PowerSetFilter<G, T> powerSetFilter) {
        final Map<G, List<T>> combinationsByGroup = powerSetCombinations
            .stream()
            .collect(Collectors.groupingBy(powerSetFilter.groupingFunction()));

        final Map<G, Predicate<List<T>>> predicates = powerSetFilter.predicates();
        for (final Map.Entry<G, Predicate<List<T>>> predicateEntry : predicates.entrySet()) {
            final G group = predicateEntry.getKey();
            final List<T> combinations = combinationsByGroup.getOrDefault(group, new ArrayList<>());
            final Predicate<List<T>> predicateForGroup = predicateEntry.getValue();

            if (!predicateForGroup.test(combinations)) {
                return false;
            }
        }

        return true;
    }
}
