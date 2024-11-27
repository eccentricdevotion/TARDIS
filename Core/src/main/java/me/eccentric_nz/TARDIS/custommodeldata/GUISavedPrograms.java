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

import me.eccentric_nz.TARDIS.custommodeldata.keys.Arrow;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bucket;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUISavedPrograms {

    // Saved Programs
    BACK_TO_EDITOR(Arrow.BACK.getKey(), 45, Material.ARROW),
    LOAD_SELECTED_PROGRAM_IN_EDITOR(Bowl.LOAD_SELECTED_PROGRAM_IN_EDITOR.getKey(), 47, Material.BOWL),
    DEACTIVATE_SELECTED_PROGRAM(Bucket.DEACTIVATE.getKey(), 48, Material.BUCKET),
    DELETE_SELECTED_PROGRAM(Bucket.DELETE.getKey(), 49, Material.BUCKET),
    CHECK_OUT_SELECTED_PROGRAM(Bowl.CHECK_OUT_SELECTED_PROGRAM.getKey(), 51, Material.BOWL),
    CLOSE(Bowl.CLOSE.getKey(), 53, Material.BOWL);

    private final NamespacedKey model;
    private final int slot;
    private final Material material;

    GUISavedPrograms(NamespacedKey model, int slot, Material material) {
        this.model = model;
        this.slot = slot;
        this.material = material;
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

    public String getName() {
        String s = toString();
        return TARDISStringUtils.sentenceCase(s);
    }
}
