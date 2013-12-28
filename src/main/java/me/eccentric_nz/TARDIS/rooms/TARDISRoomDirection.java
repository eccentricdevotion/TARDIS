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

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRoomDirection {

    private final Block b;
    private boolean found;
    private BlockFace face;
    private COMPASS compass;

    public TARDISRoomDirection(Block b) {
        this.b = b;
        this.found = false;
    }

    /**
     * Gets the compass the room should be grown by finding the pressure plate
     * in front of the door.
     */
    public void getDirection() {
        for (COMPASS c : COMPASS.values()) {
            BlockFace tmp = BlockFace.valueOf(c.toString());
            if (b.getRelative(tmp).getType().equals(Material.STONE_PLATE)) {
                face = tmp;
                found = true;
                compass = c;
            }
        }
    }

    public boolean isFound() {
        return found;
    }

    public BlockFace getFace() {
        return face;
    }

    public COMPASS getCompass() {
        return compass;
    }
}
