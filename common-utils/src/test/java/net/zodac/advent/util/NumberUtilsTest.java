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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for {@link NumberUtils}.
 */
class NumberUtilsTest {

    @ParameterizedTest
    @CsvSource({
        "123,true",                                 // Valid integer
        "-123,true",                                // Valid negative integer
        "9999999999999999,false",                   // Valid long, but too large for integer
        "-9999999999999999,false",                  // Valid negative long, but too large for integer
        "99999999999999999999999999999999,false",   // Excessively large value
        "3.14,false",                               // Valid float
        "2/3,false",                                // Valid fraction
        "abc,false",                                // Invalid numeric value
        "'',false",                                 // Empty
        "' ',false",                                // Blank
    })
    void testIsInteger(final String input, final boolean expected) {
        final boolean output = NumberUtils.isInteger(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "123,true",                                 // Valid integer
        "-123,true",                                // Valid negative integer
        "9999999999999999,true",                    // Valid long, but too large for integer
        "-9999999999999999,true",                   // Valid negative long, but too large for integer
        "99999999999999999999999999999999,false",   // Value too large for long
        "3.14,false",                               // Valid float
        "2/3,false",                                // Valid fraction
        "abc,false",                                // Invalid numeric value
        "'',false",                                 // Empty
        "' ',false",                                // Blank
    })
    void testIsLong(final String input, final boolean expected) {
        final boolean output = NumberUtils.isLong(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "123,0,123",                                // Valid integer
        "-123,0,-123",                              // Valid negative integer
        "9999999999999999,1,1",                     // Valid long, but too large for integer
        "-9999999999999999,2,2",                    // Valid negative long, but too large for integer
        "99999999999999999999999999999999,3,3",     // Value too large for long
        "abc,5,5",                                  // Invalid numeric value
    })
    void testToIntOrDefault(final String inputValue, final int defaultValue, final int expectedValue) {
        final int output = NumberUtils.toIntOrDefault(inputValue, defaultValue);
        assertThat(output)
            .isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @CsvSource({
        "123,0,123",                                // Valid integer
        "-123,0,-123",                              // Valid negative integer
        "9999999999999999,1,9999999999999999",      // Valid long, but too large for integer
        "-9999999999999999,2,-9999999999999999",    // Valid negative long, but too large for integer
        "99999999999999999999999999999999,3,3",     // Value too large for long
        "abc,5,5",                                  // Invalid numeric value
    })
    void testToLongOrDefault(final String inputValue, final long defaultValue, final long expectedValue) {
        final long output = NumberUtils.toLongOrDefault(inputValue, defaultValue);
        assertThat(output)
            .isEqualTo(expectedValue);
    }
}
