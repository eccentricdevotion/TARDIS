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

public enum GUIInteriorSounds {

    // TARDIS Interior Sounds
    ALIEN(3, 0, Material.BOWL),
    ATMOSPHERE(7, 1, Material.BOWL),
    COMPUTER(46, 2, Material.BOWL),
    COPPER(48, 3, Material.BOWL),
    CORAL(49, 4, Material.BOWL),
    GALAXY(53, 5, Material.BOWL),
    LEARNING(59, 6, Material.BOWL),
    MIND(63, 7, Material.BOWL),
    NEON(64, 8, Material.BOWL),
    SLEEPING(78, 9, Material.BOWL),
    VOID(85, 10, Material.BOWL),
    RANDOM(70, 11, Material.BOWL),
    ACTION(88, 15, Material.BOWL),
    CLOSE(1, 17, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIInteriorSounds(int customModelData, int slot, Material material) {
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
        return TARDISStringUtils.sentenceCase(s);
    }
}
