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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.zodac.advent.pojo.AssemblyInstruction;
import net.zodac.advent.pojo.SegmentedDisplay;

/**
 * Solution for 2022, Day 10.
 *
 * @see <a href="https://adventofcode.com/2022/day/10">AoC 2022, Day 10</a>
 */
public final class Day10 {

    private static final int START_CYCLE_FOR_SIGNAL_CHECK = 20;
    private static final int CYCLE_INTERVAL_FOR_SIGNAL_CHECK = 40;
    private static final int NUMBER_OF_CHARACTERS_IN_SEGMENTED_DISPLAY = 8;
    private static final int MAXIMUM_PERMITTED_DISTANCE_FOR_SEGMENTED_DISPLAY = 1;

    private Day10() {

    }

    /**
     * Given a {@link List} of {@link AssemblyInstruction}s, execute each instruction according to the following rules:
     * <ul>
     *     <li>If the {@link AssemblyInstruction} is a {@link AssemblyInstruction#isNoop()}, do nothing</li>
     *     <li>Otherwise, the {@link AssemblyInstruction} takes two cycles to execute (including the current)</li>
     * </ul>
     *
     * <p>
     * If an {@link AssemblyInstruction} is waiting to be executed, no new {@link AssemblyInstruction}s are considered until the pending ones have
     * completed.
     *
     * <p>
     * At specific cycles (cycle #20 and every 40th cycle after that), we calculate the current signal value, as:
     * <pre>
     *     currentValueOfRegisterX * cycle
     * </pre>
     *
     * <p>
     * We sum up these signal values and return the total.
     *
     * @param assemblyInstructions the input {@link AssemblyInstruction}s
     * @return the total signal values
     */
    public static long sumOfSignalValues(final List<AssemblyInstruction> assemblyInstructions) {
        int valueOfRegisterX = 1;
        long sumOfSignalValuesAtImportantCycles = 0;
        int instructionIndex = 0;
        int cycle = 1;

        final int numberOfAssemblyInstructions = assemblyInstructions.size();
        final Map<Integer, Integer> offsetByTargetCycle = new HashMap<>();

        while (instructionIndex < numberOfAssemblyInstructions) {
            if (isCycleToConsiderSignal(cycle)) {
                final long signalValue = ((long) valueOfRegisterX * cycle);
                sumOfSignalValuesAtImportantCycles += signalValue;
            }

            // If we have any delayed 'addX' commands to execute this cycle, execute them
            if (offsetByTargetCycle.containsKey(cycle)) {
                valueOfRegisterX += offsetByTargetCycle.get(cycle);
                instructionIndex++;
            } else {
                // Otherwise handle next instruction
                final AssemblyInstruction assemblyInstruction = assemblyInstructions.get(instructionIndex);

                if (assemblyInstruction.isNoop()) {
                    instructionIndex++;
                } else {
                    // If not a noop instruction, add the instruction to be executed in the next cycle
                    offsetByTargetCycle.put(cycle + 1, assemblyInstruction.offset());
                }
            }

            cycle++;
        }

        return sumOfSignalValuesAtImportantCycles;
    }

    /**
     * Given a {@link List} of {@link AssemblyInstruction}s, execute each instruction according to the following rules:
     * <ul>
     *     <li>If the {@link AssemblyInstruction} is a {@link AssemblyInstruction#isNoop()}, do nothing</li>
     *     <li>Otherwise, the {@link AssemblyInstruction} takes two cycles to execute (including the current)</li>
     * </ul>
     *
     * <p>
     * If an {@link AssemblyInstruction} is waiting to be executed, no new {@link AssemblyInstruction}s are considered until the pending ones have
     * completed.
     *
     * <p>
     * Each cycle, we are also iterating through a {@link SegmentedDisplay}. As the X register value gets updated, it will always refer to a point in
     * the current {@link SegmentedDisplay} row. If the current cycle value (mod {@link SegmentedDisplay#length()}) is within 1 of the X register,
     * then that cell on the {@link SegmentedDisplay} is considered lit.
     *
     * <p>
     * Once all {@link AssemblyInstruction}s have been completed, the {@link SegmentedDisplay} should be showing
     * {@value NUMBER_OF_CHARACTERS_IN_SEGMENTED_DISPLAY} characters. We can parse these values and return the {@link String} of characters.
     *
     * @param assemblyInstructions the input {@link AssemblyInstruction}s
     * @return the {@link String} of characters on the {@link SegmentedDisplay}
     * @see SegmentedDisplay#getCharacters()
     */
    public static String charactersOnSegmentedDisplay(final List<AssemblyInstruction> assemblyInstructions) {
        int valueOfRegisterX = 1;
        int instructionIndex = 0;
        int cycle = 1;

        final int numberOfAssemblyInstructions = assemblyInstructions.size();
        final Map<Integer, Integer> offsetByTargetCycle = new HashMap<>();
        final SegmentedDisplay segmentedDisplay = SegmentedDisplay.create(NUMBER_OF_CHARACTERS_IN_SEGMENTED_DISPLAY);

        while (instructionIndex < numberOfAssemblyInstructions) {
            updateSegmentedDisplay(valueOfRegisterX, cycle, segmentedDisplay);

            // If we have any delayed 'addX' commands to execute this cycle, execute them
            if (offsetByTargetCycle.containsKey(cycle)) {
                valueOfRegisterX += offsetByTargetCycle.get(cycle);
                instructionIndex++;
            } else {
                // Otherwise handle next instruction
                final AssemblyInstruction assemblyInstruction = assemblyInstructions.get(instructionIndex);

                if (assemblyInstruction.isNoop()) {
                    instructionIndex++;
                } else {
                    // If not a noop instruction, add the instruction to be executed in the next cycle
                    offsetByTargetCycle.put(cycle + 1, assemblyInstruction.offset());
                }
            }

            cycle++;
        }

        return segmentedDisplay.getCharacters();
    }

    private static void updateSegmentedDisplay(final int valueOfRegisterX, final int cycle, final SegmentedDisplay segmentedDisplay) {
        final int displayIndex = cycle - 1;
        final int displayRow = displayIndex / segmentedDisplay.length();
        final int displayColumn = displayIndex % segmentedDisplay.length();

        // The column value must be within 1 space of the register X value
        if (Math.abs(valueOfRegisterX - displayColumn) <= MAXIMUM_PERMITTED_DISTANCE_FOR_SEGMENTED_DISPLAY) {
            segmentedDisplay.turnOn(displayRow, displayColumn);
        }
    }

    // We check at cycle #20, and every subsequent 40 cycles
    private static boolean isCycleToConsiderSignal(final int cycle) {
        return cycle % CYCLE_INTERVAL_FOR_SIGNAL_CHECK == START_CYCLE_FOR_SIGNAL_CHECK;
    }
}
