/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.artron.TARDISAdaptiveBoxLampToggler;
import me.eccentric_nz.TARDIS.artron.TARDISBeaconToggler;
import me.eccentric_nz.TARDIS.artron.TARDISLampToggler;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Throticle;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.sensor.PowerSensor;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.TARDIS.utility.protection.TARDISLWCChecker;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.*;
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
import org.bukkit.persistence.PersistentDataType;

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

    public TARDISStattenheimListener(TARDIS plugin) {
        this.plugin = plugin;
        // add useless blocks
        useless.add(Material.SNOW);
        useless.add(Material.MOSS_CARPET);
        useless.addAll(Tag.WOOL_CARPETS.getValues());
        useless.addAll(TARDISMaterials.plants);
        useless.addAll(Tag.SAPLINGS.getValues());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onStattenheimInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType().equals(Material.FLINT) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            int uses;
            if (im.getDisplayName().endsWith("Stattenheim Remote")) {
                UUID uuid = player.getUniqueId();
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.STATTENHEIM_REMOTE)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Stattenheim Remote");
                    return;
                }
                // check uses
                if (im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.INTEGER)) {
                    uses = im.getPersistentDataContainer().get(plugin.getCustomBlockKey(), PersistentDataType.INTEGER);
                    if (uses <= 0) {
                        // break the remote
                        player.getInventory().setItemInMainHand(null);
                        player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0f, 1.0f);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "STATTENHEIM_USED");
                        return;
                    }
                } else {
                    // add uses
                    int u = plugin.getConfig().getInt("circuits.uses.stattenheim");
                    uses = u > 0 ? u : 1000;
                    im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, uses);
                    is.setItemMeta(im);
                }
                Action action = event.getAction();
                // check they are a Time Lord
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                if (!rs.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                    return;
                }
                Tardis tardis = rs.getTardis();
                int id = tardis.getTardisId();
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                    return;
                }
                if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                    plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
                    return;
                }
                boolean power = tardis.isPoweredOn();
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    Block b = event.getClickedBlock();
                    Material m = b.getType();
                    if (b.getState() instanceof InventoryHolder || Tag.DOORS.isTagged(m)) {
                        return;
                    }
                    if (TARDISPermission.hasPermission(player, "tardis.timetravel")) {
                        Location remoteLocation = b.getLocation();
                        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && remoteLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
                            return;
                        }
                        if (!plugin.getPluginRespect().getRespect(remoteLocation, new Parameters(player, Flag.getDefaultFlags()))) {
                            return;
                        }
                        if (TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                            String areaPerm = plugin.getTardisArea().getExileArea(player);
                            if (plugin.getTardisArea().areaCheckInExile(areaPerm, remoteLocation)) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "EXILE_NO_TRAVEL");
                                return;
                            }
                        }
                        if (plugin.getTardisArea().isInExistingArea(remoteLocation)) {
                            plugin.getMessenger().sendColouredCommand(player, "AREA_NO_STAT", "/tardistravel area [area name]", plugin);
                            return;
                        }
                        if (!useless.contains(m)) {
                            int yplusone = remoteLocation.getBlockY();
                            remoteLocation.setY(yplusone + 1);
                        }
                        // check the world is not excluded
                        String world = remoteLocation.getWorld().getName();
                        if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".time_travel")) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PB_IN_WORLD");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !power) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                            return;
                        }
                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                        tcc.getCircuits();
                        if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true) && !tcc.hasMaterialisation()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MAT_CIRCUIT");
                            return;
                        }
                        // damage circuit if configured
                        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                            // decrement uses
                            int uses_left = tcc.getMaterialisationUses();
                            new TARDISCircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player).damage();
                        }
                        // decrement uses
                        int decremented = uses - 1;
                        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, decremented);
                        // set the lore
                        String coloured = ChatColor.YELLOW + "" + decremented;
                        List<String> lore = List.of("Right-click block", "to call TARDIS", "Uses left", coloured);
                        im.setLore(lore);
                        is.setItemMeta(im);
                        boolean hidden = tardis.isHidden();
                        int level = tardis.getArtronLevel();
                        // check they are not in the tardis
                        HashMap<String, Object> wherettrav = new HashMap<>();
                        wherettrav.put("uuid", uuid.toString());
                        wherettrav.put("tardis_id", id);
                        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                        if (rst.resultSet()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PB_IN_TARDIS");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                            return;
                        }
                        // get TARDIS's current location
                        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
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
                        if (plugin.getPM().isPluginEnabled("BlockLocker") && (BlockLockerAPIv2.isProtected(remoteLocation.getBlock()) || BlockLockerAPIv2.isProtected(under))) {
                            count = 1;
                        }
                        if (plugin.getPM().isPluginEnabled("LWC") && new TARDISLWCChecker().isBlockProtected(remoteLocation.getBlock(), under, player)) {
                            count = 1;
                        }
                        if (count > 0) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "WOULD_GRIEF_BLOCKS");
                            return;
                        }
                        Throticle throticle = new ResultSetThrottle(plugin).getSpeedAndParticles(uuid.toString());
                        int ch = Math.round(plugin.getArtronConfig().getInt("comehere") * throticle.getThrottle().getArtronMultiplier());
                        if (level < ch) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
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
                        plugin.getMessenger().sendStatus(player, "TARDIS_COMING");
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
                            dd.setThrottle(throticle.getThrottle());
                            dd.setParticles(throticle.getParticles());
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
                        bd.setThrottle(throticle.getThrottle());
                        bd.setParticles(throticle.getParticles());
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), delay * 2);
                        // remove energy from TARDIS
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        plugin.getQueryFactory().alterEnergyLevel("tardis", -ch, wheret, player);
                        plugin.getTrackerKeeper().getHasDestination().remove(id);
                        plugin.getTrackerKeeper().getRescue().remove(id);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
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
                        ChameleonPreset preset = tardis.getPreset();
                        HashMap<String, Object> wherep = new HashMap<>();
                        wherep.put("tardis_id", id);
                        HashMap<String, Object> setp = new HashMap<>();
                        setp.put("powered_on", 1);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_ON");
                        // if lights are off, turn them on
                        if (tardis.isLightsOn()) {
                            new TARDISLampToggler(plugin).flickSwitch(id, uuid, false, tardis.getSchematic().getLights());
                        }
                        // if beacon is off turn it on
                        if (beacon_on) {
                            new TARDISBeaconToggler(plugin).flickSwitch(uuid, id, true);
                        }
                        // police box lamp
                        if (preset.equals(ChameleonPreset.ADAPTIVE) || preset.usesArmourStand()) {
                            new TARDISAdaptiveBoxLampToggler(plugin).toggleLamp(id, true, preset);
                        }
                        plugin.getQueryFactory().doUpdate("tardis", setp, wherep);
                        // toggle power sensor
                        new PowerSensor(plugin, id).toggle();
                    }
                }
            }
        }
    }
}
