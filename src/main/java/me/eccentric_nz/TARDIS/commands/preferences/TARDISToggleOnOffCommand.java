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
package me.eccentric_nz.tardis.commands.preferences;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.artron.TARDISBeaconToggler;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISToggleOnOffCommand {

	private final TARDISPlugin plugin;
	private final List<String> was;

	TARDISToggleOnOffCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
		was = Arrays.asList("auto", "auto_powerup", "auto_siege", "beacon", "build", "ctm", "difficulty", "dnd", "eps", "farm", "font", "hads", "minecart", "renderer", "submarine", "travelbar", "telepathy");
	}

	public boolean toggle(Player player, String[] args) {
		String pref = args[0];
		if (pref.equals("auto") && !plugin.getConfig().getBoolean("allow.autonomous")) {
			TARDISMessage.send(player, "AUTO_DISABLED");
			return true;
		}
		if (pref.equals("auto_powerup") && !plugin.getConfig().getBoolean("allow.power_down")) {
			TARDISMessage.send(player, "POWER_DOWN_DISABLED");
			return true;
		}
		if (pref.equals("eps") && !plugin.getConfig().getBoolean("allow.emergency_npc")) {
			TARDISMessage.send(player, "EP1_DISABLED");
			return true;
		}
		if (pref.equals("hads") && !plugin.getConfig().getBoolean("allow.hads")) {
			TARDISMessage.send(player, "HADS_DISBALED");
			return true;
		}
		HashMap<String, Object> setp = new HashMap<>();
		HashMap<String, Object> wherep = new HashMap<>();
		wherep.put("uuid", player.getUniqueId().toString());
		if (args[1].equalsIgnoreCase("on")) {
			setp.put(pref + "_on", 1);
			if (pref.equals("beacon")) {
				UUID uuid = player.getUniqueId();
				// get tardis id
				ResultSetTardisID rsi = new ResultSetTardisID(plugin);
				if (rsi.fromUUID(uuid.toString())) {
					new TARDISBeaconToggler(plugin).flickSwitch(uuid, rsi.getTardisId(), true);
				}
			}
			String grammar = (was.contains(pref)) ? "PREF_WAS_ON" : "PREF_WERE_ON";
			TARDISMessage.send(player, grammar, pref);
		}
		if (args[1].equalsIgnoreCase("off")) {
			setp.put(pref + "_on", 0);
			if (pref.equals("beacon")) {
				UUID uuid = player.getUniqueId();
				// get tardis id
				ResultSetTardisID rsi = new ResultSetTardisID(plugin);
				if (rsi.fromUUID(uuid.toString())) {
					new TARDISBeaconToggler(plugin).flickSwitch(uuid, rsi.getTardisId(), false);
				}
			}
			String grammar = (was.contains(pref)) ? "PREF_WAS_OFF" : "PREF_WERE_OFF";
			TARDISMessage.send(player, grammar, pref);
		}
		plugin.getQueryFactory().doUpdate("player_prefs", setp, wherep);
		return true;
	}
}
