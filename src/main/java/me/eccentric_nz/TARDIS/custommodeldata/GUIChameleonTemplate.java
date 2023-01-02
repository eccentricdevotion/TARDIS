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

public enum GUIChameleonTemplate {

    // Chameleon Template
    BACK_HELP(1, 0, Material.ARROW),
    INFO_TEMPLATE(57, 4, Material.BOWL),
    GO_CONSTRUCT(4, 8, Material.ARROW),
    COL_L_FRONT(35, 45, Material.BOWL, "1"),
    COL_L_MIDDLE(36, 36, Material.BOWL, "2"),
    COL_L_BACK(37, 27, Material.BOWL, "3"),
    COL_B_MIDDLE(38, 28, Material.BOWL, "4"),
    COL_R_BACK(39, 29, Material.BOWL, "5"),
    COL_R_MIDDLE(40, 38, Material.BOWL, "6"),
    COL_R_FRONT(41, 47, Material.BOWL, "7"),
    COL_F_MIDDLE(42, 46, Material.BOWL, "8"),
    COL_C_LAMP(43, 37, Material.BOWL, "9");

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String number;

    GUIChameleonTemplate(int customModelData, int slot, Material material, String number) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        this.number = number;
    }

    GUIChameleonTemplate(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        number = "";
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
        if (s.startsWith("INFO")) {
            return TARDIS.plugin.getChameleonGuis().getString("INFO");
        } else if (s.startsWith("COL")) {
            return number;
        } else {
            return TARDIS.plugin.getChameleonGuis().getString(s);
        }
    }
}
