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

public record GUIControlCentre() {

    // Control Centre
    // 1st column
    public static GUIData BUTTON_RANDOM = new GUIData(24, 0, Material.BOWL);
    public static GUIData BUTTON_SAVES = new GUIData(26, 9, Material.BOWL);
    public static GUIData BUTTON_BACK = new GUIData(15, 18, Material.BOWL);
    public static GUIData BUTTON_AREAS = new GUIData(12, 27, Material.BOWL);
    public static GUIData BUTTON_TERM = new GUIData(30, 36, Material.BOWL);
    public static GUIData BUTTON_THROTTLE = new GUIData(149, 45, Material.BOWL);
    // 2nd column
    public static GUIData BUTTON_ARS = new GUIData(13, 2, Material.BOWL);
    public static GUIData BUTTON_THEME = new GUIData(31, 11, Material.BOWL);
    public static GUIData BUTTON_POWER = new GUIData(17, 20, Material.REPEATER);
    public static GUIData BUTTON_LIGHTS = new GUIData(8, 29, Material.COAL_BLOCK);
    public static GUIData BUTTON_TOGGLE = new GUIData(19, 38, Material.REPEATER);
    public static GUIData BUTTON_TARDIS_MAP = new GUIData(3, 47, Material.MAP);
    // 3rd column
    public static GUIData BUTTON_CHAMELEON = new GUIData(17, 4, Material.BOWL);
    public static GUIData BUTTON_SIEGE = new GUIData(18, 13, Material.REPEATER);
    public static GUIData BUTTON_HIDE = new GUIData(20, 22, Material.BOWL);
    public static GUIData BUTTON_REBUILD = new GUIData(25, 31, Material.BOWL);
    public static GUIData BUTTON_DIRECTION = new GUIData(19, 40, Material.BOWL);
    public static GUIData BUTTON_TEMP = new GUIData(29, 49, Material.BOWL);
    // 4th column
    public static GUIData BUTTON_ARTRON = new GUIData(14, 6, Material.BOWL);
    public static GUIData BUTTON_SCANNER = new GUIData(27, 15, Material.BOWL);
    public static GUIData BUTTON_INFO = new GUIData(21, 24, Material.BOWL);
    public static GUIData BUTTON_TRANSMAT = new GUIData(133, 33, Material.BOWL);
    // 5th column
    public static GUIData BUTTON_ZERO = new GUIData(32, 8, Material.BOWL);
    public static GUIData BUTTON_PREFS = new GUIData(23, 17, Material.BOWL);
    public static GUIData COMPANIONS_MENU = new GUIData(45, 26, Material.BOWL);
    public static GUIData BUTTON_SYSTEM_UPGRADES = new GUIData(156, 35, Material.BOWL);
    public static GUIData BUTTON_CLOSE = new GUIData(1, 53, Material.BOWL);
}
