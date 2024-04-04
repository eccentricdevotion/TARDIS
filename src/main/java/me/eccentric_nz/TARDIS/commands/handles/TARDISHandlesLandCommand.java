/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISHandbrake;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISRandomiserCircuit;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.Handbrake;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISHandlesLandCommand {

    private final TARDIS plugin;

    TARDISHandlesLandCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean exitVortex(Player player, int id, String uuid) {
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (tardis.getPreset().equals(ChameleonPreset.JUNK)) {
                plugin.getMessenger().handlesSend(player, "HANDLES_JUNK");
                return true;
            }
            if (tardis.isHandbrakeOn()) {
                plugin.getMessenger().handlesSend(player, "HANDBRAKE_ON_ERR");
                return true;
            }
            if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                plugin.getMessenger().handlesSend(player, "HANDLES_VORTEX");
                return true;
            }
            // must have a destination, but setting one will make the TARDIS automatically exit the time vortex
            // so generate a random overworld location
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
            if (rscl.resultSet()) {
                Location l = new TARDISRandomiserCircuit(plugin).getRandomlocation(player, rscl.getDirection());
                if (l != null) {
                    HashMap<String, Object> set_next = new HashMap<>();
                    HashMap<String, Object> where_next = new HashMap<>();
                    set_next.put("world", l.getWorld().getName());
                    set_next.put("x", l.getBlockX());
                    set_next.put("y", l.getBlockY());
                    set_next.put("z", l.getBlockZ());
                    set_next.put("direction", rscl.getDirection().toString());
                    boolean sub = plugin.getTrackerKeeper().getSubmarine().contains(id);
                    set_next.put("submarine", (sub) ? 1 : 0);
                    plugin.getTrackerKeeper().getSubmarine().remove(id);
                    where_next.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("random_circuit"), TravelType.RANDOM));
                    plugin.getTrackerKeeper().getHasRandomised().add(id);
                    new TARDISLand(plugin, id, player).exitVortex();
                    plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.RANDOM, id));
                    // delay setting handbrake
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        HashMap<String, Object> whereh = new HashMap<>();
                        whereh.put("type", 0);
                        whereh.put("tardis_id", id);
                        whereh.put("secondary", 0);
                        ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
                        if (rsc.resultSet()) {
                            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                            TARDISSounds.playTARDISSound(location, "tardis_handbrake_engage");
                            // Changes the lever to on
                            TARDISHandbrake.setLevers(location.getBlock(), true, true, location.toString(), id, plugin);
                            // Check if it's at a recharge point
                            TARDISArtronLevels tal = new TARDISArtronLevels(plugin);
                            tal.recharge(id);
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                            boolean beac_on = true;
                            String beacon = tardis.getBeacon();
                            if (rsp.resultSet()) {
                                beac_on = rsp.isBeaconOn();
                            }
                            if (!beac_on && !beacon.isEmpty()) {
                                new Handbrake(plugin).toggleBeacon(beacon, false);
                            }
                            // Remove energy from TARDIS and sets database
                            plugin.getMessenger().sendStatus(player, "HANDBRAKE_ON");
                            int amount = plugin.getTrackerKeeper().getHasDestination().get(id).getCost() * -1;
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("tardis_id", id);
                            plugin.getQueryFactory().alterEnergyLevel("tardis", amount, wheret, player);
                            plugin.getMessenger().sendArtron(player, id, Math.abs(amount));
                            plugin.getTrackerKeeper().getHasDestination().remove(id);
                            if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                plugin.getTrackerKeeper().getHasRandomised().removeAll(Collections.singleton(id));
                            }
                            // damage the circuit if configured
                            TARDISCircuitChecker tcc = null;
                            if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
                                tcc = new TARDISCircuitChecker(plugin, id);
                                tcc.getCircuits();
                            }
                            if (tcc != null && plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                                // decrement uses
                                int uses_left = tcc.getMaterialisationUses();
                                new TARDISCircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player).damage();
                            }
                            HashMap<String, Object> set = new HashMap<>();
                            set.put("handbrake_on", 1);
                            HashMap<String, Object> whereb = new HashMap<>();
                            whereb.put("tardis_id", id);
                            plugin.getQueryFactory().doUpdate("tardis", set, whereb);
                        }
                    }, 400L);
                } else {
                    plugin.getMessenger().handlesSend(player, "HANDLES_NO_LOCATION");
                }
            } else {
                plugin.getMessenger().handlesSend(player, "CURRENT_NOT_FOUND");
            }
        }
        return true;
    }
}
