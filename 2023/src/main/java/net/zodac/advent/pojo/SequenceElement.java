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

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Record used to define a single element of a comma-separated initialisation sequence.
 *
 * @param sequenceElement the full element, unchanged
 * @param label           the label of the element
 * @param operation       the operation (<b>'='</b> for add, <b>'-'</b> for remove)
 * @param focalLength     the focal length of the element, if it exists
 */
public record SequenceElement(String sequenceElement, String label, char operation, Optional<Integer> focalLength) {

    private static final Pattern LENS_PATTERN = Pattern.compile("([a-z]*)([=-])(\\d?)");
    private static final int HASH_ALGORITHM_MULTIPLIER = 17;
    private static final int HASH_ALGORITHM_MODULO = 256;
    private static final char ADD_OPERATION = '=';
    private static final char REMOVE_OPERATION = '-';

    /**
     * Creates an {@link SequenceElement} from a {@link String} in the format:
     * <pre>
     *     [label][operation][focalLength]
     * </pre>
     *
     * @param input the {@link CharSequence} to parse
     * @return the {@link SequenceElement}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static SequenceElement parse(final String input) {
        final Matcher matcher = LENS_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + input);
        }

        final String label = matcher.group(1);
        final char operation = matcher.group(2).charAt(0);
        final String rawFocalLength = matcher.group(3);
        final Optional<Integer> focalLength = rawFocalLength.isEmpty() ? Optional.empty() : Optional.of(Integer.parseInt(matcher.group(3)));
        return new SequenceElement(input, label, operation, focalLength);
    }

    /**
     * Calculates the HASH algorithm value for the {@link SequenceElement}.
     *
     * @return the HASH value
     */
    public int calculateHashOfSequenceElement() {
        return calculateHash(sequenceElement);
    }

    /**
     * Calculates the HASH algorithm value for the {@link SequenceElement#label()}.
     *
     * @return the HASH value
     */
    public int calculateHashOfLabel() {
        return calculateHash(label);
    }

    /**
     * The HASH is calculated for the provided {@link String} by performing the following actions:
     * <ul>
     *     <li>Start a {@code counter} at <b>0</b></li>
     *      <li>For each {@link Character}:
     *      <ol>
     *          <li>Get the {@link #getAsciiValue(char)}, add to the {@code counter}</li>
     *          <li>Multiply the {@code counter} by {@link #HASH_ALGORITHM_MULTIPLIER}</li>
     *          <li>Get the remainder of the {@code counter} after dividing by {@link #HASH_ALGORITHM_MODULO}</li>
     *      </ol>
     *     </li>
     * </ul>
     *
     * @param input the {@link String} to HASH
     * @return the HASH value
     */
    public static int calculateHash(final String input) {
        int current = 0;
        for (final char c : input.toCharArray()) {
            current += getAsciiValue(c);
            current *= HASH_ALGORITHM_MULTIPLIER;
            current = current % HASH_ALGORITHM_MODULO;
        }
        return current;
    }

    private static int getAsciiValue(final char c) {
        return c;
    }

    /**
     * Checks if the {@link SequenceElement} is an 'add' operation.
     *
     * @return {@code true} if the {@link SequenceElement#operation()} is <b>'='</b>
     */
    public boolean isAddOperation() {
        return operation == ADD_OPERATION;
    }

    /**
     * Checks if the {@link SequenceElement} is a 'remove' operation.
     *
     * @return {@code true} if the {@link SequenceElement#operation()} is <b>'-'</b>
     */
    public boolean isRemoveOperation() {
        return operation == REMOVE_OPERATION;
    }
}
