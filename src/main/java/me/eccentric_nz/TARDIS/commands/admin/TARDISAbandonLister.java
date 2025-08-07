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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISAbandonLister {

    private final TARDIS plugin;

    public TARDISAbandonLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void list(CommandSender sender) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("abandoned", 1);
        ResultSetTardis rst = new ResultSetTardis(TARDIS.plugin, where, "", true);
        plugin.getMessenger().messageWithColour(sender, plugin.getLanguage().getString("ABANDONED_LIST"), "#AAAAAA");
        if (rst.resultSet()) {
            boolean click = (sender instanceof Player);
            if (click) {
                sender.sendMessage(plugin.getLanguage().getString("ABANDONED_CLICK"));
            }
            int i = 1;
            for (Tardis tardis : rst.getData()) {
                String owner = (tardis.getOwner().isEmpty()) ? "TARDIS Admin" : tardis.getOwner();
                // get current location
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, tardis.getTardisId());
                if (rsc.resultSet()) {
                    Current current = rsc.getCurrent();
                    String w = (!plugin.getPlanetsConfig().getBoolean("planets." + current.location().getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(current.location().getWorld()) : TARDISAliasResolver.getWorldAlias(current.location().getWorld());
                    String l = w + " " + current.location().getBlockX() + ", " + current.location().getBlockY() + ", " + current.location().getBlockZ();
                    if (click) {
                        plugin.getMessenger().sendAbandoned(sender, i, owner, l, tardis.getTardisId());
                    } else {
                        sender.sendMessage(i + ". Abandoned by: " + owner + ", location: " + l);
                    }
                    i++;
                }
            }
        } else {
            sender.sendMessage(plugin.getLanguage().getString("ABANDONED_NONE"));
        }
    }
}
