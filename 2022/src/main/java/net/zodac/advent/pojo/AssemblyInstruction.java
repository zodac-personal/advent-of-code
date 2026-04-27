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

import java.util.Set;
import net.zodac.advent.util.StringUtils;

/**
 * Simple class defining an assembly instruction.
 *
 * @param instruction the instruction
 * @param offset      the value offset
 */
public record AssemblyInstruction(String instruction, int offset) {

    private static final Set<String> NOOP_INSTRUCTIONS = Set.of("noop");
    private static final int SINGLE_ELEMENT = 1;

    /**
     * Creates a {@link AssemblyInstruction} from a {@link String} in the format:
     * <pre>
     *     [instruction] [offset]
     * </pre>
     *
     * <p>
     * <b>NOTE:</b> The offset is optional, so we will return a value of <b>0</b> in those cases.
     *
     * @param input the {@link String} to parse
     * @return the {@link AssemblyInstruction}
     */
    public static AssemblyInstruction parse(final String input) {
        final String[] tokens = StringUtils.splitOnWhitespace(input);

        if (tokens.length == SINGLE_ELEMENT) {
            return new AssemblyInstruction(input, 0);
        }

        final int offset = Integer.parseInt(tokens[1]);
        return new AssemblyInstruction(tokens[0], offset);
    }

    /**
     * Checks if the {@link AssemblyInstruction} is a 'noop' - 'no operation', meaning no action to be taken.
     *
     * @return {@code true} if the {@link AssemblyInstruction} is a noop
     */
    public boolean isNoop() {
        return NOOP_INSTRUCTIONS.contains(instruction);
    }
}
