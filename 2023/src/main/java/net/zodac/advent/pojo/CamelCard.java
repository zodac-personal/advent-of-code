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

import java.util.Map;
import java.util.regex.Pattern;
import net.zodac.advent.util.StringUtils;

/**
 * Class defining a game of Camel Cards, where each game includes a hand of cards, a wager, and some definition of the game.
 *
 * @param camelCardType the hand's {@link CamelCardType}
 * @param hand          the cards in the hand
 * @param wager         the amount in the wager
 * @param withJokers    whether this game has Jokers, or Jacks
 */
public record CamelCard(CamelCardType camelCardType, String hand, long wager, boolean withJokers) implements Comparable<CamelCard> {

    private static final int NUMBER_OF_CARDS_IN_HAND = 5;
    private static final String ALL_JOKERS_HAND = "J".repeat(NUMBER_OF_CARDS_IN_HAND);
    private static final char JOKER_SYMBOL = 'J';
    private static final Pattern JOKER_MATCHER = Pattern.compile("J");
    private static final Map<Character, Integer> VALUE_FOR_FACE_CARD = Map.of(
        'A', 14,
        'K', 13,
        'Q', 12,
        'J', 11, // Jack value, not Joker
        'T', 10
    );

    /**
     * Given an input {@link String}, will parse it and return a {@link CamelCard} game.
     *
     * @param input      the input {@link String}
     * @param withJokers whether a {@link #JOKER_SYMBOL} refers to a Joker, or if it defaults to a Jack
     * @return the {@link CamelCard}
     */
    public static CamelCard parse(final String input, final boolean withJokers) {
        final String[] inputTokens = StringUtils.splitOnWhitespace(input);
        final String hand = inputTokens[0].trim();
        final long wager = Long.parseLong(inputTokens[1]);

        if (!withJokers || !JOKER_MATCHER.matcher(hand).find()) {
            return new CamelCard(CamelCardType.getByHand(hand), hand, wager, withJokers);
        }

        if (ALL_JOKERS_HAND.equals(hand)) {
            return new CamelCard(CamelCardType.FIVE_OF_A_KIND, hand, wager, true);
        }

        final char mostOccurringCard = StringUtils.mostOccurringCharacter(hand);
        if (mostOccurringCard == JOKER_SYMBOL) {
            final char secondMostOccurringCard = StringUtils.mostOccurringCharacter(JOKER_MATCHER.matcher(hand).replaceAll(""));
            final String handWithoutJokers = JOKER_MATCHER.matcher(hand).replaceAll(String.valueOf(secondMostOccurringCard));
            return new CamelCard(CamelCardType.getByHand(handWithoutJokers), hand, wager, true);
        }

        final String handWithoutJokers = JOKER_MATCHER.matcher(hand).replaceAll(String.valueOf(mostOccurringCard));
        return new CamelCard(CamelCardType.getByHand(handWithoutJokers), hand, wager, true);
    }

    @Override
    public int compareTo(final CamelCard other) {
        if (camelCardType != other.camelCardType) {
            return Integer.compare(camelCardType.getStrength(), other.camelCardType.getStrength());
        }

        // Same hand type, compare card by card
        for (int i = 0; i < NUMBER_OF_CARDS_IN_HAND; i++) {
            final char thisHandCard = hand.charAt(i);
            final char otherHandCard = other.hand.charAt(i);

            if (thisHandCard != otherHandCard) {
                return Integer.compare(getValueOfCard(thisHandCard), getValueOfCard(otherHandCard));
            }
        }

        // Equal hands
        return 0;
    }

    private int getValueOfCard(final char card) {
        if (withJokers && card == JOKER_SYMBOL) {
            return 1;
        }

        return VALUE_FOR_FACE_CARD.getOrDefault(card, Character.getNumericValue(card));
    }
}
