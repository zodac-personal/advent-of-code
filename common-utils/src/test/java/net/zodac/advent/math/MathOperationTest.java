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
 * Unit tests for {@link MathOperation}.
 */
class MathOperationTest {

    @ParameterizedTest
    @CsvSource({
        "ADD,2,6,8",
        "CONCATENATE,2,6,26",
        "DIVIDE,12,6,2",
        "MINUS,2,6,-4",
        "MULTIPLY,2,6,12",
        "POWER,2,6,64",
        "REMAINDER,2,6,2",
        "INVALID,2,6,2",
    })
    void testOperation(final MathOperation mathOperation, final long first, final long second, final long expected) {
        assertThat(mathOperation.calculate(first, second))
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "add,ADD",              // Lowercase
        "DIVIDE,DIVIDE",        // Uppercase
        "pOWer,POWER",          // Mixed case
        "not-existing,INVALID"  // Invalid
    })
    void testGetByName(final String input, final MathOperation mathOperation) {
        assertThat(MathOperation.getByName(input))
            .isEqualTo(mathOperation);
    }

    @ParameterizedTest
    @CsvSource({
        "+,ADD",    // Valid
        "s,INVALID" // Invalid
    })
    void testGetBySymbol(final char input, final MathOperation mathOperation) {
        assertThat(MathOperation.getBySymbol(input))
            .isEqualTo(mathOperation);
    }
}
