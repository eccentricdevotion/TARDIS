/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.Hum;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISHumCommand {

    private final TARDIS plugin;

    public TARDISHumCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setHumPref(Player player, String[] args) {
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "HUM_NEED");
            return false;
        }
        Hum go;
        try {
            go = Hum.valueOf(args[1].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "HUM_NOT_VALID");
            return false;
        }
        String hum_set = (go.equals(Hum.RANDOM)) ? "" : go.toString().toLowerCase(Locale.ROOT);
        HashMap<String, Object> set = new HashMap<>();
        set.put("hum", hum_set);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        TARDIS.plugin.getQueryFactory().doUpdate("player_prefs", set, where);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "HUM_SAVED");
        return true;
    }
}
