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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetFlightCommand {

    private final TARDIS plugin;

    public TARDISSetFlightCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setMode(Player player, String[] args, QueryFactory qf) {
        if (args.length < 2) {
            TARDISMessage.send(player, "FLIGHT_NEED");
            return false;
        }
        FlightMode fm;
        try {
            fm = FlightMode.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(player, "FLIGHT_INFO");
            return true;
        }
        int mode = 1;
        switch (fm) {
            case REGULATOR:
                mode = 2;
                break;
            case MANUAL:
                mode = 3;
                break;
            default:
                break;
        }
        HashMap<String, Object> setf = new HashMap<String, Object>();
        setf.put("flying_mode", mode);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", player.getUniqueId().toString());
        qf.doUpdate("player_prefs", setf, where);
        TARDISMessage.send(player, "FLIGHT_SAVED");
        return true;
    }

    public enum FlightMode {

        NORMAL, REGULATOR, MANUAL
    }
}
