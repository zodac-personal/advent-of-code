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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.zodac.advent.util.MathUtils;

/**
 * Solution for 2023, Day 8.
 *
 * @see <a href="https://adventofcode.com/2023/day/8">[2023: 08] Haunted Wasteland</a>
 */
public final class Day08 {

    private static final char LEFT_INSTRUCTION = 'L';
    private static final char RIGHT_INSTRUCTION = 'R';
    private static final String START_NODE_NAME = "AAA";
    private static final String END_NODE_NAME = "ZZZ";
    private static final String START_NODE_SUFFIX = "A";
    private static final String END_NODE_SUFFIX = "Z";
    private static final Pattern NODE_PATTERN = Pattern.compile("([12A-Z]{3}) = \\(([12A-Z]{3}), ([12A-Z]{3})\\)"); // Include 1 & 2 for example

    private Day08() {

    }

    /**
     * Given some left/right instructions, and a {@link List} of {@link Node}s as {@link String}s, we can parse out some {@link Node}s which have a
     * valid path from the start node {@link #START_NODE_NAME} to the end node {@link #END_NODE_NAME}. If we exhaust all the instructions, we loop
     * back to the start and repeat until the end node is reached.
     *
     * @param values the input instruction and {@link Node}s
     * @return the number of steps to reach the end node
     */
    public static long countStepsToReachEndNode(final List<String> values) {
        final Map<String, Node> nodesByName = parseNodes(values.subList(2, values.size()));

        final String instructions = values.getFirst();
        final Node startNode = getNode(nodesByName, START_NODE_NAME);
        return followInstructions(startNode, instructions, nodesByName, END_NODE_NAME);
    }

    /**
     * Given some left/right instructions, and a {@link List} of {@link Node}s as {@link String}s, we can parse out some {@link Node}s. Each
     * {@link Node} ending with {@link #START_NODE_SUFFIX} is a start {@link Node}, and will terminate at an end node, ending with
     * {@link #END_NODE_SUFFIX}. We iterate through the instructions until each {@link Node} reaches its end node. If we exhaust all the instructions,
     * we loop back to the start and repeat until the end node is reached.
     *
     * <p>
     * We wish to count the number of steps until all start {@link Node}s reach their end {@link Node} on the same step (they synchronise). Rather
     * than attempt to traverse all start {@link Node}s in each step, instead we will calculate the number of steps needed for each start
     * {@link Node}. We will then calculate the {@link MathUtils#lowestCommonMultiple(Collection)} for these steps, which will be the smallest number
     * of steps needed for all start {@link Node}s to reach their end state at the same time.
     *
     * @param values the input instruction and {@link Node}s
     * @return the number of steps for all start nodes to reach their end node at the same time
     */
    public static long countStepsToReachAllEndNodesAtSameTime(final List<String> values) {
        final Map<String, Node> nodesByName = parseNodes(values.subList(2, values.size()));
        final String instructions = values.getFirst();

        final Collection<Long> countsForStartNodes = nodesByName
            .values()
            .stream()
            .filter(node -> node.name.endsWith(START_NODE_SUFFIX))
            .map(startNode -> followInstructions(startNode, instructions, nodesByName, END_NODE_SUFFIX))
            .toList();

        return MathUtils.lowestCommonMultiple(countsForStartNodes);
    }

    private static Map<String, Node> parseNodes(final Collection<String> values) {
        final Map<String, Node> nodesByName = new HashMap<>();

        for (final String value : values) {
            final Matcher matcher = NODE_PATTERN.matcher(value);

            if (!matcher.find()) {
                throw new IllegalArgumentException("Unable to find match in input: " + value);
            }

            final String name = matcher.group(1);
            final String left = matcher.group(2);
            final String right = matcher.group(3);

            final Node node = new Node(name, left, right);
            nodesByName.put(node.name, node);
        }
        return nodesByName;
    }

    private static long followInstructions(final Node startNode,
                                           final CharSequence instructions,
                                           final Map<String, Node> nodesByName,
                                           final String endStateSuffix) {
        Node current = startNode;
        int count = 0;

        while (!current.name.endsWith(endStateSuffix)) {
            final int instructionIndex = count % instructions.length(); // modulo the instruction so we keep looping until the end state is found
            final char currentInstruction = instructions.charAt(instructionIndex);
            count++;

            current = switch (currentInstruction) {
                case LEFT_INSTRUCTION -> getNode(nodesByName, current.left);
                case RIGHT_INSTRUCTION -> getNode(nodesByName, current.right);
                default -> throw new IllegalStateException(String.format("Unable to parse instruction: '%s'", currentInstruction));
            };
        }
        return count;
    }

    private static Node getNode(final Map<String, Node> nodesByName, final String wantedNode) {
        if (!nodesByName.containsKey(wantedNode)) {
            throw new IllegalArgumentException(String.format("Unable to find node with name '%s'", wantedNode));
        }

        return nodesByName.get(wantedNode);
    }

    private record Node(String name, String left, String right) {

    }
}
