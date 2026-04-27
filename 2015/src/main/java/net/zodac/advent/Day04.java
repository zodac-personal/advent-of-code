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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.zodac.advent.util.CryptoUtils;

/**
 * Solution for 2015, Day 4.
 *
 * @see <a href="https://adventofcode.com/2015/day/4">AoC 2015, Day 4</a>
 */
public final class Day04 {

    private Day04() {

    }

    /**
     * Generates an MD5 hash of the concatenation of the provided {@code secretKey} and an {@link Integer} counter. This hash is then checked to see
     * if it starts with the provided {@code prefixToFind}.
     *
     * <p>
     * The counter is incremented until either the wanted prefix is found, or {@link Integer#MAX_VALUE}.
     *
     * @param secretKey    the secret key prefix to the hash input
     * @param prefixToFind the wanted prefix to the hash output
     * @return the number of attempts needed to find the wanted prefix
     * @throws IllegalArgumentException if the {@code prefixToFind} is not found
     * @throws NoSuchAlgorithmException thrown if the {@link MessageDigest} hashing algorithm is not found
     */
    public static long indexOfHashWithPrefix(final String secretKey, final String prefixToFind) throws NoSuchAlgorithmException {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            final String stringToHash = secretKey + i;
            final char[] hash = CryptoUtils.hexadecimalHash(stringToHash, "MD5");

            if (doesArrayStartWith(hash, prefixToFind.toCharArray())) {
                return i;
            }
        }

        throw new IllegalArgumentException(String.format("No valid MD5 hash found for '%s' starting with prefix '%s'", secretKey, prefixToFind));
    }

    private static boolean doesArrayStartWith(final char[] array, final char[] prefix) {
        for (int j = 0; j < prefix.length; j++) {
            if (prefix[j] != array[j]) {
                return false;
            }
        }
        return true;
    }
}
