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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 * <p>
 * From the planet's surface, Gallifrey boasts an orange sky at night, snow-capped mountains, fields of red grass, and
 * trees with bright silver leaves. These reflect the morning sunlight, making it look like the forests are on fire.
 * There are also green forests, golden fields and red deserts, but overall it is a much drier world than Earth. The
 * Sixth Doctor once declared the climate to be "like the Serengeti all year round".
 */
public class TARDISSkaroChunkPopulateListener implements Listener {

    private final TARDIS plugin;
    private final List<ChunkInfo> chunks = new ArrayList<>();
    private long timeCheck;

    public TARDISSkaroChunkPopulateListener(TARDIS plugin) {
        this.plugin = plugin;
        timeCheck = System.currentTimeMillis() + 3000;
    }

    @EventHandler(ignoreCancelled = true)
    public void skaroOnChunkPopulate(ChunkPopulateEvent event) {
        Chunk chunk = event.getChunk();
        // check world
        if (!chunk.getWorld().getName().endsWith("skaro")) {
            return;
        }
        ChunkInfo chunkInfo = new ChunkInfo(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
        if (chunks.contains(chunkInfo)) {
            return;
        }
        // scan chunk for COAL_ORE between y = 50 , 70
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 45; y < 66; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType().equals(Material.COAL_ORE)) {
                        int hy = chunk.getWorld().getHighestBlockYAt(block.getLocation()) + 1;
                        if (System.currentTimeMillis() < timeCheck) {
                            return;
                        }
                        // don't build structures on top of trees
                        Block sand = chunk.getBlock(x, hy - 1, z);
                        if (sand.getType().equals(Material.SAND)) {
                            buildStructure(chunk, chunkInfo, x, hy, z);
                        }
                        return;
                    }
                }
            }
        }
    }

    private void buildStructure(Chunk chunk, ChunkInfo chunkInfo, int x, int y, int z) {
        timeCheck = System.currentTimeMillis() + 2000;
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            chunks.add(chunkInfo);
            // create structure
            TARDISBuildSkaroStructure tbss = new TARDISBuildSkaroStructure(plugin, chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, tbss, 1L, 1L);
            tbss.setTask(task);
        }, 2L);
    }
}
