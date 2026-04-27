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

package net.zodac.advent.math;

import static org.assertj.core.api.Assertions.assertThat;

import net.zodac.advent.pojo.tuple.Pair;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SimultaneousEquation}.
 */
class SimultaneousEquationTest {

    @Test
    void testSimultaneousEquation() {
        final SimultaneousEquation simultaneousEquation = new SimultaneousEquation(2L, 4L, 14L, 4L, -4L, 4L);
        assertThat(simultaneousEquation.solve())
            .isPresent()
            .hasValue(Pair.of(3L, 2L));
    }

    @Test
    void testSimultaneousEquation_invalid() {
        final SimultaneousEquation noDeterminant = new SimultaneousEquation(2L, 4L, 6L, 1L, 2L, 3L);
        assertThat(noDeterminant.solve())
            .isEmpty();

        final SimultaneousEquation invalidValueForX = new SimultaneousEquation(3L, 2L, 7L, 5L, -4L, -13L);
        assertThat(invalidValueForX.solve())
            .isEmpty();

        final SimultaneousEquation invalidValueForY = new SimultaneousEquation(2L, 3L, 8L, 4L, -5L, 2L);
        assertThat(invalidValueForY.solve())
            .isEmpty();
    }
}
