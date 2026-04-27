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

/**
 * Utility class for basic {@code int} and {@code long} calculations.
 */
public final class NumberUtils {

    private NumberUtils() {

    }

    /**
     * Checks if the input {@link String} is a valid {@link Integer}.
     *
     * @param input the {@link String} to check
     * @return {@code true} if the input is an {@link Integer}
     */
    public static boolean isInteger(final String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (final NumberFormatException ignored) {
            return false;
        }
    }

    /**
     * Checks if the input {@link String} is a valid {@link Long}.
     *
     * @param input the {@link String} to check
     * @return {@code true} if the input is an {@link Long}
     */
    public static boolean isLong(final String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (final NumberFormatException ignored) {
            return false;
        }
    }

    /**
     * Converts the provided {@link Object} by converting it to a {@link String} then calling {@link Integer#parseInt(String)}. If it is an invalid
     * {@link Integer}, the {@link NumberFormatException} will be handled and instead {@code defaultValue} will be returned.
     *
     * @param input        the {@link Object} to convert
     * @param defaultValue the default {@link Integer} to return if the {@code input} is not a valid {@link Integer}
     * @return the converted {@link Integer}, or {@code defaultValue}
     */
    public static int toIntOrDefault(final Object input, final int defaultValue) {
        try {
            return Integer.parseInt(String.valueOf(input));
        } catch (final NumberFormatException ignored) {
            return defaultValue;
        }
    }

    /**
     * Converts the provided {@link Object} by converting it to a {@link String} then calling {@link Long#parseLong(String)}. If it is an invalid
     * {@link Long}, the {@link NumberFormatException} will be handled and instead {@code defaultValue} will be returned.
     *
     * @param input        the {@link Object} to convert
     * @param defaultValue the default {@link Long} to return if the {@code input} is not a valid {@link Long}
     * @return the converted {@link Long}, or {@code defaultValue}
     */
    public static long toLongOrDefault(final Object input, final long defaultValue) {
        try {
            return Long.parseLong(String.valueOf(input));
        } catch (final NumberFormatException ignored) {
            return defaultValue;
        }
    }
}
