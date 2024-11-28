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
package me.eccentric_nz.tardisweepingangels.monsters.k9;

import me.eccentric_nz.TARDIS.custommodeldata.keys.Bone;
import me.eccentric_nz.tardisweepingangels.equip.DisguiseEquipper;
import me.eccentric_nz.tardisweepingangels.equip.FollowerEquipper;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class K9Equipment {

    public static void set(OfflinePlayer player, Entity entity, boolean disguise) {
        ItemStack head = new ItemStack(Material.BONE);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("K9 Head");
        headMeta.setItemModel(Bone.K9.getKey());
        head.setItemMeta(headMeta);
        if (entity instanceof Husk) {
            new FollowerEquipper().setHelmetAndInvisibilty(player, entity, Monster.K9, head);
        } else if (disguise) {
            new DisguiseEquipper().setHelmetAndInvisibilty(entity, head);
        }
    }
}
