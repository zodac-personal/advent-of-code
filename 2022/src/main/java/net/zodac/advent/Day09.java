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
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.Movement;

/**
 * Solution for 2022, Day 9.
 *
 * @see <a href="https://adventofcode.com/2022/day/9">AoC 2022, Day 9</a>
 */
public final class Day09 {

    private static final int DISTANCE_FOR_FOLLOWER_TO_MOVE_TOWARDS_LEADER = 2;

    private Day09() {

    }

    /**
     * Given {@link Movement}s defining a route for a 'head' {@link Point} to follow around a 2D grid, {@code numberOfTails} tails will follow. Each
     * 'tail' {@link Point} will only follow its direct predecessor (heads--tail0--tail1--tail2...--tail-n).
     *
     * <p>
     * After each movement the leading {@link Point} makes, its follower will move according to one of these rules, in priority order.
     *
     * <p>
     * If the {@link Point}:
     * <ol>
     *     <li>And its follower are on the same {@link Point}, the follower does not move</li>
     *     <li>And its follower are direct neighbours (including diagonals), the follower does not move</li>
     *     <li>Is on the same row, the follower moves one column towards it</li>
     *     <li>Is on the same column, the follower moves one row towards it</li>
     *     <li>Is not on the same row or column, the follower moves one row and one column diagonally towards it</li>
     * </ol>
     *
     * <p>
     * The input {@link Movement}s define the route the head {@link Point} should follow.
     *
     * @param movements     the {@link Movement}s for the head to follow
     * @param numberOfTails the number of tails following the head
     * @return the number of unique {@link Point}s visited by the last tail
     */
    public static long uniquePointsVisitedByTail(final Collection<Movement> movements, final int numberOfTails) {
        Point head = Point.atOrigin();
        final Point[] tails = createTails(numberOfTails);

        final Collection<Point> visitedByTail = new HashSet<>();
        visitedByTail.add(tails[tails.length - 1]);

        for (final Movement movement : movements) {
            final int numberOfSpaces = movement.spaces();
            for (int i = 0; i < numberOfSpaces; i++) {
                // Move the head
                head = head.move(movement.direction());

                // Move all tails in sequence
                for (int tailNumber = 0; tailNumber < tails.length; tailNumber++) {
                    final Point leader = tailNumber == 0 ? head : tails[tailNumber - 1];
                    tails[tailNumber] = moveFollower(leader, tails[tailNumber]);
                }

                // Only add the last tail to the visited list
                visitedByTail.add(tails[tails.length - 1]);
            }
        }

        return visitedByTail.size();
    }

    private static Point[] createTails(final int numberOfTails) {
        final Point[] tails = new Point[numberOfTails];

        for (int i = 0; i < numberOfTails; i++) {
            tails[i] = Point.atOrigin();
        }

        return tails;
    }

    private static Point moveFollower(final Point leader, final Point follower) {
        final int deltaX = leader.x() - follower.x();
        final int deltaY = leader.y() - follower.y();

        // Check for the distance between leader and follower in both row and column.
        // Since the leader only moves one space at a time and cannot move diagonally,
        // only one of the X or Y coordinate will be multiple spaces away.
        // The follower should only move 1 space, so we use Integer#signum(int).
        if (Math.abs(deltaX) == DISTANCE_FOR_FOLLOWER_TO_MOVE_TOWARDS_LEADER || Math.abs(deltaY) == DISTANCE_FOR_FOLLOWER_TO_MOVE_TOWARDS_LEADER) {
            return follower.move(Integer.signum(deltaX), Integer.signum(deltaY));
        }

        // If neither X nor Y coordinate is far away enough, do not move
        return follower;
    }
}
