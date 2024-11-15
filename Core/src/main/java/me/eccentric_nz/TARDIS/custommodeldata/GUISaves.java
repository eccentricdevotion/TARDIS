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

import me.eccentric_nz.TARDIS.custommodeldata.keys.Arrow;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bucket;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Map;
import org.bukkit.Material;

public record GUISaves() {

    // TARDIS saves
    public static GUIData HOME = new GUIData(Bowl.HOME, 0, Material.BOWL);
    public static GUIData DEATH = new GUIData(Bowl.BUTTON_DEATH, 2, Material.BOWL);
    public static GUIData REARRANGE_SAVES = new GUIData(Arrow.REARRANGE.getKey(), 45, Material.ARROW);
    public static GUIData LOAD_TARDIS_AREAS = new GUIData(Map.LOAD_SELECTED_PROGRAM_IN_EDITOR.getKey(), 53, Material.MAP);
    public static GUIData DELETE_SAVE = new GUIData(Bucket.DELETE.getKey(), 47, Material.BUCKET);
    public static GUIData LOAD_MY_SAVES = new GUIData(Bowl.OWN_SAVES, 49, Material.BOWL);
    public static GUIData LOAD_SAVES_FROM_THIS_TARDIS = new GUIData(Bowl.TARDIS_SAVES, 49, Material.BOWL);
    public static GUIData BACK_TO_PLANETS = new GUIData(Arrow.BACK.getKey(), 51, Material.ARROW);
    public static GUIData GO_TO_PAGE_1 = new GUIData(Arrow.PAGE_ONE.getKey(), 51, Material.ARROW);
    public static GUIData GO_TO_PAGE_2 = new GUIData(Arrow.PAGE_TWO.getKey(), 51, Material.ARROW);
}
