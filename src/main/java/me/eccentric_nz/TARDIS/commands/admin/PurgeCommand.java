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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.destroyers.Exterminator;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
public class PurgeCommand {

    private final TARDIS plugin;

    public PurgeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean clearAll(CommandSender sender, String who, String uuid) {
        if (uuid != null) {
            // get the player's TARDIS id
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(uuid.toString())) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_FOUND_DB", who);
                return true;
            }
            int id = rs.getTardisId();
            Exterminator purger = new Exterminator(plugin);
            purger.cleanHashMaps(id);
            purger.cleanDatabase(id);
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PURGE_PLAYER", who);
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "UUID_NOT_FOUND", who);
        }
        return true;
    }
}
