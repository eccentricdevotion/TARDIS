/*
 * Copyright (C) 2014 eccentric_nz
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
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
        doors.add(Material.IRON_DOOR_BLOCK);
        doors.add(Material.WOODEN_DOOR);
        useless.add(Material.BROWN_MUSHROOM);
        useless.add(Material.CARPET);
        useless.add(Material.DEAD_BUSH);
        useless.add(Material.LONG_GRASS);
        useless.add(Material.RED_MUSHROOM);
        useless.add(Material.RED_ROSE);
        useless.add(Material.SAPLING);
        useless.add(Material.SNOW);
        useless.add(Material.YELLOW_FLOWER);
        remote = Material.valueOf(plugin.getRecipesConfig().getString("shaped.Stattenheim Remote.result"));
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onStattenheimInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack is = player.getItemInHand();
        if (is.getType().equals(remote) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.getDisplayName().equals("Stattenheim Remote") && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Block b = event.getClickedBlock();
                Material m = b.getType();
                if (b.getState() instanceof InventoryHolder || doors.contains(m)) {
                    return;
                }
                if (player.hasPermission("tardis.timetravel")) {
                    Location remoteLocation = b.getLocation();
                    if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && remoteLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
                        TARDISMessage.send(player, plugin.getPluginName() + "The server admin will not allow you to bring the TARDIS to this world!");
                        return;
                    }
                    if (!plugin.getPluginRespect().getRespect(player, remoteLocation, true)) {
                        return;
                    }
                    if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                        String areaPerm = plugin.getTardisArea().getExileArea(player);
                        if (plugin.getTardisArea().areaCheckInExile(areaPerm, remoteLocation)) {
                            TARDISMessage.send(player, plugin.getPluginName() + "You exile status does not allow you to bring the TARDIS to this location!");
                            return;
                        }
                    }
                    if (!plugin.getTardisArea().areaCheckInExisting(remoteLocation)) {
                        TARDISMessage.send(player, plugin.getPluginName() + "You cannot use /tardis comehere to bring the Police Box to a TARDIS area! Please use " + ChatColor.AQUA + " /tardistravel area [area name]");
                        return;
                    }
                    if (!useless.contains(m)) {
                        int yplusone = remoteLocation.getBlockY();
                        remoteLocation.setY(yplusone + 1);
                    }
                    // check the world is not excluded
                    String world = remoteLocation.getWorld().getName();
                    if (!plugin.getConfig().getBoolean("worlds." + world)) {
                        TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PB_IN_WORLD.getText());
                        return;
                    }
                    // check they are a timelord
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("owner", player.getName());
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (!rs.resultSet()) {
                        TARDISMessage.send(player, plugin.getPluginName() + "You don't have a TARDIS!");
                        return;
                    }
                    final int id = rs.getTardis_id();
                    TARDISCircuitChecker tcc = null;
                    if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                        tcc = new TARDISCircuitChecker(plugin, id);
                        tcc.getCircuits();
                    }
                    if (tcc != null && !tcc.hasMaterialisation()) {
                        TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_MAT_CIRCUIT.getText());
                        return;
                    }
                    boolean hidden = rs.isHidden();
                    int level = rs.getArtron_level();
                    boolean cham = (plugin.getConfig().getBoolean("travel.chameleon") && rs.isChamele_on());
                    // check they are not in the tardis
                    HashMap<String, Object> wherettrav = new HashMap<String, Object>();
                    wherettrav.put("player", player.getName());
                    wherettrav.put("tardis_id", id);
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                    if (rst.resultSet()) {
                        TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PB_IN_TARDIS.getText());
                        return;
                    }
                    if (plugin.getTrackerKeeper().getTrackInVortex().contains(Integer.valueOf(id))) {
                        TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NOT_WHILE_MAT.getText());
                        return;
                    }
                    // get TARDIS's current location
                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                    wherecl.put("tardis_id", rs.getTardis_id());
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        hidden = true;
                    }
                    COMPASS d = rsc.getDirection();
                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                    int count;
                    boolean sub = false;
                    if (b.getRelative(BlockFace.UP).getTypeId() == 8 || b.getRelative(BlockFace.UP).getTypeId() == 9) {
                        count = (tt.isSafeSubmarine(remoteLocation, d)) ? 0 : 1;
                        if (count == 0) {
                            sub = true;
                        }
                    } else {
                        int[] start_loc = tt.getStartLocation(remoteLocation, d);
                        // safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, COMPASS d)
                        count = tt.safeLocation(start_loc[0], remoteLocation.getBlockY(), start_loc[2], start_loc[1], start_loc[3], remoteLocation.getWorld(), d);
                    }
                    if (count > 0) {
                        TARDISMessage.send(player, plugin.getPluginName() + "That location would grief existing blocks! Try somewhere else!");
                        return;
                    }
                    int ch = plugin.getArtronConfig().getInt("comehere");
                    if (level < ch) {
                        TARDISMessage.send(player, plugin.getPluginName() + ChatColor.RED + MESSAGE.NOT_ENOUGH_ENERGY.getText());
                        return;
                    }
                    final QueryFactory qf = new QueryFactory(plugin);
                    Location oldSave = null;
                    HashMap<String, Object> bid = new HashMap<String, Object>();
                    bid.put("tardis_id", id);
                    HashMap<String, Object> bset = new HashMap<String, Object>();
                    if (rsc.getWorld() != null) {
                        oldSave = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                        // set fast return location
                        bset.put("world", rsc.getWorld().getName());
                        bset.put("x", rsc.getX());
                        bset.put("y", rsc.getY());
                        bset.put("z", rsc.getZ());
                        bset.put("direction", rsc.getDirection().toString());
                        bset.put("submarine", rsc.isSubmarine());
                    } else {
                        // set fast return location
                        bset.put("world", remoteLocation.getWorld().getName());
                        bset.put("x", remoteLocation.getX());
                        bset.put("y", remoteLocation.getY());
                        bset.put("z", remoteLocation.getZ());
                        bset.put("submarine", (sub) ? 1 : 0);
                    }
                    qf.doUpdate("back", bset, bid);

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
                    // update tardis
                    if (hidden) {
                        HashMap<String, Object> tid = new HashMap<String, Object>();
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("hidden", 0);
                        tid.put("tardis_id", id);
                        qf.doUpdate("tardis", set, tid);
                    }
                    TARDISMessage.send(player, plugin.getPluginName() + "The TARDIS is coming...");
                    boolean mat = plugin.getConfig().getBoolean("police_box.materialise");
                    long delay = (mat) ? 10L : 180L;
                    plugin.getTrackerKeeper().getTrackInVortex().add(Integer.valueOf(id));
                    final boolean hid = hidden;
                    final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
                    pdd.setChameleon(cham);
                    pdd.setDirection(d);
                    pdd.setLocation(oldSave);
                    pdd.setDematerialise(mat);
                    pdd.setPlayer(player);
                    pdd.setHide(false);
                    pdd.setOutside(true);
                    pdd.setSubmarine(rsc.isSubmarine());
                    pdd.setTardisID(id);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if (!hid) {
                                plugin.getTrackerKeeper().getTrackDematerialising().add(Integer.valueOf(id));
                                plugin.getPresetDestroyer().destroyPreset(pdd);
                            } else {
                                plugin.getPresetDestroyer().removeBlockProtection(id, qf);
                            }
                        }
                    }, delay);
                    final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
                    pbd.setChameleon(cham);
                    pbd.setDirection(d);
                    pbd.setLocation(remoteLocation);
                    pbd.setMalfunction(false);
                    pbd.setOutside(true);
                    pbd.setPlayer(player);
                    pbd.setRebuild(false);
                    pbd.setSubmarine(sub);
                    pbd.setTardisID(id);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.getPresetBuilder().buildPreset(pbd);
                        }
                    }, delay * 2);
                    // remove energy from TARDIS
                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                    wheret.put("tardis_id", id);
                    qf.alterEnergyLevel("tardis", -ch, wheret, player);
                    plugin.getTrackerKeeper().getTrackHasDestination().remove(id);
                    if (plugin.getTrackerKeeper().getTrackRescue().containsKey(Integer.valueOf(id))) {
                        plugin.getTrackerKeeper().getTrackRescue().remove(Integer.valueOf(id));
                    }
                } else {
                    TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
                }
            }
        }
    }
}
