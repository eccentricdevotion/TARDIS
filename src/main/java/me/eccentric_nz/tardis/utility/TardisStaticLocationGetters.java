/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.utility;

import org.bukkit.*;

import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TardisStaticLocationGetters {

    //    private static final int[] fourByFour = new int[]{-2, -1, 0, 1, 2};
    private static final int[] THREE_BY_THREE = new int[]{-1, 0, 1};

    /**
     * Get a World from a stored string.
     *
     * @param data the string containing the world name
     * @return the World or null if not world matched
     */
    public static World getWorld(String data) {
        String[] split = data.split(":");
        return Bukkit.getServer().getWorld(split[0]);
    }

    /**
     * Gets a location object from data stored in the database.
     *
     * @param s the saved location data from the database.
     * @return a Location.
     */
    public static Location getLocationFromDB(String s) {
        double savedx, savedy, savedz;
        // compile location from string
        String[] data = s.split(":");
        World savedw = Bukkit.getServer().getWorld(data[0]);
        if (savedw == null) {
            return null;
        }
        savedx = TardisNumberParsers.parseDouble(data[1]);
        savedy = TardisNumberParsers.parseDouble(data[2]);
        savedz = TardisNumberParsers.parseDouble(data[3]);
        return new Location(savedw, savedx, savedy, savedz);
    }

    /**
     * Gets a location object from data stored in the database.
     *
     * @param s the saved location data from the database.
     * @return a Location.
     */
    public static Location getSpawnLocationFromDB(String s) {
        double savedx, savedy, savedz;
        // compile location from string
        String[] data = s.split(":");
        World savedw = Bukkit.getServer().getWorld(data[0]);
        if (savedw == null) {
            return null;
        }
        savedx = TardisNumberParsers.parseDouble(data[1]) + 0.5d;
        savedy = TardisNumberParsers.parseDouble(data[2]) + 1.0d;
        savedz = TardisNumberParsers.parseDouble(data[3]) + 0.5d;
        return new Location(savedw, savedx, savedy, savedz);
    }

    /**
     * Gets a location object from data stored in the database.
     *
     * @param string the stored Bukkit location string e.g. Location{world=CraftWorld{name=world},x=0.0,y=0.0,z=0.0,pitch=0.0,yaw=0.0}
     * @return the location or null
     */
    public static Location getLocationFromBukkitString(String string) {
        //Location{world=CraftWorld{name=world},x=0.0,y=0.0,z=0.0,pitch=0.0,yaw=0.0}
        String[] loc_data = string.split(",");
        // w, x, y, z - 0, 1, 2, 3
        String[] wStr = loc_data[0].split("=");
        String[] xStr = loc_data[1].split("=");
        String[] yStr = loc_data[2].split("=");
        String[] zStr = loc_data[3].split("=");
        String tmp = wStr[2].substring(0, (wStr[2].length() - 1));
        World w = Bukkit.getServer().getWorld(tmp);
        if (w == null) {
            return null;
        }
        // Location{world=CraftWorld{name=world},x=1.0000021E7,y=67.0,z=1824.0,pitch=0.0,yaw=0.0}
        double x = (xStr[1].contains("E")) ? Double.parseDouble(xStr[1]) : TardisNumberParsers.parseDouble(xStr[1]);
        double y = TardisNumberParsers.parseDouble(yStr[1]);
        double z = (zStr[1].contains("E")) ? Double.parseDouble(zStr[1]) : TardisNumberParsers.parseDouble(zStr[1]);
        return new Location(w, x, y, z);
    }

    /**
     * Create a Bukkit location string from block coordinates.
     *
     * @param w the block's world
     * @param x the x coordinate of the block's location
     * @param y the y coordinate of the block's location
     * @param z the z coordinate of the block's location
     * @return a String in the style of org.bukkit.Location.toString() e.g. Location{world=CraftWorld{name=world},x=0.0,y=0.0,z=0.0,pitch=0.0,yaw=0.0}
     */
    public static String makeLocationStr(String w, String x, String y, String z) {
        return "Location{world=CraftWorld{name=" + w + "},x=" + x + ".0,y=" + y + ".0,z=" + z + ".0,pitch=0.0,yaw=0.0}";
    }

    /**
     * Create a Bukkit location string from block coordinates.
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

    /**
     * Create a TARDIS Repeater location string from a Bukkit location.
     *
     * @param location the location to convert to a String
     * @return a String in the style of world:x:y:z
     */
    public static String makeLocationStr(Location location) {
        return Objects.requireNonNull(location.getWorld()).getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ();
    }

    /**
     * Gets the chunk the TARDIS interior is in.
     *
     * @param str the saved location data from the database.
     * @return a Chunk.
     */
    public static Chunk getChunk(String str) {
        String[] split = str.split(":");
        World cw = Bukkit.getServer().getWorld(split[0]);
        int cx = TardisNumberParsers.parseInt(split[1]);
        int cz = TardisNumberParsers.parseInt(split[2]);
        assert cw != null;
        return cw.getChunkAt(cx, cz);
    }

    public static int getHighestYIn3x3(World world, int x, int z) {
        int y = 0;
        for (int xx : THREE_BY_THREE) {
            for (int zz : THREE_BY_THREE) {
                // need to +1 due to Spigot change
                int tmp = world.getHighestBlockYAt(x + xx, z + zz) + 1;
                y = Math.max(tmp, y);
                if (world.getName().equals("Siluria") && world.getBlockAt(x, y - 1, z).getType().equals(Material.BAMBOO)) {
                    y--;
                    while (world.getBlockAt(x, y, z).getType().equals(Material.BAMBOO)) {
                        y--;
                    }
                }
            }
        }
        return y;
    }
}