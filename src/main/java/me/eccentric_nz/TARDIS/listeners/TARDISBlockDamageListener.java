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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

/**
 * @author eccentric_nz
 */
public class TARDISBlockDamageListener implements Listener {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISBlockDamageListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for block damage to the TARDIS Police Box. If the block is a
     * Police Box block then the event is canceled, and the player warned.
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPoliceBoxDamage(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player p = event.getPlayer();
        Block b = event.getBlock();
        String l = b.getLocation().toString();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("location", l);
        ResultSetBlocks rs = new ResultSetBlocks(plugin, where, false);
        if (rs.resultSet()) {
            event.setCancelled(true);
            p.sendMessage(plugin.pluginName + "You cannot break the TARDIS blocks!");
        }
    }
}