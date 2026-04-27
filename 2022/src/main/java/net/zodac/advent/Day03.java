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
import java.util.List;
import java.util.Set;
import net.zodac.advent.pojo.tuple.Pair;
import net.zodac.advent.util.CollectionUtils;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2022, Day 3.
 *
 * @see <a href="https://adventofcode.com/2022/day/3">AoC 2022, Day 3</a>
 */
public final class Day03 {

    // Add a meaningless first character, so we can use a String#indexOf() call without any offset
    private static final String VALUES = "*abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private Day03() {

    }

    /**
     * Given an input of {@link String}s, where each {@link String} has an even number of characters, we can split the {@link String} into two. We
     * then search for the common {@link Character}s between the two {@link String} half. We assume there is only 1 single common {@link Character}.
     *
     * <p>
     * We then give each common {@link Character} a value based on:
     * <pre>
     *     a: 1
     *     b: 2
     *     ...
     *     y: 25
     *     z: 26
     *     A: 27
     *     B: 28
     *     ...
     *     Y: 51
     *     Z: 52
     * </pre>
     *
     * <p>
     * Finally, we sum all the values of the common {@link Character} per {@link String}
     *
     * @param values the input {@link String}s
     * @return the sum of all common {@link Character} values
     */
    public static long sumCommonCharacterValuesInStringHalves(final Collection<String> values) {
        long total = 0L;

        for (final String value : values) {
            final Pair<String, String> bisectedString = StringUtils.bisect(value);
            final Set<Character> commonCharacters = StringUtils.commonChars(bisectedString.first(), bisectedString.second());
            final Character commonCharacter = getFirst(commonCharacters); // Assuming only one common char
            total += VALUES.indexOf(commonCharacter);
        }

        return total;
    }

    /**
     * Given an input of {@link String}s, we group them together into groups of {@code amountPerGroup}, then find the common {@link Character} in all
     * {@link String}s within a group. We assume there is only 1 single common {@link Character}.
     *
     * <p>
     * We then give each common {@link Character} a value based on:
     * <pre>
     *     a: 1
     *     b: 2
     *     ...
     *     y: 25
     *     z: 26
     *     A: 27
     *     B: 28
     *     ...
     *     Y: 51
     *     Z: 52
     * </pre>
     *
     * <p>
     * Finally, we sum all the values of the common {@link Character} per {@link String}
     *
     * @param values         the input {@link String}s
     * @param amountPerGroup the number of entries per group of {@link String}s
     * @return the sum of all common {@link Character} values
     */
    public static long sumCommonCharacterValuesInGroupedStrings(final Collection<String> values, final int amountPerGroup) {
        long total = 0L;

        final List<List<String>> groupedValues = CollectionUtils.groupBySize(values, amountPerGroup);

        for (final List<String> groupedValue : groupedValues) {
            final List<String> groupAsList = new ArrayList<>(groupedValue);
            final Set<Character> commonCharacters = StringUtils.commonChars(groupAsList.get(0), groupAsList.get(1), groupAsList.get(2));
            final Character commonCharacter = getFirst(commonCharacters); // Assuming only one common char
            total += VALUES.indexOf(commonCharacter);
        }

        return total;
    }

    private static <E> E getFirst(final Set<E> set) {
        final List<E> temp = new ArrayList<>(set);
        return temp.getFirst();
    }
}
