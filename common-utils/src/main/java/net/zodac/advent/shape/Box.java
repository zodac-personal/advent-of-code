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

package net.zodac.advent.shape;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A perfect rectangular prism, describing a box.
 *
 * @param length the length of the {@link Box}
 * @param width  the width of the {@link Box}
 * @param height the height of the {@link Box}
 */
public record Box(int length, int width, int height) {

    private static final Pattern DIMENSIONS_PATTERN = Pattern.compile("(\\d+)x(\\d+)x(\\d+)");

    /**
     * Creates a {@link Box} from a {@link CharSequence} in the format:
     * <pre>
     *     [length]x[width]x[height]
     * </pre>
     *
     *@param input the {@link CharSequence} to parse
     *@return the {@link Box}
     * @throws IllegalArgumentException thrown if the input does not match the expected format
     */
    public static Box parse(final CharSequence input) {
        final Matcher matcher = DIMENSIONS_PATTERN.matcher(input);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to find match in input: " + input);
        }

        final int length = Integer.parseInt(matcher.group(1));
        final int width = Integer.parseInt(matcher.group(2));
        final int height = Integer.parseInt(matcher.group(3));

        return new Box(length, width, height);
    }

    /**
     * Calculates the surface area of a rectangular prism:
     * <pre>
     *     (2xLength*Width) + (2xWidth*Height) + (2xHeight*Length)
     * </pre>
     *
     * @return the surface area of the {@link Box}
     */
    public long surfaceArea() {
        return (2L * length * width) + (2L * width * height) + (2L * height * length);
    }

    /**
     * Calculates the area of only the smallest side of the {@link Box}.
     *
     * @return the area of the smallest side
     */
    public long areaOfSmallestSide() {
        final int sideLengthWidth = length * width;
        final int sideWidthHeight = width * height;
        final int sideHeightLength = height * length;

        return (sideLengthWidth < sideWidthHeight)
            ? Math.min(sideHeightLength, sideLengthWidth)
            : Math.min(sideWidthHeight, sideHeightLength);
    }

    /**
     * Calculates the smallest perimeter around the {@link Box}, across the smallest two sides.
     *
     * @return the smallest perimeter
     */
    public long smallestPerimeter() {
        if (length < width) {
            return (height < width) ? 2L * (length + height) : 2L * (length + width);
        } else {
            return (height < length) ? 2L * (width + height) : 2L * (length + width);
        }
    }

    /**
     * The product of the {@code length}, {@code width}, {@code height}.
     *
     * @return the product of the {@link Box} dimensions
     */
    public long productOfDimensions() {
        return (long) length * width * height;
    }
}
