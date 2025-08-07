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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISDirectionCommand;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DirectionAction {

    private final TARDIS plugin;

    public DirectionAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public String rotate(int id, Player player) {
        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
            return "";
        }
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            Current current = rsc.getCurrent();
            String direction = current.direction().toString();
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
            if (rst.resultSet()) {
                if (!rst.getTardis().getPreset().usesArmourStand()) {
                    // skip the angled rotations
                    switch (current.direction()) {
                        case SOUTH -> direction = "SOUTH_WEST";
                        case EAST -> direction = "SOUTH_EAST";
                        case NORTH -> direction = "NORTH_EAST";
                        case WEST -> direction = "NORTH_WEST";
                        default -> {
                        }
                    }
                }
                int ordinal = COMPASS.valueOf(direction).ordinal() + 1;
                if (ordinal == 8) {
                    ordinal = 0;
                }
                direction = COMPASS.values()[ordinal].toString();
            }
            String[] args = new String[]{"direction", direction};
            new TARDISDirectionCommand(plugin).changeDirection(player, args);
            return direction;
        }
        return "";
    }
}
