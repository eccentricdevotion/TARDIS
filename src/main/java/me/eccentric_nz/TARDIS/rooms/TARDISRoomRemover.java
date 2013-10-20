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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

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

    public TARDISRoomRemover(TARDIS plugin, String r, Location l, COMPASS d) {
        this.plugin = plugin;
        this.r = r;
        this.l = l;
        this.d = d;
    }

    /**
     * Jettison a TARDIS room, leaving just the walls behind. We will probably
     * need to get the dimensions of the room from the schematic, if user
     * supplied room schematics will be allowed.
     *
     * @return false if the room has already been jettisoned
     */
    public boolean remove() {
        // get start locations
        int sx, sy, sz, ex, ey, ez, downy, upy, half, lessthree;
        // calculate values for downy and upy from schematic dimensions / config
        short[] dimensions = plugin.room_dimensions.get(r);
        downy = Math.abs(plugin.getRoomsConfig().getInt("rooms." + r + ".offset"));
        upy = dimensions[0] - (downy + 1);
        half = (dimensions[1] - 2) / 2;
        lessthree = dimensions[1] - 3;
        switch (d) {
            case NORTH:
                sx = l.getBlockX() - half;
                sz = l.getBlockZ() - lessthree;
                ex = l.getBlockX() + half;
                ez = l.getBlockZ();
                break;
            case WEST:
                sx = l.getBlockX() - lessthree;
                sz = l.getBlockZ() - half;
                ex = l.getBlockX();
                ez = l.getBlockZ() + half;
                break;
            case SOUTH:
                sx = l.getBlockX() - half;
                sz = l.getBlockZ();
                ex = l.getBlockX() + half;
                ez = l.getBlockZ() + lessthree;
                break;
            default:
                sx = l.getBlockX();
                sz = l.getBlockZ() - half;
                ex = l.getBlockX() + lessthree;
                ez = l.getBlockZ() + half;
                break;
        }
        sy = l.getBlockY() - downy;
        ey = l.getBlockY() + upy;
        World w = l.getWorld();
        if (w.getBlockAt(sx + 2, l.getBlockY() - 1, sz + 2).getType().equals(Material.AIR)) {
            return false;
        }
        // loop through blocks and set them to air
        for (int y = ey; y >= sy; y--) {
            for (int x = sx; x <= ex; x++) {
                for (int z = sz; z <= ez; z++) {
                    Block block = w.getBlockAt(x, y, z);
                    block.setTypeId(0);
                }
            }
        }
        return true;
    }
}
