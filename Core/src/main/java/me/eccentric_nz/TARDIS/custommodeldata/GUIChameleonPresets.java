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

import me.eccentric_nz.TARDIS.custommodeldata.keys.ArrowVariant;
import me.eccentric_nz.TARDIS.custommodeldata.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bucket;
import org.bukkit.Material;

public record GUIChameleonPresets() {

    // Chameleon Presets
    public static GUIData GO_TO_PAGE_2 = new GUIData(ArrowVariant.PAGE_TWO.getKey(), 52, Material.ARROW);
    public static GUIData NEW = new GUIData(GuiVariant.NEW.getKey(), 49, Material.BOWL);
    public static GUIData SAVE = new GUIData(GuiVariant.SAVE.getKey(), 50, Material.BOWL);
    public static GUIData USE_SELECTED = new GUIData(GuiVariant.USE_SELECTED.getKey(), 45, Material.BOWL);
    public static GUIData UPDATE_SELECTED = new GUIData(GuiVariant.UPDATE_SELECTED.getKey(), 47, Material.BOWL);
    public static GUIData DELETE_SELECTED = new GUIData(Bucket.DELETE.getKey(), 46, Material.BUCKET);
    public static GUIData CURRENT = new GUIData(GuiVariant.BUTTON_CHAMELEON.getKey(), 50, Material.BOWL);
    public static GUIData SAVED = new GUIData(GuiVariant.BUTTON_CONSTRUCT.getKey(), 51, Material.BOWL);
    public static GUIData BACK = new GUIData(GuiVariant.BACK.getKey(), 51, Material.BOWL);
    public static GUIData CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 53, Material.BOWL);
}
