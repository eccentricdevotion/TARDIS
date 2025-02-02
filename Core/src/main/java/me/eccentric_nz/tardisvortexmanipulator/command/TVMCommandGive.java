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
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TVMCommandGive {

    private final TARDIS plugin;
    private final int full;

    public TVMCommandGive(TARDIS plugin) {
        this.plugin = plugin;
        full = this.plugin.getVortexConfig().getInt("tachyon_use.max");
    }

    public boolean process(CommandSender sender, String[] args) {
        if (!TARDISPermission.hasPermission(sender, "tardis.admin")) {
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        if (args.length < 3) {
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_UUID");
            return true;
        }
        Player p = plugin.getServer().getPlayer(args[1]);
        if (p == null || !p.isOnline()) {
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "NOT_ONLINE");
            return true;
        }
        UUID uuid = p.getUniqueId();
        // check for existing record
        TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, uuid.toString());
        if (rs.resultSet()) {
            int tachyon_level = rs.getTachyonLevel();
            int amount;
            if (args[2].equalsIgnoreCase("full")) {
                amount = full;
            } else if (args[2].equalsIgnoreCase("empty")) {
                amount = 0;
            } else {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_LAST_ARG");
                    return true;
                }
                if (tachyon_level + amount > full) {
                    amount = full;
                } else {
                    amount += tachyon_level;
                }
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("tachyon_level", amount);
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            plugin.getQueryFactory().doUpdate("manipulator", set, where);
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_TACHYON_SET", "" + amount);
        } else {
            plugin.getMessenger().send(sender, TardisModule.VORTEX_MANIPULATOR, "VM_NONE", p.getName());
        }
        return true;
    }
}
