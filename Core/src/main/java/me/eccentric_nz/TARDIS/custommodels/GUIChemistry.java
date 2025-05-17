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
import me.eccentric_nz.TARDIS.custommodels.keys.ChemistryBottle;
import me.eccentric_nz.TARDIS.custommodels.keys.ChemistryEquipment;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.tardischemistry.element.Element;
import org.bukkit.Material;

public record GUIChemistry() {

    // TARDIS Areas
    public static final GUIData CLOSE = new GUIData(GuiVariant.CLOSE.getKey(), 26, Material.BOWL);
    public static final GUIData INFO = new GUIData(GuiVariant.INFO.getKey(), 8, Material.BOWL);
    public static final GUIData CHECK = new GUIData(GuiVariant.CHECK.getKey(), 17, Material.BOWL);
    public static final GUIData CRAFT = new GUIData(GuiVariant.CRAFT.getKey(), 17, Material.BOWL);
    public static final GUIData ELECTRONS = new GUIData(GuiVariant.ELECTRONS.getKey(), 22, Material.BOWL);
    public static final GUIData NEUTRONS = new GUIData(GuiVariant.NEUTRONS.getKey(), 13, Material.BOWL);
    public static final GUIData PROTONS = new GUIData(GuiVariant.PROTONS.getKey(), 4, Material.BOWL);
    public static final GUIData REDUCE = new GUIData(GuiVariant.REDUCE.getKey(), 17, Material.BOWL);
    public static final GUIData SCROLL_DOWN = new GUIData(ArrowVariant.SCROLL_DOWN.getKey(), 1, Material.ARROW);
    public static final GUIData SCROLL_UP = new GUIData(ArrowVariant.SCROLL_UP.getKey(), 1, Material.ARROW);
    public static final GUIData PLUS = new GUIData(ArrowVariant.HANDLES_OPERATOR_ADDITION.getKey(), 24, Material.ARROW);
    public static final GUIData MINUS = new GUIData(ArrowVariant.HANDLES_OPERATOR_SUBTRACTION.getKey(), 23, Material.ARROW);
    public static final GUIData ELEMENTS = new GUIData(Element.Unknown.getModel(), 35, Material.FEATHER);
    public static final GUIData COMPOUNDS = new GUIData(ChemistryBottle.ALUMINIUM_OXIDE.getKey(), 44, Material.GLASS_BOTTLE);
    public static final GUIData PRODUCTS = new GUIData(ChemistryEquipment.SUPER_FERTILISER.getKey(), 44, Material.BONE_MEAL);
}
