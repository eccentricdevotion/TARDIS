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
import me.eccentric_nz.tardisweepingangels.utils.ResetMonster;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity;
import org.bukkit.entity.Husk;
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
        if (event.getEntity() instanceof Husk husk && event.getDamager() instanceof Player player) {
            if (husk.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID) && husk.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                event.setCancelled(true);
                player.playSound(husk.getLocation(), "ood", 1.0f, 1.0f);
                if (!TARDISPermission.hasPermission(player, "tardisweepingangels.ood")) {
                    return;
                }
                UUID oodId = husk.getPersistentDataContainer().get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                Entity entity = ((CraftEntity) husk).getHandle();
                if (player.getUniqueId().equals(oodId)) {
                    if (entity instanceof TWAOod ood) {
                        // set redeye
                        ood.setRedeye(!ood.isRedeye());
                    } else {
                        // reset because it had all the correct PDC
                        new ResetMonster(plugin, husk).reset();
                    }
                } else if (TARDISWeepingAngels.UNCLAIMED.equals(oodId)) {
                    // claim the Ood
                    UUID pid = player.getUniqueId();
                    husk.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, pid);
                    plugin.getMessenger().send(player, TardisModule.MONSTERS, "WA_CLAIMED", "Ood");
                    if (entity instanceof TWAOod ood) {
                        ood.setOwnerUUID(pid);
                    }
                    // TODO update follower record if there is one
                }
            }
        }
    }
}
