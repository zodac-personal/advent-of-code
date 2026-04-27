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
import java.util.Map;

/**
 * Solution for 2022, Day 2.
 *
 * @see <a href="https://adventofcode.com/2022/day/2">AoC 2022, Day 2</a>
 */
public final class Day02 {

    private static final Map<String, Integer> PART_1_VALUES = Map.of(
        "A X", 4,
        "A Y", 8,
        "A Z", 3,
        "B X", 1,
        "B Y", 5,
        "B Z", 9,
        "C X", 7,
        "C Y", 2,
        "C Z", 6
    );

    private static final Map<String, Integer> PART_2_VALUES = Map.of(
        "A X", 3,
        "A Y", 4,
        "A Z", 8,
        "B X", 1,
        "B Y", 5,
        "B Z", 9,
        "C X", 2,
        "C Y", 6,
        "C Z", 7
    );

    private Day02() {

    }

    /**
     * Each {@link String} value is a valid Rock-Paper-Scissors game, where the first move is your opponent move. We are provided a guide where the
     * second move is our move:
     * <pre>
     *     A and X = Rock
     *     B and Y = Paper
     *     C and Z = Scissors
     * </pre>
     *
     * <p>
     * The scores for each game are:
     * <pre>
     *     Win:  6pts
     *     Draw: 3pts
     *     Loss: 0pts
     * </pre>
     *
     * <p>
     * There are also points for each move:
     * <pre>
     *     Rock:     1pts
     *     Paper:    2pts
     *     Scissors: 3pts
     * </pre>
     *
     * @param values the rock-paper-scissors games
     * @return the final score following the guide with player moves
     */
    public static long finalScoreFollowingGuideWithMoves(final Collection<String> values) {
        return values
            .stream()
            .mapToInt(value -> PART_1_VALUES.getOrDefault(value, 0))
            .sum();
    }

    /**
     * Each {@link String} value is a valid Rock-Paper-Scissors game, where the first move is your opponent move. We are provided a guide where the
     * second value is the end result:
     * <pre>
     *     A = Rock
     *     B = Paper
     *     C = Scissors
     *
     *     X = Losing move
     *     Y = Drawing move
     *     Z = Winning move
     * </pre>
     *
     * <p>
     * The scores for each game are:
     * <pre>
     *     Win:  6pts
     *     Draw: 3pts
     *     Loss: 0pts
     * </pre>
     *
     * <p>
     * There are also points for each move:
     * <pre>
     *     Rock:     1pts
     *     Paper:    2pts
     *     Scissors: 3pts
     * </pre>
     *
     * @param values the rock-paper-scissors games
     * @return the final score following the guide with end results
     */
    public static long finalScoreFollowingGuideWithResults(final Collection<String> values) {
        return values
            .stream()
            .mapToInt(value -> PART_2_VALUES.getOrDefault(value, 0))
            .sum();
    }
}
