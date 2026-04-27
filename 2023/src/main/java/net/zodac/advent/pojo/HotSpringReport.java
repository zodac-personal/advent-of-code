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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.zodac.advent.util.StringUtils;

/**
 * Record defining a report of the condition of hot springs, and their frequency.
 *
 * @param condition the condition of the location
 * @param frequency the expected frequency of hot springs within the condition
 */
public record HotSpringReport(String condition, List<Long> frequency) {

    private static final char DAMAGED_HOT_SPRING_SYMBOL = '#';
    private static final char OPERATIONAL_HOT_SPRING_SYMBOL = '.';
    private static final char CONDITION_RECORD_REPEAT_DELIMITER = '?';
    private static final char HOT_SPRING_FREQUENCY_REPEAT_DELIMITER = ',';

    private static final Pattern CONDITION_REPORT_PATTERN = Pattern.compile("([.?#]+)\\s([\\d+,]+)");

    /**
     * Creates a {@link HotSpringReport} from a {@link CharSequence} in the format:
     * <pre>
     *     [condition] [frequency].
     * </pre>
     *
     * @param input       the {@link CharSequence} to parse
     * @param withRepeats the number of times to repeat the {@code condition} and {@code frequency} to expand the {@link HotSpringReport}
     * @return the {@link HotSpringReport}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static HotSpringReport parse(final CharSequence input, final int withRepeats) {
        final Matcher matcher = CONDITION_REPORT_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + input);
        }

        final int initialSize = withRepeats << 1; // 2 characters per withRepeats iteration
        final StringBuilder first = new StringBuilder(initialSize);
        final StringBuilder second = new StringBuilder(initialSize);

        for (int i = 0; i < withRepeats; i++) {
            first.append(matcher.group(1)).append(CONDITION_RECORD_REPEAT_DELIMITER);
            second.append(matcher.group(2)).append(HOT_SPRING_FREQUENCY_REPEAT_DELIMITER);
        }
        first.deleteCharAt(first.length() - 1);

        final List<Long> hotSpringFrequency = StringUtils.collectNumbersInOrder(second);
        return new HotSpringReport(first.toString(), hotSpringFrequency);
    }

    /**
     * Checks if the provided {@link Character} signifies a damaged hot spring.
     *
     * @param input the {@link Character} to check
     * @return {@code true} if the input equals {@link #DAMAGED_HOT_SPRING_SYMBOL}
     */
    public static boolean isDamagedHotSpring(final char input) {
        return input == DAMAGED_HOT_SPRING_SYMBOL;
    }

    /**
     * Checks if the provided {@link Character} signifies an operational hot spring.
     *
     * @param input the {@link Character} to check
     * @return {@code true} if the input equals {@link #OPERATIONAL_HOT_SPRING_SYMBOL}
     */
    public static boolean isOperationalHotSpring(final char input) {
        return input == OPERATIONAL_HOT_SPRING_SYMBOL;
    }
}
