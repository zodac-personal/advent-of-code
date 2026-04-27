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

import net.zodac.advent.input.InputReader;
import net.zodac.advent.pojo.rpg.mage.MageBoss;
import net.zodac.advent.pojo.rpg.mage.MagePlayer;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day22}.
 */
class Day22Test {

    private static final String INPUT_FILENAME = "day22.txt";
    private static final MagePlayer EXAMPLE_STARTING_PLAYER = MagePlayer.create(10, 250);
    private static final MagePlayer PUZZLE_STARTING_PLAYER = MagePlayer.create(50, 500);

    @Test
    void example() {
        final String value = InputReader
            .forExample(INPUT_FILENAME)
            .asStrings()
            .readAllAsSingleString();
        final MageBoss boss = MageBoss.parse(value);

        final long part1Result = Day22.findCheapestManaCostToWinBattle(EXAMPLE_STARTING_PLAYER, boss, 0);
        assertThat(part1Result)
            .isEqualTo(641L);
    }

    @Test
    void part1() {
        final String value = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .readAllAsSingleString();
        final MageBoss boss = MageBoss.parse(value);

        final long part1Result = Day22.findCheapestManaCostToWinBattle(PUZZLE_STARTING_PLAYER, boss, 0);
        assertThat(part1Result)
            .isEqualTo(953L);
    }

    @Test
    void part2() {
        final String value = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .readAllAsSingleString();
        final MageBoss boss = MageBoss.parse(value);

        final long part2Result = Day22.findCheapestManaCostToWinBattle(PUZZLE_STARTING_PLAYER, boss, 1);
        assertThat(part2Result)
            .isEqualTo(1_289L);
    }
}
