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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISComehereCommand {

    private final TARDIS plugin;

    public TARDISComehereCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public boolean doComeHere(Player player) {
        if (plugin.getConfig().getString("preferences.difficulty").equalsIgnoreCase("easy")) {
            if (player.hasPermission("tardis.timetravel")) {
                final Location eyeLocation = player.getTargetBlock(plugin.tardisCommand.transparent, 50).getLocation();
                if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && eyeLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
                    player.sendMessage(plugin.pluginName + "The server admin will not allow you to bring the TARDIS to this world!");
                    return true;
                }
                TARDISPluginRespect respect = new TARDISPluginRespect(plugin);
                if (!respect.getRespect(player, eyeLocation, true)) {
                    return true;
                }
                if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                    String areaPerm = plugin.ta.getExileArea(player);
                    if (plugin.ta.areaCheckInExile(areaPerm, eyeLocation)) {
                        player.sendMessage(plugin.pluginName + "You exile status does not allow you to bring the TARDIS to this location!");
                        return true;
                    }
                }
                if (!plugin.ta.areaCheckInExisting(eyeLocation)) {
                    player.sendMessage(plugin.pluginName + "You cannot use /tardis comehere to bring the Police Box to a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
                    return true;
                }
                Material m = player.getTargetBlock(plugin.tardisCommand.transparent, 50).getType();
                if (m != Material.SNOW) {
                    int yplusone = eyeLocation.getBlockY();
                    eyeLocation.setY(yplusone + 1);
                }
                // check the world is not excluded
                String world = eyeLocation.getWorld().getName();
                if (!plugin.getConfig().getBoolean("worlds." + world)) {
                    player.sendMessage(plugin.pluginName + "You cannot bring the TARDIS Police Box to this world");
                    return true;
                }
                // check they are a timelord
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", player.getName());
                final ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    player.sendMessage(plugin.pluginName + "You must be the Timelord of the TARDIS to use this command!");
                    return true;
                }
                final int id = rs.getTardis_id();
                // check they are not in the tardis
                HashMap<String, Object> wherettrav = new HashMap<String, Object>();
                wherettrav.put("player", player.getName());
                wherettrav.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                if (rst.resultSet()) {
                    player.sendMessage(plugin.pluginName + "You cannot bring the Police Box here because you are inside a TARDIS!");
                    return true;
                }
                if (plugin.tardisMaterialising.contains(id) || plugin.tardisDematerialising.contains(id)) {
                    player.sendMessage(plugin.pluginName + "You cannot do that while the TARDIS is materialising!");
                    return true;
                }
                int level = rs.getArtron_level();
                boolean chamtmp = false;
                if (plugin.getConfig().getBoolean("travel.chameleon")) {
                    chamtmp = rs.isChamele_on();
                }
                final boolean hidden = rs.isHidden();
                // get current police box location
                HashMap<String, Object> wherecl = new HashMap<String, Object>();
                wherecl.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                if (rsc.resultSet()) {
                    final COMPASS d = rsc.getDirection();
                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                    int count;
                    boolean sub = false;
                    Block b = eyeLocation.getBlock();
                    if (b.getRelative(BlockFace.UP).getTypeId() == 8 || b.getRelative(BlockFace.UP).getTypeId() == 9) {
                        count = (tt.isSafeSubmarine(eyeLocation, d)) ? 0 : 1;
                        if (count == 0) {
                            plugin.trackSubmarine.add(id);
                            sub = true;
                        }
                    } else {
                        if (plugin.trackSubmarine.contains(Integer.valueOf(id))) {
                            plugin.trackSubmarine.remove(Integer.valueOf(id));
                        }
                        int[] start_loc = tt.getStartLocation(eyeLocation, d);
                        // safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, COMPASS d)
                        count = tt.safeLocation(start_loc[0], eyeLocation.getBlockY(), start_loc[2], start_loc[1], start_loc[3], eyeLocation.getWorld(), d);
                    }
                    if (count > 0) {
                        player.sendMessage(plugin.pluginName + "That location would grief existing blocks! Try somewhere else!");
                        return true;
                    }
                    int ch = plugin.getArtronConfig().getInt("comehere");
                    if (level < ch) {
                        player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                        return true;
                    }
                    final Player p = player;
                    final boolean cham = chamtmp;
                    World w = rsc.getWorld();
                    if (w != null) {
                        final Location oldSave = new Location(w, rsc.getX(), rsc.getY(), rsc.getZ());
                        final QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> tid = new HashMap<String, Object>();
                        tid.put("tardis_id", id);
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("world", eyeLocation.getWorld().getName());
                        set.put("x", eyeLocation.getBlockX());
                        set.put("y", eyeLocation.getBlockY());
                        set.put("z", eyeLocation.getBlockZ());
                        set.put("submarine", (sub) ? 1 : 0);
                        if (hidden) {
                            HashMap<String, Object> sett = new HashMap<String, Object>();
                            sett.put("hidden", 0);
                            HashMap<String, Object> ttid = new HashMap<String, Object>();
                            ttid.put("tardis_id", id);
                            qf.doUpdate("tardis", sett, ttid);
                        }
                        qf.doUpdate("current", set, tid);
                        // set fast return location
                        HashMap<String, Object> bid = new HashMap<String, Object>();
                        bid.put("tardis_id", id);
                        HashMap<String, Object> bset = new HashMap<String, Object>();
                        bset.put("world", rsc.getWorld().getName());
                        bset.put("x", rsc.getX());
                        bset.put("y", rsc.getY());
                        bset.put("z", rsc.getZ());
                        bset.put("direction", rsc.getDirection().toString());
                        bset.put("submarine", rsc.isSubmarine());
                        qf.doUpdate("back", bset, bid);
                        player.sendMessage(plugin.pluginName + "The TARDIS is coming...");
                        final boolean mat = plugin.getConfig().getBoolean("police_box.materialise");
                        long delay = (mat) ? 1L : 180L;
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if (!hidden) {
                                    plugin.tardisDematerialising.add(id);
                                    plugin.destroyerP.destroyPreset(oldSave, d, id, false, mat, cham, p);
                                } else {
                                    plugin.destroyerP.removeBlockProtection(id, qf);
                                }
                            }
                        }, delay);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                plugin.builderP.buildPreset(id, eyeLocation, d, cham, p, false, false);
                            }
                        }, delay * 2);
                        // remove energy from TARDIS
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        qf.alterEnergyLevel("tardis", -ch, wheret, player);
                        plugin.tardisHasDestination.remove(id);
                        if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                            plugin.trackRescue.remove(Integer.valueOf(id));
                        }
                        return true;
                    }
                } else {
                    player.sendMessage(plugin.pluginName + "Could not get the previous location of the TARDIS!");
                    return true;
                }
            } else {
                player.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
                return false;
            }
        } else {
            player.sendMessage(plugin.pluginName + "You need to craft a Stattenheim Remote Control! Type " + ChatColor.AQUA + "/tardisrecipe remote" + ChatColor.RESET + " to see how to make it.");
            return true;
        }
        return false;
    }
}
