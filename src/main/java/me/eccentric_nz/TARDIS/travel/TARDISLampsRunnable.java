/*
 * Copyright (C) 2014 eccentric_nz
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
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Phosphor lamps are used for lighting. They use electron excitation; when
 * shaken, they grow brighter.
 *
 * @author eccentric_nz
 */
public class TARDISLampsRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<Block> lamps;
    private final long start;
    private final Material light;
    private final boolean use_wool;
    private final boolean lights_on;
    private int task;
    private Location handbrake_loc;

    public TARDISLampsRunnable(TARDIS plugin, List<Block> lamps, long start, Material light, boolean use_wool) {
        this.plugin = plugin;
        this.lamps = lamps;
        this.start = start;
        this.light = light;
        this.use_wool = use_wool;
        this.lights_on = (lamps.get(0).getType().equals(this.light));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        // play smoke effect
        for (int j = 0; j < 9; j++) {
            handbrake_loc.getWorld().playEffect(handbrake_loc, Effect.SMOKE, j);
        }
        for (Block b : lamps) {
            if (b.getType().equals(light)) {
                if (use_wool) {
                    b.setType(Material.WOOL);
                    b.setData((byte) 15);
                } else {
                    b.setType(Material.SPONGE);
                }
            } else if (b.getType().equals(Material.SPONGE) || (b.getType().equals(Material.WOOL) && b.getData() == (byte) 15)) {
                b.setType(light);
            }
        }
        if (System.currentTimeMillis() > start) {
            // set all lamps back to whatever they were when the malfunction happened
            if (lights_on) {
                for (Block b : lamps) {
                    if (b.getType().equals(Material.SPONGE) || (b.getType().equals(Material.WOOL) && b.getData() == (byte) 15)) {
                        b.setType(light);
                    }
                }
            } else {
                for (Block b : lamps) {
                    if (b.getType().equals(light)) {
                        if (use_wool) {
                            b.setType(Material.WOOL);
                            b.setData((byte) 15);
                        } else {
                            b.setType(Material.SPONGE);
                        }
                    }
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
