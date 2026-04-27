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

/**
 * A simple die that returns a deterministic roll. Each roll will increment the value by 1 until it reaches {@code maxValue}.
 */
public final class DeterministicDie {

    private final long maxValue;

    private long counter;

    private DeterministicDie(final long maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Creates a {@link DeterministicDie} with a maximum value.
     *
     * @param maxValue the maximum value for a die roll
     * @return the created {@link DeterministicDie}
     */
    public static DeterministicDie createWithMaxValue(final long maxValue) {
        return new DeterministicDie(maxValue);
    }

    /**
     * Performs multiple rolls of the die, then returns the sum of all rolls.
     *
     * @param numberOfRolls the number of times to roll the die
     * @return the roll values
     * @throws IllegalArgumentException if the {@code maxValue} of a die roll is equal to {@link Long#MAX_VALUE} / {@code numberOfRolls} an overflow
     *                                  may occur
     */
    public long rollDieMultipleTimes(final int numberOfRolls) {
        if (maxValue > (Long.MAX_VALUE / numberOfRolls)) {
            throw new IllegalArgumentException(String.format("Possible overflow with %s rolls with a max value of %s", numberOfRolls, maxValue));
        }

        long totalRollScore = 0;

        for (int i = 0; i < numberOfRolls; i++) {
            totalRollScore += rollDie();
        }

        return totalRollScore;
    }

    /**
     * Performs a single roll of the die.
     *
     * @return the roll value
     */
    public long rollDie() {
        counter = ((counter + 1) % maxValue);
        return counter;
    }
}
