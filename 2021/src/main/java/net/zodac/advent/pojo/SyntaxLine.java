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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * POJO defining a line of syntax characters.
 *
 * @param line the input {@link String} to create into a {@link SyntaxLine}
 */
public record SyntaxLine(String line) {

    private static final Pattern VALID_SYNTAX_LINE = Pattern.compile("[()\\[\\]{}<>]+");
    private static final Set<Character> VALID_OPEN_SYMBOLS = Set.of('(', '[', '{', '<');
    private static final Set<Character> VALID_CLOSE_SYMBOLS = Set.of(')', ']', '}', '>');
    private static final Map<Character, Character> CLOSE_SYMBOL_FOR_OPEN_SYMBOL = Map.of(
        '(', ')',
        '[', ']',
        '{', '}',
        '<', '>'
    );

    /**
     * Takes the input {@link String} and creates a {@link SyntaxLine}. The only characters permitted are:
     * <ul>
     *     <li>(</li>
     *     <li>}</li>
     *     <li>[</li>
     *     <li>]</li>
     *     <li>{</li>
     *     <li>}</li>
     *     <li>{@literal <}</li>
     *     <li>{@literal >}</li>
     * </ul>
     *
     * @param inputLine the input {@link String} to create into a {@link SyntaxLine}
     * @return the created {@link SyntaxLine}
     * @throws IllegalArgumentException thrown if the input {@link String} contains an invalid character
     */
    public static SyntaxLine create(final String inputLine) {
        if (!VALID_SYNTAX_LINE.matcher(inputLine).matches()) {
            throw new IllegalArgumentException(String.format("Invalid input: '%s'", inputLine));
        }

        return new SyntaxLine(inputLine);
    }

    /**
     * Evaluates the {@link SyntaxLine} for validity. An invalid {@link SyntaxLine} will have a close symbol appear in order before another expected
     * close symbol. For example, given the input:
     * <pre>
     *     [[(({{)
     * </pre>
     *
     * <p>
     * We would expect the first close symbol to appear to be <b>}</b>, as <b>{</b> was the last open symbol listed. Instead, we find <b>)</b>. Note
     * that a {@link SyntaxLine} that has no invalid close symbols may be incomplete, but is considered valid.
     *
     * <p>
     * Based on whether there is an invalid close symbol, we can return one of two {@link Pair}s:
     * <ul>
     *     <li>For an invalid {@link SyntaxLine}, a {@link Pair} of the first invalid close symbol and an empty {@link Deque}</li>
     *     <li>For a valid {@link SyntaxLine}, a {@link Pair} of the <b>'0'</b> character and a {@link Deque} of the remaining open characters</li>
     * </ul>
     *
     * @return the {@link Pair} for either invalid or valid {@link SyntaxLine}
     */
    public Pair<Character, Deque<Character>> evaluateSyntaxErrorScoreForLine() {
        final Deque<Character> openSymbols = new ArrayDeque<>();

        for (final char syntaxSymbol : symbols()) {
            if (isValidOpenSymbol(syntaxSymbol)) {
                openSymbols.push(syntaxSymbol);
            } else if (isValidCloseSymbol(syntaxSymbol)) {
                final char nextOpenSymbol = openSymbols.pop();
                final char expectedCloseSymbol = getMatchingCloseSymbol(nextOpenSymbol);

                if (expectedCloseSymbol != syntaxSymbol) {
                    return Pair.of(syntaxSymbol, new ArrayDeque<>());
                }
            }
        }

        return Pair.of('0', openSymbols);
    }

    /**
     * Returns the symbols in the {@link SyntaxLine}.
     *
     * @return the {@link SyntaxLine} symbols as a {@code char[]}
     */
    public char[] symbols() {
        return line.toCharArray();
    }

    /**
     * Checks if the provided input symbol is a valid opening symbol.
     *
     * @param inputSymbol the symbol to check
     * @return {@code true} if the input is a valid opening symbol
     */
    public static boolean isValidOpenSymbol(final char inputSymbol) {
        return VALID_OPEN_SYMBOLS.contains(inputSymbol);
    }

    /**
     * Checks if the provided input symbol is a valid close symbol.
     *
     * @param inputSymbol the symbol to check
     * @return {@code true} if the input is a valid close symbol
     */
    public static boolean isValidCloseSymbol(final char inputSymbol) {
        return VALID_CLOSE_SYMBOLS.contains(inputSymbol);
    }

    /**
     * Gets the matching close symbol for the provided open symbol.
     *
     * @param openSymbol the symbol for which the close symbol should be found
     * @return the matching close symbol
     * @throws IllegalArgumentException thrown if the input open symbol has no matching close symbol
     */
    public static char getMatchingCloseSymbol(final char openSymbol) {
        if (!CLOSE_SYMBOL_FOR_OPEN_SYMBOL.containsKey(openSymbol)) {
            throw new IllegalArgumentException(String.format("No close symbol found for input: '%s'", openSymbol));
        }

        // Value will be non-null since we define a static map, but adding the check so the compiler knows too
        return Objects.requireNonNull(CLOSE_SYMBOL_FOR_OPEN_SYMBOL.get(openSymbol));
    }
}
