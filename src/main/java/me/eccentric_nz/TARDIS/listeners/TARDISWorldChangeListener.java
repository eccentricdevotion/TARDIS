/*
 * Copyright (C) 2023 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravelledTo;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

/**
 * @author Technoguyfication
 */
public class TARDISWorldChangeListener implements Listener {

    private final TARDIS plugin;

    public TARDISWorldChangeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    // lowest priority because we aren't affecting the world change
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        handleWorld(event.getPlayer());
    }

    // handle player joins too
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        handleWorld(event.getPlayer());
    }

    // add that the player has been to this world to the database
    private void handleWorld(Player player) {
        Environment environment = player.getWorld().getEnvironment();
        // check if player has been to this dimension before
        ResultSetTravelledTo rs = new ResultSetTravelledTo(plugin);
        if (!rs.resultSet(player.getUniqueId().toString(), environment.toString())) {
            // add this dimension to the database
            QueryFactory queryFactory = plugin.getQueryFactory();
            HashMap<String, Object> values = new HashMap<>();
            values.put("uuid", player.getUniqueId().toString());
            values.put("environment", environment.toString());
            queryFactory.doInsert("traveled_to", values);
        }
    }
}
