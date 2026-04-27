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

package net.zodac.advent;

import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.zodac.advent.filesystem.Directory;
import net.zodac.advent.filesystem.File;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2022, Day 7.
 *
 * @see <a href="https://adventofcode.com/2022/day/7">AoC 2022, Day 7</a>
 */
public final class Day07 {

    private static final long DIRECTORY_SIZE_THRESHOLD = 100_000L;
    private static final long TOTAL_DISK_SPACE = 70_000_000L;
    private static final long REQUIRED_DISK_SPACE = 30_000_000L;

    private static final String CD_ROOT_COMMAND = "$ cd /";
    private static final String CD_PREVIOUS_DIRECTORY_COMMAND = "$ cd ..";
    private static final String CD_COMMAND_PREFIX = "$ cd";
    private static final String DIRECTORY_PREFIX = "dir";
    private static final String LS_COMMAND = "$ ls";

    private Day07() {

    }

    /**
     * Iterates over the input {@link String} commands and builds a filesystem of {@link Directory}s and {@link Files}. Finds all {@link Directory}s
     * with a total size (including child {@link Directory}s) under {@value #DIRECTORY_SIZE_THRESHOLD}, then sums up their values.
     *
     * @param commands the input {@link String}s
     * @return the total size of all valid {@link Directory}s
     */
    public static long totalSizeOfDirectoriesOverThreshold(final Collection<String> commands) {
        final Map<String, Long> sizeByDirPath = getDirectorySizesByDirectoryPath(commands);
        return sizeByDirPath
            .values()
            .stream()
            .filter(directorySize -> directorySize < DIRECTORY_SIZE_THRESHOLD)
            .mapToLong(Long::longValue)
            .sum();
    }

    /**
     * Iterates over the input {@link String} commands and builds a filesystem of {@link Directory}s and {@link Files}. Based on the total used disk
     * space, a total disk space of {@value #TOTAL_DISK_SPACE}, and a required unused disk space of {@value #REQUIRED_DISK_SPACE}, we find the size
     * of the smallest {@link Directory} that can be deleted to free enough space on the filesystem.
     *
     * @param commands the input {@link String}s
     * @return the size of the smallest {@link Directory} that can be deleted to free enough disk space
     */
    public static long smallestDirectorySizeToDeleteToMeetSpaceRequirements(final Collection<String> commands) {
        final Map<String, Long> directorySizeByPath = getDirectorySizesByDirectoryPath(commands);

        final long totalUsedSpace = directorySizeByPath.getOrDefault(Directory.ROOT_DIRECTORY_PATH, Long.MAX_VALUE);
        final long unusedSpace = TOTAL_DISK_SPACE - totalUsedSpace;
        final long spaceToFind = REQUIRED_DISK_SPACE - unusedSpace;

        return directorySizeByPath
            .values()
            .stream()
            .filter(directorySize -> directorySize >= spaceToFind)
            .mapToLong(l -> l)
            .min()
            .orElse(0L);
    }

    private static Map<String, Long> getDirectorySizesByDirectoryPath(final Collection<String> commands) {
        final Map<String, Directory> dirsByPath = parseDirectoryStructure(commands);

        final Map<String, Long> directorySizeByPath = new HashMap<>();
        for (final Map.Entry<String, Directory> entry : dirsByPath.entrySet()) {
            directorySizeByPath.put(entry.getKey(), entry.getValue().calculateSizeInBytes());
        }

        return directorySizeByPath;
    }

    private static Map<String, Directory> parseDirectoryStructure(final Collection<String> commands) {
        final Map<String, Directory> directoriesByPath = new HashMap<>();
        final Directory rootDirectory = Directory.createRoot();

        // Start with root directory
        Directory currentDirectory = rootDirectory;
        directoriesByPath.put(currentDirectory.path(), currentDirectory);

        for (final String value : commands) {
            if (LS_COMMAND.equals(value)) {
                // No action needed
                continue;
            }

            if (CD_ROOT_COMMAND.equals(value)) {
                currentDirectory = rootDirectory;
            } else if (CD_PREVIOUS_DIRECTORY_COMMAND.equals(value)) {
                if (currentDirectory.parent() != null) {
                    currentDirectory = currentDirectory.parent();
                }
            } else if (value.startsWith(CD_COMMAND_PREFIX)) {
                final String childDirectoryName = StringUtils.splitOnWhitespace(value)[2];
                final Directory childDirectory = Directory.create(currentDirectory, childDirectoryName);

                if (directoriesByPath.containsKey(childDirectory.path())) {
                    currentDirectory = directoriesByPath.get(childDirectory.path());
                }
            } else if (value.startsWith(DIRECTORY_PREFIX)) {
                final String childDirectoryName = StringUtils.splitOnWhitespace(value)[1];
                final Directory childDirectory = Directory.create(currentDirectory, childDirectoryName);
                currentDirectory.addChildDirectory(childDirectory);

                directoriesByPath.put(childDirectory.path(), childDirectory);
            } else {
                final String[] tokens = StringUtils.splitOnWhitespace(value);
                final long fileSize = Long.parseLong(tokens[0]);
                final String fileName = tokens[1];

                final File file = File.create(currentDirectory, fileName, fileSize);
                currentDirectory.addFile(file);
            }
        }
        return directoriesByPath;
    }
}
