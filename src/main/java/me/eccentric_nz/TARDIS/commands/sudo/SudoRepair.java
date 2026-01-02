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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.sudo;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.desktop.TARDISRepair;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SudoRepair {

    private final TARDIS plugin;
    private final UUID uuid;
    private final boolean clean;

    public SudoRepair(TARDIS plugin, UUID uuid, boolean clean) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.clean = clean;
    }

    public boolean repair() {
        Player player = plugin.getServer().getPlayer(uuid);
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
            return false;
        }
        Tardis tardis = rs.getTardis();
        // get player's current console
        Schematic current_console = tardis.getSchematic();
        int level = tardis.getArtronLevel();
        TARDISUpgradeData tud = new TARDISUpgradeData();
        tud.setPrevious(current_console);
        tud.setLevel(level);
        plugin.getTrackerKeeper().getUpgrades().put(player.getUniqueId(), tud);
        TARDISRepair tr = new TARDISRepair(plugin, player);
        tr.restore(clean);
        return true;
    }
}
