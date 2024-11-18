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
import org.bukkit.Material;

public record GUIChameleonHelp() {

    // Chameleon Help
    public static GUIData BACK_CHAM_OPTS = new GUIData(Arrow.BACK.getKey(), 0, Material.ARROW);
    public static GUIData INFO_HELP_1 = new GUIData(Bowl.INFO.getKey(), 3, Material.BOWL);
    public static GUIData INFO_HELP_2 = new GUIData(Bowl.INFO.getKey(), 4, Material.BOWL);
    public static GUIData INFO_HELP_3 = new GUIData(Bowl.INFO.getKey(), 16, Material.BOWL);
    public static GUIData INFO_HELP_4 = new GUIData(Bowl.INFO.getKey(), 19, Material.BOWL);
    public static GUIData COL_L_FRONT = new GUIData(Bowl.ONE.getKey(), 45, Material.BOWL);
    public static GUIData COL_L_MIDDLE = new GUIData(Bowl.TWO.getKey(), 36, Material.BOWL);
    public static GUIData COL_L_BACK = new GUIData(Bowl.THREE.getKey(), 27, Material.BOWL);
    public static GUIData COL_B_MIDDLE = new GUIData(Bowl.FOUR.getKey(), 28, Material.BOWL);
    public static GUIData COL_R_BACK = new GUIData(Bowl.FIVE.getKey(), 29, Material.BOWL);
    public static GUIData COL_R_MIDDLE = new GUIData(Bowl.SIX.getKey(), 38, Material.BOWL);
    public static GUIData COL_R_FRONT = new GUIData(Bowl.SEVEN.getKey(), 47, Material.BOWL);
    public static GUIData COL_F_MIDDLE = new GUIData(Bowl.EIGHT.getKey(), 46, Material.BOWL);
    public static GUIData COL_C_LAMP = new GUIData(Bowl.NINE.getKey(), 37, Material.BOWL);
    public static GUIData ROW_1 = new GUIData(Bowl.ONE.getKey(), 52, Material.BOWL);
    public static GUIData ROW_2 = new GUIData(Bowl.TWO.getKey(), 43, Material.BOWL);
    public static GUIData ROW_3 = new GUIData(Bowl.THREE.getKey(), 34, Material.BOWL);
    public static GUIData ROW_4 = new GUIData(Bowl.FOUR.getKey(), 25, Material.BOWL);
    public static GUIData VIEW_TEMP = new GUIData(Bowl.VIEW_TEMP.getKey(), 40, Material.BOWL);
}
