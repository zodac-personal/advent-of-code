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

package net.zodac.advent;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import net.zodac.advent.pojo.SequenceElement;

/**
 * Solution for 2023, Day 15.
 *
 * @see <a href="https://adventofcode.com/2023/day/15">[2023: 15] Lens Library</a>
 */
public final class Day15 {

    private Day15() {

    }

    /**
     * For each {@link SequenceElement}, we calculate the HASH algorithm, using {@link SequenceElement#calculateHashOfSequenceElement()}.
     * We then sum these values up.
     *
     * @param sequenceElements the input {@link SequenceElement}s to hash
     * @return the sum of all HASH values
     */
    public static long calculateSumOfHashAlgorithms(final Collection<SequenceElement> sequenceElements) {
        return sequenceElements
            .stream()
            .mapToInt(SequenceElement::calculateHashOfSequenceElement)
            .sum();
    }

    /**
     * For each {@link SequenceElement}, we calculate the HASH algorithm of the {@link SequenceElement#label()}, which defined which 'box' the
     * {@link SequenceElement} belongs to. There are 256 boxes, <b>0</b>-<b>255</b>. Based on the {@link SequenceElement#operation()}, we populate the
     * boxes based on the following rules:
     * <ul>
     *     <li>{@link SequenceElement#isAddOperation()}: If no {@link SequenceElement} with a matching {@link SequenceElement#label()} already exists,
     *     the current {@link SequenceElement} is appended to the box</li>
     *     <li>{@link SequenceElement#isAddOperation()}: If a {@link SequenceElement} with a matching {@link SequenceElement#label()} already exists,
     *     the current {@link SequenceElement} overwrites the existing value</li>
     *     <li>{@link SequenceElement#isRemoveOperation()}: If a {@link SequenceElement} with a matching {@link SequenceElement#label()} already
     *     exists, it is removed from the box</li>
     * </ul>
     *
     * <p>
     * Once the boxes have all been filled, the focus power of all {@link SequenceElement}s in each box is calculated by:
     * <pre>
     *     (@code boxNumber + 1) * (@code indexOfElementInBox + 1) * {@code focalLengthOfElement}
     * </pre>
     *
     * <p>
     * The total focus power of each box is then summed up and returned.
     *
     * @param sequenceElements the input {@link SequenceElement}s from which to calculate the total focus power
     * @return the sum of focus powers
     */
    public static long calculateTotalFocusPower(final Collection<SequenceElement> sequenceElements) {
        final Map<Integer, Map<String, Integer>> boxes = new LinkedHashMap<>();

        for (final SequenceElement sequenceElement : sequenceElements) {
            final int box = sequenceElement.calculateHashOfLabel();
            final Map<String, Integer> currentBox = boxes.getOrDefault(box, new LinkedHashMap<>());

            if (sequenceElement.isAddOperation()) {
                final int focalLength = sequenceElement.focalLength()
                    .orElseThrow(() -> new IllegalStateException("Cannot find focalLength for add operation: " + sequenceElement));
                currentBox.put(sequenceElement.label(), focalLength);
            } else if (sequenceElement.isRemoveOperation()) {
                currentBox.remove(sequenceElement.label());
            }
            boxes.put(box, currentBox);
        }

        return calculateFocusPower(boxes);
    }

    private static long calculateFocusPower(final Map<Integer, ? extends Map<String, Integer>> boxes) {
        return boxes
            .entrySet()
            .stream()
            .mapToLong(Day15::calculateFocusPowerForBox)
            .sum();
    }

    private static Long calculateFocusPowerForBox(final Map.Entry<Integer, ? extends Map<String, Integer>> boxEntry) {
        long boxTotal = 0L;

        final int boxMultiplier = boxEntry.getKey() + 1;
        int slot = 1;
        for (final int focalLength : boxEntry.getValue().values()) {
            boxTotal += ((long) boxMultiplier * slot * focalLength);
            slot++;
        }

        return boxTotal;
    }
}
