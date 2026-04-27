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
 * Basic class defining an interval or range between a start and an end value.
 *
 * @param start the start value
 * @param end   the end value
 */
public record Interval(long start, long end) {

    /**
     * Creates an {@link Interval} with an inclusive {@code start}, and an exclusive {@code end}.
     *
     * @param start the start value
     * @param end   the end value
     * @return the {@link Interval}
     */
    public static Interval openInterval(final long start, final long end) {
        return closedInterval(start, end - 1);
    }

    /**
     * Creates an {@link Interval} with an inclusive {@code start}, and also an inclusive {@code end}.
     *
     * @param start the start value
     * @param end   the end value
     * @return the {@link Interval}
     */
    public static Interval closedInterval(final long start, final long end) {
        return new Interval(start, end);
    }

    /**
     * Creates an {@link Interval} with a single value.
     *
     * @param value the start and end value
     * @return the {@link Interval}
     */
    public static Interval singular(final long value) {
        return closedInterval(value, value);
    }

    /**
     * Creates an {@link Interval} with both {@code start} and {@code end} set to <b>0</b>.
     *
     * @return the {@link Interval}
     */
    public static Interval empty() {
        return singular(0L);
    }

    /**
     * Returns the overlap or intersection of this {@link Interval} compared to another.
     *
     * @param other the other {@link Interval}
     * @return the range of values common to both {@link Interval}s
     */
    public Interval intersect(final Interval other) {
        if (other.start > end || start > other.end) {
            return empty();
        }

        final long newStart = Math.max(start, other.start());
        final long newEnd = Math.min(end, other.end);
        return closedInterval(newStart, newEnd);
    }

    /**
     * If the {@link Interval} is empty. An {@link Interval} is considered empty if both the {@code start} and {@code end} values are <b>0</b>.
     *
     * @return {@code true} if the {@link Interval} is empty
     * @see #empty()
     */
    public boolean isEmpty() {
        return start == 0L && end == 0L;
    }
}
