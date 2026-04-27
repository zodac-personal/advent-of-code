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

package net.zodac.advent.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link BinaryConversionUtils}.
 */
class BinaryConversionUtilsTest {

    @Test
    void whenToDecimal_givenValidBinary_thenDecimalValueIsReturned() {
        final String input = "100";
        final long output = BinaryConversionUtils.toDecimal(input);
        assertThat(output)
            .isEqualTo(4);
    }

    @Test
    void whenToDecimal_givenInvalidBinary_thenNumberFormatExceptionIsThrown() {
        final String input = "abc";
        assertThatThrownBy(() -> BinaryConversionUtils.toDecimal(input))
            .isInstanceOf(NumberFormatException.class);
    }
}
