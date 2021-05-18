/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.block.Block;

import java.util.List;

public class TARDISMushroomRunnable implements Runnable {

	private final TARDIS plugin;
	private final List<MushroomBlock> mushrooms;
	private int task;
	private int i = 0;

	public TARDISMushroomRunnable(TARDIS plugin, List<MushroomBlock> mushrooms) {
		this.plugin = plugin;
		this.mushrooms = mushrooms;
	}

	@Override
	public void run() {
		Block block = mushrooms.get(i).getBlock();
		block.setBlockData(mushrooms.get(i).getBlockData());
		i++;
		if (i == mushrooms.size()) {
			plugin.getServer().getScheduler().cancelTask(task);
			task = 0;
		}
	}

	public void setTask(int task) {
		this.task = task;
	}
}
