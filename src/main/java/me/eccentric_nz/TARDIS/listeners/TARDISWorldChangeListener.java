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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;

import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTraveledTo;

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
    private void handleWorld(Player p) {
        Environment env = p.getWorld().getEnvironment();

        // check if player has been to this dimension before
        ResultSetTraveledTo rs = new ResultSetTraveledTo(plugin);
        if (!rs.resultSet(p, env)) {
            // add this dimension to the database
            QueryFactory queryFactory = plugin.getQueryFactory();
            HashMap<String, Object> values = new HashMap<>();
            values.put("uuid", p.getUniqueId().toString());
            values.put("environment", env.toString());

            queryFactory.doInsert("traveled_to", values);
        }
    }
}