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

import me.eccentric_nz.TARDIS.custommodels.keys.ArrowVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Button;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import org.bukkit.Material;

public record GUISaves() {

    // TARDIS saves
    public static final GUIData HOME = new GUIData(GuiVariant.HOME.getKey(), 0, Material.BOWL);
    public static final GUIData DEATH = new GUIData(Button.DEATH.getKey(), 2, Material.BOWL);
    public static final GUIData REARRANGE_SAVES = new GUIData(ArrowVariant.REARRANGE.getKey(), 45, Material.ARROW);
    public static final GUIData DELETE_SAVE = new GUIData(Button.DELETE.getKey(), 47, Material.BUCKET);
    public static final GUIData BACK_TO_PLANETS = new GUIData(GuiVariant.BACK.getKey(), 51, Material.ARROW);
    public static GUIData LOAD_TARDIS_AREAS = new GUIData(GuiVariant.LOAD_SELECTED_PROGRAM_IN_EDITOR.getKey(), 53, Material.MAP);
    public static GUIData LOAD_MY_SAVES = new GUIData(GuiVariant.OWN_SAVES.getKey(), 49, Material.BOWL);
    public static GUIData LOAD_SAVES_FROM_THIS_TARDIS = new GUIData(GuiVariant.TARDIS_SAVES.getKey(), 49, Material.BOWL);
    public static GUIData GO_TO_PAGE_1 = new GUIData(ArrowVariant.PAGE_ONE.getKey(), 51, Material.ARROW);
    public static GUIData GO_TO_PAGE_2 = new GUIData(ArrowVariant.PAGE_TWO.getKey(), 51, Material.ARROW);
}
