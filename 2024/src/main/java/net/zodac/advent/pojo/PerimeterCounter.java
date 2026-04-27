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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.AdjacentPointsSelector;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;

/**
 * Utility class used to check a {@link Set} of connected {@link Point}s and count the size of the perimeter needed to enclose the {@link Set}.
 */
public final class PerimeterCounter {

    private static final int DEFAULT_MAX_PERIMETERS = 4; // Start with 4 possible perimeters, and remove as matching neighbours are found

    private PerimeterCounter() {

    }

    /**
     * Counts the total perimeter of the provided {@link Point}s. Assumes that each {@link Point} can have a maximum perimeter of 4. But if a
     * neighbour {@link Point} is equal to the current {@link Point}'s value, then no perimeter is needed.
     *
     * @param points the {@link Point}s to check
     * @param grid   the {@link Grid} for the {@link Point}s to check the value
     * @param <E>    the type of the {@link Grid}
     * @return the size of the perimeter of the {@link Point}s
     */
    public static <E> long countFullPerimeter(final Collection<Point> points, final Grid<E> grid) {
        long countForGroup = 0;

        for (final Point point : points) {
            int countForPoint = DEFAULT_MAX_PERIMETERS;
            final AdjacentPointsSelector adjacentPointsSelector = AdjacentPointsSelector.bounded(false, AdjacentDirection.CARDINAL, grid.size());
            final List<Point> neighbourPoints = point.getAdjacentPoints(adjacentPointsSelector).toList();
            final E currentValue = grid.at(point);

            for (final Point neighbourPoint : neighbourPoints) {
                if (grid.at(neighbourPoint).equals(currentValue)) {
                    countForPoint--;
                }
            }

            countForGroup += Math.max(countForPoint, 0); // Don't go into negatives
        }

        return countForGroup;
    }

    /**
     * Counts the total number of sides along the perimeter of the provided {@link Point}s. Checks for the number of corners/turns in the perimeter
     * and uses that as the number of sides
     *
     * @param points the {@link Point}s to check
     * @return the number of sides along the perimeter of the {@link Point}s
     */
    public static long countSides(final Collection<Point> points) {
        long cornersForGroup = 0; // Count the corners, which should be equal to the number of sides

        for (final Point point : points) {
            int corners = DEFAULT_MAX_PERIMETERS;

            if (hasFirstCorner(points, point)) {
                corners--;
            }

            if (hasSecondCorner(points, point)) {
                corners--;
            }

            if (hasThirdCorner(points, point)) {
                corners--;
            }

            if (hasFourthCorner(points, point)) {
                corners--;
            }

            cornersForGroup += corners;
        }

        return cornersForGroup;
    }

    private static boolean hasFirstCorner(final Collection<Point> points, final Point point) {
        return containsAny(points, point.moveLeft(), point.moveUp())
            && (points.contains(point.moveUpLeft()) || containsNone(points, point.moveLeft(), point.moveUp()));
    }

    private static boolean hasSecondCorner(final Collection<Point> points, final Point point) {
        return containsAny(points, point.moveRight(), point.moveUp())
            && (points.contains(point.moveUpRight()) || containsNone(points, point.moveRight(), point.moveUp()));
    }

    private static boolean hasThirdCorner(final Collection<Point> points, final Point point) {
        return containsAny(points, point.moveRight(), point.moveDown())
            && (points.contains(point.moveDownRight()) || containsNone(points, point.moveRight(), point.moveDown()));
    }

    private static boolean hasFourthCorner(final Collection<Point> points, final Point point) {
        return containsAny(points, point.moveLeft(), point.moveDown())
            && (points.contains(point.moveDownLeft()) || containsNone(points, point.moveLeft(), point.moveDown()));
    }

    private static boolean containsAny(final Collection<Point> points, final Point... pointsToCheck) {
        return Arrays.stream(pointsToCheck)
            .anyMatch(points::contains);
    }

    private static boolean containsNone(final Collection<Point> points, final Point... pointsToCheck) {
        for (final Point point : pointsToCheck) {
            if (!points.contains(point)) {
                return true;
            }
        }
        return false;
    }
}
