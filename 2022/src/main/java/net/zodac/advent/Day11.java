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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.zodac.advent.pojo.Monkey;
import net.zodac.advent.util.MathUtils;

/**
 * Solution for 2022, Day 11.
 *
 * @see <a href="https://adventofcode.com/2022/day/11">AoC 2022, Day 11</a>
 */
public final class Day11 {

    private static final int NUMBER_OF_MONKEYS_TO_CHECK = 2;

    private Day11() {

    }

    /**
     * We are provided some {@link Monkey}s with their own items, and each item is assigned a 'worry' level. For each round, every {@link Monkey} will
     * inspect their items and the 'worry' level is increased according to {@link Monkey#throwItemsToOtherMonkeys(long)}. Once inspected, the
     * {@link Monkey} will throw the updated item to another {@link Monkey}.
     *
     * <p>
     * After {@code rounds} have completed, we take the number of items inspected by each {@link Monkey}, and considered the
     * {@value #NUMBER_OF_MONKEYS_TO_CHECK} {@link Monkey}s that are most active. We return the product of their {@link Monkey#numberOfInspections()}.
     *
     * @param monkeysById the input {@link Monkey}s keyed by their ID
     * @param rounds      the number of rounds the {@link Monkey}s will throw items
     * @return the product of the {@link Monkey#numberOfInspections()} of the {@value #NUMBER_OF_MONKEYS_TO_CHECK} most active {@link Monkey}s
     */
    public static long productOfActiveMonkeys(final Map<Integer, Monkey> monkeysById, final int rounds) {
        final List<Long> monkeyDivisors = monkeysById
            .values()
            .stream()
            .map(Monkey::divisorTest)
            .toList();
        final long lcmOfAllMonkeyDivisors = MathUtils.lowestCommonMultiple(monkeyDivisors);

        for (int i = 0; i < rounds; i++) {
            throwItemsForRound(monkeysById, lcmOfAllMonkeyDivisors);
        }

        return monkeysById
            .values()
            .stream()
            .map(Monkey::numberOfInspections)
            .sorted(Comparator.reverseOrder())
            .limit(NUMBER_OF_MONKEYS_TO_CHECK)
            .reduce(1L, (first, second) -> first * second);
    }

    private static void throwItemsForRound(final Map<Integer, Monkey> monkeysById, final long productOfAllMonkeyDivisors) {
        for (final Monkey monkey : monkeysById.values()) {
            throwItemsForMonkey(monkeysById, productOfAllMonkeyDivisors, monkey);
        }
    }

    private static void throwItemsForMonkey(final Map<Integer, Monkey> monkeysById, final long productOfAllMonkeyDivisors, final Monkey monkey) {
        final Map<Integer, List<Long>> thrownItemsByTargetMonkeyId = monkey.throwItemsToOtherMonkeys(productOfAllMonkeyDivisors);

        for (final Map.Entry<Integer, List<Long>> entry : thrownItemsByTargetMonkeyId.entrySet()) {
            final int targetMonkeyId = entry.getKey();
            final Monkey targetMonkey = monkeysById.get(targetMonkeyId);

            if (targetMonkey == null) {
                throw new IllegalStateException(
                    String.format("Unable to find target %s with ID '%s' from map: %s", Monkey.class.getSimpleName(), targetMonkeyId, monkeysById));
            }

            for (final long thrownItem : entry.getValue()) {
                targetMonkey.addItem(thrownItem);
            }
        }
    }
}
