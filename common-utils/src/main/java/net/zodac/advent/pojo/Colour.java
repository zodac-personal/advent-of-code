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
 * Enum defining a colour.
 */
public enum Colour {

    /**
     * The colour <b>blue</b>.
     */
    BLUE,

    /**
     * The colour <b>green</b>.
     */
    GREEN,

    /**
     * The colour <b>red</b>.
     */
    RED;

    /**
     * Utility method to retrieve an instance of {@link Colour} based on the input {@link String}.
     *
     * @param input the representation of a {@link Colour} as a {@link String}
     * @return the matching {@link Colour}
     * @throws IllegalArgumentException thrown if there is no valid {@link Colour} match for the provided {@code input}
     */
    public static Colour get(final String input) {
        return Arrays.stream(values())
            .filter(colour -> colour.toString().equalsIgnoreCase(input.trim()))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Unable to find %s for input '%s'", Colour.class.getSimpleName(), input)));
    }
}
