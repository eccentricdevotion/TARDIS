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

public record GUIMap() {

    // TARDIS Map
    public static GUIData BUTTON_UP = new GUIData(1, 1, Material.CYAN_WOOL);
    public static GUIData BUTTON_DOWN = new GUIData(2, 18, Material.CYAN_WOOL);
    public static GUIData BUTTON_LEFT = new GUIData(3, 9, Material.CYAN_WOOL);
    public static GUIData BUTTON_RIGHT = new GUIData(4, 11, Material.CYAN_WOOL);
    public static GUIData BUTTON_MAP = new GUIData(2, 10, Material.MAP);
    public static GUIData BUTTON_LEVEL_B = new GUIData(1, 27, Material.WHITE_WOOL);
    public static GUIData BUTTON_LEVEL = new GUIData(2, 28, Material.YELLOW_WOOL);
    public static GUIData BUTTON_LEVEL_T = new GUIData(3, 29, Material.WHITE_WOOL);
    public static GUIData BUTTON_CLOSE = new GUIData(1, 45, Material.BOWL);
    public static GUIData BUTTON_TRANSMAT = new GUIData(133, 46, Material.BOWL);
    public static GUIData BUTTON_WHERE = new GUIData(2, 47, Material.COMPASS);
    public static GUIData BUTTON_MAP_ON = new GUIData(1, -1, Material.BLACK_WOOL);
}
