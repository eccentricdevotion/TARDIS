/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.travel;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.TARDISAreaCheck;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelSave {

    private final TARDIS plugin;

    public TARDISTravelSave(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, String[] args, int id, ChameleonPreset preset) {
        // we're thinking this is a saved destination name
        if (TARDISPermission.hasPermission(player, "tardis.save")) {
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("dest_name", args[1]);
            whered.put("tardis_id", id);
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
            if (!rsd.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NOT_FOUND");
                return true;
            }
            World w = TARDISAliasResolver.getWorldFromAlias(rsd.getWorld());
            if (w != null) {
                if (w.getName().startsWith("TARDIS_")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NO_TARDIS");
                    return true;
                }
                Location save_dest = new Location(w, rsd.getX(), rsd.getY(), rsd.getZ());
                if (!plugin.getPluginRespect().getRespect(save_dest, new Parameters(player, Flag.getDefaultFlags()))) {
                    if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                        plugin.getTrackerKeeper().getMalfunction().put(id, true);
                    } else {
                        return true;
                    }
                }
                TARDISAreaCheck tac = plugin.getTardisArea().isSaveInArea(save_dest);
                if (tac.isInArea()) {
                    // save is in a TARDIS area, so check that the spot is not occupied
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("world", rsd.getWorld());
                    wheres.put("x", rsd.getX());
                    wheres.put("y", rsd.getY());
                    wheres.put("z", rsd.getZ());
                    ResultSetCurrentLocation rsz = new ResultSetCurrentLocation(plugin, wheres);
                    if (rsz.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_IN_SPOT", ChatColor.AQUA + "/tardistravel area [name]" + ChatColor.RESET);
                        return true;
                    }
                    String invisibility = tac.getArea().getInvisibility();
                    if (invisibility.equals("DENY") && preset.equals(ChameleonPreset.INVISIBLE)) {
                        // check preset
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NO_INVISIBLE");
                        return true;
                    } else if (!invisibility.equals("ALLOW")) {
                        // force preset
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_FORCE_PRESET", invisibility);
                        HashMap<String, Object> wherei = new HashMap<>();
                        wherei.put("tardis_id", id);
                        HashMap<String, Object> seti = new HashMap<>();
                        seti.put("chameleon_preset", invisibility);
                        // set chameleon adaption to OFF
                        seti.put("adapti_on", 0);
                        plugin.getQueryFactory().doSyncUpdate("tardis", seti, wherei);
                    }
                }
                HashMap<String, Object> set = new HashMap<>();
                set.put("world", rsd.getWorld());
                set.put("x", rsd.getX());
                set.put("y", rsd.getY());
                set.put("z", rsd.getZ());
                if (!rsd.getDirection().isEmpty() && rsd.getDirection().length() < 6) {
                    set.put("direction", rsd.getDirection());
                } else {
                    // get current direction
                    HashMap<String, Object> wherecl = new HashMap<>();
                    wherecl.put("tardis_id", id);
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                        return true;
                    }
                    set.put("direction", rsc.getDirection().toString());
                }
                set.put("submarine", (rsd.isSubmarine()) ? 1 : 0);
                if (!rsd.getPreset().isEmpty()) {
                    // set the chameleon preset
                    HashMap<String, Object> seti = new HashMap<>();
                    seti.put("chameleon_preset", rsd.getPreset());
                    // set chameleon adaption to OFF
                    seti.put("adapti_on", 0);
                    HashMap<String, Object> wherei = new HashMap<>();
                    wherei.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("tardis", seti, wherei);
                }
                HashMap<String, Object> tid = new HashMap<>();
                tid.put("tardis_id", id);
                plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "LOC_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.SAVE));
                plugin.getTrackerKeeper().getRescue().remove(id);
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                    new TARDISLand(plugin, id, player).exitVortex();
                    plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.SAVE, id));
                }
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NO_WORLD");
            }
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_PERM_SAVE");
            return true;
        }
    }
}
