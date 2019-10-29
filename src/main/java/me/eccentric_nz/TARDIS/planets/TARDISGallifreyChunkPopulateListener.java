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
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Chunk;
import org.bukkit.Material;
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
public class TARDISGallifreyChunkPopulateListener implements Listener {

    private final TARDIS plugin;
    private final List<Chunk> chunks = new ArrayList<>();
    private boolean isBuilding = false;
    private long timeCheck;

    public TARDISGallifreyChunkPopulateListener(TARDIS plugin) {
        this.plugin = plugin;
        timeCheck = System.currentTimeMillis() + 5000;
    }

    @EventHandler(ignoreCancelled = true)
    public void gallifreyOnChunkPopulate(ChunkPopulateEvent event) {
        Chunk chunk = event.getChunk();
        // check world
        if (!chunk.getWorld().getName().equalsIgnoreCase("Gallifrey")) {
            return;
        }
        if (chunks.contains(chunk) || isBuilding) {
            return;
        }
        // scan chunk for GOLD_ORE between y = 50 , 70
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 45; y < 66; y++) {
                    if (chunk.getBlock(x, y, z).getType().equals(Material.GOLD_ORE)) {
                        int hy = chunk.getWorld().getHighestBlockYAt(x, z);
                        if (System.currentTimeMillis() < timeCheck) {
                            return;
                        }
                        buildStructure(chunk, x, hy, z);
                        return;
                    }
                }
            }
        }
    }

    private void buildStructure(Chunk chunk, int x, int y, int z) {
        timeCheck = System.currentTimeMillis() + 5000;
        isBuilding = true;
        chunks.add(chunk);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // create structure
            if (TARDISConstants.RANDOM.nextBoolean()) {
                isBuilding = new TARDISBuildGallifreyanStructure(plugin).buildCity(chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z);
            } else {
                isBuilding = false;
            }
        }, 1L);
    }
}
