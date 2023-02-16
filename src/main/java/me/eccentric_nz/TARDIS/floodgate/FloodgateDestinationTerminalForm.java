package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
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
                .validResultHandler(response -> handleResponse(response))
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(CustomFormResponse response) {
        // TODO
        Player player = plugin.getServer().getPlayer(uuid);
        String world = worlds.get(response.asDropdown(3));
        // check player hs permission for world
        if (plugin.getConfig().getBoolean("travel.per_world_perms") && !TARDISPermission.hasPermission(player, "tardis.travel." + world)) {
            TARDISMessage.send(player, "TRAVEL_NO_PERM_WORLD", world);
            return;
        }
        World w = plugin.getServer().getWorld(world);
        if (w != null) {
            World.Environment e = w.getEnvironment();
            // if nether or the end check if travel is enabled there
            if (e.equals(World.Environment.NETHER) && !plugin.getConfig().getBoolean("travel.nether")) {
                TARDISMessage.send(player, "TRAVEL_DISABLED", "Nether");
                return;
            }
            if (e.equals(World.Environment.THE_END) && !plugin.getConfig().getBoolean("travel.the_end")) {
                TARDISMessage.send(player, "TRAVEL_DISABLED", "The End");
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
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wheret);
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
                                        TARDISMessage.send(player, "LOC_SET");
                                    } else {
                                        TARDISMessage.send(player, "PROTECTED");
                                    }
                                } else {
                                    TARDISMessage.send(player, "NOT_SAFE");
                                }
                            } else {
                                TARDISMessage.send(player, "NOT_SAFE");
                            }
                        }
                        case NETHER -> {
                            if (tt.safeNether(w, x, z, rsc.getDirection(), player)) {
                                String save = world + ":" + x + ":" + plugin.getUtils().getHighestNetherBlock(w, x, z) + ":" + z;
                                location = new Location(w, x, plugin.getUtils().getHighestNetherBlock(w, x, z), z);
                                TARDISMessage.send(player, "LOC_SET");
                            } else {
                                TARDISMessage.send(player, "NOT_SAFE");
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
                                    TARDISMessage.send(player, "LOC_SET");
                                } else {
                                    TARDISMessage.send(player, "PROTECTED");
                                }
                            } else {
                                TARDISMessage.send(player, "NOT_SAFE");
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
                        TARDISMessage.send(player, "DEST_SET", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            new TARDISLand(plugin, id, player).exitVortex();
                            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.TERMINAL, id));
                        }
                        // damage the circuit if configured
                        if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.input") > 0) {
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
