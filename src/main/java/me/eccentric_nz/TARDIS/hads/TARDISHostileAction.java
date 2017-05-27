/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.hads;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISHostileAction {

    private final TARDIS plugin;

    public TARDISHostileAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void processAction(final int id, Player hostile) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            UUID uuid = tardis.getUuid();
            boolean poweredOn = tardis.isPowered_on();
            PRESET preset = tardis.getPreset();
            HashMap<String, Object> wherep = new HashMap<>();
            wherep.put("uuid", uuid.toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
            if (rsp.resultSet()) {
                if (rsp.isHadsOn() && poweredOn) {
                    switch (rsp.getHadsType()) {
                        case DISPLACEMENT:
                            new TARDISHostileDisplacement(plugin).moveTARDIS(id, uuid, hostile, preset);
                            break;
                        case DISPERSAL:
                            new TARDISHostileDispersal(plugin).disperseTARDIS(id, uuid, hostile, preset);
                            break;
                        default:
                            break;
                    }
                } else {
                    plugin.getTrackerKeeper().getDamage().remove(id);
                    TARDISMessage.send(hostile, "TARDIS_BREAK");
                }
            } else {
                plugin.getTrackerKeeper().getDamage().remove(id);
                TARDISMessage.send(hostile, "TARDIS_BREAK");
            }
        }
    }
}
