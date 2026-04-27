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

package net.zodac.advent.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Defines a filter for generating a power-set. A {@link PowerSetFilter} can be applied to ensure a combination is only included under specific
 * scenarios, for example, excluding combinations that have two or more of a given subclass.
 *
 * <p>
 * A {@link PowerSetFilter} is defined with two parts. First is the {@code groupingFunction}, a {@link Function} that will group the power-set using
 * {@link java.util.stream.Collectors#groupingBy(Function)}. This will return a {@link Map} containing the group as a key, and the matching values
 * within a power-set combination as a {@link List}. The second part is the {@code predicates}, where each group can have a matching {@link Predicate}
 * to determine whether it is valid.
 *
 * <p>
 * For an example, say we are creating a power-set of interface {@code I}, which has implementations {@code A}, {@code B} and {@code C}. We have a
 * limitation that there can only be <b>1</b> instance of implementation {@code A}, <b>1-2</b> instances of implementation {@code B} and <b>0-5</b>
 * instances of implementation {@code C}.
 *
 * <p>
 * First, we can define the {@code groupingFunction} as:
 * <pre>
 *     {@code
 *     I::getClass
 *     }
 * </pre>
 *
 * <p>
 * This will group the instances into {@link List}s, which we can then filter on.
 *
 * <p>
 * We then define {@link Predicate}s for each of the implementation types as:
 * <pre>
 *     {@code
 *     Map.of(
 *         A.class -> instancesOfTypeA -> instancesOfTypeA != null && instancesOfTypeA.size() == 1,
 *         B.class -> instancesOfTypeB -> instancesOfTypeB != null && instancesOfTypeB.size() > 0 && instancesOfTypeB.size() < 3,
 *         C.class -> instancesOfTypeC -> instancesOfTypeC == null || instancesOfTypeC.size() < 6,
 *     )
 *     }
 * </pre>
 *
 * <p>
 * Given an input of [A1, B1, C1, C2], we would have the following power-set, with groupings and predicate evaluations:
 * <pre>
 *     {
 *       {}                 ->  A: {},   B: {},   C: {}         ->  A is unmet, B is unmet, C is met    ->  Invalid
 *       {A1}               ->  A: {A1}, B: {},   C: {}         ->  A is met,   B is unmet, C is met    ->  Invalid
 *       {B1}               ->  A: {},   B: {B1}, C: {}         ->  A is unmet, B is met,   C is met    ->  Invalid
 *       {C1}               ->  A: {},   B: {},   C: {C1}       ->  A is unmet, B is unmet, C is met    ->  Invalid
 *       {C2}               ->  A: {},   B: {},   C: {C2}       ->  A is unmet, B is unmet, C is met    ->  Invalid
 *       {A1, B1}           ->  A: {A1}, B: {B1}, C: {}         ->  A is met,   B is met,   C is met    ->  VALID
 *       {A1, C1}           ->  A: {A1}, B: {},   C: {C1}       ->  A is met,   B is unmet, C is met    ->  Invalid
 *       {A1, C2}           ->  A: {A1}, B: {},   C: {C2}       ->  A is met,   B is unmet, C is met    ->  Invalid
 *       {B1, C1}           ->  A: {},   B: {B1}, C: {C1}       ->  A is unmet, B is met,   C is met    ->  Invalid
 *       {B1, C2}           ->  A: {},   B: {B1}, C: {C2}       ->  A is unmet, B is met,   C is met    ->  Invalid
 *       {C1, C2}           ->  A: {},   B: {},   C: {C1, C2}   ->  A is unmet, B is unmet, C is met    ->  Invalid
 *       {A1, B1, C1}       ->  A: {A1}, B: {B1}, C: {C1}       ->  A is met,   B is met,   C is met    ->  VALID
 *       {A1, B1, C2}       ->  A: {A1}, B: {B1}, C: {C2}       ->  A is met,   B is met,   C is met    ->  VALID
 *       {A1, C1, C2}       ->  A: {A1}, B: {},   C: {C1, C2}   ->  A is met,   B is unmet, C is met    ->  Invalid
 *       {B1, C1, C2}       ->  A: {},   B: {B1}, C: {C1, C2}   ->  A is unmet, B is met,   C is met    ->  Invalid
 *       {A1, B1, C1, C2}   ->  A: {A1}, B: {B1}, C: {C1, C2}   ->  A is met,   B is met,   C is met    ->  VALID
 *     }
 * </pre>
 *
 * <p>
 * Resulting in a filtered power-set of:
 * <pre>
 *     {
 *         {A1, B1}
 *         {A1, B1, C1}
 *         {A1, B1, C2}
 *         {A1, B1, C1, C2}
 *     }
 * </pre>
 *
 * <p>
 * Another example of a filter, this time using values instead of the class type could be as follows. Given an input of [1, 25, 300]. Our limitation
 * is that we must not have any odd numbers, and at least one even number. We need to group all entries based on whether they are even or odd. We
 * could simply use a {@link Boolean} for this, but for clarity we will use {@link String}s. The {@code groupingFunction} would be:
 * <pre>
 *     {@code
 *     i -> i % 2 != 0 ? "ODD" : "EVEN"
 *     }
 * </pre>
 *
 * <p>
 * We would then define the {@link Predicate}s as:
 * <pre>
 *     {@code
 *     Map.of(
 *         "ODD", oddNumbers -> oddNumbers == null || oddNumbers.isEmpty(),
 *         "EVEN", evenNumbers -> evenNumbers != null && !evenNumbers.isEmpty()
 *     )
 *     }
 * </pre>
 *
 * <b>NOTE:</b> The current limitations are:
 * <ul>
 *     <li>All {@link Predicate}s must be met. Future work would allow a combination to be valid if at least one {@link Predicate} is met.</li>
 *     <li>A grouping function must be supplied. Future work would allow {@link Predicate}s to be applied to a combination as a whole.</li>
 *     <li>An empty group will currently return {@code null} to the {@link Predicate}, need to update to return an empty {@link List}</li>
 * </ul>
 *
 * @param groupingFunction the {@link Function} defining how to group combinations in the power-set
 * @param predicates       a {@link Map} of {@link Predicate}s where the key is a group for {@code groupingFunction}, and the value is the predicate
 * @param <T>              the type of the combinations
 * @param <G>              the key type of the groups of the input {@link List}
 * @see PowerSetUtils
 */
public record PowerSetFilter<G, T>(Function<? super T, G> groupingFunction, Map<G, Predicate<List<T>>> predicates) {

}
