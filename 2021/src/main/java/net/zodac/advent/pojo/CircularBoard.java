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

package net.zodac.advent.pojo;

/**
 * A game board that loops back on itself.
 *
 * @param maxValue the maximum value for the board
 */
public record CircularBoard(int maxValue) {

    /**
     * Creates a {@link CircularBoard} with a maximum value.
     *
     * @param maxValue the maximum value for the board
     * @return the created {@link CircularBoard}
     */
    public static CircularBoard createWithMaxValue(final int maxValue) {
        return new CircularBoard(maxValue);
    }

    /**
     * Moves {@code spacesToMove} along the {@link CircularBoard} from the {@code startPosition}, then returns the final position.
     *
     * <p>
     * As the board is circular, once the maximum position is passed, we loop back to the start
     *
     * @param startPosition the initial position
     * @param spacesToMove  the number of spaces to move
     * @return the final position on the {@link CircularBoard}
     */
    public int moveOnBoardAndGetNewPosition(final int startPosition, final long spacesToMove) {
        return (int) ((startPosition + spacesToMove - 1) % maxValue) + 1;
    }
}
