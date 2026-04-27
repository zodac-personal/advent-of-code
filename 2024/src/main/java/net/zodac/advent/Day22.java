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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.zodac.advent.math.BitwiseOperation;

/**
 * Solution for 2024, Day 22.
 *
 * @see <a href="https://adventofcode.com/2024/day/22">[2024: 22] Monkey Market</a>
 */
public final class Day22 {

    private static final int STEP_1_MULTIPLICATION_CONSTANT = 6;  // 2^6 = 64
    private static final int STEP_3_MULTIPLICATION_CONSTANT = 11; // 2^11 = 2048
    private static final int DIVISION_CONSTANT = 32;
    private static final long PRUNE_CONSTANT = 16_777_216L;
    private static final int SECRET_NUMBER_INDEX_TO_FIND = 2_000;

    // Radix values for part2 speed up
    private static final int DIFF_RADIX = 19;
    private static final int DIFF_RADIX_CUBED = DIFF_RADIX * DIFF_RADIX * DIFF_RADIX;
    private static final int DIFF_RADIX_OFFSET = 9;

    private Day22() {

    }

    /**
     * Given a {@link Collection} of initial secret numbers, we must calculate the remaining secret numbers in the sequence. We then find the
     * {@value #SECRET_NUMBER_INDEX_TO_FIND}th secret number, and sum them all.
     *
     * @param initialSecretNumbers the initial secret numbers to generate the remaining sequence
     * @return the sum of {@value #SECRET_NUMBER_INDEX_TO_FIND}th secret numbers
     */
    public static long calculateSumOf2000thSecretNumbers(final Collection<Integer> initialSecretNumbers) {
        final Map<Integer, List<Long>> secretNumbersByInitial = generateSecretNumbers(initialSecretNumbers);

        return secretNumbersByInitial
            .values()
            .stream()
            .mapToLong(List::getLast)
            .sum();
    }

    /**
     * Given a {@link Collection} of initial secret numbers, we must calculate the remaining secret numbers in the sequence. We then find the
     * {@value #SECRET_NUMBER_INDEX_TO_FIND}th secret number. For each secret number, we retrieve the 'price', which is the last digit of the number.
     * We also get a running diff of the prices for the full sequence of secret numbers.
     *
     * <p>
     * For each sequence of 4 running diffs, we want to find the sequence that returns the greatest value. The first price of the sequence is
     * considered for each {@code initialSecretNumber}, and each value is added together. We should find the maximum number of the secret numbers
     * possible, considering all possible 4-runs of diffs. This will be the maximum number of bananas to be requested.
     *
     * @param initialSecretNumbers the initial secret numbers to generate the remaining sequence
     * @return the maximum number of bananas
     */
    public static long calculateMaximumPossibleBananas(final Collection<Integer> initialSecretNumbers) {
        final Map<Integer, List<Long>> secretNumbersByInitial = generateSecretNumbers(initialSecretNumbers);

        final Map<Long, Long> numberOfBananasByDiff = new HashMap<>();
        for (final Map.Entry<Integer, List<Long>> entry : secretNumbersByInitial.entrySet()) {
            final List<Long> secretNumbers = entry.getValue();
            final List<Long> prices = secretNumbers.stream().map(l -> l % 10L).toList();

            final Collection<Long> existingDiffs = new HashSet<>();

            final int numberOfPrices = prices.size();
            long runningDiff = 0L;
            for (int i = 1; i < numberOfPrices; i++) {
                // Diff calculated based on following logic, gives a concise value for the diff that can be used for comparisons
                // https://github.com/p-kovacs/advent-of-code-2024/blob/master/src/main/java/com/github/pkovacs/aoc/y2024/Day22.java#L33
                runningDiff = (runningDiff % DIFF_RADIX_CUBED) * DIFF_RADIX + (prices.get(i) - prices.get(i - 1) + DIFF_RADIX_OFFSET);

                // We don't want to include the diff until there are at least 4 values, but removing the check still gives the correct result
                if (existingDiffs.add(runningDiff)) {
                    numberOfBananasByDiff.merge(runningDiff, prices.get(i), Long::sum);
                }
            }
        }

        return numberOfBananasByDiff
            .values()
            .stream()
            .mapToLong(Long::longValue)
            .max()
            .orElse(0L);
    }

    private static Map<Integer, List<Long>> generateSecretNumbers(final Collection<Integer> initialSecretNumbers) {
        final Map<Integer, List<Long>> secretNumbersByInitial = new HashMap<>();
        for (final int initialSecretNumber : initialSecretNumbers) {
            final List<Long> secretNumbers = calculateFinalSecretNumber(initialSecretNumber);
            secretNumbersByInitial.put(initialSecretNumber, secretNumbers);
        }
        return secretNumbersByInitial;
    }

    private static List<Long> calculateFinalSecretNumber(final long initialSecretNumber) {
        final List<Long> secretNumbers = new ArrayList<>();
        secretNumbers.add(initialSecretNumber);

        long secretNumber = initialSecretNumber;
        for (int i = 0; i < SECRET_NUMBER_INDEX_TO_FIND; i++) {
            secretNumber = generateNextSecretNumber(secretNumber);
            secretNumbers.add(secretNumber);
        }

        return secretNumbers;
    }

    private static long generateNextSecretNumber(final long secretNumber) {
        final long step1Result = step1(secretNumber);
        final long step2Result = step2(step1Result);
        return step3(step2Result);
    }

    private static long step1(final long secretNumber) {
        final long multiplicationResult = multiply(secretNumber, true);
        final long mixedResult = mix(multiplicationResult, secretNumber);
        return prune(mixedResult);
    }

    private static long step2(final long secretNumber) {
        final long divisionResult = divide(secretNumber);
        final long mixedResult = mix(divisionResult, secretNumber);
        return prune(mixedResult);
    }

    private static long step3(final long secretNumber) {
        final long multiplicationResult = multiply(secretNumber, false);
        final long mixedResult = mix(multiplicationResult, secretNumber);
        return prune(mixedResult);
    }

    private static long multiply(final long input, final boolean isStep1) {
        return BitwiseOperation.LSHIFT.calculate(input, (isStep1 ? STEP_1_MULTIPLICATION_CONSTANT : STEP_3_MULTIPLICATION_CONSTANT));
    }

    private static long divide(final long input) {
        return Math.floorDiv(input, DIVISION_CONSTANT);
    }

    private static long mix(final long first, final long second) {
        return BitwiseOperation.XOR.calculate(first, second);
    }

    private static long prune(final long input) {
        return input % PRUNE_CONSTANT;
    }
}
