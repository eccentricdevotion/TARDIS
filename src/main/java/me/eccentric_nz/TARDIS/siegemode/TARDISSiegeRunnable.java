/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.siegemode;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSiegeRunnable implements Runnable {

    private final TARDIS plugin;
    private final int deplete;
    private final QueryFactory qf;

    public TARDISSiegeRunnable(TARDIS plugin) {
        this.plugin = plugin;
        this.deplete = 0 - this.plugin.getArtronConfig().getInt("siege_deplete");
        this.qf = new QueryFactory(this.plugin);
    }

    @Override
    public void run() {
        for (int id : plugin.getTrackerKeeper().getInSiegeMode()) {
            // get current Artron level
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                int level = tardis.getArtron_level();
                if (level > deplete) {
                    // remove some energy
                    HashMap<String, Object> whered = new HashMap<String, Object>();
                    whered.put("tardis_id", id);
                    qf.alterEnergyLevel("tardis", deplete, whered, null);
                } else if (plugin.getConfig().getBoolean("siege.creeper")) {
                    Location l = TARDISLocationGetters.getLocationFromDB(tardis.getCreeper(), 0.0f, 0.0f);
                    // spawn an entity so we can check for the creeper
                    Entity ent = l.getWorld().spawnEntity(l, EntityType.EGG);
                    List<Entity> creeps = ent.getNearbyEntities(1d, 1d, 1d);
                    ent.remove();
                    boolean boost = false;
                    for (Entity e : creeps) {
                        if (e instanceof Creeper) {
                            e.remove();
                            boost = true;
                        }
                    }
                    if (boost) {
                        // give some energy
                        HashMap<String, Object> wherec = new HashMap<String, Object>();
                        wherec.put("tardis_id", id);
                        qf.alterEnergyLevel("tardis", plugin.getArtronConfig().getInt("siege_creeper"), wherec, null);
                    }
                }
            }
            // give players inside TARDIS a health boost
            if (plugin.getConfig().getBoolean("siege.healing")) {
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, true);
                if (rst.resultSet()) {
                    for (UUID uuid : rst.getData()) {
                        Player p = plugin.getServer().getPlayer(uuid);
                        if (p != null && p.isOnline() && p.getHealth() < 19.5) {
                            p.setHealth(p.getHealth() + 0.5);
                        }
                    }
                }
            }
        }
    }
}
