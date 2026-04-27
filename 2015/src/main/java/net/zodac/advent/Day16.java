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
import net.zodac.advent.pojo.Sue;

/**
 * Solution for 2015, Day 16.
 *
 * @see <a href="https://adventofcode.com/2015/day/16">AoC 2015, Day 16</a>
 */
public final class Day16 {

    private static final Sue WANTED_SUE = Sue.parseAllAttributes("""
        children: 3
        cats: 7
        samoyeds: 2
        pomeranians: 3
        akitas: 0
        vizslas: 0
        goldfish: 5
        trees: 3
        cars: 2
        perfumes: 1""");

    private Day16() {

    }

    /**
     * For the {@link Collection} of provided {@link Sue}s, we will compare each and see if it is a matching {@link Sue} with the {@code WANTED_SUE}.
     *
     * @param partialSues     the {@link Sue}s to check
     * @param matchWithRanges whether to match {@link Sue}s directly, or with a range of values
     * @return the ID of the matching {@link Sue}, or of {@code WANTED_SUE} if no match is found
     */
    public static long findIdOfMatchingSue(final Collection<Sue> partialSues, final boolean matchWithRanges) {
        return partialSues
            .stream()
            .filter(otherSue -> WANTED_SUE.isMatch(otherSue, matchWithRanges))
            .findFirst()
            .orElse(WANTED_SUE)
            .id();
    }
}
