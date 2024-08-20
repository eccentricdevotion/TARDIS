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

public record GUILights() {

    // TARDIS Autonomous
    public static GUIData LIGHT_INFO = new GUIData(57, 0, Material.BOWL);
    public static GUIData BLOCK_INFO = new GUIData(57, 27, Material.BOWL);
    public static GUIData BLOCK_BUTTON = new GUIData(3, 28, Material.COAL_BLOCK);
    public static GUIData BUTTON_LIGHTS = new GUIData(16, 45, Material.REPEATER);
    public static GUIData BUTTON_LIGHT_LEVELS = new GUIData(4, 47, Material.COAL_BLOCK);
    public static GUIData BUTTON_LIGHT_SEQUENCE = new GUIData(5, 49, Material.COAL_BLOCK);
    public static GUIData EDIT_LIGHT_SEQUENCE = new GUIData(6, 51, Material.COAL_BLOCK);
    public static GUIData SEQUENCE_INFO = new GUIData(57, 4, Material.BOWL);
    public static GUIData DELAY = new GUIData(9, -1, Material.COAL_BLOCK);
    public static GUIData LEVEL = new GUIData(10, -1, Material.COAL_BLOCK);
    public static GUIData CHANGE_INFO = new GUIData(57, 34, Material.BOWL);
    public static GUIData CHANGE_LIGHTS = new GUIData(12, 35, Material.COAL_BLOCK);
    public static GUIData INTERIOR_INFO = new GUIData(6, 1, Material.BOOK);
    public static GUIData EXTERIOR_INFO = new GUIData(7, 7, Material.BOOK);
    public static GUIData CONSOLE_INFO = new GUIData(8, 22, Material.BOOK);
    public static GUIData INTERIOR = new GUIData(1, 42, Material.LANTERN);
    public static GUIData EXTERIOR = new GUIData(1, 42, Material.SOUL_LANTERN);
    public static GUIData CONSOLE = new GUIData(4, 42, Material.COMPARATOR);
    public static GUIData CONVERT_INFO = new GUIData(57, 41, Material.BOWL);
    public static GUIData SELECT_LIGHT = new GUIData(13, 42, Material.COAL_BLOCK);
    public static GUIData CONVERT_LIGHTS = new GUIData(14, 44, Material.COAL_BLOCK);
    public static GUIData CLOSE = new GUIData(1, 53, Material.BOWL);
}
