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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * POJO defining an {@link Ingredient} for a recipe.
 *
 * @param name       the name of the {@link Ingredient}
 * @param capacity   the capacity
 * @param durability the durability
 * @param flavour    the flavour
 * @param texture    the texture
 * @param calories   the calories
 */
public record Ingredient(String name, int capacity, int durability, int flavour, int texture, int calories) {

    private static final Pattern INGREDIENT_PATTERN = Pattern.compile(
        "([a-zA-Z]+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)");

    /**
     * Creates a {@link Ingredient} from a {@link CharSequence} in the format:
     * <pre>
     *     [name]: capacity [capacity], durability [durability], flavor [flavour], texture [texture], calories [calories]
     * </pre>
     *
     * @param input the {@link CharSequence} to parse
     * @return the {@link Ingredient}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static Ingredient parse(final CharSequence input) {
        final Matcher matcher = INGREDIENT_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + input);
        }

        final String name = matcher.group(1);
        final int capacity = Integer.parseInt(matcher.group(2));
        final int durability = Integer.parseInt(matcher.group(3));
        final int flavour = Integer.parseInt(matcher.group(4));
        final int texture = Integer.parseInt(matcher.group(5));
        final int calories = Integer.parseInt(matcher.group(6));

        return new Ingredient(name, capacity, durability, flavour, texture, calories);
    }
}
