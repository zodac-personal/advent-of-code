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
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Implements Dijkstra's algorithm for finding the shortest distance between {@link SearchNode}s, where edges between {@link SearchNode}s may not have
 * the same cost/weight.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">Dijkstra's algorithm</a>
 */
public final class DijkstraSearcher { // TODO: Replace with common implementation

    private static final long START_NODE_DISTANCE = 0L;

    private DijkstraSearcher() {

    }

    /**
     * Finds the shortest weighted distance from the provided {@link SearchNode} to a {@link SearchNode} meets the {@link SearchNode#isAtEndState()}
     * condition.
     *
     * @param startNode the starting {@link SearchNode} to begin our search
     * @return the shortest distance from the provided {@link SearchNode} to a {@link SearchNode} at the end state
     * @throws IllegalStateException thrown if no {@link SearchNode} can be found that meets the {@link SearchNode#isAtEndState()} condition
     */
    public static long findShortestDistance(final SearchNode startNode) {
        final Queue<SearchNode> neighbouringNodes = new PriorityQueue<>();
        startNode.setDistance(START_NODE_DISTANCE);
        neighbouringNodes.add(startNode);

        while (!neighbouringNodes.isEmpty()) {
            final SearchNode closestNeighbour = neighbouringNodes.remove();

            if (closestNeighbour.isAtEndState()) {
                return closestNeighbour.getDistance();
            }

            final Queue<SearchNode> validNeighbouringNodes = getValidNeighbouringNodes(closestNeighbour);
            neighbouringNodes.addAll(validNeighbouringNodes);
        }

        throw new IllegalStateException("Unable to find endState with provided input node");
    }

    private static Queue<SearchNode> getValidNeighbouringNodes(final SearchNode closestNeighbour) {
        final Queue<SearchNode> validNeighbouringNodes = new PriorityQueue<>();
        for (final Map.Entry<Long, ? extends SearchNode> neighbourNodeEntry : closestNeighbour.getNeighbourNodesByDistance().entrySet()) {
            final long distanceToNeighbour = closestNeighbour.getDistance() + neighbourNodeEntry.getKey();

            final SearchNode neighborNode = neighbourNodeEntry.getValue();
            neighborNode.setDistance(distanceToNeighbour);
            validNeighbouringNodes.add(neighborNode);
        }

        return validNeighbouringNodes;
    }
}
