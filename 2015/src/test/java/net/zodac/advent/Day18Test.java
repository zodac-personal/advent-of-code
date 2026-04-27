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

import static org.assertj.core.api.Assertions.assertThat;

import net.zodac.advent.grid.Grid;
import net.zodac.advent.input.InputReader;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day18}.
 */
class Day18Test {

    private static final String INPUT_FILENAME = "day18.txt";
    private static final char SYMBOL_SIGNIFYING_TRUE = '#';

    @Test
    void example() {
        final Grid<Boolean> booleanGrid = InputReader
            .forExample(INPUT_FILENAME)
            .asGrid()
            .ofBooleans(SYMBOL_SIGNIFYING_TRUE);

        final long part1Result = Day18.playGameOfLife(booleanGrid, 4, false);
        assertThat(part1Result)
            .isEqualTo(4L);

        final long part2Result = Day18.playGameOfLife(booleanGrid, 5, true);
        assertThat(part2Result)
            .isEqualTo(17L);
    }

    @Test
    void part1() {
        final Grid<Boolean> booleanGrid = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asGrid()
            .ofBooleans(SYMBOL_SIGNIFYING_TRUE);

        final long part1Result = Day18.playGameOfLife(booleanGrid, 100, false);
        assertThat(part1Result)
            .isEqualTo(814L);
    }

    @Test
    void part2() {
        final Grid<Boolean> booleanGrid = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asGrid()
            .ofBooleans(SYMBOL_SIGNIFYING_TRUE);

        final long part2Result = Day18.playGameOfLife(booleanGrid, 100, true);
        assertThat(part2Result)
            .isEqualTo(924L);
    }
}
