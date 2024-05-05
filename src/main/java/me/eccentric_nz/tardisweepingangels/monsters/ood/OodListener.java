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
package me.eccentric_nz.tardisweepingangels.monsters.ood;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.TWAOod;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class OodListener implements Listener {

    private final TARDIS plugin;

    public OodListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageOod(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (((CraftEntity) entity).getHandle() instanceof TWAOod ood && event.getDamager() instanceof Player player) {
            if (entity.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID) && entity.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                event.setCancelled(true);
                player.playSound(entity.getLocation(), "ood", 1.0f, 1.0f);
                if (!TARDISPermission.hasPermission(player, "tardisweepingangels.ood")) {
                    return;
                }
                UUID oodId = entity.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                if (player.getUniqueId().equals(oodId)) {
                    // set redeye
                    ood.setRedeye(!ood.isRedeye());
//                    ood.getOwnerUUID();
                } else if (TARDISWeepingAngels.UNCLAIMED.equals(oodId)) {
                    // claim the Ood
                    UUID pid = player.getUniqueId();
                    entity.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, pid);
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_CLAIMED", "Ood");
                    ood.setOwnerUUID(pid);
//                    ood.getOwnerUUID();
                }
//                else {
//                    ood.getOwnerUUID();
//                }
            }
        }
    }
}
