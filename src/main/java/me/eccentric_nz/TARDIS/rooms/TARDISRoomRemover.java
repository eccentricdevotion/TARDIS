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
package me.eccentric_nz.TARDIS.rooms;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * When the Eleventh Doctor was trying to get out of his universe, he said he
 * was deleting the scullery room and squash court seven to give the TARDIS an
 * extra boost.
 *
 * @author eccentric_nz
 */
public class TARDISRoomRemover {

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
     * Jettison a TARDIS room, leaving just the walls behind. We will probably
     * need to get the dimensions of the room from the schematic, if user
     * supplied room schematics will be allowed.
     *
     * @return false if the room has already been jettisoned
     */
    @SuppressWarnings("deprecation")
    public boolean remove() {
        int check_distance = (r.equals("ARBORETUM")) ? 5 : 7;
        if (l.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.valueOf(d.toString()), check_distance).getType().equals(Material.AIR)) {
            return false;
        }
        // get start locations
        int sx, sy, sz, ex, ey, ez, downy, upy;
        // calculate values for downy and upy from schematic dimensions / config
        short[] dimensions = plugin.getBuildKeeper().getRoomDimensions().get(r);
        downy = Math.abs(plugin.getRoomsConfig().getInt("rooms." + r + ".offset"));
        upy = dimensions[0] - (downy + 1);
        int xzoffset = (dimensions[1] / 2);
        switch (d) {
            case NORTH:
                l.setX(l.getX() - xzoffset);
                l.setZ(l.getZ() - dimensions[1]);
                break;
            case WEST:
                l.setX(l.getX() - dimensions[1]);
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
        ex = l.getBlockX() + (dimensions[1] - 1);
        sz = l.getBlockZ();
        ez = l.getBlockZ() + (dimensions[1] - 1);
        sy = l.getBlockY() + upy;
        ey = l.getBlockY() - downy;
        World w = l.getWorld();
        QueryFactory qf = new QueryFactory(plugin);
        // loop through blocks and set them to air
        for (int y = sy; y >= ey; y--) {
            for (int x = sx; x <= ex; x++) {
                for (int z = sz; z <= ez; z++) {
                    Block block = w.getBlockAt(x, y, z);
                    block.setType(Material.AIR);
                    // if it is a GRAVITY or ANTIGRAVITY well remove it from the database
                    if (r.equals("GRAVITY") || r.equals("ANTIGRAVITY")) {
                        byte data = block.getData();
                        if ((data == (byte) 5 || data == (byte) 6) && block.getType().equals(Material.WOOL)) {
                            String loc = new Location(w, x, y, z).toString();
                            HashMap<String, Object> where = new HashMap<String, Object>();
                            where.put("location", loc);
                            where.put("tardis_id", id);
                            qf.doDelete("gravity_well", where);
                        }
                    }
                }
            }
        }
        if (r.equals("FARM") || r.equals("STABLE") || r.equals("RAIL") || r.equals("VILLAGE") || r.equals("RENDERER")) {
            // remove stored location from the database
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put(r.toLowerCase(), "");
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            qf.doUpdate("tardis", set, where);
        }
        return true;
    }
}
