/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * @author eccentric_nz
 */
public final class TARDISGallifreySpawnListener implements Listener {

    private final TARDIS plugin;

    public TARDISGallifreySpawnListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGallifreyanSpawn(CreatureSpawnEvent event) {
        if (!event.getLocation().getWorld().getName().endsWith("gallifrey")) {
            return;
        }
        CreatureSpawnEvent.SpawnReason spawnReason = event.getSpawnReason();
        // get default server world
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        // if configured prevent spawns (unless from spawners and plugins)
        if (!plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_gallifrey.spawn_other_mobs") && spawnReason != SpawnReason.SPAWNER && spawnReason != SpawnReason.CUSTOM) {
            event.setCancelled(true);
            return;
        }
        if (spawnReason == SpawnReason.SPAWNER) {
            if (!event.getEntity().getType().equals(EntityType.VILLAGER)) {
                return;
            }
            LivingEntity le = event.getEntity();
            // it's a Gallifreyan - give it a random profession and outfit!
            Villager villager = (Villager) le;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                villager.setProfession(Villager.Profession.values()[TARDISConstants.RANDOM.nextInt(Villager.Profession.values().length)]);
                villager.setVillagerLevel(1); // minimum level is 1
                villager.setVillagerExperience(1); // should be greater than 0 so villager doesn't lose its profession
                villager.setVillagerType(Villager.Type.values()[TARDISConstants.RANDOM.nextInt(Villager.Type.values().length)]);
            }, 2L);
        }
    }
}
