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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author eccentric_nz
 */
public class TARDISAreaListener implements Listener {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISAreaListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking blocks. If the player's name is contained in
     * various tracking HashMaps then we know that they are trying to create a
     * TARDIS area.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAreaInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();
        final String playerNameStr = player.getName();

        Block block = event.getClickedBlock();
        if (block != null) {
            if (plugin.trackName.containsKey(playerNameStr) && !plugin.trackBlock.containsKey(playerNameStr)) {
                Location block_loc = block.getLocation();
                // check if block is in an already defined area
                if (plugin.ta.areaCheckInExisting(block_loc)) {
                    String locStr = block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockZ();
                    plugin.trackBlock.put(playerNameStr, locStr);
                    player.sendMessage(plugin.pluginName + "You have 60 seconds to select the area end block - use the " + ChatColor.GREEN + "/tardisarea end" + ChatColor.RESET + " command.");
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.trackName.remove(playerNameStr);
                            plugin.trackBlock.remove(playerNameStr);
                        }
                    }, 1200L);
                } else {
                    player.sendMessage(plugin.pluginName + "That block is inside an already defined area! Try somewhere else.");
                }
            } else if (plugin.trackBlock.containsKey(playerNameStr) && plugin.trackEnd.containsKey(playerNameStr)) {
                Location block_loc = block.getLocation();
                // check if block is in an already defined area
                if (plugin.ta.areaCheckInExisting(block_loc)) {
                    String[] firstblock = plugin.trackBlock.get(playerNameStr).split(":");
                    if (!block_loc.getWorld().getName().equals(firstblock[0])) {
                        player.sendMessage(plugin.pluginName + ChatColor.RED + " Area start and end blocks must be in the same world! Try again");
                    }
                    int minx, minz, maxx, maxz;
                    if (plugin.utils.parseNum(firstblock[1]) < block_loc.getBlockX()) {
                        minx = plugin.utils.parseNum(firstblock[1]);
                        maxx = block_loc.getBlockX();
                    } else {
                        minx = block_loc.getBlockX();
                        maxx = plugin.utils.parseNum(firstblock[1]);
                    }
                    if (plugin.utils.parseNum(firstblock[2]) < block_loc.getBlockZ()) {
                        minz = plugin.utils.parseNum(firstblock[2]);
                        maxz = block_loc.getBlockZ();
                    } else {
                        minz = block_loc.getBlockZ();
                        maxz = plugin.utils.parseNum(firstblock[2]);
                    }
                    String n = plugin.trackName.get(playerNameStr);
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("area_name", n);
                    set.put("world", firstblock[0]);
                    set.put("minx", minx);
                    set.put("minz", minz);
                    set.put("maxx", maxx);
                    set.put("maxz", maxz);
                    qf.doPreparedInsert("areas", set);
                    player.sendMessage(plugin.pluginName + "The area [" + plugin.trackName.get(playerNameStr) + "] was saved successfully");
                    plugin.trackName.remove(playerNameStr);
                    plugin.trackBlock.remove(playerNameStr);
                    plugin.trackEnd.remove(playerNameStr);
                } else {
                    player.sendMessage(plugin.pluginName + "That block is inside an already defined area! Try somewhere else.");
                }
            }
        }
    }
}