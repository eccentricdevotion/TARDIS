/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * TARDIS corridors normally used anti-gravity, as the corridors went vertically
 * as well as horizontally. House disabled the anti-gravity in the corridors in
 * an attempt to kill Amy Pond and Rory Williams.
 *
 * @author eccentric_nz
 */
public class TARDISGravityWellRunnable implements Runnable {

    TARDIS plugin;
    Player p;
    double up;
    double end;
    int task, x, z;

    public TARDISGravityWellRunnable(TARDIS plugin, Player p, double up, double end, int x, int z) {
        this.plugin = plugin;
        this.p = p;
        this.up = up;
        this.end = end;
        this.x = x;
        this.z = z;
    }

    public void run() {
        if (p.getLocation().getY() < end && p.getLocation().getBlockX() == x && p.getLocation().getBlockZ() == z) {
            p.setVelocity(new Vector(0.0D, up, 0.0D));
        } else {
            p.setFallDistance(0.0F);
            plugin.getServer().getScheduler().cancelTask(task);
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}