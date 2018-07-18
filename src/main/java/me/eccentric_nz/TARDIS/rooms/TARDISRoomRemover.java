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
package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * When the Eleventh Doctor was trying to get out of his universe, he said he was deleting the scullery room and squash
 * court seven to give the TARDIS an extra boost.
 *
 * @author eccentric_nz
 */
class TARDISRoomRemover {

    private final TARDIS plugin;
    private final String r;
    private final Location l;
    private final COMPASS d;
    private final int id;

    public TARDISRoomRemover(TARDIS plugin, String r, Location l, COMPASS d, int id) {
        this.plugin = plugin;
        this.r = r;
        this.l = l;
        this.d = d;
        this.id = id;
    }

    /**
     * Jettison a TARDIS room, leaving just the walls behind. We will probably need to get the dimensions of the room
     * from the schematic, if user supplied room schematics will be allowed.
     *
     * @return false if the room has already been jettisoned
     */
    public boolean remove() {
        int check_distance = (r.equals("ARBORETUM")) ? 5 : 7;
        if (l.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.valueOf(d.toString()), check_distance).getType().equals(Material.AIR)) {
            return false;
        }
        // get start locations
        int sx, sy, sz, ex, ey, ez, downy, upy;
        // calculate values for downy and upy from schematic dimensions / config
        String directory = (plugin.getRoomsConfig().getBoolean("rooms." + r + ".user")) ? "user_schematics" : "schematics";
        String path = plugin.getDataFolder() + File.separator + directory + File.separator + r.toLowerCase(Locale.ENGLISH) + ".tschm";
        // get JSON
        JSONObject obj = TARDISSchematicGZip.unzip(path);
        // get dimensions
        JSONObject dimensions = (JSONObject) obj.get("dimensions");
        int h = dimensions.getInt("height");
        int wid = dimensions.getInt("width");
        downy = Math.abs(plugin.getRoomsConfig().getInt("rooms." + r + ".offset"));
        upy = h - (downy + 1);
        int xzoffset = (wid / 2);
        switch (d) {
            case NORTH:
                l.setX(l.getX() - xzoffset);
                l.setZ(l.getZ() - wid);
                break;
            case WEST:
                l.setX(l.getX() - wid);
                l.setZ(l.getZ() - xzoffset);
                break;
            case SOUTH:
                l.setX(l.getX() - xzoffset);
                break;
            default:
                l.setZ(l.getZ() - xzoffset);
                break;
        }
        sx = l.getBlockX();
        ex = l.getBlockX() + (wid - 1);
        sz = l.getBlockZ();
        ez = l.getBlockZ() + (wid - 1);
        sy = l.getBlockY() + upy;
        ey = l.getBlockY() - downy;
        World w = l.getWorld();
        QueryFactory qf = new QueryFactory(plugin);
        // loop through blocks and set them to air
        for (int y = sy; y >= ey; y--) {
            for (int x = sx; x <= ex; x++) {
                for (int z = sz; z <= ez; z++) {
                    Block block = w.getBlockAt(x, y, z);
                    block.setBlockData(TARDISConstants.AIR);
                    // if it is a GRAVITY or ANTIGRAVITY well remove it from the database
                    if (r.equals("GRAVITY") || r.equals("ANTIGRAVITY")) {
                        if (block.getType().equals(Material.LIME_WOOL) || block.getType().equals(Material.PINK_WOOL)) {
                            String loc = new Location(w, x, y, z).toString();
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("location", loc);
                            where.put("tardis_id", id);
                            qf.doDelete("gravity_well", where);
                        }
                    }
                }
            }
        }
        if (r.equals("FARM") || r.equals("STABLE") || r.equals("STALL") || r.equals("RAIL") || r.equals("VILLAGE") || r.equals("RENDERER") || r.equals("HUTCH") || r.equals("IGLOO") || r.equals("AQUARIUM")) {
            // remove stored location from the database
            HashMap<String, Object> set = new HashMap<>();
            set.put(r.toLowerCase(Locale.ENGLISH), "");
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            qf.doUpdate("farming", set, where);
        }
        return true;
    }
}
