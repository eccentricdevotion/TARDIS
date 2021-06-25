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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisCircuitChecker;
import me.eccentric_nz.tardis.advanced.TardisCircuitDamager;
import me.eccentric_nz.tardis.artron.TardisArtronIndicator;
import me.eccentric_nz.tardis.artron.TardisArtronLevels;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.TardisTimeRotor;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.DiskCircuit;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * The handbrake was a utensil on the tardis used for quick stops. River song once claimed that the tardis made it's
 * "whoosh" noise because the Doctor had left the handbrake on.
 *
 * @author eccentric_nz
 */
public class TardisHandbrakeListener implements Listener {

    private final TardisPlugin plugin;

    public TardisHandbrakeListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public static void toggleBeacon(String str, boolean on) {
        Block b = Objects.requireNonNull(TardisStaticLocationGetters.getLocationFromDB(str)).getBlock();
        b.setBlockData((on) ? TardisConstants.GLASS : TardisConstants.POWER);
    }

    /**
     * Listens for player interaction with the handbrake (lever) on the TARDIS console. If the button is right-clicked
     * the handbrake is set off, if right-clicked while sneaking it is set on.
     *
     * @param event the player clicking the handbrake
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND) || event.getPlayer().isSneaking()) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (blockType == Material.LEVER) {
                UUID uuid = player.getUniqueId();
                HashMap<String, Object> whereu = new HashMap<>();
                whereu.put("uuid", uuid.toString());
                ResultSetTravellers rsv = new ResultSetTravellers(plugin, whereu, false);
                boolean inside = rsv.resultSet();
                // check handbrake location against the database.
                Location handbrake_loc = block.getLocation();
                HashMap<String, Object> where = new HashMap<>();
                boolean found = false;
                int tmp_id = -1;
                if (inside) {
                    where.put("type", 0);
                    where.put("location", handbrake_loc.toString());
                    ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                    if (rsc.resultSet()) {
                        found = true;
                        tmp_id = rsc.getTardisId();
                    }
                } else {
                    where.put("uuid", uuid.toString());
                    where.put("handbrake", handbrake_loc.toString());
                    ResultSetJunk rsj = new ResultSetJunk(plugin, where);
                    if (rsj.resultSet()) {
                        found = true;
                        tmp_id = rsj.getTardisId();
                    }
                }
                if (found) {
                    event.setCancelled(true);
                    int id = tmp_id;
                    TardisCircuitChecker tcc = null;
                    if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
                        tcc = new TardisCircuitChecker(plugin, id);
                        tcc.getCircuits();
                    }
                    if (tcc != null && !tcc.hasMaterialisation()) {
                        TardisMessage.send(player, "NO_MAT_CIRCUIT");
                        return;
                    }
                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                        return;
                    }
                    if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                        TardisMessage.send(player, "NOT_WHILE_DISPERSED");
                        return;
                    }
                    HashMap<String, Object> wherei = new HashMap<>();
                    wherei.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        Preset preset = tardis.getPreset();
                        if (preset.equals(Preset.JUNK)) {
                            return;
                        }
                        UUID ownerUUID = tardis.getUuid();
                        if ((tardis.isIsoOn() && !uuid.equals(ownerUUID) && event.useInteractedBlock().equals(Event.Result.DENY) && !TardisPermission.hasPermission(player, "tardis.skeletonkey")) || plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                            // check if cancelled so we don't get double messages from the bind listener
                            TardisMessage.send(player, "ISO_HANDS_OFF");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered()) {
                            TardisMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        String beacon = tardis.getBeacon();
                        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getDidDematToVortex().contains(id) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                            TardisMessage.send(player, "HANDBRAKE_IN_VORTEX");
                        } else {
                            Action action = event.getAction();
                            // should the beacon turn on
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                            boolean beac_on = true;
                            boolean bar = false;
                            SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.NORMAL;
                            if (rsp.resultSet()) {
                                beac_on = rsp.isBeaconOn();
                                bar = rsp.isTravelBarOn();
                                spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
                            }
                            if (action == Action.RIGHT_CLICK_BLOCK) {
                                if (tardis.isHandbrakeOn()) {
                                    if (preset.equals(Preset.JUNK_MODE) && !plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                                        TardisMessage.send(player, "TRAVEL_NEED_DEST");
                                        return;
                                    }
                                    // check there is enough power for at last random travel
                                    if (!plugin.getTrackerKeeper().getHasDestination().containsKey(id) && tardis.getArtronLevel() < plugin.getArtronConfig().getInt("random")) {
                                        TardisMessage.send(player, "ENERGY_NOT_ENOUGH");
                                        return;
                                    }
                                    // check if door is open
                                    if (isDoorOpen(id)) {
                                        TardisMessage.send(player, "DOOR_CLOSE");
                                        // track handbrake clicked for takeoff when door closed
                                        plugin.getTrackerKeeper().getHasClickedHandbrake().add(id);
                                        // give them 30 seconds to close the door
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getHasClickedHandbrake().removeAll(Collections.singleton(id)), 600L);
                                        return;
                                    }
                                    new TardisTakeoff(plugin).run(id, block, handbrake_loc, player, beac_on, beacon, bar, spaceTimeThrottle);
                                    // start time rotor?
                                    if (tardis.getRotor() != null) {
                                        ItemFrame itemFrame = TardisTimeRotor.getItemFrame(tardis.getRotor());
                                        if (itemFrame != null) {
                                            TardisTimeRotor.setRotor(TardisTimeRotor.getRotorModelData(itemFrame), itemFrame, true);
                                        }
                                    }
                                } else {
                                    TardisMessage.send(player, "HANDBRAKE_OFF_ERR");
                                }
                            }
                            if (action == Action.LEFT_CLICK_BLOCK) {
                                if (!tardis.isHandbrakeOn()) {
                                    // stop time rotor?
                                    if (tardis.getRotor() != null) {
                                        ItemFrame itemFrame = TardisTimeRotor.getItemFrame(tardis.getRotor());
                                        if (itemFrame != null) {
                                            TardisTimeRotor.setRotor(TardisTimeRotor.getRotorModelData(itemFrame), itemFrame, false);
                                        }
                                    }
                                    TardisSounds.playTardisSound(handbrake_loc, "tardis_handbrake_engage");
                                    // Changes the lever to on
                                    TardisHandbrake.setLevers(block, true, inside, handbrake_loc.toString(), id, plugin);
                                    // Check if it's at a recharge point
                                    new TardisArtronLevels(plugin).recharge(id);
                                    if (!beac_on && !beacon.isEmpty()) {
                                        toggleBeacon(beacon, false);
                                    }
                                    // Remove energy from TARDIS and sets database
                                    TardisMessage.send(player, "HANDBRAKE_ON");
                                    if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                                        int amount = Math.round(plugin.getTrackerKeeper().getHasDestination().get(id) * spaceTimeThrottle.getArtronMultiplier());
                                        HashMap<String, Object> wheret = new HashMap<>();
                                        wheret.put("tardis_id", id);
                                        plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wheret, player);
                                        if (!uuid.equals(ownerUUID)) {
                                            Player ptl = plugin.getServer().getPlayer(ownerUUID);
                                            if (ptl != null) {
                                                new TardisArtronIndicator(plugin).showArtronLevel(ptl, id, Math.abs(amount));
                                            }
                                        }
                                    }
                                    plugin.getTrackerKeeper().getHasDestination().remove(id);
                                    if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().removeAll(Collections.singleton(id));
                                    }
                                    // damage the circuit if configured
                                    if (tcc != null && plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                                        // decrement uses
                                        int uses_left = tcc.getMaterialisationUses();
                                        new TardisCircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player).damage();
                                    }
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("handbrake_on", 1);
                                    HashMap<String, Object> whereh = new HashMap<>();
                                    whereh.put("tardis_id", id);
                                    plugin.getQueryFactory().doUpdate("tardis", set, whereh);
                                } else {
                                    TardisMessage.send(player, "HANDBRAKE_ON_ERR");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isDoorOpen(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            Block door = Objects.requireNonNull(TardisStaticLocationGetters.getLocationFromDB(rs.getDoorLocation())).getBlock();
            return TardisStaticUtils.isDoorOpen(door);
        }
        return false;
    }
}
