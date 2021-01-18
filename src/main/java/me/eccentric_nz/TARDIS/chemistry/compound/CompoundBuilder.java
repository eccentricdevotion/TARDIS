/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.chemistry.compound;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class CompoundBuilder {

    public static ItemStack getCompound(Compound compound) {
        ItemStack is;
        switch (compound) {
            case Ink:
                is = new ItemStack(Material.INK_SAC, 1);
                break;
            case Charcoal:
                is = new ItemStack(Material.CHARCOAL, 1);
                break;
            case Rust:
                is = new ItemStack(Material.LAVA_BUCKET, 1);
                ItemMeta rm = is.getItemMeta();
                rm.setDisplayName("Rust Bucket");
                rm.setCustomModelData(1);
                is.setItemMeta(rm);
                break;
            case Sugar:
                is = new ItemStack(Material.SUGAR, 1);
                break;
            case Sulphuric_Acid:
                is = new ItemStack(Material.WATER_BUCKET, 1);
                ItemMeta am = is.getItemMeta();
                am.setDisplayName("Acid Bucket");
                am.setCustomModelData(1);
                is.setItemMeta(am);
                break;
            default:
                is = new ItemStack(Material.GLASS_BOTTLE, 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(compound.toString().replace("_", " "));
                im.setLore(Collections.singletonList(compound.getSymbol()));
                im.setCustomModelData(10000001 + compound.ordinal());
                is.setItemMeta(im);
                break;
        }
        return is;
    }
}
