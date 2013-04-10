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
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Phosphor lamps are used for lighting. They use electron excitation; when
 * shaken, they grew brighter.
 *
 * @author eccentric_nz
 */
public class TARDISLampsRunnable implements Runnable {

    private final TARDIS plugin;
    private final List<Block> lamps;
    private final long start;
    private int task;

    public TARDISLampsRunnable(TARDIS plugin, List<Block> lamps, long start) {
        this.plugin = plugin;
        this.lamps = lamps;
        this.start = start;
    }

    @Override
    public void run() {
        for (Block b : lamps) {
            if (b.getType() == Material.REDSTONE_LAMP_OFF) {
                b.setType(Material.REDSTONE_LAMP_ON);
                plugin.debug("on");
            } else {
                b.setType(Material.REDSTONE_LAMP_OFF);
                plugin.debug("off");
            }
        }
        if (System.currentTimeMillis() > start) {
            plugin.debug("Cancelling malfunction...");
            // set all lamps back to on
            for (Block b : lamps) {
                b.setType(Material.REDSTONE_LAMP_ON);
            }
            plugin.getServer().getScheduler().cancelTask(task);
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
