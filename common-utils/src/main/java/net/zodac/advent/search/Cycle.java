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

package net.zodac.advent.search;

import java.util.Optional;

/**
 * Class that holds the result of a {@link CycleFinder} invocation.
 *
 * @param finalCycleValue the final value at the end of the cycle
 * @param cycleSize       the size of the cycle/loop
 * @param <T>             the type of the final value
 */
public record Cycle<T>(Optional<T> finalCycleValue, int cycleSize) {

    /**
     * A cycle was found, and the {@code finalCycleValue} and {@code cycleSize} are populated.
     *
     * @param finalCycleValue the final value at the end of the cycle
     * @param cycleSize       the size of the cycle/loop
     * @param <T>             the type of the {@code cycleValue}
     * @return the successful {@link Cycle}
     */
    public static <T> Cycle<T> cycleFound(final T finalCycleValue, final int cycleSize) {
        return new Cycle<>(Optional.of(finalCycleValue), cycleSize);
    }

    /**
     * No cycle was found, and the {@code finalCycleValue} and {@code cycleSize} are no populated.
     *
     * @param <T> the type of the {@code cycleValue}
     * @return the unsuccessful {@link Cycle}
     */
    public static <T> Cycle<T> noCycle() {
        return new Cycle<>(Optional.empty(), 0);
    }

    /**
     * Checks if the {@link Cycle} was successful and whether a cycle exists.
     *
     * @return {@code true} if a cycle exists and a {@link #cycleValue()} can be returned
     */
    public boolean doesCycleExist() {
        return finalCycleValue.isPresent();
    }

    /**
     * Unwraps the {@link #finalCycleValue()} and returns the value itself. Should only be checked after {@link #doesCycleExist()}.
     *
     * @return the final cycle value
     */
    public T cycleValue() {
        return finalCycleValue.orElseThrow(() -> new IllegalStateException("No cycle found"));
    }
}
