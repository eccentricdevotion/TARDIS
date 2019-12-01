/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISVillageTravel {

    private final TARDIS plugin;

    public TARDISVillageTravel(TARDIS plugin) {
        this.plugin = plugin;
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
                case NETHER:
                    loc = world.locateNearestStructure(location, StructureType.NETHER_FORTRESS, 64, false);
                    break;
                case THE_END:
                    loc = world.locateNearestStructure(location, StructureType.END_CITY, 64, false);
                    int highesty = world.getHighestBlockYAt(loc);
                    loc.setY(highesty);
                    break;
                default: // NORMAL
                    loc = world.locateNearestStructure(location, StructureType.VILLAGE, 64, false);
                    break;
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
