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

import me.eccentric_nz.tardis.TARDISPlugin;
import org.bukkit.Material;

public enum GUIChameleonHelp {

    // Chameleon Help
    BACK_CHAM_OPTS(1, 0, Material.ARROW),
    INFO_HELP_1(57, 3, Material.BOWL),
    INFO_HELP_2(57, 4, Material.BOWL),
    INFO_HELP_3(57, 16, Material.BOWL),
    INFO_HELP_4(57, 19, Material.BOWL),
    COL_L_FRONT(35, 45, Material.BOWL, "1"),
    COL_L_MIDDLE(36, 36, Material.BOWL, "2"),
    COL_L_BACK(37, 27, Material.BOWL, "3"),
    COL_B_MIDDLE(38, 28, Material.BOWL, "4"),
    COL_R_BACK(39, 29, Material.BOWL, "5"),
    COL_R_MIDDLE(40, 38, Material.BOWL, "6"),
    COL_R_FRONT(41, 47, Material.BOWL, "7"),
    COL_F_MIDDLE(42, 46, Material.BOWL, "8"),
    COL_C_LAMP(43, 37, Material.BOWL, "9"),
    ROW_1(35, 52, Material.BOWL, "1"),
    ROW_2(36, 43, Material.BOWL, "2"),
    ROW_3(37, 34, Material.BOWL, "3"),
    ROW_4(38, 25, Material.BOWL, "4"),
    VIEW_TEMP(84, 40, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String number;

    GUIChameleonHelp(int customModelData, int slot, Material material, String number) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        this.number = number;
    }

    GUIChameleonHelp(int customModelData, int slot, Material material) {
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
            return TARDISPlugin.plugin.getChameleonGuis().getString("INFO");
        } else if (s.startsWith("COL") || s.startsWith("ROW")) {
            return number;
        } else {
            return TARDISPlugin.plugin.getChameleonGuis().getString(s);
        }
    }
}
