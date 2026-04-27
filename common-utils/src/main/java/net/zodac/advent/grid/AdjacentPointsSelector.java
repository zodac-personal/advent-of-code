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

package net.zodac.advent.grid;

/**
 * Record class defining the possible options when selection adjacent {@link Point}s.
 *
 * @param withSelf          whether to include the current {@link Point}
 * @param adjacentDirection the {@link AdjacentDirection} in which to search for adjacent {@link Point}s
 * @param allowOutOfBounds  whether to include the adjacent {@link Point}s even if they go out of bounds
 * @param gridSize          the limits of the 2D grid for the {@link Point}s, if {@code #allowOutOfBounds} is {@code false}
 */
public record AdjacentPointsSelector(boolean withSelf, AdjacentDirection adjacentDirection, boolean allowOutOfBounds, int gridSize) {

    private static final int DEFAULT_GRID_SIZE = 0;

    /**
     * Creates an {@link AdjacentPointsSelector} for a 2D grid that is unbounded (meaning all adjacent {@link Point}s should be returned, even if
     * they would be out of bounds).
     *
     * @param withSelf          whether to include the current {@link Point}
     * @param adjacentDirection the {@link AdjacentDirection} in which to search for adjacent {@link Point}s
     * @return the {@link AdjacentPointsSelector}
     */
    public static AdjacentPointsSelector unbounded(final boolean withSelf, final AdjacentDirection adjacentDirection) {
        return new AdjacentPointsSelector(withSelf, adjacentDirection, true, DEFAULT_GRID_SIZE);
    }

    /**
     * Creates an {@link AdjacentPointsSelector} for a 2D grid that is bounded (meaning any adjacent {@link Point}s that would be out of bounds
     * should be excluded).
     *
     * @param withSelf          whether to include the current {@link Point}
     * @param adjacentDirection the {@link AdjacentDirection} in which to search for adjacent {@link Point}s
     * @param gridSize          the limits of the 2D grid for the {@link Point}s
     * @return the {@link AdjacentPointsSelector}
     */
    public static AdjacentPointsSelector bounded(final boolean withSelf, final AdjacentDirection adjacentDirection, final int gridSize) {
        return new AdjacentPointsSelector(withSelf, adjacentDirection, false, gridSize);
    }
}
