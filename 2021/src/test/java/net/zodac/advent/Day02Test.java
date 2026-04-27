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
import net.zodac.advent.pojo.Movement;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day02}.
 */
class Day02Test {

    private static final String INPUT_FILENAME = "day02.txt";

    @Test
    void example() {
        final List<Movement> movements = InputReader
            .forExample(INPUT_FILENAME)
            .as(Movement::parse)
            .readAllLines();

        final long part1Result = Day02.magnitudeOfAllMovements(movements);
        assertThat(part1Result)
            .isEqualTo(150L);

        final long part2Result = Day02.magnitudeOfAllMovementsWithAim(movements);
        assertThat(part2Result)
            .isEqualTo(900L);
    }

    @Test
    void part1() {
        final List<Movement> movements = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(Movement::parse)
            .readAllLines();

        final long magnitude = Day02.magnitudeOfAllMovements(movements);
        assertThat(magnitude)
            .isEqualTo(1_813_801L);
    }

    @Test
    void part2() {
        final List<Movement> movements = InputReader
            .forPuzzle(INPUT_FILENAME)
            .as(Movement::parse)
            .readAllLines();

        final long magnitude = Day02.magnitudeOfAllMovementsWithAim(movements);
        assertThat(magnitude)
            .isEqualTo(1_960_569_556L);
    }
}
