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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.zodac.advent.math.MathOperation;
import net.zodac.advent.util.NumberUtils;
import net.zodac.advent.util.StringUtils;

/**
 * Defines a {@link Monkey} that has a {@link Collection} of items, and each item is assigned a 'worry' level. The {@link Monkey} will then inspect
 * each item, updating the worry level, and then throw the item to another {@link Monkey}.
 */
public final class Monkey {

    private static final int DEFAULT_POWER_OPERAND = 2;
    private static final int DIVISOR_WHEN_WORRIED = 3;
    private static final int NUMBER_OF_STRINGS_FOR_MONKEY_INPUT = 6;

    private static final Pattern MONKEY_PATTERN = Pattern.compile("""
            Monkey (\\d+):
            \\s+Starting items: (\\d+(,\\s?\\d+)*+)
            \\s+Operation: new = old ([+*]) (\\w+)
            \\s+Test: divisible by (\\d+)
            \\s+If true: throw to monkey (\\d+)
            \\s+If false: throw to monkey (\\d+)""");

    private final int id;
    private final Collection<Long> currentItems;
    private final MathOperation mathOperation;
    private final int operand;
    private final int divisorTest;
    private final int trueMonkeyId;
    private final int falseMonkeyId;
    private final boolean isWorried;

    private long numberOfInspections;

    private Monkey(final int id,
                   final List<Long> currentItems,
                   final MathOperation mathOperation,
                   final int operand,
                   final int divisorTest,
                   final int trueMonkeyId,
                   final int falseMonkeyId,
                   final boolean isWorried
    ) {
        this.currentItems = new ArrayList<>(currentItems);
        this.id = id;
        this.mathOperation = mathOperation;
        this.operand = operand;
        this.divisorTest = divisorTest;
        this.trueMonkeyId = trueMonkeyId;
        this.falseMonkeyId = falseMonkeyId;
        this.isWorried = isWorried;
    }

    /**
     * Takes the input {@link String}s in the following format, and parses it into a {@link Monkey}:
     * <pre>
     *     Monkey [id]:
     *       Starting items: [currentItems]
     *       Operation: new = old [operation] [operand]
     *       Test: divisible by [divisorTest]
     *         If true: throw to monkey [trueMonkeyId]
     *         If false: throw to monkey [falseMonkeyId]
     * </pre>
     *
     * @param input     the input {@link List} of {@value #NUMBER_OF_STRINGS_FOR_MONKEY_INPUT} {@link String}s defining the {@link Monkey}
     * @param isWorried whether we are worried about the items per throw or not
     * @return the {@link Monkey}
     * @throws IllegalArgumentException thrown if the incorrect number of {@link String}s are provided, or the format is not matched
     */
    public static Monkey parse(final List<String> input, final boolean isWorried) {
        if (input.size() != NUMBER_OF_STRINGS_FOR_MONKEY_INPUT) {
            throw new IllegalArgumentException(String.format("Expected %s values, found: %s", NUMBER_OF_STRINGS_FOR_MONKEY_INPUT, input.size()));
        }

        final String inputAsSingleString = String.join("\n", input);
        final Matcher matcher = MONKEY_PATTERN.matcher(inputAsSingleString);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + inputAsSingleString);
        }

        final int id = Integer.parseInt(matcher.group(1));
        final List<Long> currentItems = StringUtils.collectNumbersInOrder(matcher.group(2));

        final String operationString = matcher.group(5);
        final MathOperation mathOperation;
        final int opValue;
        if (NumberUtils.isInteger(operationString)) {
            mathOperation = MathOperation.getBySymbol(matcher.group(4).charAt(0));
            opValue = Integer.parseInt(operationString);
        } else {
            mathOperation = MathOperation.POWER;
            opValue = DEFAULT_POWER_OPERAND;
        }

        final int divisorTest = Integer.parseInt(matcher.group(6));
        final int trueMonkeyId = Integer.parseInt(matcher.group(7));
        final int falseMonkeyId = Integer.parseInt(matcher.group(8));
        return new Monkey(id, currentItems, mathOperation, opValue, divisorTest, trueMonkeyId, falseMonkeyId, isWorried);
    }

    /**
     * Throws new items to the other {@link Monkey}s. For each of the {@link Monkey}'s {@code currentItems}, the following actions are performed:
     * <ol>
     *     <li>The {@link Monkey} inspects the item, updating its value according to {@code operation} and {@code operand}</li>
     *     <li>If {@code isWorried}, divide the value by {@value #DIVISOR_WHEN_WORRIED}, or else mod the value by {@code lowestCommonMultiple}</li>
     *     <li>Perform a test to see if the new value is divisible by {@code divisorTest}</li>
     *     <li>Based on the above result, choose the target {@link Monkey} to throw to: {@code trueMonkeyId} or {@code falseMonkeyId}</li>
     * </ol>
     *
     * @param lowestCommonMultiple the LCM used to reduce the 'worry' level of the item to avoid the numbers getting too large
     * @return A {@link Map} of all items to be thrown to a {@link Monkey} keyed by the ID of the target {@link Monkey}
     */
    public Map<Integer, List<Long>> throwItemsToOtherMonkeys(final long lowestCommonMultiple) {
        final Map<Integer, List<Long>> valsByTarget = new HashMap<>();

        for (final long currentItemValue : currentItems) {
            final long newItemValue = calculateNewItemValue(lowestCommonMultiple, currentItemValue);

            final int targetMonkeyId = determineTargetMonkeyIdForNewItem(newItemValue);
            valsByTarget.computeIfAbsent(targetMonkeyId, _ -> new ArrayList<>()).add(newItemValue);
        }

        numberOfInspections += currentItems.size();
        currentItems.clear();
        return valsByTarget;
    }

    private int determineTargetMonkeyIdForNewItem(final long newItemValue) {
        return newItemValue % divisorTest == 0 ? trueMonkeyId : falseMonkeyId;
    }

    private long calculateNewItemValue(final long lowestCommonMultiple, final long currentItemValue) {
        long newItemValue = mathOperation.calculate(currentItemValue, operand);

        if (isWorried) {
            newItemValue = newItemValue / DIVISOR_WHEN_WORRIED;
        } else {
            // The numbers can get large depending on the operation being applied
            // Since our tests only care about the remainder of the item value after dividing, we don't actually need the full value
            // Instead, we can mod each value by the LCM of all possible divisors
            // This was the number size remains manageable, while we can continue testing against the divisors
            newItemValue = newItemValue % lowestCommonMultiple;
        }
        return newItemValue;
    }

    /**
     * Adds a new item to the {@link Monkey}.
     *
     * @param newItem the item to add
     */
    public void addItem(final long newItem) {
        currentItems.add(newItem);
    }

    /**
     * The number of inspections performed by the {@link Monkey}.
     *
     * @return the number of inspections
     */
    public long numberOfInspections() {
        return numberOfInspections;
    }

    /**
     * The {@link Monkey} ID.
     *
     * @return the {@link Monkey} ID
     */
    public int id() {
        return id;
    }

    /**
     * The {@code divisorTest} value.
     *
     * @return the {@code divisorTest} value
     */
    public long divisorTest() {
        return divisorTest;
    }
}
