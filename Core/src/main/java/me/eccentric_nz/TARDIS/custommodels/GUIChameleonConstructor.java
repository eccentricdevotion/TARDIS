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

import me.eccentric_nz.TARDIS.custommodels.keys.ArrowVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import org.bukkit.Material;

public record GUIChameleonConstructor() {

    // Chameleon Constructor
    public static GUIData BACK_TO_CHAMELEON_CIRCUIT = new GUIData(ArrowVariant.SCROLL_UP.getKey(), 0, Material.ARROW);
    public static GUIData HELP = new GUIData(GuiVariant.HELP.getKey(), 2, Material.BOWL);
    public static GUIData INFO = new GUIData(GuiVariant.INFO.getKey(), 3, Material.BOWL);
    public static GUIData ABORT = new GUIData(GuiVariant.ABORT.getKey(), 5, Material.BOWL);
    public static GUIData USE_LAST_SAVED_CONSTRUCT = new GUIData(GuiVariant.USE_LAST_SAVED_CONSTRUCT.getKey(), 7, Material.BOWL);
    public static GUIData SAVE_CONSTRUCT = new GUIData(GuiVariant.SAVE.getKey(), 8, Material.BOWL);

}
