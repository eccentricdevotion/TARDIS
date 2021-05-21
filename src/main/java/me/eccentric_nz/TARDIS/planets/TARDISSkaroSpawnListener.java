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
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelsAPI;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author eccentric_nz
 */
public class TARDISSkaroSpawnListener implements Listener {

    private final TARDIS plugin;
    private final TARDISWeepingAngelsAPI twaAPI;

    public TARDISSkaroSpawnListener(TARDIS plugin) {
        this.plugin = plugin;
        twaAPI = TARDISAngelsAPI.getAPI(this.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDalekSpawn(CreatureSpawnEvent event) {
        if (!event.getLocation().getWorld().getName().endsWith("skaro")) {
            return;
        }
        SpawnReason spawnReason = event.getSpawnReason();
        // get default server world
        String s_world = plugin.getServer().getWorlds().get(0).getName();
        // if configured prevent spawns (unless from spawners and plugins)
        if (!plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_skaro.spawn_other_mobs") && spawnReason != SpawnReason.SPAWNER && spawnReason != SpawnReason.CUSTOM) {
            event.setCancelled(true);
            return;
        }
        if (spawnReason == SpawnReason.SPAWNER) {
            if (!event.getEntity().getType().equals(EntityType.SKELETON)) {
                return;
            }
            LivingEntity le = event.getEntity();
            // it's a Dalek - disguise it!
            twaAPI.setDalekEquipment(le, false);
            if (plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_skaro.flying_daleks") && TARDISConstants.RANDOM.nextInt(100) < 10) {
                // make the Dalek fly
                EntityEquipment ee = le.getEquipment();
                ee.setChestplate(new ItemStack(Material.ELYTRA, 1));
                // teleport them straight up
                le.teleport(le.getLocation().add(0.0d, 20.0d, 0.0d));
                plugin.getTardisHelper().setFallFlyingTag(le);
            }
        }
    }
}
