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
package me.eccentric_nz.TARDIS.customblocks;

import com.google.gson.JsonObject;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDisplayBlockConverter implements Runnable {

    private final TARDIS plugin;
    private final Player owner;
    private boolean running;
    private int c, h, w, level = 0, row = 0, startx, starty, startz, taskId;
    private World world;
    private JsonObject obj;

    public TARDISDisplayBlockConverter(TARDIS plugin, Player owner) {
        this.plugin = plugin;
        this.owner = owner;
    }

    @Override
    public void run() {
        // initialise
        if (!running) {
            plugin.debug("first run");
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", owner.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                plugin.debug("found tardis record");
                Tardis tardis = rs.getTardis();
                int slot = tardis.getTIPS();
                if (slot != -1) { // default world - use TIPS
                    TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
                    TARDISTIPSData pos = tintpos.getTIPSData(slot);
                    startx = pos.getCentreX();
                    startz = pos.getCentreZ();
                } else {
                    int[] gsl = plugin.getLocationUtils().getStartLocation(tardis.getTardis_id());
                    startx = gsl[0];
                    startz = gsl[2];
                }
                world = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getChunk());
                Schematic schm = tardis.getSchematic();
                // get JSON
                obj = TARDISSchematicGZip.getObject(plugin, "consoles", schm.getPermission(), schm.isCustom());
                if (obj != null) {
                    plugin.debug("got schematic JSON");
                    // get dimensions
                    JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
                    h = dimensions.get("height").getAsInt();
                    w = dimensions.get("width").getAsInt();
                    c = dimensions.get("length").getAsInt();
                    if (schm.getPermission().equals("mechanical")) {
                        starty = 62;
                    } else if (TARDISConstants.HIGHER.contains(schm.getPermission())) {
                        starty = 65;
                    } else {
                        starty = 64;
                    }
                }
            }
            running = true;
        }
        if (level == (h - 1) && row == (w - 1)) {
            // we're finished
            plugin.getServer().getScheduler().cancelTask(taskId);
            taskId = 0;
            TARDISMessage.message(owner, "Display Block conversion complete");
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
                    TARDISDisplayItem tsd = TARDISMushroomBlockData.getTARDISBlock(data);
                    if (tsd != null) {
                        plugin.debug(tsd.getName());
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
