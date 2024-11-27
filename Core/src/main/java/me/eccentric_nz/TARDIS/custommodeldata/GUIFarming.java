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

import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.custommodeldata.keys.RoomBlock;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUIFarming {

    // TARDIS Farming
    ALLAY(RoomBlock.ALLAY.getKey(), 0, Material.LIGHT_BLUE_CONCRETE, "Allay"),
    APIARY(RoomBlock.APIARY.getKey(), 1, Material.BEE_NEST, "Bee"),
    AQUARIUM(RoomBlock.AQUARIUM.getKey(), 2, Material.TUBE_CORAL_BLOCK, "Fish"),
    BAMBOO(RoomBlock.BAMBOO.getKey(), 3, Material.BAMBOO, "Panda"),
    BIRDCAGE(RoomBlock.BIRDCAGE.getKey(), 4, Material.YELLOW_GLAZED_TERRACOTTA, "Bird"),
    FARM(RoomBlock.FARM.getKey(), 5, Material.DIRT, "Cow & Mooshroom, Sheep, Pig, Chicken"),
    GEODE(RoomBlock.GEODE.getKey(), 6, Material.AMETHYST_BLOCK, "Axolotl"),
    HUTCH(RoomBlock.HUTCH.getKey(), 7, Material.ACACIA_LOG, "Rabbit"),
    IGLOO(RoomBlock.IGLOO.getKey(), 8, Material.PACKED_ICE, "Polar Bear"),
    IISTUBIL(RoomBlock.IISTUBIL.getKey(), 18, Material.WHITE_GLAZED_TERRACOTTA, "Camel"),
    LAVA(RoomBlock.LAVA.getKey(), 19, Material.MAGMA_BLOCK, "Strider"),
    MANGROVE(RoomBlock.MANGROVE.getKey(), 20, Material.MUDDY_MANGROVE_ROOTS, "Frog"),
    PEN(RoomBlock.PEN.getKey(), 21, Material.MOSS_BLOCK, "Sniffer"),
    STABLE(RoomBlock.STABLE.getKey(), 22, Material.HAY_BLOCK, "Horse"),
    STALL(RoomBlock.STALL.getKey(), 23, Material.BROWN_GLAZED_TERRACOTTA, "Llama"),
    VILLAGE(RoomBlock.VILLAGE.getKey(), 24, Material.OAK_LOG, "Villager"),
    ON(null, -1, Material.LIME_WOOL, "On"),
    OFF(null, -1, Material.RED_WOOL, "Off"),
    CLOSE(Bowl.CLOSE.getKey(), 35, Material.BOWL, "Close");

    private final NamespacedKey model;
    private final int slot;
    private final Material material;
    private final String mob;

    GUIFarming(NamespacedKey model, int slot, Material material, String mob) {
        this.model = model;
        this.slot = slot;
        this.material = material;
        this.mob = mob;
    }

    public NamespacedKey getModel() {
        return model;
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
