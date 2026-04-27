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

import java.util.List;
import net.zodac.advent.input.InputReader;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day22}.
 */
class Day22Test {

    private static final String INPUT_FILENAME = "day22.txt";
    private static final String INPUT_FILENAME_PART_2 = "day22_2.txt";

    @Test
    void example() {
        final List<Integer> valuesPart1 = InputReader
            .forExample(INPUT_FILENAME)
            .asIntegers()
            .readAllLines();

        final long part1Result = Day22.calculateSumOf2000thSecretNumbers(valuesPart1);
        assertThat(part1Result)
            .isEqualTo(37_327_623L);

        final List<Integer> valuesPart2 = InputReader
            .forExample(INPUT_FILENAME_PART_2)
            .asIntegers()
            .readAllLines();

        final long part2Result = Day22.calculateMaximumPossibleBananas(valuesPart2);
        assertThat(part2Result)
            .isEqualTo(23L);
    }

    @Test
    void part1() {
        final List<Integer> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asIntegers()
            .readAllLines();

        final long part1Result = Day22.calculateSumOf2000thSecretNumbers(values);
        assertThat(part1Result)
            .isEqualTo(14_119_253_575L);
    }

    @Test
    void part2() {
        final List<Integer> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asIntegers()
            .readAllLines();

        final long part2Result = Day22.calculateMaximumPossibleBananas(values);
        assertThat(part2Result)
            .isEqualTo(1_600L);
    }
}
