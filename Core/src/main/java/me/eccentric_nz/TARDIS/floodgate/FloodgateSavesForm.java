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
package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.TARDISAreaCheck;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.UUID;

public class FloodgateSavesForm {

    private final TARDIS plugin;
    private final String path = "textures/blocks/%s.png";
    private final UUID uuid;
    private final int id;

    public FloodgateSavesForm(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void send() {
        int i = 2;
        // get saves
        HashMap<String, Object> did = new HashMap<>();
        did.put("tardis_id", id);
        ResultSetDestinations rsd = new ResultSetDestinations(plugin, did, true);
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Saves Menu");
        builder.button("Home", FormImage.Type.PATH, "textures/blocks/beehive_front.png");
        if (rsd.resultSet()) {
            for (HashMap<String, String> map : rsd.getData()) {
                builder.button(map.get("dest_name") + " ~ " + map.get("dest_id"), FormImage.Type.PATH, String.format(path, FloodgateColouredBlocks.IMAGES.get(i)));
                i++;
            }
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        Location current = null;
        if (rsc.resultSet()) {
            current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        }
        // get tardis artron level
        ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
        if (!rs.fromID(id)) {
            return;
        }
        int level = rs.getArtronLevel();
        int travel = plugin.getArtronConfig().getInt("travel");
        if (level < travel) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
            return;
        }
        String dest = response.clickedButton().text();
        String[] split = dest.split(" ~ ");
        if (split.length > 1) {
            int dest_id = TARDISNumberParsers.parseInt(split[1]);
            HashMap<String, Object> where = new HashMap<>();
            where.put("dest_id", dest_id);
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, where, false);
            if (rsd.resultSet()) {
                World w = TARDISAliasResolver.getWorldFromAlias(rsd.getWorld());
                if (w == null) {
                    return;
                }
                Location save_dest = new Location(w, rsd.getX(), rsd.getY(), rsd.getZ());
                if (save_dest != null) {
                    if (rsd.getWorld().startsWith("TARDIS_")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NO_TARDIS");
                        return;
                    }
                    // check the player is allowed!
                    if (!plugin.getPluginRespect().getRespect(save_dest, new Parameters(player, Flag.getDefaultFlags()))) {
                        return;
                    }
                    TARDISAreaCheck tac = plugin.getTardisArea().isSaveInArea(save_dest);
                    if (tac.isInArea()) {
                        // save is in a TARDIS area, so check that the spot is not occupied
                        HashMap<String, Object> wheresave = new HashMap<>();
                        wheresave.put("world", rsd.getWorld());
                        wheresave.put("x", rsd.getX());
                        wheresave.put("y", rsd.getY());
                        wheresave.put("z", rsd.getZ());
                        ResultSetCurrentLocation rsz = new ResultSetCurrentLocation(plugin, wheresave);
                        if (rsz.resultSet()) {
                            plugin.getMessenger().sendColouredCommand(player, "TARDIS_IN_SPOT", "/tardistravel area [name]", plugin);
                            return;
                        }
                        String invisibility = tac.getArea().invisibility();
//                        HashMap<String, Object> wheret = new HashMap<>();
//                        wheret.put("tardis_id", id);
//                        ResultSetTardis resultSetTardis = new ResultSetTardis(plugin, wheret, "", false, 2);
//                        if (resultSetTardis.resultSet()) {
                        Tardis tardis = TARDISCache.BY_ID.get(id);
                        if (tardis != null) {
                            if (invisibility.equals("DENY") && tardis.getPreset().equals(ChameleonPreset.INVISIBLE)) {
                                // check preset
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NO_INVISIBLE");
                                return;
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
                                TARDISCache.invalidate(id);
                            }
                        }
                    }
                    if (!save_dest.equals(current) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        // damage circuit if configured
                        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                            // decrement uses
                            int uses_left = tcc.getMemoryUses();
                            new TARDISCircuitDamager(plugin, DiskCircuit.MEMORY, uses_left, id, player).damage();
                        }
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("world", rsd.getWorld());
                        set.put("x", rsd.getX());
                        set.put("y", rsd.getY());
                        set.put("z", rsd.getZ());
                        set.put("direction", rsd.getDirection());
                        set.put("submarine", rsd.isSubmarine() ? 1 : 0);
                        HashMap<String, Object> sett = new HashMap<>();
                        if (!rsd.getPreset().isEmpty()) {
                            sett.put("chameleon_preset", rsd.getPreset());
                            // set chameleon adaption to OFF
                            sett.put("adapti_on", 0);
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("tardis_id", id);
                            plugin.getQueryFactory().doSyncUpdate("tardis", sett, wheret);
                            TARDISCache.invalidate(id);
                        }
                        HashMap<String, Object> whereid = new HashMap<>();
                        whereid.put("tardis_id", id);
                        plugin.getQueryFactory().doSyncUpdate("next", set, whereid);
                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, TravelType.SAVE));
                        plugin.getTrackerKeeper().getRescue().remove(id);
                        plugin.getMessenger().sendJoined(player, "DEST_SET_TERMINAL", split[0], !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            new TARDISLand(plugin, id, player).exitVortex();
                            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.SAVE, id));
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "AT_DEST", split[0]);
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_NOT_VALID", split[0]);
                }
            }
        } else {
            // home
            HashMap<String, Object> wherehl = new HashMap<>();
            wherehl.put("tardis_id", id);
            ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
            if (rsh.resultSet()) {
                Location home = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
                if (!home.equals(current) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("world", rsh.getWorld());
                    set.put("x", rsh.getX());
                    set.put("y", rsh.getY());
                    set.put("z", rsh.getZ());
                    set.put("direction", rsh.getDirection());
                    set.put("submarine", rsh.isSubmarine() ? 1 : 0);
                    HashMap<String, Object> sett = new HashMap<>();
                    sett.put("chameleon_preset", rsh.getPreset());
                    // set chameleon adaption to OFF
                    sett.put("adapti_on", 0);
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("tardis", sett, wheret);
                    TARDISCache.invalidate(id);
                    HashMap<String, Object> whereid = new HashMap<>();
                    wheret.put("tardis_id", id);
                    plugin.getQueryFactory().doSyncUpdate("next", set, whereid);
                    plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(travel, TravelType.HOME));
                    plugin.getTrackerKeeper().getRescue().remove(id);
                    plugin.getMessenger().sendJoined(player, "DEST_SET_TERMINAL", split[0], !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                        new TARDISLand(plugin, id, player).exitVortex();
                        plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.HOME, id));
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AT_HOME");
                }
            }
        }
    }
}
