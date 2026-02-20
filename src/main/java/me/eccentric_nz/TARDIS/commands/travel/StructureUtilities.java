package me.eccentric_nz.TARDIS.commands.travel;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.CircuitChecker;
import me.eccentric_nz.TARDIS.advanced.CircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.travel.TARDISStructureTravel;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.HashMap;
import java.util.List;

public class StructureUtilities {

    private static Location getCurrentLocation(TARDIS plugin, Player player, int id) {
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
        return current.location();
    }

    private static final List<Structure> VILLAGES = List.of(Structure.VILLAGE_DESERT, Structure.VILLAGE_PLAINS, Structure.VILLAGE_SAVANNA, Structure.VILLAGE_SNOWY, Structure.VILLAGE_TAIGA);

    public static void randomVillage(TARDIS plugin, Player player, int id) {
        if (doChecks(plugin, player, id)) {
            return;
        }
        Location current = getCurrentLocation(plugin, player, id);
        if (current == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return;
        }
        World.Environment env = current.getWorld().getEnvironment();
        if (!env.equals(World.Environment.NORMAL)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", "village", (env.equals(World.Environment.THE_END) ? "" : "a ") + TARDISStringUtils.capitalise(env.toString()));
            return;
        }
        // choose a random village type
        Structure village = VILLAGES.get(TARDISConstants.RANDOM.nextInt(VILLAGES.size()));
        StructureSearchResult villageResult = current.getWorld().locateNearestStructure(current, village, 64, false);
        Location loc = (villageResult != null) ? villageResult.getLocation() : null;
        if (loc == null) {
            return;
        }
        set(plugin, loc, player, id);
    }

    private static final List<Structure> STRUCTURES = List.of(
            Structure.ANCIENT_CITY, Structure.BASTION_REMNANT, Structure.BURIED_TREASURE, Structure.DESERT_PYRAMID,
            Structure.END_CITY, Structure.FORTRESS, Structure.IGLOO, Structure.JUNGLE_PYRAMID, Structure.MANSION,
            Structure.MINESHAFT, Structure.MINESHAFT_MESA, Structure.MONUMENT, Structure.NETHER_FOSSIL, Structure.OCEAN_RUIN_COLD,
            Structure.OCEAN_RUIN_WARM, Structure.PILLAGER_OUTPOST, Structure.RUINED_PORTAL, Structure.RUINED_PORTAL_DESERT,
            Structure.RUINED_PORTAL_JUNGLE, Structure.RUINED_PORTAL_MOUNTAIN, Structure.RUINED_PORTAL_NETHER,
            Structure.RUINED_PORTAL_OCEAN, Structure.RUINED_PORTAL_SWAMP, Structure.SHIPWRECK, Structure.SHIPWRECK_BEACHED,
            Structure.STRONGHOLD, Structure.SWAMP_HUT, Structure.TRAIL_RUINS, Structure.TRIAL_CHAMBERS
    );

    public static void randomStructure(TARDIS plugin, Player player, int id) {
        if (doChecks(plugin, player, id)) {
            return;
        }
        Location current = getCurrentLocation(plugin, player, id);
        if (current == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return;
        }
        // choose a random structure type
        Structure structure = STRUCTURES.get(TARDISConstants.RANDOM.nextInt(STRUCTURES.size()));
        if (validate(plugin, player, structure, current)) {
            return;
        }
        StructureSearchResult structureResult = current.getWorld().locateNearestStructure(current, structure, 64, false);
        Location loc = (structureResult != null) ? structureResult.getLocation() : null;
        if (loc == null) {
            return;
        }
        set(plugin, loc, player, id);
    }

    public static void search(TARDIS plugin, Player player, Structure structure, int id) {
        if (doChecks(plugin, player, id)) {
            return;
        }
        Location current = getCurrentLocation(plugin, player, id);
        if (current == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return;
        }
        if (validate(plugin, player, structure, current)) {
            return;
        }
        StructureSearchResult structureResult = current.getWorld().locateNearestStructure(current, structure, 64, false);
        Location loc = (structureResult != null) ? structureResult.getLocation() : null;
        if (loc == null) {
            return;
        }
        set(plugin, loc, player, id);
    }

    private static boolean doChecks(TARDIS plugin, Player player, int id) {
        if (!plugin.getConfig().getBoolean("allow.village_travel")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_VILLAGE");
            return true;
        }
        if (!TARDISPermission.hasPermission(player, "tardis.timetravel.village")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_NO_PERM_VILLAGE");
            return true;
        }
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.TELEPATHIC_CIRCUIT)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Telepathic Circuit");
            return true;
        }
        CircuitChecker tcc = new CircuitChecker(plugin, id);
        tcc.getCircuits();
        // check for telepathic circuit
        if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true) && !tcc.hasTelepathic()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TELEPATHIC_CIRCUIT");
            return true;
        }
        // damage circuit if configured
        if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.telepathic") > 0) {
            // decrement uses
            int uses_left = tcc.getTelepathicUses();
            new CircuitDamager(plugin, DiskCircuit.TELEPATHIC, uses_left, id, player).damage();
        }
        return false;
    }

    private static boolean validate(TARDIS plugin, Player player, Structure structure, Location current) {
        String key = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).getKey(structure).getKey();
        World.Environment env = current.getWorld().getEnvironment();
        // check structure arg is appropriate for the world environment
        if (!env.equals(World.Environment.NETHER) && TARDISStructureTravel.netherStructures.contains(structure)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", key, (env.equals(World.Environment.THE_END) ? "" : "a ") + TARDISStringUtils.capitalise(env.toString()));
            return true;
        }
        if (!env.equals(World.Environment.THE_END) && structure.equals(Structure.END_CITY)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", key, "a " + TARDISStringUtils.capitalise(env.toString()));
            return true;
        }
        if (!env.equals(World.Environment.NORMAL) && TARDISStructureTravel.overworldStructures.contains(structure)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", key, (env.equals(World.Environment.THE_END) ? "" : "a ") + TARDISStringUtils.capitalise(env.toString()));
            return true;
        }
        return false;
    }

    private static void set(TARDIS plugin, Location loc, Player player, int id) {
        // check for space
        Block b = loc.getBlock();
        boolean unsafe = true;
        while (unsafe) {
            boolean clear = true;
            for (BlockFace f : plugin.getGeneralKeeper().getSurrounding()) {
                if (!TARDISConstants.GOOD_MATERIALS.contains(b.getRelative(f).getType())) {
                    b = b.getRelative(BlockFace.UP);
                    clear = false;
                    break;
                }
            }
            unsafe = !clear;
        }
        loc.setY(b.getY());
        // check respect
        if (!plugin.getPluginRespect().getRespect(loc, new Parameters(player, Flag.getDefaultFlags()))) {
            if (plugin.getConfig().getBoolean("travel.no_destination_malfunctions")) {
                plugin.getTrackerKeeper().getMalfunction().put(id, true);
            } else {
                return;
            }
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", loc.getWorld().getName());
        set.put("x", loc.getBlockX());
        set.put("y", loc.getBlockY());
        set.put("z", loc.getBlockZ());
        set.put("submarine", 0);
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_LOADED", "village", !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
        TravelType travelType = switch (loc.getWorld().getEnvironment()) {
            case THE_END -> TravelType.VILLAGE_THE_END;
            case NETHER -> TravelType.VILLAGE_NETHER;
            default -> TravelType.VILLAGE_OVERWORLD;
        };
        plugin.getTrackerKeeper().getHasDestination().put(id, new TravelCostAndType(plugin.getArtronConfig().getInt("travel"), travelType));
        plugin.getTrackerKeeper().getRescue().remove(id);
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            new TARDISLand(plugin, id, player).exitVortex();
            plugin.getPM().callEvent(new TARDISTravelEvent(player, null, travelType, id));
        }
    }
}
