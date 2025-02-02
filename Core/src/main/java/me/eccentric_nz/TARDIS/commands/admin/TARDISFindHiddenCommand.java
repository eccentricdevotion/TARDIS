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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Hidden;
import me.eccentric_nz.TARDIS.database.data.ProtectedBlock;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFindHidden;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFindProtected;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TARDISFindHiddenCommand {

    public boolean search(TARDIS plugin, CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            Location location = player.getLocation();
            int radius = 16;
            if (args.length > 1) {
                int parsed = TARDISNumberParsers.parseInt(args[1]);
                if (parsed > 0) {
                    radius = parsed;
                }
            }
            ResultSetFindHidden rsfh = new ResultSetFindHidden(plugin);
            List<Hidden> data = rsfh.search(location, radius);
            if (!data.isEmpty()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HIDDEN_FOUND");
                int i = 1;
                for (Hidden h : data) {
                    plugin.getMessenger().message(player, i
                            + ". X=" + h.getX()
                            + ", Y=" + h.getY()
                            + ", Z=" + h.getZ()
                            + ", owned by " + h.getOwner()
                            + ", " + h.getStatus());
                    i++;
                }
            } else {
                ResultSetFindProtected rsfp = new ResultSetFindProtected(plugin);
                List<ProtectedBlock> blocks = rsfp.search(location, radius);
                if (!blocks.isEmpty()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "PROTECTED_FOUND");
                    int i = 1;
                    for (ProtectedBlock h : blocks) {
                        plugin.getMessenger().sendProtected(player, i
                                + ". X=" + h.getX()
                                + ", Y=" + h.getY()
                                + ", Z=" + h.getZ()
                                + ", PROTECTED ", h.getLocation(), h.getId());
                        i++;
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "HIDDEN_NONE");
                }
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
        }
        return true;
    }
}
