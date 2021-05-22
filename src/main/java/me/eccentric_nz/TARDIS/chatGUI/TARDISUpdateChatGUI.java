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
package me.eccentric_nz.tardis.chatGUI;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.entity.Player;

public class TARDISUpdateChatGUI {

	private final TARDIS plugin;

	public TARDISUpdateChatGUI(TARDIS plugin) {
		this.plugin = plugin;
	}

	public static void sendJSON(String json, Player p) {
		TARDIS.plugin.getTardisHelper().sendJson(p, json);
	}

	public boolean showInterface(Player player, String[] args) {
		if (args.length == 1) {
			TARDISMessage.send(player, "UPDATE_SECTION");
			player.sendMessage("------");
			plugin.getJsonKeeper().getSections().forEach((s) -> sendJSON(s, player));
			player.sendMessage("------");
			return true;
		}
		if (args[1].equalsIgnoreCase("controls")) {
			TARDISMessage.send(player, "UPDATE_SECTION");
			player.sendMessage("------");
			plugin.getJsonKeeper().getControls().forEach((c) -> sendJSON(c, player));
			player.sendMessage("------");
			return true;
		}
		if (args[1].equalsIgnoreCase("interfaces")) {
			TARDISMessage.send(player, "UPDATE_INTERFACE");
			player.sendMessage("------");
			plugin.getJsonKeeper().getInterfaces().forEach((i) -> sendJSON(i, player));
			player.sendMessage("------");
			return true;
		}
		if (args[1].equalsIgnoreCase("locations")) {
			TARDISMessage.send(player, "UPDATE_LOCATION");
			player.sendMessage("------");
			plugin.getJsonKeeper().getLocations().forEach((l) -> sendJSON(l, player));
			player.sendMessage("------");
			return true;
		}
		if (args[1].equalsIgnoreCase("others")) {
			TARDISMessage.send(player, "UPDATE_OTHER");
			player.sendMessage("------");
			plugin.getJsonKeeper().getOthers().forEach((o) -> sendJSON(o, player));
			player.sendMessage("------");
			return true;
		}
		return false;
	}
}
