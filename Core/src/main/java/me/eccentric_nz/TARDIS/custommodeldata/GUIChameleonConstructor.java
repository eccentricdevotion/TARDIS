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
import org.bukkit.Material;

public record GUIChameleonConstructor() {

    // Chameleon Constructor
    public static GUIData BACK_TO_CHAMELEON_CIRCUIT = new GUIData(Arrow.SCROLL_UP.getKey(), 0, Material.ARROW);
    public static GUIData HELP = new GUIData(Bowl.HELP.getKey(), 2, Material.BOWL);
    public static GUIData INFO = new GUIData(Bowl.INFO.getKey(), 3, Material.BOWL);
    public static GUIData ABORT = new GUIData(Bowl.ABORT.getKey(), 5, Material.BOWL);
    public static GUIData USE_LAST_SAVED_CONSTRUCT = new GUIData(Bowl.USE_LAST_SAVED_CONSTRUCT.getKey(), 7, Material.BOWL);
    public static GUIData SAVE_CONSTRUCT = new GUIData(Bowl.SAVE.getKey(), 8, Material.BOWL);

}
