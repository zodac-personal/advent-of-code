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
import net.zodac.advent.pojo.Signal;
import net.zodac.advent.pojo.SignalDecoder;

/**
 * Solution for 2021, Day 8.
 *
 * @see <a href="https://adventofcode.com/2021/day/8">AoC 2021, Day 8</a>
 */
public final class Day08 {

    private Day08() {

    }

    /**
     * Provided a {@link List} of {@link Signal}s, count the total number of unique output values.
     *
     * @param signals the {@link Signal}s to check
     * @return the sum of unique output values
     * @see SignalDecoder#isUniqueOutputValue(String)
     */
    public static long identifyUniqueOutputValues(final Collection<Signal> signals) {
        return signals
            .stream()
            .map(Signal::outputs)
            .flatMap(List::stream)
            .filter(SignalDecoder::isUniqueOutputValue)
            .count();
    }

    /**
     * Provided a {@link List} of {@link Signal}s, for each {@link Signal} we use the inputs values to generate a {@link SignalDecoder} that can be
     * used to decode the output value for that {@link Signal}. We then sum all the decoded outputs.
     *
     * @param signals the {@link Signal}s to decode
     * @return the sum of the decoded {@link Signal} values
     * @see SignalDecoder#decode(Signal)
     */
    public static long sumOfDecodedOutputs(final Collection<Signal> signals) {
        return signals
            .stream()
            .mapToLong(SignalDecoder::decode)
            .sum();
    }
}
