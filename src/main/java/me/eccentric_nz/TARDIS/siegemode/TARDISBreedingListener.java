/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.siegemode;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * @author eccentric_nz
 */
public class TARDISBreedingListener implements Listener {

    private final TARDIS plugin;
    private final int chance;

    public TARDISBreedingListener(TARDIS plugin) {
        this.plugin = plugin;
        chance = this.plugin.getConfig().getInt("siege.breeding");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(CreatureSpawnEvent event) {
        Entity ent = event.getEntity();
        if (!(ent instanceof Ageable)) {
            return;
        }
        if (!event.getSpawnReason().equals(SpawnReason.BREEDING)) {
            return;
        }
        Location l = ent.getLocation();
        String w = l.getWorld().getName();
        if (!plugin.getTrackerKeeper().getSiegeBreedingAreas().containsKey(w)) {
            return;
        }
        plugin.getTrackerKeeper().getSiegeBreedingAreas().get(w).forEach((area) -> {
            if (area.isInSiegeArea(l) && TARDISConstants.RANDOM.nextInt(100) < chance) {
                // make them twins
                plugin.setTardisSpawn(true);
                Ageable twin = (Ageable) l.getWorld().spawnEntity(l, ent.getType());
                twin.setBaby();
            }
        });
    }
}
