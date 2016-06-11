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
package me.eccentric_nz.TARDIS.flight;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.artron.TARDISArtronIndicator;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetJunk;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.material.Lever;

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
    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (blockType == Material.LEVER) {
                final UUID uuid = player.getUniqueId();
                HashMap<String, Object> whereu = new HashMap<String, Object>();
                whereu.put("uuid", uuid.toString());
                ResultSetTravellers rsv = new ResultSetTravellers(plugin, whereu, false);
                boolean inside = rsv.resultSet();
                // check handbrake location against the database.
                final Location handbrake_loc = block.getLocation();
                HashMap<String, Object> where = new HashMap<String, Object>();
                boolean found = false;
                int tmp_id = -1;
                if (inside) {
                    where.put("type", 0);
                    where.put("location", handbrake_loc.toString());
                    ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                    if (rsc.resultSet()) {
                        found = true;
                        tmp_id = rsc.getTardis_id();
                    }
                } else {
                    where.put("uuid", uuid.toString());
                    where.put("handbrake", handbrake_loc.toString());
                    ResultSetJunk rsj = new ResultSetJunk(plugin, where);
                    if (rsj.resultSet()) {
                        found = true;
                        tmp_id = rsj.getTardis_id();
                    }
                }
                if (found) {
                    event.setCancelled(true);
                    final int id = tmp_id;
                    TARDISCircuitChecker tcc = null;
                    if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
                        tcc = new TARDISCircuitChecker(plugin, id);
                        tcc.getCircuits();
                    }
                    if (tcc != null && !tcc.hasMaterialisation()) {
                        TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                        return;
                    }
                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                        return;
                    }
                    if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                        TARDISMessage.send(player, "NOT_WHILE_DISPERSED");
                        return;
                    }
                    HashMap<String, Object> wherei = new HashMap<String, Object>();
                    wherei.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        final PRESET preset = tardis.getPreset();
                        if (preset.equals(PRESET.JUNK)) {
                            return;
                        }
                        UUID ownerUUID = tardis.getUuid();
                        if ((tardis.isIso_on() && !uuid.equals(ownerUUID) && event.isCancelled() && !player.hasPermission("tardis.skeletonkey")) || plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                            // check if cancelled so we don't get double messages from the bind listener
                            TARDISMessage.send(player, "ISO_HANDS_OFF");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                            TARDISMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        String beacon = tardis.getBeacon();
                        boolean error = false;
                        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            TARDISMessage.send(player, "HANDBRAKE_IN_VORTEX");
                        } else {
                            Action action = event.getAction();
                            BlockState state = block.getState();
                            Lever lever = (Lever) state.getData();
                            int dist = 0;
                            // should the beacon turn on
                            HashMap<String, Object> wherek = new HashMap<String, Object>();
                            wherek.put("uuid", uuid.toString());
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
                            boolean beac_on = true;
                            boolean bar = false;
                            int flight_mode = 1;
                            if (rsp.resultSet()) {
                                beac_on = rsp.isBeaconOn();
                                flight_mode = rsp.getFlightMode();
                                bar = rsp.isTravelbarOn();
                            }
                            final QueryFactory qf = new QueryFactory(plugin);
                            if (action == Action.RIGHT_CLICK_BLOCK) {
                                if (tardis.isHandbrake_on()) {
                                    // Changes the lever to off
                                    lever.setPowered(false);
                                    state.setData(lever);
                                    state.update();
                                    if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                                        plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(Integer.valueOf(id));
                                    }
                                    // check if door is open
                                    if (isDoorOpen(id)) {
                                        TARDISMessage.send(player, "DOOR_CLOSE");
                                        return;
                                    }
                                    TARDISSounds.playTARDISSound(handbrake_loc, "tardis_handbrake_release");
                                    if (!beac_on && !beacon.isEmpty()) {
                                        toggleBeacon(beacon, true);
                                    }
                                    set.put("handbrake_on", 0);
                                    TARDISMessage.send(player, "HANDBRAKE_OFF");
                                    plugin.getTrackerKeeper().getInVortex().add(id);
                                    // dematerialise
                                    new TARDISDematerialiseToVortex(plugin, id, player, handbrake_loc).run();
                                    if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                                        if (bar) {
                                            long bar_time = (flight_mode == 2 || flight_mode == 3) ? 1500L : 890L;
                                            new TARDISTravelBar(plugin).showTravelRemaining(player, bar_time);
                                        }
                                        // materialise
                                        new TARDISMaterialseFromVortex(plugin, id, player, handbrake_loc).run();
                                    } else {
                                        new TARDISTravelBar(plugin).showTravelRemaining(player, 445L);
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new TARDISLoopingFlightSound(plugin, handbrake_loc, id), 500L);
                                    }
                                } else {
                                    TARDISMessage.send(player, "HANDBRAKE_OFF_ERR");
                                    error = true;
                                }
                            }
                            if (action == Action.LEFT_CLICK_BLOCK) {
                                if (!tardis.isHandbrake_on()) {
                                    TARDISSounds.playTARDISSound(handbrake_loc, "tardis_handbrake_engage");
                                    // Changes the lever to on
                                    lever.setPowered(true);
                                    state.setData(lever);
                                    state.update();
                                    // Check if its at a recharge point
                                    TARDISArtronLevels tal = new TARDISArtronLevels(plugin);
                                    tal.recharge(id);
                                    if (!beac_on && !beacon.isEmpty()) {
                                        toggleBeacon(beacon, false);
                                    }
                                    // Remove energy from TARDIS and sets database
                                    set.put("handbrake_on", 1);
                                    TARDISMessage.send(player, "HANDBRAKE_ON");
                                    if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                                        int amount = plugin.getTrackerKeeper().getHasDestination().get(id) * -1;
                                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                                        wheret.put("tardis_id", id);
                                        qf.alterEnergyLevel("tardis", amount, wheret, player);
                                        if (!uuid.equals(ownerUUID)) {
                                            Player ptl = plugin.getServer().getPlayer(ownerUUID);
                                            if (ptl != null) {
                                                new TARDISArtronIndicator(plugin).showArtronLevel(ptl, id, Math.abs(amount));
                                            }
                                        }
                                    }
                                    plugin.getTrackerKeeper().getHasDestination().remove(id);
                                    if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().remove(Integer.valueOf(id));
                                    }
                                    // damage the circuit if configured
                                    if (tcc != null && plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                                        // decrement uses
                                        int uses_left = tcc.getMaterialisationUses();
                                        new TARDISCircuitDamager(plugin, DISK_CIRCUIT.MATERIALISATION, uses_left, id, player).damage();
                                    }
                                } else {
                                    TARDISMessage.send(player, "HANDBRAKE_ON_ERR");
                                    error = true;
                                }
                            }
                            if (!error) {
                                HashMap<String, Object> whereh = new HashMap<String, Object>();
                                whereh.put("tardis_id", id);
                                qf.doUpdate("tardis", set, whereh);
                            }
                        }
                    }
                }
            }
        }
    }

    private void toggleBeacon(String str, boolean on) {
        String[] beaconData = str.split(":");
        World w = plugin.getServer().getWorld(beaconData[0]);
        int bx = TARDISNumberParsers.parseInt(beaconData[1]);
        int by = TARDISNumberParsers.parseInt(beaconData[2]);
        int bz = TARDISNumberParsers.parseInt(beaconData[3]);
        Location bl = new Location(w, bx, by, bz);
        Block b = bl.getBlock();
        b.setType((on) ? Material.GLASS : Material.REDSTONE_BLOCK);
    }

    @SuppressWarnings("deprecation")
    private boolean isDoorOpen(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            byte data = TARDISLocationGetters.getLocationFromDB(rs.getDoor_location(), 0.0f, 0.0f).getBlock().getData();
            return plugin.getGeneralKeeper().getDoorListener().isDoorOpen(data, rs.getDoor_direction());
        }
        return false;
    }
}
