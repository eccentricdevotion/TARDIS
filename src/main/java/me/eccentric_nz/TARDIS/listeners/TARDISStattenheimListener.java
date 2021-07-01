/*
 * Copyright (C) 2021 eccentric_nz
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

import com.griefcraft.cache.ProtectionCache;
import com.griefcraft.lwc.LWC;
import com.griefcraft.model.Protection;
import me.crafter.mc.lockettepro.LocketteProAPI;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.artron.TARDISAdaptiveBoxLampToggler;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The handheld Recall Button on the TARDIS Stattenheim remote broadcasts a Stattenheim signal through the Vortex, which
 * summons the operator's TARDIS when the operator is in the field.
 *
 * @author eccentric_nz
 */
public class TARDISStattenheimListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> useless = new ArrayList<>();
    private final Material remote;

    public TARDISStattenheimListener(TARDIS plugin) {
        this.plugin = plugin;
        // add useless blocks
        useless.add(Material.SNOW);
        useless.addAll(TARDISMaterials.carpet);
        useless.addAll(TARDISMaterials.plants);
        useless.addAll(TARDISMaterials.saplings);
        remote = Material.valueOf(plugin.getRecipesConfig().getString("shaped.Stattenheim Remote.result"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onStattenheimInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType().equals(remote) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.getDisplayName().equals("Stattenheim Remote")) {
                Action action = event.getAction();
                // check they are a Time Lord
                UUID uuid = player.getUniqueId();
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                if (!rs.resultSet()) {
                    TARDISMessage.send(player, "NO_TARDIS");
                    return;
                }
                Tardis tardis = rs.getTardis();
                int id = tardis.getTardis_id();
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                    return;
                }
                if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                    TARDISMessage.send(player.getPlayer(), "NOT_WHILE_DISPERSED");
                    return;
                }
                boolean power = tardis.isPowered_on();
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    Block b = event.getClickedBlock();
                    Material m = b.getType();
                    if (b.getState() instanceof InventoryHolder || Tag.DOORS.isTagged(m)) {
                        return;
                    }
                    if (TARDISPermission.hasPermission(player, "tardis.timetravel")) {
                        Location remoteLocation = b.getLocation();
                        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && remoteLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
                            TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                            return;
                        }
                        if (!plugin.getPluginRespect().getRespect(remoteLocation, new Parameters(player, Flag.getDefaultFlags()))) {
                            return;
                        }
                        if (TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
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
                        if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".time_travel")) {
                            TARDISMessage.send(player, "NO_PB_IN_WORLD");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !power) {
                            TARDISMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        TARDISCircuitChecker tcc = null;
                        if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
                            tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                        }
                        if (tcc != null && !tcc.hasMaterialisation()) {
                            TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                            return;
                        }
                        boolean hidden = tardis.isHidden();
                        int level = tardis.getArtron_level();
                        // check they are not in the tardis
                        HashMap<String, Object> wherettrav = new HashMap<>();
                        wherettrav.put("uuid", uuid.toString());
                        wherettrav.put("tardis_id", id);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                        if (rst.resultSet()) {
                            TARDISMessage.send(player, "NO_PB_IN_TARDIS");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                            TARDISMessage.send(player, "NOT_WHILE_MAT");
                            return;
                        }
                        // get TARDIS's current location
                        HashMap<String, Object> wherecl = new HashMap<>();
                        wherecl.put("tardis_id", tardis.getTardis_id());
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                        if (!rsc.resultSet()) {
                            hidden = true;
                        }
                        COMPASS d = rsc.getDirection();
                        COMPASS player_d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                        TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                        int count;
                        boolean sub = false;
                        if (b.getRelative(BlockFace.UP).getType().equals(Material.WATER)) {
                            count = (tt.isSafeSubmarine(remoteLocation, player_d)) ? 0 : 1;
                            if (count == 0) {
                                sub = true;
                            }
                        } else {
                            int[] start_loc = TARDISTimeTravel.getStartLocation(remoteLocation, player_d);
                            // safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, COMPASS player_d)
                            count = TARDISTimeTravel.safeLocation(start_loc[0], remoteLocation.getBlockY(), start_loc[2], start_loc[1], start_loc[3], remoteLocation.getWorld(), player_d);
                        }
                        Block under = remoteLocation.getBlock().getRelative(BlockFace.DOWN);
                        if (plugin.getPM().isPluginEnabled("LockettePro")) {
                            if (LocketteProAPI.isProtected(remoteLocation.getBlock()) || LocketteProAPI.isProtected(under) || plugin.getUtils().checkSurrounding(under)) {
                                count = 1;
                            }
                        }
                        if (plugin.getPM().isPluginEnabled("BlockLocker")) {
                            if (BlockLockerAPIv2.isProtected(remoteLocation.getBlock()) || BlockLockerAPIv2.isProtected(under)) {
                                count = 1;
                            }
                        }
                        if (plugin.getPM().isPluginEnabled("LWC")) {
                            ProtectionCache protectionCache = LWC.getInstance().getProtectionCache();
                            if (protectionCache != null) {
                                Protection protection = protectionCache.getProtection(remoteLocation.getBlock());
                                Protection underProtection = protectionCache.getProtection(under);
                                if (protection != null && !protection.isOwner(player) || underProtection != null && !underProtection.isOwner(player)) {
                                    count = 1;
                                }
                            }
                        }
                        if (count > 0) {
                            TARDISMessage.send(player, "WOULD_GRIEF_BLOCKS");
                            return;
                        }
                        SpaceTimeThrottle spaceTimeThrottle = new ResultSetThrottle(plugin).getSpeed(uuid.toString());
                        int ch = Math.round(plugin.getArtronConfig().getInt("comehere") * spaceTimeThrottle.getArtronMultiplier());
                        if (level < ch) {
                            TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
                            return;
                        }
                        Location oldSave = null;
                        HashMap<String, Object> bid = new HashMap<>();
                        bid.put("tardis_id", id);
                        HashMap<String, Object> bset = new HashMap<>();
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
                            hidden = true;
                            // set fast return location
                            bset.put("world", remoteLocation.getWorld().getName());
                            bset.put("x", remoteLocation.getX());
                            bset.put("y", remoteLocation.getY());
                            bset.put("z", remoteLocation.getZ());
                            bset.put("submarine", (sub) ? 1 : 0);
                        }
                        plugin.getQueryFactory().doUpdate("back", bset, bid);
                        // set current location
                        HashMap<String, Object> cid = new HashMap<>();
                        cid.put("tardis_id", id);
                        HashMap<String, Object> cset = new HashMap<>();
                        cset.put("world", remoteLocation.getWorld().getName());
                        cset.put("x", remoteLocation.getBlockX());
                        cset.put("y", remoteLocation.getBlockY());
                        cset.put("z", remoteLocation.getBlockZ());
                        cset.put("direction", player_d.toString());
                        cset.put("submarine", (sub) ? 1 : 0);
                        plugin.getQueryFactory().doUpdate("current", cset, cid);
                        // update tardis
                        if (hidden) {
                            HashMap<String, Object> tid = new HashMap<>();
                            HashMap<String, Object> set = new HashMap<>();
                            set.put("hidden", 0);
                            tid.put("tardis_id", id);
                            plugin.getQueryFactory().doUpdate("tardis", set, tid);
                        }
                        TARDISMessage.send(player, "TARDIS_COMING");
                        long delay = 10L;
                        plugin.getTrackerKeeper().getInVortex().add(id);
                        boolean hid = hidden;
                        if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            DestroyData dd = new DestroyData();
                            dd.setDirection(d);
                            dd.setLocation(oldSave);
                            dd.setPlayer(player);
                            dd.setHide(false);
                            dd.setOutside(true);
                            dd.setSubmarine(rsc.isSubmarine());
                            dd.setTardisID(id);
                            dd.setThrottle(spaceTimeThrottle);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                if (!hid) {
                                    plugin.getTrackerKeeper().getDematerialising().add(id);
                                    plugin.getPresetDestroyer().destroyPreset(dd);
                                } else {
                                    plugin.getPresetDestroyer().removeBlockProtection(id);
                                }
                            }, delay);
                        }
                        BuildData bd = new BuildData(uuid.toString());
                        bd.setDirection(player_d);
                        bd.setLocation(remoteLocation);
                        bd.setMalfunction(false);
                        bd.setOutside(true);
                        bd.setPlayer(player);
                        bd.setRebuild(false);
                        bd.setSubmarine(sub);
                        bd.setTardisID(id);
                        bd.setThrottle(spaceTimeThrottle);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), delay * 2);
                        // remove energy from TARDIS
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        plugin.getQueryFactory().alterEnergyLevel("tardis", -ch, wheret, player);
                        plugin.getTrackerKeeper().getHasDestination().remove(id);
                        plugin.getTrackerKeeper().getRescue().remove(id);
                    } else {
                        TARDISMessage.send(player, "NO_PERMS");
                    }
                } else if (action.equals(Action.RIGHT_CLICK_AIR) && plugin.getConfig().getBoolean("allow.power_down")) {
                    // is the power off?
                    if (!power) {
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                        boolean beacon_on = true;
                        if (rsp.resultSet()) {
                            beacon_on = rsp.isBeaconOn();
                        }
                        // power up
                        PRESET preset = tardis.getPreset();
                        HashMap<String, Object> wherep = new HashMap<>();
                        wherep.put("tardis_id", id);
                        HashMap<String, Object> setp = new HashMap<>();
                        setp.put("powered_on", 1);
                        TARDISMessage.send(player, "POWER_ON");
                        // if lights are off, turn them on
                        if (tardis.isLights_on()) {
                            new TARDISLampToggler(plugin).flickSwitch(id, uuid, false, tardis.getSchematic().hasLanterns());
                        }
                        // if beacon is off turn it on
                        if (beacon_on) {
                            new TARDISBeaconToggler(plugin).flickSwitch(uuid, id, true);
                        }
                        // police box lamp
                        if (preset.equals(PRESET.ADAPTIVE) || preset.usesItemFrame()) {
                            new TARDISAdaptiveBoxLampToggler(plugin).toggleLamp(id, true, preset);
                        }
                        plugin.getQueryFactory().doUpdate("tardis", setp, wherep);
                    }
                }
            }
        }
    }
}
