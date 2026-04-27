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
import net.zodac.advent.math.SimultaneousEquation;
import net.zodac.advent.util.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day13}.
 */
class Day13Test {

    private static final String INPUT_FILENAME = "day13.txt";
    private static final long RESULT_OFFSET_PART_1 = 0L;
    private static final long RESULT_OFFSET_PART_2 = 10_000_000_000_000L;

    @Test
    void example() {
        final List<SimultaneousEquation> valuesPart1 = InputReader
            .forExample(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank)
            .stream()
            .map((List<String> input) -> parseToSimultaneousEquation(input, RESULT_OFFSET_PART_1))
            .toList();

        final long part1Result = Day13.part1(valuesPart1);
        assertThat(part1Result)
            .isEqualTo(480L);

        final List<SimultaneousEquation> valuesPart2 = InputReader
            .forExample(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank)
            .stream()
            .map((List<String> input) -> parseToSimultaneousEquation(input, RESULT_OFFSET_PART_2))
            .toList();

        final long part2Result = Day13.part1(valuesPart2);
        assertThat(part2Result)
            .isEqualTo(875318608908L);
    }

    @Test
    void part1() {
        final List<SimultaneousEquation> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank)
            .stream()
            .map((List<String> input) -> parseToSimultaneousEquation(input, RESULT_OFFSET_PART_1))
            .toList();

        final long part1Result = Day13.part1(values);
        assertThat(part1Result)
            .isEqualTo(27_157L);
    }

    @Test
    void part2() {
        final List<SimultaneousEquation> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank)
            .stream()
            .map((List<String> input) -> parseToSimultaneousEquation(input, RESULT_OFFSET_PART_2))
            .toList();

        final long part2Result = Day13.part1(values);
        assertThat(part2Result)
            .isEqualTo(104_015_411_578_548L);
    }

    private static SimultaneousEquation parseToSimultaneousEquation(final List<String> input, final long resultOffset) {
        final List<Long> first = StringUtils.collectNumbersInOrder(input.getFirst());
        final List<Long> second = StringUtils.collectNumbersInOrder(input.get(1));
        final List<Long> result = StringUtils.collectNumbersInOrder(input.getLast());

        final long firstResult = result.getFirst() + resultOffset;
        final long secondResult = result.getLast() + resultOffset;

        return new SimultaneousEquation(first.getFirst(), second.getFirst(), firstResult, first.getLast(), second.getLast(), secondResult);
    }
}
