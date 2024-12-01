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
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import org.bukkit.Material;

public record GUIUpgrade() {

    // TARDIS Upgrade Menu
    public static GUIData INFO = new GUIData(GuiVariant.INFO.getKey(), 45, Material.BOWL);
    public static GUIData ARCHIVE_CONSOLES = new GUIData(GuiVariant.ARCHIVE_CONSOLES.getKey(), 46, Material.BOWL);
    public static GUIData REPAIR_CONSOLE = new GUIData(GuiVariant.REPAIR_CONSOLE.getKey(), 47, Material.BOWL);
    public static GUIData CLEAN = new GUIData(GuiVariant.CLEAN.getKey(), 48, Material.BOWL);
    public static GUIData CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 53, Material.BOWL);
}
