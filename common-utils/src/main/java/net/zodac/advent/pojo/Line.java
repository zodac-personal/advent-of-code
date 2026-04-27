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

import java.util.HashSet;
import java.util.Set;
import net.zodac.advent.grid.Point;
import net.zodac.advent.util.MathUtils;

/**
 * POJO defining a line on a coordinate grid, defined by two {@link Point}s.
 *
 * @param first  the first {@link Point}
 * @param second the second {@link Point}
 */
public record Line(Point first, Point second) {

    /**
     * Creates a {@link Line} based off the provided {@link Point}s.
     *
     * @param first  the first {@link Point} in the {@link Line}
     * @param second the first {@link Point} in the {@link Line}
     * @return the created {@link Line}
     * @throws IllegalArgumentException thrown of the input {@link Point}s do not form a valid {@link Line}
     */
    public static Line of(final Point first, final Point second) {
        if (!isHorizontal(first, second) && !isVertical(first, second) && !isPerfectDiagonal(first, second)) {
            throw new IllegalArgumentException(
                String.format("Input %ss (%s and %s) do not form a valid %s", Point.class.getSimpleName(), first, second,
                    Line.class.getSimpleName()));
        }

        return new Line(first, second);
    }

    /**
     * Returns the maximum value of the x1, x2, y1, y2 coordinates.
     *
     * @return the largest value of the coordinates
     * @see MathUtils#max(int, int...)
     */
    public int maxCoordinateValue() {
        return MathUtils.max(first.x(), second.x(), first.y(), second.y());
    }

    /**
     * Checks if the line is horizontal.
     *
     * @return {@code true} if the {@link Line} is horizontal
     */
    public boolean isHorizontal() {
        return isHorizontal(first, second);
    }

    /**
     * Checks if the line is horizontal.
     *
     * @param first  the first {@link Point} in the {@link Line}
     * @param second the first {@link Point} in the {@link Line}
     * @return {@code true} if the {@link Line} is horizontal
     */
    public static boolean isHorizontal(final Point first, final Point second) {
        return first.x() == second.x();
    }

    /**
     * Checks if the line is vertical.
     *
     * @return {@code true} if the {@link Line} is vertical
     */
    public boolean isVertical() {
        return isVertical(first, second);
    }

    /**
     * Checks if the line is vertical.
     *
     * @param first  the first {@link Point} in the {@link Line}
     * @param second the first {@link Point} in the {@link Line}
     * @return {@code true} if the {@link Line} is vertical
     */
    public static boolean isVertical(final Point first, final Point second) {
        return first.y() == second.y();
    }

    /**
     * Checks if the line is a 'perfect' diagonal (meaning a 45-degree angle).
     *
     * @return {@code true} if the {@link Line} is a 45-degree diagonal
     */
    public boolean isPerfectDiagonal() {
        return isPerfectDiagonal(first, second);
    }

    /**
     * Checks if the line is a 'perfect' diagonal (meaning a 45-degree angle).
     *
     * @param first  the first {@link Point} in the {@link Line}
     * @param second the first {@link Point} in the {@link Line}
     * @return {@code true} if the {@link Line} is a 45-degree diagonal
     */
    public static boolean isPerfectDiagonal(final Point first, final Point second) {
        return Math.abs(first.x() - second.x()) == Math.abs(first.y() - second.y());
    }

    /**
     * Returns all {@link Point}s in the {@link Line}.
     *
     * @return the {@link Point}s in the {@link Line}
     */
    public Set<Point> getPointsInLine() {
        final Set<Point> points = new HashSet<>();
        final int minX = Math.min(first.x(), second.x());
        final int maxX = Math.max(first.x(), second.x());
        final int minY = Math.min(first.y(), second.y());
        final int maxY = Math.max(first.y(), second.y());

        if (isHorizontal()) {
            for (int y = minY; y <= maxY; y++) {
                points.add(Point.of(minX, y));
            }
        } else if (isVertical()) {
            for (int x = minX; x <= maxX; x++) {
                points.add(Point.of(x, minY));
            }
        } else if (isPerfectDiagonal()) {
            final int incrementForX = diagonalIncrement(second.x() - first.x());
            final int incrementForY = diagonalIncrement(second.y() - first.y());

            final int x2 = second.x();
            final int y2 = second.y();

            int currX = first.x();
            int currY = first.y();

            while (currX != x2 && currY != y2) {
                points.add(Point.of(currX, currY));
                currX += incrementForX;
                currY += incrementForY;
            }

            // Add the second coordinate, as it is missed in the while loop
            points.add(Point.of(second.x(), second.y()));
        }

        return points;
    }

    private static int diagonalIncrement(final int i) {
        return Integer.compare(i, 0);
    }
}
