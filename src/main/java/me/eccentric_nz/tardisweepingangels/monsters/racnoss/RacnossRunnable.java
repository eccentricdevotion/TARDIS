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
package me.eccentric_nz.tardisweepingangels.monsters.racnoss;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import me.eccentric_nz.tardisweepingangels.utils.WorldGuardChecker;
import me.eccentric_nz.tardisweepingangels.utils.WorldProcessor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class RacnossRunnable implements Runnable {

    private final TARDISWeepingAngels plugin;
    private final int spawn_rate;

    public RacnossRunnable(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        spawn_rate = plugin.getConfig().getInt("spawn_rate.how_many");
    }

    @Override
    public void run() {
        plugin.getServer().getWorlds().forEach((w) -> {
            // only configured worlds
            String name = WorldProcessor.sanitiseName(w.getName());
            if (plugin.getConfig().getInt("racnoss.worlds." + name) > 0) {
                if (w.getEnvironment() != Environment.NETHER) {
                    plugin.debug("Tried to spawn Racnoss in non-Nether world, please remove " + w.getName() + " from the racnoss worlds configuration!");
                    return;
                }
                // get the current warriors
                int racnoss = 0;
                Collection<PiglinBrute> brutes = w.getEntitiesByClass(PiglinBrute.class);
                for (PiglinBrute b : brutes) {
                    PersistentDataContainer pdc = b.getPersistentDataContainer();
                    if (pdc.has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)) {
                        racnoss++;
                    }
                }
                if (racnoss < plugin.getConfig().getInt("racnoss.worlds." + name)) {
                    // if less than maximum, spawn some more
                    for (int i = 0; i < spawn_rate; i++) {
                        spawnRacnoss(w);
                    }
                }
            }
        });
    }

    private void spawnRacnoss(World world) {
        Chunk[] chunks = world.getLoadedChunks();
        if (chunks.length > 0) {
            Chunk chunk = chunks[TARDISWeepingAngels.random.nextInt(chunks.length)];
            int x = chunk.getX() * 16 + TARDISWeepingAngels.random.nextInt(16);
            int z = chunk.getZ() * 16 + TARDISWeepingAngels.random.nextInt(16);
            int y = getHighestNetherBlock(world, x, z);
            Location l = new Location(world, x, y + 1, z);
            if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null && !WorldGuardChecker.canSpawn(l)) {
                return;
            }
            LivingEntity racnoss = (LivingEntity) world.spawnEntity(l, EntityType.PIGLIN_BRUTE);
            racnoss.setSilent(true);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                new Equipper(Monster.RACNOSS, racnoss, false, false).setHelmetAndInvisibilty();
                plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(racnoss, EntityType.PIGLIN_BRUTE, Monster.RACNOSS, l));
            }, 5L);
        }
    }

    private int getHighestNetherBlock(World w, int wherex, int wherez) {
        int y = 100;
        Block startBlock = w.getBlockAt(wherex, y, wherez);
        while (!startBlock.getType().isAir()) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
        }
        int air = 0;
        while (startBlock.getType().isAir() && startBlock.getLocation().getBlockY() > 30) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
            air++;
        }
        Material mat = startBlock.getType();
        if (air >= 5 && goodNether.contains(mat)) {
            y = startBlock.getLocation().getBlockY() + 1;
        }
        return y;
    }

    private final List<Material> goodNether = Arrays.asList(Material.NETHERRACK, Material.SOUL_SAND, Material.GLOWSTONE, Material.NETHER_BRICK, Material.NETHER_BRICK_FENCE, Material.NETHER_BRICK_STAIRS);
}
