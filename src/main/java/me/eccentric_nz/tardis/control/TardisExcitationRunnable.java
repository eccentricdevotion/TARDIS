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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.utility.TardisParticles;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author eccentric_nz
 * <p>
 * Atmospheric excitation is an unnatural disturbance in the atmosphere which causes the weather to change. The Tenth
 * Doctor's sonic screwdriver, the tardis, and moving a planet can all cause atmospheric excitation.
 * <p>
 * The Tenth Doctor used a device above the inside of the door of the tardis to excite the atmosphere, causing snow, in
 * an attempt to cheer up Donna Noble.
 */
class TardisExcitationRunnable implements Runnable {

    private final TardisPlugin plugin;
    private final Location location;
    private final Player player;
    private final List<Block> snow = new ArrayList<>();
    public int task;
    private int i = 0;

    TardisExcitationRunnable(TardisPlugin plugin, Location l, Player player) {
        this.plugin = plugin;
        location = l;
        this.player = player;
    }

    @Override
    public void run() {
        if (i < 100) {
            TardisParticles.sendSnowParticles(location, player);
            player.getNearbyEntities(16.0, 16.0, 16.0).forEach((e) -> {
                if (e instanceof Player p) {
                    TardisParticles.sendSnowParticles(location, p);
                }
            });
            if (i % 5 == 0) {
                Location s = calculateLocationInCircle(location);
                s.setY(Objects.requireNonNull(location.getWorld()).getHighestBlockYAt(s) + 1);
                Block b = s.getBlock();
                if (b.isEmpty() && b.getRelative(BlockFace.DOWN).getType().isOccluding()) {
                    b.setBlockData(TardisConstants.SNOW);
                    snow.add(b);
                }
            }
            i++;
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            plugin.getTrackerKeeper().getExcitation().remove(player.getUniqueId());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> snow.forEach((block) -> block.setBlockData(TardisConstants.AIR)), 40L);
        }
    }

    private Location calculateLocationInCircle(Location location) {
        double angle = TardisConstants.RANDOM.nextDouble() * Math.PI * 2;
        double radius = TardisConstants.RANDOM.nextDouble() * 6;
        double x = radius * Math.cos(angle);
        double z = radius * Math.sin(angle);
        return location.clone().add(x, 0, z);
    }
}
