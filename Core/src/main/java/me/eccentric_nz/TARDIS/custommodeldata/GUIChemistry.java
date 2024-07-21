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

public record GUIChemistry() {

    // TARDIS Areas
    public static GUIData CLOSE = new GUIData(1, 26, Material.BOWL);
    public static GUIData INFO = new GUIData(57, 8, Material.BOWL);
    public static GUIData CHECK = new GUIData(89, 17, Material.BOWL);
    public static GUIData CRAFT = new GUIData(90, 17, Material.BOWL);
    public static GUIData ELECTRONS = new GUIData(91, 22, Material.BOWL);
    public static GUIData NEUTRONS = new GUIData(92, 13, Material.BOWL);
    public static GUIData PROTONS = new GUIData(93, 4, Material.BOWL);
    public static GUIData REDUCE = new GUIData(94, 17, Material.BOWL);
    public static GUIData SCROLL_DOWN = new GUIData(7, 1, Material.ARROW);
    public static GUIData SCROLL_UP = new GUIData(8, 1, Material.ARROW);
    public static GUIData PLUS = new GUIData(9, 24, Material.ARROW);
    public static GUIData MINUS = new GUIData(10, 23, Material.ARROW);
    public static GUIData ELEMENTS = new GUIData(10000999, 35, Material.FEATHER);
    public static GUIData COMPOUNDS = new GUIData(1, 44, Material.GLASS_BOTTLE);
    public static GUIData PRODUCTS = new GUIData(1, 44, Material.CRAFTING_TABLE);
}
