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
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link PermutationUtils}.
 */
class PermutationUtilsTest {

    @ParameterizedTest
    @MethodSource("provideForGenerateAll")
    <E> void testGenerateAll(final List<? extends E> input, final List<? extends List<E>> expected) {
        final List<List<E>> output = PermutationUtils.generateAll(input);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForGenerateAll() {
        return Stream.of(
            Arguments.of(List.of("a"), List.of(List.of("a"))),   // Single String entry
            Arguments.of(List.of(1), List.of(List.of(1))),       // Single Integer entry
            Arguments.of(List.of(), List.of(List.of())),                // Empty
            Arguments.of(List.of("a", "b", "c"),                        // Multiple String entries
                List.of(
                    List.of("a", "b", "c"),
                    List.of("a", "c", "b"),
                    List.of("b", "a", "c"),
                    List.of("b", "c", "a"),
                    List.of("c", "a", "b"),
                    List.of("c", "b", "a")
                )
            ),
            Arguments.of(List.of(1, 2, 3),                              // Multiple Integer entries
                List.of(
                    List.of(1, 2, 3),
                    List.of(1, 3, 2),
                    List.of(2, 1, 3),
                    List.of(2, 3, 1),
                    List.of(3, 1, 2),
                    List.of(3, 2, 1)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideForGenerateAllWithOneRemovedEntry")
    <E> void testGenerateAllWithOneRemovedEntry(final List<? extends E> input, final List<? extends List<E>> expected) {
        final List<List<E>> output = PermutationUtils.generateAllWithOneRemovedEntry(input);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForGenerateAllWithOneRemovedEntry() {
        return Stream.of(
            Arguments.of(List.of("a"), List.of(List.of())),  // Single entry
            Arguments.of(List.of(), List.of()),                 // Empty
            // Multiple String entries
            Arguments.of(List.of("a", "b", "c"),
                List.of(
                    List.of("a", "b"),
                    List.of("b", "c"),
                    List.of("a", "c")
                )
            ),
            // Multiple Integer entries
            Arguments.of(List.of(1, 2, 3),
                List.of(
                    List.of(1, 2),
                    List.of(2, 3),
                    List.of(1, 3)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideForGenerateWithSeparators")
    <E> void testGenerateWithSeparators(final List<String> input, final Set<E> separators, final List<? extends List<String>> expected) {
        final List<List<String>> output = PermutationUtils.generateWithSeparators(input, separators);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForGenerateWithSeparators() {
        return Stream.of(
            // Single separator
            Arguments.of(List.of("A", "B", "C"), Set.of("a"), List.of(
                    List.of("A", "a", "B", "a", "C")
                )
            ),
            // Multiple separators
            Arguments.of(List.of("A", "B", "C"), Set.of("a", "b"), List.of(
                    List.of("A", "a", "B", "a", "C"),
                    List.of("A", "a", "B", "b", "C"),
                    List.of("A", "b", "B", "a", "C"),
                    List.of("A", "b", "B", "b", "C")
                )
            ),
            // Multiple separators, non-string input
            Arguments.of(List.of(1, 2, 3), Set.of("a", "b"), List.of(
                    List.of("1", "a", "2", "a", "3"),
                    List.of("1", "a", "2", "b", "3"),
                    List.of("1", "b", "2", "a", "3"),
                    List.of("1", "b", "2", "b", "3")
                )
            ),
            // Multiple separators, non-string separator
            Arguments.of(List.of("A", "B", "C"), Set.of(1, 2), List.of(
                    List.of("A", "1", "B", "1", "C"),
                    List.of("A", "1", "B", "2", "C"),
                    List.of("A", "2", "B", "1", "C"),
                    List.of("A", "2", "B", "2", "C")
                )
            ),
            // Single input
            Arguments.of(List.of("A"), Set.of("a", "b"), List.of(
                    List.of("A")
                )
            ),
            // Empty input
            Arguments.of(List.of(), Set.of("a", "b"), List.of(
                    List.of()
                )
            ),
            // Empty separators
            Arguments.of(List.of("A", "B", "C"), Set.of(), List.of(
                    List.of("A", "B", "C")
                )
            )
        );
    }
}
