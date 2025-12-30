/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class ChunkCleanListener implements Listener {

    private final TARDIS plugin;

    public ChunkCleanListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent event) {
        if (event.getWorld().getName().contains("TARDIS")) {
            return;
        }
        if (!plugin.getConfig().getBoolean("preferences.clean")) {
            return;
        }
        for (Entity entity : event.getChunk().getEntities()) {
            int i = 0;
            int j = 0;
            if (entity instanceof ArmorStand stand) {
                if (stand.isInvisible() && stand.isInvulnerable()) {
                    // check it has no TARDIS record
                    if (checkLocation(stand)) {
                        continue;
                    }
                    stand.remove();
                    i++;
                }
            }
            if (entity instanceof Interaction interaction) {
                if (interaction.getPersistentDataContainer().has(plugin.getTardisIdKey(), PersistentDataType.INTEGER)) {
                    // check it has no TARDIS record
                    if (checkLocation(interaction)) {
                        continue;
                    }
                    interaction.remove();
                    j++;
                }
            }
            if (i > 0) {
                plugin.debug("Removed " + i + " armour stands in " + event.getWorld().getName());
            }
            if (j > 0) {
                plugin.debug("Removed " + j + " interactions in " + event.getWorld().getName());
            }
        }
    }

    private boolean checkLocation(Entity entity) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("world", entity.getWorld().getName());
        where.put("x", entity.getLocation().getBlockX());
        where.put("y", entity.getLocation().getBlockY());
        where.put("z", entity.getLocation().getBlockZ());
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
        return rsc.resultSet();
    }
}