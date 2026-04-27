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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link CollectionUtils}.
 */
class CollectionUtilsTest {

    @ParameterizedTest
    @MethodSource("provideForContainsDuplicates")
    <E> void testContainsDuplicates(final List<E> input, final boolean expected) {
        final boolean output = CollectionUtils.containsDuplicates(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForContainsDuplicates() {
        return Stream.of(
            Arguments.of(List.of(1, 2, 2), true),               // Integer input with single duplicate
            Arguments.of(List.of(1, 1, 2, 2), true),            // Integer input with multiple duplicates
            Arguments.of(List.of(1, 2, 3), false),              // Integer input with no duplicates
            Arguments.of(List.of("a", "b", "c"), false),        // String input with no duplicates
            Arguments.of(List.of("a", "b", "b"), true),         // String input with single duplicate
            Arguments.of(List.of("a", "a", "b", "b"), true),    // String input with multiple duplicates
            Arguments.of(List.of(), false)                      // Empty
        );
    }

    @ParameterizedTest
    @MethodSource("provideForCountMatches")
    <E> void testCountMatches(final List<E> input, final E wantedValue, final long expectedCount) {
        final long output = CollectionUtils.countMatches(input, wantedValue);
        assertThat(output)
            .isEqualTo(expectedCount);
    }

    private static Stream<Arguments> provideForCountMatches() {
        return Stream.of(
            Arguments.of(List.of(1, 2, 2), 1, 1L),                      // Integer input with single match
            Arguments.of(List.of(1, 1, 2, 2, 2), 2, 3L),                // Integer input with three matches
            Arguments.of(List.of(1, 2, 3), 4, 0L),                      // Integer input with no matches
            Arguments.of(List.of("a", "b", "c"), "a", 1L),              // String input with single match
            Arguments.of(List.of("a", "b", "b"), "b", 2L),              // String input with two matches
            Arguments.of(List.of(true, true, false, true), true, 3L),   // Boolean input with three matches
            Arguments.of(List.of(), 1, 0)                               // Empty
        );
    }

    @ParameterizedTest
    @MethodSource("provideForExtractValuesAsList")
    <I, O> void testExtractValuesAsList(final List<I> input, final Function<? super I, O> extractionFunction, final List<? extends O> expected) {
        final List<O> output = CollectionUtils.extractValuesAsList(input, extractionFunction);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForExtractValuesAsList() {
        return Stream.of(
            Arguments.of(List.of(1L, 2L, 3L), (Function<Long, Integer>) Long::intValue, List.of(1, 2, 3)),               // Long -> Integer
            Arguments.of(List.of('a', 'b', 'c'), (Function<Character, String>) String::valueOf, List.of("a", "b", "c")), // Character -> String
            Arguments.of(List.of(), (Function<Character, String>) String::valueOf, List.of())                            // Empty
        );
    }

    @ParameterizedTest
    @MethodSource("provideForFindValuesLessThan")
    <E> void testFindValuesLessThan(final List<E> input, final E value, final Comparator<? super E> comparator, final List<? extends E> expected) {
        final List<E> output = CollectionUtils.findValuesLessThan(input, value, comparator);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForFindValuesLessThan() {
        return Stream.of(
            Arguments.of(List.of(1, 2, 3), 2, (Comparator<Integer>) Integer::compareTo, List.of(1)),            // Integer, single match
            Arguments.of(List.of(1, 2, 3), 5, (Comparator<Integer>) Integer::compareTo, List.of(1, 2, 3)),          // Integer, all matches
            Arguments.of(List.of(1, 2, 3), 0, (Comparator<Integer>) Integer::compareTo, List.of()),                // Integer, no matches
            Arguments.of(List.of("a", "b", "c"), "b", (Comparator<String>) String::compareTo, List.of("a")),    // String, single match
            Arguments.of(List.of(), 0, (Comparator<Integer>) Integer::compareTo, List.of())                        // Empty
        );
    }

    @ParameterizedTest
    @MethodSource("provideForGetKeyByValue")
    <K, V> void testGetKeyByValue(final Map<K, ? super V> input, final V value, final Optional<V> expected) {
        final Optional<K> output = CollectionUtils.getKeyByValue(input, value);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForGetKeyByValue() {
        return Stream.of(
            Arguments.of(Map.of("key1", "value1", "key2", "value2"), "value2", Optional.of("key2")),    // String Key found
            Arguments.of(Map.of("key1", "value1", "key2", "value2"), "value3", Optional.empty()),             // No String key found
            Arguments.of(Map.of(1, 10, 2, 20), 20, Optional.of(2)),                                     // Integer key found
            Arguments.of(Map.of(1, 10, 2, 20), 30, Optional.empty()),                                         // No Integer key found
            Arguments.of(Map.of(), "value1", Optional.empty())                                                              // Empty
        );
    }

    @ParameterizedTest
    @MethodSource("provideForMiddleValue")
    <E extends Comparable<E>> void testMiddleValue(final List<E> input, final E expected) {
        final E output = CollectionUtils.getMiddleSortedValue(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForMiddleValue() {
        return Stream.of(
            Arguments.of(List.of("a", "b", "c"), "b"),  // Sorted String list of odd size
            Arguments.of(List.of("c", "a", "b"), "b"),  // Unsorted String list of odd size
            Arguments.of(List.of("a"), "a"),        // List String with single size
            Arguments.of(List.of(1, 2, 3), 2),          // Sorted Integer list of odd size
            Arguments.of(List.of(3, 1, 2), 2),          // Unsorted Integer list of odd size
            Arguments.of(List.of(1), 1)             // List Integer with single size
        );
    }

    @ParameterizedTest
    @MethodSource("provideForMiddleValue_invalid")
    <E extends Comparable<E>> void testMiddleValue_givenInvalidInputs(final List<E> input, final String errorMessage) {
        assertThatThrownBy(() -> CollectionUtils.getMiddleSortedValue(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    private static Stream<Arguments> provideForMiddleValue_invalid() {
        return Stream.of(
            Arguments.of(List.of("a", "b"), "Expected list with positive odd size, found size: 2"),     // String list of even size
            Arguments.of(List.of(1, 2), "Expected list with positive odd size, found size: 2"),     // Integer list of even size
            Arguments.of(List.of(), "Expected list with positive odd size, found size: 0")              // Empty
        );
    }

    @ParameterizedTest
    @MethodSource("provideForGroupBySize")
    <E> void testGroupBySize(final List<? extends E> input, final int amountPerGroup, final List<? extends List<E>> expected) {
        final List<List<E>> output = CollectionUtils.groupBySize(input, amountPerGroup);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForGroupBySize() {
        return Stream.of(
            Arguments.of(List.of("a", "b", "c", "d"), 2, List.of(List.of("a", "b"), List.of("c", "d"))),    // Divisible by amountPerGroup
            Arguments.of(List.of(1, 2, 3, 4), 2, List.of(List.of(1, 2), List.of(3, 4))),                    // Divisible by amountPerGroup
            Arguments.of(List.of(), 2, List.of(List.of()))                                                  // Empty
        );
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "2|Expected number of entries to be divisible by 2, found: 1",  // Invalid number of entries
        "0|amountPerGroup must be at least 1, found: 0",                // Zero amount per group
        "-2|amountPerGroup must be at least 1, found: -2",              // Negative amount per group
    })
    void testGroupBySize_givenInvalidInputs(final int amountPerGroup, final String errorMessage) {
        final List<String> input = List.of("a");
        assertThatThrownBy(() -> CollectionUtils.groupBySize(input, amountPerGroup))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    @ParameterizedTest
    @MethodSource("provideForIntersection")
    <E> void testIntersection(final Set<? extends E> first, final Set<E> second, final Set<? extends E> expected) {
        final Set<E> output = CollectionUtils.intersection(first, second);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    @ParameterizedTest
    @MethodSource("provideForIndexOf")
    <E> void testIndexOf(final Set<? extends E> input, final E wantedValue, final int expected) {
        final int output = CollectionUtils.indexOf(input, wantedValue);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForIndexOf() {
        final Set<String> unsequencedSet = new HashSet<>(4);
        unsequencedSet.add("a");
        unsequencedSet.add("b");
        unsequencedSet.add("c");
        unsequencedSet.add("d");

        return Stream.of(
            Arguments.of(unsequencedSet, "c", 2),                                       // Non-sequenced set, order not guaranteed
            Arguments.of(new LinkedHashSet<>(List.of("a", "b", "c", "d")), "b", 1),     // Sequenced set
            Arguments.of(new LinkedHashSet<>(List.of("a", "b", "c", "d")), "e", -1),    // Value doesn't exist
            Arguments.of(new LinkedHashSet<>(List.of(1, 2, 3, 4)), 4, 3),               //Integers
            Arguments.of(Set.of(), 2, -1)                                               // Empty
        );
    }

    private static Stream<Arguments> provideForIntersection() {
        return Stream.of(
            // Strings
            Arguments.of(Set.of("a", "b", "c"), Set.of("a", "b", "c"), Set.of("a", "b", "c")),                  // Both inputs match
            Arguments.of(Set.of("a", "b", "c"), Set.of("b", "c", "a"), Set.of("a", "b", "c")),                  // Order insensitive
            Arguments.of(Set.of("a", "b", "c"), Set.of("c", "d", "e", "f"), Set.of("c")),                       // Single match
            Arguments.of(Set.of("a", "b", "c"), Set.of("a", "b", "c", "d", "e", "f"), Set.of("a", "b", "c")),   // Multiple matches
            Arguments.of(Set.of("a", "b", "c"), Set.of("a", "b"), Set.of("a", "b")),                            // Second is a subset of first
            Arguments.of(Set.of("a", "b", "c"), Set.of("d", "e", "f"), Set.of()),                               // No match
            // Integers
            Arguments.of(Set.of(1, 2, 3), Set.of(1, 2, 3), Set.of(1, 2, 3)),            // Both inputs match
            Arguments.of(Set.of(1, 2, 3), Set.of(2, 3, 1), Set.of(1, 2, 3)),            // Order insensitive
            Arguments.of(Set.of(1, 2, 3), Set.of(3, 4, 5, 6), Set.of(3)),               // Single match
            Arguments.of(Set.of(1, 2, 3), Set.of(1, 2, 3, 4, 5, 6), Set.of(1, 2, 3)),   // Multiple matches
            Arguments.of(Set.of(1, 2, 3), Set.of(1, 2), Set.of(1, 2)),                  // Second is a subset of first
            Arguments.of(Set.of(1, 2, 3), Set.of(4, 5, 6), Set.of())                    // No match
        );
    }

    @ParameterizedTest
    @MethodSource("provideForIsStrictDecreasing")
    <E extends Comparable<E>> void testIsStrictlyDecreasing(final List<E> input, final boolean expected) {
        final boolean output = CollectionUtils.isStrictlyDecreasing(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForIsStrictDecreasing() {
        return Stream.of(
            Arguments.of(List.of(3, 2, 1), true),           // Descending integers
            Arguments.of(List.of(3), true),             // Single integer
            Arguments.of(List.of(3, 1, 0, -1), true),       // Descending negative integers
            Arguments.of(List.of(3, 1, 2), false),          // Not consistently descending integers
            Arguments.of(List.of(1, 2, 3), false),          // Increasing integers
            Arguments.of(List.of(3, 3, 2), false),          // Equal integers
            Arguments.of(List.of("c", "b", "a"), true),     // Descending strings
            Arguments.of(List.of("c"), true),           // Single string
            Arguments.of(List.of("c", "a", "0", ""), true), // Descending non-numeric strings
            Arguments.of(List.of("c", "a", "b"), false),    // Not consistently descending strings
            Arguments.of(List.of("a", "b", "c"), false),    // Increasing strings
            Arguments.of(List.of("c", "c", "b"), false),    // Equal strings
            Arguments.of(List.of(), true)                   // Empty
        );
    }

    @ParameterizedTest
    @MethodSource("provideForIsStrictIncreasing")
    <E extends Comparable<E>> void testIsStrictlyIncreasing(final List<E> input, final boolean expected) {
        final boolean output = CollectionUtils.isStrictlyIncreasing(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForIsStrictIncreasing() {
        return Stream.of(
            Arguments.of(List.of(1, 2, 3), true),           // Increasing integers
            Arguments.of(List.of(3), true),             // Single integer
            Arguments.of(List.of(-3, -1, 0, 1), true),      // Increasing negative integers
            Arguments.of(List.of(3, 1, 2), false),          // Not consistently descending integers
            Arguments.of(List.of(3, 2, 1), false),          // Descending integers
            Arguments.of(List.of(3, 3, 2), false),          // Equal integers
            Arguments.of(List.of("a", "b", "c"), true),     // Increasing strings
            Arguments.of(List.of("c"), true),           // Single string
            Arguments.of(List.of("", "0", "a", "b"), true), // Increasing non-numeric strings
            Arguments.of(List.of("c", "a", "b"), false),    // Not consistently descending strings
            Arguments.of(List.of("c", "b", "a"), false),    // Descending strings
            Arguments.of(List.of("c", "c", "b"), false),    // Equal strings
            Arguments.of(List.of(), true)                   // Empty
        );
    }
}
