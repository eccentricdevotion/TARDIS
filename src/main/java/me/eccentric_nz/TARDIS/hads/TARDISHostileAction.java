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
package me.eccentric_nz.tardis.hads;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISHostileAction {

    private final TARDISPlugin plugin;

    public TARDISHostileAction(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    public void processAction(int id, Player hostile) {
        if (plugin.getTrackerKeeper().getDamage().get(id) > 99) {
            return;
        }
        plugin.getTrackerKeeper().getDamage().put(id, 100);
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            TARDIS tardis = rs.getTardis();
            UUID uuid = tardis.getUuid();
            boolean powered = tardis.isPowered();
            PRESET preset = tardis.getPreset();
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            if (rsp.resultSet()) {
                if (rsp.isHadsOn() && powered) {
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
