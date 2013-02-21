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

import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.TARDISConstants.ROOM;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Data storage class for room building. We can just create a new instance and
 * pass this around to the various methods instead of a whole bunch of
 * parameters.
 *
 * The TARDIS had a full kitchen which included a refrigerator.
 *
 * @author eccentric_nz
 */
public class TARDISRoomData {

    COMPASS direction;
    ROOM room;
    Location location;
    Block block;
    String[][][] schematic;
    short[] dimensions;
    int middle_id, x, z, tardis_id;
    byte middle_data;

    public TARDISRoomData() {
    }

    public COMPASS getDirection() {
        return direction;
    }

    public void setDirection(COMPASS direction) {
        this.direction = direction;
    }

    public ROOM getRoom() {
        return room;
    }

    public void setRoom(ROOM room) {
        this.room = room;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public String[][][] getSchematic() {
        return schematic;
    }

    public void setSchematic(String[][][] schematic) {
        this.schematic = schematic;
    }

    public short[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(short[] dimensions) {
        this.dimensions = dimensions;
    }

    public int getMiddle_id() {
        return middle_id;
    }

    public void setMiddle_id(int middle_id) {
        this.middle_id = middle_id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public void setTardis_id(int tardis_id) {
        this.tardis_id = tardis_id;
    }

    public byte getMiddle_data() {
        return middle_data;
    }

    public void setMiddle_data(byte middle_data) {
        this.middle_data = middle_data;
    }
}