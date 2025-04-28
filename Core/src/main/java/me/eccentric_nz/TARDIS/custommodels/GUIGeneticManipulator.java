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
import org.bukkit.Material;

public record GUIGeneticManipulator() {

    // Genetic Manipulator
    public static GUIData BUTTON_SKINS = new GUIData(Button.SKINS.getKey(), 43, Material.BOWL);
    public static GUIData BUTTON_TWA = new GUIData(Button.TWA.getKey(), 44, Material.BOWL);
    public static GUIData BUTTON_MASTER = new GUIData(Button.MASTER_OFF.getKey(), 45, Material.REPEATER);
    public static GUIData BUTTON_AGE = new GUIData(Button.AGE.getKey(), 47, Material.HOPPER);
    public static GUIData BUTTON_TYPE = new GUIData(Button.TYPE.getKey(), 48, Material.CYAN_DYE);
    public static GUIData BUTTON_OPTS = new GUIData(Button.OPTIONS.getKey(), 49, Material.LEAD);
    public static GUIData BUTTON_RESTORE = new GUIData(Button.RESTORE.getKey(), 51, Material.APPLE);
    public static GUIData BUTTON_DNA = new GUIData(Button.DNA.getKey(), 52, Material.WRITABLE_BOOK);
    public static GUIData BUTTON_CANCEL = new GUIData(Button.CANCEL.getKey(), 53, Material.BOWL);
}
