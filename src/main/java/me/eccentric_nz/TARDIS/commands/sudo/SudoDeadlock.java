/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.sudo;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;

class SudoDeadlock {

    private final TARDIS plugin;

    SudoDeadlock(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean toggleDeadlock(UUID uuid, CommandSender sender) {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        // does the player have a TARDIS
        if (rs.fromUUID(uuid.toString())) {
            int id = rs.getTardis_id();
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            ResultSetDoors rsd = new ResultSetDoors(plugin, wheret, false);
            if (rsd.resultSet()) {
                int lock = rsd.isLocked() ? 0 : 1;
                String lockedUnlocked = rsd.isLocked() ? plugin.getLanguage().getString("DOOR_UNLOCK") : plugin.getLanguage().getString("DOOR_DEADLOCK");
                HashMap<String, Object> setd = new HashMap<>();
                setd.put("locked", lock);
                HashMap<String, Object> whered = new HashMap<>();
                whered.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("doors", setd, whered);
                TARDISMessage.send(sender, "DOOR_LOCK", lockedUnlocked);
            }
        } else {
            TARDISMessage.send(sender, "NO_TARDIS");
        }
        return true;
    }
}
