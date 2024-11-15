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

public record GUIWallFloor() {

    // TARDIS Wall & Floor Menu
    public static GUIData BUTTON_SCROLL_U = new GUIData(Arrow.SCROLL_UP.getKey(), 8, Material.ARROW);
    public static GUIData BUTTON_SCROLL_D = new GUIData(Arrow.SCROLL_DOWN.getKey(), 17, Material.ARROW);
    public static GUIData WALL = new GUIData(Bowl.WALL, 26, Material.BOWL);
    public static GUIData FLOOR = new GUIData(Bowl.FLOOR, 35, Material.BOWL);
    public static GUIData BUTTON_ABORT = new GUIData(Bowl.ABORT, 53, Material.BOWL);
}
