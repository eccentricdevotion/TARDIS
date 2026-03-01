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
package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TVMCommandRemove {

    private final TARDIS plugin;

    public TVMCommandRemove(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Player player, String name) {
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return;
        }
        String uuid = player.getUniqueId().toString();
        // check for existing save
        TVMResultSetWarpByName rs = new TVMResultSetWarpByName(plugin, uuid, name);
        if (rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_NONE");
            return;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        where.put("save_name", name);
        plugin.getQueryFactory().doDelete("saves", where);
        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_REMOVED", name);
    }
}
