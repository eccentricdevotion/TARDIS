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
package me.eccentric_nz.tardis.rooms;

import java.util.HashMap;

/**
 * Store required block data for growing rooms. Used to debit the condenser table when the room is grown.
 *
 * @author eccentric_nz
 */
public class TARDISCondenserData {

	private HashMap<String, Integer> blockIDCount = new HashMap<>();
	private int tardisId;

	public TARDISCondenserData() {
	}

	public HashMap<String, Integer> getBlockIDCount() {
		return blockIDCount;
	}

	public void setBlockIDCount(HashMap<String, Integer> blockIDCount) {
		this.blockIDCount = blockIDCount;
	}

	public int getTardisId() {
		return tardisId;
	}

	public void setTardisId(int tardisId) {
		this.tardisId = tardisId;
	}
}
