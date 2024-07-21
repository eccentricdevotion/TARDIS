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

public record GUIParticle() {

    // TARDIS Autonomous
    public static GUIData SHAPE_INFO = new GUIData(2, 0, Material.LAPIS_BLOCK);
    public static GUIData SHAPE = new GUIData(1, -1, Material.LAPIS_LAZULI);
    public static GUIData EFFECT_INFO = new GUIData(2, 9, Material.REDSTONE_BLOCK);
    public static GUIData EFFECT = new GUIData(1, -1, Material.REDSTONE);
    public static GUIData COLOUR_INFO = new GUIData(2, 8, Material.EMERALD_BLOCK);
    public static GUIData COLOUR = new GUIData(1, 17,Material.EMERALD);
    public static GUIData BLOCK_INFO = new GUIData(2, 26, Material.COAL_BLOCK);
    public static GUIData BLOCK = new GUIData(1, 35, Material.COAL);
    public static GUIData TOGGLE = new GUIData(19, 44, Material.REPEATER);
    public static GUIData TEST = new GUIData(1, 48, Material.COPPER_INGOT);
    public static GUIData MINUS = new GUIData(40, -1, Material.PAPER);
    public static GUIData PLUS = new GUIData(39, -1, Material.PAPER);
    public static GUIData DENSITY = new GUIData(5, 46, Material.IRON_INGOT);
    public static GUIData SPEED = new GUIData(1, 50, Material.GOLD_INGOT);
    public static GUIData CLOSE = new GUIData(1, 53, Material.BOWL);
}
