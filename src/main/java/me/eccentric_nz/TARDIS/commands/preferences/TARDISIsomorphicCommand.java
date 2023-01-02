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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISIsomorphicCommand {

    private final TARDIS plugin;

    public TARDISIsomorphicCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean toggleIsomorphicControls(Player player) {
        return toggleIsomorphicControls(player.getUniqueId(), player);
    }

    public boolean toggleIsomorphicControls(UUID uuid, CommandSender sender) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        // does the player have a TARDIS
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int iso = (tardis.isIso_on()) ? 0 : 1;
            String onoff = (tardis.isIso_on()) ? "ISO_OFF" : "ISO_ON";
            int id = tardis.getTardis_id();
            HashMap<String, Object> seti = new HashMap<>();
            seti.put("iso_on", iso);
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", seti, wheret);
            TARDISMessage.send(sender, onoff);
        } else {
            TARDISMessage.send(sender, "NO_TARDIS");
        }
        return true;
    }
}
