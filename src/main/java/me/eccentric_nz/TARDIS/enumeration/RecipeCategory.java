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
package me.eccentric_nz.tardis.enumeration;

import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public enum RecipeCategory {

    ACCESSORIES(Material.LEATHER_HELMET, 10000037, 9, ChatColor.GREEN),
    CONSOLE_CIRCUITS(Material.GLOWSTONE_DUST, 10001977, 11, ChatColor.LIGHT_PURPLE),
    FOOD(Material.MELON_SLICE, 10000002, 13, ChatColor.GRAY),
    ITEM_CIRCUITS(Material.GLOWSTONE_DUST, 10001967, 15, ChatColor.RED),
    ITEMS(Material.GOLD_NUGGET, 12, 17, ChatColor.BLUE),
    ROTORS(Material.LIGHT_GRAY_DYE, 10000002, 18, ChatColor.GOLD),
    SONIC_CIRCUITS(Material.GLOWSTONE_DUST, 10001971, 20, ChatColor.GREEN),
    SONIC_UPGRADES(Material.BLAZE_ROD, 10000009, 22, ChatColor.LIGHT_PURPLE),
    STORAGE_DISKS(Material.MUSIC_DISC_STRAD, 10000001, 24, ChatColor.AQUA),
    MISC(Material.WATER_BUCKET, 1, 26, ChatColor.GRAY),
    UNCRAFTABLE(Material.CRAFTING_TABLE, 1, -1, ChatColor.DARK_RED),
    UNUSED(Material.STONE, 1, -1, ChatColor.BLACK);

    private final Material material;
    private final int customModelData;
    private final int slot;
    private final ChatColor colour;

    RecipeCategory(Material material, int customModelData, int slot, ChatColor colour) {
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

    public ChatColor getColour() {
        return colour;
    }
}
