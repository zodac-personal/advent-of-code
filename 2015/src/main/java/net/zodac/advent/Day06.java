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
import net.zodac.advent.pojo.GridUpdateInstruction;
import net.zodac.advent.pojo.tuple.Triple;

/**
 * Solution for 2015, Day 6.
 *
 * @see <a href="https://adventofcode.com/2015/day/6">AoC 2015, Day 6</a>
 */
public final class Day06 {

    private static final int GRID_SIZE = 1_000;

    private Day06() {

    }

    /**
     * Creates a {@link Boolean} {@link Grid} and sets {@link Point} based on the input {@link GridUpdateInstruction}.
     *
     * @param instructionsAndPoints the {@link Point}s defining a box on the {@link Boolean} {@link Grid}, with the {@link GridUpdateInstruction}
     * @return the number of {@link Boolean} {@link Grid} {@link Point}s that have been turned on
     */
    public static long countSwitchedOnLights(final Collection<Triple<GridUpdateInstruction, Point, Point>> instructionsAndPoints) {
        final Grid<Boolean> booleanGrid = GridFactory.ofBooleansWithSize(GRID_SIZE);

        for (final Triple<GridUpdateInstruction, Point, Point> instructionAndPoints : instructionsAndPoints) {
            final GridUpdateInstruction gridUpdateInstruction = instructionAndPoints.first();
            final Point first = instructionAndPoints.second();
            final Point second = instructionAndPoints.third();

            booleanGrid.drawBox(first.x(), first.y(), second.x(), second.y(),
                currentValue -> canUpdateBooleanGrid(gridUpdateInstruction, currentValue));
        }

        return booleanGrid.sumValues(booleanValue -> Boolean.TRUE.equals(booleanValue) ? 1 : 0);
    }

    private static boolean canUpdateBooleanGrid(final GridUpdateInstruction gridUpdateInstruction, final boolean currentValue) {
        return switch (gridUpdateInstruction) {
            case ON -> true;
            case OFF -> false;
            case TOGGLE -> !currentValue;
            default -> throw new IllegalStateException("Cannot update with instruction: " + gridUpdateInstruction);
        };
    }

    /**
     * Creates an {@link Integer} {@link Grid} and sets {@link Point} based on the input {@link GridUpdateInstruction}.
     *
     * @param instructionsAndPoints the {@link Point}s defining a box on the {@link Integer} {@link Grid}, with the {@link GridUpdateInstruction}
     * @return the total value of all {@link Integer} {@link Grid} {@link Point}s
     */
    public static long calculateBrightness(final Collection<Triple<GridUpdateInstruction, Point, Point>> instructionsAndPoints) {
        final Grid<Integer> integerGrid = GridFactory.ofIntegersWithSize(GRID_SIZE);

        for (final Triple<GridUpdateInstruction, Point, Point> instructionAndPoints : instructionsAndPoints) {
            final GridUpdateInstruction gridUpdateInstruction = instructionAndPoints.first();
            final Point first = instructionAndPoints.second();
            final Point second = instructionAndPoints.third();

            integerGrid.drawBox(first.x(), first.y(), second.x(), second.y(),
                currentValue -> updateIntegerGrid(gridUpdateInstruction, currentValue));
        }

        return integerGrid.sumValues(integerValue -> integerValue);
    }

    private static int updateIntegerGrid(final GridUpdateInstruction gridUpdateInstruction, final int currentValue) {
        return switch (gridUpdateInstruction) {
            case ON -> currentValue + 1;
            case OFF -> Math.max(0, currentValue - 1); // Value should not be less than 0
            case TOGGLE -> currentValue + 2;
            default -> throw new IllegalStateException("Cannot update with instruction: " + gridUpdateInstruction);
        };
    }
}
