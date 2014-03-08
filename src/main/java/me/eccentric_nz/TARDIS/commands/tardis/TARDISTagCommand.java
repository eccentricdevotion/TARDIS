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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTag;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTagCommand {

    private final TARDIS plugin;

    public TARDISTagCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean getStats(Player player) {
        ResultSetTag rs = new ResultSetTag(plugin);
        TARDISMessage.send(player, plugin.getPluginName() + "Here are the stats:");
        String who = (!plugin.getTagConfig().getString("it").equals("")) ? plugin.getTagConfig().getString("it") : "No one";
        TARDISMessage.send(player, who + " is currently the " + ChatColor.RED + "'OOD'");
        TARDISMessage.send(player, "-----------");
        TARDISMessage.send(player, ChatColor.GOLD + "Top 5 OODs");
        TARDISMessage.send(player, "-----------");
        if (rs.resultSet()) {
            ArrayList<HashMap<String, String>> data = rs.getData();
            for (HashMap<String, String> map : data) {
                String p = map.get("player");
                long t = plugin.getUtils().parseLong(map.get("time"));
                TARDISMessage.send(player, p + ": " + ChatColor.GREEN + getHoursMinutesSeconds(t));
            }
        } else {
            TARDISMessage.send(player, "The are no stats yet :(");
        }
        TARDISMessage.send(player, "-----------");
        return true;
    }

    private String getHoursMinutesSeconds(long millis) {
        return String.format("%02dh:%02dm:%02ds",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
