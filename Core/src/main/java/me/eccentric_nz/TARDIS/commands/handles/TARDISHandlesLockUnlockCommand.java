/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISHandlesLockUnlockCommand {

    private final TARDIS plugin;

    TARDISHandlesLockUnlockCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean toggleLock(Player player, int id, boolean lock) {
        // get the TARDIS current location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", id);
            ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
            if (rsd.resultSet()) {
                // toggle door lock
                int locked = (!lock) ? 0 : 1;
                HashMap<String, Object> setl = new HashMap<>();
                setl.put("locked", locked);
                HashMap<String, Object> wherel = new HashMap<>();
                wherel.put("tardis_id", id);
                // always lock / unlock both doors
                plugin.getQueryFactory().doUpdate("doors", setl, wherel);
                String message = (!lock) ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                plugin.getMessenger().handlesSend(player, "DOOR_LOCK", message);
                TARDISSounds.playTARDISSound(rsc.getCurrent().location(), "tardis_lock");
            }
        }
        return true;
    }
}
