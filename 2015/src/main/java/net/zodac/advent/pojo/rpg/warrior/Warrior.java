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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * POJO defining a {@link Warrior} with health, attack and defence, which can fight against other {@link Warrior}s.
 *
 * @param hitPoints     the {@link Warrior}'s hit points
 * @param equipmentCost the cost of all {@link Equipment} for the {@link Warrior}
 * @param attack        the {@link Warrior}'s attack stats
 * @param defence       the {@link Warrior}'s defence stats
 */
public record Warrior(long hitPoints, int equipmentCost, int attack, int defence) {

    private static final int MINIMUM_HITPOINTS_VALUE = 0;
    private static final int MINIMUM_ATTACK_VALUE = 0;
    private static final int MINIMUM_DEFENCE_VALUE = 0;

    private static final int MINIMUM_DAMAGE_PER_ROUND = 1;

    private static final Pattern FIGHTER_PATTERN = Pattern.compile("Hit Points: (\\d+)\nDamage: (\\d+)\nArmor: (\\d+)");

    /**
     * Checks whether this {@link Warrior} can defeat another {@link Warrior}. Each turn, first this {@link Warrior} attacks, then the other
     * {@link Warrior}. The damage is calculated as this {@link Warrior}'s attack stat, minus the other {@link Warrior}'s defence. Each attack will
     * have a minimum of {@value #MINIMUM_DAMAGE_PER_ROUND}, regardless of how high the other {@link Warrior}'s defence is.
     *
     * @param other the {@link Warrior} to fight
     * @return {@code true} if this {@link Warrior} defeats the other {@link Warrior}
     */
    public boolean canWinFightAgainst(final Warrior other) {
        final double turnsToKillOther = Math.ceil(other.hitPoints / (double) (attack - other.defence));
        return hitPoints - (other.attack - defence) * (turnsToKillOther - MINIMUM_DAMAGE_PER_ROUND) >= 0;
    }

    /**
     * Creates a {@link Warrior}.
     *
     * @param hitPoints     the {@link Warrior}'s hit points, at least {@value #MINIMUM_HITPOINTS_VALUE}
     * @param equipmentCost the cost of all {@link Equipment} for the {@link Warrior}
     * @param attack        the {@link Warrior}'s attack stats, at least {@value #MINIMUM_ATTACK_VALUE}
     * @param defence       the {@link Warrior}'s defence stats, at least {@value #MINIMUM_DEFENCE_VALUE}
     * @return the created {@link Warrior}
     * @throws IllegalArgumentException thrown if {@code hitPoints}, {@code attack} or {@code defence} is too low
     */
    public static Warrior create(final int hitPoints, final int equipmentCost, final int attack, final int defence) {
        if (hitPoints < MINIMUM_HITPOINTS_VALUE) {
            throw new IllegalArgumentException(String.format("'hitPoints' must be greater than %s, found: %s", MINIMUM_HITPOINTS_VALUE, hitPoints));
        }

        if (attack < MINIMUM_ATTACK_VALUE) {
            throw new IllegalArgumentException(String.format("'attack' must be greater than %s, found: %s", MINIMUM_ATTACK_VALUE, attack));
        }

        if (defence < MINIMUM_DEFENCE_VALUE) {
            throw new IllegalArgumentException(String.format("'defence' must be greater than %s, found: %s", MINIMUM_DEFENCE_VALUE, defence));
        }

        return new Warrior(hitPoints, equipmentCost, attack, defence);
    }

    /**
     * Creates a {@link Warrior} from a {@link CharSequence} in the format:
     * <pre>
     *     Hit Points: [hitPoints]
     *     Damage: [attack]
     *     Armor: [defence]
     * </pre>
     *
     * @param input the {@link CharSequence} to parse
     * @return the {@link Warrior}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static Warrior parse(final CharSequence input) {
        final Matcher matcher = FIGHTER_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + input);
        }

        final int hitPoints = Integer.parseInt(matcher.group(1));
        final int attack = Integer.parseInt(matcher.group(2));
        final int armour = Integer.parseInt(matcher.group(3));

        return create(hitPoints, 0, attack, armour);
    }
}
