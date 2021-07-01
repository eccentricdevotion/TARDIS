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
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.utility.TARDISParticles;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISJunkVortexRunnable implements Runnable {

    private static final int LOOPS = 12;
    private final TARDIS plugin;
    private final Location vortexJunkLoc;
    private final Location effectsLoc;
    private final Location destJunkLoc;
    private final OfflinePlayer player;
    private final int id;
    private int i = 0;
    private int task;
    private int fryTask;

    TARDISJunkVortexRunnable(TARDIS plugin, Location vortexJunkLoc, OfflinePlayer player, int id) {
        this.plugin = plugin;
        this.vortexJunkLoc = vortexJunkLoc;
        effectsLoc = this.vortexJunkLoc.clone().add(0.5d, 0, 0.5d);
        destJunkLoc = this.plugin.getGeneralKeeper().getJunkDestination();
        this.player = player;
        this.id = id;
    }

    @Override
    public void run() {
        if (i < LOOPS) {
            i++;
            if (i == 1) {
                fryTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISJunkItsDangerousRunnable(plugin, vortexJunkLoc), 0, 1L);
            }
            if (plugin.getConfig().getBoolean("junk.particles")) {
                plugin.getUtils().getJunkTravellers(vortexJunkLoc).forEach((e) -> {
                    if (e instanceof Player p) {
                        TARDISParticles.sendVortexParticles(effectsLoc, p);
                    }
                });
            }
            if (i == 2) {
                // play sound
                TARDISSounds.playTARDISSound(vortexJunkLoc, "junk_arc");
            }
            if (i == LOOPS - 1) {
                // build the TARDIS at the location
                BuildData bd = new BuildData(null);
                bd.setDirection(COMPASS.SOUTH);
                bd.setLocation(destJunkLoc);
                bd.setMalfunction(false);
                bd.setOutside(true);
                bd.setPlayer(player);
                bd.setRebuild(false);
                bd.setSubmarine(false);
                bd.setTardisID(id);
                bd.setThrottle(SpaceTimeThrottle.JUNK);
                plugin.getPresetBuilder().buildPreset(bd);
            }
            if (i == LOOPS) {
                // teleport players
                getJunkTravellers().forEach((e) -> {
                    if (e instanceof Player p) {
                        Location relativeLoc = getRelativeLocation(p);
                        p.teleport(relativeLoc);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> p.teleport(relativeLoc), 2L);
                    }
                });
                plugin.getServer().getScheduler().cancelTask(fryTask);
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
            }
        }
    }

    private List<Entity> getJunkTravellers() {
        // spawn an entity
        Entity orb = vortexJunkLoc.getWorld().spawnEntity(vortexJunkLoc, EntityType.EXPERIENCE_ORB);
        List<Entity> ents = orb.getNearbyEntities(4.0d, 4.0d, 4.0d);
        orb.remove();
        return ents;
    }

    private Location getRelativeLocation(Player p) {
        Location playerLoc = p.getLocation();
        double x = destJunkLoc.getX() + (playerLoc.getX() - vortexJunkLoc.getX());
        double y = destJunkLoc.getY() + (playerLoc.getY() - vortexJunkLoc.getY()) + 0.5d;
        double z = destJunkLoc.getZ() + (playerLoc.getZ() - vortexJunkLoc.getZ());
        return new Location(destJunkLoc.getWorld(), x, y, z, playerLoc.getYaw(), playerLoc.getPitch());
    }

    public void setTask(int task) {
        this.task = task;
    }
}
