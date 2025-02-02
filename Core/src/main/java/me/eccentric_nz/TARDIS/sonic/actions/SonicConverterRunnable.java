/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.sonic.actions;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class SonicConverterRunnable implements Runnable {

    private final Block block;
    private final Material from;
    private final Material to;
    private int taskId;
    private int count = 0;
    public SonicConverterRunnable(Block block, Material from, Material to) {
        this.block = block;
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        Material which = (block.getType() == from) ? to : from;
        block.setType(which);
        count++;
        if (count > 4) {
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(to, 1));
            Bukkit.getServer().getScheduler().cancelTask(taskId);
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

}
