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

import org.bukkit.Material;

public record GUIChameleonTemplate() {

    // Chameleon Template
    public static GUIData BACK_HELP = new GUIData(1, 0, Material.ARROW);
    public static GUIData INFO_TEMPLATE = new GUIData(57, 4, Material.BOWL);
    public static GUIData GO_CONSTRUCT = new GUIData(4, 8, Material.ARROW);
    public static GUIData COL_L_FRONT = new GUIData(35, 45, Material.BOWL);
    public static GUIData COL_L_MIDDLE = new GUIData(36, 36, Material.BOWL);
    public static GUIData COL_L_BACK = new GUIData(37, 27, Material.BOWL);
    public static GUIData COL_B_MIDDLE = new GUIData(38, 28, Material.BOWL);
    public static GUIData COL_R_BACK = new GUIData(39, 29, Material.BOWL);
    public static GUIData COL_R_MIDDLE = new GUIData(40, 38, Material.BOWL);
    public static GUIData COL_R_FRONT = new GUIData(41, 47, Material.BOWL);
    public static GUIData COL_F_MIDDLE = new GUIData(42, 46, Material.BOWL);
    public static GUIData COL_C_LAMP = new GUIData(43, 37, Material.BOWL);
}
