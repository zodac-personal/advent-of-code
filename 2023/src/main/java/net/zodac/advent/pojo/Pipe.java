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

import java.util.Arrays;

/**
 * Enum defining pipe pieces that make up a pipe loop.
 */
public enum Pipe {

    /**
     * A bottom-left corner pipe piece.
     */
    BOTTOM_LEFT_CORNER('L', 'L'),

    /**
     * A vertical pipe piece.
     */
    BOTTOM_RIGHT_CORNER('J', '┘'),

    /**
     * An empty space, signifying no pipe piece.
     */
    EMPTY('.', ' '),

    /**
     * A horizontal pipe piece.
     */
    HORIZONTAL('-', '-'),

    /**
     * The start of the pipe loop.
     */
    START('S', 'S'),

    /**
     * A top-right corner pipe piece.
     */
    TOP_RIGHT_CORNER('7', '┐'),

    /**
     * A top-left corner pipe piece.
     */
    TOP_LEFT_CORNER('F', '┌'),

    /**
     * A vertical pipe piece.
     */
    VERTICAL('|', '|');

    private final char inputValue;
    private final char outputValue;

    Pipe(final char inputValue, final char outputValue) {
        this.inputValue = inputValue;
        this.outputValue = outputValue;
    }

    /**
     * Finds a {@link Pipe} based in the input {@link Character}.
     *
     * @param input the {@link Character} to check
     * @return the matching {@link Pipe}, or {@link Pipe#EMPTY}
     */
    public static Pipe get(final Character input) {
        return Arrays.stream(values())
            .filter(pipe -> pipe.inputValue == input)
            .findAny()
            .orElse(EMPTY);
    }

    /**
     * Given two {@link Pipe}s (in order), we can determine the missing {@link Pipe} that will connect them.
     *
     * @param firstConnection  the first {@link Pipe} connection
     * @param secondConnection the second {@link Pipe} connection
     * @return the missing {@link Pipe}
     */
    public static Pipe getConnectingPipe(final Pipe firstConnection, final Pipe secondConnection) {
        if (isBottomLeft(firstConnection, secondConnection)) {
            return BOTTOM_LEFT_CORNER;
        }

        if (isBottomRight(firstConnection, secondConnection)) {
            return BOTTOM_RIGHT_CORNER;
        }

        if (isTopLeft(firstConnection, secondConnection)) {
            return TOP_LEFT_CORNER;
        }

        if (isTopRight(firstConnection, secondConnection)) {
            return TOP_RIGHT_CORNER;
        }

        if (firstConnection == HORIZONTAL && secondConnection == HORIZONTAL) {
            return HORIZONTAL;
        }

        if (firstConnection == VERTICAL && secondConnection == VERTICAL) {
            return VERTICAL;
        }

        throw new IllegalStateException(
            String.format("Unable to find replacement for connections: '%s' and '%s'", firstConnection, secondConnection));
    }

    private static boolean isBottomRight(final Pipe firstConnection, final Pipe secondConnection) {
        return (firstConnection == TOP_LEFT_CORNER || firstConnection == HORIZONTAL || firstConnection == BOTTOM_LEFT_CORNER)
            && (secondConnection == BOTTOM_RIGHT_CORNER || secondConnection == VERTICAL || secondConnection == BOTTOM_LEFT_CORNER);
    }

    private static boolean isTopRight(final Pipe firstConnection, final Pipe secondConnection) {
        return (firstConnection == VERTICAL || firstConnection == TOP_RIGHT_CORNER || firstConnection == TOP_LEFT_CORNER)
            && (secondConnection == TOP_LEFT_CORNER || secondConnection == HORIZONTAL || secondConnection == BOTTOM_RIGHT_CORNER);
    }

    private static boolean isTopLeft(final Pipe firstConnection, final Pipe secondConnection) {
        return (firstConnection == BOTTOM_RIGHT_CORNER || firstConnection == TOP_RIGHT_CORNER || firstConnection == HORIZONTAL)
            && (secondConnection == VERTICAL || secondConnection == TOP_LEFT_CORNER || secondConnection == TOP_RIGHT_CORNER);
    }

    private static boolean isBottomLeft(final Pipe firstConnection, final Pipe secondConnection) {
        return (firstConnection == TOP_RIGHT_CORNER || firstConnection == HORIZONTAL || firstConnection == BOTTOM_LEFT_CORNER)
            && (secondConnection == TOP_RIGHT_CORNER || secondConnection == VERTICAL || secondConnection == TOP_LEFT_CORNER);
    }

    @Override
    public String toString() {
        return String.valueOf(outputValue);
    }
}
