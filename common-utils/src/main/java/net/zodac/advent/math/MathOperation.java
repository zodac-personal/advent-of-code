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
 * Enum defining different mathematical operations that can be performed.
 */
public enum MathOperation {

    /**
     * An addition operation:
     * <pre>
     *     a + b
     * </pre>
     */
    ADD("+", Long::sum),

    /**
     * A concatenation operation:
     * <pre>
     *     "a" + "b"
     * </pre>
     */
    CONCATENATE("||", (first, second) -> Long.parseLong(String.valueOf(first) + second)),

    /**
     * A division operation:
     * <pre>
     *     a / b
     * </pre>
     */
    DIVIDE("/", (first, second) -> first / second),

    /**
     * A subtraction operation:
     * <pre>
     *     a - b
     * </pre>
     */
    MINUS("-", (first, second) -> first - second),

    /**
     * A multiplication operation:
     * <pre>
     *     a * b
     * </pre>
     */
    MULTIPLY("*", (first, second) -> first * second),

    /**
     * A power operation:
     * <pre>
     *     a^b
     * </pre>
     */
    POWER("^", (first, second) -> Math.round(StrictMath.pow(first, second))),

    /**
     * A remainder operation:
     * <pre>
     *     a % b
     * </pre>
     */
    REMAINDER("%", (first, second) -> first % second),

    /**
     * An invalid operation.
     */
    INVALID("", (first, _) -> first);

    private static final Collection<MathOperation> ALL_VALUES = Stream.of(values())
        .filter(mathOperation -> mathOperation != INVALID)
        .toList();

    private final String symbol;
    private final BiFunction<? super Long, ? super Long, Long> function;

    MathOperation(final String symbol, final BiFunction<? super Long, ? super Long, Long> function) {
        this.symbol = symbol;
        this.function = function;
    }

    /**
     * Calculates the value of the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the {@link MathOperation} result
     */
    public long calculate(final long t, final long u) {
        return function.apply(t, u);
    }

    /**
     * Retrieve a {@link MathOperation} based on if the input matches the {@link MathOperation#name()}.
     *
     * @param input the potential {@link MathOperation}
     * @return the matching {@link MathOperation}, or {@link MathOperation#INVALID} if none is found
     */
    public static MathOperation getByName(final String input) {
        return ALL_VALUES
            .stream()
            .filter(mathOperation -> mathOperation.name().equalsIgnoreCase(String.valueOf(input)))
            .findAny()
            .orElse(INVALID);
    }

    /**
     * Retrieve a {@link MathOperation} based on if the input matches the {@link MathOperation#symbol}.
     *
     * @param input the potential {@link MathOperation}
     * @return the matching {@link MathOperation}, or {@link MathOperation#INVALID} if none is found
     */
    public static MathOperation getBySymbol(final char input) {
        return ALL_VALUES
            .stream()
            .filter(mathOperation -> mathOperation.symbol.equals(String.valueOf(input)))
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
