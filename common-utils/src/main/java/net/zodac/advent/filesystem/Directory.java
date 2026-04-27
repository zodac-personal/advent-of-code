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

import java.util.ArrayList;
import java.util.Collection;
import org.jspecify.annotations.Nullable;

/**
 * Simple class defining a {@link Directory} on a filesystem.
 */
public final class Directory {

    /**
     * The path of the root {@link Directory} of the filesystem.
     */
    public static final String ROOT_DIRECTORY_PATH = "";

    private static final String ROOT_DIRECTORY_NAME = "/";

    @Nullable
    private final Directory parent;

    private final String path;
    private final Collection<Directory> childDirectories = new ArrayList<>();
    private final Collection<File> files = new ArrayList<>();

    private Directory(@Nullable final Directory parent, final String path) {
        this.parent = parent;
        this.path = path;
    }

    /**
     * Creates a {@link Directory}.
     *
     * @param parent the parent {@link Directory}
     * @param name   the name of the {@link Directory}
     * @return the created {@link Directory}
     */
    public static Directory create(@Nullable final Directory parent, final String name) {
        final String path = parent == null ? ROOT_DIRECTORY_PATH : parent.path + File.PATH_SEPARATOR + name;
        return new Directory(parent, path);
    }

    /**
     * Creates the root {@link Directory}, {@value ROOT_DIRECTORY_NAME}.
     *
     * @return the created root {@link Directory}
     */
    public static Directory createRoot() {
        return create(null, ROOT_DIRECTORY_NAME);
    }

    /**
     * Adds a {@link Directory} as a child of this {@link Directory}.
     *
     * @param childDirectory the child {@link Directory}
     */
    public void addChildDirectory(final Directory childDirectory) {
        childDirectories.add(childDirectory);
    }

    /**
     * Adds a {@link File} in this {@link Directory}.
     *
     * @param file the {@link File}
     */
    public void addFile(final File file) {
        files.add(file);
    }

    /**
     * Calculates the total size of the {@link Directory}, by checking the size of its {@code childDirectories} and {@code files}.
     *
     * @return the total size of the {@link Directory} in bytes
     */
    public long calculateSizeInBytes() {
        long totalSizeInBytes = 0L;

        for (final Directory childDirectory : childDirectories) {
            totalSizeInBytes += childDirectory.calculateSizeInBytes();
        }

        for (final File file : files) {
            totalSizeInBytes += file.sizeInBytes();
        }

        return totalSizeInBytes;
    }

    /**
     * Return the parent {@link Directory}.
     *
     * @return the parent {@link Directory}
     */
    public @Nullable Directory parent() {
        return parent;
    }

    /**
     * The {@link String} path of the {@link Directory}.
     *
     * @return the path of the {@link Directory}
     */
    public String path() {
        return path;
    }
}
