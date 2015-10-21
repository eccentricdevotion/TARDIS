/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Perception filters had the effect of directing attention away from the object
 * or its bearer, rendering them unnoticeable. The Doctor was able to construct
 * a perception filter around three keys to the TARDIS, activated when they were
 * worn around the neck on a chain.
 *
 * @author eccentric_nz
 */
public class TARDISPerceptionFilter {

    private final TARDIS plugin;
    private Team perceptionFilter;

    public TARDISPerceptionFilter(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void createPerceptionFilter() {
        Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
        perceptionFilter = board.getTeam("PerceptionFilter");
        if (perceptionFilter == null) {
            perceptionFilter = board.registerNewTeam("PerceptionFilter");
            perceptionFilter.setCanSeeFriendlyInvisibles(true);
            for (Player olp : plugin.getServer().getOnlinePlayers()) {
                perceptionFilter.addPlayer(olp);
            }
        }
    }

    public static void removePerceptionFilter() {
        Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        Team perceptionFilter = board.getTeam("PerceptionFilter");
        if (perceptionFilter != null) {
            for (OfflinePlayer olp : Bukkit.getServer().getOfflinePlayers()) {
                perceptionFilter.removePlayer(olp);
            }
            perceptionFilter.unregister();
        }
    }

    public void addPerceptionFilter(Player player) {
        perceptionFilter.addPlayer(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
    }

    public void addPlayer(Player player) {
        if (!perceptionFilter.hasPlayer(player)) {
            perceptionFilter.addPlayer(player);
        }
    }

    public void removePerceptionFilter(Player player) {
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }
}
