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
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import net.zodac.advent.pojo.PresentGroup;
import net.zodac.advent.pojo.Replacement;

/**
 * Solution for 2015, Day 24.
 *
 * @see <a href="https://adventofcode.com/2015/day/24">AoC 2015, Day 24</a>
 */
public final class Day24 {

    private Day24() {

    }

    /**
     * Given a {@link Collection} of {@link Integer} presents, we must group these presents into {@code numberOfGroups} of even-sized
     * {@link PresentGroup}s (by present value). We must then find the {@link PresentGroup}s with the least number of elements. Of these
     * {@link PresentGroup}s, we calculate their {@link PresentGroup#calculateQuantumEntanglement()}, and return the smallest value.
     *
     * @param values         the input {@link Integer}s
     * @param numberOfGroups the number of groups of presents
     * @return the smallest {@link PresentGroup#calculateQuantumEntanglement()}
     */
    public static long findQuantumEntanglementOfSmallestGroupOfPresents(final Collection<Integer> values, final int numberOfGroups) {
        final int totalSize = values
            .parallelStream()
            .mapToInt(Integer::intValue)
            .sum();
        final int groupSize = totalSize / numberOfGroups;

        return findSmallestFirstGroup(values, groupSize)
            .parallelStream()
            .mapToLong(PresentGroup::calculateQuantumEntanglement)
            .min()
            .orElse(0L);
    }

    private static Set<PresentGroup> findSmallestFirstGroup(final Collection<Integer> values, final int groupSize) {
        final Set<PresentGroup> presentGroups = new HashSet<>();
        int maxSize = Integer.MAX_VALUE;

        final Queue<Replacement<PresentGroup>> queue = createPriorityQueue();
        final Replacement<PresentGroup> start = Replacement.of(PresentGroup.create(values), PresentGroup.empty());
        queue.add(start);

        while (!queue.isEmpty()) {
            final Replacement<PresentGroup> current = queue.poll();
            final PresentGroup target = current.target();
            final int currentNumberOfPresents = target.numberOfPresents();

            if (currentNumberOfPresents > maxSize) {
                break;
            }

            if (target.calculateTotalWeight() == groupSize) {
                if (maxSize > currentNumberOfPresents) {
                    maxSize = currentNumberOfPresents;
                }

                presentGroups.add(target);
            } else if (currentNumberOfPresents < maxSize) {
                queue.addAll(createNextGroups(current, groupSize));
            }
        }

        return presentGroups;
    }

    private static Queue<Replacement<PresentGroup>> createNextGroups(final Replacement<PresentGroup> current, final int groupSize) {
        final PresentGroup source = current.source();
        final PresentGroup target = current.target();

        return source.presents()
                .stream()
                .filter(present -> target.calculateTotalWeight() + present <= groupSize)
                .map(present -> Replacement.of(source.createWithoutPresent(present), target.createWithPresent(present)))
                .collect(Collectors.toCollection(Day24::createPriorityQueue));
    }

    private static Queue<Replacement<PresentGroup>> createPriorityQueue() {
        return new PriorityQueue<>(Comparator.comparing(
            step -> step.target().calculateTotalWeight(), Comparator.reverseOrder()
        ));
    }
}
