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
 * Describes an actively running {@link Spell}, with the remaining duration defined.
 */
public final class ActiveSpell {

    private final Spell spell;

    private int remainingDuration;

    private ActiveSpell(final Spell spell, final int remainingDuration) {
        this.spell = spell;
        this.remainingDuration = remainingDuration;
    }

    /**
     * Creates an {@link ActiveSpell}.
     *
     * @param spell             the {@link Spell}
     * @param remainingDuration the remaining number of turns for the {@link ActiveSpell}
     * @return the created {@link ActiveSpell}
     */
    public static ActiveSpell create(final Spell spell, final int remainingDuration) {
        return new ActiveSpell(spell, remainingDuration);
    }

    /**
     * The {@link Spell}.
     *
     * @return the {@link Spell}
     */
    public Spell spell() {
        return spell;
    }

    /**
     * The remaining turns for the {@link ActiveSpell}.
     *
     * @return the remaining turns
     */
    public int remainingDuration() {
        return remainingDuration;
    }

    /**
     * Removes <b>1</b> turn from the remaining duration of the {@link ActiveSpell}.
     */
    public void reduceDuration() {
        remainingDuration--;
    }
}
