/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.doors.inner;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInnerDoorBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;

public class InnerDoor {

    private final TARDIS plugin;
    private final int id;

    public InnerDoor(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public Inner get() {
        Block block = null;
        boolean b = false;
        // get inner door block
        ResultSetInnerDoorBlock rs = new ResultSetInnerDoorBlock(plugin, id);
        if (rs.resultSet()) {
            block = rs.getInnerBlock();
            if (block != null) {
                // check for item display
                ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(block);
                b = display != null;
            }
        }
        return new Inner(b, block);
    }
}
