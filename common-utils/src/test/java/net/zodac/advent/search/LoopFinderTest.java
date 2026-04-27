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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import net.zodac.advent.grid.Direction;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.GridFactory;
import net.zodac.advent.grid.Point;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link LoopFinder}.
 */
class LoopFinderTest {

    @ParameterizedTest
    @MethodSource("provideForHasLoop_integerGrid")
    void testHasLoop_integerGrid(final Grid<Integer> inputGrid, final boolean expected) {
        final boolean result = LoopFinder.doesLoopExist(inputGrid, Point.of(0, 0), Direction.RIGHT,
            (integerGrid, point, direction) -> {
                Direction nextDirection = direction;
                // Move in the current direction. If it doesn't exist within the Grid, return Direction.INVALID to end the traversal
                Point nextPoint = point.move(direction);
                if (!integerGrid.exists(nextPoint)) {
                    return Direction.INVALID;
                }
                // If we find a value greater than 3, turn right
                while (integerGrid.at(nextPoint) > 3) {
                    nextDirection = nextDirection.rotateRight();
                    nextPoint = point.move(nextDirection);

                    if (nextDirection == direction || !integerGrid.exists(nextPoint)) {
                        return Direction.INVALID;
                    }
                }
                return nextDirection;
            },
            (_, point, direction) -> point.move(direction)
        );

        assertThat(result)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForHasLoop_integerGrid() {
        return Stream.of(
            // Integer grid with no loop
            Arguments.of(GridFactory.ofIntegers(List.of(
                    "100008",
                    "060200",
                    "000000",
                    "000000",
                    "000000",
                    "000000")
                ), false
            ),
            // Integer grid with loop
            Arguments.of(GridFactory.ofIntegers(List.of(
                    "100205",
                    "009200",
                    "000005",
                    "050000",
                    "000050",
                    "000000")
                ), true
            ),
            // Integer grid with no loop, using all cells
            Arguments.of(GridFactory.ofIntegers(List.of(
                        "100"
                    )
                ), false
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideForHasLoop_stringGrid")
    void testHasLoop_stringGrid(final Grid<String> inputGrid, final boolean expected) {
        final boolean result = LoopFinder.doesLoopExist(inputGrid, Point.of(1, 1), Direction.RIGHT,
            (integerGrid, point, direction) -> {
                Direction nextDirection = direction;
                // Move in the current direction. If it doesn't exist within the Grid, return Direction.INVALID to end the traversal
                Point nextPoint = point.move(direction);
                if (!integerGrid.exists(nextPoint)) {
                    return Direction.INVALID;
                }
                // If we find a value greater than 3, turn right
                while ("NO".equals(integerGrid.at(nextPoint))) {
                    nextDirection = nextDirection.rotateRight();
                    nextPoint = point.move(nextDirection);

                    if (nextDirection == direction) {
                        return Direction.INVALID;
                    }
                }
                return nextDirection;
            },
            (_, point, direction) -> point.move(direction)
        );

        assertThat(result)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForHasLoop_stringGrid() {
        return Stream.of(
            // Integer grid with no loop
            Arguments.of(new Grid<>(new String[][]{
                    {"0", "0", "0", "0", "0", "0"},
                    {"0", "START", "0", "No?", "0", "0"},
                    {"0", "0", "0", "0", "0", "0"},
                    {"0", "0", "0", "0", "0", "0"},
                    {"0", "0", "0", "0", "0", "0"},
                    {"0", "0", "0", "0", "0", "0"},
                }), false
            ),
            // Integer grid with loop
            Arguments.of(new Grid<>(new String[][]{
                    {"I", "NO", "Like", "TO", "Use", "Random"},
                    {"StRiNgs", "START", "To", "No?", "Make", "NO"},
                    {"My", "Point()", ".", "And", "i", "think"},
                    {"NO", "it's", "more", "ƃuᴉʇsǝɹǝʇuᴉ", "thaN", "uSiNg"},
                    {"PLACE_HOLDER", "values", "LIKE", "~~Lorem", "NO", "Ipsum~~"},
                    {"LIKE", "everyONE", "ElsE,", "don'T", "U", "think?"},
                }), true
            )
        );
    }
}
