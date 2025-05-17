/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.custommodels.keys.Button;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import org.bukkit.Material;

public record GUICompanion() {

    // Add Companion
    public static final GUIData INFO = new GUIData(GuiVariant.INFO.getKey(), 45, Material.BOOK);
    public static final GUIData LIST_COMPANIONS = new GUIData(Button.COMPANION_LIST.getKey(), 47, Material.WRITABLE_BOOK);
    public static final GUIData ALL_COMPANIONS = new GUIData(Button.COMPANION_ALL.getKey(), 49, Material.WRITABLE_BOOK);
    public static final GUIData ADD_COMPANION = new GUIData(Button.ADD_COMPANION.getKey(), 48, Material.NETHER_STAR);
    public static final GUIData DELETE_COMPANION = new GUIData(Button.DELETE.getKey(), 51, Material.BUCKET);
    public static final GUIData BUTTON_CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 53, Material.BOWL);
}
