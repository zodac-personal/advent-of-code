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

package net.zodac.advent.pojo.rpg.mage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.zodac.advent.pojo.SearchNode;

/**
 * Implementation of {@link SearchNode} that defines each {@link SearchNode} as a round of a battle (pitting a {@link MagePlayer} against a
 * {@link MageBoss}). We carry the state of each round to the next {@link SearchNode}, updating hitPoints, mana, {@link ActiveSpell}s, etc.
 */
public final class BattleRound implements SearchNode {

    private static final long DISTANCE_FOR_IMMEDIATE_ROUND = 0L;
    private static final int MINIMUM_BOSS_DAMAGE_TO_PLAYER_PER_ROUND = 1;
    private static final Set<Spell> AVAILABLE_SPELLS = Set.of(
        Spell.missile(4, 53),
        Spell.drain(2, 2, 73),
        Spell.shield(7, 6, 113),
        Spell.poison(3, 6, 173),
        Spell.recharge(101, 5, 229)
    );

    private final MageBoss boss;
    private final List<ActiveSpell> activeSpells;
    private final List<Spell> castSpells;
    private final boolean isPlayerRound;
    private final int heathLossEachPlayerRound;

    private MagePlayer player;
    private long distance = Long.MAX_VALUE;

    private BattleRound(final MagePlayer player, final MageBoss boss, final List<ActiveSpell> activeSpells,
                        final List<Spell> castSpells,
                        final boolean isPlayerRound, final int heathLossEachPlayerRound) {
        this.player = player;
        this.boss = boss;
        this.activeSpells = new ArrayList<>(activeSpells);
        this.castSpells = Collections.unmodifiableList(castSpells);
        this.isPlayerRound = isPlayerRound;
        this.heathLossEachPlayerRound = heathLossEachPlayerRound;
    }

    /**
     * Creates the first {@link BattleRound} for the battle.
     *
     * @param player                  the {@link MagePlayer}
     * @param boss                    the {@link MageBoss}
     * @param heathLossEachPlayerTurn the health the player loses at the start of each turn
     * @return the created {@link BattleRound}
     */
    public static BattleRound createFirstRound(final MagePlayer player, final MageBoss boss, final int heathLossEachPlayerTurn) {
        return new BattleRound(player, boss, new ArrayList<>(), List.of(), true, heathLossEachPlayerTurn);
    }

    /**
     * Creates a {@link MagePlayer} {@link BattleRound}.
     *
     * @param player                   the {@link MagePlayer}
     * @param boss                     the {@link MageBoss}
     * @param activeSpells             the {@link ActiveSpell}s that are running
     * @param castSpells               the {@link Spell}s that have been cast
     * @param heathLossEachPlayerRound the health the player loses at the start of each {@link BattleRound}
     * @return the created {@link MagePlayer} {@link BattleRound}
     */
    public static BattleRound createPlayerRound(final MagePlayer player, final MageBoss boss, final List<ActiveSpell> activeSpells,
                                                final List<Spell> castSpells, final int heathLossEachPlayerRound) {
        return new BattleRound(player, boss, activeSpells, castSpells, true, heathLossEachPlayerRound);
    }

    /**
     * Creates a {@link MagePlayer} {@link BattleRound}.
     *
     * @param player                   the {@link MagePlayer}
     * @param boss                     the {@link MageBoss}
     * @param activeSpells             the {@link ActiveSpell}s that are running
     * @param castSpells               the {@link Spell}s that have been cast
     * @param heathLossEachPlayerRound the health the player loses at the start of each {@link BattleRound}
     * @return the created {@link MageBoss} {@link BattleRound}
     */
    public static BattleRound createBossRound(final MagePlayer player, final MageBoss boss, final List<ActiveSpell> activeSpells,
                                              final List<Spell> castSpells, final int heathLossEachPlayerRound) {
        return new BattleRound(player, boss, activeSpells, castSpells, false, heathLossEachPlayerRound);
    }

    @Override
    public Map<Long, ? extends SearchNode> getNeighbourNodesByDistance() {
        if (isGameFinished()) {
            return Map.of();
        }

        final Map<Long, BattleRound> neighbourNodes = new HashMap<>();

        // Calculate updates for the round from active spells
        final int totalDamage = activeSpells.stream().map(ActiveSpell::spell).mapToInt(Spell::damage).sum();
        final int totalHealing = activeSpells.stream().map(ActiveSpell::spell).mapToInt(Spell::healing).sum();
        final int totalManaIncrease = activeSpells.stream().map(ActiveSpell::spell).mapToInt(Spell::manaRecovery).sum();
        final int totalDefense = activeSpells.stream().map(ActiveSpell::spell).mapToInt(Spell::defence).sum();

        activeSpells.forEach(ActiveSpell::reduceDuration);
        activeSpells.removeIf(activeSpell -> activeSpell.remainingDuration() <= 0);

        if (isPlayerRound) {
            // Decrease player's health at start of the round
            if (heathLossEachPlayerRound != 0) {
                player = player.attack(heathLossEachPlayerRound);
            }

            // Create a new possible round for each available Spell
            for (final Spell spell : AVAILABLE_SPELLS) {
                if (isSpellTooExpensive(spell.cost(), player.mana() + totalManaIncrease) || isSpellAlreadyActive(activeSpells, spell)) {
                    continue;
                }

                final BattleRound spellBattleRound = generateRoundForSpell(totalDamage, totalHealing, totalManaIncrease, spell);
                neighbourNodes.put((long) spell.cost(), spellBattleRound);
            }
        } else {
            final BattleRound nextBossBattleRound = generateBossRound(totalDamage, totalHealing, totalManaIncrease, totalDefense);
            // Boss must go next
            neighbourNodes.put(DISTANCE_FOR_IMMEDIATE_ROUND, nextBossBattleRound);
        }

        return neighbourNodes;
    }

    private static boolean isSpellTooExpensive(final int spellCost, final int availableMana) {
        return spellCost > availableMana;
    }

    private static boolean isSpellAlreadyActive(final Collection<ActiveSpell> activeSpells, final Spell spell) {
        return activeSpells
            .stream()
            .anyMatch(activeSpell -> spell.equals(activeSpell.spell()));
    }

    private BattleRound generateRoundForSpell(final int totalDamage, final int totalHealing, final int totalManaIncrease, final Spell spell) {
        final List<ActiveSpell> newActiveSpells = new ArrayList<>(activeSpells
            .stream()
            .map(activeSpell -> ActiveSpell.create(activeSpell.spell(), activeSpell.remainingDuration()))
            .toList());
        final int manaChange = totalManaIncrease - spell.cost();

        final Collection<Spell> newCastedSpells = new ArrayList<>(castSpells);
        newCastedSpells.add(spell);

        if (spell.spellDuration() == 0) {
            final int hitPointsChange = totalHealing + spell.healing();

            final MagePlayer newPlayer = player.update(hitPointsChange, manaChange);

            final int damageToBoss = totalDamage + spell.damage();
            final MageBoss newBoss = boss.attack(damageToBoss);
            return createBossRound(newPlayer, newBoss, newActiveSpells, List.copyOf(newCastedSpells), heathLossEachPlayerRound);
        }

        final MagePlayer newPlayer = player.update(totalHealing, manaChange);
        final MageBoss newBoss = boss.attack(totalDamage);

        newActiveSpells.add(ActiveSpell.create(spell, spell.spellDuration()));
        return createBossRound(newPlayer, newBoss, newActiveSpells, List.copyOf(newCastedSpells), heathLossEachPlayerRound);
    }

    private BattleRound generateBossRound(final int damageToBoss, final int totalHealing, final int totalManaIncrease, final int totalDefense) {
        final MageBoss updatedBoss = boss.attack(damageToBoss);

        final List<ActiveSpell> newActiveSpells = new ArrayList<>(activeSpells
            .stream()
            .map(activeSpell -> ActiveSpell.create(activeSpell.spell(), activeSpell.remainingDuration()))
            .toList());

        if (updatedBoss.hitPoints() <= 0) {
            return createPlayerRound(player, updatedBoss, newActiveSpells, castSpells, heathLossEachPlayerRound);
        }

        final int bossDamageToPlayer = Math.max(MINIMUM_BOSS_DAMAGE_TO_PLAYER_PER_ROUND, boss.attack() - totalDefense);
        final int hitPointsChange = totalHealing - bossDamageToPlayer;
        final MagePlayer newPlayer = player.update(hitPointsChange, totalManaIncrease);
        return createPlayerRound(newPlayer, updatedBoss, newActiveSpells, castSpells, heathLossEachPlayerRound);
    }

    @Override
    public long getDistance() {
        return distance;
    }

    @Override
    public void setDistance(final long distance) {
        this.distance = distance;
    }

    @Override
    public boolean isAtEndState() {
        return isGameFinished() && isPlayerAlive();
    }

    private boolean isGameFinished() {
        return !isPlayerAlive() || boss.hitPoints() <= 0;
    }

    private boolean isPlayerAlive() {
        return player.hitPoints() > 0 && player.mana() > 0;
    }
}
