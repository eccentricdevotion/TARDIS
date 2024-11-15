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

import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.custommodeldata.keys.CoalBlock;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Map;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Repeater;
import org.bukkit.Material;

public record GUIControlCentre() {

    // Control Centre
    // 1st column
    public static GUIData BUTTON_RANDOM = new GUIData(Bowl.BUTTON_RANDOM, 0, Material.BOWL);
    public static GUIData BUTTON_SAVES = new GUIData(Bowl.BUTTON_SAVES, 9, Material.BOWL);
    public static GUIData BUTTON_BACK = new GUIData(Bowl.BUTTON_BACK, 18, Material.BOWL);
    public static GUIData BUTTON_AREAS = new GUIData(Bowl.BUTTON_AREAS, 27, Material.BOWL);
    public static GUIData BUTTON_TERM = new GUIData(Bowl.BUTTON_TERM, 36, Material.BOWL);
    public static GUIData BUTTON_THROTTLE = new GUIData(Bowl.BUTTON_THROTTLE, 45, Material.BOWL);
    // 2nd column
    public static GUIData BUTTON_ARS = new GUIData(Bowl.BUTTON_ARS, 2, Material.BOWL);
    public static GUIData BUTTON_THEME = new GUIData(Bowl.BUTTON_THEME, 11, Material.BOWL);
    public static GUIData BUTTON_POWER = new GUIData(Repeater.BUTTON_POWER_ON.getKey(), 20, Material.REPEATER);
    public static GUIData BUTTON_LIGHTS = new GUIData(CoalBlock.LIGHTS_BUTTON.getKey(), 29, Material.COAL_BLOCK);
    public static GUIData BUTTON_TOGGLE = new GUIData(Repeater.BUTTON_TOGGLE_ON.getKey(), 38, Material.REPEATER);
    public static GUIData BUTTON_TARDIS_MAP = new GUIData(Map.BUTTON_TARDIS_MAP.getKey(), 47, Material.MAP);
    // 3rd column
    public static GUIData BUTTON_CHAMELEON = new GUIData(Bowl.BUTTON_CHAMELEON, 4, Material.BOWL);
    public static GUIData BUTTON_SIEGE = new GUIData(Repeater.SIEGE_ON.getKey(), 13, Material.REPEATER);
    public static GUIData BUTTON_HIDE = new GUIData(Bowl.BUTTON_HIDE, 22, Material.BOWL);
    public static GUIData BUTTON_REBUILD = new GUIData(Bowl.BUTTON_REBUILD, 31, Material.BOWL);
    public static GUIData BUTTON_DIRECTION = new GUIData(Bowl.BUTTON_DIRECTION, 40, Material.BOWL);
    public static GUIData BUTTON_TEMP = new GUIData(Bowl.BUTTON_TEMP, 49, Material.BOWL);
    // 4th column
    public static GUIData BUTTON_ARTRON = new GUIData(Bowl.BUTTON_ARTRON, 6, Material.BOWL);
    public static GUIData BUTTON_SCANNER = new GUIData(Bowl.BUTTON_SCANNER, 15, Material.BOWL);
    public static GUIData BUTTON_INFO = new GUIData(Bowl.BUTTON_INFO, 24, Material.BOWL);
    public static GUIData BUTTON_TRANSMAT = new GUIData(Bowl.BUTTON_TRANSMAT, 33, Material.BOWL);
    // 5th column
    public static GUIData BUTTON_ZERO = new GUIData(Bowl.BUTTON_ZERO, 8, Material.BOWL);
    public static GUIData BUTTON_PREFS = new GUIData(Bowl.BUTTON_PREFS, 17, Material.BOWL);
    public static GUIData COMPANIONS_MENU = new GUIData(Bowl.COMPANIONS_MENU, 26, Material.BOWL);
    public static GUIData BUTTON_SYSTEM_UPGRADES = new GUIData(Bowl.BUTTON_SYSTEM_UPGRADES, 35, Material.BOWL);
    public static GUIData BUTTON_CLOSE = new GUIData(Bowl.CLOSE, 53, Material.BOWL);
}
