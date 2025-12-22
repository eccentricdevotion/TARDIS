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

public record GUIChameleonPresets() {

    // Chameleon Presets
    public static final GUIData USE_SELECTED = new GUIData(GuiVariant.USE_SELECTED.getKey(), 45, Material.BOWL);
    public static final GUIData DELETE_SELECTED = new GUIData(Button.DELETE.getKey(), 46, Material.BUCKET);
    public static final GUIData UPDATE_SELECTED = new GUIData(GuiVariant.UPDATE_SELECTED.getKey(), 47, Material.BOWL);
    public static final GUIData CUSTOM = new GUIData(Button.CUSTOM.getKey(), 48, Material.ENDER_CHEST, "Custom presets");
    public static final GUIData NEW = new GUIData(GuiVariant.NEW.getKey(), 49, Material.BOWL);
    public static final GUIData CURRENT = new GUIData(Button.CHAMELEON.getKey(), 50, Material.BOWL);
    public static final GUIData SAVE = new GUIData(GuiVariant.SAVE.getKey(), 50, Material.BOWL);
    public static final GUIData SAVED = new GUIData(Button.CONSTRUCT.getKey(), 51, Material.BOWL);
    public static final GUIData GO_TO_PAGE_2 = new GUIData(ArrowVariant.PAGE_TWO.getKey(), 52, Material.ARROW, "Go to page 2");
    public static final GUIData BACK = new GUIData(GuiVariant.BACK.getKey(), 51, Material.BOWL);
    public static final GUIData CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 53, Material.BOWL);
}
