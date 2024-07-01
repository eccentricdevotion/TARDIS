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

import org.bukkit.Material;

public record GUIChameleonConstructor() {

    // Chameleon Constructor
    public static GUIData BACK_TO_CHAMELEON_CIRCUIT = new GUIData(8, 0, Material.ARROW);
    public static GUIData HELP = new GUIData(55, 2, Material.BOWL);
    public static GUIData INFO = new GUIData(57, 3, Material.BOWL);
    public static GUIData ABORT = new GUIData(2, 5, Material.BOWL);
    public static GUIData USE_LAST_SAVED_CONSTRUCT = new GUIData(82, 7, Material.BOWL);
    public static GUIData SAVE_CONSTRUCT = new GUIData(74, 8, Material.BOWL);

}
