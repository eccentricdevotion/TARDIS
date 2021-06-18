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
package me.eccentric_nz.tardis.commands.bind;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.commands.TardisCommandHelper;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.Bind;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * A Time Control Unit is a golden sphere about the size of a Cricket ball. It is stored in the Secondary Control Room.
 * All TARDISes have one of these devices, which can be used to remotely control a tardis by broadcasting Stattenheim
 * signals that travel along the time contours in the Space/Time Vortex.
 *
 * @author eccentric_nz
 */
public class TardisBindCommands implements CommandExecutor {

    private final TardisPlugin plugin;

    public TardisBindCommands(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisbind")) {
            if (!TardisPermission.hasPermission(sender, "tardis.update")) {
                TardisMessage.send(sender, "NO_PERMS");
                return false;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TardisMessage.send(sender, "CMD_PLAYER");
                return false;
            }
            if (args.length < 2) {
                TardisMessage.send(player, "TOO_FEW_ARGS");
                new TardisCommandHelper(plugin).getCommand("tardisbind", sender);
                return false;
            }
            Bind bind;
            try {
                bind = Bind.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                TardisMessage.send(player, "BIND_NOT_VALID");
                return false;
            }
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TardisMessage.send(player, "NOT_A_TIMELORD");
                return false;
            }
            int id = rs.getTardisId();
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                TardisMessage.send(player, "NOT_IN_TARDIS");
                return false;
            }
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length < bind.getArgs()) {
                    TardisMessage.send(player, "TOO_FEW_ARGS");
                    return false;
                }
                return new BindAdd(plugin).setClick(bind, player, id, args);
            } else if (args[0].equalsIgnoreCase("remove")) {
                return new BindRemove(plugin).setClick(bind, player);
            }
        }
        return false;
    }
}
