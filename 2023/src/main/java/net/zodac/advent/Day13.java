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
import java.util.List;
import java.util.SequencedCollection;
import net.zodac.advent.grid.GridFactory;
import net.zodac.advent.pojo.ReflectionResult;
import net.zodac.advent.pojo.ReflectionResultType;
import net.zodac.advent.pojo.tuple.Pair;
import net.zodac.advent.util.ArrayUtils;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2023, Day 13.
 *
 * @see <a href="https://adventofcode.com/2023/day/13">[2023: 13] Point of Incidence</a>
 */
public final class Day13 {

    private Day13() {

    }

    /**
     * Given a {@link List} of {@link String} {@code Pattern}s, we find the vertical or horizontal {@link ReflectionResult} within the
     * {@code pattern}. We then calculate the value of each {@code pattern} using {@link ReflectionResult#calculateValue()}, and then sum these values
     * together.
     *
     * @param patterns the input patterns
     * @return the sum of all {@link ReflectionResult} values
     */
    public static long calculateSumOfReflectionValues(final Collection<? extends List<String>> patterns) {
        return patterns
            .stream()
            .map(pattern -> getReflectionResult(pattern).second())
            .mapToLong(ReflectionResult::calculateValue)
            .sum();
    }

    /**
     * Similar to {@link #calculateSumOfReflectionValues(Collection)}, except this time we find the second {@link ReflectionResult}. Once we have the
     * first {@link ReflectionResult}, we then flip the value of each {@link Character} in the pattern. When we find a new {@link ReflectionResult},
     * we consider this to be the correct {@link ReflectionResult}.
     *
     * <p>
     * We assume each pattern has exactly two valid results.
     *
     * @param patterns the input values
     * @return the sum of all second {@link ReflectionResult} values
     */
    public static long calculateSumOfSecondReflectionValues(final Collection<? extends List<String>> patterns) {
        return patterns
            .stream()
            .map(Day13::getReflectionResult)
            .map(Day13::getUpdatedReflectionResult)
            .mapToLong(ReflectionResult::calculateValue)
            .sum();
    }

    private static Pair<List<String>, ReflectionResult> getReflectionResult(final List<String> pattern) {
        return Pair.of(pattern, getNewReflectionResult(pattern, ReflectionResult.none()));
    }

    private static ReflectionResult countColumnsLeftOfReflection(final SequencedCollection<String> pattern, final ReflectionResult previous) {
        final int firstPatternLength = pattern.getFirst().length();
        for (int i = 0; i < firstPatternLength - 1; i++) {
            if (matchesPreviousResult(previous, i, ReflectionResultType.COLUMN)) {
                continue;
            }

            if (isColumnReflectedAroundIndex(pattern, i)) {
                return ReflectionResult.vertical(i);
            }
        }

        return ReflectionResult.none();
    }

    private static boolean matchesPreviousResult(final ReflectionResult previous, final int index, final ReflectionResultType currentType) {
        return previous.hasReflection() && previous.reflectionResultType() == currentType && previous.index() == index;
    }

    private static boolean isColumnReflectedAroundIndex(final SequencedCollection<String> pattern, final int startColumnIndex) {
        final int firstPatternLength = pattern.getFirst().length();
        int previousColumnIndex = startColumnIndex;
        int nextColumnIndex = startColumnIndex + 1;

        while (previousColumnIndex >= 0 && nextColumnIndex < firstPatternLength) {
            final String currentColumn = StringUtils.buildColumn(pattern, previousColumnIndex);
            final String nextColumn = StringUtils.buildColumn(pattern, nextColumnIndex);

            if (!currentColumn.equals(nextColumn)) {
                return false;
            }
            nextColumnIndex++;
            previousColumnIndex--;
        }

        return true;
    }

    private static ReflectionResult getNewReflectionResult(final List<String> pattern, final ReflectionResult previousReflectionResult) {
        final ReflectionResult rows = countRowsAboveReflection(pattern, previousReflectionResult);
        if (rows.hasReflection()) {
            return rows;
        }

        final ReflectionResult columns = countColumnsLeftOfReflection(pattern, previousReflectionResult);
        if (columns.hasReflection()) {
            return columns;
        }

        return ReflectionResult.none();
    }

    private static ReflectionResult countRowsAboveReflection(final List<String> pattern, final ReflectionResult previous) {
        final int patternLength = pattern.size();
        for (int i = 0; i < patternLength - 1; i++) {
            if (matchesPreviousResult(previous, i, ReflectionResultType.ROW)) {
                continue;
            }

            if (isRowReflectedAroundIndex(pattern, i)) {
                return ReflectionResult.horizontal(i);
            }
        }

        return ReflectionResult.none();
    }

    private static boolean isRowReflectedAroundIndex(final List<String> pattern, final int startRowIndex) {
        final int patternSize = pattern.size();
        int previousRowIndex = startRowIndex;
        int nextRowIndex = startRowIndex + 1;

        while (previousRowIndex >= 0 && nextRowIndex < patternSize) {
            final String currentRow = pattern.get(previousRowIndex);
            final String nextRow = pattern.get(nextRowIndex);

            if (!currentRow.equals(nextRow)) {
                return false;
            }
            previousRowIndex--;
            nextRowIndex++;
        }

        return true;
    }

    private static ReflectionResult getUpdatedReflectionResult(final Pair<? extends List<String>, ReflectionResult> previous) {
        final Character[][] grid = GridFactory.ofCharacters(previous.first()).getInternalGrid();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                final List<String> newMap = flipCharacterAndReturnPattern(grid, i, j);

                final ReflectionResult newReflectionResult = getNewReflectionResult(newMap, previous.second());
                if (newReflectionResult.hasReflection()) {
                    return newReflectionResult;
                }
            }
        }

        return ReflectionResult.none();
    }

    private static List<String> flipCharacterAndReturnPattern(final Character[][] grid, final int row, final int column) {
        final Character[][] newGrid = ArrayUtils.deepCopy(grid);
        newGrid[row][column] = flipCharacter(newGrid[row][column]);
        return ArrayUtils.toListOfStrings(newGrid, String::valueOf);
    }

    private static char flipCharacter(final char c) {
        return c == '.' ? '#' : '.';
    }
}
