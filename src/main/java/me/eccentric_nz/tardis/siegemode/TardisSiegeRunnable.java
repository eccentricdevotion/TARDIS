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
package me.eccentric_nz.tardis.siegemode;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TardisSiegeRunnable implements Runnable {

    private final TardisPlugin plugin;
    private final int deplete;

    public TardisSiegeRunnable(TardisPlugin plugin) {
        this.plugin = plugin;
        deplete = -this.plugin.getArtronConfig().getInt("siege_deplete");
    }

    @Override
    public void run() {
        plugin.getTrackerKeeper().getInSiegeMode().forEach((id) -> {
            // get current Artron level
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                int level = tardis.getArtronLevel();
                if (level > deplete) {
                    // remove some energy
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", id);
                    plugin.getQueryFactory().alterEnergyLevel("tardis", deplete, whered, null);
                } else if (plugin.getConfig().getBoolean("siege.creeper")) {
                    Location l = TardisStaticLocationGetters.getLocationFromDB(tardis.getCreeper());
                    // spawn an entity so we can check for the creeper
                    assert l != null;
                    Entity ent = Objects.requireNonNull(l.getWorld()).spawnEntity(l, EntityType.EGG);
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
                        HashMap<String, Object> wherec = new HashMap<>();
                        wherec.put("tardis_id", id);
                        plugin.getQueryFactory().alterEnergyLevel("tardis", plugin.getArtronConfig().getInt("siege_creeper"), wherec, null);
                    }
                }
            }
            // give players inside TARDIS a health boost
            if (plugin.getConfig().getBoolean("siege.healing")) {
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, true);
                if (rst.resultSet()) {
                    rst.getData().forEach((uuid) -> {
                        Player p = plugin.getServer().getPlayer(uuid);
                        if (p != null && p.isOnline() && p.getHealth() < 19.5) {
                            p.setHealth(p.getHealth() + 0.5);
                        }
                    });
                }
            }
        });
    }
}
