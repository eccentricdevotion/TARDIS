/*
 * Copyright (C) 2022 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author eccentric_nz
 */
public class TARDISVillageTravel {

    private final TARDIS plugin;
    private final List<Structure> netherStructures = new ArrayList<>();
    private final List<Structure> villageStructures = new ArrayList<>();

    public TARDISVillageTravel(TARDIS plugin) {
        this.plugin = plugin;
        netherStructures.add(Structure.ANCIENT_CITY);
        netherStructures.add(Structure.BASTION_REMNANT);
        netherStructures.add(Structure.FORTRESS);
        villageStructures.add(Structure.VILLAGE_PLAINS);
        villageStructures.add(Structure.VILLAGE_DESERT);
        villageStructures.add(Structure.VILLAGE_SAVANNA);
        villageStructures.add(Structure.VILLAGE_SNOWY);
        villageStructures.add(Structure.VILLAGE_TAIGA);
    }

    public Location getRandomVillage(Player p, int id) {
        // get world the Police Box is in
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rs = new ResultSetCurrentLocation(plugin, where);
        if (rs.resultSet()) {
            World world = rs.getWorld();
            Location location = new Location(world, rs.getX(), rs.getY(), rs.getZ());
            Environment env = world.getEnvironment();
            Location loc;
            switch (env) {
                case NETHER -> {
                    // choose a random Nether structure - FORTRESS, ANCIENT_CITY, BASTION_REMNANT
                    StructureSearchResult netherResult = world.locateNearestStructure(location, netherStructures.get(ThreadLocalRandom.current().nextInt(netherStructures.size())), 64, false);
                    loc = netherResult.getLocation();
                }
                case THE_END -> {
                    StructureSearchResult endResult = world.locateNearestStructure(location, Structure.END_CITY, 64, false);
                    loc = endResult.getLocation();
                    int highesty = TARDISStaticLocationGetters.getHighestYin3x3(world, rs.getX(), rs.getZ());
                    loc.setY(highesty);
                }
                // NORMAL
                default -> {
                    // choose a random village structure - VILLAGE_DESERT, VILLAGE_PLAINS, VILLAGE_SAVANNA, VILLAGE_SNOWY, VILLAGE_TAIGA
                    StructureSearchResult normalResult = world.locateNearestStructure(location, villageStructures.get(ThreadLocalRandom.current().nextInt(villageStructures.size())), 64, false);
                    loc = normalResult.getLocation();
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
            return loc;
        } else {
            TARDISMessage.send(p, "CURRENT_NOT_FOUND");
            return null;
        }
    }
}
