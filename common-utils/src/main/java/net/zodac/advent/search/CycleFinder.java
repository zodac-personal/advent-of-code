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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import net.zodac.advent.util.CollectionUtils;

/**
 * Utility class used to perform an operation a set number of times, and determine if there is a cycle in the results.
 */
public final class CycleFinder {

    private static final int DEFAULT_CYCLE_START_INDEX = 1;

    private CycleFinder() {

    }

    /**
     * Given an {@code initialState}, we update this state {@code maximumIterations} number of times, using the provided {@code manipulator}
     * {@link Function}. If at any point we find this {@link Function} returns the same value as was seen previously, we assume a cycle has started.
     * We can then find the first instance of this value occurring, and from that we can derive the {@link Cycle}. We also perform a
     * 'jump-ahead' to determine the expected value at the end of the {@code maximumIterations}, which is also included in the {@link Cycle}.
     *
     * <p>
     * If no cycle is found by the end of {@code maximumIterations}, we end and return {@link Cycle#noCycle()}.
     *
     * @param initialState      the initial state before starting the cycle
     * @param manipulator       the {@link Function} to perform on the initial state (and all intermediate states)
     * @param maximumIterations the number of times to perform the {@link Function}
     * @param <T>               the type of the input
     * @return the {@link Cycle}
     */
    public static <T> Cycle<T> findCycle(final T initialState,
                                         final Function<? super T, ? extends T> manipulator,
                                         final int maximumIterations) {
        int cycleStartIndex = DEFAULT_CYCLE_START_INDEX;
        final Set<T> seen = new LinkedHashSet<>();
        T mutableInput = initialState;

        for (int i = 0; i < maximumIterations; i++) {
            mutableInput = manipulator.apply(mutableInput);

            if (!seen.add(mutableInput)) {
                cycleStartIndex = CollectionUtils.indexOf(seen, mutableInput);
                break;
            }
        }

        if (cycleStartIndex == DEFAULT_CYCLE_START_INDEX) {
            return Cycle.noCycle();
        }

        final int cycleSize = seen.size() - cycleStartIndex;
        final int index = (cycleStartIndex + ((maximumIterations - cycleStartIndex) % cycleSize)) - 1;
        final T finalCycleValue = new ArrayList<>(seen).get(index);
        return Cycle.cycleFound(finalCycleValue, cycleSize);
    }
}
