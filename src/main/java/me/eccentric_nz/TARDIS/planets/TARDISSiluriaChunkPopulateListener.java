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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
public class TARDISSiluriaChunkPopulateListener implements Listener {

    private final TARDIS plugin;
    private final List<Chunk> chunks = new ArrayList<>();
    private boolean isBuilding = false;

    public TARDISSiluriaChunkPopulateListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void siluriaOnChunkPopulate(ChunkPopulateEvent event) {
        Chunk chunk = event.getChunk();
        // check world
        if (!chunk.getWorld().getName().equalsIgnoreCase("Siluria")) {
            return;
        }
        if (chunks.contains(chunk) || isBuilding) {
            return;
        }
        // find water
        int cx = chunk.getX() * 16;
        int cz = chunk.getZ() * 16;
        int hy = chunk.getWorld().getHighestBlockYAt(cx + 8, cz + 8);
        Block water = chunk.getWorld().getBlockAt(cx + 8, hy, cz + 8);
        if (water.getType() == Material.AIR) {
            while (water.getType() == Material.AIR) {
                water = water.getRelative(BlockFace.DOWN);
            }
            hy = water.getLocation().getBlockY() - 1;
        }
        if (water.getType().equals(Material.WATER)) {
            buildStructure(chunk, cx, hy, cz);
        }
    }

    private void buildStructure(Chunk chunk, int x, int y, int z) {
        isBuilding = true;
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            chunks.add(chunk);
            // create structure
            isBuilding = new TARDISBuildSilurianStructure(plugin).buildCity(x, y, z);
        }, 2L);
    }
}
