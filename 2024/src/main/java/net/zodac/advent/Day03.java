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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.zodac.advent.pojo.MultiplicationOperation;

/**
 * Solution for 2024, Day 3.
 *
 * @see <a href="https://adventofcode.com/2024/day/3">[2024: 03] Mull It Over</a>
 */
public final class Day03 {

    private static final Pattern MULTIPLICATION_PATTERN = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)");
    private static final String ENABLE_OPERATION_FLAG = "do()";
    private static final String DISABLE_OPERATION_FLAG = "don't()";

    private Day03() {

    }

    /**
     * Given a {@link String} containing many {@link MultiplicationOperation}s, extract each one, multiply its values, and then sum the result of
     * each.
     *
     * @param value the input {@link String} containing the {@link MultiplicationOperation}s
     * @return the sum of all multiplied {@link MultiplicationOperation}s
     */
    public static long sumOfMultipliedOperations(final CharSequence value) {
        final Collection<MultiplicationOperation> multiplicationOperations = getMultiplicationOperations(value);

        return multiplicationOperations
            .stream()
            .mapToLong(MultiplicationOperation::multiply)
            .sum();
    }

    /**
     * Given a {@link String} containing many {@link MultiplicationOperation}s, extract each one, multiply its values, and then sum the result of
     * each. However, if prior to the {@link MultiplicationOperation} a {@value #DISABLE_OPERATION_FLAG}, all subsequent
     * {@link MultiplicationOperation}s should be ignored until a {@value #ENABLE_OPERATION_FLAG} is found.
     *
     * @param value the input {@link String} containing the {@link MultiplicationOperation}s
     * @return the sum of all valid multiplied {@link MultiplicationOperation}s
     */
    public static long sumOfValidMultipliedOperations(final String value) {
        final Collection<MultiplicationOperation> multiplicationOperations = getMultiplicationOperations(value);

        long total = 0L;
        boolean isOperationActive = true;
        String currentString = value;

        for (final MultiplicationOperation multiplicationOperation : multiplicationOperations) {
            // For each value, find the mul(a,b) value in the string, then substring everything prefixing it
            final int indexOfMultiplicationOperation = currentString.indexOf(multiplicationOperation.input());
            final String prefix = currentString.substring(0, indexOfMultiplicationOperation);

            // Assuming you won't have both flags in the same prefix, so a simple check for either
            if (prefix.contains(ENABLE_OPERATION_FLAG)) {
                isOperationActive = true;
            }

            if (prefix.contains(DISABLE_OPERATION_FLAG)) {
                isOperationActive = false;
            }

            if (isOperationActive) {
                total += multiplicationOperation.multiply();
            }

            // Cut the string up to the current operation, so we only scan the remainder of the string in the next iteration
            currentString = currentString.substring(indexOfMultiplicationOperation);
        }

        return total;
    }

    private static Collection<MultiplicationOperation> getMultiplicationOperations(final CharSequence value) {
        final Matcher matcher = MULTIPLICATION_PATTERN.matcher(value);

        final Collection<MultiplicationOperation> multiplicationOperations = new ArrayList<>();
        while (matcher.find()) {
            multiplicationOperations.add(MultiplicationOperation.parse(matcher.group()));
        }
        return multiplicationOperations;
    }
}
