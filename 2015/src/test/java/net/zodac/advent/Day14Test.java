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

import java.time.Duration;
import java.util.List;
import net.zodac.advent.input.InputReader;
import net.zodac.advent.pojo.Reindeer;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day14}.
 */
class Day14Test {

    private static final String INPUT_FILENAME = "day14.txt";
    private static final Duration EXAMPLE_TRAVEL_TIME = Duration.ofSeconds(1_000);
    private static final Duration PUZZLE_TRAVEL_TIME = Duration.ofSeconds(2_503);

    @Test
    void example() {
        final List<Reindeer> values = InputReader
            .forExample(INPUT_FILENAME)
            .as(Reindeer::parse)
            .readAllLines();

        final long part1Result = Day14.distanceOfFurthestTravelledReindeer(values, EXAMPLE_TRAVEL_TIME);
        assertThat(part1Result)
            .isEqualTo(1_120L);

        final long part2Result = Day14.calculateTheHighestScore(values, EXAMPLE_TRAVEL_TIME);
        assertThat(part2Result)
            .isEqualTo(689L);
    }

    @Test
    void part1() {
        final List<Reindeer> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(Reindeer::parse)
            .readAllLines();

        final long part1Result = Day14.distanceOfFurthestTravelledReindeer(values, PUZZLE_TRAVEL_TIME);
        assertThat(part1Result)
            .isEqualTo(2_660L);
    }

    @Test
    void part2() {
        final List<Reindeer> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(Reindeer::parse)
            .readAllLines();

        final long part2Result = Day14.calculateTheHighestScore(values, PUZZLE_TRAVEL_TIME);
        assertThat(part2Result)
            .isEqualTo(1_256L);
    }
}
