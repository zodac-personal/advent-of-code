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
import java.util.List;
import java.util.SequencedCollection;
import net.zodac.advent.pojo.BitParityCount;
import net.zodac.advent.util.BinaryConversionUtils;

/**
 * Solution for 2021, Day 3.
 *
 * @see <a href="https://adventofcode.com/2021/day/3">AoC 2021, Day 3</a>
 */
public final class Day03 {

    private static final int EXPECTED_NUMBER_OF_VALID_RATINGS = 1;

    private Day03() {

    }

    /**
     * Given an input {@link List} of binary values, we calculate the most common bit and least common bit per index. We then reconstitute these
     * most or least common bits to calculate the gamma (the most common bits) and epsilon (the least common bits) rates of the input.
     *
     * <p>
     * For example, take the input:
     * <pre>
     *     01101
     *     10010
     *     10000
     *     11000
     *     00010
     * </pre>
     *
     * <p>
     * We can calculate the gamma rate as:
     * <pre>
     *     10000
     * </pre>
     *
     * <p>
     * And the epsilon rate as:
     * <pre>
     *     01111
     * </pre>
     *
     * <p>
     * We can then convert these binary values to decimal values, and multiply them to determine the power consumption.
     *
     * @param binaryValues the values to be checked
     * @return the power consumption
     * @see BinaryConversionUtils
     * @see BitParityCount#createForIndexOfBinaryValues(Collection, int)
     */
    public static long calculatePowerConsumption(final SequencedCollection<String> binaryValues) {
        if (binaryValues.isEmpty()) {
            return 0L;
        }

        // Input should have 12 digits, but no harm being a bit flexible
        final int lengthOfBinaryValue = binaryValues.getFirst().length();

        final StringBuilder gammaRate = new StringBuilder(lengthOfBinaryValue);
        final StringBuilder epsilonRate = new StringBuilder(lengthOfBinaryValue);

        for (int i = 0; i < lengthOfBinaryValue; i++) {
            final BitParityCount bitParityCount = BitParityCount.createForIndexOfBinaryValues(binaryValues, i);

            final char gammaValue = bitParityCount.mostCommonBit();
            final char epsilonValue = bitParityCount.leastCommonBit();

            gammaRate.append(gammaValue);
            epsilonRate.append(epsilonValue);
        }

        final long gamma = BinaryConversionUtils.toDecimal(gammaRate.toString());
        final long epsilon = BinaryConversionUtils.toDecimal(epsilonRate.toString());

        return gamma * epsilon;
    }

    /**
     * Given an input {@link List} of binary values, we calculate the most common bit and least common bit of each index, and filter out any values
     * that do not have the corresponding most or least common bit for that index.
     *
     * <p>
     * We then recalculate the most or least common bits for the next index of the remaining binary values, and continue until there is only one
     * binary value remaining. This value is either the oxygen rating (the most common bits) or carbon dioxide rating (the least common bits).
     *
     * <p>
     * For example, take the input:
     * <pre>
     *     01101
     *     10010
     *     10000
     *     11000
     *     00010
     * </pre>
     *
     * <p>
     * For the first index, the most common bit is <b>1</b>, so we filter the list to:
     * <pre>
     *     10010
     *     10000
     *     11000
     * </pre>
     *
     * <p>
     * For the second index, the most common bit is <b>0</b>, so the list is filtered to:
     * <pre>
     *     10010
     *     10000
     * </pre>
     * The third index is the same for both values, and the fourth index has a common bit of <b>1</b> (the default for ties for common bit). So the
     * final value will be <b>10010</b>, which converts to decimal <b>18</b> for the oxygen rating.
     *
     * <p>
     * Similarly, we instead use the least common bit. The final value will be <b>00010</b>, for a decimal <b>2</b> for the carbon dioxide rating.
     *
     * <p>
     * We then multiply these values to determine the life support rating.
     *
     * @param binaryValues the values to be checked
     * @return the life support rating
     * @see BinaryConversionUtils
     * @see BitParityCount#createForIndexOfBinaryValues(Collection, int)
     */
    public static long calculateLifeSupportRating(final SequencedCollection<String> binaryValues) {
        if (binaryValues.isEmpty()) {
            return 0L;
        }

        final long oxygenRating = getOxygenRating(binaryValues);
        final long carbonDioxideRating = getCarbonDioxideRating(binaryValues);

        return oxygenRating * carbonDioxideRating;
    }

    private static long getOxygenRating(final SequencedCollection<String> binaryValues) {
        return getRating(binaryValues, true);
    }

    private static long getCarbonDioxideRating(final SequencedCollection<String> binaryValues) {
        return getRating(binaryValues, false);
    }

    private static long getRating(final SequencedCollection<String> binaryValues, final boolean mostCommon) {
        final int lengthOfBinaryValue = binaryValues.getFirst().length();
        List<String> validRatings = new ArrayList<>(binaryValues);

        for (int i = 0; i < lengthOfBinaryValue; i++) {
            final BitParityCount bitParityCount = BitParityCount.createForIndexOfBinaryValues(validRatings, i);
            final char common = mostCommon ? bitParityCount.mostCommonBit() : bitParityCount.leastCommonBit();
            final List<String> nextValidValues = new ArrayList<>();

            for (final String validValue : validRatings) {
                final char ithBit = validValue.charAt(i);

                if (ithBit == common) {
                    nextValidValues.add(validValue);
                }
            }

            validRatings = nextValidValues;
            if (validRatings.size() == EXPECTED_NUMBER_OF_VALID_RATINGS) {
                break;
            }
        }

        if (validRatings.size() != EXPECTED_NUMBER_OF_VALID_RATINGS) {
            throw new IllegalStateException("Expected there to be only 1 valid rating, found: " + validRatings.size());
        }
        return BinaryConversionUtils.toDecimal(validRatings.getFirst());
    }
}


