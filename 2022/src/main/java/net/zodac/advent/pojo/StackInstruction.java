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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple class defining how many objects to move between two stacks (implemented as {@link java.util.Deque}s), that are IDed by an {@link Integer}.
 *
 * @param sourceStackId          the ID of the source stack
 * @param destinationStackId     the ID of the destination stack
 * @param numberOfElementsToMove the number of elements to move from source to destination
 */
public record StackInstruction(int sourceStackId, int destinationStackId, int numberOfElementsToMove) {

    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    /**
     * Creates a {@link StackInstruction} from a {@link CharSequence} in the format:
     * <pre>
     *     move [entriesToMove] from [sourceStackId] to [destinationStackId]
     * </pre>
     *
     * @param input the {@link CharSequence} to parse
     * @return the {@link StackInstruction}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static StackInstruction parse(final CharSequence input) {
        final Matcher matcher = INSTRUCTION_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + input);
        }

        final int sourceId = Integer.parseInt(matcher.group(2));
        final int destinationId = Integer.parseInt(matcher.group(3));
        final int numberOfElementsToMove = Integer.parseInt(matcher.group(1));

        return new StackInstruction(sourceId, destinationId, numberOfElementsToMove);
    }
}
