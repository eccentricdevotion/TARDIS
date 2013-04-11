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
    private Location handbrake;
    private Random rand = new Random();

    public TARDISLeversRunnable(TARDIS plugin, List<Block> levers, long start) {
        this.plugin = plugin;
        this.levers = levers;
        this.start = start;
    }

    @Override
    public void run() {
        plugin.debug(handbrake.getWorld().getName());
        // play smoke effect
        for (int j = 0; j < 9; j++) {
            handbrake.getWorld().playEffect(handbrake, Effect.SMOKE, j);
        }
        for (Block b : levers) {
            BlockState state = b.getState();
            Lever l = (Lever) state.getData();
            if (l.isPowered()) {
                l.setPowered(false);
                plugin.debug("off");
            } else {
                l.setPowered(true);
                plugin.debug("on");
            }
            state.setData(l);
            state.update();
        }
        if (System.currentTimeMillis() > start) {
            plugin.debug("Cancelling malfunction...");
            // set all levers back to on
            for (Block b : levers) {
                BlockState state = b.getState();
                Lever l = (Lever) state.getData();
                l.setPowered(true);
            }
            plugin.getServer().getScheduler().cancelTask(task);
        }
    }

    public void setTask(int task) {
        this.task = task;
    }

    public void setHandbrake(Location handbrake) {
        this.handbrake = handbrake;
    }
}
