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
package me.eccentric_nz.TARDIS.rooms;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.ROOM;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRoomBuilder {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISRoomBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean buildRoom(ROOM r, String player) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", player);
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
        if (rsp.resultSet()) {
            if (rsp.getArtron_level() < plugin.getConfig().getLong("rooms." + r.toString())) {
                switch (r) {
                    case POOL:

                        break;
                    case LIBRARY:

                        break;
                    case KITCHEN:

                        break;
                    case VAULT:

                        break;
                    case ARBORETUM:

                        break;
                    case BEDROOM:

                        break;
                    default:
                        // PASSAGE
                        break;
                }
            }
        }
        return true;
    }
}
