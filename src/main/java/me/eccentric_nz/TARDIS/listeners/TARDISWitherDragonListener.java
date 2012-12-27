/*
 * Copyright (C) 2012 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

/**
 * Listens for Wither and Dragon entity interaction with the TARDIS blocks. If
 * the block is a TARDIS block, then the block change event is canceled.
 *
 * @author eccentric_nz
 */
public class TARDISWitherDragonListener implements Listener {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISWitherDragonListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void bossBlockBreak(EntityChangeBlockEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        EntityType eType;
        try {
            eType = event.getEntityType();
        } catch (Exception e) {
            eType = null;
        }
        if ((eType != null) && (eType == EntityType.WITHER || eType == EntityType.ENDER_DRAGON)) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("location", l);
            ResultSetBlocks rs = new ResultSetBlocks(plugin, where, false);
            if (rs.resultSet()) {
                event.setCancelled(true);
            }
        }
    }
}