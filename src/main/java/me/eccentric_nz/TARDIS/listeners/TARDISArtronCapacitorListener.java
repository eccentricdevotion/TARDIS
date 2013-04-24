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
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import org.bukkit.Bukkit;
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
 * The Ninth Doctor used the Cardiff rift to "re-charge" his TARDIS. The process
 * took 2 days.
 *
 * @author eccentric_nz
 */
public class TARDISArtronCapacitorListener implements Listener {

    private final TARDIS plugin;
    List<Material> validBlocks = new ArrayList<Material>();
    Version bukkitversion;
    Version prewoodbuttonversion = new Version("1.4.2");

    public TARDISArtronCapacitorListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        bukkitversion = (!v[0].equalsIgnoreCase("unknown")) ? new Version(v[0]) : new Version("1.4.7");
        if (bukkitversion.compareTo(prewoodbuttonversion) >= 0) {
            validBlocks.add(Material.WOOD_BUTTON);
        }
        validBlocks.add(Material.STONE_BUTTON);
    }

    /**
     * Listens for player interaction with the button on the Artron Energy
     * Capacitor. If the button is right-clicked, then the Artron levels are
     * updated. Clicking with a Nether Star puts the capacitor to maximum,
     * clicking with the TARDIS key initialises the capacitor by spawning a
     * charged creeper inside it and sets the level to 50%. Clicking while
     * sneaking transfers player Artron Energy into the capacitor.
     *
     * If the button is just right-clicked, it displays the current capacitor
     * level as percentage of full.
     *
     * @param event the player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCapacitorInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                // only proceed if they are clicking a button!
                if (validBlocks.contains(blockType)) {
                    // we need to get this block's location and then get the tardis_id from it
                    String buttonloc = block.getLocation().toString();
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("type", 6);
                    where.put("location", buttonloc);
                    ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                    if (rsc.resultSet()) {
                        // get tardis data
                        int id = rsc.getTardis_id();
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                        if (rs.resultSet()) {
                            HashMap<String, Object> whereid = new HashMap<String, Object>();
                            whereid.put("tardis_id", id);
                            int current_level = rs.getArtron_level();
                            int fc = plugin.getConfig().getInt("full_charge");
                            Material item = player.getItemInHand().getType();
                            Material full = Material.valueOf(plugin.getConfig().getString("full_charge_item"));
                            QueryFactory qf = new QueryFactory(plugin);
                            // determine key item
                            HashMap<String, Object> wherek = new HashMap<String, Object>();
                            wherek.put("player", player.getName());
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
                            String key;
                            boolean hasPrefs = false;
                            if (rsp.resultSet()) {
                                hasPrefs = true;
                                key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("key");
                            } else {
                                key = plugin.getConfig().getString("key");
                            }
                            if (item.equals(full)) {
                                // give TARDIS full charge
                                HashMap<String, Object> set = new HashMap<String, Object>();
                                set.put("artron_level", fc);
                                qf.doUpdate("tardis", set, whereid);
                                // remove the NETHER_STAR!
                                int a = player.getInventory().getItemInHand().getAmount();
                                int a2 = Integer.valueOf(a) - 1;
                                if (a2 > 0) {
                                    player.getInventory().getItemInHand().setAmount(a2);
                                } else {
                                    player.getInventory().removeItem(new ItemStack(full, 1));
                                }
                                player.sendMessage(plugin.pluginName + "Artron Energy Levels at maximum!");
                            } else if (item.equals(Material.getMaterial(key))) {
                                // kickstart the TARDIS Artron Energy Capacitor
                                // has the TARDIS been initialised?
                                if (!rs.isTardis_init()) {
                                    // get location from database
                                    String creeper = rs.getCreeper();
                                    if (!creeper.isEmpty() && !creeper.equals(":")) {
                                        String[] creeperData = creeper.split(":");
                                        World w = block.getLocation().getWorld();
                                        float cx = 0, cy = 0, cz = 0;
                                        try {
                                            cx = Float.parseFloat(creeperData[1]);
                                            cy = Float.parseFloat(creeperData[2]) + 1;
                                            cz = Float.parseFloat(creeperData[3]);
                                        } catch (NumberFormatException nfe) {
                                            plugin.debug("Couldn't convert to a float! " + nfe.getMessage());
                                        }
                                        Location l = new Location(w, cx, cy, cz);
                                        plugin.myspawn = true;
                                        Entity e = w.spawnEntity(l, EntityType.CREEPER);
                                        Creeper c = (Creeper) e;
                                        c.setPowered(true);
                                    }
                                    // set the capacitor to 50% charge
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    int half = Math.round(fc / 2.0F);
                                    set.put("artron_level", half);
                                    set.put("tardis_init", 1);
                                    qf.doUpdate("tardis", set, whereid);
                                    player.sendMessage(plugin.pluginName + "Artron Energy Capacitor activated! Levels at 50%");
                                } else {
                                    player.sendMessage(plugin.pluginName + "You can only kick-start the Artron Energy Capacitor once!");
                                }
                            } else if (player.isSneaking()) {
                                // transfer player artron energy into the capacitor
                                int ten_percent = Math.round(fc * 0.1F);
                                if (current_level >= ten_percent && plugin.getConfig().getBoolean("create_worlds")) {
                                    player.sendMessage(plugin.pluginName + "You can only transfer Timelord Artron Energy when the capacitor is below 10%");
                                    return;
                                }
                                HashMap<String, Object> wherep = new HashMap<String, Object>();
                                wherep.put("player", player.getName());
                                if (hasPrefs) {
                                    int level = rsp.getArtron_level();
                                    if (level < 1) {
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
                                    qf.doUpdate("tardis", sett, whereid);
                                    int percent = Math.round((new_level * 100F) / fc);
                                    player.sendMessage(plugin.pluginName + "You charged the Artron Energy Capacitor to " + percent + "%");
                                } else {
                                    player.sendMessage(plugin.pluginName + "You don't have any Artron Energy to give the TARDIS");
                                }
                            } else {
                                // just tell us how much energy we have
                                int percent = Math.round((current_level * 100F) / fc);
                                player.sendMessage(plugin.pluginName + "The Artron Energy Capacitor is at " + percent + "%");
                            }
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
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (plugin.myspawn) {
            event.setCancelled(false);
            plugin.myspawn = false;
        }
    }
}
