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

import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum RecipeCategory {

    SEED_BLOCKS(Material.LAPIS_BLOCK, SeedBlock.TOM.getKey(), 2, ""),
    CHEMISTRY(Material.BREWING_STAND, null, -1, "#AA0000"),
    CUSTOM_BLOCKS(Material.ANVIL, null, 8, "#AA0000"),
    ACCESSORIES(Material.LEATHER_HELMET, LeatherHelmet.BOWTIE_RED.getKey(), 9, "#55FF55"),
    //    CONTROLS(Material.LEVER, 1000, 10, "#AAFF00"),
    PLANETS(Material.SLIME_BALL, SlimeBall.DALEK_PINK.getKey(), 6, "#AAFF00"),
    CONSOLES(Material.LIGHT_GRAY_CONCRETE, ConsoleBlock.CONSOLE_LIGHT_GRAY.getKey(), 4, "#FF55FF"),
    CONSOLE_CIRCUITS(Material.GLOWSTONE_DUST, GlowstoneDust.SCANNER.getKey(), 11, "#FF55FF"),
    FOOD(Material.MELON_SLICE, MelonSlice.JELLY_BABY_ORANGE.getKey(), 13, "#AAAAAA"),
    ITEM_CIRCUITS(Material.GLOWSTONE_DUST, GlowstoneDust.SONIC.getKey(), 15, "#FF5555"),
    ITEMS(Material.GOLD_NUGGET, GoldNugget.BRASS_STRING.getKey(), 17, "#5555FF"),
    ROTORS(Material.LIGHT_GRAY_DYE, LightGrayDye.TIME_ROTOR_EARLY_OFF.getKey(), 18, "#FFAA00"),
    SONIC_CIRCUITS(Material.GLOWSTONE_DUST, GlowstoneDust.DIAMOND.getKey(), 20, "#55FF55"),
    SONIC_UPGRADES(Material.BLAZE_ROD, BlazeRod.NINTH.getKey(), 22, "#FF55FF"),
    STORAGE_DISKS(Material.MUSIC_DISC_STRAD, MusicDisc.BLANK_DISK.getKey(), 24, "#55FFFF"),
    MISC(Material.WATER_BUCKET, WaterBucket.ACID_BUCKET.getKey(), 26, "#AAAAAA"),
    MICROSCOPE(Material.SPYGLASS, null, 27, "#FFFFAA"),
    UNCRAFTABLE(Material.CRAFTING_TABLE, null, -1, "#AA0000"),
    UNUSED(Material.STONE, RoomBlock.SLOT.getKey(), -1, "#000000");

    private final Material material;
    private final NamespacedKey model;
    private final int slot;
    private final String colour;

    RecipeCategory(Material material, NamespacedKey model, int slot, String colour) {
        this.material = material;
        this.model = model;
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

    public NamespacedKey getModel() {
        return model;
    }

    public int getSlot() {
        return slot;
    }

    public String getColour() {
        return colour;
    }
}
