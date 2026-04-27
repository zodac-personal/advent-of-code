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

package net.zodac.advent.pojo.rpg.warrior;

/**
 * Implementation of {@link Equipment} defining a ring.
 */
public final class Ring extends Equipment {

    private Ring(final String name, final int cost, final int attack, final int defence) {
        super(name, cost, attack, defence);
    }

    /**
     * Creates a {@link Ring}.
     *
     * @param name    the name of the {@link Ring}
     * @param cost    the cost of the {@link Ring}
     * @param attack  the attack stats of the {@link Ring}
     * @param defence the defence stats of the {@link Ring}
     * @return the created {@link Ring}
     * @throws IllegalArgumentException if the {@code name} is null or blank, or if the input stats are invalid
     */
    public static Ring create(final String name, final int cost, final int attack, final int defence) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("'name' cannot be null or blank");
        }

        if (cost < MINIMUM_COST_VALUE) {
            throw new IllegalArgumentException(String.format("'cost' must be greater than %s, found: %s", MINIMUM_COST_VALUE, cost));
        }

        if (attack < MINIMUM_ATTACK_VALUE) {
            throw new IllegalArgumentException(String.format("'attack' must be greater than %s, found: %s", MINIMUM_ATTACK_VALUE, attack));
        }

        if (defence < MINIMUM_DEFENCE_VALUE) {
            throw new IllegalArgumentException(String.format("'defence' must be greater than %s, found: %s", MINIMUM_DEFENCE_VALUE, defence));
        }

        return new Ring(name, cost, attack, defence);
    }
}
