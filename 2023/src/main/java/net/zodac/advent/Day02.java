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
import java.util.EnumMap;
import java.util.Map;
import net.zodac.advent.pojo.BallGame;
import net.zodac.advent.pojo.Colour;

/**
 * Solution for 2023, Day 2.
 *
 * @see <a href="https://adventofcode.com/2023/day/2">[2023: 02] Cube Conundrum</a>
 */
public final class Day02 {

    private static final Map<Colour, Integer> MAX_VALUES_BY_COLOUR = Map.of(
        Colour.RED, 12,
        Colour.GREEN, 13,
        Colour.BLUE, 14
    );

    private Day02() {

    }

    /**
     * Given some {@link BallGame}s, we check if the listed games are possible according to the rules defined by {@link #MAX_VALUES_BY_COLOUR}. For
     * all possible {@link BallGame}s, the IDs are summed up and returned.
     *
     * @param ballGames the {@link BallGame}s to check
     * @return the sum of IDs of all possible {@link BallGame}s
     */
    public static long sumOfIdsOfPossibleGames(final Collection<BallGame> ballGames) {
        return ballGames
            .stream()
            .filter(Day02::hasEnoughBallsExistForGame)
            .mapToLong(BallGame::id)
            .sum();
    }

    private static boolean hasEnoughBallsExistForGame(final BallGame ballGame) {
        for (final BallGame.BallDetails ballDetails : ballGame.ballDetails()) {
            if (ballDetails.numberOfBalls() > MAX_VALUES_BY_COLOUR.getOrDefault(ballDetails.colour(), 0)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Given some {@link BallGame}s, count the minimum number of balls of each {@link Colour} needed to make the game possible. The values for each
     * {@link Colour} are multiplied together and this is the 'power' for that {@link BallGame}. The 'power' of all {@link BallGame}s are then summed
     * up and returned.
     *
     * @param ballGames the {@link BallGame}s to check
     * @return the sum of the 'power' of all {@link BallGame}s
     */
    public static long sumOfPowersOfGames(final Collection<BallGame> ballGames) {
        long total = 0L;
        for (final BallGame ballGame : ballGames) {
            final Map<Colour, Integer> minimumNeededCountByColour = new EnumMap<>(Colour.class);
            minimumNeededCountByColour.put(Colour.BLUE, 0);
            minimumNeededCountByColour.put(Colour.GREEN, 0);
            minimumNeededCountByColour.put(Colour.RED, 0);

            for (final BallGame.BallDetails ballDetails : ballGame.ballDetails()) {
                final int currentMinimumValue = minimumNeededCountByColour.getOrDefault(ballDetails.colour(), 0);

                if (ballDetails.numberOfBalls() > currentMinimumValue) {
                    minimumNeededCountByColour.put(ballDetails.colour(), ballDetails.numberOfBalls());
                }
            }

            long valueToAddToTotal = 1L;
            for (final Integer minimumNeededCount : minimumNeededCountByColour.values()) {
                valueToAddToTotal *= minimumNeededCount;
            }
            total += valueToAddToTotal;
        }

        return total;
    }
}
