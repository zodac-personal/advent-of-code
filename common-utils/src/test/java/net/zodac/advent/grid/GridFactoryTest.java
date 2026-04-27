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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link GridFactory}.
 */
class GridFactoryTest {

    @Test
    void testOfIntegers() {
        final List<String> input = List.of(
            "12",
            "23"
        );

        final Grid<Integer> output = GridFactory.ofIntegers(input);
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

    @Test
    void testOfIntegersWithSize() {
        final Grid<Integer> output = GridFactory.ofIntegersWithSize(2);
        assertThat(output.elementsInGrid())
            .isEqualTo(4);

        assertThat(output.at(Point.of(0, 0)))
            .isZero();
        assertThat(output.at(Point.of(0, 1)))
            .isZero();
        assertThat(output.at(Point.of(1, 0)))
            .isZero();
        assertThat(output.at(Point.of(1, 1)))
            .isZero();

        assertThatThrownBy(() -> GridFactory.ofIntegersWithSize(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Size must be positive integer, found: 0");
        assertThatThrownBy(() -> GridFactory.ofIntegersWithSize(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Size must be positive integer, found: -1");
    }
}
