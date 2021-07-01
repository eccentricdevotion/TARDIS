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
package me.eccentric_nz.tardis.custommodeldata;

import me.eccentric_nz.tardis.utility.TardisStringUtils;
import org.bukkit.Material;

public enum GuiSaves {

    // TARDIS saves
    REARRANGE_SAVES(5, 45, Material.ARROW),
    LOAD_TARDIS_AREAS(1, 53, Material.MAP),
    DELETE_SAVE(1, 47, Material.BUCKET),
    LOAD_MY_SAVES(138, 49, Material.BOWL),
    LOAD_SAVES_FROM_THIS_TARDIS(139, 49, Material.BOWL),
    GO_TO_PAGE_1(11, 51, Material.ARROW),
    GO_TO_PAGE_2(12, 51, Material.ARROW);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GuiSaves(int customModelData, int slot, Material material) {
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
        return TardisStringUtils.sentenceCase(s);
    }
}
