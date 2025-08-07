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

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.RoomVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Wool;
import org.bukkit.Material;

public record GUIArs() {

    // Architectural Reconfiguration
    public static final GUIData BUTTON_UP = new GUIData(Wool.UP.getKey(), 1, Material.CYAN_WOOL);
    public static final GUIData BUTTON_DOWN = new GUIData(Wool.DOWN.getKey(), 19, Material.CYAN_WOOL);
    public static final GUIData BUTTON_LEFT = new GUIData(Wool.LEFT.getKey(), 9, Material.CYAN_WOOL);
    public static final GUIData BUTTON_RIGHT = new GUIData(Wool.RIGHT.getKey(), 11, Material.CYAN_WOOL);
    public static final GUIData BUTTON_MAP = new GUIData(GuiVariant.LOAD_MAP.getKey(), 10, Material.MAP);
    public static final GUIData BUTTON_RECON = new GUIData(Wool.RECONFIGURE.getKey(), 12, Material.PINK_WOOL);
    public static final GUIData BUTTON_LEVEL_B = new GUIData(Wool.LEVEL_BOTTOM.getKey(), 27, Material.WHITE_WOOL);
    public static final GUIData BUTTON_LEVEL = new GUIData(Wool.LEVEL_MAIN.getKey(), 28, Material.YELLOW_WOOL);
    public static final GUIData BUTTON_LEVEL_T = new GUIData(Wool.LEVEL_TOP.getKey(), 29, Material.WHITE_WOOL);
    public static final GUIData BUTTON_RESET = new GUIData(SonicVariant.STANDARD_SONIC.getKey(), 30, Material.COBBLESTONE);
    public static final GUIData BUTTON_SCROLL_L = new GUIData(Wool.SCROLL_LEFT.getKey(), 36, Material.RED_WOOL);
    public static final GUIData BUTTON_SCROLL_R = new GUIData(Wool.SCROLL_RIGHT.getKey(), 38, Material.LIME_WOOL);
    public static final GUIData BUTTON_JETT = new GUIData(RoomVariant.JETTISON.getKey(), 39, Material.TNT);
    public static final GUIData BUTTON_MAP_ON = new GUIData(Wool.BLANK.getKey(), -1, Material.BLACK_WOOL);
    public static GUIData EMPTY_SLOT = new GUIData(RoomVariant.SLOT.getKey(), -1, Material.STONE);
}
