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
package me.eccentric_nz.tardis.database.data;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

/**
 * @author eccentric_nz
 */
public class ReplacedBlock {

	private final int id;
	private final int tardis_id;
	private final Location location;
	private final String strLocation;
	private final BlockData blockData;
	private final int police_box;

	public ReplacedBlock(int id, int tardis_id, Location location, String strLocation, BlockData blockData, int police_box) {
		this.id = id;
		this.tardis_id = tardis_id;
		this.location = location;
		this.strLocation = strLocation;
		this.blockData = blockData;
		this.police_box = police_box;
	}

	public int getId() {
		return id;
	}

	public int getTardis_id() {
		return tardis_id;
	}

	public Location getLocation() {
		return location;
	}

	public String getStrLocation() {
		return strLocation;
	}

	public BlockData getBlockData() {
		return blockData;
	}

	public int getPolice_box() {
		return police_box;
	}
}
