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
import net.zodac.advent.grid.Direction;
import net.zodac.advent.pojo.Movement;

/**
 * Solution for 2021, Day 2.
 *
 * @see <a href="https://adventofcode.com/2021/day/2">AoC 2021, Day 2</a>
 */
public final class Day02 {

    private Day02() {

    }

    /**
     * Iterates over and calculates the magnitude of the supplied {@link Movement}s.
     *
     * <p>Vertical {@link Movement}s are defined by {@link Direction#DOWN} (positive) and {@link Direction#UP} (negative), while horizontal
     * {@link Movement}s are defined by {@link Direction#RIGHT}.
     *
     * <p>
     * The magnitude is the resultant vertical multiplied by the resultant horizontal.
     *
     * @param movements the {@link Movement}s to iterate over
     * @return the magnitude of all vertical and horizontal {@link Movement}s
     */
    public static long magnitudeOfAllMovements(final Collection<Movement> movements) {
        long horizontal = 0L;
        long vertical = 0L;

        for (final Movement movement : movements) {
            final Direction direction = movement.direction();
            final int spaces = movement.spaces();

            switch (direction) {
                case DOWN -> vertical += spaces;
                case RIGHT -> horizontal += spaces;
                case UP -> vertical -= spaces;
                default -> throw new IllegalStateException(String.format("Cannot handle direction: '%s'", direction));
            }
        }

        return horizontal * vertical;
    }

    /**
     * Iterates over and calculates the magnitude of the supplied {@link Movement}s.
     *
     * <p>
     * A {@link Movement} with {@link Direction} of {@link Direction#DOWN} or {@link Direction#UP} does not change the vertical value, but instead
     * sets and {@code aim} value. When a {@link Direction#RIGHT} {@link Movement} is found, it will calculate the change in the vertical position
     * as this {@code aim} and the number of spaces. It will also increment the horizontal position at the same time.
     *
     * <p>
     * The magnitude is the resultant vertical multiplied by the resultant horizontal.
     *
     * @param movements the {@link Movement}s to iterate over
     * @return the magnitude of all vertical and horizontal {@link Movement}s
     */
    public static long magnitudeOfAllMovementsWithAim(final Collection<Movement> movements) {
        long horizontal = 0L;
        long vertical = 0L;
        long aim = 0L;

        for (final Movement movement : movements) {
            final Direction direction = movement.direction();
            final int spaces = movement.spaces();

            switch (direction) {
                case DOWN -> aim += spaces;
                case RIGHT -> {
                    horizontal += spaces;
                    vertical += (aim * spaces);
                }
                case UP -> aim -= spaces;
                default -> throw new IllegalStateException(String.format("Cannot handle direction: '%s'", direction));
            }
        }

        return horizontal * vertical;
    }
}
