/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.artron;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 *
 * @author eccentric_nz
 */
public class TARDISArtronIndicator {

    private final TARDIS plugin;
    private final ScoreboardManager manager;
    private final int fc;

    public TARDISArtronIndicator(TARDIS plugin) {
        this.plugin = plugin;
        this.manager = plugin.getServer().getScoreboardManager();
        this.fc = plugin.getArtronConfig().getInt("full_charge");
    }

    public void showArtronLevel(final Player p, int id, boolean hide, int used) {
        Scoreboard board = manager.getMainScoreboard();
        Objective tmp = board.getObjective("TARDIS");
        if (tmp == null) {
            tmp = board.registerNewObjective("TARDIS", "Artron");
        }
        final Objective objective = tmp;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Artron Energy");
        Score current = objective.getScore(plugin.getServer().getOfflinePlayer(ChatColor.GREEN + "Remaining:"));
        Score max = objective.getScore(plugin.getServer().getOfflinePlayer(ChatColor.AQUA + "Maximum:"));
        Score percentage = objective.getScore(plugin.getServer().getOfflinePlayer(ChatColor.LIGHT_PURPLE + "Percent:"));
        Score timelord = objective.getScore(plugin.getServer().getOfflinePlayer(ChatColor.YELLOW + "Time Lord:"));
        if (used > 0) {
            Score amount_used = objective.getScore(plugin.getServer().getOfflinePlayer(ChatColor.RED + "Used:"));
            amount_used.setScore(used);
        }
        // get Artron level
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            int current_level = rs.getArtron_level();
            int percent = Math.round((current_level * 100F) / fc);
            current.setScore(current_level);
            max.setScore(fc);
            percentage.setScore(percent);
        }
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("player", p.getName());
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
        if (rsp.resultSet()) {
            timelord.setScore(rsp.getArtronLevel());
        }
        p.setScoreboard(board);
        if (hide) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    objective.setDisplaySlot(null);
//                    p.setScoreboard(manager.getMainScoreboard());
                }
            }, 150L);
        }
    }
}
