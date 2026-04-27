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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.zodac.advent.pojo.BingoBoard;
import net.zodac.advent.pojo.tuple.Pair;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2021, Day 4.
 *
 * @see <a href="https://adventofcode.com/2021/day/4">AoC 2021, Day 4</a>
 */
public final class Day04 {

    // To be used when no winning bingo board can be found
    private static final int INVALID_WINNING_NUMBER = -1;
    private static final char BLANK_SPACE = ' ';

    private Day04() {

    }

    /**
     * For the provided {@link BingoBoard} values, we convert the raw values to {@link BingoBoard}s, then iterate through drawing the bingo numbers.
     * We iterate until we find the first {@link BingoBoard} to be a winner.
     *
     * <p>
     * We then calculate the sum of its remaining, un-drawn values. This is multiplied by the winning number, to calculate the final score of the
     * {@link BingoBoard}.
     *
     * @param bingoNumbersToDraw an ordered {@link List} of the numbers to be drawn
     * @param bingoBoardValues   the raw {@link BingoBoard} values
     * @return the final score of the first winning {@link BingoBoard}
     * @see BingoBoard#isWinner()
     */
    public static long finalScoreOfFirstWinningBingoBoard(final Collection<Integer> bingoNumbersToDraw, final List<String> bingoBoardValues) {
        final List<BingoBoard> bingoBoards = convertBingoBoards(bingoBoardValues);
        final Pair<Integer, Optional<BingoBoard>> firstWinningNumberAndBingoBoard = drawNumbersAndReturnFirstWinner(bingoNumbersToDraw, bingoBoards);

        if (firstWinningNumberAndBingoBoard.first() == INVALID_WINNING_NUMBER || firstWinningNumberAndBingoBoard.second().isEmpty()) {
            return 0L;
        }

        return firstWinningNumberAndBingoBoard.second().get().sum() * firstWinningNumberAndBingoBoard.first();
    }

    /**
     * For the provided {@link BingoBoard} values, we convert the raw values to {@link BingoBoard}s, then iterate through drawing the bingo numbers.
     * We iterate through them all, and keep not of the last {@link BingoBoard} to be a winner.
     *
     * <p>
     * We then calculate the sum of its remaining, un-drawn values. This is multiplied by the winning number, to calculate the final score of the
     * {@link BingoBoard}.
     *
     * @param bingoNumbersToDraw an ordered {@link List} of the numbers to be drawn
     * @param bingoBoardValues   the raw {@link BingoBoard} values
     * @return the final score of the last winning {@link BingoBoard}
     * @see BingoBoard#isWinner()
     */
    public static long finalScoreOfLastWinningBingoBoard(final Collection<Integer> bingoNumbersToDraw, final List<String> bingoBoardValues) {
        final List<BingoBoard> bingoBoards = convertBingoBoards(bingoBoardValues);
        final Pair<Integer, Optional<BingoBoard>> lastWinningNumberAndBingoBoard = drawNumbersAndReturnLastWinner(bingoNumbersToDraw, bingoBoards);

        if (lastWinningNumberAndBingoBoard.first() == INVALID_WINNING_NUMBER || lastWinningNumberAndBingoBoard.second().isEmpty()) {
            return 0L;
        }

        return lastWinningNumberAndBingoBoard.second().get().sum() * lastWinningNumberAndBingoBoard.first();
    }

    private static Pair<Integer, Optional<BingoBoard>> drawNumbersAndReturnFirstWinner(final Collection<Integer> pickedNumbers,
                                                                                       final Collection<BingoBoard> bingoBoards) {
        for (final int pickedNumber : pickedNumbers) {
            for (final BingoBoard bingoBoard : bingoBoards) {
                bingoBoard.mark(pickedNumber);

                if (bingoBoard.isWinner()) {
                    return Pair.of(pickedNumber, Optional.of(bingoBoard));
                }
            }
        }

        return Pair.ofSingle(INVALID_WINNING_NUMBER);
    }

    private static Pair<Integer, Optional<BingoBoard>> drawNumbersAndReturnLastWinner(final Collection<Integer> bingoNumbers,
                                                                                      final List<BingoBoard> bingoBoards) {
        Pair<Integer, Optional<BingoBoard>> lastWinner = Pair.ofSingle(INVALID_WINNING_NUMBER);

        // If the board is not a winner, it is added to the boards to be checked for the next number
        // If it is a winner, we update 'lastWinner' and stop checking it for future numbers
        List<BingoBoard> bingoBoardsToCheck = new ArrayList<>(bingoBoards);
        for (final int number : bingoNumbers) {
            final List<BingoBoard> nextBingoBoardsToCheck = new ArrayList<>();

            for (final BingoBoard bingoBoard : bingoBoardsToCheck) {
                bingoBoard.mark(number);

                if (bingoBoard.isWinner()) {
                    lastWinner = Pair.of(number, Optional.of(bingoBoard));
                } else {
                    nextBingoBoardsToCheck.add(bingoBoard);
                }
            }

            bingoBoardsToCheck = nextBingoBoardsToCheck;
        }

        return lastWinner;
    }

    private static List<BingoBoard> convertBingoBoards(final List<String> bingoBoardValues) {
        if (bingoBoardValues.isEmpty()) {
            return Collections.emptyList();
        }

        final int boardSize = StringUtils.splitOnWhitespace(bingoBoardValues.getFirst()).length;
        final List<BingoBoard> bingoBoards = new ArrayList<>();
        final int numberOfBoardValues = bingoBoardValues.size();

        for (int i = 0; i < numberOfBoardValues; i += boardSize) {
            final StringBuilder boardNumbersRaw = new StringBuilder();
            for (int j = 0; j < boardSize; j++) {
                boardNumbersRaw.append(bingoBoardValues.get(i + j)).append(BLANK_SPACE);
            }

            final List<Integer> boardNumbers =
                Arrays.stream(StringUtils.splitOnWhitespace(boardNumbersRaw.toString()))
                    .filter(input -> !input.isBlank())
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .toList();

            bingoBoards.add(BingoBoard.create(boardNumbers, boardSize));
        }

        return bingoBoards;
    }
}
