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
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.GridFactory;
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.Line;

/**
 * Solution for 2021, Day 5.
 *
 * @see <a href="https://adventofcode.com/2021/day/5">AoC 2021, Day 5</a>
 */
public final class Day05 {

    private static final int NUMBER_SIGNIFYING_OVERLAP = 2;

    private Day05() {

    }

    /**
     * Draws only the horizontal and vertical {@link Line}s in the supplied {@code coordinateLines} onto a {@link Integer} {@link Grid} and counts the
     * number of points that have overlapping {@link Line}s.
     *
     * @param coordinateLines the {@link Line}s to be drawn on the {@link Integer} {@link Grid}
     * @return the number of overlapping points
     * @see Line#isHorizontal()
     * @see Line#isVertical()
     */
    public static long addHorizontalAndVerticalLinesAndReturnOverlap(final Collection<Line> coordinateLines) {
        final int maxGridSize = getMaxGridSize(coordinateLines);
        Grid<Integer> integerGrid = GridFactory.ofIntegersWithSize(maxGridSize);

        for (final Line coordinateLine : coordinateLines) {
            integerGrid = addLine(integerGrid, coordinateLine, false);
        }

        return integerGrid.findValue(integer -> integer >= NUMBER_SIGNIFYING_OVERLAP).count();
    }

    /**
     * Draws the horizontal, vertical and 'perfect' diagonal {@link Line}s in the supplied {@code coordinateLines} onto a {@link Integer} {@link Grid}
     * and counts the number of points that have overlapping {@link Line}s.
     *
     * @param coordinateLines the {@link Line}s to be drawn on the {@link Integer} {@link Grid}
     * @return the number of overlapping points
     * @see Line#isHorizontal()
     * @see Line#isVertical()
     * @see Line#isPerfectDiagonal()
     */
    public static long addAllLinesAndReturnOverlap(final Collection<Line> coordinateLines) {
        final int maxGridSize = getMaxGridSize(coordinateLines);
        Grid<Integer> integerGrid = GridFactory.ofIntegersWithSize(maxGridSize);

        for (final Line coordinateLine : coordinateLines) {
            integerGrid = addLine(integerGrid, coordinateLine, true);
        }

        return integerGrid.findValue(integer -> integer >= NUMBER_SIGNIFYING_OVERLAP).count();
    }

    private static int getMaxGridSize(final Collection<Line> coordinateLines) {
        int max = 0;
        for (final Line coordinateLine : coordinateLines) {
            if (coordinateLine.maxCoordinateValue() > max) {
                max = coordinateLine.maxCoordinateValue();
            }
        }

        // Coordinates (0,0) would need a grid of size 1, not 0, so make sure to add 1
        return max + 1;
    }

    // Don't think this is needed in the base Grid class, but maybe one day...
    private static Grid<Integer> addLine(final Grid<Integer> integerGrid, final Line line, final boolean allowAllLines) {
        if (!allowAllLines && line.isPerfectDiagonal()) {
            return integerGrid;
        }

        final Integer[][] internalGrid = integerGrid.getInternalGrid();
        for (final Point point : line.getPointsInLine()) {
            internalGrid[point.y()][point.x()]++;
        }

        return new Grid<>(internalGrid);
    }
}
