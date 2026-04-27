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

package net.zodac.advent.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class used to parse input an {@link String} into a {@link JsonElement}.
 */
public final class JsonParser {

    private static final char JSON_LIST_START = '[';
    private static final char JSON_LIST_END = ']';
    private static final char JSON_DELIMITER = ',';
    private static final int MINIMUM_LENGTH_TO_PARSE_AS_LIST = 2;

    private JsonParser() {

    }

    /**
     * Parses the input {@link String} into a {@link JsonElement}.
     *
     * @param input the {@link String} to parse
     * @return the parsed {@link JsonElement}
     * @throws IllegalArgumentException      thrown if the input is null or blank
     * @throws UnsupportedOperationException thrown if the input is not a valid {@link JsonInteger} or {@link JsonList}
     */
    public static JsonElement parse(final String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be null or blank");
        }

        final char firstChar = input.charAt(0);
        if (Character.isDigit(firstChar)) {
            return JsonInteger.create(input);
        }

        if (firstChar != JSON_LIST_START) {
            throw new UnsupportedOperationException("Unable to parse input starting with: " + firstChar);
        }

        // Assuming JsonList
        if (input.length() < MINIMUM_LENGTH_TO_PARSE_AS_LIST) {
            return JsonList.create(new ArrayList<>());

        }

        return JsonList.create(parseListElements(input));
    }

    private static List<JsonElement> parseListElements(final String input) {
        final List<JsonElement> jsonElements = new ArrayList<>();
        final int inputLength = input.length();
        int elementDepth = 0;
        int stringIndex = 1;

        for (int subStringIndex = stringIndex; subStringIndex < inputLength; subStringIndex++) {
            final char currentChar = input.charAt(subStringIndex);
            switch (currentChar) {
                case JSON_LIST_START -> elementDepth++;
                case JSON_LIST_END -> elementDepth--;
                case JSON_DELIMITER -> {
                    if (elementDepth == 0) {
                        final String nextElement = input.substring(stringIndex, subStringIndex);
                        jsonElements.add(parse(nextElement));
                        stringIndex = subStringIndex + 1;
                    }
                }
                default -> {
                }
            }
        }

        if (stringIndex < input.length() - 1) {
            final String nextElement = input.substring(stringIndex, input.length() - 1);
            jsonElements.add(parse(nextElement));
        }

        return jsonElements;
    }
}
