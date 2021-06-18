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
package me.eccentric_nz.tardis.siegemode;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TardisBreedingListener implements Listener {

    private final TardisPlugin plugin;
    private final int chance;

    public TardisBreedingListener(TardisPlugin plugin) {
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
        String w = Objects.requireNonNull(l.getWorld()).getName();
        if (!plugin.getTrackerKeeper().getSiegeBreedingAreas().containsKey(w)) {
            return;
        }
        plugin.getTrackerKeeper().getSiegeBreedingAreas().get(w).forEach((area) -> {
            if (area.isInSiegeArea(l) && TardisConstants.RANDOM.nextInt(100) < chance) {
                // make them twins
                plugin.setTardisSpawn(true);
                Ageable twin = (Ageable) l.getWorld().spawnEntity(l, ent.getType());
                twin.setBaby();
            }
        });
    }
}
