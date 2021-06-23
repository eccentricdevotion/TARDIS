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
package me.eccentric_nz.tardis.commands.handles;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisCircuitChecker;
import me.eccentric_nz.tardis.advanced.TardisCircuitDamager;
import me.eccentric_nz.tardis.artron.TardisArtronIndicator;
import me.eccentric_nz.tardis.artron.TardisArtronLevels;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.DiskCircuit;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.flight.TardisHandbrake;
import me.eccentric_nz.tardis.flight.TardisHandbrakeListener;
import me.eccentric_nz.tardis.flight.TardisLand;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TardisRandomiserCircuit;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
class TardisHandlesLandCommand {

    private final TardisPlugin plugin;

    TardisHandlesLandCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean exitVortex(Player player, int id, String uuid) {
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (tardis.getPreset().equals(Preset.JUNK)) {
                TardisMessage.handlesSend(player, "HANDLES_JUNK");
                return true;
            }
            if (tardis.isHandbrakeOn()) {
                TardisMessage.handlesSend(player, "HANDBRAKE_ON_ERR");
                return true;
            }
            if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                TardisMessage.handlesSend(player, "HANDLES_VORTEX");
                return true;
            }
            // must have a destination, but setting one will make the TARDIS automatically exit the time vortex
            // so generate a random overworld location
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
            if (rscl.resultSet()) {
                Location l = new TardisRandomiserCircuit(plugin).getRandomlocation(player, rscl.getDirection());
                if (l != null) {
                    HashMap<String, Object> set_next = new HashMap<>();
                    HashMap<String, Object> where_next = new HashMap<>();
                    set_next.put("world", Objects.requireNonNull(l.getWorld()).getName());
                    set_next.put("x", l.getBlockX());
                    set_next.put("y", l.getBlockY());
                    set_next.put("z", l.getBlockZ());
                    set_next.put("direction", rscl.getDirection().toString());
                    boolean sub = plugin.getTrackerKeeper().getSubmarine().contains(id);
                    set_next.put("submarine", (sub) ? 1 : 0);
                    plugin.getTrackerKeeper().getSubmarine().remove(id);
                    where_next.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("next", set_next, where_next);
                    plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("random_circuit"));
                    plugin.getTrackerKeeper().getHasRandomised().add(id);
                    new TardisLand(plugin, id, player).exitVortex();
                    // delay setting handbrake
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        HashMap<String, Object> whereh = new HashMap<>();
                        whereh.put("type", 0);
                        whereh.put("tardis_id", id);
                        whereh.put("secondary", 0);
                        ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
                        if (rsc.resultSet()) {
                            Location location = TardisStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                            assert location != null;
                            TardisSounds.playTARDISSound(location, "tardis_handbrake_engage");
                            // Changes the lever to on
                            TardisHandbrake.setLevers(location.getBlock(), true, true, location.toString(), id, plugin);
                            // Check if it's at a recharge point
                            TardisArtronLevels tal = new TardisArtronLevels(plugin);
                            tal.recharge(id);
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
                            boolean beac_on = true;
                            String beacon = tardis.getBeacon();
                            if (rsp.resultSet()) {
                                beac_on = rsp.isBeaconOn();
                            }
                            if (!beac_on && !beacon.isEmpty()) {
                                TardisHandbrakeListener.toggleBeacon(beacon, false);
                            }
                            // Remove energy from TARDIS and sets database
                            TardisMessage.send(player, "HANDBRAKE_ON");
                            int amount = plugin.getTrackerKeeper().getHasDestination().get(id) * -1;
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("tardis_id", id);
                            plugin.getQueryFactory().alterEnergyLevel("tardis", amount, wheret, player);
                            new TardisArtronIndicator(plugin).showArtronLevel(player, id, Math.abs(amount));
                            plugin.getTrackerKeeper().getHasDestination().remove(id);
                            if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                plugin.getTrackerKeeper().getHasRandomised().removeAll(Collections.singleton(id));
                            }
                            // damage the circuit if configured
                            TardisCircuitChecker tcc = null;
                            if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
                                tcc = new TardisCircuitChecker(plugin, id);
                                tcc.getCircuits();
                            }
                            if (tcc != null && plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                                // decrement uses
                                int uses_left = tcc.getMaterialisationUses();
                                new TardisCircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player).damage();
                            }
                            HashMap<String, Object> set = new HashMap<>();
                            set.put("handbrake_on", 1);
                            HashMap<String, Object> whereb = new HashMap<>();
                            whereb.put("tardis_id", id);
                            plugin.getQueryFactory().doUpdate("tardis", set, whereb);
                        }
                    }, 400L);
                } else {
                    TardisMessage.handlesSend(player, "HANDLES_NO_LOCATION");
                }
            } else {
                TardisMessage.handlesSend(player, "CURRENT_NOT_FOUND");
            }
        }
        return true;
    }
}
