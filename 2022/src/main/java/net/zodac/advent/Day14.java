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

package net.zodac.advent;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.Line;

/**
 * Solution for 2022, Day 14.
 *
 * @see <a href="https://adventofcode.com/2022/day/14">AoC 2022, Day 14</a>
 */
public final class Day14 {

    private static final Point SAND_SPAWN_POINT = Point.of(500, 0);

    private Day14() {

    }

    /**
     * Given an input {@link List} of {@link List} of {@link Point}s, each entry will refer to {@link Line}s of {@link Point}:
     * <pre>
     *     0,0 -> 0,4 -> 3,4
     * </pre>
     *
     * <p>
     * Given these input {@link Line}s, we then draw them onto a 2D grid. We also start dropping a grain of sand from {@link #SAND_SPAWN_POINT}. Each
     * grain will attempt to move in the following order:
     * <ol>
     *     <li>Down</li>
     *     <li>Down, then left</li>
     *     <li>Down, then right</li>
     * </ol>
     *
     * <p>
     * If any of these new positions is already occupied (either by the initial {@link Line}s, or by a grain of sand at rest), the next one is
     * attempted. If after all attempts the grain cannot move, it stops at that point.
     *
     * <p>
     * We can calculate the known floor of the area by finding the lowest Y coordinate of the drawn {@link Line}s. We keep dropping grains of sand
     * until one grain drops below the known floor.
     *
     * @param values the input {@link Point}s
     * @return the total number of grains dropped before falling past the floor
     */
    public static long countGrainsOfSandBeforeFallingPastTheFloor(final Collection<? extends List<Point>> values) {
        final Collection<Point> filledPoints = fillInputPoints(values);

        final int floor = filledPoints
            .stream()
            .mapToInt(Point::y)
            .max()
            .orElse(0);

        final int initialNumberOfFilledPoints = filledPoints.size();
        Point currentSandGrain = SAND_SPAWN_POINT;

        while (currentSandGrain.y() < floor) {
            currentSandGrain = dropSandPoint(filledPoints, currentSandGrain);
        }

        return (long) filledPoints.size() - initialNumberOfFilledPoints;
    }

    /**
     * Given an input {@link List} of {@link List} of {@link Point}s, each entry will refer to {@link Line}s of {@link Point}:
     * <pre>
     *     0,0 -> 0,4 -> 3,4
     * </pre>
     *
     * <p>
     * Given these input {@link Line}s, we then draw them onto a 2D grid. We also start dropping a grain of sand from {@link #SAND_SPAWN_POINT}. Each
     * grain will attempt to move in the following order:
     * <ol>
     *     <li>Down</li>
     *     <li>Down, then left</li>
     *     <li>Down, then right</li>
     * </ol>
     *
     * <p>
     * If any of these new positions is already occupied (either by the initial {@link Line}s, or by a grain of sand at rest), the next one is
     * attempted. If after all attempts the grain cannot move, it stops at that point.
     *
     * <p>
     * We can calculate the known floor of the area by finding the lowest Y coordinate of the drawn {@link Line}s plus <b>2</b>. This floor is known
     * to be infinite. We keep dropping grains of sand until they pile up in the area and the last grain of sand is resting on the
     * {@link #SAND_SPAWN_POINT}.
     *
     * @param values the input {@link Point}s
     * @return the total number of grains dropped before reaching the {@link #SAND_SPAWN_POINT}
     */
    public static long countGrainsOfSandBeforeReachingSandSpawnPoint(final Collection<? extends List<Point>> values) {
        final Collection<Point> filledPoints = fillInputPoints(values);

        final int floor = filledPoints
            .stream()
            .mapToInt(Point::y)
            .max()
            .orElse(0) + 2; // Floor is two spaces below last filled Point

        // Extend floor, to allow sand to keep heaping up
        final Collection<Point> extendedFloorPoints = generateFloorPoints(floor);
        filledPoints.addAll(extendedFloorPoints);

        final int initialNumberOfFilledPoints = filledPoints.size();
        Point currentSandPoint = SAND_SPAWN_POINT;

        while (!filledPoints.contains(SAND_SPAWN_POINT)) {
            currentSandPoint = dropSandPoint(filledPoints, currentSandPoint);
        }

        return (long) filledPoints.size() - initialNumberOfFilledPoints;
    }

    private static Collection<Point> fillInputPoints(final Collection<? extends List<Point>> values) {
        final Collection<Point> filledPoints = new HashSet<>();

        for (final List<Point> pointsForLine : values) {
            final int numberOfPointsForLine = pointsForLine.size();

            for (int i = 1; i < numberOfPointsForLine; i++) {
                final Point firstCoordinate = pointsForLine.get(i - 1);
                final Point secondCoordinate = pointsForLine.get(i);
                final Line line = Line.of(firstCoordinate, secondCoordinate);

                filledPoints.addAll(line.getPointsInLine());
            }
        }

        return filledPoints;
    }

    private static Collection<Point> generateFloorPoints(final int floorY) {
        final Collection<Point> floorPoints = new HashSet<>();

        // Extend the floor out left and right from the start point
        final int expandedSize = SAND_SPAWN_POINT.x() << 1;
        for (int x = 0; x < expandedSize; x++) {
            floorPoints.add(Point.of(x, floorY));
        }

        return floorPoints;
    }

    // As the Y coordinate increases as you go down, but our Point values decrease as you go down, we use unexpected directions
    private static Point dropSandPoint(final Collection<? super Point> filledPoints, final Point startSandPoint) {
        final Point possibleDownPoint = startSandPoint.moveRight();
        if (!filledPoints.contains(possibleDownPoint)) {
            return possibleDownPoint;
        }

        final Point possibleDownLeftPoint = startSandPoint.moveUpRight();
        if (!filledPoints.contains(possibleDownLeftPoint)) {
            return possibleDownLeftPoint;
        }

        final Point possibleDownRightPoint = startSandPoint.moveDownRight();
        if (!filledPoints.contains(possibleDownRightPoint)) {
            return possibleDownRightPoint;
        }

        // Cannot move any further
        filledPoints.add(startSandPoint);
        return SAND_SPAWN_POINT;
    }
}
