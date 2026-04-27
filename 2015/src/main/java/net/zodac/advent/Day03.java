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
import net.zodac.advent.grid.Point;
import net.zodac.advent.util.MathUtils;

/**
 * Solution for 2015, Day 3.
 *
 * @see <a href="https://adventofcode.com/2015/day/3">AoC 2015, Day 3</a>
 */
public final class Day03 {

    private Day03() {

    }

    /**
     * For the given {@link Direction}s, a {@link Point} moves to a new house. Counts the number of unique houses.
     *
     * @param values the {@link Direction}s
     * @return the number of unique houses
     */
    public static int countUniqueHouses(final Collection<Direction> values) {
        final Collection<Point> visitedHouses = new HashSet<>();
        Point currentPoint = Point.atOrigin();

        visitedHouses.add(currentPoint);

        for (final Direction direction : values) {
            currentPoint = currentPoint.move(direction);
            visitedHouses.add(currentPoint);
        }

        return visitedHouses.size();
    }

    /**
     * For the given {@link Direction}s, a {@link Point} moves to a new house for each even index, and another {@link Point} moves to a new house for
     * each odd index. Counts the number of unique houses.
     *
     * @param values the {@link Direction}s
     * @return the number of unique houses
     */
    public static int countUniqueHousesTwoUsers(final List<Direction> values) {
        final Collection<Point> visitedHouses = new HashSet<>();
        Point currentPointUserOne = Point.atOrigin();
        Point currentPointUserTwo = Point.atOrigin();

        visitedHouses.add(currentPointUserOne);
        visitedHouses.add(currentPointUserTwo);

        final int numberOfEntries = values.size();
        for (int i = 0; i < numberOfEntries; i++) {
            final Direction direction = values.get(i);

            if (MathUtils.isEven(i)) {
                currentPointUserOne = currentPointUserOne.move(direction);
                visitedHouses.add(currentPointUserOne);
            } else {
                currentPointUserTwo = currentPointUserTwo.move(direction);
                visitedHouses.add(currentPointUserTwo);
            }
        }

        return visitedHouses.size();
    }
}
