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
package me.eccentric_nz.TARDIS.commands.sudo;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class SudoAssemble {

    private final TARDIS plugin;

    SudoAssemble(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean restore(CommandSender sender, UUID uuid, String player) {
        // turn off dispersal for this player
        plugin.getTrackerKeeper().getDispersed().remove(uuid);
        // get players TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            plugin.getTrackerKeeper().getDispersedTARDII().remove(tardis.getTardisId());
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ASSEMBLE_PLAYER", player);
            Player dispersed = plugin.getServer().getPlayer(uuid);
            if (dispersed != null) {
                plugin.getMessenger().sendColouredCommand(dispersed, "ASSEMBLE_REBUILD", "/tardis rebuild", plugin);
            }
        }
        return true;
    }
}
