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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class defining a group of {@link Integer} presents.
 */
public final class PresentGroup {

    private final List<Integer> presents = new LinkedList<>();

    private PresentGroup(final Collection<Integer> presents) {
        if (!presents.isEmpty()) {
            this.presents.addAll(presents);
        }
    }

    /**
     * Creates a {@link PresentGroup} with presents.
     *
     * @param presents the presents
     * @return the {@link PresentGroup}
     */
    public static PresentGroup create(final Collection<Integer> presents) {
        return new PresentGroup(presents);
    }

    /**
     * Creates a {@link PresentGroup} with no presents.
     *
     * @return the {@link PresentGroup}
     */
    public static PresentGroup empty() {
        return new PresentGroup(List.of());
    }

    /**
     * Creates a new instance of {@link PresentGroup} and adds the provided present.
     *
     * @param present the present to add
     * @return the new {@link PresentGroup}
     */
    public PresentGroup createWithPresent(final int present) {
        final PresentGroup newPresent = create(presents);
        newPresent.presents.add(present);
        return newPresent;
    }

    /**
     * Creates a new {@link PresentGroup} where the provided {@link Integer} present is not included.
     *
     * @param present the present to remove
     * @return the new {@link PresentGroup}
     */
    public PresentGroup createWithoutPresent(final int present) {
        final int indexOfPresent = presents.indexOf(present);
        final Collection<Integer> newPresents = presents.subList(indexOfPresent + 1, presents.size());
        return create(newPresents);
    }

    /**
     * Calculates the quantum entanglement of the {@link PresentGroup} by taking the product of all present {@link Integer}s.
     *
     * @return the quantum entanglement of the {@link PresentGroup}
     */
    public long calculateQuantumEntanglement() {
        return presents
            .stream()
            .mapToLong(Integer::longValue)
            .reduce((first, second) -> first * second)
            .orElse(0L);
    }

    /**
     * Calculates the quantum entanglement of the {@link PresentGroup} by taking the sum of all present {@link Integer}s.
     *
     * @return the total weight of the {@link PresentGroup}
     */
    public long calculateTotalWeight() {
        return presents
            .stream()
            .mapToInt(Integer::intValue)
            .sum();
    }

    /**
     * Retrieves the {@link PresentGroup} presents.
     *
     * @return an {@link Collections#unmodifiableList(List)} of presents
     */
    public List<Integer> presents() {
        return Collections.unmodifiableList(presents);
    }

    /**
     * Retrieves the number of presents in the {@link PresentGroup}.
     *
     * @return the number of presents
     */
    public int numberOfPresents() {
        return presents.size();
    }
}
