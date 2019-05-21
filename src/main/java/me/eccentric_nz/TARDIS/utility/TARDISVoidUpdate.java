/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.HashMap;

import static me.eccentric_nz.TARDIS.utility.TARDISSpiral.SPIRAL;

/**
 * @author eccentric_nz
 */
public class TARDISVoidUpdate {

    private final TARDIS plugin;
    private final int id;
    private int taskID;

    public TARDISVoidUpdate(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public void updateBiome() {
        // get TIPS slot
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            // get start chunk for this TARDIS
            String[] cstr = tardis.getChunk().split(":");
            World w = plugin.getServer().getWorld(cstr[0]);
            int cx = TARDISNumberParsers.parseInt(cstr[1]);
            int cz = TARDISNumberParsers.parseInt(cstr[2]);
            taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Updater(w, cx, cz), 1L, 20L);
        }
    }

    class Updater implements Runnable {

        private final World world;
        private final int cx;
        private final int cz;
        private int idx = 0;

        Updater(World world, int cx, int cz) {
            this.world = world;
            this.cx = cx;
            this.cz = cz;
        }

        @Override
        public void run() {
            Chunk chunk = world.getChunkAt(cx, cz);
            if (!chunk.isLoaded()) {
                chunk.load();
            }
            int sx = (cx + SPIRAL.get(idx).x) * 16;
            int ex = sx + 16;
            int sz = (cz + SPIRAL.get(idx).y) * 16;
            int ez = sz + 16;
            if (world.getBlockAt(sx, 64, sz).getBiome().equals(Biome.THE_END)) {
                for (int x = sx; x < ex; x++) {
                    for (int z = sz; z < ez; z++) {
                        world.setBiome(x, z, Biome.THE_VOID);
                    }
                }
            }
            plugin.getTardisHelper().refreshChunk(chunk);
            idx++;
            if (idx == 81) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                taskID = 0;
            }
        }
    }
}
