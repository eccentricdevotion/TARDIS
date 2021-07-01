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
package me.eccentric_nz.TARDIS.rooms;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;

/**
 * Data storage class for room building. We can just create a new instance and pass this around to the various methods
 * instead of a whole bunch of parameters.
 * <p>
 * The TARDIS had a full kitchen which included a refrigerator.
 *
 * @author eccentric_nz
 */
public class TARDISRoomData {

    private COMPASS direction;
    private String room;
    private Location location;
    private Block block;
    private JsonObject schematic;
    private int tardis_id;
    private int row;
    private int column;
    private int level;
    private Material middleType;
    private Material floorType;
    private List<String> postBlocks;

    public TARDISRoomData() {
        row = 0;
        column = 0;
        level = 0;
    }

    public COMPASS getDirection() {
        return direction;
    }

    public void setDirection(COMPASS direction) {
        this.direction = direction;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
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

    public JsonObject getSchematic() {
        return schematic;
    }

    public void setSchematic(JsonObject schematic) {
        this.schematic = schematic;
    }

    Material getMiddleType() {
        return middleType;
    }

    public void setMiddleType(Material middleType) {
        this.middleType = middleType;
    }

    Material getFloorType() {
        return floorType;
    }

    public void setFloorType(Material floorType) {
        this.floorType = floorType;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public void setTardis_id(int tardis_id) {
        this.tardis_id = tardis_id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<String> getPostBlocks() {
        return postBlocks;
    }

    public void setPostBlocks(List<String> postBlocks) {
        this.postBlocks = postBlocks;
    }
}
