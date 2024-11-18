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

import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import org.bukkit.Material;

public record GUILights() {

    // TARDIS Autonomous
    public static GUIData LIGHT_INFO = new GUIData(Bowl.INFO.getKey(), 0, Material.BOWL);
    public static GUIData BLOCK_INFO = new GUIData(Bowl.INFO.getKey(), 27, Material.BOWL);
    public static GUIData BLOCK_BUTTON = new GUIData(CoalBlock.BLOCK_BUTTON.getKey(), 28, Material.COAL_BLOCK);
    public static GUIData LIGHT_SWITCH = new GUIData(Repeater.BUTTON_LIGHTS_ON.getKey(), 45, Material.REPEATER);
    public static GUIData BUTTON_LIGHT_LEVELS = new GUIData(CoalBlock.LIGHT_LEVELS.getKey(), 47, Material.COAL_BLOCK);
    public static GUIData BUTTON_LIGHT_SEQUENCE = new GUIData(CoalBlock.SEQUENCE_BUTTON.getKey(), 49, Material.COAL_BLOCK);
    public static GUIData EDIT_LIGHT_SEQUENCE = new GUIData(CoalBlock.EDIT_BUTTON.getKey(), 51, Material.COAL_BLOCK);
    public static GUIData SEQUENCE_INFO = new GUIData(Bowl.INFO.getKey(), 4, Material.BOWL);
    public static GUIData DELAY = new GUIData(CoalBlock.DELAY.getKey(), -1, Material.COAL_BLOCK);
    public static GUIData LEVEL = new GUIData(CoalBlock.LIGHT_LEVELS.getKey(), -1, Material.COAL_BLOCK);
    public static GUIData CHANGE_INFO = new GUIData(Bowl.INFO.getKey(), 34, Material.BOWL);
    public static GUIData CHANGE_LIGHTS = new GUIData(CoalBlock.CHANGE.getKey(), 35, Material.COAL_BLOCK);
    public static GUIData INTERIOR_INFO = new GUIData(Bowl.INFO.getKey(), 1, Material.BOWL);
    public static GUIData EXTERIOR_INFO = new GUIData(Bowl.INFO.getKey(), 7, Material.BOWL);
    public static GUIData CONSOLE_INFO = new GUIData(Bowl.INFO.getKey(), 22, Material.BOWL);
    public static GUIData INTERIOR = new GUIData(Lantern.INTERIOR.getKey(), 42, Material.LANTERN);
    public static GUIData EXTERIOR = new GUIData(SoulLantern.EXTERIOR.getKey(), 42, Material.SOUL_LANTERN);
    public static GUIData CONSOLE = new GUIData(Comparator.CONSOLE.getKey(), 42, Material.COMPARATOR);
    public static GUIData CONVERT_INFO = new GUIData(Bowl.INFO.getKey(), 41, Material.BOWL);
    public static GUIData SELECT_LIGHT = new GUIData(CoalBlock.BLOCK_BUTTON.getKey(), 42, Material.COAL_BLOCK);
    public static GUIData CONVERT_LIGHTS = new GUIData(CoalBlock.CONVERT.getKey(), 44, Material.COAL_BLOCK);
    public static GUIData CLOSE = new GUIData(Bowl.CLOSE.getKey(), 53, Material.BOWL);
}
