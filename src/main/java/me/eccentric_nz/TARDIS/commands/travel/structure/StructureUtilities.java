package me.eccentric_nz.TARDIS.commands.travel.structure;

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
import me.eccentric_nz.TARDIS.travel.TARDISStructure;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.HashMap;
import java.util.List;

public class StructureUtilities {

    private static Current getCurrentLocation(TARDIS plugin, Player player, int id) {
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
        if (!plugin.getPlanetsConfig().getBoolean("planets." + world.getKey().getKey() + ".time_travel")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
            return null;
        }
        return current;
    }

    private static final List<Structure> VILLAGES = List.of(Structure.VILLAGE_DESERT, Structure.VILLAGE_PLAINS, Structure.VILLAGE_SAVANNA, Structure.VILLAGE_SNOWY, Structure.VILLAGE_TAIGA);

    public static void randomVillage(TARDIS plugin, Player player, int id) {
        if (doChecks(plugin, player, id)) {
            return;
        }
        Current current = getCurrentLocation(plugin, player, id);
        if (current == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return;
        }
        World.Environment env = current.location().getWorld().getEnvironment();
        if (!env.equals(World.Environment.NORMAL)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", "village", (env.equals(World.Environment.THE_END) ? "" : "a ") + TARDISStringUtils.capitalise(env.toString()));
            return;
        }
        // choose a random village type
        Structure village = VILLAGES.get(TARDISConstants.RANDOM.nextInt(VILLAGES.size()));
        StructureSearchResult villageResult = current.location().getWorld().locateNearestStructure(current.location(), village, 64, false);
        Location loc = (villageResult != null) ? villageResult.getLocation() : null;
        if (loc == null) {
            return;
        }
        set(plugin, loc, player, id, village);
    }

    public static void randomStructure(TARDIS plugin, Player player, int id) {
        if (doChecks(plugin, player, id)) {
            return;
        }
        Current current = getCurrentLocation(plugin, player, id);
        if (current == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return;
        }
        // choose a random structure type
        Structure structure = TARDISStructure.getRandom(current.location());
        if (validate(plugin, player, structure, current.location())) {
            return;
        }
        StructureSearchResult structureResult = current.location().getWorld().locateNearestStructure(current.location(), structure, 64, false);
        Location loc = (structureResult != null) ? structureResult.getLocation() : null;
        if (loc == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NOT_FOUND");
            return;
        }
        set(plugin, loc, player, id, structure);
    }

    public static void search(TARDIS plugin, Player player, Structure structure, int id) {
        if (doChecks(plugin, player, id)) {
            return;
        }
        Current current = getCurrentLocation(plugin, player, id);
        if (current == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return;
        }
        if (validate(plugin, player, structure, current.location())) {
            return;
        }
        StructureSearchResult structureResult = current.location().getWorld().locateNearestStructure(current.location(), structure, 64, false);
        if (structureResult != null) {
            String perm = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).getKey(structure).getKey();
            if (isUnderground(perm)) {
                Limit limits = getLimits(perm);
                AsyncStructureFinder.getSafeLocation(structureResult.getLocation(), current.direction(), getStructureMaterial(perm), limits.min(), limits.max())
                        .thenAccept(optionalLocation -> optionalLocation.ifPresentOrElse(
                                value -> set(plugin, value, player, id, structure),
                                () -> plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NOT_FOUND")));
            } else {
                set(plugin, structureResult.getLocation(), player, id, structure);
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NOT_FOUND");
        }
    }

    private static Material getStructureMaterial(String structure) {
        switch (structure) {
            case "ancient_city" -> {
                return Material.DEEPSLATE_BRICKS;
            }
            case "stronghold" -> {
                return Material.STONE_BRICKS;
            }
            case "trial_chambers" -> {
                return Material.WAXED_OXIDIZED_COPPER;
            }
            case "bastion_remnant" -> {
                return Material.POLISHED_BLACKSTONE_BRICKS;
            }
            case "fortress" -> {
                return Material.NETHER_BRICKS;
            }
            case "nether_fossil" -> {
                return Material.SOUL_SAND;
            }
            default -> {
                return Material.NETHERRACK;
            }
        }
    }

    private static boolean isUnderground(String structure) {
        switch (structure) {
            case "ancient_city", "stronghold", "trial_chambers", "bastion_remnant", "fortress", "nether_fossil" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private static Limit getLimits(String structure) {
        switch (structure) {
            case "ancient_city" -> {
                return new Limit(-51, -41);
            }
            case "stronghold" -> {
                return new Limit(-40, 16);
            }
            case "trial_chambers" -> {
                return new Limit(-41, 32);
            }
            case "bastion_remnant" -> {
                return new Limit(32, 65);
            }
            case "fortress" -> {
                return new Limit(60, 85);
            }
            case "nether_fossil" -> {
                return new Limit(40, 96);
            }
            default -> {
                return new Limit(0, 10);
            }
        }
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
        if (!env.equals(World.Environment.NETHER) && TARDISStructure.netherStructures.containsKey(structure)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", key, (env.equals(World.Environment.THE_END) ? "" : "a ") + TARDISStringUtils.capitalise(env.toString()));
            return true;
        }
        if (!env.equals(World.Environment.THE_END) && structure.equals(Structure.END_CITY)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", key, "a " + TARDISStringUtils.capitalise(env.toString()));
            return true;
        }
        if (!env.equals(World.Environment.NORMAL) && TARDISStructure.overworldStructures.containsKey(structure)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", key, (env.equals(World.Environment.THE_END) ? "" : "a ") + TARDISStringUtils.capitalise(env.toString()));
            return true;
        }
        return false;
    }

    private static void set(TARDIS plugin, Location loc, Player player, int id, Structure structure) {
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
        set.put("world", loc.getWorld().getKey().asString());
        set.put("x", loc.getBlockX());
        set.put("y", loc.getBlockY());
        set.put("z", loc.getBlockZ());
        set.put("submarine", 0);
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        plugin.getQueryFactory().doSyncUpdate("next", set, tid);
        String which = getWhich(structure);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_LOADED", which, !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
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

    public static String getWhich(Structure which) {
        String s = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).getKey(which).getKey();
        if (s.startsWith("ocean_ruin_") || s.startsWith("ruined_portal_") || s.startsWith("village_")) {
            return TARDISStringUtils.switchCapitalise(s);
        } else if (which.equals(Structure.FORTRESS)) {
            return "Nether Fortress";
        } else {
            return TARDISStringUtils.capitalise(s);
        }
    }
}
