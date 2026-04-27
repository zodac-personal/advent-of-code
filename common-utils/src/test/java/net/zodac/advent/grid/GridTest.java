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

package net.zodac.advent.grid;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import net.zodac.advent.util.NumberUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Grid}. Placeholder tests to fix PIT failures until I build the test coverage up.
 */
class GridTest {

    @Test
    void testEquals() {
        final Grid<Boolean> first = new Grid<>(5, new Boolean[5][5], false);
        final Grid<Boolean> second = new Grid<>(5, new Boolean[5][5], true);
        final Grid<Boolean> likeFirst = new Grid<>(5, new Boolean[5][5], false);

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                assertThat(first.at(row, column))
                    .isFalse();
            }
        }

        assertThat(first)
            .isEqualTo(likeFirst);
        assertThat(first.elementsInGrid())
            .isEqualTo(25);
        assertThat(first)
            .isNotEqualTo(second);
    }

    @Test
    void testHashcode() {
        final Grid<Boolean> first = new Grid<>(5, new Boolean[5][5], false);
        final Grid<Boolean> second = new Grid<>(5, new Boolean[5][5], true);
        final Grid<Boolean> likeFirst = new Grid<>(5, new Boolean[5][5], false);

        assertThat(first)
            .hasSameHashCodeAs(likeFirst)
            .doesNotHaveSameHashCodeAs(second);
    }

    @Test
    void testIntegerGrid() {
        final List<String> input = List.of(
            "12",
            "23"
        );

        final Grid<Integer> output = Grid.parseGrid(input, character -> NumberUtils.toIntOrDefault(character, 0));
        assertThat(output.elementsInGrid())
            .isEqualTo(4);

        assertThat(output.at(Point.of(0, 0)))
            .isEqualTo(1);
        assertThat(output.at(Point.of(0, 1)))
            .isEqualTo(2);
        assertThat(output.at(Point.of(1, 0)))
            .isEqualTo(2);
        assertThat(output.at(Point.of(1, 1)))
            .isEqualTo(3);
    }
}
