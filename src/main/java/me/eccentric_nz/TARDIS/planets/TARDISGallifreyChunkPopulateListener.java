/*
 * Copyright (C) 2017 eccentric_nz
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

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

/**
 *
 * @author eccentric_nz
 *
 * From the planet's surface, Gallifrey boasts an orange sky at night,
 * snow-capped mountains, fields of red grass, and trees with bright silver
 * leaves. These reflect the morning sunlight, making it look like the forests
 * are on fire. There are also green forests, golden fields and red deserts, but
 * overall it is a much drier world than Earth. The Sixth Doctor once declared
 * the climate to be "like the Serengeti all year round".
 */
public class TARDISGallifreyChunkPopulateListener implements Listener {

    private final TARDIS plugin;
    private final List<Chunk> chunks = new ArrayList<>();

    public TARDISGallifreyChunkPopulateListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkPopulate(ChunkPopulateEvent event) {
        Chunk chunk = event.getChunk();
        // check world
        if (!chunk.getWorld().getName().equals("Gallifrey")) {
            return;
        }
        if (!chunk.getBlock(7, 63, 7).getBiome().toString().equals("GALLIFREY")) {
            return;
        }
        if (chunks.contains(chunk)) {
            return;
        }
        // scan chunk for STRUCTURE_BLOCK between y = 50 , 70
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 50; y < 71; y++) {
                    if (chunk.getBlock(x, y, z).getType().equals(Material.STRUCTURE_BLOCK)) {
                        buildStructure(chunk, x, y, z);
                        return;
                    }
                }
            }
        }
    }

    private void buildStructure(Chunk chunk, int x, int y, int z) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            chunks.add(chunk);
            // remove STRUCTURE_BLOCK so we don't get this chunk again
            Block structure = chunk.getBlock(x, y, z);
            structure.setType(Material.RED_SAND);
            // create structure
            new TARDISBuildGallifreyanStructure(plugin).buildCity(chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z);
        }, 1L);
    }
}
