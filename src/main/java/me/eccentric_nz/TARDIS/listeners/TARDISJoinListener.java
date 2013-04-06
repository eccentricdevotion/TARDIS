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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISBook;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAchievements;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJoinListener implements Listener {

    private final TARDIS plugin;

    public TARDISJoinListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for a player joining the server. If the player has TARDIS
     * permissions (ie not a guest), then check whether they have achieved the
     * building of a TARDIS. If not then insert an achievement record and give
     * them the tardis book.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("tardis.book")) {
            String playerNameStr = player.getName();
            // check if they have started building a TARDIS yet
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("player", playerNameStr);
            where.put("name", "tardis");
            ResultSetAchievements rsa = new ResultSetAchievements(plugin, where, false);
            if (!rsa.resultSet()) {
                //add a record
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("player", player.getName());
                set.put("name", "tardis");
                QueryFactory qf = new QueryFactory(plugin);
                qf.doInsert("achievements", set);
                TARDISBook book = new TARDISBook(plugin);
                // title, author, filename, player
                book.writeBook("Get transport", "Rassilon", "tardis", player);
            }
        }
    }
}
