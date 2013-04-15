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
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Lever;

/**
 * Phosphor levers are used for lighting. They use electron excitation; when
 * shaken, they grew brighter.
 *
 * @author eccentric_nz
 */
public class TARDISLeversRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<Block> levers;
    private final long start;
    private int task;
    private Location handbrake_loc;

    public TARDISLeversRunnable(TARDIS plugin, List<Block> levers, long start) {
        this.plugin = plugin;
        this.levers = levers;
        this.start = start;
    }

    @Override
    public void run() {
        // play smoke effect
        for (int j = 0; j < 9; j++) {
            handbrake_loc.getWorld().playEffect(handbrake_loc, Effect.SMOKE, j);
        }
        for (Block b : levers) {
            if (b.getTypeId() == 69) {
                BlockState state = b.getState();
                Lever l = (Lever) state.getData();
                if (l.isPowered()) {
                    l.setPowered(false);
                } else {
                    l.setPowered(true);
                }
                state.setData(l);
                state.update();
            }
        }
        if (System.currentTimeMillis() > start) {
            // set all levers back to on
            for (Block b : levers) {
                if (b.getTypeId() == 69) {
                    BlockState state = b.getState();
                    Lever l = (Lever) state.getData();
                    l.setPowered(true);
                    state.setData(l);
                    state.update();
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
