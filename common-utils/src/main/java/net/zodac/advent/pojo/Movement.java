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
import net.zodac.advent.grid.Direction;

/**
 * Simple POJO defining a movement in a given {@link Direction} for a number of spaces.
 *
 * @param direction the {@link Direction} to move in
 * @param spaces    the number of spaces to move
 */
public record Movement(Direction direction, int spaces) {

    private static final Pattern MOVEMENT_PATTERN = Pattern.compile("(\\w+) (\\d+)");

    /**
     * Creates a {@link Movement} from a {@link CharSequence} in the format:
     * <pre>
     *     [direction] [spaces]
     * </pre>
     *
     * @param input the {@link CharSequence} to parse
     * @return the {@link Movement}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static Movement parse(final CharSequence input) {
        final Matcher matcher = MOVEMENT_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + input);
        }

        final Direction direction = Direction.get(matcher.group(1));
        final int spaces = Integer.parseInt(matcher.group(2));
        return new Movement(direction, spaces);
    }
}
