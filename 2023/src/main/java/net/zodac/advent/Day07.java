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

package net.zodac.advent;

import java.util.Collection;
import java.util.List;
import net.zodac.advent.pojo.CamelCard;

/**
 * Solution for 2023, Day 7.
 *
 * @see <a href="https://adventofcode.com/2023/day/7">[2023: 07] Camel Cards</a>
 */
public final class Day07 {

    private Day07() {

    }

    /**
     * Given a {@link Collection} of {@link String}s which represent {@link CamelCard}s, all {@link CamelCard}s are sorted, then the winnings are
     * calculated. This is done by multiplying the {@link CamelCard}'s rank (its place in a sorted {@link List}) with the {@link CamelCard#wager()}.
     *
     * @param values     the {@link String}s representing {@link CamelCard}s
     * @param withJokers whether the {@link CamelCard}s consider a <b>'J'</b> value to be a Joker, or a Jack
     * @return the total winnings
     */
    public static long calculateTotalWinnings(final Collection<String> values, final boolean withJokers) {
        final List<CamelCard> sortedCards = values.stream()
            .map((String input) -> CamelCard.parse(input, withJokers))
            .sorted()
            .toList();

        final int numberOfSortedCards = sortedCards.size();
        long totalWinnings = 0L;
        for (int rank = 1; rank <= numberOfSortedCards; rank++) {
            final CamelCard camelCard = sortedCards.get(rank - 1);
            totalWinnings += camelCard.wager() * rank;
        }

        return totalWinnings;
    }
}
