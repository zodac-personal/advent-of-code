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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for {@link BitwiseOperation}.
 */
class BitwiseOperationTest {

    @ParameterizedTest
    @CsvSource({
        "AND,2,6,2",
        "LSHIFT,2,6,128",
        "NOT,2,6,-3",
        "OR,2,6,6",
        "RSHIFT,6,1,3",
        "XOR,2,6,4",
        "INVALID,2,6,2",
    })
    void testOperation(final BitwiseOperation bitwiseOperation, final long first, final long second, final long expected) {
        assertThat(bitwiseOperation.calculate(first, second))
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "and,AND",              // Lowercase
        "LSHIFT,LSHIFT",        // Uppercase
        "rShifT,RSHIFT",        // Mixed case
        "not-existing,INVALID"  // Invalid
    })
    void testGet(final String input, final BitwiseOperation expected) {
        assertThat(BitwiseOperation.get(input))
            .isEqualTo(expected);
    }
}
