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
package me.eccentric_nz.tardischemistry.compound;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CompoundBuilder {

    public static ItemStack getCompound(Compound compound) {
        ItemStack is;
        switch (compound) {
            case Ink -> is = ItemStack.of(Material.INK_SAC, 1);
            case Charcoal -> is = ItemStack.of(Material.CHARCOAL, 1);
            case Rust -> {
                is = ItemStack.of(Material.LAVA_BUCKET, 1);
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Rust Bucket"));
                is.setData(DataComponentTypes.ITEM_MODEL, Whoniverse.RUST_BUCKET.getKey());
            }
            case Sugar -> is = ItemStack.of(Material.SUGAR, 1);
            case Sulphuric_Acid -> {
                is = ItemStack.of(Material.WATER_BUCKET, 1);
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Acid Bucket"));
                is.setData(DataComponentTypes.ITEM_MODEL, Whoniverse.ACID_BUCKET.getKey());
            }
            default -> {
                is = ItemStack.of(Material.GLASS_BOTTLE, 1);
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(compound.getName()));
                is.lore(List.of(Component.text(compound.getSymbol())));
                if (compound.getModel() != null) {
                    is.setData(DataComponentTypes.ITEM_MODEL, compound.getModel());
                }
            }
        }
        return is;
    }
}
