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
package me.eccentric_nz.tardis.junk;

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetBlocks;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.destroyers.DestroyData;
import me.eccentric_nz.tardis.planets.TARDISBiome;
import me.eccentric_nz.tardis.utility.TARDISBlockSetters;
import me.eccentric_nz.tardis.utility.TARDISParticles;
import me.eccentric_nz.tardis.utility.TARDISSounds;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TARDISJunkDestroyer implements Runnable {

    private final TARDISPlugin plugin;
    private final DestroyData pdd;
    private final int startX, endX, startY, endY, startZ, endZ;
    private final Location junkLoc;
    private final Location effectsLoc;
    private final World world;
    private final TARDISBiome biome;
    private int task;
    private int i = 0;
    private Location vortexJunkLoc;
    private int fryTask;

    public TARDISJunkDestroyer(TARDISPlugin plugin, DestroyData pdd) {
        this.plugin = plugin;
        this.pdd = pdd;
        junkLoc = this.pdd.getLocation();
        effectsLoc = junkLoc.clone().add(0.5d, 0, 0.5d);
        startX = junkLoc.getBlockX() - 3;
        endX = junkLoc.getBlockX() + 2;
        startY = junkLoc.getBlockY();
        endY = junkLoc.getBlockY() + 5;
        startZ = junkLoc.getBlockZ() - 2;
        endZ = junkLoc.getBlockZ() + 3;
        world = junkLoc.getWorld();
        biome = this.pdd.getTardisBiome();
    }

    @Override
    public void run() {
        // get relative locations
        if (i < 25) {
            i++;
            if (i == 1) {
                getJunkTravellers().forEach((e) -> {
                    if (e instanceof Player) {
                        Player p = (Player) e;
                        plugin.getGeneralKeeper().getJunkTravellers().add(p.getUniqueId());
                    }
                });
                TARDISSounds.playTARDISSound(junkLoc, "junk_takeoff");
                fryTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISJunkItsDangerousRunnable(plugin, junkLoc), 0, 1L);
            }
            if (i == 25) {
                // get junk vortex location
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
                if (rs.resultSet()) {
                    // teleport players to vortex
                    vortexJunkLoc = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getTardis().getCreeper()).add(3.0d, 0.0d, 2.0d);
                    getJunkTravellers().forEach((e) -> {
                        if (e instanceof Player) {
                            Player p = (Player) e;
                            Location relativeLoc = getRelativeLocation(p);
                            p.teleport(relativeLoc);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> p.teleport(relativeLoc), 2L);
                        }
                    });
                    TARDISJunkVortexRunnable runnable = new TARDISJunkVortexRunnable(plugin, vortexJunkLoc, pdd.getPlayer(), pdd.getTardisId());
                    int jvrtask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 1L, 20L);
                    runnable.setTask(jvrtask);
                }
                // what biome?
                Biome b = null;
                if (biome.getKey().getNamespace().equalsIgnoreCase("minecraft")) {
                    try {
                        b = Biome.valueOf(biome.name());
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
                List<Chunk> chunks = new ArrayList<>();
                // remove blocks
                for (int row = startX; row <= endX; row++) {
                    for (int column = startY; column <= endY; column++) {
                        for (int aisle = startZ; aisle <= endZ; aisle++) {
                            Block block = world.getBlockAt(row, column, aisle);
                            block.setBlockData(TARDISConstants.AIR);
                        }
                    }
                }
                plugin.getTrackerKeeper().getDematerialising().remove(pdd.getTardisId());
                plugin.getTrackerKeeper().getInVortex().remove(pdd.getTardisId());
                // check protected blocks if has block material and data stored then put the block back!
                HashMap<String, Object> tid = new HashMap<>();
                tid.put("tardis_id", pdd.getTardisId());
                ResultSetBlocks rsb = new ResultSetBlocks(plugin, tid, true);
                if (rsb.resultSet()) {
                    rsb.getData().forEach((rp) -> {
                        int rx = rp.getLocation().getBlockX();
                        int ry = rp.getLocation().getBlockY();
                        int rz = rp.getLocation().getBlockZ();
                        TARDISBlockSetters.setBlock(world, rx, ry, rz, rp.getBlockData());
                    });
                }
                // remove block protection
                plugin.getPresetDestroyer().removeBlockProtection(pdd.getTardisId());
                plugin.getServer().getScheduler().cancelTask(fryTask);
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
            } else if (plugin.getConfig().getBoolean("junk.particles")) {
                // just animate particles
                plugin.getUtils().getJunkTravellers(junkLoc).forEach((e) -> {
                    if (e instanceof Player) {
                        Player p = (Player) e;
                        TARDISParticles.sendVortexParticles(effectsLoc, p);
                    }
                });
            }
        }
    }

    private Location getRelativeLocation(Player p) {
        Location playerLoc = p.getLocation();
        double x = vortexJunkLoc.getX() + (playerLoc.getX() - junkLoc.getX());
        double y = vortexJunkLoc.getY() + (playerLoc.getY() - junkLoc.getY()) + 0.5d;
        double z = vortexJunkLoc.getZ() + (playerLoc.getZ() - junkLoc.getZ());
        Location l = new Location(vortexJunkLoc.getWorld(), x, y, z, playerLoc.getYaw(), playerLoc.getPitch());
        while (!l.getChunk().isLoaded()) {
            l.getChunk().load();
        }
        return l;
    }

    private List<Entity> getJunkTravellers() {
        // spawn an entity
        Entity orb = Objects.requireNonNull(junkLoc.getWorld()).spawnEntity(junkLoc, EntityType.EXPERIENCE_ORB);
        List<Entity> ents = orb.getNearbyEntities(4.0, 4.0, 4.0);
        orb.remove();
        return ents;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
