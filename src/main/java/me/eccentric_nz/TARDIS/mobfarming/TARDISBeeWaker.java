/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetApiaries;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Beehive;
import org.bukkit.block.Block;
import org.bukkit.entity.Bee;

public class TARDISBeeWaker implements Runnable {

    private final TARDIS plugin;

    public TARDISBeeWaker(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        World world = plugin.getServer().getWorlds().get(0);
        if (world != null) {
            long ticks = world.getTime();
            if (ticks > 0 && ticks < 600) {
                wakeBees();
            }
        }
    }

    private void wakeBees() {
        // get apiaries
        ResultSetApiaries rsa = new ResultSetApiaries(plugin);
        if (rsa.resultSet()) {
            for (Location location : rsa.getData()) {
                // scan chunk for beehives and bee nests
                Chunk chunk = location.getChunk();
                if (chunk.isLoaded()) {
                    int cx = chunk.getX() << 4; // chunks x
                    int cz = chunk.getZ() << 4; // chunks z
                    for (int x = cx; x < cx + 16; x++) {
                        for (int z = cz; z < cz + 16; z++) {
                            for (int y = 51; y < 96; y++) { // limit Y values to within TARDIS ARS levels
                                Block block = chunk.getWorld().getBlockAt(x, y, z);
                                Material material = block.getType();
                                if (material.equals(Material.BEE_NEST) || material.equals(Material.BEEHIVE)) {
                                    Beehive beehive = (Beehive) block.getState();
                                    for (Bee bee : beehive.releaseEntities()) {
                                        int random = TARDISConstants.RANDOM.nextInt(1200) + 1200;
                                        bee.setCannotEnterHiveTicks(random);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
