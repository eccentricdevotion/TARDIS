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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum SonicConfig {

    NOT_UPGRADED(1, Material.GRAY_WOOL),
    ENABLED(2, Material.LIME_WOOL),
    DISABLED(2, Material.RED_WOOL);

    private final int customModelData;
    private final Material material;

    SonicConfig(int customModelData, Material material) {
        this.customModelData = customModelData;
        this.material = material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }
}
