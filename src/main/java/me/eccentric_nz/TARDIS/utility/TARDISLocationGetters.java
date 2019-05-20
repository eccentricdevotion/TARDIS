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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetChunks;
import me.eccentric_nz.TARDIS.database.ResultSetTardisChunk;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

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
            String world = (data[0].equals("TARDIS_TimeVortex") ? "tardis_time_vortex" : data[0].toLowerCase(Locale.ENGLISH));
            World w = plugin.getServer().getWorld(world);
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
            String world = (split[0].equals("TARDIS_TimeVortex") ? "tardis_time_vortex" : split[0].toLowerCase(Locale.ENGLISH));
            World w = plugin.getServer().getWorld(world);
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
     * Gets a location object from data stored in the database. This is used when teleporting the player in and out of
     * the TARDIS
     *
     * @param s     the saved location data from the database.
     * @param yaw   the player's yaw.
     * @param pitch the player's pitch.
     * @return a Location.
     */
    public static Location getLocationFromDB(String s, float yaw, float pitch) {
        double savedx, savedy, savedz;
        // compile location from string
        String[] data = s.split(":");
        String world = (data[0].equals("TARDIS_TimeVortex") ? "tardis_time_vortex" : data[0].toLowerCase(Locale.ENGLISH));
        World savedw = Bukkit.getServer().getWorld(world);
        if (savedw == null) {
            return null;
        }
        savedx = TARDISNumberParsers.parseDouble(data[1]);
        savedy = TARDISNumberParsers.parseDouble(data[2]);
        savedz = TARDISNumberParsers.parseDouble(data[3]);
        return new Location(savedw, savedx, savedy, savedz, yaw, pitch);
    }

    /**
     * Gets a location object from data stored in the database.
     *
     * @param string the stored Bukkit location string e.g. Location{world=CraftWorld{name=world},x=0.0,y=0.0,z=0.0,pitch=0.0,yaw=0.0}
     * @return the location or null
     */
    public Location getLocationFromBukkitString(String string) {
        //Location{world=CraftWorld{name=world},x=0.0,y=0.0,z=0.0,pitch=0.0,yaw=0.0}
        String[] loc_data = string.split(",");
        // w, x, y, z - 0, 1, 2, 3
        String[] wStr = loc_data[0].split("=");
        String[] xStr = loc_data[1].split("=");
        String[] yStr = loc_data[2].split("=");
        String[] zStr = loc_data[3].split("=");
        String tmp = wStr[2].substring(0, (wStr[2].length() - 1));
        String world = (tmp.equals("TARDIS_TimeVortex") ? "tardis_time_vortex" : tmp.toLowerCase(Locale.ENGLISH));
        World w = plugin.getServer().getWorld(world);
        if (w == null) {
            return null;
        }
        // Location{world=CraftWorld{name=world},x=1.0000021E7,y=67.0,z=1824.0,pitch=0.0,yaw=0.0}
        double x = (xStr[1].contains("E")) ? Double.valueOf(xStr[1]) : TARDISNumberParsers.parseDouble(xStr[1]);
        double y = TARDISNumberParsers.parseDouble(yStr[1]);
        double z = (zStr[1].contains("E")) ? Double.valueOf(zStr[1]) : TARDISNumberParsers.parseDouble(zStr[1]);
        return new Location(w, x, y, z);
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

    /**
     * Create a TARDIS v2.3 location string from block coordinates.
     *
     * @param w the block's world
     * @param x the x coordinate of the block's location
     * @param y the y coordinate of the block's location
     * @param z the z coordinate of the block's location
     * @return a String in the style of org.bukkit.Location.toString() e.g. Location{world=CraftWorld{name=world},x=0.0,y=0.0,z=0.0,pitch=0.0,yaw=0.0}
     */
    public static String makeLocationStr(World w, int x, int y, int z) {
        return "Location{world=CraftWorld{name=" + w.getName() + "},x=" + x + ".0,y=" + y + ".0,z=" + z + ".0,pitch=0.0,yaw=0.0}";
    }
}
