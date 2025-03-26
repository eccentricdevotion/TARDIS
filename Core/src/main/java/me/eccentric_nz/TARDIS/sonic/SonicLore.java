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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class SonicLore {

    public static void setChargeLevel(ItemStack sonic) {
        ItemMeta im = sonic.getItemMeta();
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        if (pdc.has(TARDIS.plugin.getSonicChargeKey(), PersistentDataType.INTEGER)) {
            int charge = pdc.get(TARDIS.plugin.getSonicChargeKey(), PersistentDataType.INTEGER);
            List<String> lore;
            int index = -1;
            if (im.hasLore()) {
                lore = im.getLore();
                for (int i = lore.size() - 1; i >= 0; i--) {
                    if (lore.get(i).startsWith("Charge: ")) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    lore.set(index, "Charge: " + charge);
                } else {
                    lore.add("Charge: " + charge);
                }
            } else {
                lore = List.of("Charge: " + charge);
            }
            im.setLore(lore);
            sonic.setItemMeta(im);
        }
    }

    public static ItemStack addUpgrade(List<String> lore, String dn, List<Float> floats, ItemStack result, String upgrade) {
        int index = -1;
        String charge = null;
        for (int i = lore.size() - 1; i >= 0; i--) {
            if (lore.get(i).startsWith("Charge: ")) {
                charge = lore.get(i);
                index = i;
                break;
            }
        }
        if (index != -1 && charge != null) {
            lore.remove(index);
            lore.add(upgrade);
            lore.add(charge);
        } else {
            lore.add(upgrade);
        }
        ItemMeta rim = result.getItemMeta();
        rim.setDisplayName(dn);
        CustomModelDataComponent component = rim.getCustomModelDataComponent();
        component.setFloats(floats);
        rim.setCustomModelDataComponent(component);
        rim.setLore(lore);
        result.setItemMeta(rim);
        return result;
    }
}
