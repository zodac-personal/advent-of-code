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

import net.zodac.advent.util.StringUtils;

/**
 * POJO defining a route between a source and a target, and the value of the distance between them.
 *
 * @param from  the start of the {@link Route}
 * @param to    the end of the {@link Route}
 * @param value the value of the distance between the start and end of the {@link Route}
 */
public record Route(String from, String to, int value) {

    /**
     * Creates a {@link Route}.
     *
     * @param from  the start of the {@link Route}
     * @param to    the end of the {@link Route}
     * @param value the value of the distance between the start and end of the {@link Route}
     * @return the created {@link Route}
     */
    public static Route create(final String from, final String to, final int value) {
        return new Route(from, to, value);
    }

    /**
     * Creates a {@link Route} from a {@link String} in the format:
     * <pre>
     *     [source] to [destination] = [value]
     * </pre>
     *
     * @param input the {@link String} to parse
     * @return the {@link Route}
     */
    public static Route parseSourceDestination(final String input) {
        final String[] tokens = StringUtils.splitOnWhitespace(input);

        final String source = tokens[0];
        final String destination = tokens[2];
        final int value = Integer.parseInt(tokens[4]);

        return create(source, destination, value);
    }

    /**
     * Creates a {@link Route} from a {@link String} in the format:
     * <pre>
     *     [source] would gain/lose [value] happiness units by sitting next to [destination].
     * </pre>
     *
     * @param input the {@link String} to parse
     * @return the {@link Route}
     */
    public static Route parseSittingNextTo(final String input) {
        final String[] tokens = StringUtils.splitOnWhitespace(input);

        final String source = tokens[0];
        final String destination = StringUtils.removeLastCharacter(tokens[10]);
        final int routeValue = Integer.parseInt(tokens[3]);
        final int sign = "gain".equals(tokens[2]) ? 1 : -1;
        final int value = sign * routeValue;

        return create(source, destination, value);
    }
}
