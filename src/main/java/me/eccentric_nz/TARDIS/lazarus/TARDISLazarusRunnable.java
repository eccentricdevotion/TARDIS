/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * The Genetic Manipulation Device uses hypersonic sound waves to destabilise the cell structure, then a metagenic
 * programme to manipulate the coding in the protein strands.
 *
 * @author eccentric_nz
 */
class TARDISLazarusRunnable implements Runnable {

    private final TARDIS plugin;
    private final Block b;
    private int taskID;
    private static final int LOOPS = 12;
    private int i = 0;

    public TARDISLazarusRunnable(TARDIS plugin, Block b) {
        this.plugin = plugin;
        this.b = b;
    }

    @Override

    public void run() {
        if (i < LOOPS) {
            plugin.getGeneralKeeper().getFaces().forEach((face) -> {
                if ((i % 4) == face.ordinal()) {
                    // set mossy
                    b.getRelative(face).setType(Material.MOSSY_COBBLESTONE_WALL);
                    b.getRelative(face).getRelative(BlockFace.UP).setType(Material.MOSSY_COBBLESTONE_WALL);
                } else {
                    // set plain
                    b.getRelative(face).setType(Material.COBBLESTONE_WALL);
                    b.getRelative(face).getRelative(BlockFace.UP).setType(Material.COBBLESTONE_WALL);
                }
            });
            i++;
        } else {
            plugin.getGeneralKeeper().getFaces().forEach((face) -> {
                b.getRelative(face).setType(Material.COBBLESTONE_WALL);
                b.getRelative(face).getRelative(BlockFace.UP).setType(Material.COBBLESTONE_WALL);
            });
            plugin.getServer().getScheduler().cancelTask(taskID);
            taskID = 0;
        }
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
