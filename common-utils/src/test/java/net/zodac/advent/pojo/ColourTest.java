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

package net.zodac.advent.pojo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for {@link Colour}.
 */
class ColourTest {

    @ParameterizedTest
    @CsvSource({
        "blue,BLUE",    // Lowercase
        "BLUE,BLUE",    // Uppercase
        "BlUe,BLUE",    // Mixed case
        "green,GREEN",  // Green
        "red,RED"       // Red
    })
    void testGet(final String input, final Colour expected) {
        assertThat(Colour.get(input))
            .isEqualTo(expected);
    }

    @Test
    void testGet_givenInvalidInput() {
        assertThatThrownBy(() -> Colour.get("invalid"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Unable to find Colour for input 'invalid'");
    }
}
