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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * Solution for 2024, Day 8.
 *
 * @see <a href="https://adventofcode.com/2024/day/8">[2024: 08] Resonant Collinearity</a>
 */
public final class Day08 {

    private static final Set<Character> CHARACTERS_TO_IGNORE = Set.of('.');

    private Day08() {

    }

    /**
     * Given a {@link Character} {@link Grid}, {@link Character}s of the same value indicate antennas transmitting on the same frequency. Each antenna
     * pair will create an antinode - an area of resonant frequency double the distance from antenna1 to antenna2 (for each antenna). Antennas and
     * antinodes may overlap, and there is an option for a single antinode per pair, or antinodes in a line along the same axis as the
     * antenna pair. We restrict the antinodes to only be contained within the bounds of the {@link Grid}.
     *
     * <p>
     * For each frequency, we count the number of unique antinodes for the provided {@link Grid}.
     *
     * @param characterGrid     the {@link Grid} of antennas
     * @param hasSingleAntinode if each pair of antennas creates a single antinode, or all possible antinodes in a line.
     * @return the unique antinodes for all antennas
     */
    public static long countUniqueAntinodes(final Grid<Character> characterGrid, final boolean hasSingleAntinode) {
        final Map<Character, Set<Point>> pointsByCharacter = getPointsByCharacter(characterGrid);

        final Collection<Point> antinodes = new HashSet<>();
        for (final Set<Point> points : pointsByCharacter.values()) {
            for (final Point currentPoint : points) {
                antinodes.addAll(getAntinodesForPoint(characterGrid, points, currentPoint, hasSingleAntinode));
            }
        }

        return antinodes.size();
    }

    private static Set<Point> getAntinodesForPoint(final Grid<Character> characterGrid,
                                                   final Collection<Point> gridPoints,
                                                   final Point currentPoint,
                                                   final boolean hasSingleAntinode
    ) {
        final Set<Point> antinodes = new HashSet<>();

        for (final Point nextPoint : gridPoints) {
            if (currentPoint.equals(nextPoint)) {
                continue;
            }

            // Include the current point, since it will be in the same line if there are multiple antinodes
            if (!hasSingleAntinode) {
                antinodes.add(nextPoint);
            }

            final Pair<Integer, Integer> delta = currentPoint.deltaTo(nextPoint);
            Point possibleAntinode = Point.of(nextPoint.x() + delta.first(), nextPoint.y() + delta.second());

            // Always take the first antinode, then loop over if we need more
            do {
                if (characterGrid.exists(possibleAntinode)) {
                    antinodes.add(possibleAntinode);
                    possibleAntinode = Point.of(possibleAntinode.x() + delta.first(), possibleAntinode.y() + delta.second());
                }
            } while (!hasSingleAntinode && characterGrid.exists(possibleAntinode));
        }

        return antinodes;
    }

    private static Map<Character, Set<Point>> getPointsByCharacter(final Grid<Character> characterGrid) {
        final Set<Character> allValues = characterGrid
            .allPoints()
            .stream()
            .map(characterGrid::at)
            .collect(Collectors.toSet());
        allValues.removeAll(CHARACTERS_TO_IGNORE); // Remove values that do not signify antennas

        final Map<Character, Set<Point>> pointsByCharacter = new HashMap<>();
        for (final Character character : allValues) {
            pointsByCharacter.put(character, characterGrid.findValue(character1 -> character1.equals(character)).collect(Collectors.toSet()));
        }
        return pointsByCharacter;
    }
}
