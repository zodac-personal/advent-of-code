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

import java.util.Objects;

/**
 * POJO defining a piece of {@link Equipment} to be used by a {@link Warrior}.
 */
public sealed class Equipment permits Armour, Ring, Weapon {

    /**
     * The minimum permitted 'attack' value for an {@link Equipment}.
     */
    protected static final int MINIMUM_ATTACK_VALUE = 0;

    /**
     * The minimum permitted 'cost' value for an {@link Equipment}.
     */
    protected static final int MINIMUM_COST_VALUE = 0;

    /**
     * The minimum permitted 'defence' value for an {@link Equipment}.
     */
    protected static final int MINIMUM_DEFENCE_VALUE = 0;

    private final String name;
    private final int cost;
    private final int attack;
    private final int defence;

    /**
     * Default constructor.
     *
     * @param name    the name of the {@link Equipment}
     * @param cost    the cost of the {@link Equipment}
     * @param attack  the attack stats of the {@link Equipment}
     * @param defence the defence stats of the {@link Equipment}
     */
    protected Equipment(final String name, final int cost, final int attack, final int defence) {
        this.name = name;
        this.cost = cost;
        this.attack = attack;
        this.defence = defence;
    }

    /**
     * Retrieve the {@link Equipment} cost.
     *
     * @return the {@link Equipment} cost
     */
    public int cost() {
        return cost;
    }

    /**
     * Retrieve the {@link Equipment} attack.
     *
     * @return the {@link Equipment} attack
     */
    public int attack() {
        return attack;
    }

    /**
     * Retrieve the {@link Equipment} defence.
     *
     * @return the {@link Equipment} defence
     */
    public int defence() {
        return defence;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof final Equipment equipment)) {
            return false;
        }

        return cost == equipment.cost && attack == equipment.attack && defence == equipment.defence && Objects.equals(name, equipment.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost, attack, defence);
    }
}
