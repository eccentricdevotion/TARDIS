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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.travel;

import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Phosphor lamps are used for lighting. They use electron excitation; when
 * shaken, they grow brighter.
 *
 * @author eccentric_nz
 */
public class TARDISLeversRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<Block> lamps;
    private final long start;
    private int task;
    private Location handbrake_loc;

    public TARDISLeversRunnable(TARDIS plugin, List<Block> lamps, long start) {
        this.plugin = plugin;
        this.lamps = lamps;
        this.start = start;
    }

    @Override
    public void run() {
        // play smoke effect
        for (int j = 0; j < 9; j++) {
            handbrake_loc.getWorld().playEffect(handbrake_loc, Effect.SMOKE, j);
        }
        for (Block b : lamps) {
            if (b.getTypeId() == 124) {
                b.setTypeId(19);
            } else {
                b.setTypeId(124);
            }
        }
        if (System.currentTimeMillis() > start) {
            // set all lamps back to on
            for (Block b : lamps) {
                if (b.getTypeId() == 19) {
                    b.setTypeId(124);
                }
            }
            plugin.getServer().getScheduler().cancelTask(task);
        }
    }

    public void setTask(int task) {
        this.task = task;
    }

    public void setHandbrake(Location handbrake_loc) {
        this.handbrake_loc = handbrake_loc;
    }
}
