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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.zodac.advent.input.InputReader;
import net.zodac.advent.pojo.StackInstruction;
import net.zodac.advent.util.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day05}.
 */
class Day05Test {

    private static final String INPUT_FILENAME = "day05.txt";
    private static final int INDEX_OF_SECOND_GROUP = 1;

    @Test
    void example() {
        final List<List<String>> values = InputReader
            .forExample(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isEmpty);

        final Map<Integer, Deque<String>> stacksById1 = getStacksById(values);
        final List<StackInstruction> stackInstructions1 = getStackInstructions(values);
        final String part1Result = Day05.moveElementsBetweenStacksLastInFirstOutOrderAndCreateCode(stacksById1, stackInstructions1);
        assertThat(part1Result)
            .isEqualTo("CMZ");

        // Need to create a fresh map since each Deque is changed
        final Map<Integer, Deque<String>> stacksById2 = getStacksById(values);
        final List<StackInstruction> stackInstructions2 = getStackInstructions(values);
        final String part2Result = Day05.moveElementsBetweenStacksRetainingOrderAndCreateCode(stacksById2, stackInstructions2);
        assertThat(part2Result)
            .isEqualTo("MCD");
    }

    @Test
    void part1() {
        final List<List<String>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isEmpty);

        final Map<Integer, Deque<String>> stacksById = getStacksById(values);
        final List<StackInstruction> stackInstructions = getStackInstructions(values);

        final String part1Result = Day05.moveElementsBetweenStacksLastInFirstOutOrderAndCreateCode(stacksById, stackInstructions);
        assertThat(part1Result)
            .isEqualTo("RFFFWBPNS");
    }

    @Test
    void part2() {
        final List<List<String>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isEmpty);

        final Map<Integer, Deque<String>> stacksById = getStacksById(values);
        final List<StackInstruction> stackInstructions = getStackInstructions(values);

        final String part2Result = Day05.moveElementsBetweenStacksRetainingOrderAndCreateCode(stacksById, stackInstructions);
        assertThat(part2Result)
            .isEqualTo("CQQBBJFCS");
    }

    private static Map<Integer, Deque<String>> getStacksById(final List<? extends List<String>> values) {
        final List<String> input = values.getFirst();
        final Character[][] arrayOfCharArrays = ArrayUtils.toArrayOfArrays(input, (character -> character));
        final Character[][] reversedArrayOfCharArrays = ArrayUtils.reverseRows(arrayOfCharArrays);
        final Character[][] transposedArrayOfCharArrays = ArrayUtils.transpose(reversedArrayOfCharArrays, ' ');

        // Filter and add to Map, keyed by ID
        final Map<Integer, Deque<String>> stacksById = new HashMap<>();
        for (final Character[] transposedCharArray : transposedArrayOfCharArrays) {
            final char firstChar = transposedCharArray[0];

            if (Character.isDigit(firstChar)) {
                final Deque<String> stack = createStack(transposedCharArray);
                stacksById.put(Character.getNumericValue(firstChar), stack);
            }
        }
        return stacksById;
    }

    private static Deque<String> createStack(final Character[] transposedCharArray) {
        final Deque<String> stack = new ArrayDeque<>();

        // Skip first element as that is the stack ID
        for (int i = 1; i < transposedCharArray.length; i++) {
            final char c = transposedCharArray[i];
            if (Character.isAlphabetic(c)) {
                stack.push(String.valueOf(c));
            }
        }
        return stack;
    }

    private static List<StackInstruction> getStackInstructions(final List<? extends List<String>> values) {
        return values
            .get(INDEX_OF_SECOND_GROUP)
            .stream()
            .map(StackInstruction::parse)
            .toList();
    }
}
