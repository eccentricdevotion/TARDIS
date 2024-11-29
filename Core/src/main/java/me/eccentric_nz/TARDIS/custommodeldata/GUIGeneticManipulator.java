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
import org.bukkit.Material;

public record GUIGeneticManipulator() {

    // Genetic Manipulator
    public static GUIData BUTTON_SKINS = new GUIData(GuiVariant.BUTTON_SKINS.getKey(), 43, Material.BOWL);
    public static GUIData BUTTON_TWA = new GUIData(GuiVariant.BUTTON_TWA.getKey(), 44, Material.BOWL);
    public static GUIData BUTTON_MASTER = new GUIData(Buttons.BUTTON_MASTER_OFF.getKey(), 45, Material.COMPARATOR);
    public static GUIData BUTTON_AGE = new GUIData(Buttons.BUTTON_AGE.getKey(), 47, Material.HOPPER);
    public static GUIData BUTTON_TYPE = new GUIData(Buttons.BUTTON_TYPE.getKey(), 48, Material.CYAN_DYE);
    public static GUIData BUTTON_OPTS = new GUIData(Buttons.BUTTON_OPTS.getKey(), 49, Material.LEAD);
    public static GUIData BUTTON_RESTORE = new GUIData(Buttons.BUTTON_RESTORE.getKey(), 51, Material.APPLE);
    public static GUIData BUTTON_DNA = new GUIData(Buttons.BUTTON_DNA.getKey(), 52, Material.WRITABLE_BOOK);
    public static GUIData BUTTON_CANCEL = new GUIData(GuiVariant.BUTTON_CANCEL.getKey(), 53, Material.BOWL);
}
