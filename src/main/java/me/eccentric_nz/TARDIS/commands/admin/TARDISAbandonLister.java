/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.WorldManager;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TARDISAbandonLister {

	private final TARDISPlugin plugin;

	public TARDISAbandonLister(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public void list(CommandSender sender) {
		ResultSetTardis rst = new ResultSetTardis(TARDISPlugin.plugin, new HashMap<>(), "", true, 1);
		sender.sendMessage(ChatColor.GRAY + plugin.getLanguage().getString("ABANDONED_LIST"));
		if (rst.resultSet()) {
			boolean click = (sender instanceof Player);
			if (click) {
				sender.sendMessage(Objects.requireNonNull(plugin.getLanguage().getString("ABANDONED_CLICK")));
			}
			int i = 1;
			for (TARDIS t : rst.getData()) {
				String owner = (t.getOwner().equals("")) ? "tardis Admin" : t.getOwner();
				// get current location
				HashMap<String, Object> wherec = new HashMap<>();
				wherec.put("tardis_id", t.getTardisId());
				ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
				if (rsc.resultSet()) {
					String w = (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(rsc.getWorld()) : TARDISAliasResolver.getWorldAlias(rsc.getWorld());
					String l = w + " " + rsc.getX() + ", " + rsc.getY() + ", " + rsc.getZ();
					if (click) {
						String json = "[{\"text\":\"" + i + ". Abandoned by: " + owner + ", " + l + "\"},{\"text\":\" < Enter > \",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardisadmin enter " + t.getTardisId() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click to enter this tardis\"}]}}},{\"text\":\" < Delete >\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardisadmin delete " + t.getTardisId() + " abandoned\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click to delete this tardis\"}]}}}]";
						TARDISUpdateChatGUI.sendJSON(json, (Player) sender);
					} else {
						sender.sendMessage(i + ". Abandoned by: " + owner + ", location: " + l);
					}
					i++;
				}
			}
		} else {
			sender.sendMessage(Objects.requireNonNull(plugin.getLanguage().getString("ABANDONED_NONE")));
		}
	}
}
