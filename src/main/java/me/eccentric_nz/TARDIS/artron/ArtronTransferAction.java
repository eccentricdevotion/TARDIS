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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ArtronTransferAction {

    private final TARDIS plugin;

    public ArtronTransferAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void add(int current_level, int level, Player player, int id, boolean hasPrefs) {
        // transfer player artron energy into the capacitor
        int fc = plugin.getArtronConfig().getInt("full_charge");
        int ten_percent = Math.round(fc * 0.1F);
        if (current_level >= ten_percent && plugin.getConfig().getBoolean("creation.create_worlds")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_UNDER_10");
            return;
        }
        if (hasPrefs) {
            if (level < 1) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NONE");
                return;
            }
            int new_level = current_level + level;
            // set player level to 0
            HashMap<String, Object> set = new HashMap<>();
            set.put("artron_level", 0);
            HashMap<String, Object> wherel = new HashMap<>();
            wherel.put("uuid", player.getUniqueId().toString());
            plugin.getQueryFactory().doUpdate("player_prefs", set, wherel);
            // add player level to TARDIS level
            HashMap<String, Object> sett = new HashMap<>();
            sett.put("artron_level", new_level);
            HashMap<String, Object> whereid = new HashMap<>();
            whereid.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("tardis", sett, whereid);
            int percent = Math.round((new_level * 100F) / fc);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_CHARGED", String.format("%d", percent));
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NONE");
        }
    }
}
