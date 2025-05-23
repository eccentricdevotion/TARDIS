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
package me.eccentric_nz.tardisweepingangels.equip;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

public class RemoveEquipment {

    public static void set(Player p) {
        PlayerInventory inv = p.getInventory();
        if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
            if (!inv.getItemInOffHand().getType().isAir()) {
                inv.setItemInOffHand(null);
            }
        }
        inv.setHelmet(null);
        inv.setChestplate(null);
        inv.setLeggings(null);
        inv.setBoots(null);
        p.updateInventory();
        p.getAttribute(Attribute.SCALE).setBaseValue(1.0d);
    }
}
