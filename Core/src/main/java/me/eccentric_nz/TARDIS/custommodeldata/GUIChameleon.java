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
import me.eccentric_nz.TARDIS.custommodeldata.keys.Comparator;
import org.bukkit.Material;

public record GUIChameleon() {

    // Chameleon Circuit
    public static GUIData BUTTON_APPLY = new GUIData(Comparator.APPLY.getKey(), 0, Material.COMPARATOR);
    public static GUIData BUTTON_CHAMELEON = new GUIData(Bowl.BUTTON_CHAMELEON.getKey(), 11, Material.BOWL);
    public static GUIData BUTTON_ADAPT = new GUIData(Bowl.BUTTON_ADAPT.getKey(), 12, Material.BOWL);
    public static GUIData BUTTON_INVISIBLE = new GUIData(Bowl.BUTTON_INVISIBLE.getKey(), 13, Material.BOWL);
    public static GUIData BUTTON_SHORT = new GUIData(Bowl.BUTTON_SHORT.getKey(), 14, Material.BOWL);
    public static GUIData BUTTON_CONSTRUCT = new GUIData(Bowl.BUTTON_CONSTRUCT.getKey(), 15, Material.BOWL);
    public static GUIData BUTTON_LOCK = new GUIData(Bowl.BUTTON_LOCK.getKey(), 16, Material.BOWL);
    public static GUIData BUTTON_CLOSE = new GUIData(Bowl.CLOSE.getKey(), 26, Material.BOWL);
}
