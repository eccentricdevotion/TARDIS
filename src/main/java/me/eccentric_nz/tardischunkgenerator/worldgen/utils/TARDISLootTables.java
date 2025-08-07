/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.worldgen.utils;

import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.util.List;

public class TARDISLootTables {

    /**
     * A list of loot tables to populate TARDIS planet chests
     */
    public static final List<LootTable> LOOT = List.of(LootTables.ABANDONED_MINESHAFT.getLootTable(),
            LootTables.BURIED_TREASURE.getLootTable(), LootTables.DESERT_PYRAMID.getLootTable(),
            LootTables.IGLOO_CHEST.getLootTable(), LootTables.JUNGLE_TEMPLE.getLootTable(),
            LootTables.SHIPWRECK_TREASURE.getLootTable(), LootTables.SIMPLE_DUNGEON.getLootTable(),
            LootTables.SPAWN_BONUS_CHEST.getLootTable(), LootTables.STRONGHOLD_LIBRARY.getLootTable(),
            LootTables.VILLAGE_ARMORER.getLootTable(), LootTables.VILLAGE_BUTCHER.getLootTable(),
            LootTables.VILLAGE_CARTOGRAPHER.getLootTable(), LootTables.VILLAGE_DESERT_HOUSE.getLootTable(),
            LootTables.VILLAGE_FISHER.getLootTable(), LootTables.VILLAGE_FLETCHER.getLootTable(),
            LootTables.VILLAGE_MASON.getLootTable(), LootTables.VILLAGE_PLAINS_HOUSE.getLootTable(),
            LootTables.VILLAGE_SAVANNA_HOUSE.getLootTable(), LootTables.VILLAGE_SHEPHERD.getLootTable(),
            LootTables.VILLAGE_SNOWY_HOUSE.getLootTable(), LootTables.VILLAGE_TAIGA_HOUSE.getLootTable(),
            LootTables.VILLAGE_TANNERY.getLootTable(), LootTables.VILLAGE_TEMPLE.getLootTable(),
            LootTables.VILLAGE_TOOLSMITH.getLootTable(), LootTables.VILLAGE_WEAPONSMITH.getLootTable(),
            LootTables.WOODLAND_MANSION.getLootTable(), LootTables.PILLAGER_OUTPOST.getLootTable()
    );
}
