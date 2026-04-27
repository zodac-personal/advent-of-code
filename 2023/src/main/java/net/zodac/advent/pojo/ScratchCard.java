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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.zodac.advent.util.CollectionUtils;
import net.zodac.advent.util.StringUtils;

/**
 * Record defining a scratchcard. The scratchcard will contain valid winning digits and the drawn digits, and we will look for any drawn winners.
 *
 * @param id      the ID of the {@link ScratchCard}
 * @param winners the drawn digits that were winners
 */
public record ScratchCard(int id, Set<Long> winners) {

    private static final Pattern VALID_SCRATCHCARD = Pattern.compile("Card\\s+(?<id>\\d+):\\s+(?<winners>(?:\\d+\\s*)*+)\\|(?<drawn>(?:\\s*\\d+)*+)");

    /**
     * Creates a {@link ScratchCard} from a {@link CharSequence} in the format:
     * <pre>
     *     Card [id]: [winning digits, space separated] | [drawn digits, space separated]
     * </pre>
     *
     * @param input the {@link CharSequence} to parse
     * @return the {@link ScratchCard}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static ScratchCard parse(final CharSequence input) { // TODO: Stop at String, not CharSequence
        final Matcher matcher = VALID_SCRATCHCARD.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Invalid input: '%s'", input));
        }

        final int id = Integer.parseInt(matcher.group("id"));
        final List<Long> winningDigits = StringUtils.collectNumbersInOrder(matcher.group("winners"));
        final List<Long> drawnDigits = StringUtils.collectNumbersInOrder(matcher.group("drawn"));
        return new ScratchCard(id, CollectionUtils.intersection(new HashSet<>(winningDigits), drawnDigits));
    }

    /**
     * Calculates the points for the {@link ScratchCard}. The first winning digit for the {@link ScratchCard} is worth <b>1</b> point, and the points
     * double for each subsequent winning digit. This can be simplified to:
     * <pre>
     *     2 ^ (numberOfWinners - 1)
     * </pre>
     *
     * <p>
     * If there were no winning digits, <b>0</b> is returned.
     *
     * @return the points for the {@link ScratchCard}
     */
    public long calculatePoints() {
        return winners.isEmpty() ? 0L : ((Double) StrictMath.pow(2L, winners().size() - 1.0D)).longValue();
    }
}
