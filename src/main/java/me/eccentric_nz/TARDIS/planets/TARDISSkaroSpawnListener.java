/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelsAPI;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSkaroSpawnListener implements Listener {

    private final TARDIS plugin;
    private final TARDISWeepingAngelsAPI twaAPI;

    public TARDISSkaroSpawnListener(TARDIS plugin) {
        this.plugin = plugin;
        this.twaAPI = TARDISAngelsAPI.getAPI(this.plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDalekSpawn(CreatureSpawnEvent event) {
        if (!event.getSpawnReason().equals(SpawnReason.SPAWNER)) {
            return;
        }
        if (!event.getLocation().getWorld().getName().equals("Skaro")) {
            return;
        }
        if (!event.getEntity().getType().equals(EntityType.SKELETON)) {
            return;
        }
        LivingEntity le = event.getEntity();
        // it's a Dalek - disguise it!
        twaAPI.setDalekEquipment(le);
    }
}
