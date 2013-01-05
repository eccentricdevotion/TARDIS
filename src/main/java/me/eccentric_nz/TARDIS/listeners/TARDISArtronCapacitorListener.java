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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISArtronCapacitorListener implements Listener {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISArtronCapacitorListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with the button on the Artron Energy
     * Capacitor. If the button is right-clicked, then the Artron levels are
     * updated. Clicking with a Nether Star puts the capacitor to maximum,
     * clicking with the TARDIS key initialises the capacitor by spawning a
     * charged creeper inside it and sets the level to 50%. Clicking while
     * sneaking transfers player artron energy into the capacitor.
     *
     * If the button is just right-clicked, it displays the current capacitor
     * level as percentage of full.
     *
     * @author eccentric_nz
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCapacitorInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                // only proceed if they are clicking a button!
                if (blockType == Material.WOOD_BUTTON || blockType == Material.STONE_BUTTON) {
                    // get clicked block location
                    Location b = block.getLocation();
                    String bw = b.getWorld().getName();
                    int bx = b.getBlockX();
                    int by = b.getBlockY();
                    int bz = b.getBlockZ();
                    String buttonloc = bw + ":" + bx + ":" + by + ":" + bz;
                    // get tardis from saved button location
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("artron_button", buttonloc);
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        QueryFactory qf = new QueryFactory(plugin);
                        int current_level = rs.getArtron_level();
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", rs.getTardis_id());
                        // we need to get this block's location and then get the tardis_id from it
                        Material item = player.getItemInHand().getType();
                        if (item.equals(Material.NETHER_STAR)) {
                            // give TARDIS full charge
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            set.put("artron_level", 1000);
                            qf.doUpdate("tardis", set, wheret);
                            // remove the NETHER_STAR!
                            int a = player.getInventory().getItemInHand().getAmount();
                            int a2 = new Integer(a - 1);
                            if (a2 > 0) {
                                player.getInventory().getItemInHand().setAmount(a2);
                            } else {
                                player.getInventory().removeItem(new ItemStack(Material.NETHER_STAR, 1));
                            }
                            player.sendMessage(plugin.pluginName + "Artron Energy Levels at maximum!");
                        } else if (item.equals(Material.valueOf(plugin.getConfig().getString("key")))) {
                            // kickstart the TARDIS Artron Energy Capacitor
                            // get location from database
                            String[] creeperData = rs.getCreeper().split(":");
                            World w = b.getWorld();
                            float cx = 0, cy = 0, cz = 0;
                            try {
                                cx = Float.parseFloat(creeperData[1]);
                                cy = Float.parseFloat(creeperData[2]);
                                cz = Float.parseFloat(creeperData[3]);
                            } catch (NumberFormatException nfe) {
                                plugin.debug("Couldn't convert to a float! " + nfe.getMessage());
                            }
                            Location l = new Location(w, cx, cy, cz);
                            plugin.myspawn = true;
                            Entity e = w.spawnEntity(l, EntityType.CREEPER);
                            // if there is a creeper there already get rid of it!
                            boolean first_time = true;
                            for (Entity k : e.getNearbyEntities(1d, 1d, 1d)) {
                                if (k.getType().equals(EntityType.CREEPER)) {
                                    e.remove();
                                    first_time = false;
                                }
                            }
                            Creeper c = (Creeper) e;
                            c.setPowered(true);
                            // set the capacitor to 50% charge
                            if (first_time && current_level == 0) {
                                HashMap<String, Object> set = new HashMap<String, Object>();
                                set.put("artron_level", 500);
                                qf.doUpdate("tardis", set, wheret);
                                player.sendMessage(plugin.pluginName + "Artron Energy Capacitor activated! Levels at 50%");
                            } else {
                                player.sendMessage(plugin.pluginName + "You can only kick-start the Artron Energy Capacitor once!");
                            }
                        } else if (player.isSneaking()) {
                            // transfer player artron energy into the capacitor
                            if (current_level > 99) {
                                player.sendMessage(plugin.pluginName + "You can only transfer Timelord Artron Energy when the capacitor is below 10%");
                                return;
                            }
                            HashMap<String, Object> wherep = new HashMap<String, Object>();
                            wherep.put("player", player.getName());
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                            if (rsp.resultSet()) {
                                int level = rsp.getArtron_level();
                                if (level > 1) {
                                    player.sendMessage(plugin.pluginName + "You don't have any Artron Energy to give the TARDIS");
                                    return;
                                }
                                int new_level = current_level + level;
                                // set player level to 0
                                HashMap<String, Object> set = new HashMap<String, Object>();
                                set.put("artron_level", 0);
                                HashMap<String, Object> wherel = new HashMap<String, Object>();
                                wherel.put("player", player.getName());
                                qf.doUpdate("player_prefs", set, wherel);
                                // add player level to TARDIS level
                                HashMap<String, Object> sett = new HashMap<String, Object>();
                                sett.put("artron_level", new_level);
                                qf.doUpdate("tardis", sett, wheret);
                                int percent = Math.round((new_level * 100F) / 1000);
                                player.sendMessage(plugin.pluginName + "You charged the Artron Energy Capacitor to " + percent + "%");
                            } else {
                                player.sendMessage(plugin.pluginName + "You don't have any Artron Energy to give the TARDIS");
                            }
                        } else {
                            // just tell us how much energy we have
                            int percent = Math.round((current_level * 100F) / 1000);
                            player.sendMessage(plugin.pluginName + "The Artron Energy Capacitor is at " + percent + "%");
                        }
                    }
                }
            }
        }
    }

    /**
     * Listens for entity spawn events. If WorldGuard is enabled it blocks
     * mob-spawning inside the TARDIS, so this checks to see if we are doing the
     * spawning and un-cancels WorldGuards setCancelled(true).
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (plugin.myspawn) {
            event.setCancelled(false);
            plugin.myspawn = false;
        }
    }
}