package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.utility.TARDISSculkShrieker;
import me.eccentric_nz.TARDIS.camera.TARDISCameraTracker;
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
import me.eccentric_nz.TARDIS.flight.TARDISExteriorFlight;
import me.eccentric_nz.TARDIS.flight.TARDISHandbrake;
import me.eccentric_nz.TARDIS.flight.TARDISTakeoff;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.sensor.BeaconSensor;
import me.eccentric_nz.TARDIS.sensor.HandbrakeSensor;
import me.eccentric_nz.TARDIS.utility.Handbrake;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SonicHandbrake {

    private final TARDIS plugin;

    public SonicHandbrake(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(int id, Player player, Block block) {
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, !tardis.isHandbrakeOn()) && !tcc.hasMaterialisation()) {
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
            ChameleonPreset preset = tardis.getPreset();
            if (preset.equals(ChameleonPreset.JUNK)) {
                return;
            }
            UUID uuid = player.getUniqueId();
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
                Location handbrake_loc = block.getLocation();
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
                    if (check.isRelativityDifferentiated(id) && TARDISPermission.hasPermission(player, "tardis.fly") && preset.usesArmourStand() && !player.isSneaking()) {
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
                        if (TARDISCameraTracker.CAMERA_IN_USE.contains(id)) {
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
                            new TARDISExteriorFlight(plugin).startFlying(player, id, block, exterior, beac_on, beacon);
                        }
                    } else {
                        new TARDISTakeoff(plugin).run(id, block, handbrake_loc, player, beac_on, beacon, bar, throticle);
                    }
                    // start time rotor?
                    if (tardis.getRotor() != null) {
                        if (tardis.getRotor() == TARDISConstants.UUID_ZERO) {
                            // get sculk shrieker and set shrieking
                            TARDISSculkShrieker.setRotor(id);
                        } else {
                            ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                            if (itemFrame != null) {
                                // get the rotor type
                                Rotor rotor = Rotor.getByModel(TARDISTimeRotor.getRotorModel(itemFrame));
                                TARDISTimeRotor.setRotor(rotor, itemFrame);
                            }
                        }
                    }
                }
                if (!tardis.isHandbrakeOn()) {
                    // stop time rotor?
                    if (tardis.getRotor() != null) {
                        if (tardis.getRotor() == TARDISConstants.UUID_ZERO) {
                            TARDISSculkShrieker.stopRotor(id);
                        } else {
                            ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                            if (itemFrame != null) {
                                // cancel the animation
                                int task = TARDISTimeRotor.ANIMATED_ROTORS.getOrDefault(itemFrame.getUniqueId(), -1);
                                plugin.getServer().getScheduler().cancelTask(task);
                                TARDISTimeRotor.setRotor(TARDISTimeRotor.getRotorOffModel(itemFrame), itemFrame);
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
                    TARDISSounds.playTARDISSound(handbrake_loc, "tardis_handbrake_engage");
                    // Changes the lever to on
                    TARDISHandbrake.setLevers(block, true, true, handbrake_loc.toString(), id, plugin);
                    // Check if it's at a recharge point
                    new TARDISArtronLevels(plugin).recharge(id);
                    if (!beac_on && !beacon.isEmpty()) {
                        new BeaconSensor().toggle(beacon, false);
                    }
                    new HandbrakeSensor(plugin, id).toggle();
                    // Remove energy from TARDIS and sets database
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
                        new TARDISCircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player).damage();
                    }
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("handbrake_on", 1);
                    HashMap<String, Object> whereh = new HashMap<>();
                    whereh.put("tardis_id", id);
                    plugin.getQueryFactory().doUpdate("tardis", set, whereh);
                } else {
                    plugin.getMessenger().sendStatus(player, "HANDBRAKE_ON_ERR");
                }
            }
        }
    }
}
