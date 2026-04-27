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
import net.zodac.advent.util.MathUtils;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2023, Day 09.
 *
 * @see <a href="https://adventofcode.com/2023/day/9">[2023: 09] Mirage Maintenance</a>
 */
public final class Day09 {

    private Day09() {

    }

    /**
     * Given a {@link Collection} of {@link String}s, where each {@link String} is a numeric sequence, we calculate the next value for each sequence,
     * then sum these new values up.
     *
     * @param numericSequences the input numeric sequences
     * @return the sum of the next value for each sequence
     * @see MathUtils#nextValueInSequence(List)
     */
    public static long sumOfNextValues(final Collection<String> numericSequences) {
        return numericSequences
            .stream()
            .map(StringUtils::collectNumbersInOrder)
            .mapToLong(MathUtils::nextValueInSequence)
            .sum();
    }

    /**
     * Given a {@link Collection} of {@link String}s, where each {@link String} is a numeric sequence, we calculate the previous value for each
     * sequence, then sum these new values up.
     *
     * @param numericSequences the input numeric sequences
     * @return the sum of the previous value for each sequence
     * @see MathUtils#previousValueInSequence(List)
     */
    public static long sumOfPreviousValues(final Collection<String> numericSequences) {
        return numericSequences
            .stream()
            .map(StringUtils::collectNumbersInOrder)
            .mapToLong(MathUtils::previousValueInSequence)
            .sum();
    }
}
