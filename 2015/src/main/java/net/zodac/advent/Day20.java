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

import net.zodac.advent.util.ArrayUtils;

/**
 * Solution for 2015, Day 20.
 *
 * @see <a href="https://adventofcode.com/2015/day/20">AoC 2015, Day 20</a>
 */
public final class Day20 {

    private Day20() {

    }

    /**
     * Delivers items to houses. There are an infinite number of deliverers, and they stagger the houses to deliver to. For example:
     * <pre>
     *     Deliverer 1 delivers to house   1,   2,   3...
     *     Deliverer 2 delivers to house   2,   4,   6...
     *     Deliverer 3 delivers to house   3,   6,   9...
     *     Deliverer n delivers to house n*1, n*2, n*3...
     * </pre>
     *
     * <p>
     * We want the first house that has {@code wantedNumberOfItemsDelivered} items delivered to it.
     *
     * @param wantedNumberOfItemsDelivered the total number of items we want delivered to a single house
     * @param itemsPerDelivery             the number of items delivered in each delivery
     * @return the first house number with the wanted number of deliveries
     */
    public static long deliverToHouses(final int wantedNumberOfItemsDelivered, final int itemsPerDelivery) {
        final int[] houses = new int[wantedNumberOfItemsDelivered / itemsPerDelivery];
        for (int deliverer = 1; deliverer < houses.length; deliverer++) {
            for (int houseNumber = deliverer; houseNumber < houses.length; houseNumber += deliverer) {
                houses[houseNumber] += (deliverer * itemsPerDelivery);
            }
        }

        return ArrayUtils.findSmallestIndexGreaterThanThreshold(houses, wantedNumberOfItemsDelivered);
    }

    /**
     * Delivers items to houses. There are an infinite number of deliverers, and they stagger the houses to deliver to. For example:
     * <pre>
     *     Deliverer 1 delivers to house   1,   2,   3...
     *     Deliverer 2 delivers to house   2,   4,   6...
     *     Deliverer 3 delivers to house   3,   6,   9...
     *     Deliverer n delivers to house n*1, n*2, n*3...
     * </pre>
     *
     * <p>
     * In addition, each deliverer has a maximum number of houses it will deliver to, {@code maxDeliveriesPerDeliverer}.
     *
     * <p>
     * We want the first house that has {@code wantedNumberOfItemsDelivered} items delivered to it.
     *
     * @param wantedNumberOfItemsDelivered the total number of items we want delivered to a single house
     * @param itemsPerDelivery             the number of items delivered in each delivery
     * @param maxDeliveriesPerDeliverer      the maximum number of deliveries a single deliverer will make
     * @return the first house number with the wanted number of deliveries
     */
    public static long deliverToHouses(final int wantedNumberOfItemsDelivered, final int itemsPerDelivery, final int maxDeliveriesPerDeliverer) {
        final int[] houses = new int[wantedNumberOfItemsDelivered / itemsPerDelivery];
        for (int deliverer = 1; deliverer < houses.length; deliverer++) {
            int numberOfHousesDelivered = 0; //
            for (int houseNumber = deliverer; houseNumber < houses.length; houseNumber += deliverer) {
                if (numberOfHousesDelivered >= maxDeliveriesPerDeliverer) {
                    break; // Deliverer cannot deliver to any more houses
                }

                houses[houseNumber] += (deliverer * itemsPerDelivery);
                numberOfHousesDelivered++;
            }
        }

        return ArrayUtils.findSmallestIndexGreaterThanThreshold(houses, wantedNumberOfItemsDelivered);
    }
}
