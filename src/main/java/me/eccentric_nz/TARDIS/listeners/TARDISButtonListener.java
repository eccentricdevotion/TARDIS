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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetLamps;
import me.eccentric_nz.TARDIS.database.ResultSetRepeaters;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.rooms.TARDISARSInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
    List<Integer> onlythese = Arrays.asList(new Integer[]{1, 8, 9, 10, 11, 12});
    public ItemStack[] items;
    private ItemStack[] tars;
    private ItemStack[] clocks;

    public TARDISButtonListener(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.bukkitversion.compareTo(plugin.prewoodbuttonversion) >= 0) {
            validBlocks.add(Material.WOOD_BUTTON);
        }
        if (plugin.bukkitversion.compareTo(plugin.precomparatorversion) >= 0) {
            validBlocks.add(Material.REDSTONE_COMPARATOR_OFF);
            validBlocks.add(Material.REDSTONE_COMPARATOR_ON);
        }
        validBlocks.add(Material.STONE_BUTTON);
        validBlocks.add(Material.LEVER);
        validBlocks.add(Material.WALL_SIGN);
        this.items = new TARDISTerminalInventory().getTerminal();
        this.tars = new TARDISARSInventory().getTerminal();
        this.clocks = new TARDISTemporalLocatorInventory().getTerminal();
    }

    /**
     * Listens for player interaction with the TARDIS console button. If the
     * button is clicked it will return a random destination based on the
     * settings of the four TARDIS console repeaters.
     *
     * @param event the player clicking a block
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
                    String buttonloc = block.getLocation().toString();
                    // get tardis from saved button location
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    //where.put("type", 1);
                    where.put("location", buttonloc);
                    ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                    if (rsc.resultSet()) {
                        int id = rsc.getTardis_id();
                        int type = rsc.getType();
                        if (!onlythese.contains(Integer.valueOf(type))) {
                            return;
                        }
                        HashMap<String, Object> whereid = new HashMap<String, Object>();
                        whereid.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, whereid, "", false);
                        if (rs.resultSet()) {
                            int level = rs.getArtron_level();
                            boolean hb = rs.isHandbrake_on();
                            boolean set_dest = false;
                            String d = rs.getCurrent();
                            QueryFactory qf = new QueryFactory(plugin);
                            switch (type) {
                                case 1:
                                    if (!hb) {
                                        player.sendMessage(plugin.pluginName + ChatColor.RED + "You cannot set a destination while the TARDIS is travelling!");
                                        return;
                                    }
                                    if (level < plugin.getArtronConfig().getInt("random")) {
                                        player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                                        return;
                                    }

                                    String[] current = d.split(":");
                                    TARDISConstants.COMPASS dir = rs.getDirection();
                                    if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("exile")) {
                                        // get the exile area
                                        String permArea = plugin.ta.getExileArea(player);
                                        player.sendMessage(plugin.pluginName + ChatColor.RED + " Notice:" + ChatColor.RESET + " Your travel has been restricted to the [" + permArea + "] area!");
                                        Location l = plugin.ta.getNextSpot(permArea);
                                        if (l == null) {
                                            player.sendMessage(plugin.pluginName + "All available parking spots are taken in this area!");
                                        } else {
                                            d = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                                            player.sendMessage(plugin.pluginName + "Your TARDIS was approved for parking in [" + permArea + "]!");
                                            set_dest = true;
                                        }
                                    } else {
                                        ResultSetRepeaters rsr = new ResultSetRepeaters(plugin, id, rsc.getSecondary());
                                        if (rsr.resultSet()) {
                                            String environment = "THIS";
                                            int nether_min = plugin.getArtronConfig().getInt("nether_min");
                                            int the_end_min = plugin.getArtronConfig().getInt("the_end_min");
                                            byte[] repeaters = rsr.getRepeaters();
                                            if (repeaters[0] <= 3) { // first position
                                                environment = "THIS";
                                            }
                                            if (repeaters[0] >= 4 && repeaters[0] <= 7) { // second position
                                                environment = "NORMAL";
                                            }
                                            if (repeaters[0] >= 8 && repeaters[0] <= 11) { // third position
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
                                            if (repeaters[0] >= 12 && repeaters[0] <= 15) { // last position
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
                                            TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                                            Location rand = tt.randomDestination(player, repeaters[1], repeaters[2], repeaters[3], dir, environment, current[0]);
                                            if (rand != null) {
                                                d = rand.getWorld().getName() + ":" + rand.getBlockX() + ":" + rand.getBlockY() + ":" + rand.getBlockZ();
                                                set_dest = true;
                                                String dchat = rand.getWorld().getName() + " at x: " + rand.getBlockX() + " y: " + rand.getBlockY() + " z: " + rand.getBlockZ();
                                                boolean isTL = true;
                                                String comps = rs.getCompanions();
                                                if (comps != null && !comps.isEmpty()) {
                                                    String[] companions = comps.split(":");
                                                    for (String c : companions) {
                                                        // are they online - AND are they travelling
                                                        if (plugin.getServer().getPlayer(c) != null) {
                                                            // are they travelling
                                                            HashMap<String, Object> wherec = new HashMap<String, Object>();
                                                            wherec.put("tardis_id", id);
                                                            wherec.put("player", c);
                                                            ResultSetTravellers rsv = new ResultSetTravellers(plugin, wherec, false);
                                                            if (rsv.resultSet()) {
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
                                            } else {
                                                player.sendMessage(plugin.pluginName + "Could not find a suitable location within the current settings, the area may be protected.");
                                            }
                                        }
                                    }
                                    break;
                                case 8:
                                    if (!hb) {
                                        player.sendMessage(plugin.pluginName + ChatColor.RED + "You cannot set a destination while the TARDIS is travelling!");
                                        return;
                                    }
                                    if (level < plugin.getArtronConfig().getInt("random")) {
                                        player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                                        return;
                                    }
                                    // fast return button
                                    if (!d.equals(rs.getFast_return())) {
                                        d = rs.getFast_return();
                                        set_dest = true;
                                        player.sendMessage(plugin.pluginName + "Previous location selected. Please release the handbrake!");
                                    } else {
                                        player.sendMessage(plugin.pluginName + "You are already at the previous location. You need to travel somewhere else first!");
                                    }
                                    break;
                                case 9:
                                    if (!hb) {
                                        player.sendMessage(plugin.pluginName + ChatColor.RED + "You cannot set a destination while the TARDIS is travelling!");
                                        return;
                                    }
                                    if (level < plugin.getArtronConfig().getInt("random")) {
                                        player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                                        return;
                                    }
                                    // terminal sign
                                    Inventory aec = plugin.getServer().createInventory(player, 54, "§4Destination Terminal");
                                    aec.setContents(items);
                                    player.openInventory(aec);
                                    break;
                                case 10:
                                    if (!hb) {
                                        player.sendMessage(plugin.pluginName + ChatColor.RED + "You cannot reconfigure rooms while the TARDIS is travelling!");
                                        return;
                                    }
                                    // ARS sign
                                    Inventory ars = plugin.getServer().createInventory(player, 54, "§4Architectural Reconfiguration");
                                    ars.setContents(tars);
                                    player.openInventory(ars);
                                    break;
                                case 11:
                                    // Temporal Locator sign
                                    if (player.hasPermission("tardis.temporal")) {
                                        Inventory tmpl = plugin.getServer().createInventory(player, 27, "§4Temporal Locator");
                                        tmpl.setContents(clocks);
                                        player.openInventory(tmpl);
                                    }
                                    break;
                                case 12:
                                    // Control room light switch
                                    HashMap<String, Object> wherel = new HashMap<String, Object>();
                                    wherel.put("tardis_id", id);
                                    ResultSetLamps rsl = new ResultSetLamps(plugin, wherel, true);
                                    List<Block> lamps = new ArrayList<Block>();
                                    if (rsl.resultSet()) {
                                        // get lamp locations
                                        ArrayList<HashMap<String, String>> data = rsl.getData();
                                        for (HashMap<String, String> map : data) {
                                            Location loc = plugin.utils.getLocationFromDB(map.get("location"), 0.0F, 0.0F);
                                            lamps.add(loc.getBlock());
                                        }
                                    }
                                    for (Block b : lamps) {
                                        if (b.getTypeId() == 124) {
                                            b.setTypeId(19);
                                        } else {
                                            b.setTypeId(124);
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            if (set_dest) {
                                HashMap<String, Object> set = new HashMap<String, Object>();
                                set.put("save", d);
                                HashMap<String, Object> wherel = new HashMap<String, Object>();
                                wherel.put("tardis_id", id);
                                qf.doUpdate("tardis", set, wherel);
                                plugin.tardisHasDestination.put(id, plugin.getArtronConfig().getInt("random"));
                                if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                    plugin.trackRescue.remove(Integer.valueOf(id));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
