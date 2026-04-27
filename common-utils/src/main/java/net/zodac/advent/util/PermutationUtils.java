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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utility class for permutations of {@link java.util.Collection}s.
 */
public final class PermutationUtils {

    private PermutationUtils() {

    }

    /**
     * Generates permutations based on the input {@link List}.
     *
     * <p>
     * Given an input {@link List} of {@link String}s:
     * <pre>
     *      England
     *      Ireland
     *      Sweden
     * </pre>
     *
     * <p>
     * We would have 6 permutations (3!):
     * <pre>
     *     England, Ireland,  Sweden
     *     England,  Sweden, Ireland
     *     Ireland, England,  Sweden
     *     Ireland,  Sweden, England
     *      Sweden, England, Ireland
     *      Sweden, Ireland, England
     * </pre>
     *
     * @param input the {@link List} from which permutations should be generated
     * @param <E>   the type of the input {@link List}
     * @return the {@link List} of permutations
     */
    public static <E> List<List<E>> generateAll(final List<? extends E> input) {
        if (input.isEmpty()) {
            return List.of(List.of());
        }

        final List<? extends E> modifiableInput = new ArrayList<>(input);
        final E firstElement = modifiableInput.removeFirst();
        final List<List<E>> returnValue = new ArrayList<>();
        final List<List<E>> permutations = generateAll(modifiableInput);

        for (final List<E> smallerPermutation : permutations) {
            final int sizeOfSmallerPermutation = smallerPermutation.size();

            for (int index = 0; index <= sizeOfSmallerPermutation; index++) {
                final List<E> temp = new ArrayList<>(smallerPermutation);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }

        return returnValue;
    }

    /**
     * Generates permutations based on the input {@link List}. It will return all permutations of the original {@link List} where a single element has
     * been removed.
     *
     * <p>
     * Given an input {@link List} of {@link String}s:
     * <pre>
     *      England
     *      Ireland
     *      Sweden
     *      New Zealand
     * </pre>
     *
     * <p>
     * We would have 4 permutations:
     * <pre>
     *     England, Ireland,      Sweden
     *     England, Ireland, New Zealand
     *     England,  Sweden, New Zealand
     *     Ireland,  Sweden, New Zealand
     * </pre>
     *
     * @param input the {@link List} from which permutations should be generated
     * @param <E>   the type of the input {@link List}
     * @return the {@link List} of permutations
     */
    public static <E> List<List<E>> generateAllWithOneRemovedEntry(final List<? extends E> input) {
        final List<List<E>> combinations = new ArrayList<>();

        final int size = input.size();
        for (int i = 0; i < size; i++) {
            final List<E> combination = new ArrayList<>(input);
            combination.remove(i);
            combinations.add(combination);
        }

        return combinations;
    }

    /**
     * Given an input {@link List} of values, generates every possible combination where the gaps between values are replaced by each
     * possible separator provided. For example, given an input of:
     *
     * <pre>
     *     [ "A", "B", "C" ]
     * </pre>
     *
     * <p>
     * And a {@link Set} of {@link Integer}s as separators:
     *
     * <pre>
     *     [ 1, 6 ]
     * </pre>
     *
     * <p>
     * Then the output would be:
     *
     * <pre>
     *     [
     *          [ "A", "1", "B", "1", "C" ],
     *          [ "A", "1", "B", "6", "C" ],
     *          [ "A", "6", "B", "1", "C" ],
     *          [ "A", "6", "B", "6", "C" ],
     *     ]
     * </pre>
     *
     * <p>
     * <b>NOTE:</b> {@link String#valueOf(Object)} will be used to convert the value and separator into a {@link String}, so ensure the
     * {@link Object#toString()} has been overridden if necessary.
     *
     * @param values     the input {@link String}s
     * @param separators the values to make the combinations
     * @param <V>        the type of the input values
     * @param <S>        the type of the separators
     * @return the a {@link List} of all combinations
     */
    public static <V, S> List<List<String>> generateWithSeparators(final List<V> values, final Set<S> separators) {
        if (values.isEmpty() || separators.isEmpty()) {
            return List.of(values.stream().map(String::valueOf).toList());
        }

        final List<List<String>> result = new ArrayList<>();
        // Start recursive generation with initial index
        generate(values, 0, new ArrayList<>(), result, separators);
        return result;
    }

    private static <V, S> void generate(final List<V> values,
                                     final int index,
                                     final List<String> current,
                                     final List<? super List<String>> result,
                                     final Set<S> separators
    ) {
        // Base case for last number
        if (index == values.size() - 1) {
            current.add(String.valueOf(values.get(index)));
            result.add(new ArrayList<>(current));
            current.removeLast();
            return;
        }

        current.add(String.valueOf(values.get(index)));

        // Recursively add combinations with separators
        for (final S separator : separators) {
            current.add(String.valueOf(separator));
            generate(values, index + 1, current, result, separators); // Recursively move to the next number
            current.removeLast(); // Backtrack to ensure last separator is removed
        }

        current.removeLast(); // Backtrack to ensure last value is removed
    }
}
