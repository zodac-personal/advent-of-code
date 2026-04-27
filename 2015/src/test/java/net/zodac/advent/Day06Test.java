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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.zodac.advent.grid.Point;
import net.zodac.advent.input.InputReader;
import net.zodac.advent.pojo.GridUpdateInstruction;
import net.zodac.advent.pojo.tuple.Triple;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day06}.
 */
class Day06Test {

    private static final String INPUT_FILENAME = "day06.txt";
    private static final Pattern INSTRUCTION_AND_POINTS_PATTERN = Pattern.compile("(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)");

    @Test
    void example() {
        final List<Triple<GridUpdateInstruction, Point, Point>> values = InputReader
            .forExample(INPUT_FILENAME)
            .as(Day06Test::convertToInstructionAndPoints)
            .readAllLines();

        final long part1Result = Day06.countSwitchedOnLights(values);
        assertThat(part1Result)
            .isEqualTo(1_000_000L);

        final long part2Result = Day06.calculateBrightness(values);
        assertThat(part2Result)
            .isEqualTo(2_000_000L);
    }

    @Test
    void part1() {
        final List<Triple<GridUpdateInstruction, Point, Point>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(Day06Test::convertToInstructionAndPoints)
            .readAllLines();

        final long part1Result = Day06.countSwitchedOnLights(values);
        assertThat(part1Result)
            .isEqualTo(377_891L);
    }

    @Test
    void part2() {
        final List<Triple<GridUpdateInstruction, Point, Point>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(Day06Test::convertToInstructionAndPoints)
            .readAllLines();

        final long part2Result = Day06.calculateBrightness(values);
        assertThat(part2Result)
            .isEqualTo(14_110_788L);
    }

    private static Triple<GridUpdateInstruction, Point, Point> convertToInstructionAndPoints(final String input) {
        final Matcher matcher = INSTRUCTION_AND_POINTS_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalStateException("Unable to find match in input string: " + input);
        }

        final String instruction = matcher.group(1);
        final GridUpdateInstruction gridUpdateInstruction = GridUpdateInstruction.match(instruction);

        if (gridUpdateInstruction == GridUpdateInstruction.INVALID) {
            throw new IllegalStateException("Invalid instruction found: " + input);
        }

        final int x1 = Integer.parseInt(matcher.group(2));
        final int y1 = Integer.parseInt(matcher.group(3));
        final int x2 = Integer.parseInt(matcher.group(4));
        final int y2 = Integer.parseInt(matcher.group(5));

        if (x1 > x2 || y1 > y2) {
            throw new IllegalStateException(String.format("Expected (x1, y1) to be less than (x2, y2), found: (%s, %s) (%s, %s)", x1, y1, x2, y2));
        }

        return Triple.of(gridUpdateInstruction, Point.of(x1, y1), Point.of(x2, y2));
    }
}
