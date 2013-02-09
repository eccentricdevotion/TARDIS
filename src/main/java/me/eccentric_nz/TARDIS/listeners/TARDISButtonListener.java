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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import me.eccentric_nz.TARDIS.travel.TARDISTimetravel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * The various systems of the console room are fairly well-understood. According
 * to one account, each of the six panels controls a discrete function. The
 * navigation panel contains a time and space forward/back control, directional
 * pointer, atom accelerator and the spatial location input.
 *
 * @author eccentric_nz
 */
public class TARDISButtonListener implements Listener {

    private TARDIS plugin;
    List<Material> validBlocks = new ArrayList<Material>();
    Version bukkitversion;
    Version prewoodbuttonversion = new Version("1.4.2");

    public TARDISButtonListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        bukkitversion = new Version(v[0]);
        if (bukkitversion.compareTo(prewoodbuttonversion) >= 0) {
            validBlocks.add(Material.WOOD_BUTTON);
        }
        validBlocks.add(Material.STONE_BUTTON);
        validBlocks.add(Material.LEVER);
    }

    /**
     * Listens for player interaction with the TARDIS console button. If the
     * button is clicked it will return a random destination based on the
     * settings of the four TARDIS console repeaters.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onButtonInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                // only proceed if they are clicking a type of a button or a lever!
                if (validBlocks.contains(blockType)) {
                    // get clicked block location
                    Location b = block.getLocation();
                    String bw = b.getWorld().getName();
                    int bx = b.getBlockX();
                    int by = b.getBlockY();
                    int bz = b.getBlockZ();
                    String buttonloc = bw + ":" + bx + ":" + by + ":" + bz;
                    // get tardis from saved button location
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("button", buttonloc);
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        int id = rs.getTardis_id();
                        if (!rs.isHandbrake_on()) {
                            player.sendMessage(plugin.pluginName + ChatColor.RED + "You cannot set a destination while the TARDIS is travelling!");
                            return;
                        }
                        int level = rs.getArtron_level();
                        if (level < plugin.getConfig().getInt("random")) {
                            player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                            return;
                        }
                        String tl = rs.getOwner();
                        String r0_str = rs.getRepeater0();
                        String r1_str = rs.getRepeater1();
                        String r2_str = rs.getRepeater2();
                        String r3_str = rs.getRepeater3();
                        TARDISConstants.COMPASS dir = rs.getDirection();

                        // check if player is travelling
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        wheret.put("player", tl);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                        if (rst.resultSet()) {
                            QueryFactory qf = new QueryFactory(plugin);
                            // how many travellers are in the TARDIS?
                            plugin.utils.updateTravellerCount(id);
                            if (player.hasPermission("tardis.exile")) {
                                // get the exile area
                                String permArea = plugin.ta.getExileArea(player);
                                player.sendMessage(plugin.pluginName + ChatColor.RED + " Notice:" + ChatColor.RESET + " Your travel has been restricted to the [" + permArea + "] area!");
                                Location l = plugin.ta.getNextSpot(permArea);
                                if (l == null) {
                                    player.sendMessage(plugin.pluginName + "All available parking spots are taken in this area!");
                                } else {
                                    String save_loc = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    set.put("save", save_loc);
                                    HashMap<String, Object> wherel = new HashMap<String, Object>();
                                    wherel.put("tardis_id", id);
                                    qf.doUpdate("tardis", set, wherel);
                                    player.sendMessage(plugin.pluginName + "Your TARDIS was approved for parking in [" + permArea + "]!");
                                }
                            } else {
                                // get repeater settings
                                Location r0_loc = plugin.utils.getLocationFromDB(r0_str, 0, 0);
                                Block r0 = r0_loc.getBlock();
                                byte r0_data = r0.getData();
                                Location r1_loc = plugin.utils.getLocationFromDB(r1_str, 0, 0);
                                Block r1 = r1_loc.getBlock();
                                byte r1_data = r1.getData();
                                Location r2_loc = plugin.utils.getLocationFromDB(r2_str, 0, 0);
                                Block r2 = r2_loc.getBlock();
                                byte r2_data = r2.getData();
                                Location r3_loc = plugin.utils.getLocationFromDB(r3_str, 0, 0);
                                Block r3 = r3_loc.getBlock();
                                byte r3_data = r3.getData();
                                boolean playSound = true;
                                String environment = "NORMAL";
                                int nether_min = plugin.getConfig().getInt("nether_min");
                                int the_end_min = plugin.getConfig().getInt("the_end_min");
                                if (r0_data <= 3) { // first position
                                    if (!plugin.getConfig().getBoolean("nether") && !plugin.getConfig().getBoolean("the_end")) {
                                        environment = "NORMAL";
                                    } else if (!plugin.getConfig().getBoolean("nether") || !plugin.getConfig().getBoolean("the_end")) {
                                        if (plugin.getConfig().getBoolean("the_end") && player.hasPermission("tardis.end")) {
                                            environment = (level >= the_end_min) ? "NORMAL:THE_END" : "NORMAL";
                                        }
                                        if (plugin.getConfig().getBoolean("nether") && player.hasPermission("tardis.nether")) {
                                            environment = (level >= nether_min) ? "NORMAL:NETHER" : "NORMAL";
                                        }
                                    } else {
                                        if (player.hasPermission("tardis.end") && player.hasPermission("tardis.nether")) {
                                            // check they have enough artron energy to travel to the NETHER or THE_END
                                            if (level < nether_min) {
                                                environment = "NORMAL";
                                            } else if (level >= nether_min && level < the_end_min) {
                                                environment = "NORMAL:NETHER";
                                            } else {
                                                environment = "NORMAL:NETHER:THE_END";
                                            }
                                        }
                                        if (!player.hasPermission("tardis.end") && player.hasPermission("tardis.nether")) {
                                            // check they have enough artron energy to travel to the NETHER
                                            environment = (level >= nether_min) ? "NORMAL:NETHER" : "NORMAL";
                                        }
                                        if (player.hasPermission("tardis.end") && !player.hasPermission("tardis.nether")) {
                                            // check they have enough artron energy to travel to THE_END
                                            environment = (level >= the_end_min) ? "NORMAL:THE_END" : "NORMAL";
                                        }
                                    }
                                }
                                if (r0_data >= 4 && r0_data <= 7) { // second position
                                    environment = "NORMAL";
                                }
                                if (r0_data >= 8 && r0_data <= 11) { // third position
                                    if (plugin.getConfig().getBoolean("nether") && player.hasPermission("tardis.nether")) {
                                        // check they have enough artron energy to travel to the NETHER
                                        if (level < nether_min) {
                                            environment = "NORMAL";
                                            player.sendMessage(plugin.pluginName + "You need at least " + nether_min + " Artron Energy to travel to the Nether! Overworld selected.");
                                        } else {
                                            environment = "NETHER";
                                        }
                                    } else {
                                        String message = (player.hasPermission("tardis.nether")) ? "The ancient, dusty senators of Gallifrey have disabled time travel to the Nether" : "You do not have permission to time travel to the Nether";
                                        player.sendMessage(plugin.pluginName + message);
                                    }
                                }
                                if (r0_data >= 12 && r0_data <= 15) { // last position
                                    if (plugin.getConfig().getBoolean("the_end") && player.hasPermission("tardis.end")) {
                                        // check they have enough artron energy to travel to THE_END
                                        if (level < the_end_min) {
                                            environment = "NORMAL";
                                            player.sendMessage(plugin.pluginName + "You need at least " + the_end_min + " Artron Energy to travel to The End! Overworld selected.");
                                        } else {
                                            environment = "THE_END";
                                        }
                                    } else {
                                        String message = (player.hasPermission("tardis.end")) ? "The ancient, dusty senators of Gallifrey have disabled time travel to The End" : "You do not have permission to time travel to The End";
                                        player.sendMessage(plugin.pluginName + message);
                                    }
                                }
                                // create a random destination
                                TARDISTimetravel tt = new TARDISTimetravel(plugin);
                                Location rand = tt.randomDestination(player, r1_data, r2_data, r3_data, dir, environment);
                                if (rand != null) {
                                    String d = rand.getWorld().getName() + ":" + rand.getBlockX() + ":" + rand.getBlockY() + ":" + rand.getBlockZ();
                                    String dchat = rand.getWorld().getName() + " at x: " + rand.getBlockX() + " y: " + rand.getBlockY() + " z: " + rand.getBlockZ();
                                    boolean isTL = true;
                                    String comps = rs.getCompanions();
                                    if (comps != null && !comps.equals("")) {
                                        String[] companions = comps.split(":");
                                        for (String c : companions) {
                                            // are they online - AND are they travelling
                                            if (plugin.getServer().getPlayer(c) != null) {
                                                // are they travelling
                                                HashMap<String, Object> wherec = new HashMap<String, Object>();
                                                wherec.put("tardis_id", id);
                                                wherec.put("player", c);
                                                ResultSetTravellers rsc = new ResultSetTravellers(plugin, wherec, false);
                                                if (rsc.resultSet()) {
                                                    plugin.getServer().getPlayer(c).sendMessage(plugin.pluginName + "Destination: " + dchat);
                                                }
                                            }
                                            if (c.equalsIgnoreCase(player.getName())) {
                                                isTL = false;
                                            }
                                        }
                                    }
                                    if (isTL == true) {
                                        player.sendMessage(plugin.pluginName + "Destination: " + dchat);
                                    } else {
                                        if (plugin.getServer().getPlayer(rs.getOwner()) != null) {
                                            plugin.getServer().getPlayer(rs.getOwner()).sendMessage(plugin.pluginName + "Destination: " + dchat);
                                        }
                                    }
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    set.put("save", d);
                                    HashMap<String, Object> wherel = new HashMap<String, Object>();
                                    wherel.put("tardis_id", id);
                                    qf.doUpdate("tardis", set, wherel);
                                    plugin.tardisHasDestination.put(id, plugin.getConfig().getInt("random"));
//                                if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled() && playSound == true) {
//                                    SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_takeoff.mp3", false, b, 9, 75);
//                                }
                                } else {
                                    player.sendMessage(plugin.pluginName + "Could not find a suitable location within the current settings, the area may be protected.");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
