/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

/**
 * The Doctor's favorite food - jelly babies have been considered a delicacy by the Doctor ever since his second
 * incarnation.
 *
 * @author eccentric_nz
 */
public class ArtronCreeperChecker {

    private final TARDIS plugin;
    private final int id;

    public ArtronCreeperChecker(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    /**
     * Checks the creeper is there and spawns in a new one if not.
     */
    public void checkCreeper() {
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
        if (rs.resultSet()) {
        Tardis tardis = rs.getTardis();
            // only if there is a saved creeper location
            if (!tardis.getCreeper().isEmpty()) {
                // only if the TARDIS has been initialised
                if (tardis.isTardisInit()) {
                    World w = TARDISStaticLocationGetters.getWorldFromSplitString(tardis.getCreeper());
                    if (w != null) {
                        Location l = TARDISStaticLocationGetters.getLocationFromDB(tardis.getCreeper());
                        plugin.setTardisSpawn(true);
                        Entity e = w.spawnEntity(l.add(0.0d, 1.0d, 0.0d), EntityType.CREEPER);
                        // if there is a creeper there already get rid of it!
                        for (Entity k : e.getNearbyEntities(1d, 1d, 1d)) {
                            if (k.getType().equals(EntityType.CREEPER)) {
                                e.remove();
                                break;
                            }
                        }
                        Creeper c = (Creeper) e;
                        c.setPowered(true);
                        c.setRemoveWhenFarAway(false);
                    }
                }
            }
        }
    }
}
