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

import net.zodac.advent.util.MathUtils;

/**
 * Defines the cost of moving between two positions.
 */
public enum MoveCostType {

    /**
     * The cost for any movement is constant (<b>1</b>).
     */
    CONSTANT {
        @Override
        public long costForMove(final long startPosition, final long endPosition) {
            // We just get the distance from the current location to the possible location
            return Math.abs(startPosition - endPosition);
        }
    },

    /**
     * The cost for movement increases per move (<b>1</b>, then <b>2</b>, then <b>3</b> ...).
     */
    VARIABLE {
        @Override
        public long costForMove(final long startPosition, final long endPosition) {
            // This is 1 + 2 + 3 ... + n, described as n*(n+1)/2
            final long diff = Math.abs(startPosition - endPosition);
            return MathUtils.calculateTriangularNumberValue(diff);
        }
    };

    /**
     * Calculates the cost for the move from {@code startPosition} to {@code endPosition}.
     *
     * @param startPosition the start position
     * @param endPosition   the end position
     * @return the cost for the move
     */
    public abstract long costForMove(long startPosition, long endPosition);
}
