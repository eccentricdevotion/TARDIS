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
import org.bukkit.Material;

public record GUIGeneticManipulator() {

    // Genetic Manipulator
    public static final GUIData BUTTON_PASSIVE = new GUIData(Button.PASSIVE.getKey(), 1, Material.BOWL);
    public static final GUIData BUTTON_NEUTRAL = new GUIData(Button.NEUTRAL.getKey(), 3, Material.BOWL);
    public static final GUIData BUTTON_HOSTILE = new GUIData(Button.HOSTILE.getKey(), 5, Material.BOWL);
    public static final GUIData BUTTON_ADJACENT = new GUIData(Button.ADJACENT.getKey(), 7, Material.BOWL);
    public static final GUIData BUTTON_DOCTORS = new GUIData(Button.DOCTOR.getKey(), 9, Material.BROWN_STAINED_GLASS_PANE);
    public static final GUIData BUTTON_COMPANIONS = new GUIData(Button.COMPANION.getKey(), 11, Material.BROWN_STAINED_GLASS_PANE);
    public static final GUIData BUTTON_CHARACTERS = new GUIData(Button.CHARACTER.getKey(), 13, Material.BROWN_STAINED_GLASS_PANE);
    public static final GUIData BUTTON_TWA = new GUIData(Button.MONSTER.getKey(), 15, Material.BROWN_STAINED_GLASS_PANE);
    public static final GUIData BUTTON_MASTER = new GUIData(Button.MASTER_OFF.getKey(), 17, Material.REPEATER);
    public static final GUIData BUTTON_AGE = new GUIData(Button.AGE.getKey(), 47, Material.HOPPER);
    public static final GUIData BUTTON_TYPE = new GUIData(Button.TYPE.getKey(), 48, Material.CYAN_DYE);
    public static final GUIData BUTTON_OPTS = new GUIData(Button.OPTIONS.getKey(), 49, Material.LEAD);
    public static final GUIData BUTTON_RESTORE = new GUIData(Button.RESTORE.getKey(), 51, Material.APPLE);
    public static final GUIData BUTTON_DNA = new GUIData(Button.DNA.getKey(), 52, Material.WRITABLE_BOOK);
    public static final GUIData BUTTON_PREVIOUS = new GUIData(GuiVariant.PREV.getKey(), 36, Material.ARROW);
    public static final GUIData BUTTON_BACK = new GUIData(GuiVariant.BACK.getKey(), 40, Material.BOWL);
    public static final GUIData BUTTON_NEXT = new GUIData(GuiVariant.NEXT.getKey(), 44, Material.ARROW);
    public static final GUIData BUTTON_CANCEL = new GUIData(Button.CANCEL.getKey(), 53, Material.BOWL);
}
