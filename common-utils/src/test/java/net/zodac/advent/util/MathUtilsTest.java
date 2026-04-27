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

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link MathUtils}.
 */
class MathUtilsTest {

    @ParameterizedTest
    @CsvSource({
        "6,21",     // Positive
        "-6,15",    // Negative
    })
    void testCalculateTriangularNumberValue(final int input, final long expected) {
        final long triangularNumber = MathUtils.calculateTriangularNumberValue(input);
        assertThat(triangularNumber)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideForContainsAnyNotAllowedValue")
    void testContainsAnyNotAllowedValue(final List<Long> input, final long allowedValue, final boolean expected) {
        final boolean output = MathUtils.containsAnyNotAllowedValue(input, allowedValue);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForContainsAnyNotAllowedValue() {
        return Stream.of(
            Arguments.of(List.of(0L, 0L), 0L, false),      // List only contains allowed value multiple times
            Arguments.of(List.of(0L), 0L, false),     // List only contains allowed value a single time
            Arguments.of(List.of(), 0L, false),          // List is empty
            Arguments.of(List.of(1L, 2L, 3L), 0L, true),    // List only contains non-allowed values
            Arguments.of(List.of(0L, 1L, 2L), 0L, true)     // List contains allowed and non-allowed values
        );
    }

    @ParameterizedTest
    @CsvSource({
        "2,4,13",   // Valid
        "1,1,0",    // Valid origin
    })
    void testDiagonalSum(final int row, final int column, final long expected) {
        final long output = MathUtils.diagonalSum(row, column);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "-2,4,Both row -2 and column 4 must be at least 1",     // Negative row
        "2,-4,Both row 2 and column -4 must be at least 1",     // Negative column
    })
    void testDiagonalSum_givenInvalidInputs(final int row, final int column, final String errorMessage) {
        assertThatThrownBy(() -> MathUtils.diagonalSum(row, column))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    @ParameterizedTest
    @MethodSource("provideForGreatestCommonDivisor")
    void testGreatestCommonDivisor(final long input, final long[] additionalInputs, final long expected) {
        final long output = MathUtils.greatestCommonDivisor(input, additionalInputs);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForGreatestCommonDivisor() {
        return Stream.of(
            Arguments.of(9L, new long[] {36L, 54, 99}, 9L),                                             // Input is divisor of others
            Arguments.of(36L, new long[] {54L, 90}, 18L),                                               // All inputs share divisor
            Arguments.of(5L, new long[] {}, 5L),                                                        // Input with no additional inputs
            Arguments.of(9L, new long[] {0L, 36L}, 9L),                                                 // Input contains zeros
            Arguments.of(0L, new long[] {0L, 0L}, 0L),                                                  // Input only has 0s
            Arguments.of(9L, new long[] {-36L, 54L, 99L}, 9L),                                          // Single negative number
            Arguments.of(9L, new long[] {-36L, -54L, -99L}, 9L),                                        // Only negative numbers
            Arguments.of(10_241_191_004_509L, new long[] {20_482_382_009_018L}, 10_241_191_004_509L)    // Input greater than int value
        );
    }

    @ParameterizedTest
    @CsvSource({
        "1,5,3,true",       // Input within range
        "3,5,3,true",       // Input equal to start
        "1,3,3,true",       // Input equal to end
        "1,1,1,true",       // Input and range equal values
        "-5,-1,-3,true",    // Input within range, all negative values
        "-5,3,1,true",      // Input within range, input positive, range positive/negative
        "-5,3,-1,true",     // Input within range, input negative, range positive/negative
        "1,5,9,false",      // Input outside range
        "-5,-1,-9,false",   // Input outside range, negative values
    })
    void testIsBetween(final int start, final int end, final int input, final boolean expected) {
        final boolean output = MathUtils.isBetween(start, end, input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @Test
    void testIsBetween_givenRangeEndIsLessThanRangeStart() {
        assertThatThrownBy(() -> MathUtils.isBetween(1, 0, 2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Cannot have end 0 less than start 1");
    }

    @ParameterizedTest
    @CsvSource({
        "2,true",       // Even number
        "-2,true",      // Even negative number
        "3,false",      // Odd number
        "-3,false",     // Odd negative number
        "0,true",       // Zero
    })
    void testIsEven(final int input, final boolean expected) {
        final boolean output = MathUtils.isEven(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "3,true",        // Odd number
        "-3,true",       // Odd negative number
        "2,false",       // Even number
        "-2,false",      // Even negative number
        "0,false",       // Zero
    })
    void testIsOdd(final int input, final boolean expected) {
        final boolean output = MathUtils.isOdd(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideForLowestCommonMultiple")
    void testLowestCommonMultiple(final List<Long> input, final long expected) {
        final long output = MathUtils.lowestCommonMultiple(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForLowestCommonMultiple() {
        return Stream.of(
            Arguments.of(List.of(6L, 9L, 18L, 15L, 27L, 54L), 270L),                    // Multiple inputs with common divisors
            Arguments.of(List.of(1L, 0L, 3L), 0L),                                      // Inputs including 0
            Arguments.of(List.of(5L), 5L),                                           // Single input
            Arguments.of(List.of(7L, 13L, 29L), 2_639L),                                // Inputs are prime numbers
            Arguments.of(List.of(10_241_191_004_509L, 20_482_382_009_018L), 877_580L)   // Input greater than int value
        );
    }

    @Test
    void testLowestCommonMultiple_givenInvalidInputs() {
        final List<Long> input = List.of();
        assertThatThrownBy(() -> MathUtils.lowestCommonMultiple(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Input cannot be empty");
    }

    @ParameterizedTest
    @MethodSource("provideForMax")
    void testMax(final int input, final int[] others, final int expected) {
        final int output = MathUtils.max(input, others);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForMax() {
        return Stream.of(
            Arguments.of(1, new int[] {2, 3}, 3),       // Multiple positive values
            Arguments.of(-1, new int[] {-2, -3}, -1),   // Multiple negative values
            Arguments.of(-1, new int[] {2, -3}, 2),     // Negative and positive values
            Arguments.of(0, new int[] {0, 0}, 0),       // Zeros
            Arguments.of(4, new int[0], 4)              // No additional inputs
        );
    }

    @ParameterizedTest
    @MethodSource("provideForMultiplyElementsThenSum")
    void testMultiplyElementsThenSum(final List<Integer> first, final List<Integer> second, final long expected) {
        final long output = MathUtils.multiplyElementsThenSum(first, second);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForMultiplyElementsThenSum() {
        return Stream.of(
            Arguments.of(List.of(1, 2, 3), List.of(4, 5, 6), 32L),          // Multiple positive values
            Arguments.of(List.of(-1, -2, -3), List.of(-4, -5, -6), 32L),    // Multiple negative values
            Arguments.of(List.of(-1, 2, 3), List.of(4, 5, 6), 24L),         // Negative and positive values
            Arguments.of(List.of(0, 2, 3), List.of(4, 5, 6), 28L),          // Including zero
            Arguments.of(List.of(0, 0, 0), List.of(0, 0, 0), 0L),           // All zeros
            Arguments.of(List.of(), List.of(), 0L)                          // Both inputs empty
        );
    }

    @Test
    void testMultiplyElementsThenSum_givenInvalidInputs() {
        final List<Integer> first = List.of(1, 2, 3);
        final List<Integer> second = List.of(4);
        assertThatThrownBy(() -> MathUtils.multiplyElementsThenSum(first, second))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Inputs must be of same length, found: 3 and 1");
    }

    @ParameterizedTest
    @MethodSource("provideForNextValueInSequence")
    void testNextValueInSequence(final List<Long> sequence, final long expected) {
        final long output = MathUtils.nextValueInSequence(sequence);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForNextValueInSequence() {
        return Stream.of(
            Arguments.of(List.of(1L, 2L, 3L), 4L),
            Arguments.of(List.of(2L, 4L, 6L), 8L),
            Arguments.of(List.of(1L, 1L, 2L, 4L, 8L, 16L, 32L), 64L),
            Arguments.of(List.of(4L, 8L, 12L, 16L), 20L),
            Arguments.of(List.of(1L, 4L, 9L, 16L, 25L), 36L),
            Arguments.of(List.of(0L, 3L, 6L, 9L, 12L, 15L), 18L),
            Arguments.of(List.of(1L, 3L, 6L, 10L, 15L, 21L), 28L),
            Arguments.of(List.of(10L, 13L, 16L, 21L, 30L, 45L), 68L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideForPreviousValueInSequence")
    void testPreviousValueInSequence(final List<Long> sequence, final long expected) {
        final long output = MathUtils.previousValueInSequence(sequence);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForPreviousValueInSequence() {
        return Stream.of(
            Arguments.of(List.of(2L, 3L, 4L), 1L),
            Arguments.of(List.of(4L, 6L, 8L), 2L),
            Arguments.of(List.of(1L, 2L, 4L, 8L, 16L, 32L, 64L), 1L),
            Arguments.of(List.of(8L, 12L, 16L, 20L), 4L),
            Arguments.of(List.of(4L, 9L, 16L, 25L, 36L), 1L),
            Arguments.of(List.of(3L, 6L, 9L, 12L, 15L, 18L), 0L),
            Arguments.of(List.of(3L, 6L, 10L, 15L, 21L, 28L), 1L),
            Arguments.of(List.of(13L, 16L, 21L, 30L, 45L, 68L), 10L)
        );
    }
}
