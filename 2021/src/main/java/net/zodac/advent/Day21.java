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
import java.util.Arrays;
import java.util.List;
import net.zodac.advent.pojo.CircularBoard;
import net.zodac.advent.pojo.DeterministicDie;

/**
 * Solution for 2021, Day 21.
 *
 * @see <a href="https://adventofcode.com/2021/day/21">AoC 2021, Day 21</a>
 */
public final class Day21 {

    private Day21() {

    }

    /**
     * Given a number of player positions on a {@link CircularBoard}, we roll a {@link DeterministicDie} {@code numberOfDiceRolls} times. After each
     * set of rolls, we check the player's score. If it is above the {@code winningScoreThreshold}, the game ends.
     *
     * <p>
     * We then get the losing score (the lowest score) and multiply by the number of dice rolls thrown during the game.
     *
     * @param startPositions        the starting position for each player
     * @param numberOfDiceRolls     the number of times the {@link DeterministicDie} should be rolled
     * @param maxDieValue           the maximum value of the {@link DeterministicDie}
     * @param boardSize             the size of the {@link CircularBoard}
     * @param winningScoreThreshold the winning score for any player to reach
     * @return the lowest player's score times the total number of dice rolls
     */
    public static long getLosingScoreTimesNumberOfRolls(final List<Integer> startPositions,
                                                        final int numberOfDiceRolls,
                                                        final int maxDieValue,
                                                        final int boardSize,
                                                        final long winningScoreThreshold) {
        final DeterministicDie deterministicDie = DeterministicDie.createWithMaxValue(maxDieValue);
        final CircularBoard circularBoard = CircularBoard.createWithMaxValue(boardSize);
        final int numberOfPlayers = startPositions.size();

        final List<Integer> boardPositions = new ArrayList<>(startPositions);
        final long[] scores = new long[numberOfPlayers];

        long numberOfTimesRolled = 0;
        boolean searchingForWinner = true;

        while (searchingForWinner) {
            for (int playerNumber = 0; playerNumber < numberOfPlayers; playerNumber++) {
                final int currentPosition = boardPositions.get(playerNumber);
                final long currentScore = scores[playerNumber];

                final long spacesToMove = deterministicDie.rollDieMultipleTimes(numberOfDiceRolls);
                numberOfTimesRolled += numberOfDiceRolls;

                final int newPosition = circularBoard.moveOnBoardAndGetNewPosition(currentPosition, spacesToMove);
                final long newScore = currentScore + newPosition;

                if (newScore >= winningScoreThreshold) {
                    searchingForWinner = false;
                    break; // Break so we don't roll for another player
                }

                boardPositions.set(playerNumber, newPosition);
                scores[playerNumber] = newScore;
            }
        }

        Arrays.sort(scores);
        return scores[0] * numberOfTimesRolled;
    }
}
