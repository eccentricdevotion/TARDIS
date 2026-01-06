/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.particles.TARDISParticles;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class JunkDestroyer implements Runnable {

    private final TARDIS plugin;
    private final DestroyData pdd;
    private final int sx, ex, sy, ey, sz, ez;
    private final Location junkLoc;
    private final Location effectsLoc;
    private final World world;
    private int task;
    private int i = 0;
    private Location vortexJunkLoc;
    private int fryTask;

    public JunkDestroyer(TARDIS plugin, DestroyData pdd) {
        this.plugin = plugin;
        this.pdd = pdd;
        junkLoc = this.pdd.getLocation();
        effectsLoc = junkLoc.clone().add(0.5d, 0, 0.5d);
        ex = junkLoc.getBlockX() + 2;
        sx = junkLoc.getBlockX() - 3;
        sy = junkLoc.getBlockY();
        ey = junkLoc.getBlockY() + 5;
        ez = junkLoc.getBlockZ() + 3;
        sz = junkLoc.getBlockZ() - 2;
        world = junkLoc.getWorld();
    }

    @Override
    public void run() {
        // get relative locations
        if (i < 25) {
            i++;
            if (i == 1) {
                // set chunk not to be force loaded
                if (plugin.getConfig().getInt("junk.return") > 0) {
                    junkLoc.getChunk().removePluginChunkTicket(plugin);
                }
                getJunkTravellers().forEach((e) -> {
                    if (e instanceof Player p) {
                        plugin.getGeneralKeeper().getJunkTravellers().add(p.getUniqueId());
                    }
                });
                TARDISSounds.playTARDISSound(junkLoc, "junk_takeoff");
                fryTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new JunkItsDangerousRunnable(plugin, junkLoc), 0, 1L);
            }
            if (i == 25) {
                // get junk vortex location
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", "00000000-aaaa-bbbb-cccc-000000000000");
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    // teleport players to vortex
                    vortexJunkLoc = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getTardis().getCreeper()).add(3.0d, 0.0d, 2.0d);
                    getJunkTravellers().forEach((e) -> {
                        if (e instanceof Player p) {
                            Location relativeLoc = getRelativeLocation(p);
                            p.teleport(relativeLoc);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                p.teleport(relativeLoc);
                                p.setPlayerTime(18000, false);
                            }, 2L);
                        }
                    });
                    JunkVortexRunnable runnable = new JunkVortexRunnable(plugin, vortexJunkLoc, pdd.getPlayer(), pdd.getTardisID());
                    int jvrtask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 1L, 20L);
                    runnable.setTask(jvrtask);
                }
                // remove blocks
                for (int level = ey; level >= sy; level--) {
                    for (int row = ex; row >= sx; row--) {
                        for (int col = sz; col <= ez; col++) {
                            Block block = world.getBlockAt(row, level, col);
                            block.setBlockData(TARDISConstants.AIR);
                            TARDISDisplayItemUtils.remove(block);
                        }
                    }
                }
                plugin.getTrackerKeeper().getDematerialising().remove(pdd.getTardisID());
                plugin.getTrackerKeeper().getInVortex().remove(pdd.getTardisID());
                // check protected blocks if it has block material and data stored then put the block back!
                HashMap<String, Object> tid = new HashMap<>();
                tid.put("tardis_id", pdd.getTardisID());
                ResultSetBlocks rsb = new ResultSetBlocks(plugin, tid, true);
                rsb.resultSetAsync((hasResult, resultSetBlocks) -> {
                    if (hasResult) {
                        resultSetBlocks.getData().forEach((rp) -> {
                            int rx = rp.getLocation().getBlockX();
                            int ry = rp.getLocation().getBlockY();
                            int rz = rp.getLocation().getBlockZ();
                            TARDISBlockSetters.setBlock(world, rx, ry, rz, rp.getBlockData());
                        });
                    }
                });
                // remove block protection
                plugin.getPresetDestroyer().removeBlockProtection(pdd.getTardisID());
                plugin.getServer().getScheduler().cancelTask(fryTask);
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
            } else if (plugin.getConfig().getBoolean("junk.particles")) {
                // just animate particles
                plugin.getUtils().getJunkTravellers(junkLoc).forEach((e) -> {
                    if (e instanceof Player p) {
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
        Entity orb = junkLoc.getWorld().spawnEntity(junkLoc, EntityType.EXPERIENCE_ORB);
        List<Entity> ents = orb.getNearbyEntities(4.0, 4.0, 4.0);
        orb.remove();
        return ents;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
