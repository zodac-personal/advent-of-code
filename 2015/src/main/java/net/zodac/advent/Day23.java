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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.zodac.advent.util.MathUtils;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2015, Day 23.
 *
 * @see <a href="https://adventofcode.com/2015/day/23">AoC 2015, Day 23</a>
 */
public final class Day23 {

    private static final long DEFAULT_REGISTER_VALUE = 0L;
    private static final int DEFAULT_INDEX_INCREMENT = 1;

    private Day23() {

    }

    /**
     * Based on the input {@link String} commands, we populate a set of registers. Once populated, we retrieve the value of the
     * {@code wantedRegisterValue}.
     *
     * @param commands            the input {@link String} commands
     * @param wantedRegisterValue the register whose value we want
     * @param initialValues       the initial values for registers, if any
     * @return the register value
     */
    public static long calculateRegisterValue(final List<String> commands, final char wantedRegisterValue, final Map<Character, Long> initialValues) {
        final Map<Character, Long> registerValues = populateRegisterValues(commands, initialValues);
        return registerValues.getOrDefault(wantedRegisterValue, 0L);
    }

    private static Map<Character, Long> populateRegisterValues(final List<String> commands, final Map<Character, Long> initialValues) {
        final Map<Character, Long> registerValues = new HashMap<>(initialValues);

        final int numberOfCommands = commands.size();
        int index = 0;
        while (index < numberOfCommands) {
            final String value = commands.get(index);
            final String[] tokens = StringUtils.splitOnWhitespace(value);

            final String command = tokens[0];

            switch (command) {
                case "hlf" -> index += halve(registerValues, getRegisterName(tokens));
                case "inc" -> index += increment(registerValues, getRegisterName(tokens));
                case "tpl" -> index += triple(registerValues, getRegisterName(tokens));
                case "jmp" -> index += jump(tokens);
                case "jie" -> index += jumpIfEven(registerValues, tokens);
                case "jio" -> index += jumpIfOne(registerValues, tokens);
                default -> throw new IllegalStateException("Unable to handle command: " + command);
            }
        }

        return registerValues;
    }

    private static int halve(final Map<? super Character, Long> registerValues, final char register) {
        final long registerValue = registerValues.getOrDefault(register, DEFAULT_REGISTER_VALUE);
        final long newRegisterValue = registerValue / 2;
        registerValues.put(register, newRegisterValue);
        return DEFAULT_INDEX_INCREMENT;
    }

    private static int increment(final Map<? super Character, Long> registerValues, final char register) {
        final long registerValue = registerValues.getOrDefault(register, DEFAULT_REGISTER_VALUE);
        final long newRegisterValue = registerValue + 1;
        registerValues.put(register, newRegisterValue);
        return DEFAULT_INDEX_INCREMENT;
    }

    private static int triple(final Map<? super Character, Long> registerValues, final char register) {
        final long registerValue = registerValues.getOrDefault(register, DEFAULT_REGISTER_VALUE);
        final long newRegisterValue = registerValue * 3;
        registerValues.put(register, newRegisterValue);
        return DEFAULT_INDEX_INCREMENT;
    }

    private static int jump(final String[] tokens) {
        return Integer.parseInt(tokens[1]);
    }

    private static int jumpIfEven(final Map<Character, Long> registerValues, final String[] tokens) {
        final char register = getRegisterNameWithoutComma(tokens);
        final int offset = Integer.parseInt(tokens[2]);

        final long registerValue = registerValues.getOrDefault(register, DEFAULT_REGISTER_VALUE);
        return MathUtils.isEven(registerValue) ? offset : DEFAULT_INDEX_INCREMENT;
    }

    private static int jumpIfOne(final Map<Character, Long> registerValues, final String[] tokens) {
        final char register = getRegisterNameWithoutComma(tokens);
        final int offset = Integer.parseInt(tokens[2]);

        final long registerValue = registerValues.getOrDefault(register, DEFAULT_REGISTER_VALUE);
        return registerValue == 1 ? offset : DEFAULT_INDEX_INCREMENT;
    }

    private static char getRegisterName(final String[] tokens) {
        return tokens[1].charAt(0);
    }

    private static char getRegisterNameWithoutComma(final String[] tokens) {
        return StringUtils.removeLastCharacter(tokens[1]).charAt(0);
    }
}
