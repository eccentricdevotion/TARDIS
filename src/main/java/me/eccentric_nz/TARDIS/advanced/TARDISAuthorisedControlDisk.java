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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.advanced;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetDestinations;
import me.eccentric_nz.tardis.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Flag;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.flight.TardisDematerialiseToVortex;
import me.eccentric_nz.tardis.flight.TardisHandbrake;
import me.eccentric_nz.tardis.flight.TardisMaterialiseFromVortex;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import me.eccentric_nz.tardis.travel.TardisEpsRunnable;
import me.eccentric_nz.tardis.utility.TardisSounds;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Security Protocol 712 is a security feature aboard the Doctor's TARDIS which causes the ship to dematerialise and
 * re-appear at a predetermined point in space and time without its pilot when activated. It can be programmed to be
 * triggered by different events or objects such as Sally Sparrow's DVDs or the activation of the echelon circuit. A
 * holographic recreation of the incumbent Doctor will be projected to inform anyone within the TARDIS of the protocol
 * being activated.
 *
 * @author eccentric_nz
 */
public class TardisAuthorisedControlDisk {

    private final TardisPlugin plugin;
    private final UUID uuid;
    private final List<String> lore;
    private final int id;
    private final Player player;
    private final String eps;
    private final String creeper;

    TardisAuthorisedControlDisk(TardisPlugin plugin, UUID uuid, List<String> lore, int id, Player player, String eps, String creeper) {
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
        Player timeLord = plugin.getServer().getPlayer(uuid);
        if (timeLord == null || !timeLord.isOnline()) {
            return "The Time Lord of this TARDIS is not online.";
        }
        Location location = null;
        CardinalDirection direction = CardinalDirection.EAST;
        boolean atPlayerLocation = false;
        if (lore.size() > 3) {
            // has a stored save
            String save = lore.get(3);
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            if (save.equals("Home")) {
                // get home location
                ResultSetHomeLocation resultSetHomeLocation = new ResultSetHomeLocation(plugin, where);
                if (resultSetHomeLocation.resultSet()) {
                    location = new Location(resultSetHomeLocation.getWorld(), resultSetHomeLocation.getX(), resultSetHomeLocation.getY(), resultSetHomeLocation.getZ());
                    direction = resultSetHomeLocation.getDirection();
                } else {
                    return "Could not find the TARDIS's home location.";
                }
            } else {
                // get save location
                ResultSetDestinations resultSetDestinations = new ResultSetDestinations(plugin, where, false);
                if (resultSetDestinations.resultSet()) {
                    World world = TardisAliasResolver.getWorldFromAlias(resultSetDestinations.getWorld());
                    if (world != null) {
                        location = new Location(world, resultSetDestinations.getX(), resultSetDestinations.getY(), resultSetDestinations.getZ());
                        direction = CardinalDirection.valueOf(resultSetDestinations.getDirection());
                    } else {
                        return "Could not find the specified TARDIS save.";
                    }
                }
            }
        } else {
            // get player location
            location = timeLord.getLocation();
            atPlayerLocation = true;
        }
        if (location != null) {
            if (atPlayerLocation) {
                if (plugin.getUtils().inTARDISWorld(timeLord)) {
                    return "The Time Lord must be outside the TARDIS.";
                }
                // check respect
                if (!plugin.getPluginRespect().getRespect(location, new Parameters(timeLord, Flag.getNoMessageFlags()))) {
                    return "The Time Lord's location does not allow travel.";
                }
            }
            HashMap<String, Object> wheren = new HashMap<>(); // TODO What is "n"?
            wheren.put("tardis_id", id);
            HashMap<String, Object> setn = new HashMap<>();
            setn.put("world", Objects.requireNonNull(location.getWorld()).getName());
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
            ResultSetControls resultSetControls = new ResultSetControls(plugin, where, false);
            if (resultSetControls.resultSet()) {
                Location handbrake = TardisStaticLocationGetters.getLocationFromBukkitString(resultSetControls.getLocation());
                assert handbrake != null;
                TardisHandbrake.setLevers(handbrake.getBlock(), false, true, handbrake.toString(), id, plugin);
                if (plugin.getConfig().getBoolean("circuits.damage")) {
                    plugin.getTrackerKeeper().getHasNotClickedHandbrake().remove(id);
                }
                TardisSounds.playTARDISSound(handbrake, "tardis_handbrake_release");
                HashMap<String, Object> set = new HashMap<>();
                set.put("handbrake_on", 0);
                HashMap<String, Object> whereHandbrake = new HashMap<>();
                whereHandbrake.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("tardis", set, whereHandbrake);
                TardisMessage.send(player, "HANDBRAKE_OFF");
                plugin.getTrackerKeeper().getInVortex().add(id);
                // show emergency program one
                HashMap<String, Object> whereVortex = new HashMap<>();
                whereVortex.put("tardis_id", id);
                ResultSetTravellers resultSetTravellers = new ResultSetTravellers(plugin, whereVortex, true);
                List<UUID> playerUUIDs;
                if (resultSetTravellers.resultSet()) {
                    playerUUIDs = resultSetTravellers.getData();
                } else {
                    playerUUIDs = new ArrayList<>();
                    playerUUIDs.add(player.getUniqueId());
                }
                String message = "The TARDIS has detected an authorised control disk, valid for one journey only. Travelling to the programmed location.";
                TardisEpsRunnable EpsRunnable = new TardisEpsRunnable(plugin, message, player, playerUUIDs, id, eps, creeper);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EpsRunnable, 20L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // dematerialise
                    new TardisDematerialiseToVortex(plugin, id, player, handbrake).run();
                    // materialise
                    new TardisMaterialiseFromVortex(plugin, id, player, handbrake, SpaceTimeThrottle.NORMAL).run();
                }, 60L);
            } else {
                return "Could not disengage handbrake.";
            }
        }
        return "success";
    }
}
