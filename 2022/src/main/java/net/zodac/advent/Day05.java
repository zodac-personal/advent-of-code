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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Map;
import java.util.function.Supplier;
import net.zodac.advent.pojo.StackInstruction;

/**
 * Solution for 2022, Day 5.
 *
 * @see <a href="https://adventofcode.com/2022/day/5">AoC 2022, Day 5</a>
 */
public final class Day05 {

    // In case an expected stack is not returned, we want to provide a new, empty stack
    // Instead of making a single empty stack, or using 'new' directly, we'll create this supplier and call #get() when we need a new one
    private static final Supplier<Deque<String>> NEW_STACK_SUPPLIER = ArrayDeque::new;

    private Day05() {

    }

    /**
     * Moves elements between stacks according to the provided {@link StackInstruction}s. The stacks are provided as a {@link Map} where the key is
     * the ID of the stack. When multiple elements are moved, they are moved one-by-one in standard 'Last-In, First-Out' order. For example, given the
     * stacks {@code 1, 2 and 3}:
     *
     * <pre>
     *     1    2    3
     *    [A]  [B]  [C]
     *    [D]  [G]  [F]
     *    [E]       [H]
     *              [I]
     * </pre>
     *
     * <p>
     * Given the {@link StackInstruction} to move <b>3</b> elements from {@code 3} to {@code 1}, our result is:
     * <pre>
     *     1    2    3
     *    [A]  [B]  [C]
     *    [D]  [G]
     *    [E]
     *    [I]
     *    [H]
     *    [F]
     * </pre>
     *
     * <p>
     * When all {@link StackInstruction}s have been applied, we take the top {@link String} from each stack (in ascending order of IDs), then combine
     * them into a new {@link String} code that is returned.
     *
     * @param stacksById        the {@link Deque} stacked, keyed by ID
     * @param stackInstructions the {@link StackInstruction}s to be applied
     * @return the final constructed {@link String} code
     */
    public static String moveElementsBetweenStacksLastInFirstOutOrderAndCreateCode(final Map<Integer, Deque<String>> stacksById,
                                                                                   final Collection<StackInstruction> stackInstructions) {
        return moveElementsBetweenStacks(stacksById, stackInstructions, false);
    }

    /**
     * Moves elements between stacks according to the provided {@link StackInstruction}s. The stacks are provided as a {@link Map} where the key is
     * the ID of the stack. When multiple elements are moved, they are moved as a whole, retaining their order from their original stack. For example,
     * given the stacks {@code 1, 2 and 3}:
     *
     * <pre>
     *     1    2    3
     *    [A]  [B]  [C]
     *    [D]  [G]  [F]
     *    [E]       [H]
     *              [I]
     * </pre>
     *
     * <p>
     * Given the {@link StackInstruction} to move <b>3</b> elements from {@code 3} to {@code 1}, our result is:
     * <pre>
     *     1    2    3
     *    [A]  [B]  [C]
     *    [D]  [G]
     *    [E]
     *    [F]
     *    [H]
     *    [I]
     * </pre>
     *
     * <p>
     * When all {@link StackInstruction}s have been applied, we take the top {@link String} from each stack (in ascending order of IDs), then combine
     * them into a new {@link String} code that is returned.
     *
     * @param stacksById        the {@link Deque} stacked, keyed by ID
     * @param stackInstructions the {@link StackInstruction}s to be applied
     * @return the final constructed {@link String} code
     */
    public static String moveElementsBetweenStacksRetainingOrderAndCreateCode(final Map<Integer, Deque<String>> stacksById,
                                                                              final Collection<StackInstruction> stackInstructions) {
        return moveElementsBetweenStacks(stacksById, stackInstructions, true);
    }

    private static String moveElementsBetweenStacks(final Map<Integer, Deque<String>> stacksById,
                                                    final Collection<StackInstruction> stackInstructions,
                                                    final boolean retainOrderOfMultipleElements
    ) {
        for (final StackInstruction stackInstruction : stackInstructions) {
            final Deque<String> src = stacksById.getOrDefault(stackInstruction.sourceStackId(), NEW_STACK_SUPPLIER.get());
            final Deque<String> des = stacksById.getOrDefault(stackInstruction.destinationStackId(), NEW_STACK_SUPPLIER.get());
            final int numberOfElementsToMove = stackInstruction.numberOfElementsToMove();

            if (retainOrderOfMultipleElements) {
                final Deque<String> buffer = NEW_STACK_SUPPLIER.get();
                for (int i = 0; i < numberOfElementsToMove; i++) {
                    buffer.push(src.pop());
                }

                for (int i = 0; i < numberOfElementsToMove; i++) {
                    des.push(buffer.pop());
                }
            } else {
                for (int i = 0; i < numberOfElementsToMove; i++) {
                    des.push(src.pop());
                }
            }
        }

        return buildStringFromFirstElements(stacksById);
    }

    private static String buildStringFromFirstElements(final Map<Integer, Deque<String>> stacksById) {
        final StringBuilder stringBuilder = new StringBuilder();
        final int numberOfStacks = stacksById.size();

        for (int i = 1; i <= numberOfStacks; i++) {
            final Deque<String> stack = stacksById.getOrDefault(i, NEW_STACK_SUPPLIER.get());
            stringBuilder.append(stack.pop());
        }

        return stringBuilder.toString();
    }
}
