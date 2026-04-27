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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.zodac.advent.grid.Direction;
import net.zodac.advent.grid.Grid;
import net.zodac.advent.grid.Point;
import net.zodac.advent.pojo.BeamStep;

/**
 * Solution for 2023, Day 16.
 *
 * @see <a href="https://adventofcode.com/2023/day/16">[2023: 16] The Floor Will Be Lava</a>
 */
public final class Day16 {

    private static final char BACKWARDS_MIRROR_SYMBOL = '\\';
    private static final char FORWARD_MIRROR_SYMBOL = '/';
    private static final char HORIZONTAL_MIRROR_SYMBOL = '-';
    private static final char VERTICAL_MIRROR_SYMBOL = '|';

    private Day16() {

    }

    /**
     * Given a {@link Character} {@link Grid}, a beam of light will shine through the {@link Grid} in a given direction until it falls off the
     * edge. If it encounters a mirror {@link Character} it is either reflected 90° ({@literal \}, {@literal /}) or split into two beams
     * ({@literal |}, {@literal -}).
     *
     * <p>
     * We start off the {@link Grid}, and come in at {@link Point} (0, 0) from the {@link Direction#LEFT}, and progress
     * the beam. Once all beams have completed their movements, we sum all the {@link Point}s that were 'energised' by the beam.
     *
     * @param grid the input {@link Grid} for the beam to traverse
     * @return the number of {@link Point}s the beams moved through
     */
    public static long countNumberOfPointsEnergisedByBeam(final Grid<Character> grid) {
        final Map<Direction, Set<Point>> startPointsBySourceDirection = Map.of(Direction.LEFT, Set.of(Point.of(0, -1)));
        return findMaxNumberOfEnergisedPoints(grid, startPointsBySourceDirection);
    }

    /**
     * Given a {@link Character} {@link Grid}, a beam of light will shine through the {@link Grid} in a given direction until it falls off the
     * edge. If it encounters a mirror {@link Character} it is either reflected 90° ({@literal \}, {@literal /}) or split into two beams
     * ({@literal |}, {@literal -}).
     *
     * <p>
     * We consider all {@link Point}s along the perimeter to be a valid starting {@link Point}.
     * We start off the {@link Grid}, and come in at {@link Point} (0, 0) from the {@link Direction#LEFT}, and progress
     * the beam. Once all beams have completed their movements, we sum all the {@link Point}s that were 'energised' by the beam sum all the
     * {@link Point}s that were 'energised' by the beam. We are only interested in the start {@link Point} that energises the most {@link Point}s, and
     * we return that total.
     *
     * @param grid the input {@link Grid} for the beam to traverse
     * @return the maximum number of {@link Point}s the beams moved through, for a single start {@link Point}
     * @see Grid#getPerimeterPoints()
     */
    public static long countMaxNumberOfPointsEnergisedBySingleBeam(final Grid<Character> grid) {
        final Map<Direction, Set<Point>> perimeterPoints = grid.getPerimeterPoints();
        return findMaxNumberOfEnergisedPoints(grid, perimeterPoints);
    }

    private static long findMaxNumberOfEnergisedPoints(final Grid<Character> grid, final Map<Direction, Set<Point>> startPointsBySourceDirection) {
        long maximumNumberOfEnergisedPoints = Long.MIN_VALUE;
        for (final Map.Entry<Direction, Set<Point>> startPointsBySourceDirectionEntry : startPointsBySourceDirection.entrySet()) {
            final Direction startDirection = startPointsBySourceDirectionEntry.getKey().getOpposite();

            final long maxNumberOfEnergisedPointsForDirection = startPointsBySourceDirectionEntry.getValue()
                .stream()
                .mapToLong(startPoint -> findNumberOfEnergisedPoints(grid, startPoint, startDirection))
                .max()
                .orElse(0L);

            maximumNumberOfEnergisedPoints = Math.max(maximumNumberOfEnergisedPoints, maxNumberOfEnergisedPointsForDirection);
        }

        return maximumNumberOfEnergisedPoints;
    }

    private static long findNumberOfEnergisedPoints(final Grid<Character> grid, final Point start, final Direction startDirection) {
        final Collection<Point> energisedPoints = new HashSet<>();
        final Collection<BeamStep> visitedBeamSteps = new HashSet<>();

        Collection<BeamStep> beamStepsToCheck = new ArrayList<>();
        beamStepsToCheck.add(new BeamStep(start, '.', startDirection));

        while (!beamStepsToCheck.isEmpty()) {
            energisedPoints.addAll(
                beamStepsToCheck.stream().map(BeamStep::point).filter(grid::exists).collect(Collectors.toSet()));

            final Collection<BeamStep> nextBeamStepsToCheck = new ArrayList<>();
            for (final BeamStep beamStep : beamStepsToCheck) {
                // Because we don't need to worry about how many times a Point has been visited (just that it has happened at least once), we don't
                // need to re-visit any Points. So if we have already done a beam steps, we can skip doing it again.
                if (visitedBeamSteps.contains(beamStep)) {
                    continue;
                }

                // Because a beam can also split into a new beam, the following methods may update 'nextBeamStepsToCheck' as a side effect
                final Direction nextDirection = getDirectionForNextBeamStep(grid, beamStep, nextBeamStepsToCheck);
                addNewBeamStepToCheck(grid, beamStep, nextDirection, nextBeamStepsToCheck);
                visitedBeamSteps.add(beamStep);
            }

            beamStepsToCheck = nextBeamStepsToCheck;
        }

        return energisedPoints.size();
    }

    private static Direction getDirectionForNextBeamStep(final Grid<Character> grid,
                                                         final BeamStep beamStep,
                                                         final Collection<? super BeamStep> nextBeamStepsToCheck) {
        if (beamStep.value() == FORWARD_MIRROR_SYMBOL || beamStep.value() == BACKWARDS_MIRROR_SYMBOL) {
            return rotate(beamStep, beamStep.value());
        }

        if (beamStep.value() == HORIZONTAL_MIRROR_SYMBOL && !Set.of(Direction.LEFT, Direction.RIGHT).contains(beamStep.direction())) {
            addNewBeamStepToCheck(grid, beamStep, Direction.LEFT, nextBeamStepsToCheck);
            return Direction.RIGHT;
        }

        if (beamStep.value() == VERTICAL_MIRROR_SYMBOL && !Set.of(Direction.UP, Direction.DOWN).contains(beamStep.direction())) {
            addNewBeamStepToCheck(grid, beamStep, Direction.DOWN, nextBeamStepsToCheck);
            return Direction.UP;
        }

        return beamStep.direction();
    }

    private static Direction rotate(final BeamStep beamStep, final char mirrorCharacter) {
        final Direction direction = beamStep.direction();
        return switch (direction) {
            case DOWN -> mirrorCharacter == FORWARD_MIRROR_SYMBOL ? Direction.LEFT : Direction.RIGHT;
            case UP -> mirrorCharacter == FORWARD_MIRROR_SYMBOL ? Direction.RIGHT : Direction.LEFT;
            case RIGHT -> mirrorCharacter == FORWARD_MIRROR_SYMBOL ? Direction.UP : Direction.DOWN;
            case LEFT -> mirrorCharacter == FORWARD_MIRROR_SYMBOL ? Direction.DOWN : Direction.UP;
            default -> throw new IllegalArgumentException(String.format("Cannot move beam in %s: %s", Direction.class.getSimpleName(), direction));
        };
    }

    private static void addNewBeamStepToCheck(final Grid<Character> grid,
                                              final BeamStep beamStep,
                                              final Direction nextDirection,
                                              final Collection<? super BeamStep> nextBeamStepsToCheck) {
        final Point nextPoint = beamStep.point().move(nextDirection);
        if (grid.exists(nextPoint)) {
            final BeamStep newBeamStep = new BeamStep(nextPoint, grid.at(nextPoint), nextDirection);
            nextBeamStepsToCheck.add(newBeamStep);
        }
    }
}
