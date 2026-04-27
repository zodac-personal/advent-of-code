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

import net.zodac.advent.util.MathUtils;

/**
 * Simple class defining a numeric {@link Range} of {@link Integer}s.
 *
 * @param start the start of the {@link Range}
 * @param end   the end of the {@link Range}
 */
public record Range(int start, int end) {

    /**
     * Creates a {@link Range}.
     *
     * @param start the start of the {@link Range}
     * @param end   the end of the {@link Range}
     * @return the created {@link Range}
     * @throws IllegalArgumentException thrown if the {@code end} value is less than the {@code start} value
     */
    public static Range of(final int start, final int end) {
        if (end < start) {
            throw new IllegalArgumentException(String.format("Cannot have end %s less than start %s", end, start));
        }

        return new Range(start, end);
    }

    /**
     * Checks if this {@link Range} completely overlaps the provided {@link Range}, or vice versa. For example, given the {@link Range}s:
     * <pre>
     *     1-5,2-6
     *     1-9,2-3
     * </pre>
     *
     * <p>
     * The second {@link Range} has a complete overlap, where {@code 2-3} is completely overlapped by {@code 1-9}.
     *
     * @param other the other {@link Range} to check
     * @return {@code true} if there is a complete overlap of either {@link Range}
     */
    public boolean hasCompleteOverlap(final Range other) {
        return (start >= other.start && end <= other.end) || (other.start >= start && other.end <= end);
    }

    /**
     * Checks if this {@link Range} partially overlaps the provided {@link Range}, or vice versa. For example, given the {@link Range}s:
     * <pre>
     *     1-5,2-6
     *     1-9,2-3
     * </pre>
     *
     * <p>
     * Both {@link Range}s have a partial overlap, where {@code 1-5} and {@code 2-6} partial overlap, and where {@code 2-3} is completely overlapped
     * by {@code 1-9}.
     *
     * @param other the other {@link Range} to check
     * @return {@code true} if there is a partial overlap of either {@link Range}
     */
    public boolean hasPartialOverlap(final Range other) {
        return MathUtils.isBetween(start, end, other.start)
            || MathUtils.isBetween(start, end, other.end)
            || MathUtils.isBetween(other.start, other.end, start)
            || MathUtils.isBetween(other.start, other.end, end);
    }
}
