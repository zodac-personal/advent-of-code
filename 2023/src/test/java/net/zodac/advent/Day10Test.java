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
import net.zodac.advent.pojo.Pipe;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day10}.
 */
class Day10Test {

    private static final String INPUT_FILENAME = "day10.txt";
    private static final String INPUT_FILENAME_PART_2 = "day10_2.txt";

    @Test
    void example() {
        final Grid<Pipe> pipeGrid1 = InputReader
            .forExample(INPUT_FILENAME)
            .asGrid()
            .of(Pipe::get);

        final long part1Result = Day10.numberOfStepsToFurthestPartOfLoop(pipeGrid1);
        assertThat(part1Result)
            .isEqualTo(8L);

        final Grid<Pipe> pipeGrid2 = InputReader
            .forExample(INPUT_FILENAME_PART_2)
            .asGrid()
            .of(Pipe::get);

        final long part2Result = Day10.numberOfPointsInsidePipeLoop(pipeGrid2);
        assertThat(part2Result)
            .isEqualTo(10L);
    }

    @Test
    void part1() {
        final Grid<Pipe> pipeGrid = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asGrid()
            .of(Pipe::get);

        final long part1Result = Day10.numberOfStepsToFurthestPartOfLoop(pipeGrid);
        assertThat(part1Result)
            .isEqualTo(6_812L);
    }

    @Test
    void part2() {
        final Grid<Pipe> pipeGrid = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asGrid()
            .of(Pipe::get);

        final long part2Result = Day10.numberOfPointsInsidePipeLoop(pipeGrid);
        assertThat(part2Result)
            .isEqualTo(527L);
    }
}
