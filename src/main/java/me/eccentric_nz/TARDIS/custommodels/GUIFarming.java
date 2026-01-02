/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.RoomVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUIFarming {

    // TARDIS Farming
    ALLAY(RoomVariant.ALLAY.getKey(), 0, Material.LIGHT_BLUE_CONCRETE, "Allay"),
    APIARY(RoomVariant.APIARY.getKey(), 1, Material.BEE_NEST, "Bee"),
    AQUARIUM(RoomVariant.AQUARIUM.getKey(), 2, Material.TUBE_CORAL_BLOCK, "Fish"),
    BAMBOO(RoomVariant.BAMBOO.getKey(), 3, Material.BAMBOO, "Panda"),
    BIRDCAGE(RoomVariant.BIRDCAGE.getKey(), 4, Material.YELLOW_GLAZED_TERRACOTTA, "Bird"),
    FARM(RoomVariant.FARM.getKey(), 5, Material.DIRT, "Cow & Mooshroom, Sheep, Pig, Chicken"),
    GEODE(RoomVariant.GEODE.getKey(), 6, Material.AMETHYST_BLOCK, "Axolotl"),
    HAPPY(RoomVariant.HAPPY.getKey(), 7, Material.STRIPPED_OAK_LOG, "Happy Ghast"),
    HUTCH(RoomVariant.HUTCH.getKey(), 8, Material.ACACIA_LOG, "Rabbit"),
    IGLOO(RoomVariant.IGLOO.getKey(), 18, Material.PACKED_ICE, "Polar Bear"),
    IISTUBIL(RoomVariant.IISTUBIL.getKey(), 19, Material.WHITE_GLAZED_TERRACOTTA, "Camel"),
    LAVA(RoomVariant.LAVA.getKey(), 20, Material.MAGMA_BLOCK, "Strider"),
    MANGROVE(RoomVariant.MANGROVE.getKey(), 21, Material.MUDDY_MANGROVE_ROOTS, "Frog"),
    NAUTILUS(RoomVariant.NAUTILUS.getKey(), 22, Material.DEAD_HORN_CORAL_BLOCK, "Nautilus"),
    PEN(RoomVariant.PEN.getKey(), 23, Material.MOSS_BLOCK, "Sniffer"),
    STABLE(RoomVariant.STABLE.getKey(), 24, Material.HAY_BLOCK, "Horse"),
    STALL(RoomVariant.STALL.getKey(), 25, Material.BROWN_GLAZED_TERRACOTTA, "Llama"),
    VILLAGE(RoomVariant.VILLAGE.getKey(), 26, Material.OAK_LOG, "Villager"),
    ON(null, -1, Material.LIME_WOOL, "On"),
    OFF(null, -1, Material.RED_WOOL, "Off"),
    CLOSE(GuiVariant.CLOSE.getKey(), 53, Material.BOWL, "Close");

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
