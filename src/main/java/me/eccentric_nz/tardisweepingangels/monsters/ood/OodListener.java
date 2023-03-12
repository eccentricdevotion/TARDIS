/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.monsters.ood;

import java.util.UUID;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class OodListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageOod(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand stand && event.getDamager() instanceof Player player) {
            if (stand.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER) && stand.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                event.setCancelled(true);
                player.playSound(stand.getLocation(), "ood", 1.0f, 1.0f);
                if (!player.hasPermission("tardisweepingangels.ood")) {
                    return;
                }
                UUID oodId = stand.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if (oodId.equals(player.getUniqueId())) {
                    EntityEquipment ee = stand.getEquipment();
                    if (ee != null) {
                        ItemStack head = ee.getHelmet();
                        ItemMeta im = head.getItemMeta();
                        int rage = stand.getPersistentDataContainer().get(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER);
                        int cmd = im.getCustomModelData();
                        if (rage == 1) {
                            cmd -= 100;
                            rage = 0;
                        } else {
                            cmd += 100;
                            rage = 1;
                        }
                        im.setCustomModelData(cmd);
                        head.setItemMeta(im);
                        ee.setHelmet(head);
                        stand.getPersistentDataContainer().set(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER, rage);
                    }
                } else if (oodId.equals(TARDISWeepingAngels.UNCLAIMED)) {
                    // claim the Ood
                    stand.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, player.getUniqueId());
                    player.sendMessage(TARDISWeepingAngels.plugin.pluginName + "You have claimed this Ood!");
                }
            }
        }
    }
}
