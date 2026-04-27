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
import net.zodac.advent.grid.Direction;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.search.LoopFinder;

/**
 * Solution for 2024, Day 6.
 *
 * @see <a href="https://adventofcode.com/2024/day/6">[2024: 06] Guard Gallivant</a>
 */
public final class Day06 {

    private static final int EXPECTED_NUMBER_OF_START_POINTS = 1;
    private static final char OBSTACLE_SYMBOL = '#';
    private static final char START_SYMBOL = '^';
    private static final Direction START_DIRECTION = Direction.UP;

    private Day06() {

    }

    /**
     * Given a {@link Character} {@link Grid}, find the start {@link Point} denoted by {@value START_SYMBOL}, then traverse the path. The rules
     * for the traversal are:
     * <ol>
     *     <li>Start moving {@link Direction#UP}, one space at a time</li>
     *     <li>If the next cell is a {@value #OBSTACLE_SYMBOL}, change the {@link Direction} to the right by 90°</li>
     *     <li>Keep traversing the {@link Grid} until you leave the {@link Grid}</li>
     * </ol>
     *
     * <p>
     * Once complete, return the number of {@link Point}s on the {@link Grid} that were visited.
     *
     * @param characterGrid the input {@link Character} {@link Grid}
     * @return the number of visited {@link Point}
     */
    public static long countTraversedPoints(final Grid<Character> characterGrid) {
        final Point startPoint = findStartPoint(characterGrid);
        final Collection<Point> pointsInPath = traverseGridAndReturnVisitedPoints(characterGrid, startPoint);
        return pointsInPath.size();
    }

    /**
     * Given a {@link Character} {@link Grid}, find the start {@link Point} denoted by {@value START_SYMBOL}, then traverse the path. The rules
     * for the traversal are:
     * <ol>
     *     <li>Start moving {@link Direction#UP}, one space at a time</li>
     *     <li>If the next cell is a {@value #OBSTACLE_SYMBOL}, change the {@link Direction} to the right by 90°</li>
     *     <li>Keep traversing the {@link Grid} until you leave the {@link Grid}</li>
     * </ol>
     *
     * <p>
     * Once the initial path is known, add a single {@value #OBSTACLE_SYMBOL} to the {@link Grid}, such that the traversal results in an infinite
     * loop. Repeat for all possible positions that result in an infinite loop, only changing the {@link Grid} by a single {@link Point}.
     *
     * @param characterGrid the input {@link Character} {@link Grid}
     * @return the number of possible infinite loops
     */
    public static long countPossibleLoops(final Grid<Character> characterGrid) {
        final Point startPoint = findStartPoint(characterGrid);
        final Collection<Point> pointsInPath = traverseGridAndReturnVisitedPoints(characterGrid, startPoint);
        pointsInPath.remove(startPoint); // Remove startPoint since we cannot place an obstacle there

        // TODO: If the point is in a line with an existing updated point, no need to check it again? Determine this and remove those points

        // For each point in the original path, add an obstacle and see if that forms a loop when traversing
        return pointsInPath
            .parallelStream() // No state between iterations, fine to execute in parallel
            .map(point -> characterGrid.updateAt(point, OBSTACLE_SYMBOL))
            .filter(updatedGrid -> hasLoop(updatedGrid, startPoint))
            .count();
    }

    private static Collection<Point> traverseGridAndReturnVisitedPoints(final Grid<Character> characterGrid, final Point startPoint) {
        final Collection<Point> visitedPoints = new HashSet<>();
        Point currentPoint = startPoint;
        Direction currentDirection = START_DIRECTION;

        do {
            visitedPoints.add(currentPoint);
            final Point nextPoint = currentPoint.move(currentDirection);

            if (!characterGrid.exists(nextPoint)) {
                break;
            }

            currentDirection = rotateAroundObstacles(characterGrid, currentPoint, currentDirection);
            currentPoint = currentPoint.move(currentDirection);
        } while (characterGrid.exists(currentPoint));
        return visitedPoints;
    }

    private static boolean hasLoop(final Grid<Character> grid, final Point startPoint) {
        return LoopFinder.doesLoopExist(grid, startPoint, START_DIRECTION,
            (characterGrid, point, direction) -> {
                final Point nextPoint = point.move(direction);
                if (!characterGrid.exists(nextPoint)) {
                    return Direction.INVALID;
                }

                Direction nextDirection = direction;
                nextDirection = rotateAroundObstacles(characterGrid, point, nextDirection);
                return nextDirection;
            },
            (_, point, direction) -> point.move(direction)
        );
    }

    private static Direction rotateAroundObstacles(final Grid<Character> characterGrid, final Point initialPoint, final Direction initialDirection) {
        Point nextPoint = initialPoint.move(initialDirection);
        Direction nextDirection = initialDirection;

        while (characterGrid.at(nextPoint) == OBSTACLE_SYMBOL) {
            nextDirection = nextDirection.rotateRight();
            nextPoint = initialPoint.move(nextDirection);

            if (nextDirection == initialDirection) {
                // Should not ever occur with our inputs, but handle case to avoid infinite loop
                return Direction.INVALID;
            }
        }
        return nextDirection;
    }

    private static Point findStartPoint(final Grid<Character> characterGrid) {
        // We *could* assume one, and only one, start point will always exist and skip this check, but keeping for completeness
        final List<Point> startPoints = characterGrid.findValue(c -> c == START_SYMBOL).toList();
        if (startPoints.size() != EXPECTED_NUMBER_OF_START_POINTS) {
            throw new IllegalStateException(String.format("Expected 1 point matching the start predicate, found: %d", startPoints.size()));
        }

        return startPoints.getFirst();
    }
}
