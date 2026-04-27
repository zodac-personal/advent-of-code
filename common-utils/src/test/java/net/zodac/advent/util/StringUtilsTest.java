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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import net.zodac.advent.pojo.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Unit tests for {@link StringUtils}.
 */
class StringUtilsTest {

    @ParameterizedTest
    @CsvSource({
        "abcdef,abc,def",   // Valid
        "'    ','  ','  '", // Blank, with valid length
    })
    void testBisect(final String input, final String firstHalf, final String secondHalf) {
        final Pair<String, String> output = StringUtils.bisect(input);
        assertThat(output)
            .isEqualTo(Pair.of(firstHalf, secondHalf));
    }

    @ParameterizedTest
    @CsvSource({
        "abcdefg,Cannot bisect input of length: 7", // Invalid length
        "'     ',Cannot bisect input of length: 5", // Blank, with invalid length
        "'',Input cannot be empty",                 // Empty
    })
    void testBisect_invalid(final String input, final String errorMessage) {
        assertThatThrownBy(() -> StringUtils.bisect(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    @ParameterizedTest
    @MethodSource("provideForBuildColumn")
    void testBuildColumn(final List<String> input, final int index, final String expected) {
        final String output = StringUtils.buildColumn(input, index);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForBuildColumn() {
        return Stream.of(
            // First index
            Arguments.of(
                List.of(
                    "abc",
                    "def",
                    "ghi",
                    "jkl"
                ),
                0,
                "adgj"
            ),
            // Last index
            Arguments.of(
                List.of(
                    "abc",
                    "def",
                    "ghi",
                    "jkl"
                ),
                2,
                "cfil"
            ),
            // Middle string shorter than others
            Arguments.of(
                List.of(
                    "abc",
                    "d",
                    "ghi",
                    "jkl"
                ),
                1,
                "bhk"
            ),
            // Middle string shorter than others
            Arguments.of(
                List.of(
                    "abc",
                    "d",
                    "ghi",
                    "jkl"
                ),
                1,
                "bhk"
            ),
            // Index too small
            Arguments.of(
                List.of(
                    "abc",
                    "def",
                    "ghi",
                    "jkl"
                ),
                -1,
                ""
            ),
            // Index too large
            Arguments.of(
                List.of(
                    "abc",
                    "def",
                    "ghi",
                    "jkl"
                ),
                3,
                ""
            ),
            // Empty
            Arguments.of(List.of(), 0, "")
        );
    }

    @ParameterizedTest
    @MethodSource("provideForCharacterFrequency")
    void testCharacterFrequency(final String input, final List<Long> expected) {
        final List<Long> output = StringUtils.characterFrequencies(input);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForCharacterFrequency() {
        return Stream.of(
            Arguments.of("hello world", List.of(3L, 2L, 1L, 1L, 1L, 1L, 1L, 1L, 1L)),   // Multiple characters with varying frequencies
            Arguments.of("abcde", List.of(1L, 1L, 1L, 1L, 1L)),                         // Multiple characters with same frequency
            Arguments.of("", List.of()),                                                // Empty
            Arguments.of(" ", List.of(1L))                                           // Blank
        );
    }

    @ParameterizedTest
    @MethodSource("provideForCharacterIndexes")
    void testCharacterIndexes(final String input, final char wantedChar, final Set<Integer> expected) {
        final Set<Integer> output = StringUtils.characterIndexes(input, wantedChar);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForCharacterIndexes() {
        return Stream.of(
            Arguments.of("aabcdeafgaaahiaj", 'a', Set.of(0, 1, 6, 9, 10, 11, 14)),  // Multiple matches
            Arguments.of("aabcdeafgaaahiaj", 'f', Set.of(7)),                       // Single match
            Arguments.of("aabcdeafgaaahiaj", 'z', Set.of()),                        // No matches
            Arguments.of("", 'a', Set.of()),                                        // Empty
            Arguments.of(" ", 'a', Set.of())                                        // Blank
        );
    }

    @ParameterizedTest
    @MethodSource("provideForCollectNumbersInOrder")
    void testCollectNumbersInOrder(final String input, final List<Long> expected) {
        final List<Long> output = StringUtils.collectNumbersInOrder(input);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForCollectNumbersInOrder() {
        return Stream.of(
            Arguments.of("1", List.of(1L)),                                                         // Single number
            Arguments.of("1 23 456", List.of(1L, 23L, 456L)),                                           // Multiple numbers
            Arguments.of("1 -23 456", List.of(1L, -23L, 456L)),                                         // Negative number
            Arguments.of("1 and 23 and 456", List.of(1L, 23L, 456L)),                                   // Numbers and words
            Arguments.of("1 23 456 9999999999999999", List.of(1L, 23L, 456L, 9_999_999_999_999_999L)),  // Multiple integers and long
            Arguments.of("99999999999999999999999999999", List.of()),                           // Number too large for long
            Arguments.of("No numbers here", List.of()),                                                 // No numbers
            Arguments.of("", List.of()),                                                                // Empty
            Arguments.of(" ", List.of())                                                                // Blank
        );
    }

    @ParameterizedTest
    @MethodSource("provideForCommonChars")
    void testCommonChars(final String first, final String[] others, final Set<Character> expected) {
        final Set<Character> output = StringUtils.commonChars(first, others);
        assertThat(output)
            .hasSameElementsAs(expected);
    }

    private static Stream<Arguments> provideForCommonChars() {
        return Stream.of(
            Arguments.of("abcd", new String[] {"aefg"}, Set.of('a')),                    // Single common character, 2 inputs
            Arguments.of("abcd", new String[] {"aefg", "ahij"}, Set.of('a')),            // Single common character, 3 inputs
            Arguments.of("abcdz", new String[] {"aefgz", "ahijz"}, Set.of('a', 'z')),    // Multiple common characters
            Arguments.of("abcd", new String[] {"aefgh", "ahijk"}, Set.of('a')),          // Single common character, partial common ignored
            Arguments.of("abcdz", new String[] {"aezfagz"}, Set.of('a', 'z')),           // Duplicate common characters
            Arguments.of("abc", new String[] {"def", "ghi"}, Set.of())                   // No common characters
        );
    }

    @ParameterizedTest
    @MethodSource("provideForCommonChars_invalid")
    void testCommonChars_givenInvalidInputs(final String first, final String[] others, final String errorMessage) {
        assertThatThrownBy(() -> StringUtils.commonChars(first, others))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    private static Stream<Arguments> provideForCommonChars_invalid() {
        return Stream.of(
            Arguments.of("abcd", new String[] {}, "Must have at least two strings to compare"),     // One input
            Arguments.of("", new String[] {"def", "ghi", "jkl"}, "Input cannot be blank"),          // Empty first
            Arguments.of(" ", new String[] {"def", "ghi", "jkl"}, "Input cannot be blank"),         // Blank first
            Arguments.of("abc", new String[] {"def", "ghi", "", "jkl"}, "Input cannot be blank"),   // Empty other
            Arguments.of("abc", new String[] {"def", "ghi", " ", "jkl"}, "Input cannot be blank")   // Blank other
        );
    }

    @ParameterizedTest
    @MethodSource("provideForContainsAll")
    void testContainsAll(final String input, final String[] subStrings, final boolean expected) {
        final boolean output = StringUtils.containsAllCharacters(input, subStrings);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForContainsAll() {
        return Stream.of(
            Arguments.of("abcdef", new String[] {"abc"}, true),                 // 1 subString, exists in input
            Arguments.of("abcd", new String[] {"abc", "bcd"}, true),            // 2 subStrings, exist in input
            Arguments.of("abcd", new String[] {"abc", "bcd", "def"}, false),    // 3 subStrings, only 2 exist in input
            Arguments.of("abcdef", new String[] {"abcdef"}, true),              // subString matches input
            Arguments.of("abcdef", new String[] {"abcdefghij"}, false),         // Input is subString of subString
            Arguments.of("abcdef", new String[] {}, false),                     // No subString provided
            Arguments.of("abcdef", new String[] {""}, true),                    // Empty subString
            Arguments.of("abcdef", new String[] {" "}, false),                  // Blank subString
            Arguments.of("", new String[] {"abc"}, false),                      // Empty input
            Arguments.of(" ", new String[] {"abc"}, false)                      // Blank input
        );
    }

    @ParameterizedTest
    @MethodSource("provideForContainsAny")
    void testContainsAny(final String input, final String[] subStrings, final boolean expected) {
        final boolean output = StringUtils.containsAny(input, subStrings);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForContainsAny() {
        return Stream.of(
            Arguments.of("abcdef", new String[] {"abc"}, true),                         // 1 subString, exists in input
            Arguments.of("abcdef", new String[] {"abc", "cde", "efg"}, true),           // 3 subStrings, only 2 exist in input
            Arguments.of("abcdef", new String[] {"abc", "ghi", "jkl", "mno"}, true),    // 4 subStrings, only 1 exists in input
            Arguments.of("abcdef", new String[] {"ghi"}, false),                        // 1 subString, does not exist in input
            Arguments.of("abcdef", new String[] {"ghi", "jkl", "mno"}, false),          // 3 subString, none exist in input
            Arguments.of("abcdef", new String[] {"abcdef"}, true),                      // subString matches input
            Arguments.of("abcdef", new String[] {"abcdefghij"}, false),                 // Input is subString of subString
            Arguments.of("abcdef", new String[] {}, false),                             // No subString provided
            Arguments.of("abcdef", new String[] {""}, true),                            // Empty subString
            Arguments.of("abcdef", new String[] {" "}, false),                          // Blank subString
            Arguments.of("", new String[] {"abc"}, false),                              // Empty input
            Arguments.of(" ", new String[] {"abc"}, false)                              // Blank input
        );
    }

    @ParameterizedTest
    @CsvSource({
        "abca,true",    // Duplicates
        "abc,false",    // No duplicates
        "'',false",     // Empty
        "' ',false",    // Blank
    })
    void whenContainsDuplicates_givenString_thenCorrectValueIsReturned(final String input, final boolean expected) {
        final boolean output = StringUtils.containsDuplicates(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "bcdf,0",       // No vowels
        "abcdef,2",     // Multiple vowels
        "aabcdeef,4",   // Repeated vowels
        "'',0",         // Empty
        "' ',0",        // Blank
    })
    void testCountVowels(final String input, final long expected) {
        final long output = StringUtils.countVowels(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "THIS is uppercase,THIS",       // Input has single uppercase word
        "THIS,THIS",                    // Input is only an uppercase word
        "THIS is UPPERCASE,THIS"        // Input has multiple uppercase words
    })
    void testFindFullyFirstUpperCaseWord(final String input, final String expected) {
        final Optional<String> output = StringUtils.findFirstFullyUpperCaseWord(input);

        assertThat(output)
            .isPresent()
            .hasValue(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "this is not uppercase",    // Input has no uppercase words
        "This Is Not Uppercase",    // Input has uppercase characters but not a full uppercase word
        "''",                       // Empty
        "' '",                      // Blank
    })
    void testFindFullyFirstUpperCaseWord_givenInvalidInputs(final String input) {
        final Optional<String> output = StringUtils.findFirstFullyUpperCaseWord(input);
        assertThat(output)
            .isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
        "abccdef,true",     // Single repeat
        "aabccdef,true",    // Multiple repeats of different characters
        "aabcdefaa,true",   // Multiple repeats of same characters
        "abcdefa,false",    // Single repeat, but not in order of same characters
        "abcdef,false",     // No repeat
        "'',false",         // Empty
        "' ',false",        // Blank
    })
    void testHasRepeatedCharacterInOrder(final String input, final boolean expected) {
        final boolean output = StringUtils.hasRepeatedCharacterInOrder(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "hello world,l",    // Input with single most occurring character
        "abcdef,a",         // Input with all characters equally occurring
        "abbcddef,b",       // Input with multiple most equally occurring
    })
    void testMostOccurringCharacter(final String input, final char expected) {
        final char output = StringUtils.mostOccurringCharacter(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "'',Input cannot be blank",             // Empty
        "' ',Input cannot be blank",            // Blank
    })
    void testMostOccurringCharacter_givenInvalidInputs(final String input, final String errorMessage) {
        assertThatThrownBy(() -> StringUtils.mostOccurringCharacter(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
        "abcdef,abcdef,abcdef", // No difference
        "abcdef,abcqef,abcef",  // One difference
        "abcdef,ghijkl,''",     // No matches
        "'','',''",             // Both are empty
        "' ',a,''",             // First is blank
        "a,' ',''",             // Second is blank
    })
    void testRemoveDifferentCharacters(final String first, final String second, final String expected) {
        final String output = StringUtils.removeDifferentCharacters(first, second);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    // Inputs of different length
    @CsvSource(delimiter = '|', value = "abcdef|abcdefg|Expected inputs of equal length, found abcdef (6) and abcdefg (7)")
    void testRemoveDifferentCharacters_givenInvalidInputs(final String first, final String second, final String errorMessage) {
        assertThatThrownBy(() -> StringUtils.removeDifferentCharacters(first, second))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
        "abc,ab",   // Normal input
        "a,''",     // Single character
        "'  ',' '", // Multiple blank spaces
        "'',''",    // Empty
        "' ',''",   // Blank
    })
    void testRemoveLastCharacter(final String input, final String expected) {
        final String output = StringUtils.removeLastCharacter(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "abcd,2,ab",    // String of length 4, removing 2
        "'   ',2,' '",  // String of length 3, blank spaces, removing 2
        "'',2,''",      // Empty
        "' ',1,''",     // Blank
    })
    void testRemoveLastCharacters(final String input, final int numberOfCharactersToRemove, final String expected) {
        final String output = StringUtils.removeLastCharacters(input, numberOfCharactersToRemove);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "a|2|Cannot remove 2 characters from input of length: 1",  // Removing too many characters
        "abcd|-1|Must remove at least 1 character, found: -1",     // Removing negative characters
    })
    void testRemoveLastCharacters_givenInvalidInputs(final String input, final int numberOfCharactersToRemove, final String errorMessage) {
        assertThatThrownBy(() -> StringUtils.removeLastCharacters(input, numberOfCharactersToRemove))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
        "abcdef,def,xxx,3,abcxxx",                      // Single matching substring
        "abcdefghidefjkl,def,xxx,3,abcxxxghidefjkl",    // Multiple matching substrings
        "abcdefghidefjkl,def,xxx,9,abcdefghixxxjkl",    // Multiple matching substrings (other index)
        "'',def,xxx,3,''",                              // Empty
        "' ',def,xxx,3,''",                             // Blank
    })
    void testReplaceAtIndex(final String input, final String subString, final String replacement, final int indexOfSubString, final String expected) {
        final String output = StringUtils.replaceAtIndex(input, subString, replacement, indexOfSubString);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "abcdefghi|def|-1|Expected index to be between 0 and 6, found: -1",                     // Negative index
        "abcdefghi|def|7|Expected index to be between 0 and 6, found: 7",                       // Out of bounds index
        "abcdefghi|def|0|Expected to find subString 'def' at index 0, instead found: 'abc'",    // Incorrect index
        "abcdefghi|yyy|3|Expected to find subString 'yyy' at index 3, instead found: 'def'",    // Substring does not exist
    })
    void testReplaceAtIndex_givenInvalidInputs(final String input, final String subString, final int indexOfSubString, final String errorMessage) {
        assertThatThrownBy(() -> StringUtils.replaceAtIndex(input, subString, "xxx", indexOfSubString))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(errorMessage);
    }

    @ParameterizedTest
    @CsvSource({
        "fedcba,abcdef",    // Unsorted string
        "abcdef,abcdef",    // Sorted string
        "'',''",            // Empty
        "' ',' '",          // Blank
    })
    void testSort(final String input, final String expected) {
        final String output = StringUtils.sort(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("provideForSplitOnNewLines")
    void testSplitOnNewLines(final String input, final String[] expected) {
        final String[] output = StringUtils.splitOnNewLines(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForSplitOnNewLines() {
        return Stream.of(
            // Input has multiple lines
            Arguments.of("""        
                line1
                line2
                line3""", new String[] {"line1", "line2", "line3"}),
            Arguments.of("line1", new String[] {"line1"}),              // Input has single line
            Arguments.of("line1 line2", new String[] {"line1 line2"}),  // Input has single line with whitespaces
            Arguments.of("", new String[] {""}),                        // Empty
            Arguments.of(" ", new String[] {" "})                       // Blank
        );
    }

    @ParameterizedTest
    @MethodSource("provideForSplitOnWhitespace")
    void testSplitOnWhitespace(final String input, final String[] expected) {
        final String[] output = StringUtils.splitOnWhitespace(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForSplitOnWhitespace() {
        return Stream.of(
            Arguments.of("abc", new String[] {"abc"}),                                  // Input has no whitespace
            Arguments.of("abc def", new String[] {"abc", "def"}),                       // Input has single whitespace
            Arguments.of("abc   def", new String[] {"abc", "def"}),                     // Input has single whitespace with multiple spaces
            Arguments.of("a bc def ghij", new String[] {"a", "bc", "def", "ghij"}),     // Input has multiple whitespaces
            Arguments.of("", new String[] {""}),                                        // Empty
            Arguments.of(" ", new String[] {""})                                        // Blank
        );
    }

    @ParameterizedTest
    @MethodSource("provideForToCharacterList")
    void testToCharacterList(final String input, final List<Character> expected) {
        final List<Character> output = StringUtils.toCharacterList(input);
        assertThat(output)
            .isEqualTo(expected);
    }

    private static Stream<Arguments> provideForToCharacterList() {
        return Stream.of(
            Arguments.of("abc", List.of('a', 'b', 'c')),                     // Input has no whitespace
            Arguments.of("abc def", List.of('a', 'b', 'c', ' ', 'd', 'e', 'f')),    // Input has single whitespace
            Arguments.of("", List.of()),                                            // Empty
            Arguments.of(" ", List.of(' '))                                     // Blank
        );
    }

    @Test
    void testMethodsAgainstBigNaughtListOfStrings() {
        for (final String naughtyString : getBigNaughtyListOfStrings()) {
            if (!naughtyString.isBlank()) {
                StringUtils.commonChars("myTest", naughtyString);

                if (naughtyString.length() % 2 == 0) {
                    StringUtils.bisect(naughtyString);
                }
            }

            StringUtils.characterFrequencies(naughtyString);
            StringUtils.characterIndexes(naughtyString, 'a');
            StringUtils.collectNumbersInOrder(naughtyString);
            StringUtils.containsAllCharacters("myTest", naughtyString);
            StringUtils.containsAny("myTest", naughtyString);
            StringUtils.containsDuplicates(naughtyString);
            StringUtils.countVowels(naughtyString);
            StringUtils.findFirstFullyUpperCaseWord(naughtyString);
            StringUtils.hasRepeatedCharacterInOrder(naughtyString);
            StringUtils.removeLastCharacter(naughtyString);
            StringUtils.removeLastCharacters(naughtyString, 1);
            StringUtils.sort(naughtyString);
            StringUtils.splitOnNewLines(naughtyString);
            StringUtils.splitOnWhitespace(naughtyString);

            // Not testing due to how specific the methods are
            // StringUtils.removeDifferentCharacters
            // StringUtils.replaceAtIndex
        }
    }

    private static List<String> getBigNaughtyListOfStrings() {
        try {
            final Path path = Paths.get(ClassLoader.getSystemResource("blns.txt").toURI());
            return Files.readAllLines(path)
                .stream()
                .filter(s -> !s.isEmpty() && s.charAt(0) == '#' || s.isBlank())
                .toList();
        } catch (final IOException | URISyntaxException e) {
            throw new AssertionError(e);
        }
    }
}

