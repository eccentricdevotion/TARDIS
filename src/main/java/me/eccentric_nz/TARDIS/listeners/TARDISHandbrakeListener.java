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
import org.bukkit.Effect;
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
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (blockType == Material.LEVER) {
                //Checks handbrake location against the database.
                Location handbrake_loc = block.getLocation();
                World handbrake_locw = block.getWorld();
                String bw = handbrake_loc.getWorld().getName();
                int bx = handbrake_loc.getBlockX();
                int by = handbrake_loc.getBlockY();
                int bz = handbrake_loc.getBlockZ();
                String hb_loc = bw + ":" + bx + ":" + by + ":" + bz;
                //ResultSet data used through the file
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("handbrake", hb_loc);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                HashMap<String, Object> set = new HashMap<String, Object>();

                if (rs.resultSet()) {
                    event.setCancelled(true);
                    int id = rs.getTardis_id();
                    COMPASS d = rs.getDirection();
                    boolean cham = rs.isChamele_on();
                    String save = rs.getSave();
                    String cl = rs.getCurrent();
                    String owner = rs.getOwner();
                    Location exit = plugin.utils.getLocationFromDB(save, 0, 0);
                    boolean error = false;

                    if (!plugin.tardisMaterialising.contains(Integer.valueOf(id))) {
                        Action action = event.getAction();
                        BlockState state = block.getState();
                        Lever lever = (Lever) state.getData();

                        if (action == Action.RIGHT_CLICK_BLOCK) {
                            if (rs.isHandbrake_on()) {
                                if (plugin.tardisHasDestination.containsKey(Integer.valueOf(id)) && exit != null) {
                                    // Changes the lever to off.
                                    lever.setPowered(false);
                                    state.setData(lever);
                                    state.update();
                                    // Removes Blue Box and loads chunk if it unloaded somehow.
                                    if (!exit.getWorld().isChunkLoaded(exit.getChunk())) {
                                        exit.getWorld().loadChunk(exit.getChunk());
                                    }
                                    exit.getWorld().refreshChunk(exit.getChunk().getX(), exit.getChunk().getZ());

                                    Location l = plugin.utils.getLocationFromDB(cl, 0, 0);
                                    plugin.destroyPB.destroyTorch(l);
                                    plugin.destroyPB.destroySign(l, d);
                                    plugin.destroyPB.destroyPoliceBox(l, d, id, false);
                                    plugin.buildPB.buildPoliceBox(id, exit, d, cham, player, false);
                                    Chunk oldChunk = l.getChunk();
                                    if (plugin.tardisChunkList.contains(oldChunk)) {
                                        plugin.tardisChunkList.remove(oldChunk);
                                    }
                                    // Sets database and sends the player/world message/sounds.
                                    set.put("handbrake_on", 0);
                                    player.sendMessage(plugin.pluginName + "Handbrake OFF! Entering the time vortex...");
                                    if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                        SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, "https://dl.dropbox.com/u/53758864/tardis_land.mp3", false, handbrake_loc, 20, 75);
                                    } else {
                                        try {
                                            Class.forName("org.bukkit.Sound");
                                            handbrake_locw.playSound(handbrake_loc, Sound.MINECART_INSIDE, 1, 0);
                                        } catch (ClassNotFoundException e) {
                                            handbrake_locw.playEffect(handbrake_loc, Effect.BLAZE_SHOOT, 0);
                                        }
                                    }
                                } else {
                                    player.sendMessage(plugin.pluginName + "You need to set a destination first!");
                                    error = true;
                                }

                            } else {
                                player.sendMessage(plugin.pluginName + "The handbrake is already off!");
                                error = true;
                            }
                        }

                        if (action == Action.LEFT_CLICK_BLOCK) {
                            if (!rs.isHandbrake_on()) {
                                //Changes the lever to on.
                                lever.setPowered(true);
                                state.setData(lever);
                                state.update();
                                //Check if its at a recharge point
                                TARDISArtronLevels tal = new TARDISArtronLevels(plugin);
                                tal.recharge(id, player);
                                //Remove energy from TARDIS and sets database
                                set.put("handbrake_on", 1);
                                player.sendMessage(plugin.pluginName + "Handbrake ON! Nice parking...");
                                if (plugin.tardisHasDestination.containsKey(Integer.valueOf(id))) {
                                    QueryFactory qf = new QueryFactory(plugin);
                                    int amount = plugin.tardisHasDestination.get(id) * -1;
                                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                                    wheret.put("tardis_id", id);
                                    qf.alterEnergyLevel("tardis", amount, wheret, player);
                                    if (!player.getName().equals(owner)) {
                                        Player ptl = plugin.getServer().getPlayer(owner);
                                        if (ptl != null) {
                                            ptl.sendMessage(plugin.pluginName + "You used " + Math.abs(amount) + " Artron Energy.");
                                        }
                                    }
                                }
                                plugin.tardisHasDestination.remove(Integer.valueOf(id));
                                plugin.tardisHasTravelled.add(Integer.valueOf(id));
                            } else {
                                player.sendMessage(plugin.pluginName + "The handbrake is already on!");
                                error = true;
                            }
                        }
                        if (!error) {
                            QueryFactory qf = new QueryFactory(plugin);
                            HashMap<String, Object> whereh = new HashMap<String, Object>();
                            whereh.put("tardis_id", id);
                            qf.doUpdate("tardis", set, whereh);
                        }
                    } else {
                        player.sendMessage(plugin.pluginName + "You cannot change the handbrake while the TARDIS is in the time vortex!");
                    }
                }
            }
        }
    }
}
