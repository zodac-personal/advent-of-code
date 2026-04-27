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

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.zodac.advent.util.NumberUtils;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2015, Day 12.
 *
 * @see <a href="https://adventofcode.com/2015/day/12">AoC 2015, Day 12</a>
 */
public final class Day12 {

    private static final Pattern JSON_ELEMENT_PATTERN = Pattern.compile("(\\{[-\\w,:\"]*})|(\\[[-\\w,\"]*])");
    private static final Pattern JSON_VALUE_PATTERN = Pattern.compile("\\{[-\\w,:\"]*}");

    private Day12() {

    }

    /**
     * Takes the input {@link String}, finds all {@link Integer} values and sums them up.
     *
     * @param input the input to be parsed
     * @return the sum of all numbers
     */
    public static long sumOfAllNumbers(final CharSequence input) {
        return StringUtils.collectNumbersInOrder(input)
            .stream()
            .mapToLong(l -> l)
            .sum();
    }

    /**
     * Takes the input {@link String}, finds all {@link Integer} values and sums them up. The input will be a JSON {@link String}, and if any of the
     * JSON elements have a value matching the {@code invalidLabel} then that JSON element and all of its children will not be included.
     *
     * <p>
     * Non-JSON solution modified from <a href="https://www.reddit.com/r/adventofcode/comments/3wh73d/day_12_solutions/cxwr7q0/">reddit</a>.
     *
     * @param input             the input to be parsed
     * @param invalidLabelValue the value signifying an invalid JSON element
     * @return the sum of all valid numbers
     */
    public static long sumOfAllNumbersWithNoInvalidLabel(final String input, final String invalidLabelValue) {
        final String invalidLabel = String.format(".*:\"%s\".*", invalidLabelValue);
        String output = input;
        Matcher jsonMatcher = JSON_ELEMENT_PATTERN.matcher(output);

        while (jsonMatcher.find()) {
            // Process JSON subString
            final int sumOfSubString = calculateSumOfSubString(invalidLabel, jsonMatcher);

            // Strip the String of processed JSON, update matcher to use remainder of the String while including the sum
            output = output.substring(0, jsonMatcher.start()) + sumOfSubString + output.substring(jsonMatcher.end());
            jsonMatcher = JSON_ELEMENT_PATTERN.matcher(output);
        }

        return Long.parseLong(output);
    }

    private static int calculateSumOfSubString(final String invalidLabel, final MatchResult jsonMatcher) {
        int sumOfSubString = 0;
        if (isValidJsonAndDoesNotIncludeInvalidLabel(invalidLabel, jsonMatcher)) {
            final Matcher integerMatcher = StringUtils.NUMBER_PATTERN.matcher(jsonMatcher.group(0));
            while (integerMatcher.find()) {
                final String value = integerMatcher.group(0);

                if (NumberUtils.isInteger(value)) {
                    sumOfSubString += Integer.parseInt(value);
                }
            }
        }
        return sumOfSubString;
    }

    private static boolean isValidJsonAndDoesNotIncludeInvalidLabel(final String invalidLabel, final MatchResult jsonMatcher) {
        return !(JSON_VALUE_PATTERN.matcher(jsonMatcher.group(0)).matches() && jsonMatcher.group(1).matches(invalidLabel));
    }
}
