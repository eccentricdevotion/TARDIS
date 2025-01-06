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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.custommodels.keys.Wool;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum SonicConfig {

    NOT_UPGRADED(Wool.NOT_UPGRADED.getKey(), Material.GRAY_WOOL),
    ENABLED(Wool.ENABLED.getKey(), Material.LIME_WOOL),
    DISABLED(Wool.DISABLED.getKey(), Material.RED_WOOL);

    private final NamespacedKey model;
    private final Material material;

    SonicConfig(NamespacedKey model, Material material) {
        this.model = model;
        this.material = material;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }
}
