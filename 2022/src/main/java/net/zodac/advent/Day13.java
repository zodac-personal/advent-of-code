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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.zodac.advent.json.JsonElement;
import net.zodac.advent.json.JsonInteger;
import net.zodac.advent.json.JsonList;
import net.zodac.advent.json.JsonParser;
import net.zodac.advent.util.CollectionUtils;

/**
 * Solution for 2022, Day 13.
 *
 * @see <a href="https://adventofcode.com/2022/day/13">AoC 2022, Day 13</a>
 */
public final class Day13 {

    private static final JsonElement DISTRESS_SIGNAL_1 = JsonParser.parse("[[2]]");
    private static final JsonElement DISTRESS_SIGNAL_2 = JsonParser.parse("[[6]]");
    private static final int LEFT_IS_ORDERED_CORRECTLY_THRESHOLD = 1;

    private Day13() {

    }

    /**
     * Given some input {@link JsonElement}s, we group the {@link JsonElement}s into pairs, then compare each pair. We assume the input will be a
     * {@link JsonList} of more {@link JsonElement}s, so we iterate through the outer {@link JsonList}s and
     * compare value by value. The comparison rules are below. If the values are equal, we move to the next rule.
     *
     * <ol>
     *     <li>If both values are a {@link JsonInteger}, the left side should be a lower value</li>
     *     <li>If both values are a {@link JsonList}, iterate through each list and apply the rules</li>
     *     <li>If one value is a {@link JsonInteger}, convert to a {@link JsonList} and apply the rules</li>
     * </ol>
     *
     * <p>
     * When comparing pairs, if ordered correctly (left side is less than right side), then we add that pair's index to the total.
     *
     * <p>
     * <b>NOTE:</b> The pair indices start from <b>1</b>.
     *
     * @param values the input {@link JsonElement}s
     * @return the sum of all valid indices
     */
    public static long calculateSumOfValidIndices(final Collection<? extends JsonElement> values) {
        final List<List<JsonElement>> jsonElementGroups = CollectionUtils.groupBySize(values, 2);

        final int numberOfElementGroups = jsonElementGroups.size();
        int total = 0;
        for (int i = 0; i < numberOfElementGroups; i++) {
            final List<JsonElement> jsonElementGroup = jsonElementGroups.get(i);
            final JsonElement left = jsonElementGroup.get(0);
            final JsonElement right = jsonElementGroup.get(1);

            if (left.compareTo(right) < LEFT_IS_ORDERED_CORRECTLY_THRESHOLD) {
                total += (i + 1);
            }
        }

        return total;
    }

    /**
     * Given some input {@link JsonElement}s, we add the known distress signals {@code [[2]]} and {@code [[6]]}, then sort all {@link JsonElement}s.
     * Once sorted, we find the indices of both distress signals, then multiply them
     *
     * @param values the input {@link JsonElement}s
     * @return the product of the distress signal indices
     */
    public static long calculateProductOfDistressSignalIndices(final Collection<? extends JsonElement> values) {
        final List<JsonElement> allJsonElements = new ArrayList<>(values);

        // Add distress signals
        allJsonElements.add(DISTRESS_SIGNAL_1);
        allJsonElements.add(DISTRESS_SIGNAL_2);

        // All elements must be ordered
        Collections.sort(allJsonElements);

        final int firstSignalIndex = allJsonElements.indexOf(DISTRESS_SIGNAL_1) + 1;
        final int secondSignalIndex = allJsonElements.indexOf(DISTRESS_SIGNAL_2) + 1;
        return (long) firstSignalIndex * secondSignalIndex;
    }
}
