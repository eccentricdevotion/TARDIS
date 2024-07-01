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

public record GUISaves() {

    // TARDIS saves
    public static GUIData HOME = new GUIData(140, 0, Material.BOWL);
    public static GUIData REARRANGE_SAVES = new GUIData(5, 45, Material.ARROW);
    public static GUIData LOAD_TARDIS_AREAS = new GUIData(1, 53, Material.MAP);
    public static GUIData DELETE_SAVE = new GUIData(1, 47, Material.BUCKET);
    public static GUIData LOAD_MY_SAVES = new GUIData(138, 49, Material.BOWL);
    public static GUIData LOAD_SAVES_FROM_THIS_TARDIS = new GUIData(139, 49, Material.BOWL);
    public static GUIData BACK_TO_PLANETS = new GUIData(1, 51, Material.ARROW);
    public static GUIData GO_TO_PAGE_1 = new GUIData(11, 51, Material.ARROW);
    public static GUIData GO_TO_PAGE_2 = new GUIData(12, 51, Material.ARROW);
}
