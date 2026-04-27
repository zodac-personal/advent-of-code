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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.zodac.advent.pojo.DiskBlock;

/**
 * Solution for 2024, Day 9.
 *
 * @see <a href="https://adventofcode.com/2024/day/9">[2024: 9] Disk Fragmenter</a>
 */
public final class Day09 {

    private Day09() {

    }

    /**
     * Given a {@link String} in the format:
     * <pre>
     *     2333133121414131402
     * </pre>
     *
     * <p>
     * Each index of the {@link String} alternates between references populated disk space or free disk space. The populated space is incremented
     * every <b>2</b> indices of the {@link String}, while the free space is denoted by {@link DiskBlock#FREE_SPACE_SYMBOL}. The above value would be
     * expanded to a representation of a disk space as follows:
     *
     * <pre>
     *     00...111...2...333.44.5555.6666.777.888899
     * </pre>
     *
     * <p>
     * Once the {@link String} is expanded, the free spaces are filled by the populated spaces, taken one-by-one from the end of the disk space. So
     * the above example would become:
     *
     * <pre>
     *      0099811188827773336446555566
     * </pre>
     *
     * <p>
     * Finally, the checksum of the disk (now fragmented) is calculated, where each index value is:
     *
     * <pre>
     *      index * value
     * </pre>
     *
     * <p>
     * The final value here is: <b>1928</b>
     *
     * @param input the input {@link String} representing the disk
     * @return the checksum of the fragmented files
     */
    public static long calculateChecksumOfFragmentedFiles(final String input) {
        // DiskBlock logic *could* be reused here, but performance is massively impacted due to the number of blocks that need to be shuffled around
        // Instead, we'll keep it as a String and use pointers to iterate backwards and forwards to calculate the value, without any need to modify
        // the content of the String
        final List<String> expandedFiles = expandFiles(input);
        return checksumFragmentedFiles(expandedFiles);
    }

    private static List<String> expandFiles(final String input) {
        final List<String> output = new ArrayList<>();
        int index = 0;
        int value = 0;

        for (final char c : input.toCharArray()) {
            final int count = Character.getNumericValue(c);

            for (int i = 0; i < count; i++) {
                if (index % 2 == 0) {
                    output.add(String.valueOf(value));
                } else {
                    output.add(DiskBlock.FREE_SPACE_SYMBOL);
                }
            }

            if (index % 2 == 0) {
                value++;
            }

            index++;
        }

        return output;
    }

    private static long checksumFragmentedFiles(final List<String> input) {
        long total = 0L;
        int forwardPointer = 0;
        int reversePointer = input.size() - 1;

        while (forwardPointer <= reversePointer) {
            final String currentValue = input.get(forwardPointer);

            if (DiskBlock.FREE_SPACE_SYMBOL.equals(currentValue)) {
                String reverseValue = input.get(reversePointer);
                final long update = Long.parseLong(reverseValue) * forwardPointer;
                total += update;

                do {
                    reversePointer--;
                    reverseValue = input.get(reversePointer);
                } while (DiskBlock.FREE_SPACE_SYMBOL.equals(reverseValue));
            } else {
                final long update = Long.parseLong(currentValue) * forwardPointer;
                total += update;
            }

            forwardPointer++;
        }

        return total;
    }

    /**
     * Given a {@link String} in the format:
     * <pre>
     *     2333133121414131402
     * </pre>
     *
     * <p>
     * Each index of the {@link String} alternates between references populated disk space or free disk space. The populated space is incremented
     * every <b>2</b> indices of the {@link String}, while the free space is denoted by {@link DiskBlock#FREE_SPACE_SYMBOL}. The above value would be
     * expanded to a representation of a disk space as follows:
     *
     * <pre>
     *     00...111...2...333.44.5555.6666.777.888899
     * </pre>
     *
     * <p>
     * Once the {@link String} is expanded, the populated spaces from the end of the disk are used to fill the free spaces from the start. However,
     * only contiguous files (full sequences of the same number) can be used to fill the free space, to avoid file fragmentation.
     *
     * <pre>
     *      00992111777.44.333....5555.6666.....8888
     * </pre>
     *
     * <p>
     * Finally, the checksum of the disk (now fragmented) is calculated, where each index value is:
     *
     * <pre>
     *      index * value
     * </pre>
     *
     * <p>
     * The final value here is: <b>2858</b>
     *
     * @param input the input {@link String} representing the disk
     * @return the checksum of the defragmented files
     */
    public static long calculateChecksumOfDefragmentedFiles(final String input) {
        final List<DiskBlock> diskBlocks = expandToDiskBlocks(input);
        final List<DiskBlock> defragmentedBlocks = defragmentDiskBlocksByFullFiles(diskBlocks);
        return checksumDefragmentedFiles(defragmentedBlocks);
    }

    private static List<DiskBlock> expandToDiskBlocks(final String input) {
        final List<DiskBlock> diskBlocks = new ArrayList<>();

        int index = 0;
        int value = 0;

        for (final char c : input.toCharArray()) {
            final int count = Character.getNumericValue(c);
            if (index % 2 == 0) {
                diskBlocks.add(DiskBlock.create(value, count));
                value++;
            } else {
                diskBlocks.add(DiskBlock.ofFreeSpace(count));
            }

            index++;
        }

        return diskBlocks;
    }

    private static List<DiskBlock> defragmentDiskBlocksByFullFiles(final List<DiskBlock> diskBlocks) {
        int reversePointer = diskBlocks.size() - 1;

        while (reversePointer > 0) {
            final DiskBlock lastBlock = diskBlocks.get(reversePointer);
            if (lastBlock.isFreeDiskSpace()) {
                reversePointer--;
                continue;
            }

            final int fileBlockSize = lastBlock.size();
            for (int i = 0; i < reversePointer; i++) {
                final DiskBlock block = diskBlocks.get(i);
                if (!block.isFreeDiskSpace() || block.size() < fileBlockSize) {
                    continue;
                }

                if (block.size() == fileBlockSize) {
                    diskBlocks.remove(i);
                    diskBlocks.add(i, lastBlock);
                } else {
                    diskBlocks.remove(i);
                    diskBlocks.add(i, DiskBlock.ofFreeSpace(block.size() - fileBlockSize));
                    diskBlocks.add(i, lastBlock);

                    reversePointer++; // Add 1 to pointer since we've increased the number of elements in the list
                }

                diskBlocks.remove(reversePointer);
                diskBlocks.add(reversePointer, DiskBlock.ofFreeSpace(fileBlockSize));

                break; // NOPMD: AvoidBranchingStatementAsLastInLoop - Once some free space of valid size is found, stop searching
            }

            reversePointer--;
        }

        return diskBlocks;
    }

    private static long checksumDefragmentedFiles(final Collection<DiskBlock> defragmentedDiskBlocks) {
        long total = 0L;
        int value = 0;

        for (final DiskBlock diskBlock : defragmentedDiskBlocks) {
            final int size = diskBlock.size();
            for (int i = 0; i < size; i++) {
                if (!diskBlock.isFreeDiskSpace()) {
                    total += Long.parseLong(diskBlock.blockValue()) * value;
                }
                value++;
            }
        }

        return total;
    }
}
