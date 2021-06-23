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
package me.eccentric_nz.tardis.commands.preferences;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisIsomorphicCommand {

    private final TardisPlugin plugin;

    public TardisIsomorphicCommand(TardisPlugin plugin) {
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
            int iso = (tardis.isIsoOn()) ? 0 : 1;
            String onoff = (tardis.isIsoOn()) ? "ISO_OFF" : "ISO_ON";
            int id = tardis.getTardisId();
            HashMap<String, Object> seti = new HashMap<>();
            seti.put("iso_on", iso);
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", seti, wheret);
            TardisMessage.send(sender, onoff);
        } else {
            TardisMessage.send(sender, "NO_TARDIS");
        }
        return true;
    }
}
