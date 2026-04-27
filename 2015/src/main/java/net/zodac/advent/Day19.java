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

import java.util.Collection;
import java.util.HashSet;
import net.zodac.advent.pojo.Replacement;
import net.zodac.advent.util.StringUtils;

/**
 * Solution for 2015, Day 19.
 *
 * @see <a href="https://adventofcode.com/2015/day/19">AoC 2015, Day 19</a>
 */
public final class Day19 {

    private Day19() {

    }

    /**
     * Given an input {@link String} representation of a molecule, we find the total number of distinct molecules that can be generated where only one
     * replacement is made given the input possible {@link Replacement}s.
     *
     * <p>
     * For example, given the input {@code ABAC}, and the possible {@link Replacement}s:
     * <ul>
     *     <li>A -> aa</li>
     *     <li>A -> ab</li>
     *     <li>B -> xx</li>
     *     <li>C -> yy</li>
     * </ul>
     *
     * <p>
     * We have the potential replacements:
     * <ul>
     *     <li>aaBAC</li>
     *     <li>abBAC</li>
     *     <li>ABaaC</li>
     *     <li>ABabC</li>
     *     <li>AxxAC</li>
     *     <li>ABAyy</li>
     * </ul>
     *
     * <p>
     * These are all unique values, so the total number of distinct replacement molecules is <b>6</b>.
     *
     * @param molecule     the input molecule
     * @param replacements the possible {@link Replacement}s
     * @return the number of distinct replacement molecules that can be generated
     */
    public static long numberOfDistinctReplacementMolecules(final String molecule, final Collection<Replacement<String>> replacements) {
        final Collection<String> newMolecules = new HashSet<>();
        for (final Replacement<String> replacement : replacements) {
            int indexInMolecule = molecule.indexOf(replacement.source());

            while (indexInMolecule >= 0) {
                final String newMolecule = StringUtils.replaceAtIndex(molecule, replacement.source(), replacement.target(), indexInMolecule);

                newMolecules.add(newMolecule);
                indexInMolecule += replacement.source().length(); // Increment the position by the length of the key
                indexInMolecule = molecule.indexOf(replacement.source(), indexInMolecule);
            }
        }

        return newMolecules.size();
    }

    /**
     * Starting with {@code input}, calculates the minimum number of molecule replacements required to generate the wanted {@code molecule}.
     *
     * <p>
     * Rather than convert the input to the wanted output by applying all combinations of replacements, we work backwards on the output. Similar to
     * {@link #numberOfDistinctReplacementMolecules(String, Collection)}, we will attempt to replace the input until we are left only with the
     * {@code input}.
     *
     * <p>
     * Note that this will likely not work in the general case as a replacement for a CYK algorithm. We assume there is only one possible solution and
     * that it will be the minimum value. Also needs more testing against less 'helpful' inputs. But will suffice for now.
     *
     * @param input          the input to the replacement process
     * @param wantedMolecule the wanted end result after {@code input} has been replaced
     * @param replacements   the possible {@link Replacement}s
     * @return the minimum number of steps needed to created {@code wantedMolecule} by applying {@code replacements} to {@code input}
     */
    public static long minimumStepsToCreateOutputFromInput(final String input,
                                                           final String wantedMolecule,
                                                           final Collection<Replacement<String>> replacements) {
        String remainingMolecule = wantedMolecule;
        int count = 0;

        while (!remainingMolecule.equals(input)) {
            for (final Replacement<String> replacement : replacements) {
                if (remainingMolecule.contains(replacement.target())) {
                    final int index = remainingMolecule.lastIndexOf(replacement.target());
                    remainingMolecule = StringUtils.replaceAtIndex(remainingMolecule, replacement.target(), replacement.source(), index);
                    count++;
                }
            }
        }

        return count;
    }
}
