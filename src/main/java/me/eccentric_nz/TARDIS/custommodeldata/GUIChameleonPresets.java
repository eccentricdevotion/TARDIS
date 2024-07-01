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

public record GUIChameleonPresets() {

    // Chameleon Presets
    public static GUIData GO_TO_PAGE_2 = new GUIData(12, 52, Material.ARROW);
    public static GUIData NEW = new GUIData(151, 49, Material.BOWL);
    public static GUIData SAVE = new GUIData(74, 50, Material.BOWL);
    public static GUIData USE_SELECTED = new GUIData(152, 45, Material.BOWL);
    public static GUIData UPDATE_SELECTED = new GUIData(153, 47, Material.BOWL);
    public static GUIData DELETE_SELECTED = new GUIData(1, 46, Material.BUCKET);
    public static GUIData CURRENT = new GUIData(17, 50, Material.BOWL);
    public static GUIData SAVED = new GUIData(18, 51, Material.BOWL);
    public static GUIData BACK = new GUIData(8, 51, Material.BOWL);
    public static GUIData CLOSE = new GUIData(1, 53, Material.BOWL);
}
