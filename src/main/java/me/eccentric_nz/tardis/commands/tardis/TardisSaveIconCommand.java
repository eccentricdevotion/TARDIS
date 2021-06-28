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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetDestinations;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

class TardisSaveIconCommand {

    private final TardisPlugin plugin;

    TardisSaveIconCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean changeIcon(Player player, String[] args) {
        if (TardisPermission.hasPermission(player, "tardis.save")) {
            if (args.length < 3) {
                TardisMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            ResultSetTardisId rs = new ResultSetTardisId(plugin);
            if (!rs.fromUuid(player.getUniqueId().toString())) {
                TardisMessage.send(player, "NO_TARDIS");
                return false;
            }
            int id = rs.getTardisId();
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("dest_name", args[1]);
            whered.put("tardis_id", id);
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
            if (!rsd.resultSet()) {
                TardisMessage.send(player, "SAVE_NOT_FOUND");
                return false;
            }
            Material material;
            try {
                material = Material.valueOf(args[2].toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                TardisMessage.send(player, "MATERIAL_NOT_VALID");
                return false;
            }
            int destID = rsd.getDestId();
            HashMap<String, Object> did = new HashMap<>();
            did.put("dest_id", destID);
            HashMap<String, Object> set = new HashMap<>();
            set.put("icon", material.toString());
            plugin.getQueryFactory().doUpdate("destinations", set, did);
            TardisMessage.send(player, "DEST_ICON", material.toString());
            return true;
        }
        return true;
    }
}
