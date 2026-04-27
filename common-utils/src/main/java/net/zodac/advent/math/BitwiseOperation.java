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

package net.zodac.advent.math;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Enum defining the available {@link Integer} bitwise operations.
 */
public enum BitwiseOperation {

    /**
     * Performs a bitwise <b>AND</b> on two provided values:
     * <pre>
     *     a {@literal &} b
     * </pre>
     */
    AND("&", (first, second) -> first & second),

    /**
     * Performs a bitwise <b>LEFT SHIFT</b> on two provided values (can be used to multiply by powers of 2):
     * <pre>
     *     a {@literal <<} b
     * </pre>
     */
    LSHIFT("<<", (first, second) -> first << second),

    /**
     * Performs a bitwise <b>NOT</b> on the single provided value:
     * <pre>
     *     {@literal ~}a
     * </pre>
     */
    NOT("~", (first, _) -> ~first),

    /**
     * Performs a bitwise <b>OR</b> on two provided values:
     * <pre>
     *     a {@literal |} b
     * </pre>
     */
    OR("|", (first, second) -> first | second),

    /**
     * Performs a bitwise <b>RIGHT SHIFT</b> on two provided values:
     * <pre>
     *     a {@literal >>} b
     * </pre>
     */
    RSHIFT(">>", (first, second) -> first >> second),

    /**
     * Performs a bitwise <b>XOR</b> on two provided values:
     * <pre>
     *     a {@literal ^} b
     * </pre>
     */
    XOR("^", (first, second) -> first ^ second),

    /**
     * An invalid operation.
     */
    INVALID("", (first, _) -> first);

    private static final Collection<BitwiseOperation> ALL_VALUES = Stream.of(values())
        .filter(bitwiseOperation -> bitwiseOperation != INVALID)
        .toList();

    private final String symbol;
    private final BiFunction<? super Long, ? super Long, Long> function;

    BitwiseOperation(final String symbol, final BiFunction<? super Long, ? super Long, Long> function) {
        this.symbol = symbol;
        this.function = function;
    }

    /**
     * Calculates the value of the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the {@link BitwiseOperation} result
     */
    public long calculate(final long t, final long u) {
        return function.apply(t, u);
    }

    /**
     * Retrieve a {@link BitwiseOperation} based on the input {@link String}. The search is case-insensitive.
     *
     * @param input the {@link BitwiseOperation} as a {@link String}
     * @return the matching {@link BitwiseOperation}, or {@link BitwiseOperation#INVALID} if none is found
     */
    public static BitwiseOperation get(final String input) {
        return ALL_VALUES
            .stream()
            .filter(bitwiseOperation -> bitwiseOperation.name().equalsIgnoreCase(input))
            .findAny()
            .orElse(INVALID);
    }

    /**
     * Replacing the default implementation to return the symbol. Can use {@link #name()} if enum name is required.
     *
     * @return the symbol
     */
    @Override
    public String toString() {
        return symbol;
    }
}
