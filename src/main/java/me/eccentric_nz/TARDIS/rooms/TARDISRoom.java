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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.TARDISConstants.ROOM;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRoom {

    private final TARDIS plugin;
    private Location l;
    Block b;
    private ROOM r;
    private COMPASS d;
    private int middle_id;
    private byte middle_data;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISRoom(TARDIS plugin, ROOM r, Location l, Block b, int middle_id, byte middle_data, COMPASS d) {
        this.plugin = plugin;
        this.l = l;
        this.b = b;
        this.r = r;
        this.d = d;
        this.middle_id = middle_id;
        this.middle_data = middle_data;
    }

    public void room() {
        String[][][] s;
        switch (r) {
            case ARBORETUM:
                s = plugin.arboretumschematic;
                break;
            case BEDROOM:
                s = plugin.bedroomschematic;
                break;
            case KITCHEN:
                s = plugin.kitchenschematic;
                break;
            case LIBRARY:
                s = plugin.libraryschematic;
                break;
            case POOL:
                s = plugin.poolschematic;
                break;
            case VAULT:
                s = plugin.vaultschematic;
                break;
            default:
                s = plugin.emptyschematic;
                break;
        }
        short[] dimensions = plugin.roomdimensions;
        short h = dimensions[0];
        short w = dimensions[1];
        short c = dimensions[2];
        int level, row, col, id, startx = l.getBlockX(), starty = l.getBlockY(), startz = l.getBlockZ(), resetx, resetz;
        resetx = startx;
        resetz = startz;
        String tmp;
        byte data;
        for (level = 0; level < h; level++) {
            for (row = 0; row < w; row++) {
                for (col = 0; col < c; col++) {
                    tmp = s[level][row][col];
                    // if the current entry is not AIR then change it, otherwise move on to the next
                    if (!tmp.equals("0:0")) {
                        String[] iddata = tmp.split(":");
                        id = plugin.utils.parseNum(iddata[0]);
                        data = Byte.parseByte(iddata[1]);
                        if (id == 35 && data == 1) {
                            id = middle_id;
                            data = middle_data;
                        }
                        plugin.utils.setBlock(l.getWorld(), startx, starty, startz, id, data);
                    }
                    startx += 1;
                }
                startx = resetx;
                startz += 1;
            }
            startz = resetz;
            starty += 1;
        }
        //put the door in
        byte door_data;
        switch (d) {
            case NORTH:
                door_data = 1;
                break;
            case WEST:
                door_data = 0;
                break;
            case SOUTH:
                door_data = 3;
                break;
            default:
                door_data = 2;
                break;
        }
        b.setTypeIdAndData(71, door_data, true);
        b.getRelative(BlockFace.UP).setTypeIdAndData(71, (byte) 8, true);
    }
}