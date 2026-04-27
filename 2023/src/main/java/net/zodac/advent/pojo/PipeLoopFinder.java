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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.zodac.advent.grid.AdjacentDirection;
import net.zodac.advent.grid.AdjacentPointsSelector;
import net.zodac.advent.grid.Direction;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.tuple.Pair;
import net.zodac.advent.util.CollectionUtils;

/**
 * Utility class used to create a {@link PipeLoop}.
 */
class PipeLoopFinder { // TODO: Replace with common implementation?

    private static final int EXPECTED_NUMBER_OF_START_POINTS = 1;

    private final Grid<Pipe> grid;
    private final Predicate<? super Pipe> startPointPredicate;

    /**
     * Package-private constructor, only to be called by {@link PipeLoop}.
     *
     * @param grid                the {@link Grid}
     * @param startPointPredicate the {@link Predicate} defining how to find the start {@link Point}
     */
    PipeLoopFinder(final Grid<Pipe> grid, final Predicate<? super Pipe> startPointPredicate) {
        this.grid = grid;
        this.startPointPredicate = startPointPredicate;
    }

    /**
     * Searches through the {@link Grid} after finding the start {@link Point}, then returns the {@link PipeLoop}.
     *
     * @return the found {@link PipeLoop}
     * @throws IllegalStateException thrown if no {@link PipeLoop} can be found
     */
    PipeLoop findLoop() {
        final Point startPoint = findStartPoint();
        final Set<Point> pointsInLoop = findLoopPointsFromStartPoint(startPoint);
        final Grid<Pipe> updatedGrid = replaceStartValue(pointsInLoop, startPoint);
        return new PipeLoop(updatedGrid, pointsInLoop);
    }

    private Point findStartPoint() {
        final List<Point> startPoints = grid.findValue(startPointPredicate).toList();
        if (startPoints.size() != EXPECTED_NUMBER_OF_START_POINTS) {
            throw new IllegalStateException(String.format("Expected 1 point matching the start predicate, found: %d", startPoints.size()));
        }

        return startPoints.getFirst();
    }

    private Grid<Pipe> replaceStartValue(final Collection<Point> pointsInLoop, final Point startPoint) {
        final Pipe replacementForStartSymbol = findConnectionForPoint(startPoint, pointsInLoop, Pipe::getConnectingPipe);
        return grid.updateAt(startPoint, replacementForStartSymbol);
    }

    // TODO: Can this be modified to use common LoopFinder class?
    private Set<Point> findLoopPointsFromStartPoint(final Point startPoint) {
        Pair<Point, Point> currentAndPreviousPoint = Pair.of(startPoint, startPoint);
        final Set<Point> pointsInLoop = new LinkedHashSet<>();

        do {
            final Pipe currentPipePiece = grid.at(currentAndPreviousPoint.first());
            final Map<Direction, Pair<Point, Pipe>> nextPointsByDirection = populateNextPoints(currentAndPreviousPoint);

            currentAndPreviousPoint = findNextPipePiece(currentPipePiece, currentAndPreviousPoint, nextPointsByDirection);
            pointsInLoop.add(currentAndPreviousPoint.first());

        } while (!currentAndPreviousPoint.first().equals(startPoint));
        return Collections.unmodifiableSet(pointsInLoop);
    }

    private Map<Direction, Pair<Point, Pipe>> populateNextPoints(final Pair<Point, Point> currentAndPreviousPoint) {
        final Point currentPoint = currentAndPreviousPoint.first();
        final Point upPoint = currentPoint.moveUp();
        final Point downPoint = currentPoint.moveDown();
        final Point leftPoint = currentPoint.moveLeft();
        final Point rightPoint = currentPoint.moveRight();
        final Pipe up = findValue(grid, upPoint);
        final Pipe down = findValue(grid, downPoint);
        final Pipe left = findValue(grid, leftPoint);
        final Pipe right = findValue(grid, rightPoint);

        final Map<Direction, Pair<Point, Pipe>> nextPointsByDirection = new EnumMap<>(Direction.class);
        nextPointsByDirection.put(Direction.UP, Pair.of(upPoint, up));
        nextPointsByDirection.put(Direction.DOWN, Pair.of(downPoint, down));
        nextPointsByDirection.put(Direction.LEFT, Pair.of(leftPoint, left));
        nextPointsByDirection.put(Direction.RIGHT, Pair.of(rightPoint, right));
        return nextPointsByDirection;
    }

    private static Pair<Point, Point> findNextPipePiece(final Pipe currentPipePiece, final Pair<Point, Point> currentAndPreviousPoint,
                                                        final Map<Direction, Pair<Point, Pipe>> nextPointsByDirection) {
        final Set<Direction> directionsToCheck = getDirectionsForPipe(currentPipePiece);
        return findNextPiece(directionsToCheck, currentAndPreviousPoint, nextPointsByDirection);
    }

    private Pipe findConnectionForPoint(final Point point,
                                        final Collection<Point> pointsInLoop,
                                        final BiFunction<? super Pipe, ? super Pipe, Pipe> connectionFunction) {
        final int size = grid.numberOfRows();
        final AdjacentPointsSelector adjacentPointsSelector = AdjacentPointsSelector.bounded(false, AdjacentDirection.CARDINAL, size);
        final Set<Point> adjacentPoints = point.getAdjacentPoints(adjacentPointsSelector).collect(Collectors.toSet());
        // Order is important, as we want to go from top-left to bottom-right
        final List<Point> pipeConnections = new ArrayList<>(CollectionUtils.intersection(adjacentPoints, pointsInLoop));

        final Pipe firstConnection = grid.at(pipeConnections.getFirst());
        final Pipe lastConnection = grid.at(pipeConnections.getLast());
        return connectionFunction.apply(firstConnection, lastConnection);
    }

    private static Set<Direction> getDirectionsForPipe(final Pipe pipe) {
        return switch (pipe) {
            case BOTTOM_LEFT_CORNER -> Set.of(Direction.UP, Direction.RIGHT);
            case BOTTOM_RIGHT_CORNER -> Set.of(Direction.UP, Direction.LEFT);
            case HORIZONTAL -> Set.of(Direction.LEFT, Direction.RIGHT);
            case TOP_RIGHT_CORNER -> Set.of(Direction.DOWN, Direction.LEFT);
            case TOP_LEFT_CORNER -> Set.of(Direction.DOWN, Direction.RIGHT);
            case VERTICAL -> Set.of(Direction.DOWN, Direction.UP);
            case START -> Set.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
            case EMPTY -> Set.of();
        };
    }

    private static Pair<Point, Point> findNextPiece(final Collection<Direction> directionsToCheck,
                                                    final Pair<Point, Point> currentState,
                                                    final Map<Direction, Pair<Point, Pipe>> nextPointsByDirection) {
        for (final Direction directionToCheck : directionsToCheck) {
            if (!nextPointsByDirection.containsKey(directionToCheck)) {
                throw new IllegalStateException(String.format("Cannot find mapping for %s: %s", Direction.class.getSimpleName(), directionToCheck));
            }

            final Pair<Point, Pipe> pipeValueByPoint = nextPointsByDirection.get(directionToCheck);
            final Point point = pipeValueByPoint.first();
            final Pipe value = pipeValueByPoint.second();

            if (!point.equals(currentState.second()) && isConnectionFromDirection(directionToCheck, value)) {
                return Pair.of(point, currentState.first());
            }
        }
        throw new IllegalStateException(String.format("Cannot next piece for: %s", currentState));
    }

    private static boolean isConnectionFromDirection(final Direction direction, final Pipe pipe) {
        return switch (direction) {
            case UP -> Set.of(Pipe.VERTICAL, Pipe.TOP_RIGHT_CORNER, Pipe.TOP_LEFT_CORNER, Pipe.START).contains(pipe);
            case DOWN -> Set.of(Pipe.VERTICAL, Pipe.BOTTOM_RIGHT_CORNER, Pipe.BOTTOM_LEFT_CORNER, Pipe.START).contains(pipe);
            case LEFT -> Set.of(Pipe.HORIZONTAL, Pipe.TOP_LEFT_CORNER, Pipe.BOTTOM_LEFT_CORNER, Pipe.START).contains(pipe);
            case RIGHT -> Set.of(Pipe.HORIZONTAL, Pipe.TOP_RIGHT_CORNER, Pipe.BOTTOM_RIGHT_CORNER, Pipe.START).contains(pipe);
            case DOWN_LEFT, DOWN_RIGHT, UP_LEFT, UP_RIGHT -> false; // No diagonal movements allowed
            case INVALID -> throw new IllegalStateException("Cannot look for a connection from: " + direction);
        };
    }

    private static Pipe findValue(final Grid<Pipe> grid, final Point point) {
        try {
            return grid.at(point);
        } catch (final ArrayIndexOutOfBoundsException ignored) {
            return Pipe.EMPTY;
        }
    }
}
