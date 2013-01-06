/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.TARDISConstants.ROOM;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.Location;
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

    TARDISDatabase service = TARDISDatabase.getInstance();
    private String r;
    private Location l;
    private COMPASS d;

    public TARDISRoomRemover(String r, Location l, COMPASS d) {
        this.r = r;
        this.l = l;
        this.d = d;
    }

    /**
     * Jettison a TARDIS room, leaving just the walls behind. We will probably
     * need to get the dimensions of the room from the schematic, if user
     * supplied room schematics will be allowed.
     */
    public boolean remove() {
        // get start locations
        int sx, sy, sz, ex, ey, ez, downy, upy;
        switch (ROOM.valueOf(r)) {
            case PASSAGE:
                downy = -2;
                upy = 4;
                break;
            case POOL:
                downy = -3;
                upy = 7;
                break;
            case ARBORETUM:
                downy = -4;
                upy = 7;
                break;
            default:
                downy = -1;
                upy = 7;
                break;
        }
        switch (d) {
            case NORTH:
                if (r.equalsIgnoreCase("PASSAGE")) {
                    sx = l.getBlockX() - 4;
                    sz = l.getBlockZ() - 6;
                    ex = l.getBlockX() + 4;
                    ez = l.getBlockZ();
                } else {
                    sx = l.getBlockX() - 5;
                    sz = l.getBlockZ() - 10;
                    ex = l.getBlockX() + 5;
                    ez = l.getBlockZ();
                }
                break;
            case WEST:
                if (r.equalsIgnoreCase("PASSAGE")) {
                    sx = l.getBlockX() - 6;
                    sz = l.getBlockZ() - 4;
                    ex = l.getBlockX();
                    ez = l.getBlockZ() + 4;
                } else {
                    sx = l.getBlockX() - 10;
                    sz = l.getBlockZ() - 5;
                    ex = l.getBlockX();
                    ez = l.getBlockZ() + 5;
                }
                break;
            case SOUTH:
                if (r.equalsIgnoreCase("PASSAGE")) {
                    sx = l.getBlockX() - 4;
                    sz = l.getBlockZ();
                    ex = l.getBlockX() + 4;
                    ez = l.getBlockZ() + 6;
                } else {
                    sx = l.getBlockX() - 5;
                    sz = l.getBlockZ();
                    ex = l.getBlockX() + 5;
                    ez = l.getBlockZ() + 10;
                }
                break;
            default:
                if (r.equalsIgnoreCase("PASSAGE")) {
                    sx = l.getBlockX();
                    sz = l.getBlockZ() - 4;
                    ex = l.getBlockX() + 6;
                    ez = l.getBlockZ() + 4;
                } else {
                    sx = l.getBlockX();
                    sz = l.getBlockZ() - 5;
                    ex = l.getBlockX() + 10;
                    ez = l.getBlockZ() + 5;
                }
                break;
        }
        sy = l.getBlockY() - downy;
        ey = l.getBlockY() + upy;
        World w = l.getWorld();
        // loop throgh blocks and set them to air
        for (int y = sy; y <= ey; y++) {
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