/*
 * Copyright (C) 2020 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.flight.TARDISDematerialiseToVortex;
import me.eccentric_nz.TARDIS.flight.TARDISHandbrake;
import me.eccentric_nz.TARDIS.flight.TARDISMaterialseFromVortex;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.travel.TARDISEPSRunnable;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Security Protocol 712 is a security feature aboard the Doctor's TARDIS which causes the ship to dematerialise and
 * re-appear at a predetermined point in space and time without its pilot when activated. It can be programmed to be
 * triggered by different events or objects such as Sally Sparrow's DVDs or the activation of the echelon circuit. A
 * holographic recreation of the incumbent Doctor will be projected to inform anyone within the TARDIS of the protocol
 * being activated.
 *
 * @author eccentric_nz
 */
public class TARDISAuthorisedControlDisk {

    private final TARDIS plugin;
    private final UUID uuid;
    private final List<String> lore;
    private final int id;
    private final Player player;
    private final String eps;
    private final String creeper;

    public TARDISAuthorisedControlDisk(TARDIS plugin, UUID uuid, List<String> lore, int id, Player player, String eps, String creeper) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.lore = lore;
        this.id = id;
        this.player = player;
        this.eps = eps;
        this.creeper = creeper;
    }

    public String process() {
        // find player
        Player timelord = plugin.getServer().getPlayer(uuid);
        if (timelord == null || !timelord.isOnline()) {
            return "The Time Lord of this TARDIS is not online.";
        }
        Location location = null;
        COMPASS direction = COMPASS.EAST;
        boolean isPlayerLocation = false;
        if (lore.size() > 3) {
            // has a stored save
            String save = lore.get(3);
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            if (save.equals("Home")) {
                // get home location
                ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, where);
                if (rsh.resultSet()) {
                    location = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
                    direction = rsh.getDirection();
                } else {
                    return "Could not find the TARDIS's home location.";
                }
            } else {
                // get save location
                ResultSetDestinations rsd = new ResultSetDestinations(plugin, where, false);
                if (rsd.resultSet()) {
                    World w = plugin.getServer().getWorld(rsd.getWorld());
                    if (w != null) {
                        location = new Location(w, rsd.getX(), rsd.getY(), rsd.getZ());
                        direction = COMPASS.valueOf(rsd.getDirection());
                    } else {
                        return "Could not find the specified TARDIS save.";
                    }
                }
            }
        } else {
            // get player location
            location = timelord.getLocation();
            isPlayerLocation = true;
        }
        if (location != null) {
            if (isPlayerLocation) {
                if (plugin.getUtils().inTARDISWorld(timelord)) {
                    return "The Time Lord must be outside the TARDIS.";
                }
                // check respect
                if (!plugin.getPluginRespect().getRespect(location, new Parameters(timelord, Flag.getNoMessageFlags()))) {
                    return "The Time Lord's location does not allow travel.";
                }
            }
            HashMap<String, Object> wheren = new HashMap<>();
            wheren.put("tardis_id", id);
            HashMap<String, Object> setn = new HashMap<>();
            setn.put("world", location.getWorld().getName());
            setn.put("x", location.getBlockX());
            setn.put("y", location.getBlockY());
            setn.put("z", location.getBlockZ());
            setn.put("direction", direction.toString());
            setn.put("submarine", 0);
            plugin.getQueryFactory().doUpdate("next", setn, wheren);
            plugin.getTrackerKeeper().getHasDestination().put(id, plugin.getArtronConfig().getInt("travel"));
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("type", 0);
            where.put("secondary", 0);
            ResultSetControls rsc = new ResultSetControls(plugin, where, false);
            if (rsc.resultSet()) {
                Location handbrake = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                TARDISHandbrake.setLevers(handbrake.getBlock(), false, true, handbrake.toString(), id, plugin);
                if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getTrackerKeeper().getHasNotClickedHandbrake().contains(id)) {
                    plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(Integer.valueOf(id));
                }
                TARDISSounds.playTARDISSound(handbrake, "tardis_handbrake_release");
                HashMap<String, Object> set = new HashMap<>();
                set.put("handbrake_on", 0);
                HashMap<String, Object> whereh = new HashMap<>();
                whereh.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("tardis", set, whereh);
                TARDISMessage.send(player, "HANDBRAKE_OFF");
                plugin.getTrackerKeeper().getInVortex().add(id);
                // show emergency program one
                HashMap<String, Object> wherev = new HashMap<>();
                wherev.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wherev, true);
                List<UUID> playerUUIDs;
                if (rst.resultSet()) {
                    playerUUIDs = rst.getData();
                } else {
                    playerUUIDs = new ArrayList<>();
                    playerUUIDs.add(player.getUniqueId());
                }
                String message = "The TARDIS has detected an authorised control disc, valid for one journey only. Travelling to the programmed location.";
                TARDISEPSRunnable EPS_runnable = new TARDISEPSRunnable(plugin, message, player, playerUUIDs, id, eps, creeper);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EPS_runnable, 20L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // dematerialise
                    new TARDISDematerialiseToVortex(plugin, id, player, handbrake).run();
                    // materialise
                    new TARDISMaterialseFromVortex(plugin, id, player, handbrake, SpaceTimeThrottle.NORMAL).run();
                }, 60L);
            } else {
                return "Could not disengage handbrake.";
            }
        }
        return "success";
    }
}
