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

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class used to parse the values of a {@link SegmentedDisplay}. The {@link SegmentedDisplay} is backed by a large 2D boolean array. By
 * chunking the full grid into smaller grids (the length of each character), we can check to see if the smaller 2D boolean array is a valid letter.
 */
final class SegmentedDisplayAlphabetMapper {

    private static final Map<String, String> ALPHABET_MAPPER = createMapper();

    private SegmentedDisplayAlphabetMapper() {

    }

    /**
     * Takes the input 2D boolean array, and checks if it is a valid letter.
     *
     * @param character the 2D boolean array representing a character
     * @return the letter, or an empty {@link String}
     */
    static String getOrEmpty(final boolean[][] character) {
        final String key = convert(character);
        return ALPHABET_MAPPER.getOrDefault(key, "");
    }

    // Missing some values, will add if they are found
    private static Map<String, String> createMapper() {
        final Map<String, String> map = new HashMap<>();

        map.put("011010011001111110011001", "A");
        map.put("111010011110100110011110", "B");
        map.put("011010011000100010010110", "C");
        map.put("111110001110100010001111", "E");
        map.put("111110001110100010001000", "F");
        map.put("011010011000101110010111", "G");
        map.put("100110011111100110011001", "H");
        map.put("001100010001000110010110", "J");
        map.put("100110101100101010101001", "K");
        map.put("100010001000100010001111", "L");
        map.put("111010011001111010001000", "P");
        map.put("111010011001111010101001", "R");
        map.put("100110011001100110010110", "U");
        map.put("111100010010010010001111", "Z");

        return map;
    }

    private static String convert(final boolean[][] input) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (final boolean[] rows : input) {
            for (int col = 0; col < input[0].length; col++) {
                stringBuilder.append(rows[col] ? "1" : "0");
            }
        }

        return stringBuilder.toString();
    }
}
