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

package net.zodac.advent.pojo;

import java.util.Collection;

/**
 * Simple POJO keeping track of the count of the bit parity (the '0's and '1's) of a binary {@link String}.
 *
 * @param zeros the number of zeros
 * @param ones  the number of ones
 */
public record BitParityCount(int zeros, int ones) {

    private static final char ZERO_BIT = '0';
    private static final char ONE_BIT = '1';

    /**
     * Given a {@link Collection} of {@link String} binary values, we calculate the {@link BitParityCount} for a specific {@code index} of all values.
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
     * If the index <b>0</b> is provided, the number of zero bits is <b>2</b> and the number of 'one' bits is <b>3</b>.
     * Similarly, given an index <b>4</b>, the number of zero bits is <b>4</b> and the number of 'one' bits is <b>1</b>.
     *
     * @param binaryValues the binary values to be checked
     * @param index        the index of the bit we are interested in
     * @return the {@link BitParityCount} for the index of the provided binary {@link String}s.
     */
    public static BitParityCount createForIndexOfBinaryValues(final Collection<String> binaryValues, final int index) {
        int zeros = 0;
        int ones = 0;

        for (final String value : binaryValues) {
            final char charAtIndex = value.charAt(index);

            switch (charAtIndex) {
                case ZERO_BIT -> zeros++;
                case ONE_BIT -> ones++;
                default -> throw new IllegalStateException(String.format("Cannot handle 'binary' input of: '%s'", charAtIndex));
            }
        }

        return new BitParityCount(zeros, ones);
    }

    /**
     * Finds the most common bit in the {@link BitParityCount}.
     *
     * <p>
     * Note that if the number of 0s and 1s are equal, we will return '1'.
     *
     * @return the most common bit
     */
    public char mostCommonBit() {
        return ones >= zeros ? ONE_BIT : ZERO_BIT;
    }

    /**
     * Finds the least common bit in the {@link BitParityCount}.
     *
     * <p>
     * Note that if the number of 0s and 1s are equal, we will return '0'.
     *
     * @return the least common bit
     */
    public char leastCommonBit() {
        return zeros <= ones ? ZERO_BIT : ONE_BIT;
    }
}
