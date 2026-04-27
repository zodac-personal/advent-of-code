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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PowerSetUtils}.
 */
class PowerSetUtilsTest {

    @Test
    void whenGetFilteredPowerList_givenThreeElements_andFilterIsClassBased_andSomeCombinationsFailFilter_thenFilteredPowerListIsReturned() {
        final List<BaseFilterable> input = List.of(
            FirstFilterable.INSTANCE,
            FirstFilterable.INSTANCE,
            SecondFilterable.INSTANCE
        );

        final PowerSetFilter<Class<? extends BaseFilterable>, BaseFilterable> powerSetFilter = new PowerSetFilter<>(
            BaseFilterable::getClass,
            Map.of(
                FirstFilterable.class, filterables -> {
                    final int count = filterables == null ? 0 : filterables.size();
                    return count >= 1 && count <= 2;
                },
                SecondFilterable.class, filterables -> {
                    final int count = filterables == null ? 0 : filterables.size();
                    return count >= 1 && count <= 2;
                }
            )
        );

        final List<List<BaseFilterable>> output = PowerSetUtils.getFilteredPowerList(input, powerSetFilter);
        assertThat(output)
            .hasSize(3)
            .containsExactlyInAnyOrder(
                List.of(FirstFilterable.INSTANCE, SecondFilterable.INSTANCE),
                List.of(FirstFilterable.INSTANCE, SecondFilterable.INSTANCE),
                List.of(FirstFilterable.INSTANCE, FirstFilterable.INSTANCE, SecondFilterable.INSTANCE)
            );
    }

    @Test
    void whenGetFilteredPowerList_givenThreeElements_andFilterIsValueBased_andSomeCombinationsFailFilter_thenFilteredPowerListIsReturned() {
        final List<Integer> input = List.of(1, 2, 3);

        // Filter allowing only combinations with even numbers
        final PowerSetFilter<Boolean, Integer> powerSetFilter = new PowerSetFilter<>(
            i -> i % 2 != 0,
            Map.of(
                Boolean.TRUE, oddNumbers -> oddNumbers == null || oddNumbers.isEmpty(),
                Boolean.FALSE, evenNumbers -> evenNumbers != null && !evenNumbers.isEmpty()
            )
        );

        final List<List<Integer>> output = PowerSetUtils.getFilteredPowerList(input, powerSetFilter);
        assertThat(output)
            .hasSize(1)
            .containsExactlyInAnyOrder(
                List.of(2)
            );
    }

    @Test
    void whenGetFilteredPowerList_givenThreeElements_andAllCombinationsFailFilter_thenNoCombinationIsReturned() {
        final List<BaseFilterable> input = List.of(
            FirstFilterable.INSTANCE,
            FirstFilterable.INSTANCE,
            SecondFilterable.INSTANCE
        );

        final PowerSetFilter<Class<? extends BaseFilterable>, BaseFilterable> powerSetFilter = new PowerSetFilter<>(
            BaseFilterable::getClass,
            Map.of(
                FirstFilterable.class, filterables -> filterables != null && filterables.size() > 5,
                SecondFilterable.class, filterables -> filterables != null && filterables.size() > 5
            )
        );

        final List<List<BaseFilterable>> output = PowerSetUtils.getFilteredPowerList(input, powerSetFilter);
        assertThat(output)
            .isEmpty();
    }

    @Test
    void whenGetFilteredPowerList_givenThreeElements_andNoFilter_thenFullPowerListIsReturned() {
        final List<BaseFilterable> input = List.of(
            FirstFilterable.INSTANCE,
            FirstFilterable.INSTANCE,
            SecondFilterable.INSTANCE
        );

        final PowerSetFilter<Class<? extends BaseFilterable>, BaseFilterable> powerSetFilter = new PowerSetFilter<>(
            BaseFilterable::getClass, Map.of()
        );

        final List<List<BaseFilterable>> output = PowerSetUtils.getFilteredPowerList(input, powerSetFilter);
        assertThat(output)
            .hasSize(8)
            .containsExactlyInAnyOrder(
                List.of(),
                List.of(FirstFilterable.INSTANCE),
                List.of(FirstFilterable.INSTANCE),
                List.of(SecondFilterable.INSTANCE),
                List.of(FirstFilterable.INSTANCE, FirstFilterable.INSTANCE),
                List.of(FirstFilterable.INSTANCE, SecondFilterable.INSTANCE),
                List.of(FirstFilterable.INSTANCE, SecondFilterable.INSTANCE),
                List.of(FirstFilterable.INSTANCE, FirstFilterable.INSTANCE, SecondFilterable.INSTANCE)
            );
    }

    @Test
    void whenGetFilteredPowerList_givenEmptyInput_andFilters_thenNoCombinationsAreReturned() {
        final List<BaseFilterable> input = List.of();
        final PowerSetFilter<Class<? extends BaseFilterable>, BaseFilterable> powerSetFilter = new PowerSetFilter<>(
            BaseFilterable::getClass,
            Map.of(
                FirstFilterable.class, filterables -> filterables != null && !filterables.isEmpty(),
                SecondFilterable.class, filterables -> filterables != null && !filterables.isEmpty()
            )
        );

        final List<List<BaseFilterable>> output = PowerSetUtils.getFilteredPowerList(input, powerSetFilter);
        assertThat(output)
            .isEmpty();
    }

    @Test
    void whenGetFilteredPowerList_givenEmptyInput_andNoFilters_thenEmptyCombinationIsReturned() {
        final List<BaseFilterable> input = List.of();
        final PowerSetFilter<Class<? extends BaseFilterable>, BaseFilterable> powerSetFilter = new PowerSetFilter<>(
            BaseFilterable::getClass, Map.of()
        );

        final List<List<BaseFilterable>> output = PowerSetUtils.getFilteredPowerList(input, powerSetFilter);
        assertThat(output)
            .hasSize(1)
            .containsExactlyInAnyOrder(
                List.of()
            );
    }

    @Test
    void whenGetFilteredPowerSet_givenTwoElements_andFilters_thenFilteredPowerSetIsReturned() {
        final Set<BaseFilterable> input = Set.of(
            FirstFilterable.INSTANCE,
            SecondFilterable.INSTANCE
        );

        final PowerSetFilter<Class<? extends BaseFilterable>, BaseFilterable> powerSetFilter = new PowerSetFilter<>(
            BaseFilterable::getClass,
            Map.of(
                FirstFilterable.class, filterables -> {
                    final int count = filterables == null ? 0 : filterables.size();
                    return count >= 1 && count <= 2;
                },
                SecondFilterable.class, filterables -> {
                    final int count = filterables == null ? 0 : filterables.size();
                    return count >= 1 && count <= 2;
                }
            )
        );

        final Set<Set<BaseFilterable>> output = PowerSetUtils.getFilteredPowerSet(input, powerSetFilter);
        assertThat(output)
            .hasSize(1)
            .containsExactlyInAnyOrder(
                Set.of(FirstFilterable.INSTANCE, SecondFilterable.INSTANCE)
            );
    }

    @Test
    void whenGetFilteredPowerSet_givenTwoElements_andMultiplicationBasedFilter_thenFilteredPowerSetIsReturned() {
        final Set<Integer> input = Set.of(
            1, 5, 25, 500
        );

        // Group combination values into units/tens/hundreds, and only allow combos where units equal 6 (contain 5 and 1)
        final PowerSetFilter<Integer, Integer> powerSetFilter = new PowerSetFilter<>(
            i -> i / 10,
            Map.of(
                0, zeros -> zeros != null && zeros.stream().mapToInt(i -> i).sum() == 6
            )
        );

        final Set<Set<Integer>> output = PowerSetUtils.getFilteredPowerSet(input, powerSetFilter);
        assertThat(output)
            .hasSize(4)
            .containsExactlyInAnyOrder(
                Set.of(1, 5),
                Set.of(1, 5, 25),
                Set.of(1, 5, 500),
                Set.of(1, 5, 25, 500)
            );
    }

    @Test
    void whenGetFilteredPowerSet_givenTwoElements_andNoFilter_thenFullPowerSetIsReturned() {
        final Set<BaseFilterable> input = Set.of(
            FirstFilterable.INSTANCE,
            SecondFilterable.INSTANCE
        );

        final PowerSetFilter<Class<? extends BaseFilterable>, BaseFilterable> powerSetFilter = new PowerSetFilter<>(
            BaseFilterable::getClass, Map.of()
        );

        final Set<Set<BaseFilterable>> output = PowerSetUtils.getFilteredPowerSet(input, powerSetFilter);
        assertThat(output)
            .hasSize(4)
            .containsExactlyInAnyOrder(
                Set.of(),
                Set.of(FirstFilterable.INSTANCE),
                Set.of(SecondFilterable.INSTANCE),
                Set.of(FirstFilterable.INSTANCE, SecondFilterable.INSTANCE)
            );
    }

    @Test
    void whenGetFilteredPowerSet_givenEmptyInput_andNoFilters_thenEmptySetIsReturned() {
        final Set<BaseFilterable> input = Set.of();
        final PowerSetFilter<Class<? extends BaseFilterable>, BaseFilterable> powerSetFilter = new PowerSetFilter<>(
            BaseFilterable::getClass, Map.of()
        );

        final Set<Set<BaseFilterable>> output = PowerSetUtils.getFilteredPowerSet(input, powerSetFilter);
        assertThat(output)
            .hasSize(1)
            .containsExactlyInAnyOrder(
                Set.of()
            );
    }

    @Test
    void whenGetFilteredPowerSet_givenEmptyInput_andFilters_thenNoCombinationsAreReturned() {
        final Set<BaseFilterable> input = Set.of();

        final PowerSetFilter<Class<? extends BaseFilterable>, BaseFilterable> powerSetFilter = new PowerSetFilter<>(
            BaseFilterable::getClass,
            Map.of(
                FirstFilterable.class, filterables -> {
                    final int count = filterables == null ? 0 : filterables.size();
                    return count >= 1 && count <= 2;
                },
                SecondFilterable.class, filterables -> {
                    final int count = filterables == null ? 0 : filterables.size();
                    return count >= 1 && count <= 2;
                }
            )
        );

        final Set<Set<BaseFilterable>> output = PowerSetUtils.getFilteredPowerSet(input, powerSetFilter);
        assertThat(output)
            .isEmpty();
    }

    @Test
    void whenGetPowerList_givenThreeElements_thenPowerSetIsReturned() {
        final List<Integer> input = List.of(1, 2, 3);
        final List<List<Integer>> output = PowerSetUtils.getPowerList(input);
        assertThat(output)
            .hasSize(8)
            .containsExactlyInAnyOrder(
                List.of(),
                List.of(1),
                List.of(2),
                List.of(3),
                List.of(1, 2),
                List.of(1, 3),
                List.of(2, 3),
                List.of(1, 2, 3)
            );
    }

    @Test
    void whenGetPowerList_givenThreeElementsWithDuplicate_thenPowerSetIsReturned() {
        final List<Integer> input = List.of(1, 1, 3);
        final List<List<Integer>> output = PowerSetUtils.getPowerList(input);
        assertThat(output)
            .hasSize(8)
            .containsExactlyInAnyOrder(
                List.of(),
                List.of(1),
                List.of(1),
                List.of(3),
                List.of(1, 1),
                List.of(1, 3),
                List.of(1, 3),
                List.of(1, 1, 3)
            );
    }

    @Test
    void whenGetPowerList_givenEmptyInput_thenEmptyListIsReturned() {
        final List<Integer> input = List.of();
        final List<List<Integer>> output = PowerSetUtils.getPowerList(input);
        assertThat(output)
            .hasSize(1)
            .containsExactlyInAnyOrder(
                List.of()
            );
    }

    @Test
    void whenGetPowerSet_givenThreeElements_thenPowerSetIsReturned() {
        final Set<Integer> input = Set.of(1, 2, 3);
        final Set<Set<Integer>> output = PowerSetUtils.getPowerSet(input);
        assertThat(output)
            .hasSize(8)
            .containsExactlyInAnyOrder(
                Set.of(),
                Set.of(1),
                Set.of(2),
                Set.of(3),
                Set.of(1, 2),
                Set.of(1, 3),
                Set.of(2, 3),
                Set.of(1, 2, 3)
            );
    }

    @Test
    void whenGetPowerSet_givenEmptyInput_thenEmptySetIsReturned() {
        final Set<Integer> input = Set.of();
        final Set<Set<Integer>> output = PowerSetUtils.getPowerSet(input);
        assertThat(output)
            .hasSize(1)
            .containsExactlyInAnyOrder(
                Set.of()
            );
    }

    private static class BaseFilterable {

    }

    private static final class FirstFilterable extends BaseFilterable {

        private static final FirstFilterable INSTANCE = new FirstFilterable();
    }

    private static final class SecondFilterable extends BaseFilterable {

        private static final SecondFilterable INSTANCE = new SecondFilterable();
    }
}
