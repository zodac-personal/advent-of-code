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
import java.util.List;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.util.CollectionUtils;

/**
 * Solution for 2023, Day 11.
 *
 * @see <a href="https://adventofcode.com/2023/day/11">[2023: 11] Cosmic Expansion</a>
 */
public final class Day11 {

    private static final char GALAXY_SYMBOL = '#';

    private Day11() {

    }

    /**
     * Given a {@link Character} {@link Grid}s representing space, each {@link #GALAXY_SYMBOL} represents a galaxy. In order to account for the
     * expansion of space-time, any row or column that does not contain a galaxy needs to be replaced with {@code expansionSize} number of empty rows
     * or columns.
     *
     * <p>
     * Once the expansion is complete, we count the distance from each galaxy to every other galaxy (in either direction, so only count the distance
     * from galaxy A -> galaxy B one time), then sum up all these distances.
     *
     * @param grid          the {@link Grid} for space
     * @param expansionSize the number of additional rows/columns to add to any empty rows/columns
     * @return the total distance between all galaxies
     */
    public static long sumOfDistancesBetweenGalaxies(final Grid<Character> grid, final int expansionSize) {
        final List<Integer> emptyRows = findRowsWithoutGalaxies(grid);
        final List<Integer> emptyColumns = findColumnsWithoutGalaxies(grid);
        final int actualExpansionSize = expansionSize - 1; // We are replacing rows/columns, not adding, so we don't count the existing ones

        final List<Point> galaxies = findGalaxies(grid, emptyColumns, emptyRows, actualExpansionSize);
        final int numberOfGalaxies = galaxies.size();
        long total = 0L;

        for (int i = 0; i < numberOfGalaxies; i++) {
            final Point currentGalaxy = galaxies.get(i);

            total += galaxies.subList(i, numberOfGalaxies)
                .stream()
                .mapToLong(currentGalaxy::distanceTo)
                .sum();
        }

        return total;
    }

    private static List<Point> findGalaxies(final Grid<Character> characterGrid,
                                            final Collection<Integer> emptyColumns,
                                            final Collection<Integer> emptyRows,
                                            final int expansionSize) {
        final List<Point> galaxies = new ArrayList<>();
        final int numberOfRows = characterGrid.numberOfRows();

        for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
            final Character[] row = characterGrid.rowAt(rowIndex);

            for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                final char val = row[columnIndex];
                if (val == GALAXY_SYMBOL) {
                    final Point galaxy = createGalaxyPoint(emptyRows, emptyColumns, rowIndex, columnIndex, expansionSize);
                    galaxies.add(galaxy);
                }
            }
        }
        return galaxies;
    }

    private static Point createGalaxyPoint(final Collection<Integer> emptyRows,
                                           final Collection<Integer> emptyColumns,
                                           final int rowIndex,
                                           final int columnIndex,
                                           final int expansionSize) {
        final int emptyColumnsToAdd = CollectionUtils.findValuesLessThan(emptyColumns, columnIndex, Integer::compareTo).size();
        final int emptyRowsToAdd = CollectionUtils.findValuesLessThan(emptyRows, rowIndex, Integer::compareTo).size();

        final int extraColumns = emptyColumnsToAdd * expansionSize;
        final int extraRows = emptyRowsToAdd * expansionSize;
        return Point.of(rowIndex + extraRows, columnIndex + extraColumns);
    }

    private static List<Integer> findColumnsWithoutGalaxies(final Grid<Character> characterGrid) {
        return characterGrid
            .findColumnsWith(character -> character != GALAXY_SYMBOL)
            .toList();
    }

    private static List<Integer> findRowsWithoutGalaxies(final Grid<Character> characterGrid) {
        return characterGrid
            .findRowsWith(character -> character != GALAXY_SYMBOL)
            .toList();
    }
}
