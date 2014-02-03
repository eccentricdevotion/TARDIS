/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Isomorphic controls could only be operated by one user. Such controls
 * ostensibly worked only after identifying the allowed user through genetics or
 * other uniquely identifying properties, such as their biological morphic
 * field, of which the name "isomorphic" was derived from.
 *
 * @author eccentric_nz
 */
public class TARDISBindListener implements Listener {

    private final TARDIS plugin;
    List<Material> validBlocks = new ArrayList<Material>();

    public TARDISBindListener(TARDIS plugin) {
        this.plugin = plugin;
        validBlocks.add(Material.WALL_SIGN);
        validBlocks.add(Material.SIGN_POST);
        validBlocks.add(Material.WOOD_BUTTON);
        validBlocks.add(Material.STONE_BUTTON);
        validBlocks.add(Material.LEVER);
    }

    /**
     * Listens for player interaction with blocks after running the /tardisbind
     * [save|cmd|player|area] command. If the player's name is contained in the
     * trackBinder HashMap then the block location is recorded in the bind field
     * of the destinations table.
     *
     * If the player is travelling in the TARDIS then a check is made of the
     * destinations table for the location of the clicked block. If found the
     * destination for the next TARDIS time travel location is set.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();
        if (b != null) {
            Material m = b.getType();
            if (validBlocks.contains(m)) {
                final Player player = event.getPlayer();
                String playerNameStr = player.getName();
                String l = b.getLocation().toString();
                HashMap<String, Object> where = new HashMap<String, Object>();
                if (plugin.getTrackerKeeper().getTrackBinder().containsKey(playerNameStr)) {
                    where.put("dest_id", plugin.getTrackerKeeper().getTrackBinder().get(playerNameStr));
                    plugin.getTrackerKeeper().getTrackBinder().remove(playerNameStr);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("bind", l);
                    QueryFactory qf = new QueryFactory(plugin);
                    qf.doUpdate("destinations", set, where);
                    player.sendMessage(plugin.getPluginName() + "Save successfully bound to " + m.toString());
                } else {
                    // is player travelling in TARDIS
                    where.put("player", playerNameStr);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                        if (rs.resultSet()) {
                            String owner = rs.getOwner();
                            if (rs.isIso_on() && !player.getName().equals(owner) && !event.isCancelled()) {
                                player.sendMessage(plugin.getPluginName() + MESSAGE.ISO_ON.getText());
                                return;
                            }
                            HashMap<String, Object> whereb = new HashMap<String, Object>();
                            whereb.put("tardis_id", id);
                            whereb.put("bind", l);
                            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whereb, false);
                            if (rsd.resultSet()) {
                                if (!rs.isHandbrake_on()) {
                                    player.sendMessage(plugin.getPluginName() + ChatColor.RED + MESSAGE.NOT_WHILE_TRAVELLING.getText());
                                    return;
                                }
                                // what bind type is it?
                                int type = rsd.getType();
                                String dest_name = rsd.getDest_name();
                                switch (type) {
                                    case 1: // command
                                        if (dest_name.equals("rebuild")) {
                                            player.performCommand("tardis rebuild");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardis rebuild");
                                        }
                                        if (dest_name.equals("hide")) {
                                            player.performCommand("tardis hide");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardis hide");
                                        }
                                        if (dest_name.equals("home")) {
                                            player.performCommand("tardistravel home");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel home");
                                        }
                                        if (dest_name.equals("cave")) {
                                            player.performCommand("tardistravel cave");
                                            plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel cave");
                                        }
                                        break;
                                    case 2: // player
                                        player.performCommand("tardistravel " + dest_name);
                                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel " + dest_name);
                                        break;
                                    case 3: // area
                                        player.performCommand("tardistravel area " + dest_name);
                                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel area " + dest_name);
                                        break;
                                    case 4: // biome
                                        player.performCommand("tardistravel biome " + dest_name);
                                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel biome " + dest_name);
                                        break;
                                    default: // (0) save
                                        player.performCommand("tardistravel dest " + dest_name);
                                        plugin.getConsole().sendMessage(player.getName() + " issued server command: /tardistravel dest " + dest_name);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
