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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.FlightMode;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISSetFlightCommand {

    private final TARDIS plugin;

    public TARDISSetFlightCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setMode(Player player, String[] args) {
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_NEED");
            return false;
        }
        FlightMode fm;
        try {
            fm = FlightMode.valueOf(args[1].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_INFO");
            return true;
        }
        int mode = 1;
        switch (fm) {
            case REGULATOR -> mode = 2;
            case MANUAL -> mode = 3;
            case EXTERIOR -> mode = 4;
            default -> {
            }
        }
        HashMap<String, Object> setf = new HashMap<>();
        setf.put("flying_mode", mode);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        plugin.getQueryFactory().doUpdate("player_prefs", setf, where);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "FLIGHT_SAVED");
        return true;
    }
}
