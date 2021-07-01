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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.destroyers.TARDISExterminator;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISPurgeCommand {

    private final TARDIS plugin;

    TARDISPurgeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean clearAll(CommandSender sender, String[] args) {
        // Look up this player's UUID
        UUID uuid = null;
        if (args[1].toLowerCase(Locale.ENGLISH).equals("junk")) {
            uuid = UUID.fromString("00000000-aaaa-bbbb-cccc-000000000000");
        } else {
            OfflinePlayer offlinePlayer = TARDISStaticUtils.getOfflinePlayer(args[1]);
            if (offlinePlayer != null) {
                uuid = offlinePlayer.getUniqueId();
            }
        }
        if (uuid != null) {
            // get the player's TARDIS id
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(uuid.toString())) {
                TARDISMessage.send(sender, "PLAYER_NOT_FOUND_DB", args[1]);
                return true;
            }
            int id = rs.getTardis_id();
            TARDISExterminator purger = new TARDISExterminator(plugin);
            purger.cleanHashMaps(id);
            purger.cleanDatabase(id);
            TARDISMessage.send(sender, "PURGE_PLAYER", args[1]);
        } else {
            TARDISMessage.send(sender, "UUID_NOT_FOUND", args[1]);
        }
        return true;
    }
}
