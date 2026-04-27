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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.zodac.advent.pojo.RotationDirection;

/**
 * Utility functions for arrays.
 */
public final class ArrayUtils {

    private ArrayUtils() {

    }

    /**
     * Counts the number of elements along the edge of a 2D array. This is calculated as:
     * <pre>
     *     (length - 1) * 4
     * </pre>
     *
     * @param input the array to check
     * @param <E>   the type of the array
     * @return the number of elements on the perimeter
     */
    public static <E> int countPerimeterElements(final E[][] input) {
        if (input.length == 0) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        final int outerLength = input.length;
        final int innerLength = input[0].length;

        if (outerLength != innerLength) {
            throw new IllegalArgumentException(
                String.format("Outer size must match inner size, found outer: %s, inner: %s", outerLength, innerLength));
        }

        return (input.length - 1) << 2;
    }

    /**
     * Creates a deep copy of the 2D array.
     *
     * @param input the 2D array to copy
     * @param <E>   the type of the 2D array
     * @return the copied 2D array
     */
    public static <E> E[][] deepCopy(final E[][] input) {
        final E[][] outerCopy = Arrays.copyOf(input, input.length);

        for (int i = 0; i < input.length; i++) {
            outerCopy[i] = Arrays.copyOf(input[i], input[i].length);
        }

        return outerCopy;
    }

    /**
     * Given a 2D array, we create a new array and fill it with the provided {@code value}. The input array is unmodified.
     *
     * @param input the input array
     * @param value the value to set in the array
     * @param <E>   the type of the {@code input}
     * @return the filled array
     * @throws IllegalArgumentException thrown if the input is empty
     */
    public static <E> E[][] deepFill(final E[][] input, final E value) {
        if (input.length == 0 || input[0].length == 0) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        final E[][] array = deepCopy(input);
        for (final E[] innerArray : array) {
            for (int j = 0; j < array[0].length; j++) {
                innerArray[j] = value;
            }
        }

        return array;
    }

    /**
     * Given an unsorted {@code int} array, finds the smallest index that has a value greater than the {@code thresholdValue}.
     *
     * @param input          the input to check
     * @param thresholdValue the threshold value that any array entry must be greater than
     * @return the smallest index in the {@code int} array
     * @throws IllegalArgumentException thrown if the input is empty or does not have any value greater than the {@code thresholdValue}
     */
    public static int findSmallestIndexGreaterThanThreshold(final int[] input, final int thresholdValue) {
        if (input.length == 0) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        for (int i = 0; i < input.length; i++) {
            final int houseValue = input[i];

            if (houseValue > thresholdValue) {
                return i;
            }
        }

        throw new IllegalArgumentException(String.format("No value in input is greater than %s", thresholdValue));
    }

    /**
     * Given a 2D array, reverses the order of the rows. For example, given the 2D array:
     * <pre>
     *     [
     *      ['a', 'b', 'c'],
     *      ['d', 'e', 'f'],
     *      ['g', 'h', 'i'],
     *      ['j', 'k', 'l']
     *     ]
     * </pre>
     *
     * <p>
     * We would then return:
     * <pre>
     *     [
     *      ['j', 'k', 'l'],
     *      ['g', 'h', 'i'],
     *      ['d', 'e', 'f'],
     *      ['a', 'b', 'c']
     *     ]
     * </pre>
     *
     * @param input the input 2D array
     * @param <E>   the type of the {@code input}
     * @return the reversed 2D array
     * @throws IllegalArgumentException thrown if the input is empty
     */
    public static <E> E[][] reverseRows(final E[][] input) {
        if (input.length == 0 || input[0].length == 0) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        final int outerLength = input.length;
        final E[][] output = create2DimensionalArray(input[0][0].getClass(), outerLength, outerLength);
        for (int i = 0; i < outerLength; i++) {
            output[i] = input[outerLength - 1 - i];
        }
        return output;
    }

    /**
     * Rotates the provided 2D array 90° in the {@code rotationDirection}.
     *
     * @param input             the 2D array to rotate
     * @param rotationDirection the direction in which to rotate the array
     * @param <E>               the type of the 2D array
     * @return the rotated array
     * @throws IllegalArgumentException thrown if the input is empty or the number of rows and columns do not match
     */
    public static <E> E[][] rotate(final E[][] input, final RotationDirection rotationDirection) {
        if (input.length == 0 || input[0].length == 0) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        final int outerLength = input.length;
        final int innerLength = Arrays.stream(input)
            .mapToInt(array -> array.length)
            .max()
            .orElse(outerLength);

        if (outerLength != innerLength) {
            throw new IllegalArgumentException(
                String.format("Expected outer and inner lengths to be equal, found: %d and %d", outerLength, innerLength));
        }

        final E[][] output = create2DimensionalArray(input[0][0].getClass(), outerLength, innerLength);

        for (int row = 0; row < outerLength; row++) {
            for (int column = 0; column < innerLength; column++) {
                switch (rotationDirection) {
                    case ANTI_CLOCKWISE -> output[row][column] = input[column][outerLength - row - 1];
                    case CLOCKWISE -> output[column][outerLength - 1 - row] = input[row][column];
                    default -> throw new IllegalArgumentException("Unable to handle rotation in direction: " + rotationDirection);
                }
            }
        }

        return output;
    }

    /**
     * Converts the provided {@link List} of {@link String}s into a 2D array. We assume each character in the {@link String} is convertible to the
     * wanted output type, and that conversion is defined by the provided {@link Function} {@code converter}.
     *
     * <p>
     * Given a {@link List} as below, we would like to convert this to a 2D array of {@link Integer}. The input is:
     * <pre>
     *     [
     *       "1234",
     *       "567",
     *       "89",
     *       "012"
     *     ]
     * </pre>
     *
     * <p>
     * We would also provide the following {@link Function} to convert each individual {@link Character} into an {@link Integer}:
     * <pre>
     *     character -> NumberUtils.toIntOrDefault(character, 0)
     * </pre>
     *
     * <p>
     * The result will be:
     * <pre>
     *     [
     *       [1, 2, 3, 4],
     *       [5, 6, 7],
     *       [8, 9],
     *       [0, 1, 2]
     *     ]
     * </pre>
     *
     * <p>
     * Similarly, given an input that we want to convert to a 2D array of {@link Boolean}s, we would have the following:
     * <pre>
     *     [
     *       "abbb",
     *       "bab",
     *       "aa",
     *       "bbb"
     *     ]
     * </pre>
     *
     * <p>
     * And the converter {@link Function}:
     * <pre>
     *     (character -> character == 'a'')
     * </pre>
     *
     * <p>
     * The result will be:
     * <pre>
     *     [
     *       [TRUE, FALSE, FALSE, FALSE],
     *       [FALSE, TRUE, FALSE],
     *       [TRUE, TRUE],
     *       [FALSE, FALSE, FALSE]
     *     ]
     * </pre>
     *
     * @param input     the input {@link List} of {@link String}s
     * @param converter the {@link Function} to convert a {@link Character} from the input {@link String}s into the correct type for the 2D array
     * @param <E>       the type of the 2D array
     * @return the 2D array
     */
    public static <E> E[][] toArrayOfArrays(final List<String> input, final Function<? super Character, ? extends E> converter) {
        if (input.isEmpty() || input.getFirst().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        final int outerLength = input.size();
        final int innerLength = input
            .stream()
            .mapToInt(String::length)
            .max()
            .orElse(outerLength);

        // Retrieve the first converted character, so we can create the array with the correct type
        final Class<?> firstThing = converter.apply(input.getFirst().charAt(0)).getClass();
        final E[][] arrayOfCharacterArrays = create2DimensionalArray(firstThing, outerLength, innerLength);
        final int inputLength = input.size();

        for (int i = 0; i < inputLength; i++) {
            final String line = input.get(i);
            arrayOfCharacterArrays[i] = toArray(line, converter, firstThing);
        }

        return arrayOfCharacterArrays;
    }

    private static <E> E[] toArray(final String input, final Function<? super Character, ? extends E> converter, final Class<?> convertedClass) {
        final int inputLength = input.length();
        final E[] array = createArray(convertedClass, inputLength);

        for (int i = 0; i < inputLength; i++) {
            array[i] = converter.apply(input.charAt(i));
        }
        return array;
    }

    /**
     * Given a 2D array, converts the values to a {@link List} of {@link String}s.
     *
     * @param arrayOfArrays the 2D array
     * @param converter     {@link Function} to convert a value of type {@code E} from the input 2D array into a {@link String} for the output
     * @param <E>           the type of the {@code array}
     * @return the {@link List} of {@link String}s
     */
    public static <E> List<String> toListOfStrings(final E[][] arrayOfArrays, final Function<? super E, String> converter) {
        final List<String> output = new ArrayList<>();

        for (final E[] row : arrayOfArrays) {
            final StringBuilder rowBuilder = new StringBuilder();
            for (final E element : row) {
                final String newValue = converter.apply(element);
                rowBuilder.append(newValue);
            }
            output.add(rowBuilder.toString());
        }

        return output;
    }

    /**
     * Performs a transpose on the provided 2D array. A transpose 'flips' the 2D array along the diagonal axis. For example, given the 2D array:
     * <pre>
     *     [
     *      ['a', 'b', 'c'],
     *      ['d', 'e', 'f'],
     *      ['g', 'h', 'i'],
     *      ['j', 'k', 'l']
     *     ]
     * </pre>
     *
     * <p>
     * We would then return:
     * <pre>
     *     [
     *      ['a', 'd', 'g', 'j'],
     *      ['b', 'e', 'h', 'k'],
     *      ['c', 'f', 'i', 'l']
     *     ]
     * </pre>
     *
     * <p>
     * <b>NOTE:</b> If the inner arrays are of different lengths, the shorter arrays will be filled with {@code blankSpaceValue} to fill them up.
     *
     * @param input           the input 2D array
     * @param blankSpaceValue the value to 'fill' in any gaps in the transposed array
     * @param <E>             the type of the {@code input}
     * @return the transposed 2D array
     * @throws IllegalArgumentException thrown if the input is empty
     */
    public static <E> E[][] transpose(final E[][] input, final E blankSpaceValue) {
        if (input.length == 0 || input[0].length == 0) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        final int outerLength = input.length;
        final int innerLength = Arrays.stream(input)
            .mapToInt(array -> array.length)
            .max()
            .orElse(outerLength);

        // Flipping inner and outer lengths for the new 2D array
        final E[][] transposedArray = create2DimensionalArray(input[0][0].getClass(), innerLength, outerLength);
        for (int i = 0; i < innerLength; i++) {
            for (int j = 0; j < outerLength; j++) {
                if (i >= input[j].length) {
                    transposedArray[i][j] = blankSpaceValue;
                } else {
                    transposedArray[i][j] = input[j][i];
                }
            }
        }

        return transposedArray;
    }

    @SuppressWarnings("unchecked") // Creating a 1D array of a generic type
    private static <E> E[] createArray(final Class<?> arrayClass, final int length) {
        return (E[]) Array.newInstance(arrayClass, length);
    }

    @SuppressWarnings("unchecked") // Creating a 2D array of a generic type
    private static <E> E[][] create2DimensionalArray(final Class<?> arrayClass, final int outerLength, final int innerLength) {
        return (E[][]) Array.newInstance(arrayClass, outerLength, innerLength);
    }
}
