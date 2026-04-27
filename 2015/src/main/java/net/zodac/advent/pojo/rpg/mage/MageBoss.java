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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * POJO defining a {@link MageBoss} with health and attack, which fights against a {@link MagePlayer}.
 *
 * @param hitPoints the {@link MageBoss}'s hit points
 * @param attack    the {@link MageBoss}'s attack stats
 */
public record MageBoss(int hitPoints, int attack) {

    private static final int MINIMUM_HITPOINTS_VALUE = 0;
    private static final int MINIMUM_ATTACK_VALUE = 0;

    private static final Pattern MAGE_BOSS_PATTERN = Pattern.compile("Hit Points: (\\d+)\nDamage: (\\d+)");

    /**
     * Creates a {@link MageBoss} from a {@link CharSequence} in the format:
     * <pre>
     *     Hit Points: [hitPoints]
     *     Damage: [attack]
     * </pre>
     *
     * @param input the {@link CharSequence} to parse
     * @return the {@link MageBoss}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static MageBoss parse(final CharSequence input) {
        final Matcher matcher = MAGE_BOSS_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + input);
        }

        final int hitPoints = Integer.parseInt(matcher.group(1));
        final int attack = Integer.parseInt(matcher.group(2));

        return create(hitPoints, attack);
    }

    /**
     * Attacks the {@link MageBoss} and returns an updated instance.
     *
     * @param damage the amount of damage to the {@link MagePlayer}
     * @return the updated {@link MagePlayer}
     */
    public MageBoss attack(final int damage) {
        final int newHitPoints = Math.max(MINIMUM_HITPOINTS_VALUE, hitPoints - damage);
        return create(newHitPoints, attack);
    }

    private static MageBoss create(final int hitPoints, final int attack) {
        if (hitPoints < MINIMUM_HITPOINTS_VALUE) {
            throw new IllegalArgumentException(String.format("'hitPoints' must be greater than %s, found: %s", MINIMUM_HITPOINTS_VALUE, hitPoints));
        }

        if (attack < MINIMUM_ATTACK_VALUE) {
            throw new IllegalArgumentException(String.format("'attack' must be greater than %s, found: %s", MINIMUM_ATTACK_VALUE, attack));
        }

        return new MageBoss(hitPoints, attack);
    }
}
