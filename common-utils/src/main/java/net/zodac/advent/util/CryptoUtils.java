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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility functions for cryptography.
 */
public final class CryptoUtils {

    private static final char[] HEXADECIMAL_VALUES = "0123456789ABCDEF".toCharArray();
    private static final int BIT_MASK_FORWARD = 0xF0;
    private static final int BIT_MASK_REVERSE = 0x0F;

    private CryptoUtils() {

    }

    /**
     * Hashes the input {@link String} using the specified {@link MessageDigest} hashing algorithm, in hexadecimal.
     *
     * @param input            the {@link String} to be hashed
     * @param hashingAlgorithm the {@link MessageDigest} hashing algorithm to be used to hash the input
     * @return the hashed {@link String}, in hexadecimal, as a {@code char[]}
     * @throws IllegalArgumentException thrown if the input is {@link String#isBlank()}
     * @throws NoSuchAlgorithmException thrown if the {@link MessageDigest} hashing algorithm is not found
     */
    public static char[] hexadecimalHash(final String input, final String hashingAlgorithm) throws NoSuchAlgorithmException {
        if (input.isBlank()) {
            throw new IllegalArgumentException(String.format("Input must have at least one non-whitespace character, found: '%s'", input));
        }

        final MessageDigest digest = MessageDigest.getInstance(hashingAlgorithm);
        digest.update(input.getBytes(StandardCharsets.UTF_8));
        final byte[] hash = digest.digest();
        return toHex(hash);
    }

    private static char[] toHex(final byte[] input) {
        // Using code from: https://stackoverflow.com/a/32976536/2000246
        final char[] result = new char[(input.length << 1)];
        for (int i = 0; i < input.length; i++) {
            final byte b = input[i];
            result[2 * i] = HEXADECIMAL_VALUES[(b & BIT_MASK_FORWARD) >>> 4];
            result[2 * i + 1] = HEXADECIMAL_VALUES[b & BIT_MASK_REVERSE];
        }
        return result;
    }
}
