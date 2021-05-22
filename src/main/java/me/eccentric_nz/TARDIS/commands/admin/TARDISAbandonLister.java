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
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
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
        ResultSetTardis rst = new ResultSetTardis(TARDIS.plugin, new HashMap<>(), "", true, 1);
        sender.sendMessage(ChatColor.GRAY + plugin.getLanguage().getString("ABANDONED_LIST"));
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
                    String w = (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(rsc.getWorld()) : TARDISAliasResolver.getWorldAlias(rsc.getWorld());
                    String l = w + " " + rsc.getX() + ", " + rsc.getY() + ", " + rsc.getZ();
                    if (click) {
                        TextComponent tcg = new TextComponent(i + ". Abandoned by: " + owner + ", " + l);
                        TextComponent tce = new TextComponent(" < Enter > ");
                        tce.setColor(ChatColor.GREEN);
                        tce.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to enter this TARDIS")));
                        tce.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardisadmin enter " + t.getTardis_id()));
                        tcg.addExtra(tce);
                        sender.spigot().sendMessage(tcg);
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
