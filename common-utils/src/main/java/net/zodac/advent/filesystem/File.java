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

package net.zodac.advent.filesystem;

import java.util.Optional;
import net.zodac.advent.pojo.tuple.Pair;

/**
 * Simple class defining a {@link File} on a filesystem.
 *
 * @param path              the full path of the {@link File}
 * @param name              the name of the {@link File} (without extension)
 * @param extension         the extension of the {@link File} (last extension if multiple exist)
 * @param nameWithExtension the {@code name} and all {@code extension}s of the file
 * @param sizeInBytes       the size of the {@link File}, in bytes
 */
public record File(String path, String name, String extension, String nameWithExtension, long sizeInBytes) {

    /**
     * The path-separator for a filesystem.
     */
    public static final char PATH_SEPARATOR = '/';

    private static final String DEFAULT_FILE_EXTENSION = "";

    /**
     * Creates a {@link File}.
     *
     * <p>
     * Builds a {@code path} by using the parent {@link Directory}.
     *
     * <p>
     * Attempts to extract the file extension if the {@code fileNameWithExtension} includes a {@code .} character, or else sets the file extension to
     * an empty {@link String}. If there are multiple file extensions, only the last one is considered. If you require multiple file extensions, you
     * can use {@link #nameWithExtension()} and parse from that.
     *
     * @param parent                the parent {@link Directory} of the {@link File}
     * @param fileNameWithExtension the name of the {@link File}, with extension
     * @param sizeInBytes           the size of the {@link File}, in bytes
     * @return the created {@link File}
     * @throws IllegalArgumentException thrown if the parent {@link Directory} is null, the {@code fileNameWithExtension} is null/blank or the
     *                                  {@code sizeInBytes} is negative
     */
    public static File create(final Directory parent, final String fileNameWithExtension, final long sizeInBytes) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent directory cannot be null");
        }

        if (fileNameWithExtension == null || fileNameWithExtension.isBlank()) {
            throw new IllegalArgumentException("File name cannot be null or blank");
        }

        if (sizeInBytes < 0) {
            throw new IllegalArgumentException("File size cannot be negative, found: " + sizeInBytes);
        }

        final String filePath = parent.path() + PATH_SEPARATOR + fileNameWithExtension;
        final Pair<String, Optional<String>> nameAndExtension = extractNameAndExtension(fileNameWithExtension);

        final String fileExtension = nameAndExtension.second().orElse(DEFAULT_FILE_EXTENSION);
        return new File(filePath, nameAndExtension.first(), fileExtension, fileNameWithExtension, sizeInBytes);
    }

    private static Pair<String, Optional<String>> extractNameAndExtension(final String fileNameWithExtension) {
        final String[] nameTokens = fileNameWithExtension.split("\\.");
        final String fileName = nameTokens[0];
        final Optional<String> fileExtension = nameTokens.length == 1 ? Optional.empty() : Optional.of(nameTokens[nameTokens.length - 1]);

        return Pair.of(fileName, fileExtension);
    }
}
