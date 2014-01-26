/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * The Doctor's favorite food - jelly babies have been considered a delicacy by
 * the Doctor ever since his second incarnation.
 *
 * @author eccentric_nz
 */
public class TARDISCreeperChecker {

    private final TARDIS plugin;

    public TARDISCreeperChecker(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * A repeating task that checks if the charged creeper in the TARDIS Artron
     * Energy Capacitor is still there.
     */
    public void startCreeperCheck() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                checkCreepers();
            }
        }, 600L, 12000L);
    }

    /**
     * Checks the creeper is there and spawns in a new one if not.
     */
    private void checkCreepers() {
        ResultSetTardis rs = new ResultSetTardis(plugin, null, "", true);
        if (rs.resultSet()) {
            ArrayList<HashMap<String, String>> data = rs.getData();
            for (HashMap<String, String> map : data) {
                // only if there is a saved creeper location
                if (!map.get("creeper").isEmpty()) {
                    // only if the TARDIS has been initialised
                    if (map.get("tardis_init").equals("1")) {
                        String[] creeperData = map.get("creeper").split(":");
                        World w = plugin.getServer().getWorld(creeperData[0]);
                        if (w != null) {
                            float cx = plugin.utils.parseFloat(creeperData[1]);
                            float cy = plugin.utils.parseFloat(creeperData[2]) + 1;
                            float cz = plugin.utils.parseFloat(creeperData[3]);
                            Location l = new Location(w, cx, cy, cz);
                            plugin.myspawn = true;
                            Entity e = w.spawnEntity(l, EntityType.CREEPER);
                            // if there is a creeper there already get rid of it!
                            for (Entity k : e.getNearbyEntities(1d, 1d, 1d)) {
                                if (k.getType().equals(EntityType.CREEPER)) {
                                    e.remove();
                                    break;
                                }
                            }
                            Creeper c = (Creeper) e;
                            c.setPowered(true);
                        }
                    }
                }
            }
        }
    }
}
