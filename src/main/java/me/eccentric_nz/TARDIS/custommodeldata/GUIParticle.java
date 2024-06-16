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

import org.bukkit.Material;

public enum GUIParticle {

    // TARDIS Autonomous
    SHAPE_INFO(2, 0, Material.LAPIS_BLOCK),
    SHAPE(1, -1, Material.LAPIS_LAZULI),
    EFFECT_INFO(2, 9, Material.REDSTONE_BLOCK),
    EFFECT(1, -1, Material.REDSTONE),
    TOGGLE(19, 27, Material.REPEATER),
    TEST(1, 35, Material.COPPER_INGOT),
    MINUS(40, -1, Material.PAPER),
    PLUS(39, -1, Material.PAPER),
    DENSITY(5, 46, Material.IRON_INGOT),
    SPEED(1, 50, Material.GOLD_INGOT),
    CLOSE(1, 54, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIParticle(int customModelData, int slot, Material material) {
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
}
