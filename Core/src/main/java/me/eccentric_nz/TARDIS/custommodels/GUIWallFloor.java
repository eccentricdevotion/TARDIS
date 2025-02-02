/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.custommodels.keys.ArrowVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import org.bukkit.Material;

public record GUIWallFloor() {

    // TARDIS Wall & Floor Menu
    public static GUIData BUTTON_SCROLL_U = new GUIData(ArrowVariant.SCROLL_UP.getKey(), 8, Material.ARROW);
    public static GUIData BUTTON_SCROLL_D = new GUIData(ArrowVariant.SCROLL_DOWN.getKey(), 17, Material.ARROW);
    public static GUIData WALL = new GUIData(GuiVariant.WALL.getKey(), 26, Material.BOWL);
    public static GUIData FLOOR = new GUIData(GuiVariant.FLOOR.getKey(), 35, Material.BOWL);
    public static GUIData BUTTON_ABORT = new GUIData(GuiVariant.ABORT.getKey(), 53, Material.BOWL);
}
