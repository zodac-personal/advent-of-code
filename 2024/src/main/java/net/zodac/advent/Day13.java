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
import java.util.Optional;
import net.zodac.advent.math.SimultaneousEquation;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * Solution for 2024, Day 13.
 *
 * @see <a href="https://adventofcode.com/2024/day/13">[2024: 13] Claw Contraption</a>
 */
public final class Day13 {

    private static final long MULTIPLIER_FOR_A_PRESSES = 3L;

    private Day13() {

    }

    /**
     * We are given a {@link Collection} of {@link SimultaneousEquation}s, each representing the number of <b>A</b> and <b>B</b> button presses that
     * are required in order to move a claw in a game machine to reach the prize. If a valid number of button presses exists to reach the prize, the
     * value of the valid results are calculated as:
     * <pre>
     *     (number of A presses * 3) + number of B presses
     * </pre>
     *
     * <p>
     * The values are summed and then returned.
     *
     * @param values the {@link SimultaneousEquation}s
     * @return the sum of all valid combinations
     */
    public static long part1(final Collection<SimultaneousEquation> values) {
        return values
            .stream()
            .map(SimultaneousEquation::solve)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .mapToLong(Day13::calculateValue)
            .sum();
    }

    private static long calculateValue(final Pair<Long, Long> input) {
        return (input.first() * MULTIPLIER_FOR_A_PRESSES) + input.second();
    }
}
