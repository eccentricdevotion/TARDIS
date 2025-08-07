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
package me.eccentric_nz.TARDIS.travel;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author eccentric_nz
 */
public class TARDISStructureTravel {

    public static final List<Structure> netherStructures = new ArrayList<>();
    public static final List<Structure> overworldStructures = new ArrayList<>();

    static {
        netherStructures.add(Structure.BASTION_REMNANT);
        netherStructures.add(Structure.FORTRESS);
        netherStructures.add(Structure.NETHER_FOSSIL);
        netherStructures.add(Structure.RUINED_PORTAL_NETHER);
        overworldStructures.add(Structure.ANCIENT_CITY);
        overworldStructures.add(Structure.DESERT_PYRAMID);
        overworldStructures.add(Structure.IGLOO);
        overworldStructures.add(Structure.JUNGLE_PYRAMID);
        overworldStructures.add(Structure.MANSION);
        overworldStructures.add(Structure.MINESHAFT);
        overworldStructures.add(Structure.MINESHAFT_MESA);
        overworldStructures.add(Structure.MONUMENT);
        overworldStructures.add(Structure.OCEAN_RUIN_COLD);
        overworldStructures.add(Structure.OCEAN_RUIN_WARM);
        overworldStructures.add(Structure.PILLAGER_OUTPOST);
        overworldStructures.add(Structure.RUINED_PORTAL);
        overworldStructures.add(Structure.RUINED_PORTAL_DESERT);
        overworldStructures.add(Structure.RUINED_PORTAL_JUNGLE);
        overworldStructures.add(Structure.RUINED_PORTAL_SWAMP);
        overworldStructures.add(Structure.RUINED_PORTAL_MOUNTAIN);
        overworldStructures.add(Structure.RUINED_PORTAL_OCEAN);
        overworldStructures.add(Structure.SHIPWRECK);
        overworldStructures.add(Structure.SHIPWRECK_BEACHED);
        overworldStructures.add(Structure.STRONGHOLD);
        overworldStructures.add(Structure.SWAMP_HUT);
        overworldStructures.add(Structure.TRAIL_RUINS);
        overworldStructures.add(Structure.VILLAGE_DESERT);
        overworldStructures.add(Structure.VILLAGE_PLAINS);
        overworldStructures.add(Structure.VILLAGE_SAVANNA);
        overworldStructures.add(Structure.VILLAGE_SNOWY);
        overworldStructures.add(Structure.VILLAGE_TAIGA);
    }

    private final TARDIS plugin;

    public TARDISStructureTravel(TARDIS plugin) {
        this.plugin = plugin;
    }

    public TARDISStructureLocation getRandom(Player p, int id, String[] args) {
        // get world the Police Box is in
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            Current current = rsc.getCurrent();
            World world = current.location().getWorld();
            Environment env = world.getEnvironment();
            Structure structure = null;
            if (args.length > 1) {
                // check it is a valid structure type
                structure = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).get(NamespacedKey.minecraft(args[1].toLowerCase(Locale.ROOT)));
                if (structure == null) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "VILLAGE_NO_STRUCTURE", args[1]);
                    return null;
                }
                // check structure travel permission
                String perm = RegistryAccess.registryAccess().getRegistry(RegistryKey.STRUCTURE).getKey(structure).getKey();
                if (!p.hasPermission("tardis.timetravel.structure." + perm)) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "NO_PERM_STRUCTURE", TARDISStringUtils.capitalise(perm));
                    return null;
                }
                // check structure arg is appropriate for the world environment
                if (!env.equals(Environment.NETHER) && netherStructures.contains(structure)) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", args[1], (env.equals(Environment.THE_END) ? "" : "a ") + TARDISStringUtils.capitalise(env.toString()));
                    return null;
                }
                if (!env.equals(Environment.THE_END) && structure.equals(Structure.END_CITY)) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", args[1], "a " + TARDISStringUtils.capitalise(env.toString()));
                    return null;
                }
                if (!env.equals(Environment.NORMAL) && overworldStructures.contains(structure)) {
                    plugin.getMessenger().send(p, TardisModule.TARDIS, "VILLAGE_NO_SEARCH", args[1], (env.equals(Environment.THE_END) ? "" : "a ") + TARDISStringUtils.capitalise(env.toString()));
                    return null;
                }
            }
            Location loc = null;
            switch (env) {
                case NETHER -> {
                    if (args.length < 2) {
                        // choose a random Nether structure - FORTRESS, BASTION_REMNANT
                        structure = netherStructures.get(ThreadLocalRandom.current().nextInt(netherStructures.size()));
                    }
                    StructureSearchResult netherResult = world.locateNearestStructure(current.location(), structure, 64, false);
                    loc = (netherResult != null) ? netherResult.getLocation() : null;
                }
                case THE_END -> {
                    StructureSearchResult endResult = world.locateNearestStructure(current.location(), Structure.END_CITY, 64, false);
                    if (endResult != null) {
                        loc = endResult.getLocation();
                        int highesty = TARDISStaticLocationGetters.getHighestYin3x3(world, current.location().getBlockX(), current.location().getBlockZ());
                        loc.setY(highesty);
                    }
                }
                // NORMAL
                default -> {
                    if (args.length < 2) {
                        // choose a random village structure - ANCIENT_CITY, VILLAGE_DESERT, VILLAGE_PLAINS, VILLAGE_SAVANNA, VILLAGE_SNOWY, VILLAGE_TAIGA, MANSION, JUNGLE_PYRAMID, DESERT_PYRAMID, IGLOO, SWAMP_HUT
                        structure = overworldStructures.get(ThreadLocalRandom.current().nextInt(overworldStructures.size()));
                    }
                    StructureSearchResult normalResult = world.locateNearestStructure(current.location(), structure, 64, false);
                    loc = (normalResult != null) ? normalResult.getLocation() : null;
                    // if ANCIENT_CITY, get underground location
                    if (loc != null) {
                        if (structure.equals(Structure.ANCIENT_CITY)) {
                            Check check = isThereRoom(world, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                            if (check.isSafe()) {
                                loc.setY(check.getY());
                            }
                        } else {
                            loc.setY(world.getHighestBlockYAt(loc));
                        }
                    }
                }
            }
            if (loc == null) {
                return null;
            }
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
            return new TARDISStructureLocation(plugin, loc, structure);
        } else {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return null;
        }
    }

    private Check isThereRoom(World w, int x, int starty, int z) {
        Check ret = new Check();
        ret.setSafe(false);
        for (int y = starty; y > w.getMinHeight() + 14; y--) {
            if (w.getBlockAt(x, y, z).getType().isAir()) {
                int yy = TARDISCaveFinder.getLowestAirBlock(w, x, y, z);
                // check there is enough height for the police box
                if (yy <= y - 3 && !w.getBlockAt(x - 1, yy - 1, z - 1).getType().equals(Material.STONE)) {
                    // check there is room for the police box
                    if (w.getBlockAt(x - 1, yy, z - 1).getType().isAir() && w.getBlockAt(x - 1, yy, z).getType().isAir() && w.getBlockAt(x - 1, yy, z + 1).getType().isAir() && w.getBlockAt(x, yy, z - 1).getType().isAir() && w.getBlockAt(x, yy, z + 1).getType().isAir() && w.getBlockAt(x + 1, yy, z - 1).getType().isAir() && w.getBlockAt(x + 1, yy, z).getType().isAir() && w.getBlockAt(x + 1, yy, z + 1).getType().isAir()) {
                        ret.setSafe(true);
                        ret.setY(yy);
                    }
                }
            }
        }
        return ret;
    }
}
