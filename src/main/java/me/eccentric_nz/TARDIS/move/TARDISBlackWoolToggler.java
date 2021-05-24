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
package me.eccentric_nz.tardis.move;

import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoorBlocks;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISBlackWoolToggler {

	private final TARDISPlugin plugin;

	public TARDISBlackWoolToggler(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public void toggleBlocks(int id, Player player) {
		ResultSetDoorBlocks rsd = new ResultSetDoorBlocks(plugin, id);
		if (rsd.resultSet()) {
			Block b = rsd.getInnerBlock().getRelative(BlockFace.NORTH);
			BlockData mat;
			if (b.getType().isAir()) {
				mat = TARDISConstants.BLACK;
			} else {
				mat = TARDISConstants.AIR;
			}
			b.setBlockData(mat);
			b.getRelative(BlockFace.UP).setBlockData(mat);
			Block door = b.getRelative(BlockFace.SOUTH);
			if (Tag.DOORS.isTagged(door.getType()) && TARDISStaticUtils.isDoorOpen(door)) {
				// toggle doors shut
				new TARDISDoorToggler(plugin, b.getRelative(BlockFace.SOUTH), player, false, true, id).toggleDoors();
			}
		}
	}

	public boolean isOpen(int id) {
		ResultSetDoorBlocks rsd = new ResultSetDoorBlocks(plugin, id);
		if (rsd.resultSet()) {
			Block b = rsd.getInnerBlock().getRelative(BlockFace.NORTH);
			return b.getType().isAir();
		} else {
			return false;
		}
	}
}
