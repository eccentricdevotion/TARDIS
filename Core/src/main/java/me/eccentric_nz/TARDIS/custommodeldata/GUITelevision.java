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

import java.util.List;

public record GUITelevision() {

    // TARDIS Areas
    public static GUIData DOCTORS = new GUIData(1000, 1, Material.BROWN_STAINED_GLASS_PANE, "Doctors");
    public static GUIData COMPANIONS = new GUIData(1001, 3, Material.BROWN_STAINED_GLASS_PANE, "Companions");
    public static GUIData CHARACTERS = new GUIData(1002, 5, Material.BROWN_STAINED_GLASS_PANE, "Characters");
    public static GUIData MONSTERS = new GUIData(1003, 7, Material.BROWN_STAINED_GLASS_PANE, "Monsters");
    public static GUIData DOWNLOAD = new GUIData(157, 29, Material.REPEATER);
    public static GUIData REMOVE = new GUIData(8, 31, Material.BUCKET);
    public static GUIData BACK = new GUIData(8, 33, Material.BOWL);
    public static GUIData CLOSE = new GUIData(1, 35, Material.BOWL);

    public static List<GUIData> values() {
        return List.of(DOCTORS, COMPANIONS, CHARACTERS, MONSTERS, REMOVE, CLOSE);
    }
}
