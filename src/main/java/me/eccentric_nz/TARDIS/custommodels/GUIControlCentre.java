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

import me.eccentric_nz.TARDIS.custommodels.keys.Button;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.LightButton;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import org.bukkit.Material;

public record GUIControlCentre() {

    // Control Centre
    // 1st column
    public static final GUIData BUTTON_RANDOM = new GUIData(Button.RANDOM.getKey(), 0, Material.BOWL);
    public static final GUIData BUTTON_SAVES = new GUIData(Button.SAVES.getKey(), 9, Material.BOWL);
    public static final GUIData BUTTON_BACK = new GUIData(GuiVariant.BACK.getKey(), 18, Material.BOWL);
    public static final GUIData BUTTON_AREAS = new GUIData(Button.AREAS.getKey(), 27, Material.BOWL);
    public static final GUIData BUTTON_TERM = new GUIData(Button.TERMINAL.getKey(), 36, Material.BOWL);
    public static final GUIData BUTTON_THROTTLE = new GUIData(Button.THROTTLE.getKey(), 45, Material.BOWL);
    // 2nd column
    public static final GUIData BUTTON_ARS = new GUIData(Button.ARS.getKey(), 2, Material.BOWL);
    public static final GUIData BUTTON_THEME = new GUIData(Button.THEME.getKey(), 11, Material.BOWL);
    public static final GUIData BUTTON_POWER = new GUIData(SwitchVariant.BUTTON_POWER_ON.getKey(), 20, Material.REPEATER);
    public static final GUIData BUTTON_LIGHTS = new GUIData(LightButton.LIGHTS_BUTTON.getKey(), 29, Material.COAL_BLOCK);
    public static final GUIData BUTTON_TOGGLE = new GUIData(SwitchVariant.BUTTON_TOGGLE_ON.getKey(), 38, Material.REPEATER);
    // 3rd column
    public static final GUIData BUTTON_CHAMELEON = new GUIData(Button.CHAMELEON.getKey(), 4, Material.BOWL);
    public static final GUIData BUTTON_SIEGE = new GUIData(SwitchVariant.SIEGE_ON.getKey(), 13, Material.REPEATER);
    public static final GUIData BUTTON_HIDE = new GUIData(Button.HIDE.getKey(), 22, Material.BOWL);
    public static final GUIData BUTTON_REBUILD = new GUIData(Button.REBUILD.getKey(), 31, Material.BOWL);
    public static final GUIData BUTTON_DIRECTION = new GUIData(Button.DIRECTION.getKey(), 40, Material.BOWL);
    public static final GUIData BUTTON_TEMP = new GUIData(Button.TEMPORAL.getKey(), 49, Material.BOWL);
    // 4th column
    public static final GUIData BUTTON_ARTRON = new GUIData(Button.ARTRON.getKey(), 6, Material.BOWL);
    public static final GUIData BUTTON_SCANNER = new GUIData(Button.SCANNER.getKey(), 15, Material.BOWL);
    public static final GUIData BUTTON_INFO = new GUIData(Button.INFO.getKey(), 24, Material.BOWL);
    public static final GUIData BUTTON_TRANSMAT = new GUIData(Button.TRANSMAT.getKey(), 33, Material.BOWL);
    // 5th column
    public static final GUIData BUTTON_ZERO = new GUIData(Button.ZERO.getKey(), 8, Material.BOWL);
    public static final GUIData BUTTON_PREFS = new GUIData(Button.PREFERENCES.getKey(), 17, Material.BOWL);
    public static final GUIData COMPANIONS_MENU = new GUIData(GuiVariant.COMPANIONS_MENU.getKey(), 26, Material.BOWL);
    public static final GUIData BUTTON_SYSTEM_UPGRADES = new GUIData(Button.SYSTEM_UPGRADES.getKey(), 35, Material.BOWL);
    public static final GUIData BUTTON_CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 53, Material.BOWL);
    public static GUIData BUTTON_TARDIS_MAP = new GUIData(Button.TARDIS_MAP.getKey(), 47, Material.MAP);
}
