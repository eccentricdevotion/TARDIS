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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.custommodeldata;

import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUIWeather {

    // tardis Weather Menu
    CLEAR(1, 0, Material.SUNFLOWER),
    RAIN(2, 1, Material.WATER_BUCKET),
    THUNDER(4, 2, Material.GUNPOWDER),
    EXCITE(1, 5, Material.FIREWORK_ROCKET),
    CLOSE(1, 8, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUIWeather(int customModelData, int slot, Material material) {
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
        return TARDISStringUtils.uppercaseFirst(s);
    }
}
