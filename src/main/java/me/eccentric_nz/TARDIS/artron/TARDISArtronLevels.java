/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.artron;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.entity.Player;

/**
 * Artron energy is used as a source of energy in the TARDIS. Visually, artron
 * energy resembles a sort of blue electricity. Within the TARDIS' generator
 * room is an Artron Energy Capacitor. Artron energy can be absorbed by
 * travelling through time, such as by travellers in a TARDIS.
 *
 * @author eccentric_nz
 */
public class TARDISArtronLevels {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISArtronLevels(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Starts a repeating task to recharge the TARDIS. The task is started each
     * time the player exits the TARDIS after travelling. If the TARDIS moves
     * away from the recharge location the task is cancelled.
     */
    public void recharge(int id, Player p) {
        plugin.trackRecharge.add(id);
        TARDISArtronRunnable runnable = new TARDISArtronRunnable(plugin, id, p);
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 480L, 480L);
        runnable.setTask(taskID);
    }

    /**
     * Checks whether the TARDIS has sufficient Artron Energy levels. If the
     * energy level will drop below 100, then the player is warned.
     */
    public boolean checkLevel(int id, int required, Player p) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            return false;
        }
        int level = rs.getArtron_level();
        if (level - required <= 100) {
            p.sendMessage(plugin.pluginName + "The Artron Levels are critically low. You should recharge soon!");
        }
        return (level > required);
    }
}