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
import java.util.regex.Pattern;
import net.zodac.advent.input.InputReader;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day21}.
 */
class Day21Test {

    private static final String INPUT_FILENAME = "day21.txt";
    private static final Pattern COLON_PATTERN = Pattern.compile(":");

    @Test
    void example() {
        final List<Integer> startPositions = InputReader
            .forExample(INPUT_FILENAME)
            .as(input -> COLON_PATTERN.split(input, 2)[1].trim())
            .as(Integer::parseInt)
            .readAllLines();

        final long part1Result = Day21.getLosingScoreTimesNumberOfRolls(startPositions, 3, 100, 10, 1_000L);
        assertThat(part1Result)
            .isEqualTo(739_785L);
    }

    @Test
    void part1() {
        final List<Integer> startPositions = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(input -> COLON_PATTERN.split(input, 2)[1].trim())
            .as(Integer::parseInt)
            .readAllLines();

        final long part1Result = Day21.getLosingScoreTimesNumberOfRolls(startPositions, 3, 100, 10, 1_000L);
        assertThat(part1Result)
            .isEqualTo(797_160L);
    }
}
