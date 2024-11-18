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

public record GUIChameleonTemplate() {

    // Chameleon Template
    public static GUIData BACK_HELP = new GUIData(Arrow.BACK.getKey(), 0, Material.ARROW);
    public static GUIData INFO_TEMPLATE = new GUIData(Bowl.INFO.getKey(), 4, Material.BOWL);
    public static GUIData GO_CONSTRUCT = new GUIData(Arrow.GO.getKey(), 8, Material.ARROW);
    public static GUIData COL_L_FRONT = new GUIData(Bowl.ONE.getKey(), 45, Material.BOWL);
    public static GUIData COL_L_MIDDLE = new GUIData(Bowl.TWO.getKey(), 36, Material.BOWL);
    public static GUIData COL_L_BACK = new GUIData(Bowl.THREE.getKey(), 27, Material.BOWL);
    public static GUIData COL_B_MIDDLE = new GUIData(Bowl.FOUR.getKey(), 28, Material.BOWL);
    public static GUIData COL_R_BACK = new GUIData(Bowl.FIVE.getKey(), 29, Material.BOWL);
    public static GUIData COL_R_MIDDLE = new GUIData(Bowl.SIX.getKey(), 38, Material.BOWL);
    public static GUIData COL_R_FRONT = new GUIData(Bowl.SEVEN.getKey(), 47, Material.BOWL);
    public static GUIData COL_F_MIDDLE = new GUIData(Bowl.EIGHT.getKey(), 46, Material.BOWL);
    public static GUIData COL_C_LAMP = new GUIData(Bowl.NINE.getKey(), 37, Material.BOWL);
}
