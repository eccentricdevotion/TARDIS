/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAbandonLister {

    private final TARDIS plugin;

    public TARDISAbandonLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void list(CommandSender sender) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("abandoned", 1);
        ResultSetTardis rst = new ResultSetTardis(TARDIS.plugin, where, "", true);
        sender.sendMessage(ChatColor.GRAY + plugin.getLanguage().getString("ABANDONED_LIST"));
        if (rst.resultSet()) {
            boolean click = (plugin.getPM().isPluginEnabled("ProtocolLib") && sender instanceof Player);
            if (click) {
                sender.sendMessage(plugin.getLanguage().getString("ABANDONED_CLICK"));
            }
            int i = 1;
            for (Tardis t : rst.getData()) {
                // get current location
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", t.getTardis_id());
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                if (rsc.resultSet()) {
                    String w = (plugin.isMVOnServer()) ? plugin.getMVHelper().getAlias(rsc.getWorld()) : rsc.getWorld().getName();
                    String l = w + " " + rsc.getX() + ", " + rsc.getY() + ", " + rsc.getZ();
                    if (click) {
                        String json = "{\"text\":\"" + i + ". Abandoned by: " + t.getOwner() + ", location: " + l + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"tardisadmin enter " + t.getTardis_id() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click me\"}]}}}";
                        TARDISUpdateChatGUI.sendJSON(json, (Player) sender);
                    } else {
                        sender.sendMessage(i + ". Abandoned by: " + t.getOwner() + ", location: " + l);
                    }
                    i++;
                }
            }
        } else {
            sender.sendMessage(plugin.getLanguage().getString("ABANDONED_NONE"));
        }
    }
}
