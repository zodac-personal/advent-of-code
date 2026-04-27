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
import java.util.HashMap;
import java.util.Map;
import net.zodac.advent.pojo.ScratchCard;

/**
 * Solution for 2023, Day 4.
 *
 * @see <a href="https://adventofcode.com/2023/day/4">[2023: 04] Scratchcards</a>
 */
public final class Day04 {

    private static final int DEFAULT_NUMBER_OF_COPIES = 0;

    private Day04() {

    }

    /**
     * Given a {@link Collection} of {@link ScratchCard}s, we calculate the points for each {@link ScratchCard}, then sum them.
     *
     * @param scratchCards the input {@link ScratchCard}s
     * @return the sum of points for all {@link ScratchCard}s
     */
    public static long totalPointsForScratchCards(final Collection<ScratchCard> scratchCards) {
        return scratchCards
            .stream()
            .mapToLong(ScratchCard::calculatePoints)
            .sum();
    }

    /**
     * Given a {@link Collection} of {@link ScratchCard}s, each winning digit in a {@link ScratchCard} provides a copy of the next <i>n</i>
     * {@link ScratchCard}s. The number of copies (and original {@link ScratchCard}s) are added up and returned.
     *
     * @param scratchCards the input {@link ScratchCard}s
     * @return the total number of {@link ScratchCard}s, including copies
     */
    public static long countTotalNumberOfScratchCards(final Collection<ScratchCard> scratchCards) {
        final Map<Integer, Integer> copiesById = new HashMap<>();
        long numberOfCopies = 0L;

        for (final ScratchCard scratchCard : scratchCards) {
            final int id = scratchCard.id();
            final int numberOfTimesToExecute = copiesById.getOrDefault(id, DEFAULT_NUMBER_OF_COPIES) + 1;

            final int currentCopiesForId = copiesById.getOrDefault(id, DEFAULT_NUMBER_OF_COPIES);
            copiesById.put(id, currentCopiesForId + 1);

            if (scratchCard.winners().isEmpty()) {
                continue;
            }

            final int numberOfWinners = scratchCard.winners().size();
            for (int i = 1; i <= numberOfWinners; i++) {
                numberOfCopies += numberOfTimesToExecute;
                final int currentCopiesForNextId = copiesById.getOrDefault(id + i, DEFAULT_NUMBER_OF_COPIES);
                copiesById.put(id + i, currentCopiesForNextId + numberOfTimesToExecute);
            }
        }

        return scratchCards.size() + numberOfCopies;
    }
}
