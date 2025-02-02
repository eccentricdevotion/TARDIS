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
package me.eccentric_nz.tardisvortexmanipulator.command;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TVMCommandSave {

    private final TARDIS plugin;

    public TVMCommandSave(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        String uuid = player.getUniqueId().toString();
        if (args.length == 1) {
            // list saves
            TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, 0, 10);
            if (rss.resultSet()) {
                TVMUtils.sendSaveList(player, rss, 1);
            }
            return true;
        }
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PAGE_NUM");
            return true;
        }
        try {
            int page = Integer.parseInt(args[1]);
            if (page <= 0) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MSG_INVALID");
                return true;
            }
            int start = (page * 10) - 10;
            int limit = page * 10;
            TVMResultSetSaves rss = new TVMResultSetSaves(plugin, uuid, start, limit);
            if (rss.resultSet()) {
                TVMUtils.sendSaveList(player, rss, page);
            }
            return true;
        } catch (NumberFormatException e) {
            plugin.debug("Wasn't a page number...");
            // check for existing save
            TVMResultSetWarpByName rs = new TVMResultSetWarpByName(plugin, uuid, args[1]);
            if (rs.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_EXISTS");
                return true;
            }
            Location l = player.getLocation();
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid);
            set.put("save_name", args[1]);
            set.put("world", l.getWorld().getName());
            set.put("x", l.getX());
            set.put("y", l.getY());
            set.put("z", l.getZ());
            set.put("yaw", l.getYaw());
            set.put("pitch", l.getPitch());
            plugin.getQueryFactory().doInsert("saves", set);
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_ADDED", args[1]);
            return true;
        }
    }
}
