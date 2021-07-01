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
package me.eccentric_nz.tardis.commands.admin;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.destroyers.TardisExterminator;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TardisPurgeCommand {

    private final TardisPlugin plugin;

    TardisPurgeCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean clearAll(CommandSender sender, String[] args) {
        // Look up this player's UUID
        UUID uuid = null;
        if (args[1].toLowerCase(Locale.ENGLISH).equals("junk")) {
            uuid = UUID.fromString("00000000-aaaa-bbbb-cccc-000000000000");
        } else {
            OfflinePlayer offlinePlayer = TardisStaticUtils.getOfflinePlayer(args[1]);
            if (offlinePlayer != null) {
                uuid = offlinePlayer.getUniqueId();
            }
        }
        if (uuid != null) {
            // get the player's Tardis id
            ResultSetTardisId rs = new ResultSetTardisId(plugin);
            if (!rs.fromUuid(uuid.toString())) {
                TardisMessage.send(sender, "PLAYER_NOT_FOUND_DB", args[1]);
                return true;
            }
            int id = rs.getTardisId();
            TardisExterminator purger = new TardisExterminator(plugin);
            purger.cleanHashMaps(id);
            purger.cleanDatabase(id);
            TardisMessage.send(sender, "PURGE_PLAYER", args[1]);
        } else {
            TardisMessage.send(sender, "UUID_NOT_FOUND", args[1]);
        }
        return true;
    }
}
