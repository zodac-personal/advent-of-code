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

import java.util.Arrays;
import java.util.List;
import net.zodac.advent.util.StringUtils;

/**
 * A definition of the type of hand for a game of {@link CamelCard}s.
 */
public enum CamelCardType {

    /**
     * A hand with 5 of the same card.
     */
    FIVE_OF_A_KIND(6, List.of(5L)),

    /**
     * A hand with 4 of the same card, and 1 other.
     */
    FOUR_OF_A_KIND(5, List.of(4L, 1L)),

    /**
     * A hand with 3 of the same card, and 2 of another card.
     */
    FULL_HOUSE(4, List.of(3L, 2L)),

    /**
     * A hand with 3 of the same card, and 2 others that are both different.
     */
    THREE_OF_A_KIND(3, List.of(3L, 1L, 1L)),

    /**
     * A hand with 2 of the same card, 2 of another card, and 1 other.
     */
    TWO_PAIR(2, List.of(2L, 2L, 1L)),

    /**
     * A hand with 2 of the same card, and 3 others that are all different.
     */
    ONE_PAIR(1, List.of(2L, 1L, 1L, 1L)),

    /**
     * A hand with 5 different cards.
     */
    HIGH_CARD(0, List.of(1L, 1L, 1L, 1L, 1L));

    private final int strength;
    private final List<Long> expectedCardFrequencies;

    CamelCardType(final int strength, final List<Long> expectedCardFrequencies) {
        this.strength = strength;
        this.expectedCardFrequencies = expectedCardFrequencies;
    }

    /**
     * The relative strength of the {@link CamelCardType}.
     *
     * @return the strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Given a {@link CamelCard} hand, we will find the frequency of cards, and map that to a valid {@link CamelCardType}.
     *
     * @param hand the input {@link CamelCard} hand
     * @return the matching {@link CamelCardType}
     * @throws IllegalArgumentException thrown if there is no match for the hand
     */
    public static CamelCardType getByHand(final String hand) {
        final List<Long> cardFrequencies = StringUtils.characterFrequencies(hand);

        return Arrays.stream(values())
            .filter(camelCardType -> camelCardType.expectedCardFrequencies.equals(cardFrequencies))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid %s: '%s'", CamelCardType.class.getSimpleName(), hand)));
    }
}
