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
package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.custommodels.keys.Schism;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * The Doctor noted on more than one occasion that it was direct exposure to the Time Vortex through the Untempered
 * Schism that caused Gallifreyans to develop more quickly than other societies. Over "billions of years", it caused
 * them to evolve into Time Lords. This is comparable to the way that having been conceived within the Time Vortex
 * itself allowed the Silence to manipulate Melody Pond's biodata to make her into a proto-Time Lord capable of
 * regeneration.
 */
public class UntemperedSchism {

    public static ItemStack create() {
        ItemStack untempered = ItemStack.of(Material.ANCIENT_DEBRIS);
        ItemMeta im = untempered.getItemMeta();
        im.setItemModel(Schism.UNTEMPERED_SCHISM_BLOCK.getKey());
        im.displayName(ComponentUtils.toWhite("Untempered Schism"));
        im.lore(List.of(
                Component.text("Renew regenerations when"),
                Component.text("you have used them all.")
        ));
        untempered.setItemMeta(im);
        return untempered;
    }

    public static boolean is(ItemStack is) {
        if (is == null || is.getType() != Material.ANCIENT_DEBRIS || !is.hasItemMeta()) {
            return false;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasItemModel()) {
            return false;
        }
        return ComponentUtils.endsWith(im.displayName(), "Untempered Schism");
    }
}
