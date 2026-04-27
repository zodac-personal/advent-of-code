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

/**
 * POJO defining contiguous blocks on a file system's storage space.
 *
 * @param blockValue the value in the {@link DiskBlock}
 * @param size       the number of occurrences of the value in the {@link DiskBlock}
 */
public record DiskBlock(String blockValue, int size) {

    /**
     * Symbol signifying a free space within a {@link DiskBlock}.
     */
    public static final String FREE_SPACE_SYMBOL = ".";

    /**
     * Creates a {@link DiskBlock} of the given value.
     *
     * @param blockValue the value in the {@link DiskBlock}
     * @param size       the number of occurrences of the value
     * @return the create {@link DiskBlock}
     */
    public static DiskBlock create(final int blockValue, final int size) {
        return new DiskBlock(String.valueOf(blockValue), size);
    }

    /**
     * Creates a {@link DiskBlock} of free space.
     *
     * @param size the number of occurrences of the free space
     * @return the created free space {@link DiskBlock}
     */
    public static DiskBlock ofFreeSpace(final int size) {
        return new DiskBlock(FREE_SPACE_SYMBOL, size);
    }

    /**
     * Checks if the current {@link DiskBlock} is free disk space, by checking if the {@link #blockValue()} is {@value #FREE_SPACE_SYMBOL}.
     *
     * @return {@code true} if the {@link DiskBlock} is free disk space
     */
    public boolean isFreeDiskSpace() {
        return FREE_SPACE_SYMBOL.equals(blockValue);
    }
}
