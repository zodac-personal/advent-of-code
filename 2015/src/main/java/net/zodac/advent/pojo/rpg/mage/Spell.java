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
 * Simple class defining a magic spell to be used by a {@link MagePlayer}.
 *
 * @param name          the name of the {@link Spell}
 * @param damage        the damage caused by the {@link Spell}
 * @param healing       the healing done by the {@link Spell}
 * @param defence       any damage-mitigation provided by the {@link Spell}
 * @param manaRecovery  the mana recovered by the {@link Spell}
 * @param spellDuration how long any multi-turn effects will last
 * @param cost          the mana cost for the {@link Spell}
 */
public record Spell(String name, int damage, int healing, int defence, int manaRecovery, int spellDuration, int cost) {

    /**
     * Creates the <b>Magic Missile</b> {@link Spell}, which causes damage.
     *
     * @param damage the damage caused
     * @param cost   the mana cost of the {@link Spell}
     * @return the created {@link Spell}
     */
    public static Spell missile(final int damage, final int cost) {
        return create("Magic Missile", damage, 0, 0, 0, 0, cost);
    }

    /**
     * Creates the <b>Shield</b> {@link Spell}, which causes some damage to be blocked.
     *
     * @param defence       the damage blocked
     * @param spellDuration how long the {@link Spell} will last
     * @param cost          the mana cost of the {@link Spell}
     * @return the created {@link Spell}
     */
    public static Spell shield(final int defence, final int spellDuration, final int cost) {
        return create("Shield", 0, 0, defence, 0, spellDuration, cost);
    }

    /**
     * Creates the <b>Drain</b> {@link Spell}, which causes some damage and recovers some health.
     *
     * @param damage  the damage caused
     * @param healing the health recovered
     * @param cost    the mana cost of the {@link Spell}
     * @return the created {@link Spell}
     */
    public static Spell drain(final int damage, final int healing, final int cost) {
        return create("Drain", damage, healing, 0, 0, 0, cost);
    }

    /**
     * Creates the <b>Poison</b> {@link Spell}, which causes some damage for several turns.
     *
     * @param damage        the damage caused
     * @param spellDuration how long the {@link Spell} will last
     * @param cost          the mana cost of the {@link Spell}
     * @return the created {@link Spell}
     */
    public static Spell poison(final int damage, final int spellDuration, final int cost) {
        return create("Poison", damage, 0, 0, 0, spellDuration, cost);
    }

    /**
     * Creates the <b>Recharge</b> {@link Spell}, which recovers some mana for several turns.
     *
     * @param manaRecovery  the mana recovered
     * @param spellDuration how long the {@link Spell} will last
     * @param cost          the mana cost of the {@link Spell}
     * @return the created {@link Spell}
     */
    public static Spell recharge(final int manaRecovery, final int spellDuration, final int cost) {
        return create("Recharge", 0, 0, 0, manaRecovery, spellDuration, cost);
    }

    private static Spell create(final String name,
                                final int damage,
                                final int healing,
                                final int defence,
                                final int manaRecovery,
                                final int spellDuration,
                                final int cost) {
        return new Spell(name, damage, healing, defence, manaRecovery, spellDuration, cost);
    }
}
