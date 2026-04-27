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
import net.zodac.advent.pojo.Signal;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day08}.
 */
class Day08Test {

    private static final String INPUT_FILENAME = "day08.txt";

    @Test
    void example() {
        final List<Signal> signals = InputReader
            .forExample(INPUT_FILENAME)
            .as(Signal::create)
            .readAllLines();

        final long part1Result = Day08.identifyUniqueOutputValues(signals);
        assertThat(part1Result)
            .isEqualTo(26L);

        final long part2Result = Day08.sumOfDecodedOutputs(signals);
        assertThat(part2Result)
            .isEqualTo(61_229L);
    }

    @Test
    void part1() {
        final List<Signal> signals = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(Signal::create)
            .readAllLines();

        final long part1Result = Day08.identifyUniqueOutputValues(signals);
        assertThat(part1Result)
            .isEqualTo(245L);
    }

    @Test
    void part2() {
        final List<Signal> signals = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(Signal::create)
            .readAllLines();

        final long part2Result = Day08.sumOfDecodedOutputs(signals);
        assertThat(part2Result)
            .isEqualTo(983_026L);
    }
}
