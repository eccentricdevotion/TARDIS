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

import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import org.bukkit.Material;

public record GUIChemistry() {

    // TARDIS Areas
    public static GUIData CLOSE = new GUIData(Bowl.CLOSE, 26, Material.BOWL);
    public static GUIData INFO = new GUIData(Bowl.INFO, 8, Material.BOWL);
    public static GUIData CHECK = new GUIData(Bowl.CHECK, 17, Material.BOWL);
    public static GUIData CRAFT = new GUIData(Bowl.CRAFT, 17, Material.BOWL);
    public static GUIData ELECTRONS = new GUIData(Bowl.ELECTRONS, 22, Material.BOWL);
    public static GUIData NEUTRONS = new GUIData(Bowl.NEUTRONS, 13, Material.BOWL);
    public static GUIData PROTONS = new GUIData(Bowl.PROTONS, 4, Material.BOWL);
    public static GUIData REDUCE = new GUIData(Bowl.REDUCE, 17, Material.BOWL);
    public static GUIData SCROLL_DOWN = new GUIData(Arrow.SCROLL_DOWN.getKey(), 1, Material.ARROW);
    public static GUIData SCROLL_UP = new GUIData(Arrow.SCROLL_UP.getKey(), 1, Material.ARROW);
    public static GUIData PLUS = new GUIData(Arrow.HANDLES_OPERATOR_ADDITION.getKey(), 24, Material.ARROW);
    public static GUIData MINUS = new GUIData(Arrow.HANDLES_OPERATOR_SUBTRACTION.getKey(), 23, Material.ARROW);
    public static GUIData ELEMENTS = new GUIData(Feather.UNKNOWN.getKey(), 35, Material.FEATHER);
    public static GUIData COMPOUNDS = new GUIData(GlassBottle.ALUMINIUM_OXIDE.getKey(), 44, Material.GLASS_BOTTLE);
    public static GUIData PRODUCTS = new GUIData(BoneMeal.SUPER_FERTILISER.getKey(), 44, Material.BONE_MEAL);
}
