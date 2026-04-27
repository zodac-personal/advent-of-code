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

package net.zodac.advent.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.GridFactory;
import net.zodac.advent.grid.Point;
import net.zodac.advent.search.GroupSearcher;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link GroupSearcher}.
 */
class GroupSearcherTest {

    @Test
    void testSearcher() {
        final Grid<Character> grid = GridFactory.ofCharacters(List.of(
            "AABA",
            "ACBA",
            "DCBA",
            "AADD"
        ));

        final Map<Character, Set<Set<Point>>> connectedPoints = GroupSearcher.findGroups(grid);

        assertThat(connectedPoints)
            .containsKeys(
                'A',
                'B',
                'C',
                'D'
            );
        assertThat(connectedPoints.getOrDefault('A', Set.of()))
            .hasSameElementsAs(
                Set.of(
                    Set.of(Point.of(0, 0), Point.of(0, 1), Point.of(1, 0)),
                    Set.of(Point.of(0, 3), Point.of(1, 3), Point.of(2, 3)),
                    Set.of(Point.of(3, 0), Point.of(3, 1))
                )
            );
        assertThat(connectedPoints.getOrDefault('B', Set.of()))
            .hasSameElementsAs(
                Set.of(
                    Set.of(Point.of(0, 2), Point.of(1, 2), Point.of(2, 2))
                )
            );
        assertThat(connectedPoints.getOrDefault('C', Set.of()))
            .hasSameElementsAs(
                Set.of(
                    Set.of(Point.of(1, 1), Point.of(2, 1))
                )
            );
        assertThat(connectedPoints.getOrDefault('D', Set.of()))
            .hasSameElementsAs(
                Set.of(
                    Set.of(Point.of(2, 0)),
                    Set.of(Point.of(3, 2), Point.of(3, 3))
                )
            );
    }
}
