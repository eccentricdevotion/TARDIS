/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.TardisLight;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISLightsCommand {

    private final TARDIS plugin;

    public TARDISLightsCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setLightsPref(Player player, String[] args) {
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LIGHT_NEED");
            return false;
        }
        String set_light = args[1].toUpperCase(Locale.ROOT);
        try {
            TardisLight.valueOf(set_light);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "LIGHT_NOT_VALID");
            return false;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("lights", set_light);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        TARDIS.plugin.getQueryFactory().doUpdate("player_prefs", set, where);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "LIGHT_SAVED");
        return true;
    }
}
