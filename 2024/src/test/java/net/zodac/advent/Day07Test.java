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
import java.util.Set;
import net.zodac.advent.input.InputReader;
import net.zodac.advent.math.MathOperation;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day07}.
 */
class Day07Test {

    private static final String INPUT_FILENAME = "day07.txt";

    @Test
    void example() {
        final List<String> values = InputReader
            .forExample(INPUT_FILENAME)
            .asStrings()
            .readAllLines();

        final long part1Result = Day07.sumValidResults(values, Set.of(MathOperation.ADD, MathOperation.MULTIPLY));
        assertThat(part1Result)
            .isEqualTo(3_749L);

        final long part2Result = Day07.sumValidResults(values, Set.of(MathOperation.ADD, MathOperation.MULTIPLY, MathOperation.CONCATENATE));
        assertThat(part2Result)
            .isEqualTo(11_387L);
    }

    @Test
    void part1() {
        final List<String> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .readAllLines();

        final Set<MathOperation> mathOperations = Set.of(MathOperation.ADD, MathOperation.MULTIPLY);
        final long part1Result = Day07.sumValidResults(values, mathOperations);
        assertThat(part1Result)
            .isEqualTo(932_137_732_557L);
    }

    @Test
    void part2() {
        final List<String> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .readAllLines();

        final Set<MathOperation> mathOperations = Set.of(MathOperation.ADD, MathOperation.MULTIPLY, MathOperation.CONCATENATE);
        final long part2Result = Day07.sumValidResults(values, mathOperations);
        assertThat(part2Result)
            .isEqualTo(661_823_605_105_500L);
    }
}
