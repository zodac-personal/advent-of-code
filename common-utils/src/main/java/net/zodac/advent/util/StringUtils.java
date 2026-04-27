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

package net.zodac.advent.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * Utility class with {@link String}-based functions.
 */
public final class StringUtils {

    private static final Comparator<Long> IN_ASCENDING_ORDER_OF_LONGS = Long::compare;
    private static final Comparator<Long> IN_DESCENDING_ORDER_OF_LONGS = IN_ASCENDING_ORDER_OF_LONGS.reversed();

    /**
     * Pattern defining a valid positive or negative number.
     */
    public static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\b(\\d+)\\b");

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\r?\\n");
    private static final Pattern FULLY_UPPERCASE_WORDS_PATTERN = Pattern.compile("(\\b[A-Z][A-Z]+\\b)");
    private static final Set<Character> VOWELS = Set.of('a', 'e', 'i', 'o', 'u');
    private static final String EMPTY_STRING = "";
    private static final int LENGTH_OF_SINGLE_CHARACTER = 1;

    private StringUtils() {

    }

    /**
     * Bisects the given {@link String} and returns both halves as a {@link Pair} of {@link String}s.
     *
     * @param input the {@link String} to split in half
     * @return the {@link String} halves as a {@link Pair}
     * @throws IllegalArgumentException thrown if the input {@link String} does not have an even length
     */
    public static Pair<String, String> bisect(final String input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }

        if (MathUtils.isOdd(input.length())) {
            throw new IllegalArgumentException(String.format("Cannot bisect input of length: %s", input.length()));
        }

        final int middleIndex = input.length() / 2;
        return Pair.of(input.substring(0, middleIndex), input.substring(middleIndex));
    }

    /**
     * Given a {@link Collection} of {@link String}s, we can build a column containing the character at the {@code columnIndex} for all
     * {@link String}. If any of the {@link String}s do not have a character at the {@code columnIndex}, an {@link #EMPTY_STRING} will be used
     * instead.
     *
     * @param inputs      the {@link String}s
     * @param columnIndex the index of the wanted column
     * @return the column of {@link String}s
     */
    public static String buildColumn(final Collection<String> inputs, final int columnIndex) {
        if (inputs.isEmpty() || columnIndex < 0) {
            return EMPTY_STRING;
        }

        final StringBuilder columnBuilder = new StringBuilder();

        for (final String input : inputs) {
            columnBuilder.append(columnIndex >= input.length() ? EMPTY_STRING : input.charAt(columnIndex));
        }

        return columnBuilder.toString();
    }

    /**
     * Returns the frequency of the {@link Character}s in the given {@link CharSequence}. The frequencies will be sorted in descending order of the
     * counts, and not related to the placement of any {@link Character}.
     *
     * <p>
     * For example, given the input {@code "hello world"}, we would return the following frequencies:
     * <pre>
     *     [
     *      3,  // 'l'
     *      2,  // 'o'
     *      1,  // ' '
     *      1,  // 'h'
     *      1,  // 'e'
     *      1,  // 'w'
     *      1,  // 'r'
     *      1   // 'd'
     *     ]
     * </pre>
     *
     * @param input the {@link CharSequence} to check
     * @return the frequency of the {@link Character}s
     */
    public static List<Long> characterFrequencies(final CharSequence input) {
        return input.chars()
            .mapToObj(character -> (char) character)
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .values()
            .stream()
            .sorted(IN_DESCENDING_ORDER_OF_LONGS)
            .toList();
    }

    /**
     * Given a {@link CharSequence}, returns the indices where the {@code wantedCharacter} can be found.
     *
     * <p>
     * For example, given the {@link CharSequence} {@code aabcdeafgaaahiaj}, and the {@code wantedCharacter} <b>'a'</b>, the indices would be:
     * <pre>
     *     [0, 1, 6, 9, 10, 11, 14]
     * </pre>
     *
     * @param input           the {@link CharSequence} to check
     * @param wantedCharacter the character to find within the {@link CharSequence}
     * @return the indices of the {@code wantedCharacter}
     */
    public static Set<Integer> characterIndexes(final CharSequence input, final char wantedCharacter) {
        final Set<Integer> index = new HashSet<>();
        final int inputLength = input.length();

        for (int i = 0; i < inputLength; i++) {
            final char characterInInput = input.charAt(i);
            if (characterInInput == wantedCharacter) {
                index.add(i);
            }
        }

        return index;
    }

    /**
     * Parses the input {@link CharSequence} and returns any {@link Long} values (of any length) in the order provided. Assumes that each 'word'
     * within the {@code input} is a valid {@link Long}, not that each character may be a separate {@link Long}.
     *
     * <p>
     * For example, given the {@code input}:
     * <pre>
     *     123 456 7 89
     * </pre>
     *
     * <p>
     * The output would be the four groupings converted to {@link Long}s, not all 9 digits as {@link Long}s, returning:
     * <pre>
     *     [123, 456, 7, 89]
     * </pre>
     *
     * @param input the {@link CharSequence} to check
     * @return the found {@link Long}s
     */
    public static List<Long> collectNumbersInOrder(final CharSequence input) {
        final Matcher matcher = NUMBER_PATTERN.matcher(input);

        final List<Long> numbers = new ArrayList<>();
        while (matcher.find()) {
            final String value = matcher.group();
            if (NumberUtils.isLong(value)) {
                numbers.add(Long.parseLong(value));
            }
        }

        return numbers;
    }

    /**
     * Compares the provided {@link String}s and returns the common {@link Character}s in all {@link String}s.
     *
     * @param first  the first {@link String}
     * @param others any additional {@link String}s
     * @return a {@link Set} of the common {@link Character}s in all {@link String}s
     * @throws IllegalArgumentException thrown if any input {@link String} is blank, or no vararg {@link String}s are provided
     */
    public static Set<Character> commonChars(final String first, final String... others) {
        if (others.length == 0) {
            throw new IllegalArgumentException("Must have at least two strings to compare");
        }

        if (first.isBlank()) {
            throw new IllegalArgumentException("Input cannot be blank");
        }

        final Collection<Set<Character>> allOtherChars = new HashSet<>();
        for (final String other : others) {
            if (other.isBlank()) {
                throw new IllegalArgumentException("Input cannot be blank");
            }

            final Set<Character> otherChars = new HashSet<>();
            for (final char otherChar : other.toCharArray()) {
                otherChars.add(otherChar);
            }
            allOtherChars.add(otherChars);
        }

        final Set<Character> firstChars = new HashSet<>();
        for (final char firstChar : first.toCharArray()) {
            firstChars.add(firstChar);
        }

        for (final Set<Character> otherChars : allOtherChars) {
            firstChars.retainAll(otherChars);
        }

        return firstChars;
    }

    /**
     * Checks if the characters in the provided {@code input} is a superset for all provided {@code subStrings}.
     *
     * @param input      the {@link String} that should contain all characters in all {@code subStrings}
     * @param subStrings the {@link String}s that should be subsets of {@code input}, at least 1 must be provided
     * @return {@code false} if any {@code subStrings} is not in the {@code input}, or if no {@code varargs} are supplied
     */
    public static boolean containsAllCharacters(final String input, final String... subStrings) {
        if (subStrings.length == 0) {
            return false;
        }

        for (final String subString : subStrings) {
            for (final char charToCheck : subString.toCharArray()) {
                if (!input.contains(Character.toString(charToCheck))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the provided {@code subStrings} are indeed contained in the provided {@code superString}.
     *
     * @param input      the {@link String} that should contain all characters in all {@code subStrings}
     * @param subStrings the {@link String}s that should be subsets of {@code superString}
     * @return {@code true} if any {@code subString} in contained in the {@code superString}
     */
    public static boolean containsAny(final String input, final String... subStrings) {
        for (final String subString : subStrings) {
            if (input.contains(subString)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the input {@link String} has any duplicate characters.
     *
     * @param input the input {@link String} to check
     * @return {@code true} if the {@link String} contains at least one duplicate character
     */
    public static boolean containsDuplicates(final CharSequence input) {
        return input.length() != input.chars().distinct().count();
    }

    /**
     * Counts the number of vowel characters ('a', 'e', 'i', 'o', 'u') in the input {@link String}.
     *
     * @param input the {@link String} to check
     * @return the number of vowels in the {@link String}
     */
    public static long countVowels(final String input) {
        if (input.isBlank()) {
            return 0L;
        }

        int numberOfVowels = 0;
        for (final char c : input.toLowerCase(Locale.UK).toCharArray()) {
            if (VOWELS.contains(c)) {
                numberOfVowels++;
            }
        }

        return numberOfVowels;
    }

    /**
     * Returns the first fully uppercase word (a substring surrounded by whitespace) in the input {@link String}.
     *
     * @param input the {@link String} to check
     * @return the first fully uppercase word
     * @throws IllegalArgumentException thrown if the {@code input} has no uppercase word
     */
    public static Optional<String> findFirstFullyUpperCaseWord(final String input) {
        if (input.isBlank()) {
            return Optional.empty();
        }

        final Matcher matcher = FULLY_UPPERCASE_WORDS_PATTERN.matcher(input);

        if (!matcher.find()) {
            return Optional.empty();
        }

        return Optional.of(matcher.group());
    }

    /**
     * Checks if the provided {@link String} has at least one character repeated, in order. For example, <b>aabc</b> will return {@code true},
     * while <b>abac</b> will return {@code false}.
     *
     * @param input the {@link String} to check
     * @return {@code true} if there is at least one character repeated in order
     */
    public static boolean hasRepeatedCharacterInOrder(final String input) {
        if (input.isBlank()) {
            return false;
        }

        final int stringLength = input.length();
        for (int i = 0; i < stringLength - 1; i++) {
            if (input.charAt(i) == input.charAt(i + 1)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks the {@code input} {@link String} for the most common {@link Character}.
     *
     * <p>
     * For example, given the input {@code "hello world"}, the most common {@link Character} is <b>'l'</b>.
     *
     * <p>
     * If there are multiple {@link Character}s which are equally the most common, the first occurring character is returned.
     *
     * @param input the input {@link String}
     * @return the most commonly occurring {@link Character} in the {@code input}
     * @throws IllegalArgumentException if the {@code input} {@link String#isBlank()}
     */
    public static char mostOccurringCharacter(final String input) {
        if (input.isBlank()) {
            throw new IllegalArgumentException("Input cannot be blank");
        }

        return input.chars()
            .mapToObj(x -> (char) x)
            .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .orElseThrow(() -> new IllegalArgumentException("Input cannot be blank"))
            .getKey();
    }

    /**
     * Iterates through both input {@link String}s index by index, and if the character at any index is different, that character is removed.
     *
     * <p>
     * For example, given {@code foobar} and {@code fuubar}, the returned value will be:
     * <pre>
     *   {@code fbar}
     * </pre>
     *
     * @param first  the first {@link String}
     * @param second the second {@link String}
     * @return the {@link String} with any differences removed
     * @throws IllegalArgumentException thrown if the two input {@link String}s do not have the same length
     */
    public static String removeDifferentCharacters(final String first, final String second) {
        if (first.length() != second.length()) {
            throw new IllegalArgumentException(
                String.format("Expected inputs of equal length, found %s (%s) and %s (%s)", first, first.length(), second, second.length()));
        }

        final int stringLength = first.length();

        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            final char charFromFirst = first.charAt(i);
            final char charFromSecond = second.charAt(i);

            if (charFromFirst == charFromSecond) {
                stringBuilder.append(charFromFirst);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Removes the last character in the {@link String}.
     *
     * <p>
     * For example, given {@code foobar}, the returned value will be:
     * <pre>
     *   {@code fooba}
     * </pre>
     *
     * @param input the input {@link String}
     * @return the updated {@link String}
     */
    public static String removeLastCharacter(final String input) {
        return removeLastCharacters(input, 1);
    }

    /**
     * Removes the last {@code numberOfCharactersToRemove} characters in the {@link String}.
     *
     * <p>
     * For example, given {@code foobar} and {@code numberOfCharactersToRemove} set to <b>3</b>, the returned value will be:
     * <pre>
     *   {@code foo}
     * </pre>
     *
     * @param input                      the input {@link String}
     * @param numberOfCharactersToRemove the number of characters to remove
     * @return the updated {@link String}
     * @throws IllegalArgumentException if the {@code numberOfCharactersToRemove} is less than 1, or is greater than the length of the {@code input}
     */
    public static String removeLastCharacters(final String input, final int numberOfCharactersToRemove) {
        if (input.isEmpty()) {
            return EMPTY_STRING;
        }

        if (numberOfCharactersToRemove < LENGTH_OF_SINGLE_CHARACTER) {
            throw new IllegalArgumentException("Must remove at least 1 character, found: " + numberOfCharactersToRemove);
        }

        if (numberOfCharactersToRemove > input.length()) {
            throw new IllegalArgumentException(
                String.format("Cannot remove %s characters from input of length: %s", numberOfCharactersToRemove, input.length()));
        }

        return input.substring(0, input.length() - numberOfCharactersToRemove);
    }

    /**
     * Replaces a sub-string in the input {@link String}, where the position of the sub-string is known. This is useful for the scenario where we only
     * want to replace a specific sub-string where multiples of that sub-string may exist in the input.
     *
     * @param input            the input {@link String}
     * @param subString        the sub-string to find
     * @param replacement      the sub-string replacement
     * @param indexOfSubString the known index of the sub-string
     * @return the replaced {@link String}
     * @throws IllegalArgumentException thrown if the index is invalid or the sub-string does not exist at that index
     */
    public static String replaceAtIndex(final String input, final CharSequence subString, final String replacement, final int indexOfSubString) {
        if (input.isBlank()) {
            return EMPTY_STRING;
        }

        final int maxIndexPosition = input.length() - subString.length();
        if (indexOfSubString < 0 || indexOfSubString > maxIndexPosition) {
            throw new IllegalArgumentException(String.format("Expected index to be between 0 and %s, found: %s", maxIndexPosition, indexOfSubString));
        }

        final String actualSubString = input.substring(indexOfSubString, indexOfSubString + subString.length());
        if (!actualSubString.contentEquals(subString)) {
            throw new IllegalArgumentException(
                String.format("Expected to find subString '%s' at index %s, instead found: '%s'", subString, indexOfSubString, actualSubString));
        }

        return input.substring(0, indexOfSubString) + replacement + input.substring(indexOfSubString + subString.length());
    }

    /**
     * Sorts the individual characters in the given {@link String} alphabetically.
     *
     * <p>
     * For example, given {@code foobar}, the returned value will be:
     * <pre>
     *   {@code abfoor}
     * </pre>
     *
     * @param input the {@link String} to sort
     * @return the sorted {@link String}
     */
    public static String sort(final String input) {
        final char[] chars = input.toCharArray();
        Arrays.sort(chars);
        return String.copyValueOf(chars);
    }

    /**
     * Splits the given {@link String} on any new lines.
     *
     * @param input the {@link String} to split
     * @return the array of split {@link String}s
     */
    public static String[] splitOnNewLines(final CharSequence input) {
        return NEW_LINE_PATTERN.split(input);
    }

    /**
     * Splits the given {@link String} on any whitespaces. Will {@link String#trim()} before splitting.
     *
     * @param input the {@link String} to split
     * @return the array of split {@link String}s
     */
    public static String[] splitOnWhitespace(final String input) {
        return WHITESPACE_PATTERN.split(input.trim());
    }

    /**
     * Converts the given {@link CharSequence} into a {@link List} of {@link Character}s.
     *
     * @param input the {@link CharSequence} to convert
     * @return the {@link List} of {@link Character}s
     */
    public static List<Character> toCharacterList(final CharSequence input) {
        return input
            .chars()
            .mapToObj(e -> (char) e)
            .toList();
    }
}
