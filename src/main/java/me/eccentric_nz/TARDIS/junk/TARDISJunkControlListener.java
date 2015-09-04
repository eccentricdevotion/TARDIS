/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.junk;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkControlListener implements Listener {

    private final TARDIS plugin;

    public TARDISJunkControlListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJunkBrakeUse(PlayerInteractEvent event) {
        if (plugin.getTrackerKeeper().isJunkTravelling()) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (blockType == Material.LEVER) {
                // Checks handbrake location against the database.
                final Location handbrake_loc = block.getLocation();
                String hb_loc = handbrake_loc.toString();
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("type", 0);
                where.put("location", hb_loc);
                ResultSetControls rsh = new ResultSetControls(plugin, where, false);
                if (rsh.resultSet()) {
                    int id = rsh.getTardis_id();
                    // is it the Junk TARDIS?
                    HashMap<String, Object> wherei = new HashMap<String, Object>();
                    wherei.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
                    if (rs.resultSet() && rs.getUuid().toString().equals("00000000-aaaa-bbbb-cccc-000000000000")) {
                        final Player player = event.getPlayer();
                        if (!player.hasPermission("tardis.junk")) {
                            TARDISMessage.send(player, "JUNK_NO_PERM");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getJunkDestination() != null) {
                            // get the current location
                            Location junkloc = null;
                            Biome biome = null;
                            HashMap<String, Object> wherecl = new HashMap<String, Object>();
                            wherecl.put("tardis_id", id);
                            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                            if (rsc.resultSet()) {
                                junkloc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                                biome = rsc.getBiome();
                            }
                            if (junkloc == null) {
                                TARDISMessage.send(player, "JUNK_NOT_FOUND");
                                return;
                            }
                            // destroy junk TARDIS
                            final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
                            pdd.setChameleon(false);
                            pdd.setDirection(COMPASS.SOUTH);
                            pdd.setLocation(junkloc);
                            pdd.setDematerialise(true);
                            pdd.setHide(false);
                            pdd.setOutside(false);
                            pdd.setSubmarine(rsc.isSubmarine());
                            pdd.setTardisID(id);
                            pdd.setBiome(biome);
                            plugin.getPresetDestroyer().destroyPreset(pdd);
                            // fly my pretties
                            plugin.getTrackerKeeper().setJunkTravelling(true);
                        } else {
                            TARDISMessage.send(event.getPlayer(), "JUNK_NO_DEST");
                        }
                    }
                }
            }
        }
    }
}
