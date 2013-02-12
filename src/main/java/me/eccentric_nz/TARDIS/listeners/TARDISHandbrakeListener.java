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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Lever;
import org.getspout.spoutapi.SpoutManager;

/**
 * The handbrake was a utensil on the TARDIS used for quick stops. River song
 * once claimed that the TARDIS made it's "whoosh" noise because the Doctor had
 * left the handbrake on.
 *
 * @author eccentric_nz
 */
public class TARDISHandbrakeListener implements Listener {

    private final TARDIS plugin;

    public TARDISHandbrakeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with the handbrake (lever) on the TARDIS
     * console. If the button is right-clicked the handbrake is set off, if
     * right-clicked while sneaking it is set on.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (blockType == Material.LEVER) {
                // get clicked block location
                Location b = block.getLocation();
                World w = b.getWorld();
                String bw = b.getWorld().getName();
                int bx = b.getBlockX();
                int by = b.getBlockY();
                int bz = b.getBlockZ();
                String hb_loc = bw + ":" + bx + ":" + by + ":" + bz;
                // get tardis from saved handbrake location
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("handbrake", hb_loc);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    event.setCancelled(true);
                    int id = rs.getTardis_id();
                    // check to make sure we're not in the time vortex
                    if (!plugin.tardisMaterialising.contains(Integer.valueOf(id))) {
                        Action action = event.getAction();
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        String message = "";
                        BlockState state = block.getState();
                        Lever lever = (Lever) state.getData();
                        boolean update = false;
                        if (action == Action.RIGHT_CLICK_BLOCK) {
                            if (rs.isHandbrake_on()) {
                                // has a destination been set?
                                if (plugin.tardisHasDestination.containsKey(Integer.valueOf(id))) {
                                    // handbrake off
                                    lever.setPowered(true);
                                    state.setData(lever);
                                    state.update();
                                    set.put("handbrake_on", 0);
                                    if (plugin.getServer().getPluginManager().getPlugin("Spout") != null) {
                                        SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, "https://dl.dropbox.com/u/53758864/tardis_land.mp3", false, b, 20, 75);
                                    } else {
                                        w.playSound(b, Sound.MINECART_INSIDE, 1, 0);
                                    }
                                    message = "OFF! Entering the time vortex...";
                                    // get location from database
                                    String save = rs.getSave();
                                    String cl = rs.getCurrent();
                                    COMPASS d = rs.getDirection();
                                    boolean cham = rs.isChamele_on();
                                    Location exit = plugin.utils.getLocationFromDB(save, 0, 0);
                                    if (exit == null) {
                                        player.sendMessage(plugin.pluginName + "Could not engage time circuits! Try setting the destination again.");
                                        return;
                                    } else {
                                        Location l = plugin.utils.getLocationFromDB(cl, 0, 0);
                                        // remove torch
                                        plugin.destroyPB.destroyTorch(l);
                                        // remove sign
                                        plugin.destroyPB.destroySign(l, d);
                                        // remove blue box
                                        plugin.destroyPB.destroyPoliceBox(l, d, id, false);
                                        // remove current location chunk from list
                                        Chunk oldChunk = l.getChunk();
                                        if (plugin.tardisChunkList.contains(oldChunk)) {
                                            plugin.tardisChunkList.remove(oldChunk);
                                        }
                                        // try preloading destination chunk
                                        World exitWorld = exit.getWorld();
                                        Chunk chunk = exitWorld.getChunkAt(exit);
                                        if (!exitWorld.isChunkLoaded(chunk)) {
                                            exitWorld.loadChunk(chunk);
                                        }
                                        exitWorld.refreshChunk(chunk.getX(), chunk.getZ());
                                        // rebuild blue box
                                        plugin.buildPB.buildPoliceBox(id, exit, d, cham, player, false);
                                        update = true;
                                    }
                                } else {
                                    player.sendMessage(plugin.pluginName + "You need to set a destination first!");
                                }
                            } else {
                                player.sendMessage(plugin.pluginName + "The handbrake is already off!");
                            }
                        }
                        // can we use left click and set the lever position - yes
                        if (action == Action.LEFT_CLICK_BLOCK) {
                            if (!rs.isHandbrake_on()) {
                                // handbrake on
                                lever.setPowered(false);
                                state.setData(lever);
                                state.update();
                                set.put("handbrake_on", 1);
                                message = "ON! Nice parking.";
                                // check if at a recharge point
                                TARDISArtronLevels tal = new TARDISArtronLevels(plugin);
                                tal.recharge(id, player);
                                // remove energy from TARDIS
                                if (plugin.tardisHasDestination.containsKey(Integer.valueOf(id))) {
                                    QueryFactory qf = new QueryFactory(plugin);
                                    int amount = plugin.tardisHasDestination.get(id) * -1;
                                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                                    wheret.put("tardis_id", id);
                                    qf.alterEnergyLevel("tardis", amount, wheret, player);
                                    String owner = rs.getOwner();
                                    if (!player.getName().equals(owner)) {
                                        Player ptl = plugin.getServer().getPlayer(owner);
                                        ptl.sendMessage(plugin.pluginName + "You used " + Math.abs(amount) + " Artron Energy.");
                                    }
                                    plugin.tardisHasDestination.remove(Integer.valueOf(id));
                                    plugin.tardisHasTravelled.add(Integer.valueOf(id));
                                }
                                update = true;
                            } else {
                                player.sendMessage(plugin.pluginName + "The handbrake is already on!");
                            }
                        }
                        if (update) {
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> whereh = new HashMap<String, Object>();
                            whereh.put("tardis_id", id);
                            qf.doUpdate("tardis", set, whereh);
                            player.sendMessage(plugin.pluginName + "Handbrake " + message);
                        }
                    } else {
                        player.sendMessage(plugin.pluginName + "You cannot change the handbrake while the TARDIS is in the time vortex!");
                    }
                }
            }
        }
    }
}
