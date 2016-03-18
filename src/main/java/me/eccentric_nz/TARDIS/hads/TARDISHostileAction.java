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
import me.eccentric_nz.TARDIS.enumeration.PRESET;
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
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            UUID uuid = rs.getUuid();
            boolean cham = rs.isChamele_on();
            PRESET preset = rs.getPreset();
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("uuid", uuid.toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
            if (rsp.resultSet()) {
                if (rsp.isHadsOn()) {
                    switch (rsp.getHadsType()) {
                        case DISPLACEMENT:
                            new TARDISHostileDisplacement(plugin).moveTARDIS(id, cham, uuid, hostile, preset);
                            break;
                        case DISPERSAL:
                            new TARDISHostileDispersal(plugin).disperseTARDIS(id, cham, uuid, hostile, preset);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}
