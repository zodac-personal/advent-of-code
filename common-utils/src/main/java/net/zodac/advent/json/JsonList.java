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

package net.zodac.advent.json;

import java.util.List;

/**
 * Implementation of {@link JsonElement} defining a {@link List} of {@link JsonElement}s.
 *
 * @param elements the {@link JsonElement}s
 */
public record JsonList(List<JsonElement> elements) implements JsonElement {

    /**
     * Creates a {@link JsonList} from the provided {@link JsonElement}s.
     *
     * @param elements the {@link JsonElement}s
     * @return the created {@link JsonList}
     */
    public static JsonList create(final List<JsonElement> elements) {
        return new JsonList(elements);
    }

    /**
     * Creates a {@link JsonList} from the provided {@link JsonElement}, which will be wrapped in a {@link List}.
     *
     * @param element the {@link JsonElement}
     * @return the created {@link JsonList}
     */
    public static JsonList create(final JsonElement element) {
        return create(List.of(element));
    }

    @Override
    public int compareTo(final JsonElement other) {
        if (other instanceof JsonList(final List<JsonElement> otherJsonListElements)) {
            final int thisSize = elements.size();
            final int otherSize = otherJsonListElements.size();
            final int maxSize = Math.max(thisSize, otherSize);

            for (int i = 0; i < maxSize; ++i) {
                if (i == thisSize) {
                    return -1;
                }

                if (i == otherSize) {
                    return 1;
                }

                final int comparison = elements.get(i).compareTo(otherJsonListElements.get(i));
                if (comparison != 0) {
                    return comparison;
                }
            }

            return 0;
        }

        if (other instanceof final JsonInteger jsonInteger) {
            final JsonList wrappedAsList = create(jsonInteger);
            return compareTo(wrappedAsList);
        }

        throw new IllegalArgumentException(
            String.format("Cannot compare %s to %s", JsonList.class.getSimpleName(), other.getClass().getSimpleName()));
    }
}
