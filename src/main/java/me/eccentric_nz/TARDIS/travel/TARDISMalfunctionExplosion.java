/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFlightControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRepeaters;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.particles.TARDISFirework;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 * <p>
 * Atmospheric excitation is an unnatural disturbance in the atmosphere which causes the weather to change. The Tenth
 * Doctor's sonic screwdriver, the TARDIS, and moving a planet can all cause atmospheric excitation.
 * <p>
 * The Tenth Doctor used a device above the inside of the door of the TARDIS to excite the atmosphere, causing snow, in
 * an attempt to cheer up Donna Noble.
 */
public class TARDISMalfunctionExplosion implements Runnable {

    private final TARDIS plugin;
    private final int id;
    private final long end;
    private final boolean console;
    private boolean started = false;
    private List<Location> locations;
    private int task;

    public TARDISMalfunctionExplosion(TARDIS plugin, int id, long end, boolean console) {
        this.plugin = plugin;
        this.id = id;
        this.end = end;
        this.console = console;
    }

    @Override
    public void run() {
        if (!started) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                if (console) {
                    ResultSetFlightControls rsfc = new ResultSetFlightControls(plugin, id);
                    if (rsfc.resultSet()) {
                        locations = rsfc.getLocations();
                    }
                } else {
                    ResultSetRepeaters rsr = new ResultSetRepeaters(plugin, tardis.getTardisId(), 0);
                    if (rsr.resultSet()) {
                        locations = rsr.getLocations();
                    }
                }
                started = true;
            }
        }
        long time = System.currentTimeMillis();
        if (time > end) {
            plugin.getServer().getScheduler().cancelTask(task);
            // toggle the malfunction sensor
            TARDISMalfunction.handleSensor(id);
        } else {
            Location l = locations.get(TARDISConstants.RANDOM.nextInt(4));
            TARDISFirework.randomize().displayEffects(plugin, l.add(0.5, 0.5, 0.5));
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
