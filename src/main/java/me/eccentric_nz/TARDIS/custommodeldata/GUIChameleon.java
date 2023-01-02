/*
 * Copyright (C) 2023 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

public enum GUIChameleon {

    // Chameleon Circuit
    BUTTON_APPLY(1, 0, Material.COMPARATOR),
    BUTTON_CHAMELEON(17, 10, Material.BOWL),
    BUTTON_ADAPT(11, 11, Material.BOWL),
    BUTTON_INVISIBLE(22, 12, Material.BOWL),
    BUTTON_SHORT(28, 13, Material.BOWL),
    BUTTON_CONSTRUCT(18, 14, Material.BOWL),
    BUTTON_CLOSE(1, 26, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIChameleon(int customModelData, int slot, Material material) {
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
        return TARDIS.plugin.getLanguage().getString(s);
    }
}
