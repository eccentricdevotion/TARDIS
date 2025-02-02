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
package me.eccentric_nz.TARDIS.flight;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISDematerialisationEvent;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.data.Throticle;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetNextLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISDematerialiseToVortex implements Runnable {

    private final TARDIS plugin;
    private final int id;
    private final Player player;
    private final Location handbrake;
    private final Throticle throticle;

    public TARDISDematerialiseToVortex(TARDIS plugin, int id, Player player, Location handbrake, Throticle throticle) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
        this.handbrake = handbrake;
        this.throticle = throticle;
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
            ResultSetCurrentFromId rscl = new ResultSetCurrentFromId(plugin, id);
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
            COMPASS cd = rscl.getDirection();
            boolean sub = rscl.isSubmarine();
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            boolean minecart = rsp.resultSet() && rsp.isMinecartOn();
            DestroyData dd = new DestroyData();
            dd.setDirection(cd);
            dd.setLocation(l);
            dd.setPlayer(player);
            dd.setHide(false);
            dd.setOutside(false);
            dd.setSubmarine(sub);
            dd.setTardisID(id);
            dd.setThrottle(throticle.getThrottle());
            dd.setParticles(throticle.getParticles());
            ChameleonPreset preset = tardis.getPreset();
            if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, id);
                if (!rsn.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_NO_LOAD");
                    return;
                }
                Location exit = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
                dd.setFromToLocation(exit);
                dd.setThrottle(SpaceTimeThrottle.JUNK);
            }
            plugin.getPM().callEvent(new TARDISDematerialisationEvent(player, tardis, l));
            if (!hidden && !plugin.getTrackerKeeper().getResetWorlds().contains(resetw)) {
                // play demat sfx
                if (!minecart) {
                    if (!preset.equals(ChameleonPreset.JUNK_MODE)) {
                        String sound;
                        if (plugin.getTrackerKeeper().getMalfunction().get(id) && plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                            sound = "tardis_malfunction_takeoff";
                        } else {
                            sound = switch (throticle.getThrottle()) {
                                case WARP -> "tardis_takeoff_warp";
                                case RAPID -> "tardis_takeoff_rapid";
                                case FASTER -> "tardis_takeoff_faster";
                                // NORMAL
                                default -> "tardis_takeoff";
                            };
                        }
                        TARDISSounds.playTARDISSound(handbrake, sound);
                        TARDISSounds.playTARDISSound(l, sound);
                    } else {
                        TARDISSounds.playTARDISSound(handbrake, "junk_takeoff");
                    }
                } else {
                    handbrake.getWorld().playSound(handbrake, Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
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
