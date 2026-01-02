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

import me.eccentric_nz.TARDIS.custommodels.keys.Button;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import org.bukkit.Material;

public record GUIChameleon() {

    // Chameleon Circuit
    public static final GUIData BUTTON_APPLY = new GUIData(GuiVariant.APPLY.getKey(), 0, Material.COMPARATOR);
    public static final GUIData BUTTON_CHAMELEON = new GUIData(Button.CHAMELEON.getKey(), 11, Material.BOWL);
    public static final GUIData BUTTON_ADAPT = new GUIData(Button.ADAPT.getKey(), 12, Material.BOWL);
    public static final GUIData BUTTON_INVISIBLE = new GUIData(Button.INVISIBLE.getKey(), 13, Material.BOWL);
    public static final GUIData BUTTON_SHORT = new GUIData(Button.SHORT.getKey(), 14, Material.BOWL);
    public static final GUIData BUTTON_CONSTRUCT = new GUIData(Button.CONSTRUCT.getKey(), 15, Material.BOWL);
    public static final GUIData BUTTON_LOCK = new GUIData(Button.LOCK.getKey(), 16, Material.BOWL);
    public static final GUIData BUTTON_CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 26, Material.BOWL);
}
