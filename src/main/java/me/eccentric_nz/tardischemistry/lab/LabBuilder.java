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
package me.eccentric_nz.tardischemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class LabBuilder {

    public static ItemStack getLabProduct(Lab lab) {
        ItemStack is = ItemStack.of(lab.getItemMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text(lab.getName()));
        im.setItemModel(lab.getModel());
        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, lab.getModel().getKey());
        is.setItemMeta(im);
        return is;
    }
}
