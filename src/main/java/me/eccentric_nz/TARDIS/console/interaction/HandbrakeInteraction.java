/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.CircuitChecker;
import me.eccentric_nz.TARDIS.advanced.CircuitDamager;
import me.eccentric_nz.TARDIS.artron.ArtronLevels;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.utility.SculkShrieker;
import me.eccentric_nz.TARDIS.camera.CameraTracker;
import me.eccentric_nz.TARDIS.console.models.HandbrakeModel;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Throticle;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetThrottle;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.flight.ExteriorFlight;
import me.eccentric_nz.TARDIS.flight.TARDISTakeoff;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import me.eccentric_nz.TARDIS.rotors.TimeRotor;
import me.eccentric_nz.TARDIS.sensor.BeaconSensor;
import me.eccentric_nz.TARDIS.sensor.HandbrakeSensor;
import me.eccentric_nz.TARDIS.utility.Handbrake;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class HandbrakeInteraction {

    private final TARDIS plugin;

    public HandbrakeInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(int id, int state, Player player, Interaction interaction) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        UUID uuid = player.getUniqueId();
        Location handbrake = interaction.getLocation();
        CircuitChecker tcc = new CircuitChecker(plugin, id);
        tcc.getCircuits();
        if (plugin.getConfig().getBoolean("difficulty.circuits") && !tcc.hasMaterialisation() && !plugin.getUtils().inGracePeriod(player, state == 0)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MAT_CIRCUIT");
            return;
        }
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return;
        }
        if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
            return;
        }
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            ChameleonPreset preset = tardis.getPreset();
            if (preset.equals(ChameleonPreset.JUNK)) {
                return;
            }
            UUID ownerUUID = tardis.getUuid();
            if ((tardis.isIsomorphicOn() && !uuid.equals(ownerUUID) && !TARDISPermission.hasPermission(player, "tardis.skeletonkey")) || plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                // check if cancelled, so we don't get double messages from the bind listener
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ISO_HANDS_OFF");
                return;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                return;
            }
            String beacon = tardis.getBeacon();
            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getDidDematToVortex().contains(id) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                plugin.getMessenger().sendStatus(player, "HANDBRAKE_IN_VORTEX");
            } else {
                // should the beacon turn on
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                boolean beac_on = true;
                boolean bar = false;
                Throticle throticle = new ResultSetThrottle(plugin).getSpeedAndParticles(uuid.toString());
                if (rsp.resultSet()) {
                    beac_on = rsp.isBeaconOn();
                    bar = rsp.isTravelbarOn();
                }
                if (state == 1) {
                    if (tardis.isHandbrakeOn()) {
                        if (preset.equals(ChameleonPreset.JUNK_MODE) && !plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NEED_DEST");
                            return;
                        }
                        // check there is enough power for at least random travel
                        if (!plugin.getTrackerKeeper().getHasDestination().containsKey(id) && tardis.getArtronLevel() < plugin.getArtronConfig().getInt("random")) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NOT_ENOUGH");
                            return;
                        }
                        Handbrake check = new Handbrake(plugin);
                        // check if door is open
                        if (check.isDoorOpen(id)) {
                            plugin.getMessenger().sendStatus(player, "DOOR_CLOSE");
                            // track handbrake clicked for takeoff when door closed
                            plugin.getTrackerKeeper().getHasClickedHandbrake().add(id);
                            // give them 30 seconds to close the door
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getHasClickedHandbrake().removeAll(Collections.singleton(id)), 600L);
                            return;
                        }
                        // check the state of the Relativity Differentiator
                        if (check.isFlightModeExterior(uuid.toString()) && TARDISPermission.hasPermission(player, "tardis.fly") && preset.usesArmourStand()) {
                            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                            if (!rsc.resultSet()) {
                                plugin.debug("No current location");
                                return;
                            }
                            Current current = rsc.getCurrent();
                            // check if TARDIS is underground
                            for (int y = current.location().getBlockY() + 4; y < current.location().getBlockY() + 8; y++) {
                                if (!current.location().getWorld().getBlockAt(current.location().getBlockX(), y, current.location().getBlockZ()).getType().isAir()) {
                                    plugin.getMessenger().sendStatus(player, "FLIGHT_AIR");
                                    return;
                                }
                            }
                            if (CameraTracker.CAMERA_IN_USE.contains(id)) {
                                plugin.getMessenger().sendStatus(player, "FLIGHT_CAMERA");
                                return;
                            }
                            // make sure it's a 3D modelled preset
                            if (!preset.usesArmourStand()) {
                                plugin.getMessenger().sendStatus(player, "FLIGHT_PRESET");
                                return;
                            }
                            // fly the TARDIS exterior
                            Location exterior = current.location().clone();
                            exterior.setYaw(player.getLocation().getYaw());
                            exterior.setPitch(player.getLocation().getPitch());
                            // check the armour stand is a custom one
                            if (VehicleUtility.isNotFlightReady(exterior)) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_REBUILD");
                                return;
                            } else {
                                new ExteriorFlight(plugin).startFlying(player, id, null, exterior, beac_on, beacon);
                                TARDISSounds.playTARDISSound(handbrake, "tardis_handbrake_release");
                            }
                        } else {
                            new TARDISTakeoff(plugin).run(id, null, handbrake, player, beac_on, beacon, bar, throticle);
                        }
                        // start time rotor?
                        if (tardis.getRotor() != null) {
                            if (tardis.getRotor() == TARDISConstants.UUID_ZERO) {
                                // get sculk shrieker and set shreiking
                                SculkShrieker.setRotor(id);
                            } else {
                                ItemFrame itemFrame = TimeRotor.getItemFrame(tardis.getRotor());
                                if (itemFrame != null) {
                                    // get the rotor type
                                    Rotor rotor = Rotor.getByModel(TimeRotor.getRotorModel(itemFrame));
                                    TimeRotor.setRotor(rotor, itemFrame);
                                }
                            }
                        }
                        // update handbrake state
                        HashMap<String, Object> seti = new HashMap<>();
                        seti.put("state", 0);
                        HashMap<String, Object> whereinteraction = new HashMap<>();
                        whereinteraction.put("tardis_id", id);
                        whereinteraction.put("control", "HANDBRAKE");
                        plugin.getQueryFactory().doUpdate("interactions", seti, whereinteraction);
                        // get the model display item
                        UUID model = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
                        if (model != null) {
                            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(model);
                            // change the custom model data so the lever is off
                            new HandbrakeModel().setState(display, 0);
                        }
                    } else {
                        plugin.getMessenger().sendStatus(player, "HANDBRAKE_OFF_ERR");
                    }
                }
                if (state == 0) {
                    if (!tardis.isHandbrakeOn()) {
                        // stop time rotor?
                        if (tardis.getRotor() != null) {
                            if (tardis.getRotor() == TARDISConstants.UUID_ZERO) {
                                SculkShrieker.stopRotor(id);
                            } else {
                                ItemFrame itemFrame = TimeRotor.getItemFrame(tardis.getRotor());
                                if (itemFrame != null) {
                                    // cancel the animation
                                    int task = TimeRotor.ANIMATED_ROTORS.getOrDefault(itemFrame.getUniqueId(), -1);
                                    plugin.getServer().getScheduler().cancelTask(task);
                                    TimeRotor.setRotor(TimeRotor.getRotorOffModel(itemFrame), itemFrame);
                                }
                            }
                        }
                        // if player is flying TARDIS exterior stop sound loop
                        Optional.ofNullable(plugin.getTrackerKeeper().getFlyingReturnLocation().get(uuid)).ifPresent(value -> {
                            player.stopAllSounds();
                            if (value.sound() != -1) {
                                plugin.getServer().getScheduler().cancelTask(value.sound());
                            }
                            plugin.getTrackerKeeper().getFlyingReturnLocation().remove(uuid);
                        });
                        TARDISSounds.playTARDISSound(handbrake, "tardis_handbrake_engage");
                        // get the model display item
                        UUID model = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
                        if (model != null) {
                            ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(model);
                            // change the custom model data so the lever is on
                            new HandbrakeModel().setState(display, 1);
                        }
                        // Check if it's at a recharge point
                        new ArtronLevels(plugin).recharge(id);
                        if (!beac_on && !beacon.isEmpty()) {
                            new BeaconSensor().toggle(beacon, false);
                        }
                        new HandbrakeSensor(plugin, id).toggle();
                        // remove energy from TARDIS and set database
                        plugin.getMessenger().sendStatus(player, "HANDBRAKE_ON");
                        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                            int amount = Math.round(plugin.getTrackerKeeper().getHasDestination().get(id).cost() * throticle.throttle().getArtronMultiplier());
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("tardis_id", id);
                            plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wheret, player);
                            if (!uuid.equals(ownerUUID)) {
                                Player ptl = plugin.getServer().getPlayer(ownerUUID);
                                if (ptl != null) {
                                    plugin.getMessenger().sendArtron(ptl, id, Math.abs(amount));
                                }
                            }
                        }
                        plugin.getTrackerKeeper().getHasDestination().remove(id);
                        if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                            plugin.getTrackerKeeper().getHasRandomised().removeAll(Collections.singleton(id));
                        }
                        // damage the circuit if configured
                        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                            // decrement uses
                            int uses_left = tcc.getMaterialisationUses();
                            new CircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player).damage();
                        }
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("handbrake_on", 1);
                        HashMap<String, Object> whereh = new HashMap<>();
                        whereh.put("tardis_id", id);
                        plugin.getQueryFactory().doUpdate("tardis", set, whereh);
                        HashMap<String, Object> seti = new HashMap<>();
                        seti.put("state", 1);
                        HashMap<String, Object> whereinteraction = new HashMap<>();
                        whereinteraction.put("tardis_id", id);
                        whereinteraction.put("control", "HANDBRAKE");
                        plugin.getQueryFactory().doUpdate("interactions", seti, whereinteraction);
                    } else {
                        plugin.getMessenger().sendStatus(player, "HANDBRAKE_ON_ERR");
                    }
                }
            }
        }
    }
}
