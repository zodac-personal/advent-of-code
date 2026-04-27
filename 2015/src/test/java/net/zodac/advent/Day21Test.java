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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.zodac.advent.input.InputReader;
import net.zodac.advent.pojo.rpg.warrior.Armour;
import net.zodac.advent.pojo.rpg.warrior.Equipment;
import net.zodac.advent.pojo.rpg.warrior.Ring;
import net.zodac.advent.pojo.rpg.warrior.Warrior;
import net.zodac.advent.pojo.rpg.warrior.Weapon;
import net.zodac.advent.util.CollectionUtils;
import net.zodac.advent.util.PowerSetFilter;
import net.zodac.advent.util.PowerSetUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests to verify answers for {@link Day21}.
 */
class Day21Test {

    private static final String INPUT_FILENAME = "day21.txt";
    private static final List<List<Equipment>> EQUIPMENT_COMBINATIONS = getEquipmentCombinations();

    // TODO: Example?

    @Test
    void part1() {
        final String value = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .readAllAsSingleString();
        final Warrior boss = Warrior.parse(value);

        final long part1Result = Day21.costOfCheapestEquipmentThatDefeatsBoss(EQUIPMENT_COMBINATIONS, boss);
        assertThat(part1Result)
            .isEqualTo(91L);
    }

    @Test
    void part2() {
        final String value = InputReader
            .forPuzzle(INPUT_FILENAME)
            .asStrings()
            .readAllAsSingleString();
        final Warrior boss = Warrior.parse(value);

        final long part2Result = Day21.costOfPriciestArmourThatLosesToBoss(EQUIPMENT_COMBINATIONS, boss);
        assertThat(part2Result)
            .isEqualTo(158L);
    }

    private static List<List<Equipment>> getEquipmentCombinations() {
        final List<Equipment> equipment = generateEquipment();

        final Map<Class<? extends Equipment>, Predicate<List<Equipment>>> predicates = Map.of(
            Armour.class, equipments -> equipments != null && equipments.size() == 1,
            Ring.class, equipments -> {
                final int count = equipments == null ? 0 : equipments.size();
                return count > 0 && count <= 2 && !CollectionUtils.containsDuplicates(equipments);
            },
            Weapon.class, equipments -> equipments != null && equipments.size() == 1
        );

        final PowerSetFilter<Class<? extends Equipment>, Equipment> powerSetFilter = new PowerSetFilter<>(
            Equipment::getClass,
            predicates
        );

        return PowerSetUtils.getFilteredPowerList(equipment, powerSetFilter);
    }

    private static List<Equipment> generateEquipment() {
        return List.of(
            // Weapons
            Weapon.create("Dagger", 8, 4, 0),
            Weapon.create("Shortsword", 10, 5, 0),
            Weapon.create("Warhammer", 25, 6, 0),
            Weapon.create("Longsword", 40, 7, 0),
            Weapon.create("Greataxe", 74, 8, 0),

            // Armour, including blank entry to simulate no armour being chosen
            Armour.create("None", 0, 0, 0),
            Armour.create("Leather", 13, 0, 1),
            Armour.create("Chainmail", 31, 0, 2),
            Armour.create("Splintmail", 53, 0, 3),
            Armour.create("Bandedmail", 75, 0, 4),
            Armour.create("Platemail", 102, 0, 5),

            // Rings, duplicated to simulate two rings being chosen
            Ring.create("Damage +1", 25, 1, 0),
            Ring.create("Damage +1", 25, 1, 0),
            Ring.create("Damage +2", 50, 2, 0),
            Ring.create("Damage +2", 50, 2, 0),
            Ring.create("Damage +3", 100, 3, 0),
            Ring.create("Damage +3", 100, 3, 0),
            Ring.create("Defence +1", 20, 0, 1),
            Ring.create("Defence +1", 20, 0, 1),
            Ring.create("Defence +2", 40, 0, 2),
            Ring.create("Defence +2", 40, 0, 2),
            Ring.create("Defence +3", 80, 0, 3),
            Ring.create("Defence +3", 80, 0, 3)
        );
    }
}
