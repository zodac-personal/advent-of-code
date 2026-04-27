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
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.PerimeterCounter;
import net.zodac.advent.search.GroupSearcher;

/**
 * Solution for 2024, Day 12.
 *
 * @see <a href="https://adventofcode.com/2024/day/12">[2024: 12] Garden Groups</a>
 */
public final class Day12 {

    private Day12() {

    }

    /**
     * Given a {@link Character} {@link Grid}, find all groups of connected same values, and calculate their value. The value is:
     * <pre>
     *     perimeter * area
     * </pre>
     *
     * <p>
     * Or:
     * <pre>
     *     unique sides in perimeter * area
     * </pre>
     *
     * <p>
     * Where unique sides means any connected {@link Point}s in the perimeter are considered a side if they are along the same edge.
     *
     * @param characterGrid  the input {@link Character} {@link Grid}
     * @param countSidesOnly whether the full perimeter should be considered, or just the sides
     * @return the total value of all connected groups
     * @see GroupSearcher#findGroups(Grid)
     * @see PerimeterCounter
     */
    public static long totalValueOfGroups(final Grid<Character> characterGrid, final boolean countSidesOnly) {
        return GroupSearcher.findGroups(characterGrid)
            .values()
            .stream()
            .flatMap(Collection::stream)
            .mapToLong(group -> calculateValueOfGroup(group, characterGrid, countSidesOnly))
            .sum();
    }

    private static long calculateValueOfGroup(final Collection<Point> group, final Grid<Character> characterGrid, final boolean countSidesOnly) {
        final long perimiterCount = countSidesOnly ? PerimeterCounter.countSides(group) : PerimeterCounter.countFullPerimeter(group, characterGrid);
        final int area = group.size();
        return area * perimiterCount;
    }
}
