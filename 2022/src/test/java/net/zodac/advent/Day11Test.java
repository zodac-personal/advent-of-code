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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.zodac.advent.input.InputReader;
import net.zodac.advent.pojo.Monkey;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day11}.
 */
class Day11Test {

    private static final String INPUT_FILENAME = "day11.txt";

    @Test
    void example() {
        final List<List<String>> values = InputReader
            .forExample(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank);
        final Map<Integer, Monkey> monkeysById1 = parseMonkeys(values, true);

        final long part1Result = Day11.productOfActiveMonkeys(monkeysById1, 20);
        assertThat(part1Result)
            .isEqualTo(10_605L);

        final Map<Integer, Monkey> monkeysById2 = parseMonkeys(values, false);
        final long part2Result = Day11.productOfActiveMonkeys(monkeysById2, 10_000);
        assertThat(part2Result)
            .isEqualTo(2_713_310_158L);
    }

    @Test
    void part1() {
        final List<List<String>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank);
        final Map<Integer, Monkey> monkeysById = parseMonkeys(values, true);

        final long part1Result = Day11.productOfActiveMonkeys(monkeysById, 20);
        assertThat(part1Result)
            .isEqualTo(151_312L);
    }

    @Test
    void part2() {
        final List<List<String>> values = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank);
        final Map<Integer, Monkey> monkeysById = parseMonkeys(values, false);

        final long part2Result = Day11.productOfActiveMonkeys(monkeysById, 10_000);
        assertThat(part2Result)
            .isEqualTo(51_382_025_916L);
    }

    private static Map<Integer, Monkey> parseMonkeys(final List<? extends List<String>> values, final boolean worry) {
        final Map<Integer, Monkey> monkeyById = new HashMap<>(values.size());

        for (final List<String> monkeyValues : values) {
            final Monkey monkey = Monkey.parse(monkeyValues, worry);
            monkeyById.put(monkey.id(), monkey);
        }

        return monkeyById;
    }
}
