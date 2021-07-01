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
package me.eccentric_nz.tardis.rooms;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * TARDIS corridors normally used anti-gravity, as the corridors went vertically as well as horizontally. House disabled
 * the anti-gravity in the corridors in an attempt to kill Amy Pond and Rory Williams.
 *
 * @author eccentric_nz
 */
public class TardisGravityWellRunnable implements Runnable {

    private final TardisPlugin plugin;
    private final Player p;
    private final double up;
    private final double end;
    private final int x;
    private final int z;
    private final int dir;
    private int task;

    public TardisGravityWellRunnable(TardisPlugin plugin, Player p, double up, double end, int x, int z, int dir) {
        this.plugin = plugin;
        this.p = p;
        this.up = up;
        this.end = end;
        this.x = x;
        this.z = z;
        this.dir = dir;
    }

    @Override
    public void run() {
        switch (dir) {
            case 1:
                if (p.getLocation().getY() < end && p.getLocation().getBlockX() == x && p.getLocation().getBlockZ() == z) {
                    p.setVelocity(new Vector(0.0D, up, 0.0D));
                } else {
                    p.setFallDistance(0.0F);
                    plugin.getServer().getScheduler().cancelTask(task);
                }
                break;
            case 2:
                if (p.getLocation().getZ() > end && p.getLocation().getBlockX() == x) {
                    p.setVelocity(new Vector(0.0D, 0.0D, -up));
                } else {
                    plugin.getServer().getScheduler().cancelTask(task);
                }
                break;
            case 3:
                if (p.getLocation().getX() > end && p.getLocation().getBlockZ() == z) {
                    p.setVelocity(new Vector(-up, 0.0D, 0.0D));
                } else {
                    plugin.getServer().getScheduler().cancelTask(task);
                }
                break;
            case 4:
                if (p.getLocation().getZ() < end && p.getLocation().getBlockX() == x) {
                    p.setVelocity(new Vector(0.0D, 0.0D, up));
                } else {
                    plugin.getServer().getScheduler().cancelTask(task);
                }
                break;
            case 5:
                if (p.getLocation().getX() < end && p.getLocation().getBlockZ() == z) {
                    p.setVelocity(new Vector(up, 0.0D, 0.0D));
                } else {
                    plugin.getServer().getScheduler().cancelTask(task);
                }
                break;
            default:
                break;
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
