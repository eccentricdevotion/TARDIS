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
package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Perception filters had the effect of directing attention away from the object or its bearer, rendering them
 * unnoticeable. The Doctor was able to construct a perception filter around three keys to the tardis, activated when
 * they were worn around the neck on a chain.
 *
 * @author eccentric_nz
 */
public class TARDISPerceptionFilter {

	private final TARDIS plugin;
	private Team perceptionFilter;

	public TARDISPerceptionFilter(TARDIS plugin) {
		this.plugin = plugin;
	}

	public static void removePerceptionFilter() {
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		Team perceptionFilter = board.getTeam("PerceptionFilter");
		if (perceptionFilter != null) {
			for (OfflinePlayer olp : Bukkit.getServer().getOfflinePlayers()) {
				if (olp != null) {
					String entry = olp.getName();
					if (perceptionFilter.hasEntry(entry)) {
						perceptionFilter.removeEntry(entry);
					}
				}
			}
			perceptionFilter.unregister();
		}
	}

	public void createPerceptionFilter() {
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		perceptionFilter = board.getTeam("PerceptionFilter");
		if (perceptionFilter == null) {
			perceptionFilter = board.registerNewTeam("PerceptionFilter");
			perceptionFilter.setCanSeeFriendlyInvisibles(true);
			plugin.getServer().getOnlinePlayers().forEach((olp) -> perceptionFilter.addEntry(olp.getName()));
		}
	}

	public void addPerceptionFilter(Player player) {
		perceptionFilter.addEntry(player.getName());
		plugin.getServer().getOnlinePlayers().forEach(this::addPlayer);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
	}

	public void addPlayer(Player player) {
		if (!perceptionFilter.hasEntry(player.getName())) {
			perceptionFilter.addEntry(player.getName());
		}
	}

	public void removePerceptionFilter(Player player) {
		if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
	}
}
