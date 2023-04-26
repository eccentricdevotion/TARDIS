/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.schematic.actions;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class SchematicReplace {

    public boolean act(TARDIS plugin, Player player, String[] args) {
        if (args.length < 3) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return true;
        }
        UUID uuid = player.getUniqueId();
        // check they have selected start and end blocks
        if (!plugin.getTrackerKeeper().getStartLocation().containsKey(uuid)) {
            TARDISMessage.send(player, "SCHM_NO_START");
            return true;
        }
        if (!plugin.getTrackerKeeper().getEndLocation().containsKey(uuid)) {
            TARDISMessage.send(player, "SCHM_NO_END");
            return true;
        }
        // get the world
        World w = plugin.getTrackerKeeper().getStartLocation().get(uuid).getWorld();
        String chk_w = plugin.getTrackerKeeper().getEndLocation().get(uuid).getWorld().getName();
        if (!w.getName().equals(chk_w)) {
            TARDISMessage.send(player, "SCHM_WORLD");
            return true;
        }
        try {
            Material from = Material.valueOf(args[1].toUpperCase());
            Material to = Material.valueOf(args[2].toUpperCase());
            // get the raw coords
            int sx = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockX();
            int sy = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockY();
            int sz = plugin.getTrackerKeeper().getStartLocation().get(uuid).getBlockZ();
            int ex = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockX();
            int ey = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockY();
            int ez = plugin.getTrackerKeeper().getEndLocation().get(uuid).getBlockZ();
            // get the min & max coords
            int minx = Math.min(sx, ex);
            int maxx = Math.max(sx, ex);
            int miny = Math.min(sy, ey);
            int maxy = Math.max(sy, ey);
            int minz = Math.min(sz, ez);
            int maxz = Math.max(sz, ez);
            // loop through the blocks inside this cube
            for (int l = miny; l <= maxy; l++) {
                for (int r = minx; r <= maxx; r++) {
                    for (int c = minz; c <= maxz; c++) {
                        Block b = w.getBlockAt(r, l, c);
                        if (b.getType().equals(from)) {
                            BlockState state = b.getState();
                            if (state instanceof BlockState) {
                                plugin.getTardisHelper().removeTileEntity(state);
                            }
                            b.setType(to);
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(player, "ARG_MATERIAL");
            return true;
        }
        return true;
    }
}
