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
package me.eccentric_nz.tardisweepingangels.monsters.ood;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.UUID;

public class VillagerSpawnListener implements Listener {

    private final TARDIS plugin;

    public VillagerSpawnListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVillagerSpawnEvent(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.VILLAGER)) {
            return;
        }
        World world = entity.getWorld();
        if (!plugin.getMonstersConfig().getBoolean("ood.worlds." + world.getName())) {
            return;
        }
        // not from spawners
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            return;
        }
        if (TARDISConstants.RANDOM.nextInt(100) < plugin.getMonstersConfig().getInt("ood.spawn_from_villager")) {
            TWAFollower follower = new MonsterSpawner().createFollower(entity.getLocation(), new Follower(UUID.randomUUID(), TARDISWeepingAngels.UNCLAIMED, Monster.OOD));
            LivingEntity ood = (LivingEntity) follower.getBukkitEntity();
            OodEquipment.set(null, ood, false);
            plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(ood, EntityType.HUSK, Monster.OOD, entity.getLocation()));
            entity.remove();
        }
    }
}
