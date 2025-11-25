/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;

public class TARDISArtronFurnaceParticle {

    private final TARDIS plugin;

    public TARDISArtronFurnaceParticle(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addParticles() {

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> plugin.getServer().getOnlinePlayers().forEach((player) -> {
            Location loc = player.getLocation();
            if (loc.getChunk().isLoaded()) {
                int sx = loc.getBlockX() - 8;
                int sy = loc.getBlockY() - 4;
                int sz = loc.getBlockZ() - 8;
                World world = loc.getWorld();
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            Block block = world.getBlockAt(sx + x, sy + y, sz + z);
                            if (isArtronFurnace(block)) {
                                player.spawnParticle(Particle.SPLASH, block.getLocation().add(0.5d, 1.0d, 0.5d), 10);
                            }
                        }
                    }
                }
            }
        }), 60L, 30L);
    }

    private boolean isArtronFurnace(Block b) {
        return b != null && b.getType().equals(Material.FURNACE) && plugin.getTardisHelper().isArtronFurnace(b);
    }
}
