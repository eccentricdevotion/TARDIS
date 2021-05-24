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
package me.eccentric_nz.tardis.travel;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetRepeaters;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.utility.TARDISFirework;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 * <p>
 * Atmospheric excitation is an unnatural disturbance in the atmosphere which causes the weather to change. The Tenth
 * Doctor's sonic screwdriver, the tardis, and moving a planet can all cause atmospheric excitation.
 * <p>
 * The Tenth Doctor used a device above the inside of the door of the tardis to excite the atmosphere, causing snow, in
 * an attempt to cheer up Donna Noble.
 */
public class TARDISMalfunctionExplosion implements Runnable {

	private final TARDISPlugin plugin;
	private final int id;
	private final long end;
	private boolean started = false;
	private List<Location> locations;
	private int task;

	public TARDISMalfunctionExplosion(TARDISPlugin plugin, int id, long end) {
		this.plugin = plugin;
		this.id = id;
		this.end = end;
	}

	@Override
	public void run() {
		if (!started) {
			HashMap<String, Object> where = new HashMap<>();
			where.put("tardis_id", id);
			ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
			if (rs.resultSet()) {
				TARDIS tardis = rs.getTardis();
				ResultSetRepeaters rsr = new ResultSetRepeaters(plugin, tardis.getTardisId(), 0);
				if (rsr.resultSet()) {
					locations = rsr.getLocations();
				}
				started = true;
			}
		}
		long time = System.currentTimeMillis();
		if (time > end) {
			plugin.getServer().getScheduler().cancelTask(task);
		} else {
			Location l = locations.get(TARDISConstants.RANDOM.nextInt(4));
			TARDISFirework.randomize().displayEffects(plugin, l.add(0.5, 0.5, 0.5));
		}
	}

	public void setTask(int task) {
		this.task = task;
	}
}
