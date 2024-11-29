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

import me.eccentric_nz.TARDIS.custommodeldata.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Buttons;
import me.eccentric_nz.TARDIS.custommodeldata.keys.LightButton;
import me.eccentric_nz.TARDIS.custommodeldata.keys.PrefsVariant;
import org.bukkit.Material;

public record GUIControlCentre() {

    // Control Centre
    // 1st column
    public static GUIData BUTTON_RANDOM = new GUIData(GuiVariant.BUTTON_RANDOM.getKey(), 0, Material.BOWL);
    public static GUIData BUTTON_SAVES = new GUIData(GuiVariant.BUTTON_SAVES.getKey(), 9, Material.BOWL);
    public static GUIData BUTTON_BACK = new GUIData(GuiVariant.BUTTON_BACK.getKey(), 18, Material.BOWL);
    public static GUIData BUTTON_AREAS = new GUIData(GuiVariant.BUTTON_AREAS.getKey(), 27, Material.BOWL);
    public static GUIData BUTTON_TERM = new GUIData(GuiVariant.BUTTON_TERM.getKey(), 36, Material.BOWL);
    public static GUIData BUTTON_THROTTLE = new GUIData(GuiVariant.BUTTON_THROTTLE.getKey(), 45, Material.BOWL);
    // 2nd column
    public static GUIData BUTTON_ARS = new GUIData(GuiVariant.BUTTON_ARS.getKey(), 2, Material.BOWL);
    public static GUIData BUTTON_THEME = new GUIData(GuiVariant.BUTTON_THEME.getKey(), 11, Material.BOWL);
    public static GUIData BUTTON_POWER = new GUIData(PrefsVariant.BUTTON_POWER_ON.getKey(), 20, Material.REPEATER);
    public static GUIData BUTTON_LIGHTS = new GUIData(LightButton.LIGHTS_BUTTON.getKey(), 29, Material.COAL_BLOCK);
    public static GUIData BUTTON_TOGGLE = new GUIData(PrefsVariant.BUTTON_TOGGLE_ON.getKey(), 38, Material.REPEATER);
    public static GUIData BUTTON_TARDIS_MAP = new GUIData(Buttons.BUTTON_TARDIS_MAP.getKey(), 47, Material.MAP);
    // 3rd column
    public static GUIData BUTTON_CHAMELEON = new GUIData(GuiVariant.BUTTON_CHAMELEON.getKey(), 4, Material.BOWL);
    public static GUIData BUTTON_SIEGE = new GUIData(PrefsVariant.SIEGE_ON.getKey(), 13, Material.REPEATER);
    public static GUIData BUTTON_HIDE = new GUIData(GuiVariant.BUTTON_HIDE.getKey(), 22, Material.BOWL);
    public static GUIData BUTTON_REBUILD = new GUIData(GuiVariant.BUTTON_REBUILD.getKey(), 31, Material.BOWL);
    public static GUIData BUTTON_DIRECTION = new GUIData(GuiVariant.BUTTON_DIRECTION.getKey(), 40, Material.BOWL);
    public static GUIData BUTTON_TEMP = new GUIData(GuiVariant.BUTTON_TEMP.getKey(), 49, Material.BOWL);
    // 4th column
    public static GUIData BUTTON_ARTRON = new GUIData(GuiVariant.BUTTON_ARTRON.getKey(), 6, Material.BOWL);
    public static GUIData BUTTON_SCANNER = new GUIData(GuiVariant.BUTTON_SCANNER.getKey(), 15, Material.BOWL);
    public static GUIData BUTTON_INFO = new GUIData(GuiVariant.BUTTON_INFO.getKey(), 24, Material.BOWL);
    public static GUIData BUTTON_TRANSMAT = new GUIData(GuiVariant.BUTTON_TRANSMAT.getKey(), 33, Material.BOWL);
    // 5th column
    public static GUIData BUTTON_ZERO = new GUIData(GuiVariant.BUTTON_ZERO.getKey(), 8, Material.BOWL);
    public static GUIData BUTTON_PREFS = new GUIData(GuiVariant.BUTTON_PREFS.getKey(), 17, Material.BOWL);
    public static GUIData COMPANIONS_MENU = new GUIData(GuiVariant.COMPANIONS_MENU.getKey(), 26, Material.BOWL);
    public static GUIData BUTTON_SYSTEM_UPGRADES = new GUIData(GuiVariant.BUTTON_SYSTEM_UPGRADES.getKey(), 35, Material.BOWL);
    public static GUIData BUTTON_CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 53, Material.BOWL);
}
