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

public enum GUISavedPrograms {

    // Saved Programs
    BACK_TO_EDITOR(1, 45, Material.ARROW),
    LOAD_SELECTED_PROGRAM_IN_EDITOR(61, 47, Material.BOWL),
    DEACTIVATE_SELECTED_PROGRAM(2, 48, Material.BUCKET),
    DELETE_SELECTED_PROGRAM(1, 49, Material.BUCKET),
    CHECK_OUT_SELECTED_PROGRAM(33, 51, Material.BOWL),
    CLOSE(1, 53, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUISavedPrograms(int customModelData, int slot, Material material) {
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
