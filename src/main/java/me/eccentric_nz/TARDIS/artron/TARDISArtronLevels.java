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
package me.eccentric_nz.tardis.artron;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Artron energy is used as a source of energy in the tardis. Visually, artron energy resembles a sort of blue
 * electricity. Within the tardis' generator room is an Artron Energy Capacitor. Artron energy can be absorbed by
 * travelling through time, such as by travellers in a tardis.
 *
 * @author eccentric_nz
 */
public class TardisArtronLevels {

    private final TardisPlugin plugin;

    public TardisArtronLevels(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Starts a repeating task to recharge the tardis. The task is started each time the player exits the tardis after
     * travelling. If the tardis moves away from the recharge location the task is cancelled.
     *
     * @param id the unique tardis database key
     */
    public void recharge(int id) {
        HashMap<String, Object> set = new HashMap<>();
        set.put("recharging", 1);
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("tardis", set, where);
        TardisArtronRunnable runnable = new TardisArtronRunnable(plugin, id);
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 480L, 480L);
        runnable.setTask(taskID);
    }

    /**
     * Checks whether the tardis has sufficient Artron Energy levels. If the energy level will drop below 100, then the
     * player is warned.
     *
     * @param id       the unique tardis database key
     * @param required the amount of Artron energy needed
     * @param p        the player to message
     * @return a boolean - true if the tardis has sufficient energy
     */
    public boolean checkLevel(int id, int required, Player p) {
        ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
        if (!rs.fromID(id)) {
            return false;
        }
        int level = rs.getArtronLevel();
        if (level - required <= 100) {
            TardisMessage.send(p, "ENERGY_LOW");
            return false;
        }
        return (level > required);
    }
}
