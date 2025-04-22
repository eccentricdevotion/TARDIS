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
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FloodgateDestinationTerminalForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final List<String> worlds;

    public FloodgateDestinationTerminalForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.worlds = this.plugin.getTardisAPI().getWorlds();
    }

    public void send() {
        CustomForm form = CustomForm.builder()
                .title("Destination Terminal")
                .slider("X", -300, 300, 25, 0)
                .slider("Z", -300, 300, 25, 0)
                .slider("Multiplier", 1, 4, 1)
                .dropdown("World", worlds)
                .toggle("Submarine", false)
                .toggle("Just check calculated destination", false)
                .validResultHandler(this::handleResponse)
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(CustomFormResponse response) {
        Player player = plugin.getServer().getPlayer(uuid);
        String world = worlds.get(response.asDropdown(3));
        // check player hs permission for world
        if (plugin.getConfig().getBoolean("travel.per_world_perms") && !TARDISPermission.hasPermission(player, "tardis.travel." + world)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_PERM_WORLD", world);
            return;
        }
        World w = plugin.getServer().getWorld(world);
        if (w != null) {
            World.Environment e = w.getEnvironment();
            // if nether or the end check if travel is enabled there
            if (e.equals(World.Environment.NETHER) && !plugin.getConfig().getBoolean("travel.nether")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_DISABLED", "Nether");
                return;
            }
            if (e.equals(World.Environment.THE_END) && !plugin.getConfig().getBoolean("travel.the_end")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_DISABLED", "The End");
                return;
            }
            // get slider values
            int multiplier = (int) response.asSlider(2);
            int x = (int) response.asSlider(0) * multiplier;
            int z = (int) response.asSlider(1) * multiplier;
            TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
            // get current location
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                if (rsc.resultSet()) {
                    Location location = null;
                    // check location
                    switch (e) {
                        case THE_END -> {
                            int endy = TARDISStaticLocationGetters.getHighestYin3x3(w, x, z);
                            if (endy > 40 && Math.abs(x) > 9 && Math.abs(z) > 9) {
                                Location loc = new Location(w, x, 0, z);
                                int[] estart = TARDISTimeTravel.getStartLocation(loc, rsc.getDirection());
                                int esafe = TARDISTimeTravel.safeLocation(estart[0], endy, estart[2], estart[1], estart[3], w, rsc.getDirection());
                                if (esafe == 0) {
//                                    String save = world + ":" + x + ":" + endy + ":" + z;
                                    loc.setY(endy);
                                    if (plugin.getPluginRespect().getRespect(loc, new Parameters(player, Flag.getNoMessageFlags()))) {
                                        location = loc;
                                        plugin.getMessenger().sendStatus(player, "LOC_SET");
                                    } else {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "PROTECTED");
                                    }
                                } else {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_SAFE");
                                }
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_SAFE");
                            }
                        }
                        case NETHER -> {
                            if (tt.safeNether(w, x, z, rsc.getDirection(), player)) {
                                location = new Location(w, x, plugin.getUtils().getHighestNetherBlock(w, x, z), z);
                                plugin.getMessenger().sendStatus(player, "LOC_SET");
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_SAFE");
                            }
                        }
                        default -> {
                            Location loc = new Location(w, x, 0, z);
                            int[] start = TARDISTimeTravel.getStartLocation(loc, rsc.getDirection());
                            int starty = TARDISStaticLocationGetters.getHighestYin3x3(w, x, z);
                            // allow room for under door block
                            if (starty <= 0) {
                                starty = 1;
                            }
                            int safe;
                            // check submarine
                            loc.setY(starty);
                            if (response.asToggle(4) && TARDISStaticUtils.isOceanBiome(loc.getBlock().getBiome())) {
                                Location subloc = tt.submarine(loc.getBlock(), rsc.getDirection());
                                if (subloc != null) {
                                    safe = 0;
                                    starty = subloc.getBlockY();
                                } else {
                                    safe = 1;
                                }
                            } else {
                                safe = TARDISTimeTravel.safeLocation(start[0], starty, start[2], start[1], start[3], w, rsc.getDirection());
                            }
                            if (safe == 0) {
                                Location over = new Location(w, x, starty, z);
                                if (plugin.getPluginRespect().getRespect(over, new Parameters(player, Flag.getNoMessageFlags()))) {
                                    location = over;
                                    plugin.getMessenger().sendStatus(player, "LOC_SET");
                                } else {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "PROTECTED");
                                }
                            } else {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_SAFE");
                            }
                        }
                    }
                    // if not just checking and location is valid
                    if (!response.asToggle(5) && location != null) {
                        // set destination
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("world", location.getWorld().toString());
                        set.put("x", location.getBlockX());
                        set.put("y", location.getBlockY());
                        set.put("z", location.getBlockZ());
                        set.put("direction", rsc.getDirection());
                        set.put("submarine", (response.asToggle(4)) ? 1 : 0);
                        HashMap<String, Object> wheretid = new HashMap<>();
                        wheretid.put("tardis_id", id);
                        plugin.getQueryFactory().doSyncUpdate("next", set, wheretid);
                        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.TERMINAL));
                        plugin.getTrackerKeeper().getRescue().remove(id);
                        plugin.getMessenger().send(player, "DEST_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            new TARDISLand(plugin, id, player).exitVortex();
                            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.TERMINAL, id));
                        }
                        // damage the circuit if configured
                        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.input") > 0) {
                            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                            // decrement uses
                            int uses_left = tcc.getInputUses();
                            new TARDISCircuitDamager(plugin, DiskCircuit.INPUT, uses_left, id, player).damage();
                        }
                    }
                } else {
                    // emergency TARDIS relocation
                    new TARDISEmergencyRelocation(plugin).relocate(id, player);
                }
            }
        }
    }
}
