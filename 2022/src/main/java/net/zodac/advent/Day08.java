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

import net.zodac.advent.grid.Grid;
import net.zodac.advent.util.ArrayUtils;

/**
 * Solution for 2022, Day 8.
 *
 * @see <a href="https://adventofcode.com/2022/day/8">AoC 2022, Day 8</a>
 */
public final class Day08 {

    private Day08() {

    }

    /**
     * Given an {@link Integer} {@link Grid} where each value represents a single tree's height in a forest, we count the number of trees that can be
     * viewed from outside a forest.
     *
     * <p>
     * For example, the input:
     * <pre>
     *     30373
     *     25512
     *     65332
     *     33549
     *     35390
     * </pre>
     *
     * <p>
     * Ignoring the outer edges (which are all visible), we have the inner 9 trees to check. A tree is visible if it can be seen from at least one
     * direction (up, down, left, right). It will be visible from a direction if there are no trees equal to or greater than its height. In the above
     * example, we have 5 visible trees in the middle, and 16 visible trees along the edge.
     *
     * @param integerGrid the input {@link Integer} {@link Grid}
     * @return the number of trees visible from outside
     * @see ArrayUtils#countPerimeterElements(Object[][])
     */
    public static long countTreesVisibleFromOutsideForest(final Grid<Integer> integerGrid) {
        final Integer[][] trees = integerGrid.getInternalGrid();

        long innerCount = 0L;
        for (int i = 1; i < trees.length - 1; i++) {
            for (int j = 1; j < trees[0].length - 1; j++) {
                if (isTreeVisibleFromOutside(trees, i, j)) {
                    innerCount++;
                }
            }
        }

        return innerCount + ArrayUtils.countPerimeterElements(trees);
    }

    private static boolean isTreeVisibleFromOutside(final Integer[][] trees, final int row, final int column) {
        final int tree = trees[row][column];

        return isVisibleUp(trees, tree, row, column)
            || isVisibleDown(trees, tree, row, column)
            || isVisibleLeft(trees, tree, row, column)
            || isVisibleRight(trees, tree, row, column);
    }

    private static boolean isVisibleUp(final Integer[][] trees, final int tree, final int row, final int column) {
        for (int i = (row - 1); i >= 0; i--) {
            if (tree <= trees[i][column]) {
                return false;
            }
        }

        return true;
    }

    private static boolean isVisibleDown(final Integer[][] trees, final int tree, final int row, final int column) {
        for (int i = (row + 1); i < trees.length; i++) {
            if (tree <= trees[i][column]) {
                return false;
            }
        }

        return true;
    }

    private static boolean isVisibleLeft(final Integer[][] trees, final int tree, final int row, final int column) {
        for (int j = (column - 1); j >= 0; j--) {
            if (tree <= trees[row][j]) {
                return false;
            }
        }

        return true;
    }

    private static boolean isVisibleRight(final Integer[][] trees, final int tree, final int row, final int column) {
        for (int j = (column + 1); j < trees[0].length; j++) {
            if (tree <= trees[row][j]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Given an {@link Integer} {@link Grid} where each value represents a single tree's height in a forest, find the tree with the highest 'scenic
     * score'. The scenic score is defined as:
     * <pre>
     *     treesWeCanSeeLookingUp * treesWeCanSeeLookingDown * treesWeCanSeeLookingLeft * treesWeCanSeeLookingRight
     * </pre>
     *
     * <p>
     * For example, the input:
     * <pre>
     *     30373
     *     25512
     *     65332
     *     33549
     *     35390
     * </pre>
     *
     * <p>
     * Ignoring the outer edges (which have 0 visible trees in at least one direction, so a scenic score of <b>0</b>), we have the inner 9 trees to
     * check.  We can see trees in a given direction (up, down, left, right) if the tree is less than the height of our tree, or we reach the edge. In
     * the above example, the middle 5 in the 2nd last row has the highest scenic score, of <b>8</b>.
     *
     * @param integerGrid the input {@link Integer} {@link Grid}
     * @return the highest scenic score
     * @see ArrayUtils#countPerimeterElements(Object[][])
     */
    public static long findHighestScenicScore(final Grid<Integer> integerGrid) {
        final Integer[][] trees = integerGrid.getInternalGrid();
        long max = Long.MIN_VALUE;

        for (int i = 1; i < trees.length - 1; i++) {
            for (int j = 1; j < trees[0].length - 1; j++) {
                final long scenicScore = getScenicScore(trees, i, j);
                max = Math.max(scenicScore, max);
            }
        }
        return max;
    }

    private static long getScenicScore(final Integer[][] trees, final int row, final int column) {
        final int tree = trees[row][column];

        return getScenicScoreUp(trees, row, column, tree)
            * getScenicScoreDown(trees, row, column, tree)
            * getScenicScoreLeft(trees, row, column, tree)
            * getScenicScoreRight(trees, row, column, tree);
    }

    private static long getScenicScoreUp(final Integer[][] trees, final int row, final int column, final int tree) {
        long upScore = 0;
        for (int i = (row - 1); i >= 0; i--) {
            if (tree <= trees[i][column]) {
                upScore++;
                break;
            }
            upScore++;
        }
        return upScore;
    }

    private static long getScenicScoreDown(final Integer[][] trees, final int row, final int column, final int tree) {
        long downScore = 0;
        for (int i = (row + 1); i < trees.length; i++) {
            if (tree <= trees[i][column]) {
                downScore++;
                break;
            }
            downScore++;
        }
        return downScore;
    }

    private static long getScenicScoreLeft(final Integer[][] trees, final int row, final int column, final int tree) {
        long leftScore = 0;
        for (int j = (column - 1); j >= 0; j--) {
            if (tree <= trees[row][j]) {
                leftScore++;
                break;
            }
            leftScore++;
        }
        return leftScore;
    }

    private static long getScenicScoreRight(final Integer[][] trees, final int row, final int column, final int tree) {
        int rightScore = 0;
        for (int j = (column + 1); j < trees[0].length; j++) {
            if (tree <= trees[row][j]) {
                rightScore++;
                break;
            }
            rightScore++;
        }
        return rightScore;
    }
}
