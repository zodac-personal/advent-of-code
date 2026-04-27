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

package net.zodac.advent.input;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.GridFactory;
import net.zodac.advent.util.StringUtils;

/**
 * Utility class used to build functions to read input files. The input files contains {@link String}s, which may be converted to other types based on
 * the functions chosen. There is support for both example input files (visible in the {@code src/main/resources} directory) and real puzzle input
 * files (hidden in a private git submodule in the {@value #INPUTS_DIRECTORY_NAME} directory).
 *
 * <p>
 * This utility class is made up of multiple inner classes, to allow for code-completion as object types are updated, or the output is limited.
 *
 * <p>
 * Some examples of how to use this class are:
 *
 * <p>
 * - To read an example input as a {@link Character} {@link Grid}:
 *
 * <p>
 * {@snippet :
 *     final Grid<Character> grid = forExample(inputFilePath)
 *         .asGrid()
 *         .ofCharacters();
 *}
 *
 * <p>
 * - To read a puzzle input where each line is an {@link Integer}, excluding lines where the value is <b>9</b>:
 *
 * <p>
 * {@snippet :
 *     final Grid<Character> grid = forPuzzle(inputFilePath)
 *         .asIntegers()
 *         .filter(i -> i != 9)
 *         .readAllLines();
 *}
 */
public final class InputReader {

    private static final String INPUTS_DIRECTORY_NAME = "advent-of-code-inputs";

    private InputReader() {

    }

    /**
     * Returns an {@link InputConverter} for example input files. The example inputs are retrieved from the
     * {@code src/main/resources} directory from the calling {@link Class}'s module itself.
     *
     * @param inputFilePath file path in {@code src/main/resources} to be read
     * @return an {@link InputConverter} for example input files
     * @throws IllegalArgumentException thrown if the file does not exist
     */
    public static InputConverter forExample(final String inputFilePath) {
        try {
            final Path filePath = Paths.get(ClassLoader.getSystemResource(inputFilePath).toURI());

            if (!filePath.toFile().exists()) {
                throw new IllegalArgumentException(String.format("Unable to read example input file '%s'", filePath));
            }

            return new InputConverter(filePath);
        } catch (final URISyntaxException e) {
            throw new IllegalArgumentException(String.format("Error loading '%s' from example resources", inputFilePath), e);
        }
    }

    /**
     * Returns an {@link InputConverter} for real puzzle input files. The example inputs are retrieved from the
     * {@code src/main/resources} directory from the calling {@link Class}'s module itself.
     *
     * <p>
     * We calculate the relative filePath by taking the current module directory, moving to the adjacent {@value #INPUTS_DIRECTORY_NAME} directory,
     * then expecting a child directory with a name matching the current module directory name. For example, given the current module <b>2015</b>, we
     * can find the inputs in the matching directory <b>2015</b> as below:
     *
     * <pre>
     *     |- /advent-of-code
     *        |- /2015
     *        |- /advent-of-code-inputs
     *           |- /2015
     * </pre>
     *
     * @param inputFilePath file path of the input file to be read
     * @return an {@link InputConverter} for real input files
     * @throws IllegalArgumentException thrown if the file does not exist
     */
    public static InputConverter forPuzzle(final String inputFilePath) {
        final Path currentModuleDirectory = Paths.get(System.getProperty("user.dir"));
        final String currentModuleName = currentModuleDirectory.getFileName().toString();
        final Path projectRootDirectory = currentModuleDirectory.getParent();
        final String inputsDirectory = projectRootDirectory + File.separator + INPUTS_DIRECTORY_NAME;
        final Path filePath = Paths.get(inputsDirectory + File.separator + currentModuleName + File.separator + inputFilePath);

        if (!filePath.toFile().exists()) {
            throw new IllegalArgumentException(String.format("Unable to read puzzle input file '%s'", filePath));
        }

        return new InputConverter(filePath);
    }

    /**
     * Takes the {@link Path} defined by either {@link InputReader#forExample(String)} or {@link InputReader#forPuzzle(String)}, and then allows for
     * the conversion of the {@link List} of {@link String}s into another type, before passing it on to a {@code Reader} which will collect the final
     * output.
     */
    public static final class InputConverter {

        private final Path inputFilePath;
        private boolean filterBlankLines;

        private InputConverter(final Path inputFilePath) {
            this.inputFilePath = inputFilePath;
        }

        /**
         * Marks the {@link InputConverter} to exclude any {@link String}s from the input file that are {@link String#isBlank()}.
         *
         * @return the current {@link InputConverter}
         */
        public InputConverter excludeBlankLines() {
            filterBlankLines = true;
            return this;
        }

        /**
         * Returns a {@link Reader}, which will allow for the conversion of each line of the input {@link String} file into a new type.
         *
         * @param converter the {@link Function} that will convert a {@link String} from the input into the return type
         * @param <T>       the return type
         * @return a {@link Reader} for the return type
         */
        public <T> Reader<T> as(final Function<? super String, ? extends T> converter) {
            return new Reader<>(filteredStream().map(converter));
        }

        /**
         * Used when the full input file should be converted to a {@link Grid}.
         *
         * @return a {@link GridReader}
         */
        public GridReader asGrid() {
            return new GridReader(filteredStream().toList());
        }

        /**
         * Used to convert each line in the input file to an {@link Integer}.
         *
         * @return a {@link Reader} for {@link Integer}s
         */
        public Reader<Integer> asIntegers() {
            return as(Integer::parseInt);
        }

        /**
         * Used to keep each line in the input file as a {@link String}.
         *
         * @return a {@link StringReader}
         */
        public StringReader asStrings() {
            return new StringReader(filteredStream());
        }

        /**
         * Used to convert each line in the input file to a {@link List} of {@link Character}s.
         *
         * @return a {@link Reader} for a {@link List} of {@link Character}s
         */
        public Reader<List<Character>> asLinesOfCharacters() {
            return as(StringUtils::toCharacterList);
        }

        /**
         * Used to convert each line in the input file into a {@link List} of {@link Long} numbers, usually used when each line is a series of
         * separated numbers (any delimiter).
         *
         * @return a {@link Reader} for a {@link List} of {@link Long}s
         */
        public Reader<List<Long>> asLinesOfSeparatedNumbers() {
            return as(StringUtils::collectNumbersInOrder);
        }

        /**
         * Used to convert each line in the input file into a {@link List} of {@link String}s, usually used when each line is a series of
         * comma-separated {@link String}s.
         *
         * @return a {@link Reader} for a {@link List} of {@link String}s
         */
        public Reader<List<String>> asLinesOfCommaSeparatedStrings() {
            return new Reader<>(
                filteredStream()
                    .map(input -> Arrays.asList(input.split(",")))
                    .map(listOfStrings -> listOfStrings.stream().toList())
            );
        }

        private Stream<String> filteredStream() {
            final Stream<String> filteredStream = readLines(inputFilePath).stream();
            return filterBlankLines ? filteredStream.filter(s -> !s.isBlank()) : filteredStream;
        }

        private static List<String> readLines(final Path filePath) {
            try {
                return Files.readAllLines(filePath);
            } catch (final IOException e) {
                throw new IllegalArgumentException(String.format("Unable to read input file '%s'", filePath), e);
            }
        }
    }

    /**
     * Intermediary builder class that takes a {@link Stream} from {@link InputConverter}, and defines how many lines are to be read.
     *
     * @param <T> the return type
     */
    public static class Reader<T> {

        /**
         * The current {@link Stream} being read.
         */
        protected final Stream<T> stream;

        /**
         * Constructor for {@link Reader}.
         *
         * @param stream the input {@link Stream}
         */
        protected Reader(final Stream<T> stream) {
            this.stream = stream;
        }

        /**
         * Reads all lines in the {@link Stream} as a {@link List}.
         *
         * @return the output {@link List}
         */
        public List<T> readAllLines() {
            return stream.toList();
        }

        /**
         * Reads all lines in the {@link Stream}, and collect based on the input {@link Collector}.
         *
         * @param collector the {@link Collector} describing the reduction
         * @param <A>       the mutable accumulation type of the reduction operation
         * @param <R>       the result type of the reduction operation
         * @return the output of type {@code R}
         */
        public <A, R> R readAllLines(final Collector<? super T, A, R> collector) {
            return stream.collect(collector);
        }

        /**
         * Reads only the first line in the {@link Stream}.
         *
         * @return the output element
         */
        public T readFirstLine() {
            return stream.limit(1).toList().getFirst();
        }

        /**
         * To allow for flexibility, returns the current {@link Stream} to allow for more specific chained functions.
         *
         * @return the current {@link Stream}
         */
        public Stream<T> readStream() {
            return stream;
        }

        /**
         * Creates a new instance of {@link Reader} where the current {@link Stream} has been filtered with elements that match the supplied
         * {@link Predicate}.
         *
         * @param predicate the {@link Predicate} defining the elements of the {@link Stream} to keep
         * @return the new {@link Reader}
         */
        public Reader<T> filter(final Predicate<? super T> predicate) {
            return new Reader<>(stream.filter(predicate));
        }

        /**
         * Creates a new instance of {@link Reader} where the current {@link Stream} is mapped based on a {@link Function} to convert elements.
         *
         * @param converter the {@link Function} that will convert a {@link String} from the input into the return type
         * @param <N>       the new return type
         * @return a {@link Reader} for the return type
         */
        public <N> Reader<N> as(final Function<? super T, ? extends N> converter) {
            return new Reader<>(stream.map(converter));
        }

        /**
         * Returns a {@link Grouper} that will allow the current {@link Stream} to be grouped.
         *
         * @return a {@link Grouper}
         */
        public Grouper<T> grouped() {
            return new Grouper<>(stream);
        }
    }

    /**
     * Extension of {@link Reader} specifically for {@link String}s.
     */
    public static final class StringReader extends Reader<String> {

        private StringReader(final Stream<String> stream) {
            super(stream);
        }

        /**
         * Reads all input lines as a single {@link String}, using {@link Collectors#joining(CharSequence)} with the delimiter being a new line.
         *
         * @return the input as a single {@link String}
         */
        public String readAllAsSingleString() {
            return stream.collect(Collectors.joining("\n"));
        }
    }

    /**
     * Takes the output {@link Stream} from a {@link Reader}, and allows the output to be grouped.
     *
     * @param <T> the return type
     */
    public static final class Grouper<T> {

        private final Stream<? extends T> stream;

        private Grouper(final Stream<? extends T> stream) {
            this.stream = stream;
        }

        /**
         * Groups the {@code stream} of lines into {@link List}s, where each group is a split that is delimited when the provided
         * {@link Predicate} is met.
         *
         * <ul>
         *     <li>Any lines that match the {@link Predicate} are discarded and not included in the output</li>
         *     <li>If the {@link Predicate} is not met, we return an empty {@link List}</li>
         *     <li>Empty groups are not included in the output</li>
         * </ul>
         *
         * @param predicate the {@link Predicate} defining where the {@link List} is to be split
         * @return the group of {@link List}s of lines of the output type
         */
        public List<List<T>> byDelimiter(final Predicate<? super T> predicate) {
            final List<List<T>> groups = new ArrayList<>();

            List<T> currentGroup = new ArrayList<>();
            for (final T line : stream.toList()) {
                if (predicate.test(line)) {
                    if (!currentGroup.isEmpty()) {
                        groups.add(currentGroup);
                    }
                    currentGroup = new ArrayList<>();
                } else {
                    currentGroup.add(line);
                }
            }

            // Add last group if not empty
            if (!currentGroup.isEmpty()) {
                groups.add(currentGroup);
            }
            return groups;
        }
    }

    /**
     * Takes the full input as a {@link List} of {@link String}s from the {@link InputConverter}, and allows it to be converted to a {@link Grid}.
     */
    public static final class GridReader {

        private final List<String> strings;

        private GridReader(final List<String> strings) {
            this.strings = Collections.unmodifiableList(strings);
        }

        /**
         * Converts the {@link List} of {@link String}s to a {@link Boolean} {@link Grid}.
         *
         * @param symbolSignifyingTrue the symbol in the {@link String} that defines a {@code true} {@link Boolean}
         * @return the {@link Boolean} {@link Grid}
         * @see GridFactory#ofBooleans(List, char)
         */
        public Grid<Boolean> ofBooleans(final char symbolSignifyingTrue) {
            return GridFactory.ofBooleans(strings, symbolSignifyingTrue);
        }

        /**
         * Converts the {@link List} of {@link String}s to a {@link Grid} of {@link Character}s.
         *
         * @return the {@link Character} {@link Grid}
         * @see GridFactory#ofCharacters(List)
         */
        public Grid<Character> ofCharacters() {
            return GridFactory.ofCharacters(strings);
        }

        /**
         * Converts the {@link List} of {@link String}s to a {@link Integer} {@link Grid}.
         *
         * @return the {@link Integer} {@link Grid}
         * @see GridFactory#ofIntegers(List)
         */
        public Grid<Integer> ofIntegers() {
            return GridFactory.ofIntegers(strings);
        }

        /**
         * Converts the {@link List} of {@link String}s to a {@link Grid} where each {@link Character} in each {@link String} is converted to a new
         * type.
         *
         * @param converter the {@link Function} to convert a {@link Character} from the input into the correct type for the {@link Grid}
         * @param <T>       the type of the {@link Grid}
         * @return the {@link Character} {@link Grid}
         */
        public <T> Grid<T> of(final Function<? super Character, ? extends T> converter) {
            return Grid.parseGrid(strings, converter);
        }
    }
}

