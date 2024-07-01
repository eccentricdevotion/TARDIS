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

public record GUIArtronStorage() {

    // Artron Storage
    public static GUIData INFO = new GUIData(57, 0, Material.BOWL);
    public static GUIData ARROW_RIGHT = new GUIData(15, 1, Material.ARROW);
    public static GUIData ARROW_LEFT = new GUIData(14, 8, Material.ARROW);
    public static GUIData CLOSE = new GUIData(1, 9, Material.BOWL);
}
