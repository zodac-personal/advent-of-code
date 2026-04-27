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

package net.zodac.advent.pojo.tuple;

/**
 * Simple tuple for three objects.
 *
 * @param first  the first object
 * @param second the second object
 * @param third  the third object
 * @param <A>    type of first value
 * @param <B>    type of second value
 * @param <C>    type of second value
 */
public record Triple<A, B, C>(A first, B second, C third) {

    /**
     * Create a {@link Triple} with three values.
     *
     * @param first  the first value
     * @param second the second value
     * @param third  the third value
     * @param <A>    type of first value
     * @param <B>    type of second value
     * @param <C>    type of third value
     * @return the created {@link Triple}
     */
    public static <A, B, C> Triple<A, B, C> of(final A first, final B second, final C third) {
        return new Triple<>(first, second, third);
    }
}
