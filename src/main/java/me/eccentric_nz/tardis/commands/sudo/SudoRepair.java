/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.commands.sudo;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.desktop.TardisRepairer;
import me.eccentric_nz.tardis.desktop.TardisUpgradeData;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SudoRepair {

    private final TardisPlugin plugin;
    private final UUID uuid;
    private final boolean clean;

    public SudoRepair(TardisPlugin plugin, UUID uuid, boolean clean) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.clean = clean;
    }

    public boolean repair() {
        Player player = plugin.getServer().getPlayer(uuid);
        HashMap<String, Object> where = new HashMap<>();
        assert player != null;
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TardisMessage.send(player, "NO_TARDIS");
            return false;
        }
        Tardis tardis = rs.getTardis();
        // get player's current console
        Schematic current_console = tardis.getSchematic();
        int level = tardis.getArtronLevel();
        TardisUpgradeData tud = new TardisUpgradeData();
        tud.setPrevious(current_console);
        tud.setLevel(level);
        plugin.getTrackerKeeper().getUpgrades().put(player.getUniqueId(), tud);
        TardisRepairer tr = new TardisRepairer(plugin, player);
        tr.restore(clean);
        return true;
    }
}
