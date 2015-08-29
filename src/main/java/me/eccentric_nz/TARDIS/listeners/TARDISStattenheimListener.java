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
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.artron.TARDISPoliceBoxLampToggler;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
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
import org.yi.acru.bukkit.Lockette.Lockette;

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
    Material remote;

    public TARDISStattenheimListener(TARDIS plugin) {
        this.plugin = plugin;
        // add useless blocks
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
            if (im.getDisplayName().equals("Stattenheim Remote")) {
                Action action = event.getAction();
                // check they are a Time Lord
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    TARDISMessage.send(player, "NO_TARDIS");
                    return;
                }
                final int id = rs.getTardis_id();
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                    return;
                }
                boolean power = rs.isPowered_on();
                final QueryFactory qf = new QueryFactory(plugin);
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    Block b = event.getClickedBlock();
                    Material m = b.getType();
                    if (b.getState() instanceof InventoryHolder || plugin.getGeneralKeeper().getDoors().contains(m)) {
                        return;
                    }
                    if (player.hasPermission("tardis.timetravel")) {
                        Location remoteLocation = b.getLocation();
                        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && remoteLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
                            TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                            return;
                        }
                        if (!plugin.getPluginRespect().getRespect(remoteLocation, new Parameters(player, FLAG.getDefaultFlags()))) {
                            return;
                        }
                        if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                            String areaPerm = plugin.getTardisArea().getExileArea(player);
                            if (plugin.getTardisArea().areaCheckInExile(areaPerm, remoteLocation)) {
                                TARDISMessage.send(player, "EXILE_NO_TRAVEL");
                                return;
                            }
                        }
                        if (!plugin.getTardisArea().areaCheckInExisting(remoteLocation)) {
                            TARDISMessage.send(player, "AREA_NO_STAT", ChatColor.AQUA + "/tardistravel area [area name]");
                            return;
                        }
                        if (!useless.contains(m)) {
                            int yplusone = remoteLocation.getBlockY();
                            remoteLocation.setY(yplusone + 1);
                        }
                        // check the world is not excluded
                        String world = remoteLocation.getWorld().getName();
                        if (!plugin.getConfig().getBoolean("worlds." + world)) {
                            TARDISMessage.send(player, "NO_PB_IN_WORLD");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !power) {
                            TARDISMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        TARDISCircuitChecker tcc = null;
                        if (plugin.getConfig().getString("preferences.difficulty").equals("hard") && !plugin.getUtils().inGracePeriod(player, true)) {
                            tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                        }
                        if (tcc != null && !tcc.hasMaterialisation()) {
                            TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                            return;
                        }
                        boolean hidden = rs.isHidden();
                        int level = rs.getArtron_level();
                        boolean cham = (plugin.getConfig().getBoolean("travel.chameleon") && rs.isChamele_on());
                        // check they are not in the tardis
                        HashMap<String, Object> wherettrav = new HashMap<String, Object>();
                        wherettrav.put("uuid", player.getUniqueId().toString());
                        wherettrav.put("tardis_id", id);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                        if (rst.resultSet()) {
                            TARDISMessage.send(player, "NO_PB_IN_TARDIS");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInVortex().contains(id)) {
                            TARDISMessage.send(player, "NOT_WHILE_MAT");
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
                        COMPASS player_d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                        TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                        int count;
                        boolean sub = false;
                        if (b.getRelative(BlockFace.UP).getType().equals(Material.WATER) || b.getRelative(BlockFace.UP).getType().equals(Material.STATIONARY_WATER)) {
                            count = (tt.isSafeSubmarine(remoteLocation, player_d)) ? 0 : 1;
                            if (count == 0) {
                                sub = true;
                            }
                        } else {
                            int[] start_loc = tt.getStartLocation(remoteLocation, player_d);
                            // safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, COMPASS player_d)
                            count = tt.safeLocation(start_loc[0], remoteLocation.getBlockY(), start_loc[2], start_loc[1], start_loc[3], remoteLocation.getWorld(), player_d);
                        }
                        if (plugin.getPM().isPluginEnabled("Lockette")) {
                            Lockette Lockette = (Lockette) plugin.getPM().getPlugin("Lockette");
                            if (Lockette.isProtected(remoteLocation.getBlock())) {
                                count = 1;
                            }
                        }
                        if (count > 0) {
                            TARDISMessage.send(player, "WOULD_GRIEF_BLOCKS");
                            return;
                        }
                        int ch = plugin.getArtronConfig().getInt("comehere");
                        if (level < ch) {
                            TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
                            return;
                        }
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
                            bset.put("direction", d.toString());
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
                        cset.put("direction", player_d.toString());
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
                        TARDISMessage.send(player, "TARDIS_COMING");
                        boolean mat = plugin.getConfig().getBoolean("police_box.materialise");
                        long delay = (mat) ? 10L : 180L;
                        plugin.getTrackerKeeper().getInVortex().add(id);
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
                        pdd.setBiome(rsc.getBiome());
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if (!hid) {
                                    plugin.getTrackerKeeper().getDematerialising().add(id);
                                    plugin.getPresetDestroyer().destroyPreset(pdd);
                                } else {
                                    plugin.getPresetDestroyer().removeBlockProtection(id, qf);
                                }
                            }
                        }, delay);
                        final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
                        pbd.setChameleon(cham);
                        pbd.setDirection(player_d);
                        pbd.setLocation(remoteLocation);
                        pbd.setMalfunction(false);
                        pbd.setOutside(true);
                        pbd.setPlayer(player);
                        pbd.setRebuild(false);
                        pbd.setSubmarine(sub);
                        pbd.setTardisID(id);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                plugin.getPresetBuilder().buildPreset(pbd);
                            }
                        }, delay * 2);
                        // remove energy from TARDIS
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        qf.alterEnergyLevel("tardis", -ch, wheret, player);
                        plugin.getTrackerKeeper().getHasDestination().remove(id);
                        if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
                            plugin.getTrackerKeeper().getRescue().remove(id);
                        }
                    } else {
                        TARDISMessage.send(player, "NO_PERMS");
                    }
                } else if (action.equals(Action.RIGHT_CLICK_AIR) && plugin.getConfig().getBoolean("allow.power_down")) {
                    // is the power off?
                    if (!power) {
                        HashMap<String, Object> wherek = new HashMap<String, Object>();
                        wherek.put("uuid", player.getUniqueId().toString());
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
                        boolean beacon_on = true;
                        if (rsp.resultSet()) {
                            beacon_on = rsp.isBeaconOn();
                        }
                        // power up
                        PRESET preset = rs.getPreset();
                        HashMap<String, Object> wherep = new HashMap<String, Object>();
                        wherep.put("tardis_id", id);
                        HashMap<String, Object> setp = new HashMap<String, Object>();
                        setp.put("powered_on", 1);
                        TARDISMessage.send(player, "POWER_ON");
                        // if lights are off, turn them on
                        if (rs.isLights_on()) {
                            new TARDISLampToggler(plugin).flickSwitch(id, player.getUniqueId(), false, rs.getSchematic().hasLanterns());
                        }
                        // if beacon is off turn it on
                        if (beacon_on) {
                            new TARDISBeaconToggler(plugin).flickSwitch(player.getUniqueId(), true);
                        }
                        // police box lamp
                        if (preset.equals(PRESET.NEW) || preset.equals(PRESET.OLD)) {
                            new TARDISPoliceBoxLampToggler(plugin).toggleLamp(id, true);
                        }
                        qf.doUpdate("tardis", setp, wherep);
                    }
                }
            }
        }
    }
}
