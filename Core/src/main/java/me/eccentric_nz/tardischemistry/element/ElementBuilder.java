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
package me.eccentric_nz.tardischemistry.element;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ElementBuilder {

    public static ItemStack getElement(Element element) {
        ItemStack is = new ItemStack(Material.FEATHER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(element.toString());
        if (element.equals(Element.Unknown)) {
            im.setLore(List.of("?", "?"));
        } else {
            im.setLore(List.of(element.getSymbol(), "" + element.getAtomicNumber()));
        }
        im.setItemModel(element.getModel());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getElement(int atomicNumber) {
        return getElement(Element.getByAtomicNumber().get(atomicNumber));
    }

    public static ItemStack getElement(String symbol) {
        return getElement(Element.getBySymbol().get(symbol));
    }
}
