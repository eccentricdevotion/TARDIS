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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetRestoreFollowers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class FollowerReset {

    private final TARDIS plugin;

    public FollowerReset(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void tame() {
        ResultSetRestoreFollowers rsf = new ResultSetRestoreFollowers(plugin);
        if (rsf.resultSet()) {
            for (Follower follower : rsf.getFollowers()) {
                Entity husk = Bukkit.getServer().getEntity(follower.getUuid());
                if (husk != null) {
                    new ResetMonster(plugin, husk).reset();
                }
            }
        }
    }
}
