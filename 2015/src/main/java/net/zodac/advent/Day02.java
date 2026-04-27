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
import net.zodac.advent.shape.Box;

/**
 * Solution for 2015, Day 2.
 *
 * @see <a href="https://adventofcode.com/2015/day/2">AoC 2015, Day 2</a>
 */
public final class Day02 {

    private Day02() {

    }

    /**
     * Given a {@link Collection} of {@link Box}s, we calculate the wrapping paper needed for each {@link Box}.
     *
     * <p>
     * The needed wrapping paper for a single {@link Box} is calculated as:
     * <pre>
     *     surfaceArea + areaOfSmallestSide
     * </pre>
     *
     * @param values the {@link Box}s to check
     * @return the total amount of wrapping paper needed
     */
    public static long calculateWrappingPaperNeeded(final Collection<Box> values) {
        return values
            .stream()
            .mapToLong(box -> box.surfaceArea() + box.areaOfSmallestSide())
            .sum();
    }

    /**
     * Given a {@link Collection} of {@link Box}s, we calculate the ribbon needed for each {@link Box}.
     *
     * <p>
     * The needed ribbon for a single {@link Box} is calculated as:
     * <pre>
     *     smallestPerimeterAroundBox + productOfDimensions
     * </pre>
     *
     * @param values the {@link Box}s to check
     * @return the total amount of wrapping paper needed
     */
    public static long calculateRibbonNeeded(final Collection<Box> values) {
        return values
            .stream()
            .mapToLong(box -> box.smallestPerimeter() + box.productOfDimensions())
            .sum();
    }
}
