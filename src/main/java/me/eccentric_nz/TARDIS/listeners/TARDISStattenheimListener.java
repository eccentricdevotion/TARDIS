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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The handheld Recall Button on the TARDIS Stattenheim remote broadcasts a
 * Stattenheim signal through the Vortex, which summons the operator's TARDIS
 * when the operator is in the field.
 *
 * @author eccentric_nz
 */
public class TARDISStattenheimListener implements Listener {

    private final TARDIS plugin;
    List<Material> useless = new ArrayList<Material>();
    List<Material> doors = new ArrayList<Material>();
    Material remote;

    public TARDISStattenheimListener(TARDIS plugin) {
        this.plugin = plugin;
        // add useless blocks
        doors.add(Material.WOODEN_DOOR);
        doors.add(Material.IRON_DOOR_BLOCK);
        useless.add(Material.DEAD_BUSH);
        useless.add(Material.LONG_GRASS);
        useless.add(Material.SNOW);
        useless.add(Material.SAPLING);
        useless.add(Material.RED_ROSE);
        useless.add(Material.YELLOW_FLOWER);
        useless.add(Material.RED_MUSHROOM);
        useless.add(Material.BROWN_MUSHROOM);
        if (plugin.bukkitversion.compareTo(plugin.precarpetversion) >= 0) {
            useless.add(Material.CARPET);
        }
        remote = Material.valueOf(plugin.getConfig().getString("stattenheim"));
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onStattenheimInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        ItemStack is = player.getItemInHand();
        if (is.getType().equals(remote) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.getDisplayName().equals("Stattenheim Remote") && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                final Block b = event.getClickedBlock();
                Material m = b.getType();
                if (b.getState() instanceof InventoryHolder || doors.contains(m)) {
                    return;
                }
                if (player.hasPermission("tardis.timetravel")) {
                    final Location remoteLocation = b.getLocation();
                    if (!plugin.getConfig().getBoolean("include_default_world") && plugin.getConfig().getBoolean("default_world") && remoteLocation.getWorld().getName().equals(plugin.getConfig().getString("default_world_name"))) {
                        player.sendMessage(plugin.pluginName + "The server admin will not allow you to bring the TARDIS to this world!");
                        return;
                    }
                    TARDISPluginRespect respect = new TARDISPluginRespect(plugin);
                    if (!respect.getRespect(player, remoteLocation, true)) {
                        return;
                    }
                    if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("exile")) {
                        String areaPerm = plugin.ta.getExileArea(player);
                        if (plugin.ta.areaCheckInExile(areaPerm, remoteLocation)) {
                            player.sendMessage(plugin.pluginName + "You exile status does not allow you to bring the TARDIS to this location!");
                            return;
                        }
                    }
                    if (!plugin.ta.areaCheckInExisting(remoteLocation)) {
                        player.sendMessage(plugin.pluginName + "You cannot use /tardis comehere to bring the Police Box to a TARDIS area! Please use " + ChatColor.AQUA + "/tardistravel area [area name]");
                        return;
                    }
                    if (!useless.contains(m)) {
                        int yplusone = remoteLocation.getBlockY();
                        remoteLocation.setY(yplusone + 1);
                    }
                    // check the world is not excluded
                    String world = remoteLocation.getWorld().getName();
                    if (!plugin.getConfig().getBoolean("worlds." + world)) {
                        player.sendMessage(plugin.pluginName + "You cannot bring the TARDIS Police Box to this world");
                        return;
                    }
                    // check they are a timelord
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("owner", player.getName());
                    final ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (!rs.resultSet()) {
                        player.sendMessage(plugin.pluginName + "You don't have a TARDIS!");
                        return;
                    }
                    final int id = rs.getTardis_id();
                    final boolean hidden = rs.isHidden();
                    int level = rs.getArtron_level();
                    final boolean cham = (plugin.getConfig().getBoolean("chameleon") && rs.isChamele_on());
                    // check they are not in the tardis
                    HashMap<String, Object> wherettrav = new HashMap<String, Object>();
                    wherettrav.put("player", player.getName());
                    wherettrav.put("tardis_id", id);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                    if (rst.resultSet()) {
                        player.sendMessage(plugin.pluginName + "You cannot bring the Police Box here because you are inside a TARDIS!");
                        return;
                    }
                    if (plugin.tardisMaterialising.contains(id) || plugin.tardisDematerialising.contains(id)) {
                        player.sendMessage(plugin.pluginName + "You cannot do that while the TARDIS is materialising!");
                        return;
                    }
                    // get TARDIS's current location
                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                    wherecl.put("tardis_id", rs.getTardis_id());
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        player.sendMessage(plugin.pluginName + "Could not get current TARDIS location!");
                        return;
                    }
                    final TARDISConstants.COMPASS d = rsc.getDirection();
                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                    int count;
                    boolean sub = false;
                    if (plugin.trackSubmarine.contains(Integer.valueOf(id))) {
                        plugin.trackSubmarine.remove(Integer.valueOf(id));
                    }
                    if (b.getRelative(BlockFace.UP).getTypeId() == 8 || b.getRelative(BlockFace.UP).getTypeId() == 9) {
                        count = (tt.isSafeSubmarine(remoteLocation, d)) ? 0 : 1;
                        if (count == 0) {
                            plugin.trackSubmarine.add(id);
                            sub = true;
                        }
                    } else {
                        int[] start_loc = tt.getStartLocation(remoteLocation, d);
                        // safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, TARDISConstants.COMPASS d)
                        count = tt.safeLocation(start_loc[0], remoteLocation.getBlockY(), start_loc[2], start_loc[1], start_loc[3], remoteLocation.getWorld(), d);
                    }
                    if (count > 0) {
                        player.sendMessage(plugin.pluginName + "That location would grief existing blocks! Try somewhere else!");
                        return;
                    }
                    int ch = plugin.getArtronConfig().getInt("comehere");
                    if (level < ch) {
                        player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                        return;
                    }
                    final Player p = player;
                    if (rsc.getWorld() != null) {
                        final Location oldSave = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                        final QueryFactory qf = new QueryFactory(plugin);
                        // set current location
                        HashMap<String, Object> cid = new HashMap<String, Object>();
                        cid.put("tardis_id", id);
                        HashMap<String, Object> cset = new HashMap<String, Object>();
                        cset.put("world", remoteLocation.getWorld().getName());
                        cset.put("x", remoteLocation.getBlockX());
                        cset.put("y", remoteLocation.getBlockY());
                        cset.put("z", remoteLocation.getBlockZ());
                        cset.put("submarine", (sub) ? 1 : 0);
                        qf.doUpdate("current", cset, cid);
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
                        // update tardis
                        if (hidden) {
                            HashMap<String, Object> tid = new HashMap<String, Object>();
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            set.put("hidden", 0);
                            tid.put("tardis_id", id);
                            qf.doUpdate("tardis", set, tid);
                        }
                        player.sendMessage(plugin.pluginName + "The TARDIS is coming...");
                        final boolean mat = plugin.getConfig().getBoolean("materialise");
                        long delay = (mat) ? 10L : 180L;
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
                                plugin.builderP.buildPreset(id, remoteLocation, d, cham, p, false, false);
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
                    } else {
                        player.sendMessage(plugin.pluginName + "Could not get the previous location of the TARDIS!");
                    }
                } else {
                    player.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                }
            }
        }
    }
}
