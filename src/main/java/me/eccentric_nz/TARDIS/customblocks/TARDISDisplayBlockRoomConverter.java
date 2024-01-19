/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.customblocks;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDisplayBlockRoomConverter implements Runnable {

    private final TARDIS plugin;
    private final Player owner;
    private final int startx, starty, startz;
    private boolean running;
    private int c = 16, h = 16, w = 16, level = 0, row = 0, taskId;
    private World world;

    public TARDISDisplayBlockRoomConverter(TARDIS plugin, Player owner, TARDISARSSlot slot) {
        this.plugin = plugin;
        this.owner = owner;
        this.startx = slot.getX();
        this.starty = slot.getY();
        this.startz = slot.getZ();
    }

    @Override
    public void run() {
        // initialise
        if (!running) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", owner.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
            }
            running = true;
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished
            plugin.getServer().getScheduler().cancelTask(taskId);
            taskId = 0;
            plugin.getMessenger().message(owner, "ROOM Display Block conversion complete");
        } else {
            // check a row of blocks
            for (int col = 0; col < c; col++) {
                int x = startx + row;
                int y = starty + level;
                int z = startz + col;
                Block block = world.getBlockAt(x, y, z);
                if (TARDISMushroomBlock.isTardisMushroom(block)) {
                    BlockData data = block.getBlockData();
                    // get which TARDIS block
                    TARDISDisplayItem tdi = TARDISMushroomBlockData.getTARDISBlock(data);
                    if (tdi != null) {
                        plugin.debug(tdi.getName());
                        block.setType(Material.BARRIER);
                        TARDISDisplayItemUtils.set(tdi, block);
                    }
                }
            }
            if (row < w) {
                row++;
            }
            if (row == w && level < h) {
                row = 0;
                level++;
            }
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
