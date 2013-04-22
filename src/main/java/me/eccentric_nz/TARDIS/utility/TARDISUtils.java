/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetChunks;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Various utility methods.
 *
 * The TARDIS can be programmed to execute automatic functions based on certain
 * conditions. It also automatically repairs after too much damage.
 *
 * @author eccentric_nz
 */
public class TARDISUtils {

    private final TARDIS plugin;
    Version bukkitversion;
    Version prewoodbuttonversion = new Version("1.4.2");

    public TARDISUtils(TARDIS plugin) {
        this.plugin = plugin;
        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        bukkitversion = (!v[0].equalsIgnoreCase("unknown")) ? new Version(v[0]) : new Version("1.4.7");
    }

    /**
     * Sets a block to the specified typeId and data.
     *
     * @param w the world the block is in.
     * @param x the x co-ordinate of the block.
     * @param y the y co-ordinate of the block.
     * @param z the z co-ordinate of the block.
     * @param m the typeId to set the block to.
     * @param d the data bit to set the block to.
     */
    public void setBlock(World w, int x, int y, int z, int m, byte d) {
        Block b = w.getBlockAt(x, y, z);
        if (m < 0) {
            if (bukkitversion.compareTo(prewoodbuttonversion) < 0 && (m == 143 || m == -113)) {
                m = 77;
            } else {
                m = m + 256;
            }
        }
        if (m == 92) { //cake -> handbrake
            m = 69;
            d = (byte) 5;
        }
        if (m == 52) { //mob spawner -> scanner button
            m = (bukkitversion.compareTo(prewoodbuttonversion) < 0) ? 77 : 143;
            d = (byte) 3;
        }
        b.setTypeIdAndData(m, d, true);
    }

    /**
     * Sets a block to the specified typeId and data and remembers its location,
     * typeId and data.
     *
     * @param w the world the block is in.
     * @param x the x co-ordinate of the block.
     * @param y the y co-ordinate of the block.
     * @param z the z co-ordinate of the block.
     * @param m the typeId to set the block to.
     * @param d the data bit to set the block to.
     * @param id the TARDIS this block belongs to.
     */
    public void setBlockAndRemember(World w, int x, int y, int z, int m, byte d, int id) {
        Block b = w.getBlockAt(x, y, z);
        // save the block location so that we can protect it from damage and restore it (if it wasn't air)!
        String l = b.getLocation().toString();
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("tardis_id", id);
        set.put("location", l);
        int bid = b.getTypeId();
        if (bid != 0) {
            byte data = b.getData();
            set.put("block", bid);
            set.put("data", data);
        }
        set.put("police_box", 1);
        qf.doInsert("blocks", set);
        plugin.protectBlockMap.put(l, id);
        // set the block
        b.setTypeIdAndData(m, d, true);
    }

    /**
     * Sets the block under the TARDIS Police Box door to the specified typeId
     * and data and remembers the block for replacement later on.
     *
     * @param w the world the block is in.
     * @param x the x co-ordinate of the block.
     * @param y the y co-ordinate of the block.
     * @param z the z co-ordinate of the block.
     * @param m the typeId to set the block to.
     * @param d the data bit to set the block to.
     * @param id the TARDIS this block belongs to.
     */
    public void setBlockCheck(World w, int x, int y, int z, int m, byte d, int id) {
        // List of blocks that a door cannot be placed on
        List<Integer> ids = Arrays.asList(0, 6, 8, 9, 10, 11, 18, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 44, 46, 50, 51, 53, 54, 55, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 75, 76, 77, 78, 79, 81, 83, 85, 89, 92, 93, 94, 96, 101, 102, 104, 105, 106, 107, 108, 109, 111, 113, 114, 115, 116, 117, 118, 119, 120, 122, 126, 128, 130, 131, 132, 134, 135, 136);
        Block b = w.getBlockAt(x, y, z);
        Integer bId = Integer.valueOf(b.getTypeId());
        byte bData = b.getData();
        if (ids.contains(bId)) {
            b.setTypeIdAndData(m, d, true);
            // remember replaced block location, TypeId and Data so we can restore it later
            String replaced = w.getName() + ":" + x + ":" + y + ":" + z + ":" + bId + ":" + bData;
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("replaced", replaced);
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            qf.doUpdate("tardis", set, where);
        }
    }
    private static int[] startLoc = new int[6];

    /**
     * Gets a start location for building the inner TARDIS.
     *
     * @param id the TARDIS this location belongs to.
     * @return
     */
    public int[] getStartLocation(int id) {
        int cx, cz;
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            String chunkstr = rs.getChunk();
            String[] split = chunkstr.split(":");
            World w = plugin.getServer().getWorld(split[0]);
            cx = parseNum(split[1]);
            cz = parseNum(split[2]);
            Chunk chunk = w.getChunkAt(cx, cz);
            startLoc[0] = chunk.getBlock(0, 15, 0).getX();
            startLoc[1] = startLoc[0];
            startLoc[2] = chunk.getBlock(0, 15, 0).getZ();
            startLoc[3] = startLoc[2];
            startLoc[4] = 1;
            startLoc[5] = 1;
        }
        return startLoc;
    }

    /**
     * Gets a location object from data stored in the database. This is used
     * when teleporting the player in and out of the TARDIS
     *
     * @param s the saved location data from the database.
     * @param yaw the player's yaw.
     * @param pitch the player's pitch.
     * @return
     */
    public Location getLocationFromDB(String s, float yaw, float pitch) {
        int savedx, savedy, savedz;
        // compile location from string
        String[] data = s.split(":");
        World savedw = Bukkit.getServer().getWorld(data[0]);
        if (savedw != null) {
            savedx = parseNum(data[1]);
            savedy = parseNum(data[2]);
            savedz = parseNum(data[3]);
            Location dest = new Location(savedw, savedx, savedy, savedz, yaw, pitch);
            return dest;
        } else {
            return null;
        }
    }

    /**
     * Checks whether a chunk is available to build a TARDIS in.
     *
     * @param w the world the chunk is in.
     * @param x the x co-ordinate of the chunk.
     * @param z the z co-ordinate of the chunk.
     * @param schm the schematic of the TARDIS being created.
     * @return
     */
    public boolean checkChunk(String w, int x, int z, TARDISConstants.SCHEMATIC schm) {
        boolean chunkchk = false;
        short[] d;
        switch (schm) {
            case BIGGER:
                d = plugin.biggerdimensions;
                break;
            case DELUXE:
                d = plugin.deluxedimensions;
                break;
            default:
                d = plugin.budgetdimensions;
                break;
        }
        int cw = roundUp(d[1], 16);
        int cl = roundUp(d[2], 16);
        // check all the chunks that will be used by the schematic
        for (int cx = 0; cx < cw; cx++) {
            for (int cz = 0; cz < cl; cz++) {
                HashMap<String, Object> where = new HashMap<String, Object>();
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
     * Returns a rounded integer after division.
     *
     * @param num the number being divided.
     * @param divisor the number to divide by.
     * @return
     */
    public static int roundUp(int num, int divisor) {
        return (num + divisor - 1) / divisor;
    }

    /**
     * Returns a rounded integer after division.
     *
     * @param i the number to convert to an int.
     * @return a number
     */
    public int parseNum(String i) {
        int num = 0;
        try {
            num = Integer.parseInt(i);
        } catch (NumberFormatException n) {
            plugin.console.sendMessage("Could not convert to number");
        }
        return num;
    }

    /**
     * Updates how many travellers there are in a TARDIS. The resulting count is
     * stored in a HashMap and is checked when players exit the TARDIS.
     *
     * @param id the TARDIS to get the count from.
     */
    public void updateTravellerCount(int id) {
        // how many travellers are in the TARDIS?
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTravellers rs = new ResultSetTravellers(plugin, where, true);
        if (rs.resultSet()) {
            int count = rs.getData().size();
            plugin.trackTravellers.put(id, count);
        }
    }

    public boolean compareLocations(Location a, Location b) {
        if (a.getWorld().equals(b.getWorld())) {
            double rd = plugin.getConfig().getDouble("recharge_distance");
            double squared = rd * rd;
            return (a.distanceSquared(b) <= squared);
        }
        return false;
    }

    public String getPlayersDirection(Player p) {
        // get player direction
        float pyaw = p.getLocation().getYaw();
        if (pyaw >= 0) {
            pyaw = (pyaw % 360);
        } else {
            pyaw = (360 + (pyaw % 360));
        }
        // determine direction player is facing
        String d = "";
        if (pyaw >= 315 || pyaw < 45) {
            d = "SOUTH";
        }
        if (pyaw >= 225 && pyaw < 315) {
            d = "EAST";
        }
        if (pyaw >= 135 && pyaw < 225) {
            d = "NORTH";
        }
        if (pyaw >= 45 && pyaw < 135) {
            d = "WEST";
        }
        return d;
    }
}
