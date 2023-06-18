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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISAbandonLister {

    private final TARDIS plugin;

    public TARDISAbandonLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void list(CommandSender sender) {
        ResultSetTardis rst = new ResultSetTardis(TARDIS.plugin, new HashMap<>(), "", true, 1);
        plugin.getMessenger().messageWithColour(sender, plugin.getLanguage().getString("ABANDONED_LIST"), "#AAAAAA");
        if (rst.resultSet()) {
            boolean click = (sender instanceof Player);
            if (click) {
                sender.sendMessage(plugin.getLanguage().getString("ABANDONED_CLICK"));
            }
            int i = 1;
            for (Tardis t : rst.getData()) {
                String owner = (t.getOwner().equals("")) ? "TARDIS Admin" : t.getOwner();
                // get current location
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", t.getTardis_id());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                if (rsc.resultSet()) {
                    String w = (!plugin.getPlanetsConfig().getBoolean("planets." + rsc.getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(rsc.getWorld()) : TARDISAliasResolver.getWorldAlias(rsc.getWorld());
                    String l = w + " " + rsc.getX() + ", " + rsc.getY() + ", " + rsc.getZ();
                    if (click) {
                        plugin.getMessenger().sendAbandoned(sender, i, owner, l, t.getTardis_id());
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
