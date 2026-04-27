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

/**
 * Interface defining a weighted node to be used in a searching algorithm.
 */
public interface SearchNode extends Comparable<SearchNode> {

    /**
     * Retrieve all neighbouring {@link SearchNode}s of this {@link SearchNode}, keyed by the distance between them.
     *
     * @return the neighbouring {@link SearchNode}s
     */
    Map<Long, ? extends SearchNode> getNeighbourNodesByDistance();

    /**
     * Retrieve the weighted distance/cost for this {@link SearchNode}.
     *
     * @return the {@link SearchNode} distance
     */
    long getDistance();

    /**
     * Set the weighted distance/cost for this {@link SearchNode}.
     *
     * @param distance the new {@link SearchNode} distance
     */
    void setDistance(long distance);

    /**
     * Checks whether the {@link SearchNode} has reached its end state.
     *
     * @return {@code true} if the end state has been reached
     */
    boolean isAtEndState();

    @Override
    default int compareTo(final SearchNode other) {
        return Long.compare(getDistance(), other.getDistance());
    }
}
