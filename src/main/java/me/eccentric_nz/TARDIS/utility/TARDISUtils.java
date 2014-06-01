/*
 * Copyright (C) 2014 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetChunks;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
    private final float volume;

    public TARDISUtils(TARDIS plugin) {
        this.plugin = plugin;
        this.volume = plugin.getConfig().getInt("preferences.sfx_volume") / 10.0F;
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
    @SuppressWarnings("deprecation")
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
        if (b != null) {
            b.setTypeId(m);
            b.setData(d, true);
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
        byte data = b.getData();
        set.put("block", bid);
        set.put("data", data);
        set.put("police_box", 1);
        qf.doInsert("blocks", set);
        plugin.getGeneralKeeper().getProtectBlockMap().put(l, id);
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
     * @param portal whether a chest can be in the portal block location
     */
    @SuppressWarnings("deprecation")
    public void setUnderDoorBlock(World w, int x, int y, int z, int m, byte d, int id, boolean portal) {
        // List of blocks that a door cannot be placed on
        List<Integer> ids = plugin.getBlocksConfig().getIntegerList("under_door_blocks");
        if (portal) {
            ids.remove(Integer.valueOf(54));
        }
        Block b = w.getBlockAt(x, y, z);
        int bid = b.getTypeId();
        if (ids.contains(bid)) {
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
            plugin.getGeneralKeeper().getProtectBlockMap().put(l, id);
            // set the block
            b.setTypeId(m);
            b.setData(d, true);
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
            cx = parseInt(split[1]);
            cz = parseInt(split[2]);
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
        World savedw = plugin.getServer().getWorld(data[0]);
        if (savedw != null) {
            savedx = parseInt(data[1]);
            savedy = parseInt(data[2]);
            savedz = parseInt(data[3]);
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
    public boolean checkChunk(String w, int x, int z, SCHEMATIC schm) {
        boolean chunkchk = false;
        short[] d;
        switch (schm) {
            case BIGGER:
                d = plugin.getBuildKeeper().getBiggerDimensions();
                break;
            case DELUXE:
                d = plugin.getBuildKeeper().getDeluxeDimensions();
                break;
            case ELEVENTH:
                d = plugin.getBuildKeeper().getEleventhDimensions();
                break;
            case REDSTONE:
                d = plugin.getBuildKeeper().getRedstoneDimensions();
                break;
            case STEAMPUNK:
                d = plugin.getBuildKeeper().getSteampunkDimensions();
                break;
            case PLANK:
                d = plugin.getBuildKeeper().getPlankDimensions();
                break;
            case TOM:
                d = plugin.getBuildKeeper().getTomDimensions();
                break;
            case ARS:
                d = plugin.getBuildKeeper().getARSDimensions();
                break;
            case CUSTOM:
                d = plugin.getBuildKeeper().getCustomDimensions();
                break;
            default:
                d = plugin.getBuildKeeper().getBudgetDimensions();
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
     * Parses a string for an integer.
     *
     * @param i the string to convert to an int.
     * @return a number
     */
    public int parseInt(String i) {
        int num = 0;
        try {
            num = Integer.parseInt(i);
        } catch (NumberFormatException n) {
            plugin.debug("Could not convert to int, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a byte.
     *
     * @param i the string to convert to an byte.
     * @return a number
     */
    public byte parseByte(String i) {
        byte num = (byte) 0;
        try {
            num = Byte.parseByte(i);
        } catch (NumberFormatException n) {
            plugin.debug("Could not convert to byte, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a short.
     *
     * @param i the string to convert to a short.
     * @return a number
     */
    public short parseShort(String i) {
        short num = 0;
        try {
            num = Short.parseShort(i);
        } catch (NumberFormatException n) {
            plugin.debug("Could not convert to short, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a float.
     *
     * @param i the string to convert to an float.
     * @return a floating point number
     */
    public float parseFloat(String i) {
        float num = 0.0f;
        try {
            num = Float.parseFloat(i);
        } catch (NumberFormatException n) {
            plugin.debug("Could not convert to float, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a double.
     *
     * @param i the string to convert to an double.
     * @return a floating point number
     */
    public double parseDouble(String i) {
        double num = 0.0d;
        try {
            num = Double.parseDouble(i);
        } catch (NumberFormatException n) {
            plugin.debug("Could not convert to double, the string was: " + i);
        }
        return num;
    }

    /**
     * Parses a string for a double.
     *
     * @param i the string to convert to an double.
     * @return a floating point number
     */
    public long parseLong(String i) {
        long num = 0L;
        try {
            num = Long.parseLong(i);
        } catch (NumberFormatException n) {
            plugin.debug("Could not convert to double, the string was: " + i);
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
        double x = parseDouble(xStr[1]);
        double y = parseDouble(yStr[1]);
        double z = parseDouble(zStr[1]);
        return new Location(w, x, y, z);
    }

    /**
     * Plays a TARDIS sound for the player and surrounding players at the
     * current location.
     *
     * @param l The location
     * @param p The player who initiated the sound playing, i.e. released the
     * handbrake
     * @param s The sound to play
     */
    @SuppressWarnings("deprecation")
    public void playTARDISSound(Location l, Player p, String s) {
        if (p != null) {
            p.playSound(l, s, volume, 1.0F);
            for (Entity e : p.getNearbyEntities(10.0d, 10.0d, 10.0d)) {
                if (e instanceof Player && !((Player) e).equals(p)) {
                    Player pp = (Player) e;
                    pp.playSound(pp.getLocation(), s, volume, 1.0f);
                }
            }
        }
    }

    /**
     * Attempts to play a TARDIS sound at an external location. Generally the
     * location is outside the TARDIS, so this will attempt to find rescued
     * players and players nearby to the TARDIS as it re-materialises.
     *
     * @param l The location to play the sound
     * @param s The sound to play
     */
    public void playTARDISSoundNearby(Location l, String s) {
        // spawn an entity at the location - an egg will do
        Entity egg = l.getWorld().spawnEntity(l, EntityType.EGG);
        for (Entity e : egg.getNearbyEntities(16.0d, 16.0d, 16.0d)) {
            if (e instanceof Player) {
                Player pp = (Player) e;
                pp.playSound(pp.getLocation(), s, volume, 1.0f);
            }
        }
        // remove entity
        egg.remove();
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

    public String getTime(long t) {
        if (t > 0 && t <= 2000) {
            return "early morning";
        }
        if (t > 2000 && t <= 3500) {
            return "mid morning";
        }
        if (t > 3500 && t <= 5500) {
            return "late morning";
        }
        if (t > 5500 && t <= 6500) {
            return "around noon";
        }
        if (t > 6500 && t <= 8000) {
            return "afternoon";
        }
        if (t > 8000 && t <= 10000) {
            return "mid afternoon";
        }
        if (t > 10000 && t <= 12000) {
            return "late afternoon";
        }
        if (t > 12000 && t <= 14000) {
            return "twilight";
        }
        if (t > 14000 && t <= 16000) {
            return "evening";
        }
        if (t > 16000 && t <= 17500) {
            return "late evening";
        }
        if (t > 17500 && t <= 18500) {
            return "around midnight";
        }
        if (t > 18500 && t <= 20000) {
            return "the small hours";
        }
        if (t > 20000 && t <= 22000) {
            return "the wee hours";
        } else {
            return "pre-dawn";
        }
    }

    public boolean inTARDISWorld(Player player) {
        // check they are still in the TARDIS world
        World world = player.getLocation().getWorld();
        String name = world.getName();
        ChunkGenerator gen = world.getGenerator();
        boolean special = (name.contains("TARDIS_TimeVortex") && (world.getWorldType().equals(WorldType.FLAT) || gen instanceof TARDISChunkGenerator));
        return name.equals("TARDIS_WORLD_" + player.getName()) || special;
    }

    /**
     * Checks if player has storage record, and update the tardis_id field if
     * they do.
     *
     * @param uuid the player's UUID
     * @param id the player's TARDIS ID
     * @param qf an instance of the database QueyFactory
     */
    public void updateStorageId(String uuid, int id, QueryFactory qf) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid);
        ResultSetDiskStorage rss = new ResultSetDiskStorage(plugin, where);
        if (rss.resultSet()) {
            HashMap<String, Object> wherej = new HashMap<String, Object>();
            wherej.put("uuid", uuid);
            HashMap<String, Object> setj = new HashMap<String, Object>();
            setj.put("tardis_id", id);
            qf.doUpdate("storage", setj, wherej);
        }
    }

    /**
     * Checks whether a door is open.
     *
     * @param door_bottom the bottom door block
     * @param dd the direction the door is facing
     * @return true or false
     */
    @SuppressWarnings("deprecation")
    public boolean isOpen(Block door_bottom, COMPASS dd) {
        byte door_data = door_bottom.getData();
        switch (dd) {
            case NORTH:
                if (door_data == 7) {
                    return true;
                }
                break;
            case WEST:
                if (door_data == 6) {
                    return true;
                }
                break;
            case SOUTH:
                if (door_data == 5) {
                    return true;
                }
                break;
            default:
                if (door_data == 4) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Gets the chat colour to use on the Ploice Box sign.
     *
     * @return the configured chat colour
     */
    public ChatColor getSignColour() {
        ChatColor colour;
        String cc = plugin.getConfig().getString("police_box.sign_colour");
        try {
            colour = ChatColor.valueOf(cc);
        } catch (IllegalArgumentException e) {
            colour = ChatColor.WHITE;
        }
        return colour;
    }

    /**
     * Gets the column to set the Police box sign in if CTM is on in the
     * player's preferences.
     *
     * @param d the direction of the Police Box
     * @return the column
     */
    public int getCol(COMPASS d) {
        switch (d) {
            case NORTH:
                return 6;
            case WEST:
                return 4;
            case SOUTH:
                return 2;
            default:
                return 0;
        }
    }

    @SuppressWarnings("deprecation")
    public int getHighestNetherBlock(World w, int wherex, int wherez) {
        int y = 100;
        Block startBlock = w.getBlockAt(wherex, y, wherez);
        while (startBlock.getTypeId() != 0) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
        }
        int air = 0;
        while (startBlock.getTypeId() == 0 && startBlock.getLocation().getBlockY() > 30) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
            air++;
        }
        int id = startBlock.getTypeId();
        if ((id == 87 || id == 88 || id == 89 || id == 112 || id == 113 || id == 114) && air >= 4) {
            y = startBlock.getLocation().getBlockY() + 1;
        }
        return y;
    }
}
