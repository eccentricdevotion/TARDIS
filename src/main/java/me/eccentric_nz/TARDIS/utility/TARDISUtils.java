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

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetChunks;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

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

    public TARDISUtils(TARDIS plugin) {
        this.plugin = plugin;
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
        final Block b = w.getBlockAt(x, y, z);
        if (m < 0) {
            m += 256;
        }
        if (m == 92) { //cake -> handbrake
            m = 69;
            d = (byte) 5;
        }
        if (m == 52) { //mob spawner -> scanner button
            m = 143;
            d = (byte) 3;
        }
        if (m == 33) {
            plugin.debug("data before setting: " + d);
        }
        b.setTypeId(m);
        b.setData(d, true);
        if (m == 33) {
            plugin.debug("data right after setting: " + b.getData());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.debug("data 20L after setting: " + b.getData());
                }
            }, 20L);
        }
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
    @SuppressWarnings("deprecation")
    public void setBlockAndRemember(World w, int x, int y, int z, int m, byte d, int id) {
        Block b = w.getBlockAt(x, y, z);
        // save the block location so that we can protect it from damage and restore it (if it wasn't air)!
        String l = b.getLocation().toString();
        QueryFactory qf = new QueryFactory(plugin);
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("tardis_id", id);
        set.put("location", l);
        int bid = b.getTypeId();
        //if (bid != 0) {
        byte data = b.getData();
        set.put("block", bid);
        set.put("data", data);
        //}
        set.put("police_box", 1);
        qf.doInsert("blocks", set);
        plugin.protectBlockMap.put(l, id);
        // set the block
        b.setTypeId(m);
        b.setData(d, true);
    }

    /**
     * Sets the block under the TARDIS Police Box door to the specified typeId
     * and data and remembers the block for replacement later on.
     *
     * @param w the world the block is in.
     * @param x the x coordinate of the block.
     * @param y the y coordinate of the block.
     * @param z the z coordinate of the block.
     * @param m the typeId to set the block to.
     * @param d the data bit to set the block to.
     * @param id the TARDIS this block belongs to.
     */
    @SuppressWarnings("deprecation")
    public void setUnderDoorBlock(World w, int x, int y, int z, int m, byte d, int id) {
        // List of blocks that a door cannot be placed on
        List<Integer> ids = plugin.getBlocksConfig().getIntegerList("under_door_blocks");
        Block b = w.getBlockAt(x, y, z);
        int bid = b.getTypeId();
        if (ids.contains(bid)) {
            b.setTypeId(m);
            b.setData(d, true);
            // remember replaced block location, TypeId and Data so we can restore it later
            String l = b.getLocation().toString();
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("tardis_id", id);
            set.put("location", l);
            set.put("block", bid);
            set.put("data", b.getData());
            set.put("police_box", 1);
            qf.doInsert("blocks", set);
            plugin.protectBlockMap.put(l, id);
        }
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
            startLoc[0] = (chunk.getBlock(0, 64, 0).getX());
            startLoc[1] = startLoc[0];
            startLoc[2] = (chunk.getBlock(0, 64, 0).getZ());
            startLoc[3] = startLoc[2];
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
     * @return a Location.
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
     * @return true or false.
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
            case ELEVENTH:
                d = plugin.eleventhdimensions;
                break;
            case REDSTONE:
                d = plugin.redstonedimensions;
                break;
            case STEAMPUNK:
                d = plugin.steampunkdimensions;
                break;
            case PLANK:
                d = plugin.plankdimensions;
                break;
            case TOM:
                d = plugin.tomdimensions;
                break;
            case ARS:
                d = plugin.arsdimensions;
                break;
            case CUSTOM:
                d = plugin.customdimensions;
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
     * @return a rounded number.
     */
    public int roundUp(int num, int divisor) {
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
            plugin.debug("Could not convert to number, the string was: " + i);
        }
        return num;
    }

    public boolean compareLocations(Location a, Location b) {
        if (a.getWorld().equals(b.getWorld())) {
            double rd = plugin.getArtronConfig().getDouble("recharge_distance");
            double squared = rd * rd;
            return (a.distanceSquared(b) <= squared);
        }
        return false;
    }

    public String getPlayersDirection(Player p, boolean swap) {
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
            d = (swap) ? "NORTH" : "SOUTH";
        }
        if (pyaw >= 225 && pyaw < 315) {
            d = (swap) ? "WEST" : "EAST";
        }
        if (pyaw >= 135 && pyaw < 225) {
            d = (swap) ? "SOUTH" : "NORTH";
        }
        if (pyaw >= 45 && pyaw < 135) {
            d = (swap) ? "EAST" : "WEST";
        }
        return d;
    }

    /**
     * Convert a pre TARDIS v2.3 location string to a v2.3 one.
     *
     * @param data an old location string retrieved from the database
     * @return a String in the style of org.bukkit.Location.toString() e.g.
     * Location{world=CraftWorld{name=world},x=0.0,y=0.0,z=0.0,pitch=0.0,yaw=0.0}
     */
    public String makeLocationStr(String data) {
        String[] s = data.split(":");
        return "Location{world=CraftWorld{name=" + s[0] + "},x=" + s[1] + ".0,y=" + s[2] + ".0,z=" + s[3] + ".0,pitch=0.0,yaw=0.0}";
    }

    /**
     * Create a TARDIS v2.3 location string from block coordinates.
     *
     * @param w the block's world
     * @param x the x coordinate of the block's location
     * @param y the y coordinate of the block's location
     * @param z the z coordinate of the block's location
     * @return a String in the style of org.bukkit.Location.toString() e.g.
     * Location{world=CraftWorld{name=world},x=0.0,y=0.0,z=0.0,pitch=0.0,yaw=0.0}
     */
    public String makeLocationStr(World w, int x, int y, int z) {
        return "Location{world=CraftWorld{name=" + w.getName() + "},x=" + x + ".0,y=" + y + ".0,z=" + z + ".0,pitch=0.0,yaw=0.0}";
    }

    public boolean canGrowRooms(String chunk) {
        String[] data = chunk.split(":");
        World room_world = plugin.getServer().getWorld(data[0]);
        ChunkGenerator gen = room_world.getGenerator();
        WorldType wt = room_world.getWorldType();
        boolean special = (data[0].contains("TARDIS_TimeVortex") && (wt.equals(WorldType.FLAT) || gen instanceof TARDISChunkGenerator));
        return (data[0].contains("TARDIS_WORLD_") || special);
    }

    public Location getLocationFromBukkitString(String string) {
        //Location{world=CraftWorld{name=world},x=0.0,y=0.0,z=0.0,pitch=0.0,yaw=0.0}
        String[] loc_data = string.split(",");
        // w, x, y, z - 0, 1, 2, 3
        String[] wStr = loc_data[0].split("=");
        String[] xStr = loc_data[1].split("=");
        String[] yStr = loc_data[2].split("=");
        String[] zStr = loc_data[3].split("=");
        World w = plugin.getServer().getWorld(wStr[2].substring(0, (wStr[2].length() - 1)));
        int x = plugin.utils.parseNum(xStr[1].substring(0, (xStr[1].length() - 2)));
        int y = plugin.utils.parseNum(yStr[1].substring(0, (yStr[1].length() - 2)));
        int z = plugin.utils.parseNum(zStr[1].substring(0, (zStr[1].length() - 2)));
        return new Location(w, x, y, z);
    }

    public void playTARDISSound(Location l, Player p, String s) {
        p.playSound(l, s, 5.0F, 1.0F);
    }

    public String getWoodType(Material m, byte d) {
        String type;
        switch (m) {
            case WOOD:
                switch (d) {
                    case 0:
                        type = "OAK";
                        break;
                    case 1:
                        type = "SPRUCE";
                        break;
                    case 2:
                        type = "BIRCH";
                        break;
                    case 3:
                        type = "JUNGLE";
                        break;
                    case 4:
                        type = "ACACIA";
                        break;
                    default:
                        type = "DARK_OAK";
                        break;
                }
                break;
            case LOG:
                switch (d) {
                    case 0:
                        type = "OAK";
                        break;
                    case 1:
                        type = "SPRUCE";
                        break;
                    case 2:
                        type = "BIRCH";
                        break;
                    default:
                        type = "JUNGLE";
                        break;
                }
                break;
            default: // LOG_2
                switch (d) {
                    case 0:
                        type = "ACACIA";
                        break;
                    default:
                        type = "DARK_OAK";
                        break;
                }
                break;
        }
        return type;
    }

    public boolean isOceanBiome(Biome b) {
        return (b.equals(Biome.OCEAN) || b.equals(Biome.DEEP_OCEAN) || b.equals(Biome.FROZEN_OCEAN));
    }
}
