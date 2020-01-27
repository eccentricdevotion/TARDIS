/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetChunks;
import me.eccentric_nz.TARDIS.database.ResultSetTardisChunk;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISLocationGetters {

    private final TARDIS plugin;

    public TARDISLocationGetters(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the chunk where the interior TARDIS is.
     *
     * @param id the database record id of the TARDIS to get the chunk for
     * @return the TARDIS interior chunk
     */
    public Chunk getTARDISChunk(int id) {
        ResultSetTardisChunk rs = new ResultSetTardisChunk(plugin);
        if (rs.fromID(id)) {
            String c = rs.getChunk();
            String[] data = c.split(":");
            World w = plugin.getServer().getWorld(data[0]);
            int cx = TARDISNumberParsers.parseInt(data[1]);
            int cz = TARDISNumberParsers.parseInt(data[2]);
            return w.getChunkAt(cx, cz);
        }
        return null;
    }

    /**
     * Gets a start location for building the inner TARDIS.
     *
     * @param id the TARDIS this location belongs to.
     * @return an array of ints.
     */
    public int[] getStartLocation(int id) {
        int[] startLoc = new int[4];
        int cx, cz;
        ResultSetTardisChunk rs = new ResultSetTardisChunk(plugin);
        if (rs.fromID(id)) {
            String chunkstr = rs.getChunk();
            String[] split = chunkstr.split(":");
            World w = plugin.getServer().getWorld(split[0]);
            cx = TARDISNumberParsers.parseInt(split[1]);
            cz = TARDISNumberParsers.parseInt(split[2]);
            Chunk chunk = w.getChunkAt(cx, cz);
            startLoc[0] = (chunk.getBlock(0, 64, 0).getX());
            startLoc[1] = startLoc[0];
            startLoc[2] = (chunk.getBlock(0, 64, 0).getZ());
            startLoc[3] = startLoc[2];
        }
        return startLoc;
    }

    /**
     * Checks whether a chunk is available to build a TARDIS in.
     *
     * @param w    the world the chunk is in.
     * @param x    the x coordinate of the chunk.
     * @param z    the z coordinate of the chunk.
     * @param schm the schematic of the TARDIS being created.
     * @return true or false.
     */
    public boolean checkChunk(String w, int x, int z, SCHEMATIC schm) {
        boolean chunkchk = false;
        String directory = (schm.isCustom()) ? "user_schematics" : "schematics";
        String path = plugin.getDataFolder() + File.separator + directory + File.separator + schm.getPermission() + ".tschm";
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JSONObject dimensions = (JSONObject) obj.get("dimensions");
        int wid = dimensions.getInt("width");
        int len = dimensions.getInt("length");
        int cw = TARDISNumberParsers.roundUp(wid, 16);
        int cl = TARDISNumberParsers.roundUp(len, 16);
        // check all the chunks that will be used by the schematic
        for (int cx = 0; cx < cw; cx++) {
            for (int cz = 0; cz < cl; cz++) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("world", w);
                where.put("x", (x + cx));
                where.put("z", (z + cl));
                ResultSetChunks rs = new ResultSetChunks(plugin, where, false);
                if (rs.resultSet()) {
                    chunkchk = true;
                }
            }
        }
        return chunkchk;
    }
}
