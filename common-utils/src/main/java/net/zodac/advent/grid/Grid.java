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
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.zodac.advent.pojo.RotationDirection;
import net.zodac.advent.util.ArrayUtils;

/**
 * Abstract class defining a grid of {@link Point}s.
 *
 * @param <E> the type of the {@link Point}s on the {@link Grid}
 */
// TODO: Lots of 'x/y', 'i/j', 'row/column'; be more consistent
public class Grid<E> {

    /**
     * The length (or width) of the {@link Grid}.
     */
    protected final int gridSize;

    /**
     * The values of the {@link Grid}.
     */
    protected final E[][] internalGrid;

    private final int elementsInGrid;

    /**
     * Default constructor.
     *
     * @param internalGrid the actual {@link Grid} represented as a 2D array.
     */
    public Grid(final E[][] internalGrid) {
        gridSize = internalGrid.length;
        this.internalGrid = ArrayUtils.deepCopy(internalGrid);
        elementsInGrid = gridSize * gridSize;
    }

    /**
     * Constructor that creates the internal {@code grid} and initialises the data.
     *
     * @param gridSize     the size of the {@link Grid}.
     * @param internalGrid the actual {@link Grid} represented as a 2D array.
     * @param initialValue the initial value for each {@link Point} in the {@link Grid}
     */
    public Grid(final int gridSize, final E[][] internalGrid, final E initialValue) {
        this.gridSize = gridSize;
        this.internalGrid = ArrayUtils.deepCopy(internalGrid);
        elementsInGrid = gridSize * gridSize;

        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                this.internalGrid[row][column] = initialValue;
            }
        }
    }

    /**
     * Given a {@link List}s of {@link String}s where each {@link String} represents a 2D array of {@link Character}s, we convert to a 2D array and
     * create a new instance of {@link Grid}. Each value in the 2D array is determined by converting the {@link Character} using the {@code converter}
     * {@link Function}.
     *
     * @param gridValues the {@link String}s representing a 2D array (where each character in the {@link String} is an element in the array)
     * @param converter  the {@link Function} to convert a {@link Character} from the input into the correct type for the 2D array
     * @param <E>        the type of the {@link Grid}
     * @return the created {@link Grid}
     * @throws IllegalArgumentException thrown if input is empty
     * @see ArrayUtils#toArrayOfArrays(List, Function)
     */
    public static <E> Grid<E> parseGrid(final List<String> gridValues, final Function<? super Character, ? extends E> converter) {
        if (gridValues.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        final E[][] internalArray = ArrayUtils.toArrayOfArrays(gridValues, converter);
        return new Grid<>(internalArray); // TODO: Any scenario where grid is not a square? Enforce if not, document otherwise
    }

    /**
     * Count the value of all {@link Point}s in the {@link Grid}. The actual value of each {@link Point} is defined by the input {@code evaluator}.
     *
     * @param evaluator {@link ToIntFunction} used to convert the value of type {@code E} into an {@link Integer}
     * @return the sum of all {@link Point}s in the {@link Grid}
     */
    public long sumValues(final ToIntFunction<? super E> evaluator) {
        int count = 0;

        for (int row = 0; row < internalGrid.length; row++) {
            for (int column = 0; column < internalGrid[0].length; column++) {
                final E value = at(row, column);
                count += evaluator.applyAsInt(value);
            }
        }

        return count;
    }

    /**
     * Sets the corners of the {@link Grid} to the input {@code newValue}.
     *
     * @param newValue the new value for the corners
     * @return a new instance of {@link Grid} with the updated corners
     */
    public Grid<E> updateCorners(final E newValue) {
        final E[][] internalGridCopy = ArrayUtils.deepCopy(internalGrid);
        internalGridCopy[0][0] = newValue;
        internalGridCopy[0][gridSize - 1] = newValue;
        internalGridCopy[gridSize - 1][0] = newValue;
        internalGridCopy[gridSize - 1][gridSize - 1] = newValue;

        return new Grid<>(internalGridCopy);
    }

    /**
     * Draws a box on the {@link Grid}, where each {@link Point} is updated based according to the update
     * {@link Function}. All values inside the box are also updated.
     *
     * <p>
     * For example, starting with a 10x10 grid where the values are {@link Integer}s:
     * <pre>
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     * </pre>
     *
     * <p>
     * Given the {@link Point}s (0, 0) and (2, 4) with the following update function:
     * {@snippet :
     *  final Function<Integer, Integer> updateFunction = (integer) -> integer + 1;
     *}
     *
     * <p>
     * In this case, each value in the box will be incremented by one, giving an updated {@link Grid} like:
     * <pre>
     *     1 1 1 1 1 0 0 0 0 0
     *     1 1 1 1 1 0 0 0 0 0
     *     1 1 1 1 1 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     * </pre>
     *
     * <p>
     * Following up with {@link Point}s (0, 1) and (0, 9) with the next update {@link Function}:
     * {@snippet :
     *  final Function<Integer, Integer> updateFunction = (integer) -> integer + 2;
     *}
     *
     * <p>
     * This would give an output of:
     * <pre>
     *     1 3 3 3 3 2 2 2 2 2
     *     1 1 1 1 1 0 0 0 0 0
     *     1 1 1 1 1 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     * </pre>
     *
     * <p>
     * And finally with {@link Point}s (0, 2) and (3, 3) with the last update {@link Function}:
     * {@snippet :
     *       final Function<Integer, Integer> updateFunction = (integer) -> integer - 1;
     *}
     *
     * <p>
     * This results in a final {@link Grid} of:
     * <pre>
     *     1 3 2 2 3 2 2 2 2 2
     *     1 1 0 0 1 0 0 0 0 0
     *     1 1 0 0 1 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     *     0 0 0 0 0 0 0 0 0 0
     * </pre>
     *
     * @param x1             the first x coordinate
     * @param y1             the first y coordinate
     * @param x2             the second x coordinate
     * @param y2             the second y coordinate
     * @param updateFunction the {@link Function} to update the internal {@link Grid} value based on the current value
     */
    public void drawBox(final int x1, final int y1, final int x2, final int y2, final UnaryOperator<E> updateFunction) {
        if (x1 < 0 || y1 < 0) {
            throw new IllegalArgumentException(String.format("x1, y1 must be at least 0, found: (%s, %s)", x1, y1));
        }

        if (x2 > numberOfRows() || y2 > numberOfColumns()) {
            throw new IllegalArgumentException(String.format("x2, y2 must be at less than (%s, %s), found: (%s, %s)", numberOfRows(),
                numberOfColumns(), x2, y2));
        }

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                internalGrid[x][y] = updateFunction.apply(internalGrid[x][y]);
            }
        }
    }

    /**
     * Creates a copy of the existing {@link Grid}, then overwrites the value at the given {@link Point} with the supplied {@code value}.
     *
     * @param point the {@link Point} to update
     * @param value the new value to be set
     * @return the new {@link Grid}
     */
    public Grid<E> updateAt(final Point point, final E value) {
        final E[][] newGrid = ArrayUtils.deepCopy(internalGrid);
        newGrid[point.x()][point.y()] = value;
        return new Grid<>(newGrid);
    }

    /**
     * Checks if the input point is one of the corners of the {@link Grid}.
     *
     * @param row    the x coordinate
     * @param column the y coordinate
     * @return {@code true} if the input {@link Point} is a corner of the {@link Grid}
     */
    public boolean isCorner(final int row, final int column) {
        return (row == 0 && column == 0)
            || (row == gridSize - 1 && column == gridSize - 1)
            || (row == 0 && column == gridSize - 1)
            || (row == gridSize - 1 && column == 0);
    }

    /**
     * Returns the backing 2D array of the {@link Grid}.
     *
     * @return the {@link Grid} as a 2D array
     */
    public E[][] getInternalGrid() {
        return ArrayUtils.deepCopy(internalGrid);
    }

    /**
     * Returns the number of rows in the {@link Grid}.
     *
     * @return the number of rows
     */
    public int numberOfRows() {
        return internalGrid.length;
    }

    /**
     * Returns the number of columns in the {@link Grid}.
     *
     * @return the number of columns
     */
    public int numberOfColumns() {
        return internalGrid[0].length;
    }

    /**
     * The size of the {@link Grid}. Equivalent to {@link #numberOfRows()}.
     *
     * @return the size of the {@link Grid}
     */
    public int size() {
        return numberOfRows();
    }

    /**
     * Returns the total number of elements in the {@link Grid}.
     *
     * @return the number of elements
     */
    public int elementsInGrid() {
        return elementsInGrid;
    }

    /**
     * Returns the value at the given ({@code row}, {@code column}).
     *
     * @param row    the row
     * @param column the column
     * @return the value
     */
    public E at(final int row, final int column) {
        return internalGrid[row][column];
    }

    /**
     * Returns the value at the given {@link Point}.
     *
     * @param point the {@link Point}
     * @return the value
     */
    public E at(final Point point) {
        return internalGrid[point.x()][point.y()];
    }

    /**
     * Returns the row of values at the given {@code row}.
     *
     * @param row the row
     * @return the values
     */
    public E[] rowAt(final int row) {
        return internalGrid[row];
    }

    /**
     * Checks if the given {@link Point} exists in the bounds of the {@link Grid}.
     *
     * @param point the {@link Point} to check
     * @return {@code true} if the {@link Point} is valid for this {@link Grid}
     */
    // TODO: Update all boundary checks to use this instead? Less efficient, but simpler
    //   Name? Maybe isInBounds() would be better?
    public boolean exists(final Point point) {
        try {
            at(point);
            return true;
        } catch (final ArrayIndexOutOfBoundsException ignored) {
            return false;
        }
    }

    /**
     * Returns all {@link Point}s in the {@link Grid}.
     *
     * @return all {@link Point}s
     */
    public Set<Point> allPoints() {
        final Set<Point> allPoints = new TreeSet<>();
        for (int i = 0; i < internalGrid.length; i++) {
            for (int j = 0; j < internalGrid[0].length; j++) {
                allPoints.add(Point.of(i, j));
            }
        }
        return allPoints;
    }

    /**
     * Returns the {@link Point}s along the perimeter of the {@link Grid}, keyed by the source {@link Direction} the {@link Point}s came from. For
     * example, any {@link Point}s from the top row will have the {@link Direction} {@link Direction#UP}, and so on.
     *
     * @return the {@link Point}s along the perimeter.
     */
    public Map<Direction, Set<Point>> getPerimeterPoints() {
        final Map<Direction, Set<Point>> perimeterPointsByDirection = new EnumMap<>(Direction.class);

        final Set<Point> firstRow = new HashSet<>();
        final Set<Point> lastRow = new HashSet<>();
        for (int j = 0; j < internalGrid[0].length; j++) {
            firstRow.add(Point.of(0, j));
            lastRow.add(Point.of(numberOfRows() - 1, j));
        }
        perimeterPointsByDirection.put(Direction.UP, firstRow);
        perimeterPointsByDirection.put(Direction.DOWN, lastRow);

        final Set<Point> firstColumn = new HashSet<>();
        final Set<Point> lastColumn = new HashSet<>();
        for (int i = 0; i < internalGrid.length; i++) {
            firstColumn.add(Point.of(i, 0));
            lastColumn.add(Point.of(i, numberOfColumns() - 1));
        }
        perimeterPointsByDirection.put(Direction.LEFT, firstColumn);
        perimeterPointsByDirection.put(Direction.RIGHT, lastColumn);

        return perimeterPointsByDirection;
    }

    /**
     * Searches through all values in the {@link Grid} looking for the {@code wantedValue}. Returns all {@link Point}s which contain the wanted value.
     *
     * @param predicate the {@link Predicate} defining the wanted value
     * @return a {@link Stream} of the {@link Point}s which match the {@link Predicate}
     */
    public Stream<Point> findValue(final Predicate<? super E> predicate) {
        final Collection<Point> points = new HashSet<>();
        for (int i = 0; i < internalGrid.length; i++) {
            final E[] row = internalGrid[i];

            for (int j = 0; j < row.length; j++) {
                final E value = row[j];

                if (predicate.test(value)) {
                    points.add(Point.of(i, j));
                }
            }
        }
        return points.stream();
    }

    /**
     * Iterates through all columns of the {@link Grid}, and checks whether they pass the provided {@link Predicate}. If they pass, the index of that
     * column is returned.
     *
     * @param predicate the {@link Predicate} to test all columns against
     * @return a {@link Stream} of indexes of all matching columns
     */
    public Stream<Integer> findColumnsWith(final Predicate<? super E> predicate) {
        final Collection<Integer> matchingColumns = new ArrayList<>();
        for (int i = 0; i < internalGrid[0].length; i++) {
            boolean match = true;

            for (final E[] characters : internalGrid) {
                final E value = characters[i];
                if (!predicate.test(value)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                matchingColumns.add(i);
            }
        }
        return matchingColumns.stream();
    }

    /**
     * Iterates through all rows of the {@link Grid}, and checks whether they pass the provided {@link Predicate}. If they pass, the index of that
     * row is returned.
     *
     * @param predicate the {@link Predicate} to test all rows against
     * @return a {@link Stream} of indexes of all matching rows
     */
    public Stream<Integer> findRowsWith(final Predicate<? super E> predicate) {
        final Collection<Integer> matchingRows = new HashSet<>();
        for (int i = 0; i < internalGrid.length; i++) {
            final E[] row = internalGrid[i];
            boolean match = true;

            for (final E value : row) {
                if (!predicate.test(value)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                matchingRows.add(i);
            }

        }
        return matchingRows.stream();
    }

    /**
     * Rotates the provided {@link Grid} 90° in the {@code rotationDirection}.
     *
     * @param rotationDirection the direction in which to rotate the array
     * @return the rotated {@link Grid}
     * @see ArrayUtils#rotate(Object[][], RotationDirection)
     */
    public Grid<E> rotate(final RotationDirection rotationDirection) {
        return new Grid<>(ArrayUtils.rotate(internalGrid, rotationDirection));
    }

    /**
     * Returns all {@link Point}s along the border of the {@link Grid}.
     *
     * @return the border {@link Point}s
     */
    public Set<Point> borderPoints() {
        final Set<Point> borderPoints = new HashSet<>();

        for (int col = 0; col < internalGrid[0].length; col++) {
            borderPoints.add(Point.of(0, col)); // Top row
            borderPoints.add(Point.of(internalGrid.length - 1, col)); // Bottom row
        }

        for (int row = 1; row < internalGrid.length; row++) {
            borderPoints.add(Point.of(row, 0)); // Left column
            borderPoints.add(Point.of(row, internalGrid[0].length - 1)); // Right column
        }

        return borderPoints;
    }

    /**
     * Prints the content of the {@link Grid}.
     *
     * @param withHeaders       whether to also print numeric headers for each column and row
     * @param transformFunction {@link Function} to convert a value into a {@link Character} to be output on the screen
     */
    @SuppressWarnings("unused") // Only used for debugging
    public void print(final boolean withHeaders, final Function<? super E, Character> transformFunction) {
        if (withHeaders) {
            log(" | ");
            for (int i = 0; i < internalGrid[0].length; i++) {
                log(i % 10);
            }

            logLine("\n" + "-".repeat(internalGrid[0].length) + "---");
        }

        for (int i = 0; i < internalGrid.length; i++) {
            final E[] row = internalGrid[i];

            if (withHeaders) {
                log((i % 10) + "| ");
            }

            for (final E val : row) {
                log(transformFunction.apply(val));
            }
            logLine("");
        }
        logLine("");
    }

    private static void logLine(final Object input) {
        log(String.valueOf(input) + '\n');
    }

    // Creating function to keep all suppressions in one place
    private static void log(final Object input) {
        //noinspection ALL
        System.out.print(input); // NOPMD: System.out.print() - Printing to stdout for debugging
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || (obj instanceof final Grid<?> otherGrid && Arrays.deepEquals(internalGrid, otherGrid.internalGrid));
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(internalGrid);
    }
}
