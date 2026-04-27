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

import net.zodac.advent.pojo.DijkstraSearcher;
import net.zodac.advent.pojo.SearchNode;
import net.zodac.advent.pojo.rpg.mage.BattleRound;
import net.zodac.advent.pojo.rpg.mage.MageBoss;
import net.zodac.advent.pojo.rpg.mage.MagePlayer;
import net.zodac.advent.pojo.rpg.mage.Spell;

/**
 * Solution for 2015, Day 22.
 *
 * @see <a href="https://adventofcode.com/2015/day/22">AoC 2015, Day 22</a>
 */
public final class Day22 {

    private Day22() {

    }

    /**
     * Given a {@link MageBoss}, we generate a {@link MagePlayer} and simulate a battle. Each round we will determine the possible next round,
     * based on available {@link Spell}s and the {@link MagePlayer} mana. Using {@link DijkstraSearcher}'s algorithm, we will attempt to find the
     * shortest path for mana usage that still lead to a victory.
     *
     * @param player                   the {@link MagePlayer}
     * @param boss                     the {@link MageBoss} to defeat
     * @param healthLossEachPlayerTurn the health the player loses at the start of each turn
     * @return the lowest amount of mana that wins the battle
     * @see BattleRound
     * @see DijkstraSearcher
     */
    public static long findCheapestManaCostToWinBattle(final MagePlayer player, final MageBoss boss, final int healthLossEachPlayerTurn) {
        final SearchNode searchNode = BattleRound.createFirstRound(player, boss, healthLossEachPlayerTurn);
        return DijkstraSearcher.findShortestDistance(searchNode);
    }
}
