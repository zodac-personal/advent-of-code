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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.atIndex;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import net.zodac.advent.pojo.RotationDirection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link ArrayUtils}.
 */
class ArrayUtilsTest {

    @ParameterizedTest
    @CsvSource({
        "3,8",
        "10,36",
        "99,392",
    })
    void testCountPerimeterElements(final int arraySize, final int expectedValue) {
        final Integer[][] input = new Integer[arraySize][arraySize];
        for (final Integer[] row : input) {
            Arrays.fill(row, 1);
        }

        final int output = ArrayUtils.countPerimeterElements(input);
        assertThat(output)
            .isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource("provideForCountPerimeterElements_invalid")
    void testCountPerimeterElements_givenInvalidInputs(final Integer[][] input, final String errorMessage) {
        assertThatThrownBy(() -> ArrayUtils.countPerimeterElements(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    private static Stream<Arguments> provideForCountPerimeterElements_invalid() {
        return Stream.of(
            Arguments.of(new Integer[][] {{1, 2, 3, 4}, {5, 6}, {7, 8, 9}}, "Outer size must match inner size, found outer: 3, inner: 4"),
            Arguments.of(new Integer[][] {}, "Input cannot be empty"),
            Arguments.of(new Integer[][] {{}}, "Outer size must match inner size, found outer: 1, inner: 0")
        );
    }

    @Test
    void whenDeepCopy_givenValidInput_thenNewFilledArrayIsReturned_andOriginalArrayIsUnchanged() {
        final Integer[][] input = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        final Integer[][] output = ArrayUtils.deepCopy(input);
        assertThat(output)
            .contains(new Integer[] {1, 2, 3}, atIndex(0))
            .contains(new Integer[] {4, 5, 6}, atIndex(1))
            .contains(new Integer[] {7, 8, 9}, atIndex(2));

        // Confirm that the original array is not modified if copy is modified
        output[1][1] = 100;
        assertThat(output)
            .contains(new Integer[] {1, 2, 3}, atIndex(0))
            .contains(new Integer[] {4, 100, 6}, atIndex(1))
            .contains(new Integer[] {7, 8, 9}, atIndex(2));
        assertThat(input)
            .contains(new Integer[] {1, 2, 3}, atIndex(0))
            .contains(new Integer[] {4, 5, 6}, atIndex(1))
            .contains(new Integer[] {7, 8, 9}, atIndex(2));
    }

    @ParameterizedTest
    @MethodSource("provideForDeepCopy")
    <E> void testForDeepCopy(final E[][] input, final E[][] expected) {
        final E[][] output = ArrayUtils.deepCopy(input);
        assertThat(output)
            .isDeepEqualTo(expected);
    }

    private static Stream<Arguments> provideForDeepCopy() {
        return Stream.of(
            // Column and row are same lengths
            Arguments.of(new Boolean[][] {{false, false}, {false, false}}, new Boolean[][] {{false, false}, {false, false}}),
            // Column and row are different lengths
            Arguments.of(new Boolean[][] {{true, true}, {true, true}, {true, true}}, new Boolean[][] {{true, true}, {true, true}, {true, true}})
        );
    }

    @Test
    void whenDeepFill_givenValidInput_thenNewFilledArrayIsReturned_andOriginalArrayIsUnchanged() {
        final Boolean[][] input = new Boolean[3][3];
        final Boolean[][] output = ArrayUtils.deepFill(input, true);
        assertThat(output)
            .contains(new Boolean[] {true, true, true}, atIndex(0))
            .contains(new Boolean[] {true, true, true}, atIndex(1))
            .contains(new Boolean[] {true, true, true}, atIndex(2));

        final Boolean[][] secondOutput = ArrayUtils.deepFill(input, false);
        assertThat(secondOutput)
            .contains(new Boolean[] {false, false, false}, atIndex(0))
            .contains(new Boolean[] {false, false, false}, atIndex(1))
            .contains(new Boolean[] {false, false, false}, atIndex(2));

        // Confirm new 2D array is not modified if original array is modified
        output[0][0] = true;
        assertThat(secondOutput)
            .contains(new Boolean[] {false, false, false}, atIndex(0))
            .contains(new Boolean[] {false, false, false}, atIndex(1))
            .contains(new Boolean[] {false, false, false}, atIndex(2));
    }

    @ParameterizedTest
    @MethodSource("provideForDeepFill")
    <E> void testForDeepFill(final E[][] input, final E value, final E[][] expected) {
        final E[][] output = ArrayUtils.deepFill(input, value);
        assertThat(output)
            .isDeepEqualTo(expected);
    }

    private static Stream<Arguments> provideForDeepFill() {
        return Stream.of(
            // Column and row are same lengths
            Arguments.of(new Boolean[2][2], false, new Boolean[][] {{false, false}, {false, false}}),
            // Column and row are different lengths
            Arguments.of(new Boolean[3][2], true, new Boolean[][] {{true, true}, {true, true}, {true, true}})
        );
    }

    @ParameterizedTest
    @MethodSource("provideForDeepFill_invalid")
    <E> void testForDeepFill_givenInvalidInputs(final E[][] input, final E value, final String errorMessage) {
        assertThatThrownBy(() -> ArrayUtils.deepFill(input, value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    private static Stream<Arguments> provideForDeepFill_invalid() {
        return Stream.of(
            Arguments.of(new Boolean[][] {}, true, "Input cannot be empty"),        // Empty 1D array
            Arguments.of(new Boolean[][] {{}, {}}, true, "Input cannot be empty")   // Empty 2D array
        );
    }

    @ParameterizedTest
    @MethodSource("provideForFindSmallestIndexGreaterThanThreshold")
    void testFindSmallestIndexGreaterThanThreshold(final int[] input, final int threshold, final int expected) {
        final int output = ArrayUtils.findSmallestIndexGreaterThanThreshold(input, threshold);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForFindSmallestIndexGreaterThanThreshold() {
        return Stream.of(
            Arguments.of(new int[] {1, 2, 3}, 2, 2),        // Single value above threshold (sorted)
            Arguments.of(new int[] {1, 2, 3}, 1, 1),        // Multiple values above threshold (sorted)
            Arguments.of(new int[] {3, 2, 1}, 2, 0),        // Single value above threshold (unsorted)
            Arguments.of(new int[] {2, 3, 1}, 1, 0),        // Multiple values above threshold (unsorted),
            Arguments.of(new int[] {-1, -2, -3}, -2, 0)     // Single value above threshold (negative)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFindSmallestIndexGreaterThanThreshold_invalid")
    void testFindSmallestIndexGreaterThanThreshold_givenInvalidInputs(final int[] input, final int threshold, final String errorMessage) {
        assertThatThrownBy(() -> ArrayUtils.findSmallestIndexGreaterThanThreshold(input, threshold))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    private static Stream<Arguments> provideFindSmallestIndexGreaterThanThreshold_invalid() {
        return Stream.of(
            Arguments.of(new int[] {1, 2, 3}, 3, "No value in input is greater than 3"),    // No value above threshold
            Arguments.of(new int[] {}, 1, "Input cannot be empty")                          // Empty array
        );
    }

    @ParameterizedTest
    @MethodSource("provideForReverseRows")
    <E> void testReverseRows(final E[][] input, final E[][] expected) {
        final E[][] output = ArrayUtils.reverseRows(input);
        assertThat(output)
            .isDeepEqualTo(expected);
    }

    private static Stream<Arguments> provideForReverseRows() {
        return Stream.of(
            // Valid input
            Arguments.of(
                new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e', 'f'},
                    {'g', 'h', 'i'}
                },
                new Character[][] {
                    {'g', 'h', 'i'},
                    {'d', 'e', 'f'},
                    {'a', 'b', 'c'}
                }
            ),
            // Different column lengths
            Arguments.of(
                new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e'},
                    {'f', 'g', 'h', 'i'}
                },
                new Character[][] {
                    {'f', 'g', 'h', 'i'},
                    {'d', 'e'},
                    {'a', 'b', 'c'}
                }
            ),
            // Different type
            Arguments.of(
                new Integer[][] {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                },
                new Integer[][] {
                    {7, 8, 9},
                    {4, 5, 6},
                    {1, 2, 3}
                }
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideForReverseRows_invalid")
    <E> void testReverseRows_givenInvalidInputs(final E[][] input, final String errorMessage) {
        assertThatThrownBy(() -> ArrayUtils.reverseRows(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    private static Stream<Arguments> provideForReverseRows_invalid() {
        return Stream.of(
            Arguments.of(new Character[][] {}, "Input cannot be empty"),         // Empty 1D array
            Arguments.of(new Character[][] {{}, {}}, "Input cannot be empty")    // Empty 2D array
        );
    }

    @ParameterizedTest
    @MethodSource("provideForRotate")
    <E> void testRotate(final E[][] input, final RotationDirection rotationDirection, final E[][] expected) {
        final E[][] output = ArrayUtils.rotate(input, rotationDirection);
        assertThat(output)
            .isDeepEqualTo(expected);
    }

    private static Stream<Arguments> provideForRotate() {
        return Stream.of(
            // Valid Character input, anti-clockwise rotation
            Arguments.of(
                new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e', 'f'},
                    {'g', 'h', 'i'}
                },
                RotationDirection.ANTI_CLOCKWISE,
                new Character[][] {
                    {'c', 'f', 'i'},
                    {'b', 'e', 'h'},
                    {'a', 'd', 'g'}
                }
            ),
            // Valid Character input, clockwise rotation
            Arguments.of(
                new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e', 'f'},
                    {'g', 'h', 'i'}
                },
                RotationDirection.CLOCKWISE,
                new Character[][] {
                    {'g', 'd', 'a'},
                    {'h', 'e', 'b'},
                    {'i', 'f', 'c'}
                }
            ),
            // Different type
            Arguments.of(
                new Integer[][] {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                },
                RotationDirection.CLOCKWISE,
                new Integer[][] {
                    {7, 4, 1},
                    {8, 5, 2},
                    {9, 6, 3}
                }
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideForRotate_invalid")
    <E> void testRotate_givenInvalidInputs(final E[][] input, final RotationDirection rotationDirection, final String errorMessage) {
        assertThatThrownBy(() -> ArrayUtils.rotate(input, rotationDirection))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    private static Stream<Arguments> provideForRotate_invalid() {
        return Stream.of(
            // Different column lengths for 2D array
            Arguments.of(
                new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e'},
                    {'f', 'g', 'h', 'i'}
                }, RotationDirection.CLOCKWISE, "Expected outer and inner lengths to be equal, found: 3 and 4"
            ),
            // Invalid rotation direction
            Arguments.of(new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e', 'f'},
                    {'g', 'h', 'i'}
                }, RotationDirection.INVALID, "Unable to handle rotation in direction: INVALID"
            ),
            Arguments.of(new Character[][] {}, RotationDirection.CLOCKWISE, "Input cannot be empty"),       // Empty 1D array
            Arguments.of(new Character[][] {{}, {}}, RotationDirection.CLOCKWISE, "Input cannot be empty")  // Empty 2D array
        );
    }

    @ParameterizedTest
    @MethodSource("provideForToArrayOfArrays")
    <E> void testToArrayOfArrays(final List<String> input, final Function<? super Character, ? extends E> converter, final E[][] expected) {
        final E[][] output = ArrayUtils.toArrayOfArrays(input, converter);
        assertThat(output)
            .isDeepEqualTo(expected);
    }

    private static Stream<Arguments> provideForToArrayOfArrays() {
        return Stream.of(
            // Valid list of Booleans
            Arguments.of(List.of("abb", "bab", "aaa"), (Function<Character, Boolean>) character -> character == 'a',
                new Boolean[][] {
                    {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE},
                    {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE},
                    {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}
                }
            ),
            // Valid list of Booleans, where first string is longer than others
            Arguments.of(List.of("abbb", "bab", "aaa"), (Function<Character, Boolean>) character -> character == 'a',
                new Boolean[][] {
                    {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
                    {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE},
                    {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}
                }
            ),
            // Valid list of Booleans, where last string is longer than others
            Arguments.of(List.of("abb", "bab", "aaaa"), (Function<Character, Boolean>) character -> character == 'a',
                new Boolean[][] {
                    {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE},
                    {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE},
                    {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}
                }
            ),
            // Valid list of Booleans, with no matches
            Arguments.of(List.of("abb", "bab", "aaa"), (Function<Character, Boolean>) character -> character == 'c',
                new Boolean[][] {
                    {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
                    {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
                    {Boolean.FALSE, Boolean.FALSE, Boolean.FALSE}
                }
            ),
            // Valid list of Characters
            Arguments.of(List.of("abc", "def", "ghi"), (Function<Character, Character>) character -> character,
                new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e', 'f'},
                    {'g', 'h', 'i'}
                }
            ),
            // Valid list of Characters, where first string is longer than others
            Arguments.of(List.of("abcd", "efg", "hij"), (Function<Character, Character>) character -> character,
                new Character[][] {
                    {'a', 'b', 'c', 'd'},
                    {'e', 'f', 'g'},
                    {'h', 'i', 'j'}
                }
            ),
            // Valid list of Characters, where last string is longer than others
            Arguments.of(List.of("abc", "def", "ghij"), (Function<Character, Character>) character -> character,
                new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e', 'f'},
                    {'g', 'h', 'i', 'j'}
                }
            ),
            // Valid list of Integers
            Arguments.of(List.of("123", "456", "789"), (Function<Character, Integer>) character -> NumberUtils.toIntOrDefault(character, 0),
                new Integer[][] {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                }
            ),
            // Valid list of Integers, where first string is longer than others
            Arguments.of(List.of("1234", "567", "890"), (Function<Character, Integer>) character -> NumberUtils.toIntOrDefault(character, 0),
                new Integer[][] {
                    {1, 2, 3, 4},
                    {5, 6, 7},
                    {8, 9, 0}
                }
            ),
            // Valid list of Integers, where last string is longer than others
            Arguments.of(List.of("123", "456", "7890"), (Function<Character, Integer>) character -> NumberUtils.toIntOrDefault(character, 0),
                new Integer[][] {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 0}
                }
            ),
            // List with some integers, some non-integers
            Arguments.of(List.of("123", "4a6", "789"), (Function<Character, Integer>) character -> NumberUtils.toIntOrDefault(character, 0),
                new Integer[][] {
                    {1, 2, 3},
                    {4, 0, 6},
                    {7, 8, 9}
                }
            ),
            // No integers
            Arguments.of(List.of("abc", "def", "ghi"), (Function<Character, Integer>) character -> NumberUtils.toIntOrDefault(character, 0),
                new Integer[][] {
                    {0, 0, 0},
                    {0, 0, 0},
                    {0, 0, 0}
                }
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideForToArrayOfArrays_invalid")
    <E> void testToArrayOfArrays_givenInvalidInputs(final List<String> input,
                                                    final Function<? super Character, ? extends E> converter,
                                                    final String errorMessage) {
        assertThatThrownBy(() -> ArrayUtils.toArrayOfArrays(input, converter))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    private static Stream<Arguments> provideForToArrayOfArrays_invalid() {
        return Stream.of(
            // List of empty string for Boolean conversion
            Arguments.of(List.of(""), (Function<Character, Boolean>) character -> character == 'a', "Input cannot be empty"),
            // Empty list for Boolean conversion
            Arguments.of(List.of(), (Function<Character, Boolean>) character -> character == 'a', "Input cannot be empty"),
            // List of empty string for Character conversion
            Arguments.of(List.of(""), (Function<Character, Character>) character -> character, "Input cannot be empty"),
            // Empty list for Character conversion
            Arguments.of(List.of(), (Function<Character, Character>) character -> character, "Input cannot be empty"),
            // List of empty string for Integer conversion
            Arguments.of(List.of(""), (Function<Character, Integer>) character -> NumberUtils.toIntOrDefault(character, 0), "Input cannot be empty"),
            // Empty list for Integer conversion
            Arguments.of(List.of(), (Function<Character, Integer>) character -> NumberUtils.toIntOrDefault(character, 0), "Input cannot be empty")
        );
    }

    @ParameterizedTest
    @MethodSource("provideForToListOfStrings")
    <E> void testToListOfStrings(final E[][] input, final Function<? super E, String> converter, final List<String> expected) {
        final List<String> output = ArrayUtils.toListOfStrings(input, converter);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForToListOfStrings() {
        return Stream.of(
            // Valid Boolean 2D array
            Arguments.of(
                new Boolean[][] {
                    {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE},
                    {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE},
                    {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}
                },
                (Function<Boolean, String>) booleanValue -> booleanValue ? "a" : "b",
                List.of("abb", "bab", "aaa")
            ),
            // Valid list of Booleans, where the first inner array is longer than others
            Arguments.of(
                new Boolean[][] {
                    {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
                    {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE},
                    {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}
                },
                (Function<Boolean, String>) booleanValue -> booleanValue ? "a" : "b",
                List.of("abbb", "bab", "aaa")
            ),
            // Valid list of Booleans, where last string is longer than others
            Arguments.of(
                new Boolean[][] {
                    {Boolean.TRUE, Boolean.FALSE, Boolean.FALSE},
                    {Boolean.FALSE, Boolean.TRUE, Boolean.FALSE},
                    {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}
                },
                (Function<Boolean, String>) booleanValue -> booleanValue ? "a" : "b",
                List.of("abb", "bab", "aaaa")
            ),
            // Valid list of Characters
            Arguments.of(
                new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e', 'f'},
                    {'g', 'h', 'i'}
                },
                (Function<Character, String>) String::valueOf,
                List.of("abc", "def", "ghi")
            ),
            // Valid list of Characters, where first string is longer than others
            Arguments.of(
                new Character[][] {
                    {'a', 'b', 'c', 'd'},
                    {'e', 'f', 'g'},
                    {'h', 'i', 'j'}
                },
                (Function<Character, String>) String::valueOf,
                List.of("abcd", "efg", "hij")
            ),
            // Valid list of Characters, where last string is longer than others
            Arguments.of(
                new Character[][] {
                    {'a', 'b', 'c'},
                    {'d', 'e', 'f'},
                    {'g', 'h', 'i', 'j'}
                },
                (Function<Character, String>) String::valueOf,
                List.of("abc", "def", "ghij")
            ),
            // Valid list of Integers
            Arguments.of(
                new Integer[][] {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                },
                (Function<Integer, String>) String::valueOf,
                List.of("123", "456", "789")
            ),
            // Valid list of Integers, where first string is longer than others
            Arguments.of(
                new Integer[][] {
                    {1, 2, 3, 4},
                    {5, 6, 7},
                    {8, 9, 0}
                },
                (Function<Integer, String>) String::valueOf,
                List.of("1234", "567", "890")
            ),
            // Valid list of Integers, where last string is longer than others
            Arguments.of(
                new Integer[][] {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9, 0}
                },
                (Function<Integer, String>) String::valueOf,
                List.of("123", "456", "7890")
            ),
            // Valid list of Integers, with multiple-digit Integers that are supported
            Arguments.of(
                new Integer[][] {
                    {11, 12, 13},
                    {14, 15, 16},
                    {17, 18, 19}
                },
                (Function<Integer, String>) String::valueOf,
                List.of("111213", "141516", "171819")
            ),
            // Valid list of Integers, with multiple-digit Integers that are truncated to the last digit only
            Arguments.of(
                new Integer[][] {
                    {11, 12, 13},
                    {14, 15, 16},
                    {17, 18, 19}
                },
                (Function<Integer, String>) integerValue -> String.valueOf(integerValue % 10),
                List.of("123", "456", "789")
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideForTranspose")
    <E> void testTranspose(final E[][] input, final E blankSpaceValue, final E[][] expected) {
        final E[][] output = ArrayUtils.transpose(input, blankSpaceValue);
        assertThat(output)
            .isDeepEqualTo(expected);
    }

    private static Stream<Arguments> provideForTranspose() {
        return Stream.of(
            // Valid
            Arguments.of(
                new Character[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}, {'j', 'k', 'l'}},
                ' ',
                new Character[][] {{'a', 'd', 'g', 'j'}, {'b', 'e', 'h', 'k'}, {'c', 'f', 'i', 'l'}}
            ),
            // Valid with gaps
            Arguments.of(
                new Character[][] {{'a', 'b', 'c'}, {'d', 'e', 'f', 'g'}, {'h', 'i'}},
                ' ',
                new Character[][] {{'a', 'd', 'h'}, {'b', 'e', 'i'}, {'c', 'f', ' '}, {' ', 'g', ' '}}
            ),
            // Different type with gaps
            Arguments.of(
                new Integer[][] {{1, 2, 3}, {4, 5, 6, 7}, {8, 9}},
                0,
                new Integer[][] {{1, 4, 8}, {2, 5, 9}, {3, 6, 0}, {0, 7, 0}}
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideForTranspose_invalid")
    <E> void testTranspose_givenInvalidInputs(final E[][] input, final E blankSpaceValue, final String errorMessage) {
        assertThatThrownBy(() -> ArrayUtils.transpose(input, blankSpaceValue))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    private static Stream<Arguments> provideForTranspose_invalid() {
        return Stream.of(
            Arguments.of(new Character[][] {}, ' ', "Input cannot be empty"),       // Empty 1D array
            Arguments.of(new Character[][] {{}, {}}, ' ', "Input cannot be empty")  // Empty 2D array
        );
    }
}
