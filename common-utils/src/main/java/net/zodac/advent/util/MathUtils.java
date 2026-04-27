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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.ObjLongConsumer;
import java.util.function.ToLongFunction;
import net.zodac.advent.math.MathOperation;

/**
 * Utility class with {@link Math}-based functions.
 */
public final class MathUtils {

    private static final double TRIANGULAR_NUMBER_DENOMINATOR = 2.0D;

    private MathUtils() {

    }

    /**
     * Similar to a factorial, but using addition instead of multiplication. The equation 1 + 2 + 3 ... + n can be simplified to:
     * <pre>
     *     n*(n+1)/2
     * </pre>
     * We then round to the nearest whole number.
     *
     * @param value the value whose triangular number is to be found
     * @return the triangular number for the input
     * @see <a href="https://en.wikipedia.org/wiki/Triangular_number">Triangular Number</a>
     */
    public static long calculateTriangularNumberValue(final long value) {
        return Math.round(value * (value + 1) / TRIANGULAR_NUMBER_DENOMINATOR);
    }

    /**
     * Checks the provided {@link Number}s for any value that is not the supplied {@code allowedValue}.
     *
     * @param numbers      the {@link Number}s to check
     * @param allowedValue the allowed value
     * @param <T>          the type of the {@link Number}
     * @return {@code true} if any value is not <b>0</b>
     */
    public static <T extends Number> boolean containsAnyNotAllowedValue(final Collection<T> numbers, final T allowedValue) {
        return numbers
            .stream()
            .anyMatch(number -> !number.equals(allowedValue));
    }

    /**
     * Sums up the number of elements needed to traverse an infinite 2D array diagonally to reach the wanted {@code row} and {@code column}. For
     * example:
     *
     * <pre>
     *        | 1   2   3   4   5   6
     *     ---+---+---+---+---+---+---+
     *      1 |  1   3   6  10  15  21
     *      2 |  2   5   9  14  20
     *      3 |  4   8  13  19
     *      4 |  7  12  18
     *      5 | 11  17
     *      6 | 16
     * </pre>
     *
     * <p>
     * To get to the value <b>14</b>, at (2, 4) would take 13 steps.
     *
     * <p>
     * <b>NOTE:</b> the infinite diagonal array starts at (1, 1), not (0, 0).
     *
     * @param row    the row
     * @param column the column
     * @return the number of elements to get to the wanted {@code row} and {@code column}
     * @throws IllegalArgumentException thrown if the input {@code row} or {@code column} is less than <b>1</b>
     */
    public static long diagonalSum(final int row, final int column) {
        if (row < 1 || column < 1) {
            throw new IllegalArgumentException(String.format("Both row %s and column %s must be at least 1", row, column));
        }

        long total = column;

        for (int i = 0; i < row + column - 1; i++) {
            total += i;
        }

        return total - 1;
    }

    /**
     * Calculates the Greatest Common Divisor (also known as the Highest Common Factor) of the provided {@code long}s.
     *
     * @param first  the first {@code long}, so at least one value is provided
     * @param others the {@code long}s to check
     * @return the GCD of the provided {@code long}s
     * @see <a href="https://en.wikipedia.org/wiki/Greatest_common_divisor">Greatest Common Divisor</a>
     */
    public static long greatestCommonDivisor(final long first, final long... others) {
        long gcd = first;
        for (final long input : others) {
            gcd = gcd(gcd, input);
        }

        return gcd;
    }

    private static long gcd(final long x, final long y) {
        return (y == 0) ? x : gcd(y, x % y);
    }

    /**
     * Checks if the given {@link Long} is between two other {@link Long}s.
     *
     * @param start the start of the {@link Long} range (inclusive)
     * @param end   the end of the {@link Long} range (inclusive)
     * @param input the value to check
     * @return {@code true} if the input is within the {@link Long} range
     * @throws IllegalArgumentException thrown if the {@code end} value is less than the {@code start} value
     */
    public static boolean isBetween(final long start, final long end, final long input) {
        if (end < start) {
            throw new IllegalArgumentException(String.format("Cannot have end %s less than start %s", end, start));
        }

        return input >= start && input <= end;
    }

    /**
     * Checks if the input {@code long} is even.
     *
     * @param value the {@code long} to check
     * @return {@code true} if the {@code long} is even
     */
    public static boolean isEven(final long value) {
        return value % 2 == 0;
    }

    /**
     * Checks if the input {@code long} is odd.
     *
     * @param value the {@code long} to check
     * @return {@code true} if the {@code long} is odd
     * @see #isEven(long)
     */
    public static boolean isOdd(final long value) {
        return !isEven(value);
    }

    /**
     * Calculates the Lowest Common Multiple (also known as Least Common Multiple) of the provided {@link Long}s.
     *
     * @param inputs the {@link Long}s to check
     * @return the LCM of the provided {@link Long}s
     * @throws IllegalArgumentException thrown if the input {@link Collection#isEmpty()}
     * @see <a href="https://en.wikipedia.org/wiki/Least_common_multiple">Lower Common Multiple</a>
     */
    public static long lowestCommonMultiple(final Collection<Long> inputs) {
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        long lcm = 1;
        for (final long input : inputs) {
            lcm = lcm(lcm, input);
        }

        return lcm;
    }

    private static long lcm(final long number1, final long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }

        final long gcd = greatestCommonDivisor(number1, number2);
        return Math.abs(number1 * number2) / gcd;
    }

    /**
     * Returns the maximum value of the provided {@code int} values.
     *
     * @param first  the first {@code int}, so at least one value is provided
     * @param others the {@code int}s to check
     * @return the largest value
     */
    public static int max(final int first, final int... others) {
        final Collection<Integer> valuesAsCollection = new ArrayList<>(Arrays.stream(others).boxed().toList());
        valuesAsCollection.add(first);
        return Collections.max(valuesAsCollection);
    }

    /**
     * Given two {@link List}s, the elements in both lists at the same index are multiplied, and the total values are summed.
     *
     * <p>
     * For example, take the {@link List}s {@code [1, 2, 3]} and {@code [4, 5, 6]}. The values at index 0, 1 and 2 are multiplied to give
     * {@code [4, 10, 18]}. These values are then summed to give the result {@code 32}.
     *
     * @param first  the first {@link List} of elements
     * @param second the second {@link List} of elements
     * @return the sum of the multiplied elements
     * @throws IllegalArgumentException if the two {@link List}s don't have an equal size
     */
    public static long multiplyElementsThenSum(final List<Integer> first, final List<Integer> second) {
        if (first.size() != second.size()) {
            throw new IllegalArgumentException(String.format("Inputs must be of same length, found: %s and %s", first.size(), second.size()));
        }

        final int sizeOfFirst = first.size();
        long total = 0L;

        for (int i = 0; i < sizeOfFirst; i++) {
            total += ((long) first.get(i) * second.get(i));
        }

        return total;
    }

    /**
     * Given a numeric sequence of {@link Long}s, the next value in the sequence is calculated based on the existing pattern.
     *
     * <p>
     * The value is determined by getting the diffs between each value in the sequence. Then the diffs of these diffs is also determined, and so on
     * until a set of diffs is found with only <b>0</b> values. For example, given the sequence:
     * <pre>
     *     [0, 3, 6, 9, 12, 15]
     * </pre>
     *
     * <p>
     * The first set of diffs would be:
     * <pre>
     *     [3, 3, 3, 3, 3]
     * </pre>
     *
     * <p>
     * And the next set of diffs would be:
     * <pre>
     *     [0, 0, 0, 0]
     * </pre>
     *
     * <p>
     * Now that we have reached a steady state, we can work backwards. Since we know the next 'final' diff should also be <b>0</b>, we know that for
     * the previous set of diffs the last value would need to be <b>3</b>. Similarly, we can work up to the initial sequence and realise the next
     * value would be <b>18</b>.
     *
     * @param existingSequence the existing {@link Long} sequence
     * @return the next value in the sequence
     */
    public static long nextValueInSequence(final List<Long> existingSequence) {
        return getValueInSequence(existingSequence, true);
    }

    /**
     * Given a numeric sequence of {@link Long}s, the previous value in the sequence is calculated based on the existing pattern.
     *
     * <p>
     * The value is determined by getting the diffs between each value in the sequence. Then the diffs of these diffs is also determined, and so on
     * until a set of diffs is found with only <b>0</b> values. For example, given the sequence:
     * <pre>
     *     [0, 3, 6, 9, 12, 15]
     * </pre>
     *
     * <p>
     * The first set of diffs would be:
     * <pre>
     *     [3, 3, 3, 3, 3]
     * </pre>
     *
     * <p>
     * And the next set of diffs would be:
     * <pre>
     *     [0, 0, 0, 0]
     * </pre>
     *
     * <p>
     * Now that we have reached a steady state, we can work backwards. Since we know the previous 'final' diff should also be <b>0</b>, we know that
     * for the previous set of diffs the last value would need to be <b>3</b>. Similarly, we can work up to the initial sequence and realise the next
     * value would be <b>-3</b>.
     *
     * @param existingSequence the existing {@link Long} sequence
     * @return the previous value in the sequence
     */
    public static long previousValueInSequence(final List<Long> existingSequence) {
        return getValueInSequence(existingSequence, false);
    }

    private static long getValueInSequence(final List<Long> existingSequence, final boolean getNextValue) {
        final List<List<Long>> allDiffs = new LinkedList<>();
        // Variable to differentiate between going backwards or forwards for the sequence
        final MathOperation operation = getNextValue ? MathOperation.ADD : MathOperation.MINUS;
        final ToLongFunction<List<Long>> retrievalFunction = getNextValue ? List::getLast : List::getFirst;
        final ObjLongConsumer<List<Long>> additionConsumer = getNextValue ? List::addLast : List::addFirst;

        List<Long> previousList = existingSequence;

        do {
            final List<Long> newDiffs = new ArrayList<>();
            final int sizeOfPreviousListExceptLast = previousList.size() - 1;

            for (int i = 0; i < sizeOfPreviousListExceptLast; i++) {
                newDiffs.add(previousList.get(i + 1) - previousList.get(i));
            }

            previousList = newDiffs;
            allDiffs.add(newDiffs);
        } while (containsAnyNotAllowedValue(allDiffs.getLast(), 0L));

        final List<Long> lastDiffs = allDiffs.getLast();
        additionConsumer.accept(lastDiffs, 0L);
        final int allDiffsSizeExceptLast = allDiffs.size() - 1;

        for (int i = allDiffsSizeExceptLast; i > 0; i--) {
            final List<Long> lastRemainingDiffs = allDiffs.get(i);
            final List<Long> secondLastRemainingDiffs = allDiffs.get(i - 1);

            final long currentLastRemainingDiff = retrievalFunction.applyAsLong(lastRemainingDiffs);
            final long currentSecondLastRemainingDiff = retrievalFunction.applyAsLong(secondLastRemainingDiffs);

            final long newDiff = operation.calculate(currentSecondLastRemainingDiff, currentLastRemainingDiff);
            additionConsumer.accept(secondLastRemainingDiffs, newDiff);
        }

        final List<Long> lastRemainingDiffs = allDiffs.getFirst();
        final long currentLastRemainingDiff = retrievalFunction.applyAsLong(lastRemainingDiffs);
        final long currentLastValue = retrievalFunction.applyAsLong(existingSequence);

        return operation.calculate(currentLastValue, currentLastRemainingDiff);
    }
}
