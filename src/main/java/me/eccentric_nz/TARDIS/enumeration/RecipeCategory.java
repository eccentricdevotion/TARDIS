/*
 * Copyright (C) 2021 eccentric_nz
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
import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum RecipeCategory {

    ACCESSORIES(Material.LEATHER_HELMET, 10000037, 9, ChatColor.GREEN, ChatColor.DARK_GREEN),
    CONSOLE_CIRCUITS(Material.GLOWSTONE_DUST, 10001977, 11, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE),
    FOOD(Material.MELON_SLICE, 10000002, 13, ChatColor.GRAY, ChatColor.DARK_GRAY),
    ITEM_CIRCUITS(Material.GLOWSTONE_DUST, 10001967, 15, ChatColor.RED, ChatColor.DARK_RED),
    ITEMS(Material.GOLD_NUGGET, 12, 17, ChatColor.GREEN, ChatColor.DARK_GREEN),
    ROTORS(Material.LIGHT_GRAY_DYE, 10000002, 18, ChatColor.GOLD, ChatColor.YELLOW),
    SONIC_CIRCUITS(Material.GLOWSTONE_DUST, 10001971, 20, ChatColor.BLUE, ChatColor.DARK_BLUE),
    SONIC_UPGRADES(Material.BLAZE_ROD, 10000009, 22, ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE),
    STORAGE_DISKS(Material.MUSIC_DISC_STRAD, 10000001, 24, ChatColor.AQUA, ChatColor.DARK_AQUA),
    MISC(Material.WATER_BUCKET, 1, 26, ChatColor.GRAY, ChatColor.DARK_GRAY),
    UNCRAFTABLE(Material.CRAFTING_TABLE, 1, -1, ChatColor.GRAY, ChatColor.DARK_GRAY),
    UNUSED(Material.STONE, 1, -1, ChatColor.RED, ChatColor.DARK_RED);

    private final Material material;
    private final int customModelData;
    private final int slot;
    private final ChatColor keyColour;
    private final ChatColor valueColour;

    RecipeCategory(Material material, int customModelData, int slot, ChatColor keyColour, ChatColor valueColour) {
        this.material = material;
        this.customModelData = customModelData;
        this.slot = slot;
        this.keyColour = keyColour;
        this.valueColour = valueColour;
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

    public ChatColor getKeyColour() {
        return keyColour;
    }

    public ChatColor getValueColour() {
        return valueColour;
    }
}
