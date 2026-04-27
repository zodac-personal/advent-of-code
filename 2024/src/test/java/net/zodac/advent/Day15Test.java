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
import net.zodac.advent.grid.Direction;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.GridFactory;
import net.zodac.advent.input.InputReader;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day15}.
 */
class Day15Test {

    private static final String INPUT_FILENAME = "day15.txt";

    @Test
    void example() {
        final List<List<String>> values = InputReader
            .forExample(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank);

        final Grid<Character> characterGrid = GridFactory.ofCharacters(values.getFirst());
        final List<Direction> directionsToMove = parseDirectionsToMove(values);

        final long part1Result = Day15.getValueOfGridAfterMoves(characterGrid, directionsToMove, false);
        assertThat(part1Result)
            .isEqualTo(10_092L);

        final long part2Result = Day15.getValueOfGridAfterMoves(characterGrid, directionsToMove, true);
        assertThat(part2Result)
            .isEqualTo(9_021L);
    }

    @Test
    void part1() {
        final List<List<String>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank);

        final Grid<Character> characterGrid = GridFactory.ofCharacters(values.getFirst());
        final List<Direction> directionsToMove = parseDirectionsToMove(values);

        final long part1Result = Day15.getValueOfGridAfterMoves(characterGrid, directionsToMove, false);
        assertThat(part1Result)
            .isEqualTo(1_577_255L);
    }

    @Test
    void part2() {
        final List<List<String>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank);

        final Grid<Character> characterGrid = GridFactory.ofCharacters(values.getFirst());
        final List<Direction> directionsToMove = parseDirectionsToMove(values);

        final long part2Result = Day15.getValueOfGridAfterMoves(characterGrid, directionsToMove, true);
        assertThat(part2Result)
            .isEqualTo(1_597_035L);
    }

    private static List<Direction> parseDirectionsToMove(final List<? extends List<String>> values) {
        return values.getLast().stream()
            .flatMap(str -> str.chars().mapToObj(character -> (char) character))
            .map(Direction::get)
            .toList();
    }
}
