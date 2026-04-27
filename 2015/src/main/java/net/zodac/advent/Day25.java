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

import net.zodac.advent.util.MathUtils;

/**
 * Solution for 2015, Day 25.
 *
 * @see <a href="https://adventofcode.com/2015/day/25">AoC 2015, Day 25</a>
 */
public final class Day25 {

    private static final long START_CODE = 20_151_125L;
    private static final long MULTIPLICAND = 252_533L;
    private static final long DIVISOR = 33_554_393L;

    private Day25() {

    }

    /**
     * Calculates the instruction manual code for the given {@code row} and {@code column}. The code is found by traversing an infinite, 2D array
     * diagonally. The first entry is {@value #START_CODE}, and each subsequent code is calculated as:
     * <pre>
     *     (currentCode * 252,533) % 33,554,393 [the % is the remainder operator, not modulo)
     * </pre>
     *
     * @param row    the row
     * @param column the column
     * @return the calculated instruction manual code
     */
    public static long calculateInstructionManualCode(final int row, final int column) {
        final long range = MathUtils.diagonalSum(row, column);

        long instructionManualCode = START_CODE;
        for (int i = 0; i < range; i++) {
            instructionManualCode = (instructionManualCode * MULTIPLICAND) % DIVISOR;
        }

        return instructionManualCode;
    }
}
