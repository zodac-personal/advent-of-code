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
import net.zodac.advent.util.StringUtils;

/**
 * POJO defining a multiplication operation, based on a {@link String} in the form:
 * <pre>
 *     mul(a,b)
 * </pre>
 *
 * @param input  the {@link String} input
 * @param first  the first multiplicand
 * @param second the second multiplicand
 */
public record MultiplicationOperation(String input, long first, long second) {

    /**
     * Parses the input {@link String} to create a {@link MultiplicationOperation}. Assumes the input will be in the form:
     * <pre>
     *     mul(a,b)
     * </pre>
     *
     * <p>
     * Where both <b>a</b> and <b>b</b> are 1-3 digit {@link Long}s. However, we simply extract all numbers using
     * {@link StringUtils#collectNumbersInOrder(CharSequence)}, meaning there is no validation of the inputs. We will use the first and last
     * {@link Long} found to construct the {@link MultiplicationOperation}.
     *
     * @param input the {@link String} to be parsed into a {@link MultiplicationOperation}
     * @return the created {@link MultiplicationOperation}
     */
    public static MultiplicationOperation parse(final String input) {
        final List<Long> numbers = StringUtils.collectNumbersInOrder(input);
        return new MultiplicationOperation(input, numbers.getFirst(), numbers.getLast());
    }

    /**
     * Multiplies the first and second multiplicands.
     *
     * @return the multiplied values
     */
    public long multiply() {
        return first * second;
    }
}
