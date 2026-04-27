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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import net.zodac.advent.input.InputReader;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day19}.
 */
class Day19Test {

    private static final String INPUT_FILENAME = "day19.txt";
    private static final Pattern COMMA_AND_SPACE_DELIMITER = Pattern.compile(", ");

    @Test
    void example() {
        final List<List<String>> valuesRaw = InputReader
            .forExample(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank);

        final List<String> keys = parseKeys(valuesRaw);
        final List<String> values = valuesRaw.getLast();

        final long part1Result = Day19.countPossibleDesigns(keys, values);
        assertThat(part1Result)
            .isEqualTo(6L);

        final long part2Result = Day19.countAllCombinationsOfValidDesigns(keys, values);
        assertThat(part2Result)
            .isEqualTo(16L);
    }

    @Test
    void part1() {
        final List<List<String>> valuesRaw = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank);

        final List<String> keys = parseKeys(valuesRaw);
        final List<String> values = valuesRaw.getLast();

        final long part1Result = Day19.countPossibleDesigns(keys, values);
        assertThat(part1Result)
            .isEqualTo(263L);
    }

    @Test
    void part2() {
        final List<List<String>> valuesRaw = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .grouped()
            .byDelimiter(String::isBlank);

        final List<String> keys = parseKeys(valuesRaw);
        final List<String> values = valuesRaw.getLast();

        final long part2Result = Day19.countAllCombinationsOfValidDesigns(keys, values);
        assertThat(part2Result)
            .isEqualTo(723_524_534_506_343L);
    }

    // Sort in reverse order of size (largest first), to make it easier to replace values
    private static List<String> parseKeys(final List<? extends List<String>> valuesRaw) {
        final String[] keys = COMMA_AND_SPACE_DELIMITER.split(valuesRaw.getFirst().getFirst());
        return new ArrayList<>(Arrays.asList(keys)).stream().sorted(Comparator.comparingInt(String::length).reversed()).toList();
    }
}
