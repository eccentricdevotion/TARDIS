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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISEffectLibHelper;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
    private final Location junkLoc;
    private Location vortexJunkLoc;
    World world;
    Biome biome;
    private final EffectManager effectManager;

    public TARDISJunkDestroyer(TARDIS plugin, TARDISMaterialisationData pdd) {
        this.plugin = plugin;
        this.pdd = pdd;
        this.junkLoc = this.pdd.getLocation();
        this.ex = this.junkLoc.getBlockX() + 2;
        this.sx = this.junkLoc.getBlockX() - 3;
        this.sy = this.junkLoc.getBlockY();
        this.ey = this.junkLoc.getBlockY() + 5;
        this.ez = this.junkLoc.getBlockZ() + 3;
        this.sz = this.junkLoc.getBlockZ() - 2;
        this.world = this.junkLoc.getWorld();
        this.biome = this.pdd.getBiome();
        this.effectManager = new EffectManager(this.plugin);
    }

    @Override
    public void run() {
        // get relative locations
        if (i < 5) {
            i++;
            if (i == 5) {
                // get junk vortex location
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    // teleport players to vortex
                    vortexJunkLoc = plugin.getLocationUtils().getLocationFromBukkitString(rs.getCreeper()).add(3.0d, 0.0d, 2.0d);
                    Entity orb = junkLoc.getWorld().spawnEntity(junkLoc, EntityType.EXPERIENCE_ORB);
                    List<Entity> ents = orb.getNearbyEntities(4.0d, 4.0d, 4.0d);
                    orb.remove();
                    for (Entity e : ents) {
                        if (e instanceof Player) {
                            final Player p = (Player) e;
                            final Location relativeLoc = getRelativeLocation(p);
                            p.teleport(relativeLoc);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    p.teleport(relativeLoc);
                                }
                            }, 2L);
                        }
                    }
                    TARDISJunkVortexRunnable runnable = new TARDISJunkVortexRunnable(plugin, vortexJunkLoc, pdd.getPlayer(), pdd.getTardisID());
                    int jvrtask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 1L, 20L);
                    runnable.setTask(jvrtask);
                }
                List<Chunk> chunks = new ArrayList<Chunk>();
                // remove blocks
                for (int level = ey; level >= sy; level--) {
                    for (int row = ex; row >= sx; row--) {
                        for (int col = sz; col <= ez; col++) {
                            Block b = world.getBlockAt(row, level, col);
                            b.setType(Material.AIR);
                            if (level == sy && (b.getBiome().equals(Biome.SKY) && !junkLoc.getWorld().getEnvironment().equals(World.Environment.THE_END)) && biome != null) {
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
                if (plugin.getConfig().getBoolean("junk.particles") && plugin.isEffectLibOnServer()) {
                    // just animate particles
                    TARDISEffectLibHelper.sendVortexParticles(junkLoc);
                }
            }
        }
    }

    private Location getRelativeLocation(Player p) {
        Location playerLoc = p.getLocation();
        double x = vortexJunkLoc.getX() + (playerLoc.getX() - junkLoc.getX());
        double y = vortexJunkLoc.getY() + (playerLoc.getY() - junkLoc.getY()) + 0.5d;
        double z = vortexJunkLoc.getZ() + (playerLoc.getZ() - junkLoc.getZ());
        return new Location(vortexJunkLoc.getWorld(), x, y, z, playerLoc.getYaw(), playerLoc.getPitch());
    }

    public void setTask(int task) {
        this.task = task;
    }
}
