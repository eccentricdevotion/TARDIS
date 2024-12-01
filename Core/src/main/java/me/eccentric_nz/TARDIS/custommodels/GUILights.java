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

import me.eccentric_nz.TARDIS.custommodels.keys.*;
import org.bukkit.Material;

public record GUILights() {

    // TARDIS Autonomous
    public static GUIData LIGHT_INFO = new GUIData(GuiVariant.INFO.getKey(), 0, Material.BOWL);
    public static GUIData BLOCK_INFO = new GUIData(GuiVariant.INFO.getKey(), 27, Material.BOWL);
    public static GUIData BLOCK_BUTTON = new GUIData(LightButton.BLOCK_BUTTON.getKey(), 28, Material.COAL_BLOCK);
    public static GUIData LIGHT_SWITCH = new GUIData(PrefsVariant.BUTTON_LIGHTS_ON.getKey(), 45, Material.REPEATER);
    public static GUIData BUTTON_LIGHT_LEVELS = new GUIData(LightButton.LIGHT_LEVELS.getKey(), 47, Material.COAL_BLOCK);
    public static GUIData BUTTON_LIGHT_SEQUENCE = new GUIData(LightButton.SEQUENCE_BUTTON.getKey(), 49, Material.COAL_BLOCK);
    public static GUIData EDIT_LIGHT_SEQUENCE = new GUIData(LightButton.EDIT_BUTTON.getKey(), 51, Material.COAL_BLOCK);
    public static GUIData SEQUENCE_INFO = new GUIData(GuiVariant.INFO.getKey(), 4, Material.BOWL);
    public static GUIData DELAY = new GUIData(LightButton.DELAY.getKey(), -1, Material.COAL_BLOCK);
    public static GUIData LEVEL = new GUIData(LightButton.LIGHT_LEVELS.getKey(), -1, Material.COAL_BLOCK);
    public static GUIData CHANGE_INFO = new GUIData(GuiVariant.INFO.getKey(), 34, Material.BOWL);
    public static GUIData CHANGE_LIGHTS = new GUIData(LightButton.CHANGE.getKey(), 35, Material.COAL_BLOCK);
    public static GUIData INTERIOR_INFO = new GUIData(GuiVariant.INFO.getKey(), 1, Material.BOWL);
    public static GUIData EXTERIOR_INFO = new GUIData(GuiVariant.INFO.getKey(), 7, Material.BOWL);
    public static GUIData CONSOLE_INFO = new GUIData(GuiVariant.INFO.getKey(), 22, Material.BOWL);
    public static GUIData INTERIOR = new GUIData(LightVariant.INTERIOR.getKey(), 42, Material.LANTERN);
    public static GUIData EXTERIOR = new GUIData(LightVariant.EXTERIOR.getKey(), 42, Material.SOUL_LANTERN);
    public static GUIData CONSOLE = new GUIData(LightVariant.CONSOLE.getKey(), 42, Material.COMPARATOR);
    public static GUIData CONVERT_INFO = new GUIData(GuiVariant.INFO.getKey(), 41, Material.BOWL);
    public static GUIData SELECT_LIGHT = new GUIData(LightButton.BLOCK_BUTTON.getKey(), 42, Material.COAL_BLOCK);
    public static GUIData CONVERT_LIGHTS = new GUIData(LightButton.CONVERT.getKey(), 44, Material.COAL_BLOCK);
    public static GUIData CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 53, Material.BOWL);
}
