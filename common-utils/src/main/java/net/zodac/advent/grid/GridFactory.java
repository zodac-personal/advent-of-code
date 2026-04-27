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

import java.util.List;
import net.zodac.advent.util.NumberUtils;

/**
 * Factory class to create instances of {@link Grid}.
 */
public final class GridFactory {

    private GridFactory() {

    }

    /**
     * Converts the {@link List} of {@link String}s to a {@link Boolean} {@link Grid}.
     *
     * @param strings              the input {@link List} of {@link String}s
     * @param symbolSignifyingTrue the symbol in the {@link String} that defines a {@code true} {@link Boolean}
     * @return the {@link Boolean} {@link Grid}
     */
    public static Grid<Boolean> ofBooleans(final List<String> strings, final char symbolSignifyingTrue) {
        return Grid.parseGrid(strings, character -> character == symbolSignifyingTrue);
    }

    /**
     * Creates a {@link Boolean} {@link Grid} with the dimensions {@code gridSize}x{@code gridSize}.
     *
     * @param gridSize the length and width of the {@link Boolean} {@link Grid}
     * @return the created {@link Boolean} {@link Grid}, with default value <b>false</b>
     * @throws IllegalArgumentException thrown if input size is less than <b>0</b>
     */
    public static Grid<Boolean> ofBooleansWithSize(final int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException("Size must be positive integer, found: " + gridSize);
        }

        return new Grid<>(gridSize, new Boolean[gridSize][gridSize], false);
    }

    /**
     * Converts the {@link List} of {@link String}s to a {@link Grid} of {@link Character}s.
     *
     * @param strings the input {@link List} of {@link String}s
     * @return the {@link Character} {@link Grid}
     */
    public static Grid<Character> ofCharacters(final List<String> strings) {
        return Grid.parseGrid(strings, character -> character);
    }

    /**
     * Creates a {@link Character} {@link Grid} with the dimensions {@code gridSize}x{@code gridSize}.
     *
     * @param gridSize the length and width of the {@link Character} {@link Grid}
     * @return the created {@link Character} {@link Grid}, with default value <b>'.'</b>
     * @throws IllegalArgumentException thrown if input size is less than <b>0</b>
     */
    public static Grid<Character> ofCharactersWithSize(final int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException("Size must be positive integer, found: " + gridSize);
        }

        return new Grid<>(gridSize, new Character[gridSize][gridSize], '.');
    }

    /**
     * Converts the {@link List} of {@link String}s to a {@link Integer} {@link Grid}. Note that this expects no blank spaces, and will assume every
     * character is a single digit {@link Integer}.
     *
     * @param strings the input {@link List} of {@link String}s
     * @return the {@link Integer} {@link Grid}, with default value <b>0</b>
     */
    public static Grid<Integer> ofIntegers(final List<String> strings) {
        return Grid.parseGrid(strings, character -> NumberUtils.toIntOrDefault(character, 0));
    }

    /**
     * Creates a {@link Integer} {@link Grid} with the dimensions {@code gridSize}x{@code gridSize}.
     *
     * @param gridSize the length and width of the {@link Integer} {@link Grid}
     * @return the created {@link Integer} {@link Grid}, with default value <b>0</b>
     * @throws IllegalArgumentException thrown if input size is less than <b>0</b>
     */
    public static Grid<Integer> ofIntegersWithSize(final int gridSize) {
        if (gridSize <= 0) {
            throw new IllegalArgumentException("Size must be positive integer, found: " + gridSize);
        }

        return new Grid<>(gridSize, new Integer[gridSize][gridSize], 0);
    }
}
