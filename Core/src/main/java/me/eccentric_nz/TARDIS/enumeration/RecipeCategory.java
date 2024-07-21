/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum RecipeCategory {

    SEED_BLOCKS(Material.LAPIS_BLOCK, 1, 2, ""),
    CHEMISTRY(Material.BREWING_STAND, 1, -1, "#AA0000"),
    CUSTOM_BLOCKS(Material.ANVIL, 1, 8, "#AA0000"),
    ACCESSORIES(Material.LEATHER_HELMET, 10000037, 9, "#55FF55"),
    //    CONTROLS(Material.LEVER, 1000, 10, "#AAFF00"),
    PLANETS(Material.SLIME_BALL, 12, 6, "#AAFF00"),
    CONSOLES(Material.LIGHT_GRAY_CONCRETE, 1001, 4, "#FF55FF"),
    CONSOLE_CIRCUITS(Material.GLOWSTONE_DUST, 10001977, 11, "#FF55FF"),
    FOOD(Material.MELON_SLICE, 10000002, 13, "#AAAAAA"),
    ITEM_CIRCUITS(Material.GLOWSTONE_DUST, 10001967, 15, "#FF5555"),
    ITEMS(Material.GOLD_NUGGET, 12, 17, "#5555FF"),
    ROTORS(Material.LIGHT_GRAY_DYE, 10000002, 18, "#FFAA00"),
    SONIC_CIRCUITS(Material.GLOWSTONE_DUST, 10001971, 20, "#55FF55"),
    SONIC_UPGRADES(Material.BLAZE_ROD, 10000009, 22, "#FF55FF"),
    STORAGE_DISKS(Material.MUSIC_DISC_STRAD, 10000001, 24, "#55FFFF"),
    MISC(Material.WATER_BUCKET, 1, 26, "#AAAAAA"),
    UNCRAFTABLE(Material.CRAFTING_TABLE, 1, -1, "#AA0000"),
    UNUSED(Material.STONE, 1, -1, "#000000");

    private final Material material;
    private final int customModelData;
    private final int slot;
    private final String colour;

    RecipeCategory(Material material, int customModelData, int slot, String colour) {
        this.material = material;
        this.customModelData = customModelData;
        this.slot = slot;
        this.colour = colour;
    }

    public String getName() {
        String s = toString();
        return TARDISStringUtils.sentenceCase(s);
    }

    public Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getSlot() {
        return slot;
    }

    public String getColour() {
        return colour;
    }
}
