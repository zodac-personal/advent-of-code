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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.zodac.advent.grid.Point;
import net.zodac.advent.input.InputReader;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day25}.
 */
class Day25Test {

    private static final String INPUT_FILENAME = "day25.txt";
    private static final Pattern INPUT_PATTERN = Pattern.compile(".*Enter the code at row (\\d+), column (\\d+).*");

    @Test
    void example() {
        final Point point = InputReader
            .forExample(INPUT_FILENAME)
            .as(Day25Test::getPoint)
            .readFirstLine();

        final long part1Result = Day25.calculateInstructionManualCode(point.x(), point.y());
        assertThat(part1Result)
            .isEqualTo(32_451_966L);
    }

    @Test
    void part1() {
        final Point point = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(Day25Test::getPoint)
            .readFirstLine();

        final long part1Result = Day25.calculateInstructionManualCode(point.x(), point.y());
        assertThat(part1Result)
            .isEqualTo(19_980_801L);
    }

    private static Point getPoint(final String input) {
        final Matcher matcher = INPUT_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalStateException("Unable to find match in input: " + input);
        }

        final int row = Integer.parseInt(matcher.group(1));
        final int column = Integer.parseInt(matcher.group(2));
        return Point.of(row, column);
    }
}
