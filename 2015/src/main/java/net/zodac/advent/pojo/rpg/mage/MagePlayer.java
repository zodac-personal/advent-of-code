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

package net.zodac.advent.pojo.rpg.mage;

/**
 * POJO defining a {@link MagePlayer} with health and mana, which can fight against a {@link MageBoss}.
 *
 * @param hitPoints the {@link MagePlayer}'s hit points
 * @param mana      the {@link MagePlayer}'s mana stats
 */
public record MagePlayer(int hitPoints, int mana) {

    private static final int MINIMUM_HITPOINTS_VALUE = 0;
    private static final int MINIMUM_MANA_VALUE = 0;

    /**
     * Creates a {@link MagePlayer}.
     *
     * @param hitPoints the {@link MagePlayer}'s hit points, at least {@value #MINIMUM_HITPOINTS_VALUE}
     * @param mana      the {@link MagePlayer}'s mana stats, at least {@value #MINIMUM_MANA_VALUE}
     * @return the created {@link MagePlayer}
     * @throws IllegalArgumentException thrown if the input {@code hitPoints} or {@code mana} is too low
     */
    public static MagePlayer create(final int hitPoints, final int mana) {
        if (hitPoints < MINIMUM_HITPOINTS_VALUE) {
            throw new IllegalArgumentException(String.format("'hitPoints' must be greater than %s, found: %s", MINIMUM_HITPOINTS_VALUE, hitPoints));
        }

        if (mana < MINIMUM_MANA_VALUE) {
            throw new IllegalArgumentException(String.format("'mana' must be greater than %s, found: %s", MINIMUM_MANA_VALUE, mana));
        }

        return new MagePlayer(hitPoints, mana);
    }

    /**
     * Attacks the {@link MagePlayer} and returns an updated instance.
     *
     * @param damage the amount of damage to the {@link MagePlayer}
     * @return the updated {@link MagePlayer}
     */
    public MagePlayer attack(final int damage) {
        final int newHitPoints = Math.max(MINIMUM_HITPOINTS_VALUE, hitPoints - damage);
        return create(newHitPoints, mana);
    }

    /**
     * Updates the {@link MagePlayer} after an attack and returns an updated instance.
     *
     * @param hitPointsIncrease the amount of hitpoints gained by the {@link MagePlayer}
     * @param manaIncrease      the amount of mana gained by the {@link MagePlayer}
     * @return the updated {@link MagePlayer}
     */
    public MagePlayer update(final int hitPointsIncrease, final int manaIncrease) {
        final int newHitPoints = Math.max(MINIMUM_HITPOINTS_VALUE, hitPoints + hitPointsIncrease);
        final int newMana = mana + manaIncrease;
        return create(newHitPoints, newMana);
    }
}
