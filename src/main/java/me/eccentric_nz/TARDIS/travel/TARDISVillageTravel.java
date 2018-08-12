/*
 * Copyright (C) 2018 eccentric_nz
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
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

/**
 * @author eccentric_nz
 */
public class TARDISVillageTravel {

    private final TARDIS plugin;
    private final Random rand = new Random();

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
            Environment env = world.getEnvironment();
            if (env.equals(Environment.NETHER)) {
                TARDISMessage.send(p, "VILLAGE_NO_NETHER");
                return null;
            }
            if (env.equals(Environment.THE_END)) {
                TARDISMessage.send(p, "VILLAGE_NO_END");
                return null;
            }
            Location loc = plugin.getTardisHelper().getRandomVillage(world);
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
