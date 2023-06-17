/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetWarpByName;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author macgeek
 */
public class TVMCommandGo {

    private final TARDIS plugin;

    public TVMCommandGo(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean execute(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_CMD");
            return true;
        }
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_SAVE_NAME");
            return true;
        }
        String uuid = player.getUniqueId().toString();
        if (args.length == 2) {
            // check save exists
            TVMResultSetWarpByName rsw = new TVMResultSetWarpByName(plugin, uuid, args[1]);
            if (!rsw.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_SAVE");
                return true;
            }
            Location l = rsw.getWarp();
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_STANDBY_TO", args[1]);
            while (!l.getChunk().isLoaded()) {
                l.getChunk().load();
            }
            List<Player> players = new ArrayList<>();
            players.add(player);
            if (plugin.getVortexConfig().getBoolean("allow.multiple")) {
                for (Entity e : player.getNearbyEntities(0.5d, 0.5d, 0.5d)) {
                    if (e instanceof Player && !e.getUniqueId().equals(player.getUniqueId())) {
                        players.add((Player) e);
                    }
                }
            }
            int required = plugin.getVortexConfig().getInt("tachyon_use.saved") * players.size();
            if (!TVMUtils.checkTachyonLevel(uuid, required)) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NEED_TACHYON", required);
                return true;
            }
            TVMUtils.movePlayers(players, l, player.getLocation().getWorld());
            // remove tachyons
            new TVMQueryFactory(plugin).alterTachyons(uuid, -required);
            return true;
        } else {
            // coords

            return true;
        }
    }
}
