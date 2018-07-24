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
import me.eccentric_nz.TARDIS.travel.TARDISNbtFactory.NbtCompound;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            // get path to world data folder
            File container = plugin.getServer().getWorldContainer();
            // check for MCPC+
            Pattern pat = Pattern.compile("MCPC", Pattern.DOTALL);
            Matcher mat = pat.matcher(plugin.getServer().getVersion());
            String server_world;
            if (mat.find()) {
                server_world = "data" + File.separator;
            } else {
                server_world = world.getName() + File.separator + "data" + File.separator;
            }
            String root = container.getAbsolutePath() + File.separator + server_world;
            File file = new File(root, "Village.dat");
            if (!file.exists()) {
                plugin.debug("Could not get Village.dat for world: " + world.getName());
                return null;
            }
            String path = root + "Village.dat";
            try {
                FileInputStream is = new FileInputStream(path);
                NbtCompound nbtc = TARDISNbtFactory.fromCompressedStream(is);
                NbtCompound nbtdata = nbtc.getPath("data");
                NbtCompound nbtf = nbtdata.getPath("Features");
                List<String> nbtl = new ArrayList<>(nbtf.keySet());
                int size = nbtl.size();
                String r = nbtl.get(rand.nextInt(size));
                String[] split = r.substring(1, r.length() - 1).split(",");
                int x = TARDISNumberParsers.parseInt(split[0]) * 16;
                int z = TARDISNumberParsers.parseInt(split[1]) * 16;
                int y = world.getHighestBlockYAt(x, z);
                Location loc = new Location(world, x, y, z);
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
            } catch (IOException ex) {
                plugin.debug("Could not get Village.dat: " + ex.getMessage());
                return null;
            }
        } else {
            TARDISMessage.send(p, "CURRENT_NOT_FOUND");
            return null;
        }
    }
}
