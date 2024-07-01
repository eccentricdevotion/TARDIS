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

public record GUICompanion() {

    // Add Companion
    public static GUIData INFO = new GUIData(1, 45, Material.BOOK);
    public static GUIData LIST_COMPANIONS = new GUIData(2, 47, Material.WRITABLE_BOOK);
    public static GUIData ALL_COMPANIONS = new GUIData(3, 49, Material.WRITABLE_BOOK);
    public static GUIData ADD_COMPANION = new GUIData(3, 48, Material.NETHER_STAR);
    public static GUIData DELETE_COMPANION = new GUIData(1, 51, Material.BUCKET);
    public static GUIData BUTTON_CLOSE = new GUIData(1, 53, Material.BOWL);
}
