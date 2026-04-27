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

package net.zodac.advent.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * POJO defining a simultaneous equation - a pair of equations in the format:
 * <pre>
 *     2x + 4y = 14
 *     4x − 4y =  4
 *     ------------
 *     x = 3, y = 2
 * </pre>
 *
 * <p>
 * We can use <a href="https://en.wikipedia.org/wiki/Cramer%27s_rule">Cramer's Rule</a> to determine the correct values for {@code x} and {@code y}
 * that satisfies both equations.
 *
 * @param x1 {@code x} value of the first equation
 * @param y1 {@code y} value of the first equation
 * @param c1 constant/result value of the first equation
 * @param x2 {@code x} value of the second equation
 * @param y2 {@code y} value of the second equation
 * @param c2 constant/result value of the second equation
 */
public record SimultaneousEquation(long x1, long y1, long c1, long x2, long y2, long c2) {

    /**
     * Solves the {@link SimultaneousEquation}, returning a {@link Pair} containing the unique values of {@code x} and {@code y}. If no unique values
     * exist for the solution, or the values are not {@link Integer}s, an {@link Optional#empty()} is returned instead.
     *
     * @return an {@link Optional} wrapping the {@link Pair} of {@code x} and {@code y} values, or {@link Optional#empty()}
     */
    public Optional<Pair<Long, Long>> solve() {
        final BigDecimal determinant = BigDecimal.valueOf((x1 * y2) - (x2 * y1));
        if (determinant.compareTo(BigDecimal.ZERO) == 0) {
            // The equations have no unique solution, as the determinant is != 0
            return Optional.empty();
        }

        final BigDecimal xNumeratorPart1 = BigDecimal.valueOf(c1).multiply(BigDecimal.valueOf(y2));
        final BigDecimal xNumeratorPart2 = BigDecimal.valueOf(c2).multiply(BigDecimal.valueOf(y1));
        final BigDecimal xNumerator = xNumeratorPart1.subtract(xNumeratorPart2);
        final BigDecimal x = xNumerator.divide(determinant, MathContext.DECIMAL128);

        final BigDecimal yNumeratorPart1 = BigDecimal.valueOf(x1).multiply(BigDecimal.valueOf(c2));
        final BigDecimal yNumeratorPart2 = BigDecimal.valueOf(x2).multiply(BigDecimal.valueOf(c1));
        final BigDecimal yNumerator = yNumeratorPart1.subtract(yNumeratorPart2);
        final BigDecimal y = yNumerator.divide(determinant, MathContext.DECIMAL128);

        if (x.scale() > 0 || y.scale() > 0) {
            // The x/y values are not exact longs
            return Optional.empty();
        }

        return Optional.of(Pair.of(x.longValue(), y.longValue()));
    }
}
