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

public record GUIArchive() {

    // TARDIS Archive
    public static GUIData BACK = new GUIData(8, 18, Material.BOWL);
    public static GUIData SET_SIZE = new GUIData(77, 19, Material.BOWL);
    public static GUIData SCAN_CONSOLE = new GUIData(75, 20, Material.BOWL);
    public static GUIData ARCHIVE_CURRENT_CONSOLE = new GUIData(5, 0, Material.BOWL);
    public static GUIData SMALL = new GUIData(79, 22, Material.BOWL);
    public static GUIData MEDIUM = new GUIData(62, 23, Material.BOWL);
    public static GUIData TALL = new GUIData(81, 24, Material.BOWL);
    public static GUIData CLOSE = new GUIData(1, 26, Material.BOWL);

    public static GUIData getByName(String name) {
        switch (name) {
            case "MEDIUM" -> {
                return MEDIUM;
            }
            case "TALL" -> {
                return TALL;
            }
            default -> {
                return SMALL;
            }
        }
    }
}
