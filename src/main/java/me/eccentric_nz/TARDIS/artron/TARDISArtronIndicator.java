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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISArtronIndicator {

    private final TARDIS plugin;
    private final int fc;

    public TARDISArtronIndicator(TARDIS plugin) {
        this.plugin = plugin;
        fc = plugin.getArtronConfig().getInt("full_charge");
    }

    public ArtronIndicatorData getLevels(Player p, int id, int used) {
        ArtronIndicatorData data = new ArtronIndicatorData();
        // get Artron level
        ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
        if (rs.fromID(id)) {
            int current_level = rs.getArtronLevel();
            data.setRemaining(current_level);
            data.setPercent(Math.round((current_level * 100F) / fc));
            if (used == 0) {
                data.setMax(fc);
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
                if (rsp.resultSet()) {
                    data.setTimelord(rsp.getArtronLevel());
                }
            }
            if (used > 0) {
                data.setUsed(used);
            } else if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                data.setCost(plugin.getTrackerKeeper().getHasDestination().get(id).cost());
            }
        }
        return data;
    }
}
