/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import org.bukkit.Material;

public record GUIArchive() {

    // TARDIS Archive
    public static final GUIData BACK = new GUIData(GuiVariant.BACK.getKey(), 18, Material.BOWL);
    public static final GUIData SET_SIZE = new GUIData(GuiVariant.SET_SIZE.getKey(), 19, Material.BOWL);
    public static final GUIData SCAN_CONSOLE = new GUIData(GuiVariant.SCAN_CONSOLE.getKey(), 20, Material.BOWL);
    public static final GUIData ARCHIVE_CURRENT_CONSOLE = new GUIData(GuiVariant.ARCHIVE_CURRENT_CONSOLE.getKey(), 0, Material.BOWL);
    public static final GUIData SMALL = new GUIData(GuiVariant.SMALL.getKey(), 22, Material.BOWL);
    public static final GUIData MEDIUM = new GUIData(GuiVariant.MEDIUM.getKey(), 23, Material.BOWL);
    public static final GUIData TALL = new GUIData(GuiVariant.TALL.getKey(), 24, Material.BOWL);
    public static final GUIData CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 26, Material.BOWL);

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
