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
package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISJunkPlayerPersister {

	private final TARDISPlugin plugin;

	public TARDISJunkPlayerPersister(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public void load() {
		HashMap<String, Object> where = new HashMap<>();
		where.put("chameleon_preset", "JUNK_MODE");
		ResultSetTardis rs = new ResultSetTardis(plugin, where, "", true, 2);
		if (rs.resultSet()) {
			rs.getData().forEach((t) -> plugin.getTrackerKeeper().getJunkPlayers().put(t.getUuid(), t.getTardisId()));
		}
	}
}
