/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUISeeds {

    // TARDIS Seeds Menu
    ARS(1, 0, Material.QUARTZ_BLOCK),
    A_BIGGER(1, 1, Material.GOLD_BLOCK),
    DEFAULT(1, 2, Material.IRON_BLOCK),
    TENTH_DOCTORS(1, 3, Material.NETHER_WART_BLOCK),
    SUPERSIZED_DELUXE(1, 4, Material.DIAMOND_BLOCK),
    ELEVENTH_DOCTORS(1, 5, Material.EMERALD_BLOCK),
    ENDER(1, 6, Material.PURPUR_BLOCK),
    FACTORY_1ST_DOCTORS(1, 7, Material.YELLOW_CONCRETE_POWDER),
    THE_MASTERS(1, 8, Material.NETHER_BRICK),
    WOOD(1, 9, Material.BOOKSHELF),
    A_SANDTONE_PYRAMID(1, 10, Material.SANDSTONE_STAIRS),
    REDSTONE(1, 11, Material.REDSTONE_BLOCK),
    STEAMPUNK(1, 12, Material.COAL_BLOCK),
    THIRTEENTH_DOCTORS(1, 13, Material.ORANGE_CONCRETE),
    FOURTH_DOCTORS(1, 14, Material.LAPIS_BLOCK),
    TWELFTH_DOCTORS(1, 15, Material.PRISMARINE),
    WAR_DOCTORS(1, 16, Material.WHITE_TERRACOTTA),
    SUPER_DUPER_CUSTOM(1, 17, Material.OBSIDIAN),
    THE_ORIGINAL_BIGGER(1, 18, Material.ORANGE_GLAZED_TERRACOTTA),
    THE_ORIGINAL_DEFAULT(1, 19, Material.LIGHT_GRAY_GLAZED_TERRACOTTA),
    THE_ORIGINAL_DELUXE(1, 20, Material.LIME_GLAZED_TERRACOTTA),
    THE_ORIGINAL_ELEVENTH(1, 21, Material.CYAN_GLAZED_TERRACOTTA),
    THE_ORIGINAL_REDSTONE(1, 22, Material.RED_GLAZED_TERRACOTTA),
    CLOSE(1, 26, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUISeeds(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        String s = toString();
        if (s.endsWith("DOCTORS")) {
            return TARDISStringUtils.capitalise(s.replace("DOCTORS", "DOCTOR'S")) + " Console";
        } else if (s.endsWith("MASTERS")) {
            return TARDISStringUtils.capitalise(s.replace("MASTERS", "MASTER'S")) + " Console";
        } else {
            return TARDISStringUtils.capitalise(s) + " Console";
        }
    }
}
