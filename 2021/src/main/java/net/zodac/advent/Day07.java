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
import java.util.Collections;
import java.util.List;
import net.zodac.advent.pojo.MoveCostType;

/**
 * Solution for 2021, Day 6.
 *
 * @see <a href="https://adventofcode.com/2021/day/7">AoC 2021, Day 7</a>
 */
public final class Day07 {

    private Day07() {

    }

    /**
     * Given an input {@link List} of {@link Integer} values defining a number of vertical locations, we attempt to find the minimum distance for all
     * values to align to a given location.
     *
     * <p>
     * The cost for the moves are defined by {@link MoveCostType}.
     *
     * @param locations    the locations to align vertically
     * @param moveCostType the {@link MoveCostType} per move
     * @return the minimum moves required to align to the ideal vertical location
     */
    public static long minimumMovesNeededToAlignVertically(final Collection<Long> locations, final MoveCostType moveCostType) {
        final long minValue = Collections.min(locations);
        final long maxValue = Collections.max(locations);
        long minimumNumberOfMoves = Long.MAX_VALUE;

        for (long possibleAlignmentLocation = minValue; possibleAlignmentLocation < maxValue; possibleAlignmentLocation++) {
            long movedNeeded = 0L;

            for (final long location : locations) {
                movedNeeded += moveCostType.costForMove(location, possibleAlignmentLocation);
            }

            if (movedNeeded < minimumNumberOfMoves) {
                minimumNumberOfMoves = movedNeeded;
            }
        }

        return minimumNumberOfMoves;
    }
}
