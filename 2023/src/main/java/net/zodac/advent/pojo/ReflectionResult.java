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
 * Class defining the result of a search for a reflection in a pattern.
 *
 * @param reflectionResultType the {@link ReflectionResultType}
 * @param index                the index of the reflection
 */
public record ReflectionResult(ReflectionResultType reflectionResultType, long index) {

    /**
     * Creates a {@link ReflectionResult} for a vertical reflection.
     *
     * @param columnIndex the column index
     * @return the {@link ReflectionResult}
     */
    public static ReflectionResult vertical(final long columnIndex) {
        return new ReflectionResult(ReflectionResultType.COLUMN, columnIndex);
    }

    /**
     * Creates a {@link ReflectionResult} for a horizontal reflection.
     *
     * @param rowIndex the row index
     * @return the {@link ReflectionResult}
     */
    public static ReflectionResult horizontal(final long rowIndex) {
        return new ReflectionResult(ReflectionResultType.ROW, rowIndex);
    }

    /**
     * Creates a {@link ReflectionResult} when no reflection can be found.
     *
     * @return the {@link ReflectionResult}
     */
    public static ReflectionResult none() {
        return new ReflectionResult(ReflectionResultType.NO_REFLECTION, -1L);
    }

    /**
     * Checks if the {@link ReflectionResult} found a reflection.
     *
     * @return {@code true} if the {@code reflectionResultType} is {@link ReflectionResultType#COLUMN} or {@link ReflectionResultType#ROW}
     */
    public boolean hasReflection() {
        return reflectionResultType != ReflectionResultType.NO_REFLECTION;
    }

    /**
     * Calculates the value for the {@link ReflectionResult}. They are calculated by incrementing the index (as the result is 1-indexed), then
     * multiplying by the {@link ReflectionResultType#multiplier()}.
     *
     * @return the value of the {@link ReflectionResult}
     */
    public long calculateValue() {
        return (index + 1) * reflectionResultType.multiplier();
    }
}
