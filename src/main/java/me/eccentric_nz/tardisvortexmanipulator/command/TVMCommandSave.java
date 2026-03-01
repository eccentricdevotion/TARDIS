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
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TVMCommandSave {

    private final TARDIS plugin;

    public TVMCommandSave(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void send(Player player, int page) {
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return;
        }
        String uuid = player.getUniqueId().toString();
        if (page == 0) {
            // list saves
            TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, 0, 10);
            if (rss.resultSet()) {
                TVMUtils.sendSaveList(player, rss, 1);
            }
        } else {
            if (page < 0) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_INVALID");
                return;
            }
            int start = (page * 10) - 10;
            int limit = page * 10;
            TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, start, limit);
            if (rss.resultSet()) {
                TVMUtils.sendSaveList(player, rss, page);
            }
        }
    }

    public void save(Player player, String name) {
        String uuid = player.getUniqueId().toString();
        // check for existing save
        TVMResultSetWarpByName rs = new TVMResultSetWarpByName(plugin, uuid, name);
        if (rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_EXISTS");
            return;
        }
        Location l = player.getLocation();
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", uuid);
        set.put("save_name", name);
        set.put("world", l.getWorld().getName());
        set.put("x", l.getX());
        set.put("y", l.getY());
        set.put("z", l.getZ());
        set.put("yaw", l.getYaw());
        set.put("pitch", l.getPitch());
        plugin.getQueryFactory().doInsert("saves", set);
        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_ADDED", name);
    }
}

