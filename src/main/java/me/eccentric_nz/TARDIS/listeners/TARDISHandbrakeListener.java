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
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.travel.TARDISMalfunction;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
     *
     * @param event the player clicking the handbrake
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (blockType == Material.LEVER) {
                //Checks handbrake location against the database.
                Location handbrake_loc = block.getLocation();
                World handbrake_locw = block.getWorld();
                String hb_loc = block.getLocation().toString();
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("type", 0);
                where.put("location", hb_loc);
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    final int id = rsc.getTardis_id();
                    HashMap<String, Object> wherei = new HashMap<String, Object>();
                    wherei.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    if (rs.resultSet()) {
                        event.setCancelled(true);
                        String owner = rs.getOwner();
                        if (rs.isIso_on() && !player.getName().equals(owner) && event.isCancelled()) { // check if cancelled so we don't get double messages from the bind listener
                            player.sendMessage(plugin.pluginName + "The isomorphic security lockout has been engaged... Hands off the controls!");
                            return;
                        }
                        final COMPASS d = rs.getDirection();
                        final boolean cham = rs.isChamele_on();
                        String save = rs.getSave();
                        String cl = rs.getCurrent();
                        String beacon = rs.getBeacon();
                        Location exit = null;
                        boolean error = false;
                        if (!plugin.tardisMaterialising.contains(Integer.valueOf(id)) && !plugin.tardisDematerialising.contains(id)) {
                            Action action = event.getAction();
                            BlockState state = block.getState();
                            Lever lever = (Lever) state.getData();
                            int dist = 0;
                            // should the beacon turn on
                            HashMap<String, Object> wherek = new HashMap<String, Object>();
                            wherek.put("player", player.getName());
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
                            boolean beac_on;
                            if (rsp.resultSet()) {
                                beac_on = rsp.isBeacon_on();
                            } else {
                                beac_on = true;
                            }
                            if (action == Action.RIGHT_CLICK_BLOCK) {
                                if (rs.isHandbrake_on()) {
                                    if (plugin.tardisHasDestination.containsKey(Integer.valueOf(id))) {
                                        if (!beac_on && !beacon.isEmpty()) {
                                            toggleBeacon(beacon, true);
                                        }
                                        boolean malfunction = false;
                                        if (plugin.getConfig().getInt("malfunction") > 0) {
                                            // check for a malfunction
                                            TARDISMalfunction m = new TARDISMalfunction(plugin, id, player, d, handbrake_loc);
                                            malfunction = m.isMalfunction();
                                            if (malfunction) {
                                                exit = m.getMalfunction();
                                                if (exit != null) {
                                                    save = exit.getWorld().getName() + ":" + exit.getBlockX() + ":" + exit.getBlockY() + ":" + exit.getBlockZ();
                                                    if (plugin.tardisHasDestination.containsKey(Integer.valueOf(id))) {
                                                        QueryFactory qf = new QueryFactory(plugin);
                                                        int amount = plugin.tardisHasDestination.get(id) * -1;
                                                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                                                        wheret.put("tardis_id", id);
                                                        qf.alterEnergyLevel("tardis", amount, wheret, player);
                                                        player.sendMessage(plugin.pluginName + "Are you sure you know how to fly this thing!");
                                                        plugin.tardisHasDestination.remove(Integer.valueOf(id));
                                                    }
                                                    // play tardis crash sound
                                                    if (plugin.pm.getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                                        SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, "https://dl.dropboxusercontent.com/u/53758864/tardis_emergency_land.mp3", false, handbrake_loc, 20, 75);
                                                    } else {
                                                        try {
                                                            Class.forName("org.bukkit.Sound");
                                                            handbrake_loc.getWorld().playSound(handbrake_loc, Sound.MINECART_INSIDE, 1, 0);
                                                        } catch (ClassNotFoundException e) {
                                                            handbrake_loc.getWorld().playEffect(handbrake_loc, Effect.BLAZE_SHOOT, 0);
                                                        }
                                                    }
                                                    // add a potion effect to the player
                                                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 5));
                                                }
                                            }
                                        }
                                        if (!malfunction) {
                                            exit = plugin.utils.getLocationFromDB(save, 0, 0);
                                            // Changes the lever to off.
                                            lever.setPowered(false);
                                            state.setData(lever);
                                            state.update();
                                            // Sets database and sends the player/world message/sounds.
                                            set.put("handbrake_on", 0);
                                            player.sendMessage(plugin.pluginName + "Handbrake OFF! Entering the time vortex...");
                                            if (plugin.pm.getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                                SpoutManager.getSoundManager().playGlobalCustomSoundEffect(plugin, "https://dl.dropboxusercontent.com/u/53758864/tardis_land.mp3", false, handbrake_loc, 20, 75);
                                            } else {
                                                try {
                                                    Class.forName("org.bukkit.Sound");
                                                    handbrake_locw.playSound(handbrake_loc, Sound.MINECART_INSIDE, 1, 0);
                                                } catch (ClassNotFoundException e) {
                                                    handbrake_locw.playEffect(handbrake_loc, Effect.BLAZE_SHOOT, 0);
                                                }
                                            }
                                        }
                                        if (exit != null) {
                                            // Removes Blue Box and loads chunk if it unloaded somehow.
                                            if (!exit.getWorld().isChunkLoaded(exit.getChunk())) {
                                                exit.getWorld().loadChunk(exit.getChunk());
                                            }
                                            exit.getWorld().refreshChunk(exit.getChunk().getX(), exit.getChunk().getZ());
                                            Location l = plugin.utils.getLocationFromDB(cl, 0, 0);
                                            if (!rs.isHidden()) {
                                                plugin.tardisDematerialising.add(id);
                                                plugin.destroyPB.destroyPoliceBox(l, d, id, false, plugin.getConfig().getBoolean("materialise"), cham, player);
//                                                plugin.destroyPB.destroyTorch(l);
//                                                plugin.destroyPB.destroySign(l, d);
                                            }
                                            long delay = (plugin.getConfig().getBoolean("materialise")) ? 200L : 1L;
                                            final Location e = exit;
                                            final boolean mal = malfunction;
                                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                @Override
                                                public void run() {
                                                    plugin.buildPB.buildPoliceBox(id, e, d, cham, player, false, mal);
                                                }
                                            }, delay);
                                            Chunk oldChunk = l.getChunk();
                                            if (plugin.tardisChunkList.contains(oldChunk)) {
                                                plugin.tardisChunkList.remove(oldChunk);
                                            }
                                            set.put("current", save);
                                            long now;
                                            if (player.hasPermission("tardis.prune.bypass")) {
                                                now = Long.MAX_VALUE;
                                            } else {
                                                now = System.currentTimeMillis();
                                            }
                                            set.put("lastuse", now);
                                            if (plugin.getAchivementConfig().getBoolean("travel.enabled")) {
                                                if (l.getWorld().equals(exit.getWorld())) {
                                                    dist = (int) l.distance(exit);
                                                }
                                            }
                                        } else {
                                            player.sendMessage(plugin.pluginName + "There was a problem finding the exit!");
                                            error = true;
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
                                    tal.recharge(id);
                                    if (!beac_on && !beacon.isEmpty()) {
                                        toggleBeacon(beacon, false);
                                    }
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
                                if (dist > 0 && plugin.getAchivementConfig().getBoolean("travel.enabled")) {
                                    TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, "travel", 1);
                                    taf.doAchievement(dist);
                                }
                            }
                        } else {
                            player.sendMessage(plugin.pluginName + "You cannot change the handbrake while the TARDIS is in the time vortex!");
                        }
                    }
                }
            }
        }
    }

    private void toggleBeacon(String str, boolean on) {
        String[] beaconData = str.split(":");
        World w = plugin.getServer().getWorld(beaconData[0]);
        float bx = 0, by = 0, bz = 0;
        try {
            bx = Float.parseFloat(beaconData[1]);
            by = Float.parseFloat(beaconData[2]);
            bz = Float.parseFloat(beaconData[3]);
        } catch (NumberFormatException nfe) {
            plugin.debug("Couldn't convert to a float! " + nfe.getMessage());
        }
        Location bl = new Location(w, bx, by, bz);
        Block b = bl.getBlock();
        b.setTypeId((on) ? 20 : 7);
    }
}
