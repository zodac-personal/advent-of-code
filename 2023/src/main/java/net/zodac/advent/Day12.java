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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.zodac.advent.pojo.HotSpringReport;

/**
 * Solution for 2023, Day 12.
 *
 * @see <a href="https://adventofcode.com/2023/day/12">[2023: 12] Hot Springs</a>
 */
public final class Day12 {

    private Day12() {

    }

    /**
     * Given a {@link Collection} of {@link String}s representing {@link HotSpringReport}s, we parse and expand them
     * {@code numberOfTimesToRepeatRecord} times using {@link HotSpringReport#parse(CharSequence, int)}.
     *
     * <p>
     * Each {@link HotSpringReport} will have some operational and damaged hot springs (defined by {@link HotSpringReport#isDamagedHotSpring(char)}
     * and {@link HotSpringReport#isOperationalHotSpring(char)}). There are also some hot springs in an unknown state (if it is neither of the
     * previous options), which we need to evaluate. Each {@link HotSpringReport} comes with a {@link HotSpringReport#frequency()} of expected
     * operational hot springs. We must check each unknown hot spring to see if it is damaged or operational, and then count the number of possible
     * arrangements.
     *
     * <p>
     * We then sum the possible arrangements for each {@link HotSpringReport} together.
     *
     * @param hotSpringReports            the input {@link HotSpringReport}s as {@link String}s
     * @param numberOfTimesToRepeatRecord the number of times we should expand the {@link HotSpringReport} while parsing
     * @return the number of possible arrangements for all {@link HotSpringReport}s
     */
    public static long countPossibleArrangements(final Collection<String> hotSpringReports, final int numberOfTimesToRepeatRecord) {
        return hotSpringReports
            .stream()
            .map(input -> HotSpringReport.parse(input, numberOfTimesToRepeatRecord))
            .mapToLong(hotSpringReport -> countArrangements(hotSpringReport, 0, 0, 0L, new HashMap<>()))
            .sum();
    }

    private static long countArrangements(final HotSpringReport hotSpringReport,
                                          final int conditionRecordPointer,
                                          final int frequencyPointer,
                                          final long frequencyOfCurrentCharacter,
                                          final Map<? super String, Long> cache) {
        // Check cache for existing entry
        final String key = buildKey(conditionRecordPointer, frequencyPointer, frequencyOfCurrentCharacter);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // Finished checking the condition record
        if (conditionRecordPointer == hotSpringReport.condition().length()) {
            // Not valid to check
            if (frequencyPointer < hotSpringReport.frequency().size() - 1) {
                return 0L;
            }

            // Last frequency value, check if it matches current character's frequency
            if (frequencyPointer == hotSpringReport.frequency().size() - 1) {
                return hotSpringReport.frequency().get(frequencyPointer) == frequencyOfCurrentCharacter ? 1L : 0L;
            }

            // No more frequencies to check, and no trailing character
            return frequencyPointer == hotSpringReport.frequency().size() && frequencyOfCurrentCharacter == 0 ? 1L : 0L;
        }

        long totalForConditionRecord = 0;
        final char currentCharacter = hotSpringReport.condition().charAt(conditionRecordPointer);
        final int nextConditionRecordPointer = conditionRecordPointer + 1;

        if (!HotSpringReport.isOperationalHotSpring(currentCharacter)) {
            final long incrementedFrequency = frequencyOfCurrentCharacter + 1;
            totalForConditionRecord += countArrangements(hotSpringReport, nextConditionRecordPointer, frequencyPointer, incrementedFrequency, cache);
        }

        if (!HotSpringReport.isDamagedHotSpring(currentCharacter)) {
            if (isNewCharacter(frequencyOfCurrentCharacter)) {
                totalForConditionRecord += countArrangements(hotSpringReport, nextConditionRecordPointer, frequencyPointer, 0L, cache);
            }

            if (isExpectedFrequency(hotSpringReport, frequencyPointer, frequencyOfCurrentCharacter)) {
                final int nextFrequencyPointer = frequencyPointer + 1;
                totalForConditionRecord += countArrangements(hotSpringReport, nextConditionRecordPointer, nextFrequencyPointer, 0L, cache);
            }
        }

        // Update cache
        cache.put(key, totalForConditionRecord);
        return totalForConditionRecord;
    }

    private static boolean isExpectedFrequency(final HotSpringReport hotSpringReport, final int frequencyPointer, final long frequencyOfCharacter) {
        return frequencyPointer < hotSpringReport.frequency().size() && hotSpringReport.frequency().get(frequencyPointer) == frequencyOfCharacter;
    }

    private static String buildKey(final int conditionRecordPointer, final int frequencyPointer, final long frequencyOfCurrentCharacter) {
        return String.format("%d_%d_%d", conditionRecordPointer, frequencyPointer, frequencyOfCurrentCharacter);
    }

    private static boolean isNewCharacter(final long frequencyOfCurrentCharacter) {
        return frequencyOfCurrentCharacter == 0L;
    }
}
