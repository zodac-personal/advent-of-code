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
 * Tests to verify answers for {@link Day05}.
 */
class Day05Test {

    private static final String INPUT_FILENAME = "day05.txt";

    @Test
    void example() {
        final List<List<String>> values = InputReader
            .forExample(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isEmpty);

        final long part1Result = Day05.sumMiddleValuesOfSortedPageNumbers(values.getFirst(), values.getLast());
        assertThat(part1Result)
            .isEqualTo(143L);

        final long part2Result = Day05.sumMiddleValuesOfReSortedPageNumbers(values.getFirst(), values.getLast());
        assertThat(part2Result)
            .isEqualTo(123L);
    }

    @Test
    void part1() {
        final List<List<String>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isEmpty);

        final long part1Result = Day05.sumMiddleValuesOfSortedPageNumbers(values.getFirst(), values.getLast());
        assertThat(part1Result)
            .isEqualTo(4_814L);
    }

    @Test
    void part2() {
        final List<List<String>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isEmpty);

        final long part2Result = Day05.sumMiddleValuesOfReSortedPageNumbers(values.getFirst(), values.getLast());
        assertThat(part2Result)
            .isEqualTo(5_448L);
    }
}
