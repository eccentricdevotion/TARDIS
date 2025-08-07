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
package me.eccentric_nz.tardischemistry.compound;

import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CompoundBuilder {

    public static ItemStack getCompound(Compound compound) {
        ItemStack is;
        switch (compound) {
            case Ink -> is = ItemStack.of(Material.INK_SAC, 1);
            case Charcoal -> is = ItemStack.of(Material.CHARCOAL, 1);
            case Rust -> {
                is = ItemStack.of(Material.LAVA_BUCKET, 1);
                ItemMeta rm = is.getItemMeta();
                rm.displayName(Component.text("Rust Bucket"));
                rm.setItemModel(Whoniverse.RUST_BUCKET.getKey());
                is.setItemMeta(rm);
            }
            case Sugar -> is = ItemStack.of(Material.SUGAR, 1);
            case Sulphuric_Acid -> {
                is = ItemStack.of(Material.WATER_BUCKET, 1);
                ItemMeta am = is.getItemMeta();
                am.displayName(Component.text("Acid Bucket"));
                am.setItemModel(Whoniverse.ACID_BUCKET.getKey());
                is.setItemMeta(am);
            }
            default -> {
                is = ItemStack.of(Material.GLASS_BOTTLE, 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text(compound.getName()));
                im.lore(List.of(Component.text(compound.getSymbol())));
                if (compound.getModel() != null) {
                    im.setItemModel(compound.getModel());
                }
                is.setItemMeta(im);
            }
        }
        return is;
    }
}
