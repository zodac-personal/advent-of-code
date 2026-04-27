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
import java.util.Set;
import net.zodac.advent.math.MathOperation;
import net.zodac.advent.util.PermutationUtils;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2024, Day 7.
 *
 * @see <a href="https://adventofcode.com/2024/day/7">[2024: 07] Bridge Repair</a>
 */
public final class Day07 {

    private Day07() {

    }

    /**
     * Given a {@link List} of {@link String}s, where each line is a result with input values in the form:
     * <pre>
     *     result: value1 value2 value3
     * </pre>
     *
     * <p>
     * There are no operators, so we must use the supplied {@link MathOperation}s and build all possible combinations with all operators. We find each
     * row that has at least <b>1</b> valid result. The valid results are then summed up and returned.
     *
     * @param values         the input values
     * @param mathOperations the operations to use to build possible combinations
     * @return the sum of all valid results
     * @see PermutationUtils#generateWithSeparators(List, Set)
     */
    public static long sumValidResults(final Collection<String> values, final Set<MathOperation> mathOperations) {
        long total = 0L;

        for (final String value : values) {
            final List<Long> numbers = StringUtils.collectNumbersInOrder(value);
            final long result = numbers.getFirst();
            final List<Long> inputs = numbers.subList(1, numbers.size());

            total += PermutationUtils.generateWithSeparators(inputs, mathOperations)
                .parallelStream()
                .map(Day07::evaluate)
                .filter(evaluatedResult -> result == evaluatedResult)
                .findAny()
                .orElse(0L);
        }
        return total;
    }

    private static long evaluate(final List<String> input) {
        long currentTotal = Long.parseLong(input.getFirst());

        MathOperation mathOperation = MathOperation.ADD;
        final int size = input.size();
        for (int i = 1; i < size; i++) {
            final String nextSymbol = input.get(i);

            switch (nextSymbol) {
                case "+" -> mathOperation = MathOperation.ADD;
                case "*" -> mathOperation = MathOperation.MULTIPLY;
                case "||" -> mathOperation = MathOperation.CONCATENATE;
                default -> {
                    final long nextValue = Long.parseLong(nextSymbol);
                    currentTotal = mathOperation.calculate(currentTotal, nextValue);
                }
            }
        }

        return currentTotal;
    }
}
