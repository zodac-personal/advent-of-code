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
import net.zodac.advent.util.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day04}.
 */
class Day04Test {

    private static final String INPUT_FILENAME = "day04.txt";

    @Test
    void example() {
        // TODO: Input could probably be modelled better? Group then convert?
        final List<String> bingoInput = InputReader
            .forExample(INPUT_FILENAME)
            .excludeBlankLines()
            .asStrings()
            .readAllLines();

        final List<Integer> bingoNumbers = StringUtils.collectNumbersInOrder(bingoInput.getFirst())
            .stream()
            .map(Long::intValue)
            .toList();

        // Remove the first entry
        final List<String> bingoBoardsInput = bingoInput.subList(1, bingoInput.size());

        final long part1Result = Day04.finalScoreOfFirstWinningBingoBoard(bingoNumbers, bingoBoardsInput);
        assertThat(part1Result)
            .isEqualTo(4_512L);

        final long part2Result = Day04.finalScoreOfLastWinningBingoBoard(bingoNumbers, bingoBoardsInput);
        assertThat(part2Result)
            .isEqualTo(1_924L);
    }

    @Test
    void part1() {
        final List<String> bingoInput = InputReader
            .forPuzzle(INPUT_FILENAME)
            .excludeBlankLines()
            .asStrings()
            .readAllLines();

        final List<Integer> bingoNumbers = StringUtils.collectNumbersInOrder(bingoInput.getFirst())
            .stream()
            .map(Long::intValue)
            .toList();

        // Remove the first entry
        final List<String> bingoBoardsInput = bingoInput.subList(1, bingoInput.size());

        final long part1Result = Day04.finalScoreOfFirstWinningBingoBoard(bingoNumbers, bingoBoardsInput);
        assertThat(part1Result)
            .isEqualTo(45_031L);
    }

    @Test
    void part2() {
        final List<String> bingoInput = InputReader
            .forPuzzle(INPUT_FILENAME)
            .excludeBlankLines()
            .asStrings()
            .readAllLines();

        final List<Integer> bingoNumbers = StringUtils.collectNumbersInOrder(bingoInput.getFirst())
            .stream()
            .map(Long::intValue)
            .toList();

        // Remove the first entry
        final List<String> bingoBoardsInput = bingoInput.subList(1, bingoInput.size());

        final long part2Result = Day04.finalScoreOfLastWinningBingoBoard(bingoNumbers, bingoBoardsInput);
        assertThat(part2Result)
            .isEqualTo(2_568L);
    }
}
