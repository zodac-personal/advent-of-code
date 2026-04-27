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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import net.zodac.advent.util.StringUtils;

/**
 * Simple class defining a signal with its input values and output values.
 *
 * @param inputs the input values
 * @param outputs the output values
 */
public record Signal(List<String> inputs, List<String> outputs) {

    private static final Pattern VALID_SIGNAL = Pattern.compile("([a-g]{2,7}\\s?){10}\\|(\\s?[a-g]{2,7}){4}");

    /**
     * Constructs a {@link Signal}, given an input {@link String} in the form:
     * <pre>
     * acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
     * </pre>
     *
     * <p>
     * We split at the delimiter and keep the inputs and outputs. Each input and output value will be ordered alphabetically.
     *
     * @param signalEntry the raw signal input
     * @return the created {@link Signal}
     * @throws IllegalArgumentException thrown if the input is invalid
     */
    public static Signal create(final String signalEntry) {
        if (!VALID_SIGNAL.matcher(signalEntry).matches()) {
            throw new IllegalArgumentException(String.format("Invalid input: '%s'", signalEntry));
        }

        final String[] inputTokens = signalEntry.split("\\|");

        final List<String> inputs = Arrays.stream(StringUtils.splitOnWhitespace(inputTokens[0]))
            .map(StringUtils::sort)
            .toList();

        final List<String> outputs = Arrays.stream(StringUtils.splitOnWhitespace(inputTokens[1]))
            .map(StringUtils::sort)
            .toList();

        return new Signal(inputs, outputs);
    }
}
