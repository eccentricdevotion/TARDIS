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
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronStorage;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.particles.Sphere;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class EyeOfHarmonyParticles {

    private final TARDIS plugin;

    public EyeOfHarmonyParticles(TARDIS plugin) {
        this.plugin = plugin;
    }

    public static void stop(TARDIS plugin, int id) {
        int task = -1;
        // stop the current particles
        ResultSetArtronStorage rs = new ResultSetArtronStorage(plugin);
        if (rs.fromID(id)) {
            task = rs.getTask();
            if (task != -1) {
                plugin.getServer().getScheduler().cancelTask(task);
            }
        }
    }

    public int start(int id, int capacitors, UUID uuid) {
        int task = -1;
        // only restart if there are capacitors in storage
        if (capacitors > 0) {
            // get the new settings
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            wherec.put("type", 53);
            ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
            if (rsc.resultSet()) {
                Location s = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                Capacitor capacitor = Capacitor.values()[capacitors - 1];
                Sphere sphere = new Sphere(plugin, uuid, s, capacitor);
                task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, sphere, 0, 10);
                sphere.setTaskID(task);
            }
        }
        return task;
    }
}
