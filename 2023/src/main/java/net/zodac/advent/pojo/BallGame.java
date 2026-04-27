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

import java.util.ArrayList;
import java.util.List;
import net.zodac.advent.util.StringUtils;

/**
 * Simple class defining a game with multiple sets of coloured balls.
 *
 * @param id          the ID of the ball game
 * @param ballDetails the number of balls of each colour in each set of the {@link BallGame}
 */
public record BallGame(int id, List<BallDetails> ballDetails) {

    private static final String GAME_DEFINITION_DELIMITER = ":";
    private static final String GAME_SET_DELIMITER = ";";
    private static final String GAME_BALLS_DELIMITER = ",";

    /**
     * Takes a representation of a {@link BallGame} as a {@link String}, with the format as follows for 3 sets of varying {@link BallDetails}s:
     * <pre>
     *     Game ID: COLOUR COUNT, COLOUR COUNT; COLOUR COUNT; COLOUR COUNT, COLOUR COUNT, COLOUR COUNT
     * </pre>
     *
     * @param input the {@link String} to parse
     * @return the {@link BallGame}
     */
    public static BallGame parse(final String input) {
        final String[] gameTokens = input.split(GAME_DEFINITION_DELIMITER, 2);
        final String gameIdentifier = gameTokens[0];
        final String gameId = StringUtils.splitOnWhitespace(gameIdentifier)[1];

        final String games = gameTokens[1];
        final String[] gameSets = games.split(GAME_SET_DELIMITER);
        final List<BallDetails> ballDetails = new ArrayList<>();

        for (final String gameSet : gameSets) {
            final String[] balls = gameSet.split(GAME_BALLS_DELIMITER);

            for (final String ball : balls) {
                final String[] ballDefinitions = StringUtils.splitOnWhitespace(ball);
                final int numberOfBalls = Integer.parseInt(ballDefinitions[0]);
                final Colour colour = Colour.get(ballDefinitions[1]);

                ballDetails.add(new BallDetails(colour, numberOfBalls));
            }
        }

        return new BallGame(Integer.parseInt(gameId), ballDetails);
    }

    /**
     * The definition of the ball's colour and the count.
     *
     * @param colour        the colour of the ball
     * @param numberOfBalls how many balls of this colour
     */
    public record BallDetails(Colour colour, int numberOfBalls) {

    }
}
