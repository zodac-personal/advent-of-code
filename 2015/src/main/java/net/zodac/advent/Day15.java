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

import static net.zodac.advent.util.CollectionUtils.extractValuesAsList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.zodac.advent.pojo.Ingredient;
import net.zodac.advent.util.CollectionUtils;
import net.zodac.advent.util.MathUtils;

/**
 * Solution for 2015, Day 15.
 *
 * @see <a href="https://adventofcode.com/2015/day/15">AoC 2015, Day 15</a>
 */
public final class Day15 {

    private static final int MAXIMUM_AMOUNT_OF_INGREDIENTS = 100;
    private static final int VALUE_FOR_NO_CALORIE_CHECK = 0;

    private Day15() {

    }

    /**
     * Given a {@link Collection} of <b>4</b> {@link Ingredient}s, we calculate the score for every combination of these ingredients that add up to
     * {@value #MAXIMUM_AMOUNT_OF_INGREDIENTS} units. For example, we might have 100 units of {@link Ingredient} #1, then 0 of the remainder. Or we
     * could have a mixture, such as 24 units of {@link Ingredient} #1, 45 units of {@link Ingredient} #2, etc. All {@link Ingredient}s must add up to
     * {@value #MAXIMUM_AMOUNT_OF_INGREDIENTS} units.
     *
     * <p>
     * The score is calculated by getting the total value of each property of an {@link Ingredient}:
     * <ul>
     *     <li>{@link Ingredient#capacity()}</li>
     *     <li>{@link Ingredient#durability()}</li>
     *     <li>{@link Ingredient#flavour()}</li>
     *     <li>{@link Ingredient#texture()}</li>
     * </ul>
     * The total value of any property is:
     * <pre>
     *     (valueOfIngredient1 * unitsOfIngredient1) + (valueOfIngredient1 * unitsOfIngredient1) ... + (valueOfIngredientN * unitsOfIngredientN)
     * </pre>
     * <b>NOTE:</b> If any property total is less than <b>0</b>, it will be instead set to <b>0</b>.
     *
     * <p>
     * Once all totals have been calculated, the total score for the combination is calculated by multiplying the total property values together. This
     * will be compared with all other combination scores, and the highest value will be returned.
     *
     * <p>
     * If {@code wantedCalorieCount} is set to any value other than {@value #VALUE_FOR_NO_CALORIE_CHECK}, the total calorie count of the combination
     * will be calculated as above for {@link Ingredient#calories()}. If it is not exactly equal to {@code wantedCalorieCount}, it will not be
     * considered a valid combination.
     *
     * @param ingredients        the {@link Ingredient}s
     * @param wantedCalorieCount the wanted total calorie count for a valid combination
     * @return the score of the highest-scoring combination
     */
    public static long scoreOfBestIngredients(final Collection<Ingredient> ingredients, final int wantedCalorieCount) {
        final List<Integer> initialCombination = new ArrayList<>();
        final int size = ingredients.size();
        for (int i = 0; i < size; i++) {
            initialCombination.add(0);
        }

        return calculateBestScore(ingredients, wantedCalorieCount, initialCombination, 0, MAXIMUM_AMOUNT_OF_INGREDIENTS);
    }

    private static long calculateBestScore(final Collection<Ingredient> ingredients, final int wantedCalorieCount,
                                           final List<Integer> currentCombination, final int currentIndex, final int remainingAmount) {
        if (currentIndex == ingredients.size() - 1) {
            currentCombination.set(currentIndex, remainingAmount);
            return calculateScore(ingredients, wantedCalorieCount, currentCombination);
        }

        long bestScore = Long.MIN_VALUE;

        for (int i = 0; i <= remainingAmount; i++) {
            currentCombination.set(currentIndex, i);
            final long score = calculateBestScore(ingredients, wantedCalorieCount, currentCombination, currentIndex + 1, remainingAmount - i);
            bestScore = Math.max(bestScore, score);
        }

        return bestScore;
    }

    private static Long calculateScore(final Collection<Ingredient> ingredients, final int wantedCalorieCount, final List<Integer> amounts) {
        if (isCaloriesInvalid(ingredients, wantedCalorieCount, amounts)) {
            return 0L;
        }

        final long totalCapacity = MathUtils.multiplyElementsThenSum(amounts, extractValuesAsList(ingredients, Ingredient::capacity));
        final long totalDurability = MathUtils.multiplyElementsThenSum(amounts, extractValuesAsList(ingredients, Ingredient::durability));
        final long totalFlavour = MathUtils.multiplyElementsThenSum(amounts, extractValuesAsList(ingredients, Ingredient::flavour));
        final long totalTexture = MathUtils.multiplyElementsThenSum(amounts, extractValuesAsList(ingredients, Ingredient::texture));

        final List<Long> valuesToCheck = List.of(totalCapacity, totalDurability, totalFlavour, totalTexture);
        final List<Long> lessThanZero = CollectionUtils.findValuesLessThan(valuesToCheck, 0L, Long::compare);
        if (!lessThanZero.isEmpty()) {
            return 0L;
        }

        return totalCapacity * totalDurability * totalFlavour * totalTexture;
    }

    private static boolean isCaloriesInvalid(final Collection<Ingredient> ingredients, final int wantedCalorieCount, final List<Integer> amounts) {
        if (wantedCalorieCount == VALUE_FOR_NO_CALORIE_CHECK) {
            return false;
        }

        final long totalCalories = MathUtils.multiplyElementsThenSum(amounts, extractValuesAsList(ingredients, Ingredient::calories));
        return wantedCalorieCount != totalCalories;
    }
}
