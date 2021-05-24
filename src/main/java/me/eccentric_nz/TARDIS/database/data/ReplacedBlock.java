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
package me.eccentric_nz.tardis.database.data;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

/**
 * @author eccentric_nz
 */
public class ReplacedBlock {

	private final int id;
	private final int tardisId;
	private final Location location;
	private final String strLocation;
	private final BlockData blockData;
	private final int policeBox;

	public ReplacedBlock(int id, int tardisId, Location location, String strLocation, BlockData blockData, int policeBox) {
		this.id = id;
		this.tardisId = tardisId;
		this.location = location;
		this.strLocation = strLocation;
		this.blockData = blockData;
		this.policeBox = policeBox;
	}

	public int getId() {
		return id;
	}

	public int getTardisId() {
		return tardisId;
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

	public int getPoliceBox() {
		return policeBox;
	}
}
