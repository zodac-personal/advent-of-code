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

package net.zodac.advent.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import net.zodac.advent.pojo.tuple.Pair;
import net.zodac.advent.util.StringUtils;

/**
 * Simple POJO defining a point on a coordinate system.
 *
 * @param x the X coordinate, or row
 * @param y the Y coordinate, or column
 */
public record Point(int x, int y) implements Comparable<Point> {

    private static final int NUMBER_OF_COORDINATES_IN_A_POINT = 2;
    private static final int DEFAULT_MOVE_DISTANCE = 1;

    /**
     * Creates a {@link Point} starting at the provided (x, y) coordinates.
     *
     * @param x the X coordinate, or row
     * @param y the Y coordinate, or column
     * @return the created {@link Point}
     */
    public static Point of(final int x, final int y) {
        return new Point(x, y);
    }

    /**
     * Creates a {@link Point} starting at the origin (0, 0).
     *
     * @return the created {@link Point}
     */
    public static Point atOrigin() {
        return of(0, 0);
    }

    /**
     * Creates a {@link Point} from an input {@link String} that contains two separate {@link Integer}s. The values can be separated by any character,
     * but there must only be two {@link Integer}s in total.
     *
     * @param input the input to parse
     * @return the {@link Point}
     * @throws IllegalArgumentException thrown if the input does not have exactly <b>2</b> {@link Integer}s
     * @see StringUtils#collectNumbersInOrder(CharSequence)
     */
    public static Point parse(final String input) {
        final List<Long> coordinates = StringUtils.collectNumbersInOrder(input);

        if (coordinates.size() != NUMBER_OF_COORDINATES_IN_A_POINT) {
            throw new IllegalArgumentException(String.format("Cannot find two values in input: '%s'", input));
        }

        if (coordinates.getFirst() > Integer.MAX_VALUE || coordinates.getLast() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(String.format("Input has values larger than an integer: '%s'", input));
        }

        if (coordinates.getFirst() < Integer.MIN_VALUE || coordinates.getLast() < Integer.MIN_VALUE) {
            throw new IllegalArgumentException(String.format("Input has values smaller than an integer: '%s'", input));
        }

        return of(coordinates.getFirst().intValue(), coordinates.getLast().intValue());
    }

    /**
     * Returns a {@link Point} with the current {@link Point}'s X and Y values flipped.
     *
     * <p>
     * This can be useful for some problems where the input is in a different orientation and debugging can be more difficult.
     *
     * @return the flipped {@link Point}
     */
    public Point flip() {
        return of(y, x);
    }

    /**
     * Creates a {@link List} of {@link Point}s from an input in the format:
     * <pre>
     *     [x1],[y1] -> [x2],[y2] -> [x3],[y3]... -> [xn],[yn]
     * </pre>
     *
     * @param input the input to parse
     * @return the {@link List} of {@link Point}s
     */
    public static List<Point> ofMany(final CharSequence input) {
        final List<Point> points = new ArrayList<>();
        final List<Long> coordinates = StringUtils.collectNumbersInOrder(input);

        final int numberOfCoordinates = coordinates.size();
        for (int i = 0; i < numberOfCoordinates; i += 2) {
            points.add(of(coordinates.get(i).intValue(), coordinates.get(i + 1).intValue()));
        }

        return points;
    }

    /**
     * Returns the delta in X and Y coordinates between the current {@link Point} and the provided {@link Point}.
     *
     * @param other the other {@link Point}
     * @return the delta for X and Y coordinates to the other {@link Point}
     */
    public Pair<Integer, Integer> deltaTo(final Point other) {
        return Pair.of(other.x - x, other.y - y);
    }

    /**
     * Returns the {@link Direction} between the current {@link Point} and the provided {@link Point}.
     *
     * <p>
     * If the two {@link Point}s are more than a single unit of distance away in any {@link Direction}, {@link Direction#INVALID} will be returned.
     *
     * @param other the other {@link Point}
     * @return the {@link Direction} to the other {@link Point}
     */
    public Direction directionTo(final Point other) {
        final int deltaX = other.x - x;
        final int deltaY = other.y - y;

        return switch (deltaX + "," + deltaY) {
            case "0,1" -> Direction.RIGHT;
            case "0,-1" -> Direction.LEFT;
            case "1,0" -> Direction.DOWN;
            case "-1,0" -> Direction.UP;
            case "1,1" -> Direction.DOWN_RIGHT;
            case "1,-1" -> Direction.DOWN_LEFT;
            case "-1,1" -> Direction.UP_RIGHT;
            case "-1,-1" -> Direction.UP_LEFT;
            default -> Direction.INVALID;
        };
    }

    /**
     * Calculates the Manhattan distance between this {@link Point} and another. The Manhattan distance can be considered as how many spaces up/down
     * and left/right between the {@link Point}s
     *
     * @param other the other {@link Point}
     * @return the distance to the other {@link Point}
     * @see <a href="https://en.wikipedia.org/wiki/Taxicab_geometry">Manhattan Distance</a>
     */
    public long distanceTo(final Point other) {
        return (long) Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    /**
     * Returns a new {@link Point} which has moved the current {@link Point} {@value DEFAULT_MOVE_DISTANCE} spaces in the {@link Direction} specified.
     *
     * @param direction the {@link Direction} to move
     * @return the moved {@link Point}
     * @throws IllegalArgumentException thrown if {@link Direction#INVALID}
     */
    public Point move(final Direction direction) {
        return switch (direction) {
            case DOWN -> moveDown();
            case DOWN_LEFT -> moveDownLeft();
            case DOWN_RIGHT -> moveDownRight();
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
            case UP -> moveUp();
            case UP_LEFT -> moveUpLeft();
            case UP_RIGHT -> moveUpRight();
            case INVALID -> throw new IllegalArgumentException(String.format("Cannot move in '%s' direction", direction));
        };
    }

    /**
     * Returns a new {@link Point} which has moved {@code deltaX} spaces for the X coordinate, and {@code deltaY} spaces for the Y coordinate.
     *
     * @param deltaX the change in the X coordinate
     * @param deltaY the change in the Y coordinate
     * @return the moved {@link Point}
     */
    public Point move(final int deltaX, final int deltaY) {
        return of(x + deltaX, y + deltaY);
    }

    /**
     * Returns a new {@link Point} which has moved the current {@link Point} {@value DEFAULT_MOVE_DISTANCE} spaces {@link Direction#UP}.
     *
     * @return the moved {@link Point}
     */
    public Point moveUp() {
        return move(-DEFAULT_MOVE_DISTANCE, 0);
    }

    /**
     * Returns a new {@link Point} which has moved the current {@link Point} {@value DEFAULT_MOVE_DISTANCE} spaces {@link Direction#DOWN}.
     *
     * @return the moved {@link Point}
     */
    public Point moveDown() {
        return move(DEFAULT_MOVE_DISTANCE, 0);
    }

    /**
     * Returns a new {@link Point} which has moved the current {@link Point} {@value DEFAULT_MOVE_DISTANCE} spaces {@link Direction#LEFT}.
     *
     * @return the moved {@link Point}
     */
    public Point moveLeft() {
        return move(0, -DEFAULT_MOVE_DISTANCE);
    }

    /**
     * Returns a new {@link Point} which has moved the current {@link Point} {@value DEFAULT_MOVE_DISTANCE} spaces {@link Direction#RIGHT}.
     *
     * @return the moved {@link Point}
     */
    public Point moveRight() {
        return move(0, DEFAULT_MOVE_DISTANCE);
    }

    /**
     * Returns a new {@link Point} which has moved the current {@link Point} {@value DEFAULT_MOVE_DISTANCE} spaces {@link Direction#UP} and
     * {@link Direction#LEFT}.
     *
     * @return the moved {@link Point}
     */
    public Point moveUpLeft() {
        return move(-DEFAULT_MOVE_DISTANCE, -DEFAULT_MOVE_DISTANCE);
    }

    /**
     * Returns a new {@link Point} which has moved the current {@link Point} {@value DEFAULT_MOVE_DISTANCE} spaces {@link Direction#UP} and
     * {@link Direction#RIGHT}.
     *
     * @return the moved {@link Point}
     */
    public Point moveUpRight() {
        return move(-DEFAULT_MOVE_DISTANCE, DEFAULT_MOVE_DISTANCE);
    }

    /**
     * Returns a new {@link Point} which has moved the current {@link Point} {@value DEFAULT_MOVE_DISTANCE} spaces {@link Direction#DOWN} and
     * {@link Direction#LEFT}.
     *
     * @return the moved {@link Point}
     */
    public Point moveDownLeft() {
        return move(DEFAULT_MOVE_DISTANCE, -DEFAULT_MOVE_DISTANCE);
    }

    /**
     * Returns a new {@link Point} which has moved the current {@link Point} {@value DEFAULT_MOVE_DISTANCE} spaces {@link Direction#DOWN} and
     * {@link Direction#RIGHT}.
     *
     * @return the moved {@link Point}
     */
    public Point moveDownRight() {
        return move(DEFAULT_MOVE_DISTANCE, DEFAULT_MOVE_DISTANCE);
    }

    /**
     * Returns a {@link Set} of the adjacent {@link Point}s for the current {@link Point}.
     *
     * @param adjacentPointsSelector definition of what to consider an adjacent {@link Point}
     * @return a {@link Stream} of the adjacent {@link Point}s according to the {@link AdjacentPointsSelector}
     */
    public Stream<Point> getAdjacentPoints(final AdjacentPointsSelector adjacentPointsSelector) {
        final Collection<Point> adjacentPoints = new HashSet<>();

        // Current point
        if (adjacentPointsSelector.withSelf()) {
            adjacentPoints.add(this);
        }

        if (AdjacentDirection.isCardinal(adjacentPointsSelector.adjacentDirection())) {
            final Set<Point> directAdjacentPoints = getDirectAdjacentPoints(adjacentPointsSelector);
            adjacentPoints.addAll(directAdjacentPoints);
        }

        if (AdjacentDirection.isDiagonal(adjacentPointsSelector.adjacentDirection())) {
            final Set<Point> diagonalAdjacentPoints = getDiagonalAdjacentPoints(adjacentPointsSelector);
            adjacentPoints.addAll(diagonalAdjacentPoints);
        }

        return adjacentPoints.stream();
    }

    private Set<Point> getDirectAdjacentPoints(final AdjacentPointsSelector adjacentPointsSelector) {
        final Set<Point> adjacentPoints = new HashSet<>();

        if (adjacentPointsSelector.allowOutOfBounds() || x - DEFAULT_MOVE_DISTANCE >= 0) {
            adjacentPoints.add(moveUp());
        }

        if (adjacentPointsSelector.allowOutOfBounds() || x + DEFAULT_MOVE_DISTANCE < adjacentPointsSelector.gridSize()) {
            adjacentPoints.add(moveDown());
        }

        if (adjacentPointsSelector.allowOutOfBounds() || y + DEFAULT_MOVE_DISTANCE < adjacentPointsSelector.gridSize()) {
            adjacentPoints.add(moveRight());
        }

        if (adjacentPointsSelector.allowOutOfBounds() || y - DEFAULT_MOVE_DISTANCE >= 0) {
            adjacentPoints.add(moveLeft());
        }

        return adjacentPoints;
    }

    private Set<Point> getDiagonalAdjacentPoints(final AdjacentPointsSelector adjacentPointsSelector) {
        final Set<Point> adjacentPoints = new HashSet<>();

        if (adjacentPointsSelector.allowOutOfBounds()
            || (x - DEFAULT_MOVE_DISTANCE >= 0 && y + DEFAULT_MOVE_DISTANCE < adjacentPointsSelector.gridSize())) {
            adjacentPoints.add(moveUpRight());
        }

        if (adjacentPointsSelector.allowOutOfBounds()
            || (x + DEFAULT_MOVE_DISTANCE < adjacentPointsSelector.gridSize() && y - DEFAULT_MOVE_DISTANCE >= 0)) {
            adjacentPoints.add(moveDownLeft());
        }

        if (adjacentPointsSelector.allowOutOfBounds()
            || (x - DEFAULT_MOVE_DISTANCE >= 0 && y - DEFAULT_MOVE_DISTANCE >= 0)) {
            adjacentPoints.add(moveUpLeft());
        }

        if (adjacentPointsSelector.allowOutOfBounds()
            || (x + DEFAULT_MOVE_DISTANCE < adjacentPointsSelector.gridSize() && y + DEFAULT_MOVE_DISTANCE < adjacentPointsSelector.gridSize())) {
            adjacentPoints.add(moveDownRight());
        }

        return adjacentPoints;
    }

    /**
     * Finds all {@link Point}s in a line from the current {@link Point} in the given {@link Direction} for {@code numberOfAdditionalPoints}
     * additional {@link Point}s. Note that the current {@link Point} is included in the returned {@link List}.
     *
     * @param direction                the {@link Direction} in which to find {@link Point}s
     * @param numberOfAdditionalPoints the length of the line to form
     * @return the {@link Point}s in the defined line
     */
    public List<Point> findPointsInLine(final Direction direction, final int numberOfAdditionalPoints) {
        final List<Point> output = new ArrayList<>();
        output.add(this);

        Point currentPoint = this;
        for (int i = 0; i < numberOfAdditionalPoints; i++) {
            final Point nextPoint = currentPoint.move(direction);
            output.add(nextPoint);
            currentPoint = nextPoint;
        }

        return output;
    }

    /**
     * Moves the {@link Point} a {@code numberOfMovements} number of times given the provided delta values for the X and Y co-ordinates. The bounds
     * of number of rows and columns is also provided.
     *
     * <p>
     * If the {@link Point} exceeds the boundary in either direction, it 'wraps around' to the other side of the bounded area.
     *
     * @param deltaX            the number of spaces in the X coordinate to move
     * @param deltaY            the number of spaces in the X coordinate to move
     * @param numberOfMovements the number of movements to apply in the X and Y directions
     * @param numberOfRows      the number of rows bounding the area
     * @param numberOfColumn    the number of columns bounding the area
     * @return the final destination {@link Point}
     */
    public Point wrapAround(final int deltaX, final int deltaY, final int numberOfMovements, final int numberOfRows, final int numberOfColumn) {
        final int newX = wrapValue(x, deltaX, numberOfRows, numberOfMovements);
        final int newY = wrapValue(y, deltaY, numberOfColumn, numberOfMovements);
        return of(newX, newY);
    }

    private static int wrapValue(final int startValue, final int deltaValue, final int maxValue, final int numberOfMovements) {
        final int newVal = (startValue + (deltaValue * numberOfMovements)) % maxValue;
        return newVal >= 0 ? newVal : (maxValue + newVal);
    }

    @Override
    public int compareTo(final Point other) {
        final int compareX = Integer.compare(x, other.x);
        if (compareX != 0) {
            return compareX;
        }
        return Integer.compare(y, other.y);
    }
}
