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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Solution for 2024, Day 19.
 *
 * @see <a href="https://adventofcode.com/2024/day/19">[2024: 19] Linen Layout</a>
 */
public final class Day19 {

    private Day19() {

    }

    /**
     * Given a {@link List} of values (representing a final 'design'), and a {@link List} of keys (representing a single 'towel' that is part of the
     * full design), find all values have a possible valid combination from the provided keys.
     *
     * @param towels       the input keys (towels to make the designs)
     * @param finalDesigns the input values (designs)
     * @return the number of valid designs that can be created
     */
    public static long countPossibleDesigns(final List<String> towels, final Collection<String> finalDesigns) {
        return finalDesigns
            .stream()
            .filter(design -> hasDesignAnyValidCombinations(design, towels))
            .count();
    }

    /**
     * Given a {@link List} of values (representing a final 'design'), and a {@link List} of keys (representing a single 'towel' that is part of the
     * full design), find all values have a possible valid combination from the provided keys. For each of these, find the total number of possible
     * combinations of towels that can make the design.
     *
     * @param towels       the input keys (towels to make the designs)
     * @param finalDesigns the input values (designs)
     * @return the total number of towel combinations to create valid designs
     */
    public static long countAllCombinationsOfValidDesigns(final List<String> towels, final Collection<String> finalDesigns) {
        return finalDesigns
            .stream()
            .filter(design -> hasDesignAnyValidCombinations(design, towels))
            .mapToLong(design -> countCombinationsForDesign(design, towels, new HashMap<>()))
            .sum();
    }

    private static boolean hasDesignAnyValidCombinations(final String value, final List<String> keys) {
        return keys
            .stream()
            .anyMatch(key -> value.equals(key) || (value.startsWith(key) && hasDesignAnyValidCombinations(value.substring(key.length()), keys)));
    }

    private static long countCombinationsForDesign(final String design, final List<String> towels, final Map<? super String, Long> cache) {
        if (cache.containsKey(design)) {
            return cache.get(design);
        }

        long numberOfCombinations = 0L;
        for (final String towel : towels) {
            if (design.equals(towel)) {
                numberOfCombinations++;
            } else if (design.startsWith(towel)) {
                numberOfCombinations += countCombinationsForDesign(design.substring(towel.length()), towels, cache);
            }
        }

        cache.put(design, numberOfCombinations);
        return numberOfCombinations;
    }
}
