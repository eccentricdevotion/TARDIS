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
package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicInventory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TelepathicCircuitInteraction {

    private final TARDIS plugin;

    public TelepathicCircuitInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player) {
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getFlight().containsKey(uuid)) {
            return;
        }
        if (player.isSneaking()) {
            // get current telepathic setting
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            int b = (rsp.resultSet() && rsp.isTelepathyOn()) ? 0 : 1;
            // update database
            HashMap<String, Object> set = new HashMap<>();
            HashMap<String, Object> whereu = new HashMap<>();
            whereu.put("uuid", uuid.toString());
            set.put("telepathy_on", b);
            plugin.getQueryFactory().doUpdate("player_prefs", set, whereu);
            plugin.getMessenger().announceRepeater(player, "Telepathic Circuit " + (b == 1 ? "ON" : "OFF"));
        } else {
            // open GUI for
            // toggling telepathic circuit on/off
            // cave finder
            // structure finder
            // biome finder
            player.openInventory(new TARDISTelepathicInventory(plugin, player).getInventory());
        }
    }
}
