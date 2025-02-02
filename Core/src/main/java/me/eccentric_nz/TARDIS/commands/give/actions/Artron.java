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
package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.UUID;

public class Artron {

    private final TARDIS plugin;
    private final int full;

    public Artron(TARDIS plugin) {
        this.plugin = plugin;
        full = this.plugin.getArtronConfig().getInt("full_charge");
    }

    public void give(CommandSender sender, String player, int amount, boolean timelord) {
        // Look up this player's UUID
        UUID uuid = plugin.getServer().getOfflinePlayer(player).getUniqueId();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        HashMap<String, Object> set = new HashMap<>();
        HashMap<String, Object> wheret = new HashMap<>();
        int set_level = amount;
        if (timelord) {
            // get current level
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            if (rsp.resultSet()) {
                int current = rsp.getArtronLevel();
                set_level += current;
                set.put("artron_level", set_level);
                wheret.put("uuid", uuid.toString());
                plugin.getQueryFactory().doUpdate("player_prefs", set, wheret);
            }
        } else {
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                int id = tardis.getTardisId();
                int level = tardis.getArtronLevel();
                if (amount == 0) {
                    set_level = 0;
                } else {
                    // always fill to full and no more
                    if (level >= full && amount > 0) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "GIVE_FULL", player);
                        return;
                    }
                    if ((full - level) < amount) {
                        set_level = full;
                    } else {
                        set_level = level + amount;
                    }
                }
                set.put("artron_level", set_level);
                wheret.put("tardis_id", id);
                plugin.getQueryFactory().doUpdate("tardis", set, wheret);
            }
        }
        plugin.getMessenger().message(sender, TardisModule.TARDIS, player + "'s Artron Energy Level was set to " + set_level);
    }
}
