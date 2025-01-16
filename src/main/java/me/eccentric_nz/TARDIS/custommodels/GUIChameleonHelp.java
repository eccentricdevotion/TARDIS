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
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import org.bukkit.Material;

public record GUIChameleonHelp() {

    // Chameleon Help
    public static GUIData BACK_CHAM_OPTS = new GUIData(GuiVariant.BACK.getKey(), 0, Material.ARROW);
    public static GUIData INFO_HELP_1 = new GUIData(GuiVariant.INFO.getKey(), 3, Material.BOWL);
    public static GUIData INFO_HELP_2 = new GUIData(GuiVariant.INFO.getKey(), 4, Material.BOWL);
    public static GUIData INFO_HELP_3 = new GUIData(GuiVariant.INFO.getKey(), 16, Material.BOWL);
    public static GUIData INFO_HELP_4 = new GUIData(GuiVariant.INFO.getKey(), 19, Material.BOWL);
    public static GUIData COL_L_FRONT = new GUIData(GuiVariant.ONE.getKey(), 45, Material.BOWL);
    public static GUIData COL_L_MIDDLE = new GUIData(GuiVariant.TWO.getKey(), 36, Material.BOWL);
    public static GUIData COL_L_BACK = new GUIData(GuiVariant.THREE.getKey(), 27, Material.BOWL);
    public static GUIData COL_B_MIDDLE = new GUIData(GuiVariant.FOUR.getKey(), 28, Material.BOWL);
    public static GUIData COL_R_BACK = new GUIData(GuiVariant.FIVE.getKey(), 29, Material.BOWL);
    public static GUIData COL_R_MIDDLE = new GUIData(GuiVariant.SIX.getKey(), 38, Material.BOWL);
    public static GUIData COL_R_FRONT = new GUIData(GuiVariant.SEVEN.getKey(), 47, Material.BOWL);
    public static GUIData COL_F_MIDDLE = new GUIData(GuiVariant.EIGHT.getKey(), 46, Material.BOWL);
    public static GUIData COL_C_LAMP = new GUIData(GuiVariant.NINE.getKey(), 37, Material.BOWL);
    public static GUIData ROW_1 = new GUIData(GuiVariant.ONE.getKey(), 52, Material.BOWL);
    public static GUIData ROW_2 = new GUIData(GuiVariant.TWO.getKey(), 43, Material.BOWL);
    public static GUIData ROW_3 = new GUIData(GuiVariant.THREE.getKey(), 34, Material.BOWL);
    public static GUIData ROW_4 = new GUIData(GuiVariant.FOUR.getKey(), 25, Material.BOWL);
    public static GUIData VIEW_TEMP = new GUIData(GuiVariant.VIEW_TEMPLATE.getKey(), 40, Material.BOWL);
}
