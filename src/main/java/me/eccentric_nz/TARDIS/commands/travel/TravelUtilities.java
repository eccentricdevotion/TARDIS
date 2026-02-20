package me.eccentric_nz.TARDIS.commands.travel;

import com.mojang.datafixers.util.Pair;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TravelUtilities {

    public static int getId(TARDIS plugin, Player player) {
        // get tardis data
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
            return -1;
        }
        Tardis tardis = rs.getTardis();
        return tardis.getTardisId();
    }

    public static Pair<Integer, ChameleonPreset> getIdAndPreset(TARDIS plugin, Player player) {
        // get tardis data
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
            return null;
        }
        Tardis tardis = rs.getTardis();
        return new Pair<>(tardis.getTardisId(), tardis.getPreset());
    }

    public static void listBiomes(TARDIS plugin, Player player) {
        StringBuilder buf = new StringBuilder();
        for (Biome b : RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME)) {
            if (!b.equals(Biome.THE_VOID)) {
                buf.append(b.getKey().getKey().toUpperCase(Locale.ROOT)).append(", ");
            }
        }
        String b = buf.substring(0, buf.length() - 2);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "BIOMES", b);
    }

    public static void random(TARDIS plugin, Player player, World world, int id) {
        // check world is an actual world
        if (world == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "COULD_NOT_FIND_WORLD");
            return;
        }
        // check world is enabled for travel
        if (!plugin.getPlanetsConfig().getBoolean("planets." + world.getName() + ".time_travel")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
            return;
        }
        // only world specified
        List<String> worlds = List.of(world.getName());
        // get current location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            Parameters parameters = new Parameters(player, Flag.getNoMessageFlags());
            parameters.setCompass(rsc.getCurrent().direction());
            Location l = plugin.getTardisAPI().getRandomLocation(worlds, world.getEnvironment(), parameters);
            if (l != null) {
                HashMap<String, Object> set = new HashMap<>();
                set.put("world", l.getWorld().getName());
                set.put("x", l.getBlockX());
                set.put("y", l.getBlockY());
                set.put("z", l.getBlockZ());
                set.put("submarine", 0);
                HashMap<String, Object> tid = new HashMap<>();
                tid.put("tardis_id", id);
                plugin.getQueryFactory().doSyncUpdate("next", set, tid);
                plugin.getMessenger().send(player, "LOC_SAVED", true);
                plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.RANDOM));
                plugin.getTrackerKeeper().getRescue().remove(id);
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                    new TARDISLand(plugin, id, player).exitVortex();
                    plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.RANDOM, id));
                }
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
        }
    }

    public static void coords(TARDIS plugin, Player player, World world, BlockPosition pos, int id) {
        if (world == null) {
            world = getCurrentWorld(plugin, player, id);
        }
        // is it still null?
        if (world == null) {
            return;
        }
        Location location = new Location(world, pos.blockX(), pos.blockY(), pos.blockZ());
        // check location
        int count = checkLocation(plugin, location, player, id);
        if (count > 0) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_SAFE");
        } else {
            HashMap<String, Object> set = new HashMap<>();
            set.put("world", location.getWorld().getName());
            set.put("x", location.getBlockX());
            set.put("y", location.getBlockY());
            set.put("z", location.getBlockZ());
            set.put("submarine", 0);
            HashMap<String, Object> tid = new HashMap<>();
            tid.put("tardis_id", id);
            plugin.getQueryFactory().doSyncUpdate("next", set, tid);
            plugin.getMessenger().send(player, "LOC_SAVED", true);
            plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), TravelType.COORDINATES));
            plugin.getTrackerKeeper().getRescue().remove(id);
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                new TARDISLand(plugin, id, player).exitVortex();
                plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.COORDINATES, id));
            }
        }
    }

    private static World getCurrentWorld(TARDIS plugin, Player player, int id) {
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return null;
        }
        Current current = rsc.getCurrent();
        if (current.submarine()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SUB_NO_CMD");
            return null;
        }
        World world = current.location().getWorld();
        if (!plugin.getPlanetsConfig().getBoolean("planets." + world.getName() + ".time_travel")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
            return null;
        }
        return world;
    }

    private static int checkLocation(TARDIS plugin, Location location, Player player, int id) {
        if (location.getWorld().getEnvironment().equals(World.Environment.NETHER) && location.getY() > 127) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NETHER");
            return 1;
        }
        if (plugin.getTardisArea().isInExistingArea(location)) {
            plugin.getMessenger().sendColouredCommand(player, "TRAVEL_IN_AREA", "/tardistravel area [area name]", plugin);
            return 1;
        }
        if (!plugin.getPluginRespect().getRespect(location, new Parameters(player, Flag.getDefaultFlags()))) {
            return 1;
        }
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return 1;
        }
        // check location
        int[] start_loc = TARDISTimeTravel.getStartLocation(location, rsc.getCurrent().direction());
        return TARDISTimeTravel.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), rsc.getCurrent().direction());
    }
}
