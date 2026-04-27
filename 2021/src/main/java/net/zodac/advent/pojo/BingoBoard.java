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

package net.zodac.advent.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * Class defining a bingo board.
 */
public final class BingoBoard {

    // As numbers are marked on the board, this value will replace the existing value
    private static final int MARKED_VALUE = 0;

    private final int[][] board; // TODO: Why is this not Grid<Integer>? Document if this typing is needed
    private final Map<Integer, Pair<Integer, Integer>> cellsAndIndex; // Used to easily find the index for a given number on the board

    private BingoBoard(final int[][] board, final Map<Integer, Pair<Integer, Integer>> cellsAndIndex) {
        this.cellsAndIndex = new HashMap<>(cellsAndIndex);
        this.board = board.clone(); // TODO: Should be ArrayUtils.deepCopy(board) when typing is fixed
    }

    /**
     * Creates a {@link BingoBoard}.
     *
     * <p>
     * The board is populated row by row.
     *
     * @param boardNumbers a {@link List} of {@link Integer}s used to populate the board
     * @param boardSize    the size of the {@link BingoBoard} rows and columns
     * @return the created {@link BingoBoard}
     * @throws IllegalArgumentException thrown if {@code boardNumbers} is empty, or if the boardSize does not match the input {@code boardNumbers}
     */
    public static BingoBoard create(final List<Integer> boardNumbers, final int boardSize) {
        if (boardNumbers.isEmpty()) {
            throw new IllegalArgumentException("boardNumbers cannot be empty!");
        }

        if (boardNumbers.size() != (boardSize * boardSize)) {
            throw new IllegalArgumentException(String.format("Expected %1$dx%1$d numbers, found %2$d", boardSize, boardNumbers.size()));
        }

        final int[][] board = new int[boardSize][boardSize];
        final Map<Integer, Pair<Integer, Integer>> cellsAndIndex = HashMap.newHashMap(boardSize * boardSize);

        int index = 0;
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                final int value = boardNumbers.get(index);
                index++;

                board[row][column] = value;
                cellsAndIndex.put(value, Pair.of(row, column));
            }
        }

        return new BingoBoard(board, cellsAndIndex);
    }

    /**
     * Marks off a number in the {@link BingoBoard}. If the number does not exist in the {@link BingoBoard}, nothing happens.
     *
     * @param number the number to mark off in the {@link BingoBoard}
     */
    public void mark(final int number) {
        final Pair<Integer, Integer> indexToMark = cellsAndIndex.get(number);

        if (indexToMark == null) {
            return;
        }

        board[indexToMark.first()][indexToMark.second()] = MARKED_VALUE;
    }

    /**
     * Checks if the {@link BingoBoard} is a winner.
     *
     * <p>
     * A winning {@link BingoBoard} has had an entire column or row marked off.
     *
     * <p>
     * We first check all rows if any is complete, then all column.
     *
     * @return {@code true} if the {@link BingoBoard} is a winner
     * @see #mark(int)
     */
    public boolean isWinner() {
        for (final int[] row : board) {
            boolean winner = true;
            for (final int columnInRow : row) {
                winner = winner && (columnInRow == MARKED_VALUE);
            }

            if (winner) {
                return true;
            }
        }

        final int boardLength = board[0].length;
        for (int column = 0; column < boardLength; column++) {
            boolean winner = true;
            for (final int[] row : board) {
                winner = winner && (row[column] == MARKED_VALUE);
            }

            if (winner) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds the sum of all non-marked numbers in the {@link BingoBoard}.
     *
     * <p>
     * If a number has been marked, its value will be set to {@link #MARKED_VALUE}, so it will not be included in the sum.
     *
     * @return the sum of all non-marked numbers in the {@link BingoBoard}
     */
    public long sum() {
        final int boardLength = board[0].length;
        long sum = 0L;

        for (final int[] row : board) {
            for (int column = 0; column < boardLength; column++) {
                sum += row[column];
            }
        }

        return sum;
    }
}
