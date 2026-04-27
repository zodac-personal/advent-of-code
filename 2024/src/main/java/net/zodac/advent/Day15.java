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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import net.zodac.advent.grid.Direction;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * Solution for 2024, Day 15.
 *
 * @see <a href="https://adventofcode.com/2024/day/15">[2024: 15] Warehouse Woes</a>
 */
public final class Day15 {

    private static final long X_COORDINATE_MULTIPLIER = 100L;
    private static final char START_SYMBOL = '@';
    private static final char BOX_SYMBOL = 'O';
    private static final char OBSTACLE_SYMBOL = '#';
    private static final char EMPTY_SYMBOL = '.';
    private static final char SPLIT_BOX_LEFT_SYMBOL = '[';
    private static final char SPLIT_BOX_RIGHT_SYMBOL = ']';
    private static final Set<Character> BOX_HALVES = Set.of(SPLIT_BOX_LEFT_SYMBOL, SPLIT_BOX_RIGHT_SYMBOL);

    private Day15() {

    }

    /**
     * Given a {@link Character} {@link Grid} containing {@value #OBSTACLE_SYMBOL} and {@value #BOX_SYMBOL}, the start {@link Point} is defined by the
     * {@value #START_SYMBOL}. The {@link #START_SYMBOL} is moved based on the provided {@link Direction}s. If it encounters a {@value #BOX_SYMBOL},
     * both the {@value #START_SYMBOL} and the {@value BOX_SYMBOL} are moved together. If either the {@value #START_SYMBOL} or any {@value BOX_SYMBOL}
     * to be moved is directly next to an {@value #OBSTACLE_SYMBOL} in the same {@link Direction} to be moved, none of the {@link Point}s can be
     * moved. There must be an {@value #EMPTY_SYMBOL} for the {@link Point}s to move into.
     *
     * <p>
     * The value of the {@link Grid} is calculated by getting the value of each {@value #BOX_SYMBOL}, then summing them all. The value of each box is:
     * <pre>
     *     (X coordinate of box * {@value #X_COORDINATE_MULTIPLIER}) + Y coordinate of box
     * </pre>
     *
     * <p>
     * It is also possible for the {@link Grid} to be expanded (width-wise only), and all boxes to be split across two spaces. This requires the input
     * {@link Grid} to have all of its {@link Character}s doubled in Y coordinate, except for the {@value #START_SYMBOL} (which has an empty space on
     * the right of its original position), and the {@value #BOX_SYMBOL} (which is split into using both the {@value #SPLIT_BOX_LEFT_SYMBOL} and
     * {@value #SPLIT_BOX_RIGHT_SYMBOL} values). These split box {@link Character}s are treated similarly to a {@value #BOX_SYMBOL}, but both halves
     * are moved as a pair. The final value is calculated the same as a non-expanded {@link Grid}, except we look for {@value #SPLIT_BOX_LEFT_SYMBOL}
     * instead.
     *
     * @param characterGrid the input {@link Character} {@link Grid}
     * @param directions    the {@link Collection} of {@link Direction}s we should move the {@value #START_SYMBOL}
     * @param expand        whether the {@link Grid} should be expanded or not
     * @return the value of the {@link Grid} after all boxes have been moved
     */
    public static long getValueOfGridAfterMoves(final Grid<Character> characterGrid, final Collection<Direction> directions, final boolean expand) {
        Grid<Character> updatedGrid = expand ? expandGridWidthwise(characterGrid) : characterGrid;
        Point currentPoint = updatedGrid.findValue(c -> c == START_SYMBOL).toList().getFirst();

        for (final Direction direction : directions) {
            final Pair<Point, Grid<Character>> movedValues = movePointAndBoxes(currentPoint, direction, updatedGrid);
            currentPoint = movedValues.first();
            updatedGrid = movedValues.second();
        }

        return updatedGrid.findValue(c -> (expand && c == SPLIT_BOX_LEFT_SYMBOL) || c == BOX_SYMBOL)
            .mapToLong(boxPoint -> (boxPoint.x() * X_COORDINATE_MULTIPLIER) + boxPoint.y())
            .sum();
    }

    private static Pair<Point, Grid<Character>> movePointAndBoxes(final Point point, final Direction direction, final Grid<Character> grid) {
        final Point nextPoint = point.move(direction);
        final Character nextValue = grid.at(nextPoint);

        return switch (nextValue) {
            case EMPTY_SYMBOL -> movePoint(point, nextPoint, grid);
            case OBSTACLE_SYMBOL -> Pair.of(point, grid);
            case BOX_SYMBOL -> moveBoxes(point, direction, grid, nextPoint);
            case SPLIT_BOX_LEFT_SYMBOL, SPLIT_BOX_RIGHT_SYMBOL -> moveSplitBoxes(point, direction, grid, nextPoint, nextValue);
            default -> throw new IllegalStateException("Unrecognized symbol at point: " + nextPoint);
        };
    }

    private static Pair<Point, Grid<Character>> movePoint(final Point from, final Point to, final Grid<Character> grid) {
        Grid<Character> updatedGrid = grid.updateAt(to, grid.at(from));
        updatedGrid = updatedGrid.updateAt(from, EMPTY_SYMBOL);
        return Pair.of(to, updatedGrid);
    }

    private static Pair<Point, Grid<Character>> moveBoxes(final Point initialPoint,
                                                          final Direction direction,
                                                          final Grid<Character> grid,
                                                          final Point nextInitialPoint) {
        final List<Point> pointsToMove = new ArrayList<>();
        Character nextValue = grid.at(nextInitialPoint);
        Point nextPoint = nextInitialPoint;

        while (nextValue != EMPTY_SYMBOL && nextValue != OBSTACLE_SYMBOL) {
            pointsToMove.add(nextPoint);
            nextPoint = nextPoint.move(direction);
            nextValue = grid.at(nextPoint);
        }

        if (nextValue == OBSTACLE_SYMBOL) {
            return Pair.of(initialPoint, grid);
        }

        final Grid<Character> updatedGrid = movePointsAlongPath(pointsToMove, direction, grid);
        return movePoint(initialPoint, initialPoint.move(direction), updatedGrid);
    }

    private static Pair<Point, Grid<Character>> moveSplitBoxes(final Point originalPoint,
                                                               final Direction direction,
                                                               final Grid<Character> grid,
                                                               final Point nextBoxHalf,
                                                               final char nextBoxValue
    ) {
        final Point otherBoxHalf = findMatch(nextBoxHalf, nextBoxValue, grid);
        final List<Point> pointsToMove = collectSplitBoxPoints(nextBoxHalf, otherBoxHalf, grid, direction);

        if (isAnyInvalid(pointsToMove, grid, direction)) {
            return Pair.of(originalPoint, grid);
        }

        final Grid<Character> updatedGrid = movePointsAlongPath(pointsToMove, direction, grid);
        return movePoint(originalPoint, originalPoint.move(direction), updatedGrid);
    }

    private static Grid<Character> movePointsAlongPath(final List<Point> pointsToMove, final Direction direction, final Grid<Character> grid) {
        Grid<Character> updatedGrid = grid;
        final int numberOfPoints = pointsToMove.size() - 1;

        for (int i = numberOfPoints; i >= 0; i--) {
            final Point from = pointsToMove.get(i);
            final Point to = from.move(direction);
            updatedGrid = updatedGrid.updateAt(to, grid.at(from));
            updatedGrid = updatedGrid.updateAt(from, EMPTY_SYMBOL);
        }
        return updatedGrid;
    }

    private static List<Point> collectSplitBoxPoints(final Point start, final Point other, final Grid<Character> grid, final Direction direction) {
        final List<Point> points = new ArrayList<>();
        points.add(start);
        points.add(other);
        final Queue<Point> queue = new LinkedList<>(points);

        while (!queue.isEmpty()) {
            final Point current = queue.poll();
            final Point nextBoxHalf = current.move(direction);
            final Character boxHalfValue = grid.at(nextBoxHalf);

            if (BOX_HALVES.contains(boxHalfValue)) {
                final Point otherBoxHalf = findMatch(nextBoxHalf, boxHalfValue, grid);

                if (!points.contains(nextBoxHalf)) {
                    points.add(nextBoxHalf);
                    queue.add(nextBoxHalf);
                }
                if (!points.contains(otherBoxHalf)) {
                    points.add(otherBoxHalf);
                    queue.add(otherBoxHalf);
                }
            }
        }

        return points;
    }

    private static boolean isAnyInvalid(final Collection<Point> pointsToCheck, final Grid<Character> grid, final Direction direction) {
        return pointsToCheck
            .stream()
            .anyMatch(p -> grid.at(p.move(direction)) == OBSTACLE_SYMBOL);
    }

    private static Point findMatch(final Point boxHalf, final char currentValue, final Grid<Character> grid) {
        final Point left = boxHalf.moveLeft();
        final Point right = boxHalf.moveRight();

        if (currentValue == SPLIT_BOX_LEFT_SYMBOL && grid.at(right) == SPLIT_BOX_RIGHT_SYMBOL) {
            return right;
        } else if (currentValue == SPLIT_BOX_RIGHT_SYMBOL && grid.at(left) == SPLIT_BOX_LEFT_SYMBOL) {
            return left;
        }

        throw new IllegalStateException("Cannot find other half for split box at Point: " + boxHalf);
    }

    private static Grid<Character> expandGridWidthwise(final Grid<Character> grid) {
        final int newGridSize = grid.size() << 1;
        final Character[][] newInternalGrid = new Character[grid.size()][newGridSize];
        final int numberOfRows = grid.numberOfRows();
        final int numberOfColumns = grid.numberOfColumns();

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                final char current = grid.at(i, j);

                switch (current) {
                    case BOX_SYMBOL -> {
                        newInternalGrid[i][(j << 1)] = SPLIT_BOX_LEFT_SYMBOL;
                        newInternalGrid[i][(j << 1) + 1] = SPLIT_BOX_RIGHT_SYMBOL;
                    }
                    case START_SYMBOL -> {
                        newInternalGrid[i][(j << 1)] = START_SYMBOL;
                        newInternalGrid[i][(j << 1) + 1] = EMPTY_SYMBOL;
                    }
                    default -> {
                        newInternalGrid[i][(j << 1)] = current;
                        newInternalGrid[i][(j << 1) + 1] = current;
                    }
                }
            }
        }

        return new Grid<>(newInternalGrid);
    }
}
