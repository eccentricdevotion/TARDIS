/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetScreenText;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ConsoleTextCommand {

    private final TARDIS plugin;
    private final List<String> SCREEN_SUBS = List.of("forward", "backward", "left", "right");

    public ConsoleTextCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean move(Player player, String[] args) {
        if (args.length < 2) {
            return false;
        }
        if (!SCREEN_SUBS.contains(args[1].toLowerCase(Locale.ROOT))) {
            return false;
        }
        // get tardis player is in
        String uuid = player.getUniqueId().toString();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (!rst.resultSet()) {
            return true;
        }
        int id = rst.getTardis_id();
        // get text display
        ResultSetScreenText rsst = new ResultSetScreenText(plugin, id);
        rsst.resultSetAsync(resultSetOccupied -> {
            UUID t = rsst.getUuid();
            Entity entity = plugin.getServer().getEntity(t);
            if (!(entity instanceof TextDisplay textDisplay)) {
                plugin.debug("Entity not a text display");
                return;
            }
            Location location = textDisplay.getLocation();
            double x;
            double z;
            switch (args[1].toLowerCase(Locale.ROOT)) {
                case "forward" -> {
                    x = 0.025d;
                    z = 0.025d;
                }
                case "backward" -> {
                    x = -0.025d;
                    z = -0.025d;
                }
                case "left" -> {
                    x = 0.025d;
                    z = 0;
                }
                case "right" -> {
                    x = 0;
                    z = 0.025d;
                }
                default -> {
                    x = 0;
                    z = 0;
                }
            }
            Location cloned = location.clone().add(x, 0, z);
            textDisplay.teleport(cloned);
        });
        return true;
    }
}
