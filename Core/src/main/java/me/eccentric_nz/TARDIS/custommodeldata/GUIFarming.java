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
package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIFarming {

    // TARDIS Farming
    ALLAY(1, 0, Material.LIGHT_BLUE_CONCRETE, "Allay"),
    APIARY(1, 1, Material.BEE_NEST, "Bee"),
    AQUARIUM(1, 2, Material.TUBE_CORAL_BLOCK, "Fish"),
    BAMBOO(1, 3, Material.BAMBOO, "Panda"),
    BIRDCAGE(1, 4, Material.YELLOW_GLAZED_TERRACOTTA, "Bird"),
    FARM(1, 5, Material.DIRT, "Cow & Mooshroom, Sheep, Pig, Chicken"),
    GEODE(1, 6, Material.AMETHYST_BLOCK, "Axolotl"),
    HUTCH(1, 7, Material.ACACIA_LOG, "Rabbit"),
    IGLOO(1, 8, Material.PACKED_ICE, "Polar Bear"),
    IISTUBIL(1, 18, Material.WHITE_GLAZED_TERRACOTTA, "Camel"),
    LAVA(1, 19, Material.MAGMA_BLOCK, "Strider"),
    MANGROVE(1, 20, Material.MUDDY_MANGROVE_ROOTS, "Frog"),
    PEN(1, 21, Material.MOSS_BLOCK, "Sniffer"),
    STABLE(1, 22, Material.HAY_BLOCK, "Horse"),
    STALL(1, 23, Material.BROWN_GLAZED_TERRACOTTA, "Llama"),
    VILLAGE(1, 24, Material.OAK_LOG, "Villager"),
    ON(-1, -1, Material.LIME_WOOL, "On"),
    OFF(-1, -1, Material.RED_WOOL, "Off"),
    CLOSE(1, 35, Material.BOWL, "Close");

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String mob;

    GUIFarming(int customModelData, int slot, Material material, String mob) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        this.mob = mob;
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

    public String getRoomName() {
        String s = toString();
        return TARDISStringUtils.sentenceCase(s);
    }

    public String getMob() {
        return mob;
    }
}
