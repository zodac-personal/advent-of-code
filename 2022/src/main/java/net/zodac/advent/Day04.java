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
import net.zodac.advent.pojo.Range;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * Solution for 2022, Day 4.
 *
 * @see <a href="https://adventofcode.com/2022/day/4">AoC 2022, Day 4</a>
 */
public final class Day04 {

    private Day04() {

    }

    /**
     * Given an input of {@link Pair}s of {@link Range}s, we count how many {@link Range}s are completely overlapped by their pair.
     *
     * @param values the {@link Range}s
     * @return the number of complete overlaps
     * @see Range#hasCompleteOverlap(Range)
     */
    public static long countCompleteOverlaps(final Collection<Pair<Range, Range>> values) {
        return values
            .stream()
            .filter(pair -> pair.first().hasCompleteOverlap(pair.second()))
            .count();
    }

    /**
     * Given an input of {@link Pair}s of {@link Range}s, we count how many ranges are partially overlapped by their pair.
     *
     * @param values the {@link Range}s
     * @return the number of partial overlaps
     * @see Range#hasPartialOverlap(Range)
     */
    public static long countPartialOverlaps(final Collection<Pair<Range, Range>> values) {
        return values
            .stream()
            .filter(pair -> pair.first().hasPartialOverlap(pair.second()))
            .count();
    }
}
