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

package net.zodac.advent.function;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link TriFunction}.
 */
class TriFunctionTest {

    @Test
    void testTriFunction() {
        final TriFunction<Integer, String, String, Boolean> triFunction =
            (integer, string1, string2) -> integer > 0 && string1.isEmpty() && !string2.isEmpty();

        assertThat(triFunction.apply(5, "", "test"))
            .isTrue();
        assertThat(triFunction.apply(-1, "", "test"))
            .isFalse();
        assertThat(triFunction.apply(5, "invalid", "test"))
            .isFalse();
        assertThat(triFunction.apply(5, "", ""))
            .isFalse();
    }
}
