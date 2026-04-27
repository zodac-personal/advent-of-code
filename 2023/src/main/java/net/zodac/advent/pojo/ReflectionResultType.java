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

/**
 * The types of {@link ReflectionResult}s.
 */
public enum ReflectionResultType {

    /**
     * The pattern is reflected vertically around a column.
     */
    COLUMN(1),

    /**
     * The pattern is reflected horizontally around a row.
     */
    ROW(100),

    /**
     * The pattern has no reflection.
     */
    NO_REFLECTION(0);

    private final int multiplier;

    ReflectionResultType(final int multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Returns the multiplier for the {@link ReflectionResultType}.
     *
     * @return the multiplier
     */
    public int multiplier() {
        return multiplier;
    }
}
