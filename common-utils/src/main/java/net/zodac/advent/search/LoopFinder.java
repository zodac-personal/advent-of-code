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

package net.zodac.advent.search;

import java.util.Collection;
import java.util.HashSet;
import net.zodac.advent.function.TriFunction;
import net.zodac.advent.grid.Direction;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * Utility class used to search through a {@link Grid} to find if a loop exists.
 */
public final class LoopFinder {

    private LoopFinder() {

    }

    /**
     * Traverses the input {@link Grid} based on the update rules supplied, and checks if the path crosses over on itself. A 'loop' is determined if
     * the path revisits a {@link Point} it has already visited <b>AND</b> the current {@link Direction} of travel is the same as previous.
     *
     * <p>
     * An example of how to apply this can be for a {@link Character} {@link Grid} with obstacles denoted by '#'. We begin walking in
     * {@code startDirection} from {@code startPoint}, and want to turn 90° to the right when we hit an obstacle. This requires the
     * {@code directionUpdate} {@link TriFunction} to update itself appropriately, like below, with comments for clarity:
     *
     * <p>
     * {@snippet :
     *     final TriFunction<Grid<Character>, Point, Direction, Direction> directionUpdate = (characterGrid, point, direction) -> {
     *             Direction nextDirection = direction;
     *             // Move in the current direction. If it doesn't exist within the Grid, return Direction.INVALID to end the traversal
     *             Point nextPoint = point.move(direction);
     *             if (!characterGrid.exists(nextPoint)) {
     *                 return Direction.INVALID;
     *             }
     *             // If we find an obstacle, turn right
     *             while (characterGrid.at(nextPoint) == '#') {
     *                 nextDirection = nextDirection.rotateRight();
     *                 nextPoint = point.move(nextDirection);
     *                 if (nextDirection == direction) {
     *                     return Direction.INVALID;
     *                 }
     *             }
     *             return nextDirection;
     *         };
     *}
     *
     * <p>
     * Based on the updated {@link Direction}, we move from our current {@link Point} to the next:
     *
     * <p>
     * {@snippet :
     *      final TriFunction<Grid<Character>, Point, Direction, Direction> pointUpdate = (_, point, direction) -> point.move(direction);
     *}
     *
     * @param grid            the {@link Grid} to traverse
     * @param startPoint      the initial {@link Point} from which to start traversing the {@link Grid}
     * @param startDirection  the initial {@link Direction} in which to move from the start {@link Point}
     * @param directionUpdate the {@link TriFunction} rule to define how the {@link Direction} of travel updates
     * @param pointUpdate     the {@link TriFunction} rule to define how to move from the current {@link Point} to the next
     * @param <E>             the type of the {@link Grid} to search
     * @return {@code true} if a loop exists within the {@link Grid} based on the update rules
     */
    public static <E> boolean doesLoopExist(final Grid<E> grid, final Point startPoint, final Direction startDirection,
                                            final TriFunction<? super Grid<E>, ? super Point, ? super Direction, Direction> directionUpdate,
                                            final TriFunction<? super Grid<E>, ? super Point, ? super Direction, Point> pointUpdate) {
        Point currentPoint = startPoint;
        Direction currentDirection = startDirection;
        final Collection<Pair<Point, Direction>> visitedPoints = new HashSet<>();

        // Keep going until every element has been visited
        while (true) {
            if (!visitedPoints.add(Pair.of(currentPoint, currentDirection))) {
                return true;
            }

            // Update rules
            currentDirection = directionUpdate.apply(grid, currentPoint, currentDirection);
            if (currentDirection == Direction.INVALID) {
                return false;
            }

            currentPoint = pointUpdate.apply(grid, currentPoint, currentDirection);
        }
    }
}
