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

public record GUIArs() {

    // Architectural Reconfiguration

    public static GUIData BUTTON_UP = new GUIData(1, 1, Material.CYAN_WOOL);
    public static GUIData BUTTON_DOWN = new GUIData(2, 19, Material.CYAN_WOOL);
    public static GUIData BUTTON_LEFT = new GUIData(3, 9, Material.CYAN_WOOL);
    public static GUIData BUTTON_RIGHT = new GUIData(4, 11, Material.CYAN_WOOL);
    public static GUIData BUTTON_MAP = new GUIData(2, 10, Material.MAP);
    public static GUIData BUTTON_RECON = new GUIData(1, 12, Material.PINK_WOOL);
    public static GUIData BUTTON_LEVEL_B = new GUIData(1, 27, Material.WHITE_WOOL);
    public static GUIData BUTTON_LEVEL = new GUIData(2, 28, Material.YELLOW_WOOL);
    public static GUIData BUTTON_LEVEL_T = new GUIData(3, 29, Material.WHITE_WOOL);
    public static GUIData BUTTON_RESET = new GUIData(1, 30, Material.COBBLESTONE);
    public static GUIData BUTTON_SCROLL_L = new GUIData(1, 36, Material.RED_WOOL);
    public static GUIData BUTTON_SCROLL_R = new GUIData(1, 38, Material.LIME_WOOL);
    public static GUIData BUTTON_JETT = new GUIData(1, 39, Material.TNT);
    public static GUIData BUTTON_MAP_ON = new GUIData(1, -1, Material.BLACK_WOOL);
    public static GUIData EMPTY_SLOT = new GUIData(1, -1, Material.STONE);
}
