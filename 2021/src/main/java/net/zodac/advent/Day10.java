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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.zodac.advent.pojo.SyntaxLine;
import net.zodac.advent.pojo.tuple.Pair;
import net.zodac.advent.util.CollectionUtils;

/**
 * Solution for 2021, Day 10.
 *
 * @see <a href="https://adventofcode.com/2021/day/10">AoC 2021, Day 10</a>
 */
public final class Day10 {

    private static final int SCORE_MULTIPLIER = 5;

    private static final Map<Character, Integer> SYNTAX_ERROR_SCORE_FOR_SYMBOL = Map.of(
        ')', 3,
        ']', 57,
        '}', 1_197,
        '>', 25_137
    );

    private static final Map<Character, Integer> COMPLETION_SCORE_FOR_SYMBOL = Map.of(
        ')', 1,
        ']', 2,
        '}', 3,
        '>', 4
    );

    private Day10() {

    }

    /**
     * For the given {@link List} of {@link SyntaxLine}s, evaluate them using {@link SyntaxLine#evaluateSyntaxErrorScoreForLine()}. This will return
     * the first error symbol on the line, which can then be mapped to {@link #SYNTAX_ERROR_SCORE_FOR_SYMBOL}. Sum these up for each
     * {@link SyntaxLine} to calculate the total syntax error score.
     *
     * <p>
     * Note that any valid {@link SyntaxLine} will not return a valid key to use for {@link #SYNTAX_ERROR_SCORE_FOR_SYMBOL}, so we default to
     * <b>0</b>, which allows us to sum all values safely.
     *
     * @param syntaxLines the {@link SyntaxLine}s whose syntax error scores are to be calculated
     * @return the syntax error score for all {@link SyntaxLine}s
     * @see SyntaxLine#evaluateSyntaxErrorScoreForLine()
     */
    public static long calculateSyntaxErrorScoreForLines(final Collection<SyntaxLine> syntaxLines) {
        return syntaxLines
            .stream()
            .map(SyntaxLine::evaluateSyntaxErrorScoreForLine)
            .map(Pair::first)
            .mapToInt(errorSymbol -> SYNTAX_ERROR_SCORE_FOR_SYMBOL.getOrDefault(errorSymbol, 0))
            .sum();

    }

    /**
     * For the given {@link List} of {@link SyntaxLine}s, evaluate them using {@link SyntaxLine#evaluateSyntaxErrorScoreForLine()}. We can then filter
     * this to only include evaluated {@link SyntaxLine}s that have some remaining open symbols, as an invalid {@link SyntaxLine} would return an
     * empty {@link Deque}, as would a valid but completed {@link SyntaxLine}.
     *
     * <p>
     * We iterate over the remaining symbols (in reverse, so we get the most recent open symbol first), find their matching close symbol, and
     * calculate the completion score for the {@link SyntaxLine}.
     *
     * <p>
     * Once we have all completion scores, we sort the {@link List} and retrieve the middle index for the final score.
     *
     * @param syntaxLines the {@link SyntaxLine}s whose middle completion score is to be found
     * @return the value of the middle index for the completion scores for the valid {@link SyntaxLine}s
     * @see SyntaxLine#evaluateSyntaxErrorScoreForLine()
     * @see CollectionUtils#getMiddleSortedValue(List)
     */
    public static long calculateMiddleScoreForIncompleteLines(final Collection<SyntaxLine> syntaxLines) {
        final List<Deque<Character>> openSymbolsForIncompleteLines = syntaxLines
            .stream()
            .map(SyntaxLine::evaluateSyntaxErrorScoreForLine)
            .filter(evaluatedSyntaxLine -> !evaluatedSyntaxLine.second().isEmpty())
            .map(Pair::second)
            .toList();

        final List<Long> scores = new ArrayList<>(openSymbolsForIncompleteLines.size());
        for (final Deque<Character> remainingOpenSymbolsForLine : openSymbolsForIncompleteLines) {
            long score = 0L;

            // Iterate over the remaining open symbols to find their required close symbol
            for (final char remainingOpenSymbol : remainingOpenSymbolsForLine) {
                final char requiredCloseSymbol = SyntaxLine.getMatchingCloseSymbol(remainingOpenSymbol);
                score = calculateNewCompletionScore(score, requiredCloseSymbol);
            }
            scores.add(score);
        }

        return CollectionUtils.getMiddleSortedValue(scores);
    }

    private static long calculateNewCompletionScore(final long currentScore, final char closeSymbol) {
        // Value will be non-null since we define a static map, but adding the check so the compiler knows too
        final int scoreForCloseSymbol = Objects.requireNonNull(COMPLETION_SCORE_FOR_SYMBOL.get(closeSymbol));
        return (currentScore * SCORE_MULTIPLIER) + scoreForCloseSymbol;
    }
}
