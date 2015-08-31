/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.junk;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.VortexEffect;
import de.slikey.effectlib.util.ParticleEffect;
import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkDestroyer implements Runnable {

    private final TARDIS plugin;
    private final TARDISMaterialisationData pdd;
    private int task;
    private int i = 0;
    private final int sx, ex, sy, ey, sz, ez;
    private final Location l;
    private final Location m;
    World world;
    Biome biome;
    private final EffectManager effectManager;

    public TARDISJunkDestroyer(TARDIS plugin, TARDISMaterialisationData pdd) {
        this.plugin = plugin;
        this.pdd = pdd;
        this.l = this.pdd.getLocation();
        this.m = l.clone().add(0.0d, 0.05d, 0.0d);
        this.ex = this.l.getBlockX() + 2;
        this.sx = this.l.getBlockX() - 3;
        this.sy = this.l.getBlockY();
        this.ey = this.l.getBlockY() + 5;
        this.ez = this.l.getBlockZ() + 3;
        this.sz = this.l.getBlockZ() - 2;
        this.world = this.l.getWorld();
        this.biome = this.pdd.getBiome();
        this.effectManager = new EffectManager(this.plugin);
    }

    @Override
    public void run() {
        // get relative locations
        if (i < 5) {
            i++;
            if (i == 5) {
                List<Chunk> chunks = new ArrayList<Chunk>();
                // remove blocks
                for (int level = ey; level >= sy; level--) {
                    for (int row = ex; row >= sx; row--) {
                        for (int col = sz; col <= ez; col++) {
                            Block b = world.getBlockAt(row, level, col);
                            b.setType(Material.AIR);
                            if (level == sy && (b.getBiome().equals(Biome.SKY) && !l.getWorld().getEnvironment().equals(World.Environment.THE_END)) && biome != null) {
                                if (!chunks.contains(b.getChunk())) {
                                    chunks.add(b.getChunk());
                                }
                                // reset the biome
                                try {
                                    world.setBiome(row, col, biome);
                                } catch (NullPointerException e) {
                                    // remove TARDIS from tracker
                                    plugin.getTrackerKeeper().getDematerialising().remove(Integer.valueOf(pdd.getTardisID()));
                                }
                            }
                        }
                        // refresh the chunks
                        for (Chunk chink : chunks) {
                            world.refreshChunk(chink.getX(), chink.getZ());
                        }
                        chunks.clear();
                    }
                }
                plugin.getTrackerKeeper().getDematerialising().remove(Integer.valueOf(pdd.getTardisID()));
                plugin.getTrackerKeeper().getInVortex().remove(Integer.valueOf(pdd.getTardisID()));
                effectManager.dispose();
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
            } else {
                if (plugin.getConfig().getBoolean("junk.particles") && plugin.getPM().isPluginEnabled("EffectLib")) {
                    // just animate particles
                    VortexEffect vortexEffect = new VortexEffect(effectManager);
                    vortexEffect.particle = ParticleEffect.SPELL;
                    vortexEffect.radius = 3;
                    vortexEffect.circles = 10;
                    vortexEffect.helixes = 10;
                    vortexEffect.setLocation(l);
                    vortexEffect.setTarget(m);
                    //vortexEffect.iterations = 5 * 20;
                    vortexEffect.start();
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
