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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for 2023, Day 1.
 *
 * @see <a href="https://adventofcode.com/2023/day/1">[2023: 01] Trebuchet?!</a>
 */
public final class Day01 {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d");
    private static final Pattern NUMBER_AS_WORDS_PATTERN = Pattern.compile("\\d|one|two|three|four|five|six|seven|eight|nine");

    private Day01() {

    }

    /**
     * Given an input list of {@link String}s representing a calibration value, we extract the first and last {@link Integer} from the calibration
     * value. These are then concatenated to form the actual calibration value for the {@link String}. These values are then summed.
     *
     * <p>
     * If only a single {@link Integer} is found, it is considered both the first and the last {@link Integer} for that calibration value.
     *
     * @param calibrationValues         the calibration values
     * @param canIntegersBeInStringForm whether the {@link Integer}s in the calibration value can be in {@link String} for ("one", "two", etc.)
     * @return the sum of the calibration values
     */
    public static long sumAllCalibrationValues(final Collection<String> calibrationValues, final boolean canIntegersBeInStringForm) {
        final Pattern pattern = canIntegersBeInStringForm ? NUMBER_AS_WORDS_PATTERN : NUMBER_PATTERN;

        return calibrationValues
            .stream()
            .map(line -> collectIntegersInOrder(line, pattern))
            .map(integers -> String.valueOf(integers.getFirst()) + integers.getLast())
            .mapToLong(Long::parseLong)
            .sum();
    }

    private static List<Integer> collectIntegersInOrder(final String input, final Pattern pattern) {
        final List<Integer> result = new ArrayList<>();
        final int length = input.length();

        for (int i = 0; i < length; i++) {
            final Matcher matcher = pattern.matcher(input.substring(i));

            if (matcher.find()) {
                result.add(convert(matcher.group()));
            }
        }

        return result;
    }

    private static int convert(final String value) {
        return switch (value) {
            case "one" -> 1;
            case "two" -> 2;
            case "three" -> 3;
            case "four" -> 4;
            case "five" -> 5;
            case "six" -> 6;
            case "seven" -> 7;
            case "eight" -> 8;
            case "nine" -> 9;

            default -> Integer.parseInt(value);
        };
    }
}
