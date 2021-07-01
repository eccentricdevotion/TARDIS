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

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisDematerialisationEvent;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetNextLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.destroyers.DestroyData;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisSounds;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisDematerialiseToVortex implements Runnable {

    private final TardisPlugin plugin;
    private final int id;
    private final Player player;
    private final Location handbrake;

    public TardisDematerialiseToVortex(TardisPlugin plugin, int id, Player player, Location handbrake) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
        this.handbrake = handbrake;
    }

    @Override
    public void run() {
        UUID uuid = player.getUniqueId();
        plugin.getTrackerKeeper().getInVortex().add(id);
        plugin.getTrackerKeeper().getDidDematToVortex().add(id);
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            boolean hidden = tardis.isHidden();
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
            String resetw = "";
            Location l = null;
            if (!rscl.resultSet()) {
                hidden = true;
            } else {
                resetw = rscl.getWorld().getName();
                l = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                // set back to current location
                HashMap<String, Object> bid = new HashMap<>();
                bid.put("tardis_id", id);
                HashMap<String, Object> bset = new HashMap<>();
                bset.put("world", rscl.getWorld().getName());
                bset.put("x", rscl.getX());
                bset.put("y", rscl.getY());
                bset.put("z", rscl.getZ());
                bset.put("direction", rscl.getDirection().toString());
                bset.put("submarine", rscl.isSubmarine());
                plugin.getQueryFactory().doUpdate("back", bset, bid);
            }
            CardinalDirection cd = rscl.getDirection();
            boolean sub = rscl.isSubmarine();
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            boolean minecart = false;
            SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.NORMAL;
            if (rsp.resultSet()) {
                minecart = rsp.isMinecartOn();
                spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
            }
            DestroyData dd = new DestroyData();
            dd.setDirection(cd);
            dd.setLocation(l);
            dd.setPlayer(player);
            dd.setHide(false);
            dd.setOutside(false);
            dd.setSubmarine(sub);
            dd.setTardisId(id);
            dd.setThrottle(spaceTimeThrottle);
            Preset preset = tardis.getPreset();
            if (preset.equals(Preset.JUNK_MODE)) {
                HashMap<String, Object> wherenl = new HashMap<>();
                wherenl.put("tardis_id", id);
                ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, wherenl);
                if (!rsn.resultSet()) {
                    TardisMessage.send(player, "DEST_NO_LOAD");
                    return;
                }
                Location exit = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
                dd.setFromToLocation(exit);
                dd.setThrottle(SpaceTimeThrottle.JUNK);
            }
            plugin.getPluginManager().callEvent(new TardisDematerialisationEvent(player, tardis, l));
            if (!hidden && !plugin.getTrackerKeeper().getResetWorlds().contains(resetw)) {
                // play demat sfx
                if (!minecart) {
                    if (!preset.equals(Preset.JUNK_MODE)) {
                        String sound;
                        if (plugin.getTrackerKeeper().getMalfunction().get(id) && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                            sound = "tardis_malfunction_takeoff";
                        } else {
                            sound = switch (spaceTimeThrottle) {
                                case WARP -> "tardis_takeoff_warp";
                                case RAPID -> "tardis_takeoff_rapid";
                                case FASTER -> "tardis_takeoff_faster";
                                default -> // NORMAL
                                        "tardis_takeoff";
                            };
                        }
                        TardisSounds.playTardisSound(handbrake, sound);
                        TardisSounds.playTardisSound(l, sound);
                    } else {
                        TardisSounds.playTardisSound(handbrake, "junk_takeoff");
                    }
                } else {
                    Objects.requireNonNull(handbrake.getWorld()).playSound(handbrake, Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                }
                plugin.getTrackerKeeper().getDematerialising().add(id);
                plugin.getPresetDestroyer().destroyPreset(dd);
            } else {
                // set hidden false!
                HashMap<String, Object> set = new HashMap<>();
                set.put("hidden", 0);
                HashMap<String, Object> whereh = new HashMap<>();
                whereh.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("tardis", set, whereh);
                plugin.getPresetDestroyer().removeBlockProtection(id);
            }
        }
    }
}
