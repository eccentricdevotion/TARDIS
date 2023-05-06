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
package me.eccentric_nz.TARDIS.commands.travel;

import java.util.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Command /tardistravel [arguments].
 * <p>
 * Time travel is the process of travelling through time, even in a non-linear
 * direction.
 *
 * @author eccentric_nz
 */
public class TARDISTravelCommands implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISTravelCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // if the player typed /tardistravel then do the following...
        if (cmd.getName().equalsIgnoreCase("tardistravel")) {
            if (sender instanceof Player player) {
                if (!TARDISPermission.hasPermission(player, "tardis.timetravel")) {
                    TARDISMessage.send(player, "NO_PERMS");
                    return true;
                }
                if (args.length < 1) {
                    new TARDISCommandHelper(plugin).getCommand("tardistravel", sender);
                    return true;
                }
                // get tardis data
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", player.getUniqueId().toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                if (!rs.resultSet()) {
                    TARDISMessage.send(player, "NO_TARDIS");
                    return true;
                }
                Tardis tardis = rs.getTardis();
                int id = tardis.getTardis_id();
                if (args[0].equalsIgnoreCase("cancel")) {
                    return new TARDISTravelCancel(plugin).action(player, id);
                }
                if (args[0].equalsIgnoreCase("costs")) {
                    return new TARDISTravelCosts(plugin).action(player);
                }
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                    TARDISMessage.send(player, "SIEGE_NO_CMD");
                    return true;
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("stop")) {
                    return new TARDISTravelStop(plugin).action(player, id);
                }
                int level = tardis.getArtron_level();
                boolean powered = tardis.isPowered_on();
                if (!tardis.isHandbrake_on() && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                    TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                    return true;
                }
                HashMap<String, Object> wheret = new HashMap<>();
                wheret.put("uuid", player.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                if (!rst.resultSet()) {
                    TARDISMessage.send(player, "NOT_IN_TARDIS");
                    return true;
                }
                int tardis_id = rst.getTardis_id();
                if (tardis_id != id) {
                    TARDISMessage.send(player, "CMD_ONLY_TL");
                    return true;
                }
                if (plugin.getConfig().getBoolean("allow.power_down") && !powered) {
                    TARDISMessage.send(player, "POWER_DOWN");
                    return true;
                }
                int travel = plugin.getArtronConfig().getInt("travel");
                if (level < travel) {
                    TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
                    return true;
                }
                if (TARDISPermission.hasPermission(player, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                    // get the exile area
                    return new TARDISTravelExile(plugin).action(player, id);
                }
                if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "cancel" -> {
                            return new TARDISTravelCancel(plugin).action(player, id);
                        }
                        case "costs" -> {
                            return new TARDISTravelCosts(plugin).action(player);
                        }
                        case "home" -> {
                            return new TARDISTravelHome(plugin).action(player, id);
                        }
                        case "back" -> {
                            return new TARDISTravelBack(plugin).action(player, id);
                        }
                        case "cave" -> {
                            return new TARDISTravelCave(plugin).action(player, id);
                        }
                        case "village", "structure" -> {
                            return new TARDISTravelStructure(plugin).action(player, args, id);
                        }
                        default -> {
                            return new TARDISTravelPlayer(plugin).action(player, args[0], id);
                        }
                    }
                }
                if (args.length == 2 && (args[0].equalsIgnoreCase("structure") || args[0].equalsIgnoreCase("village"))) {
                    return new TARDISTravelStructure(plugin).action(player, args, id);
                }
                if (args.length == 2 && args[0].equalsIgnoreCase("player")) {
                    return new TARDISTravelPlayer(plugin).action(player, args[1], id);
                }
                if (args.length == 2 && (args[1].equals("?") || args[1].equalsIgnoreCase("tpa"))) {
                    return new TARDISTravelAsk(plugin).action(player, args, id);
                }
                if (args[0].equalsIgnoreCase("biome")) {
                    // we're thinking this is a biome search
                    return new TARDISTravelBiome(plugin).action(player, args, id);
                }
                if (args.length == 2 && (args[0].equalsIgnoreCase("dest") || args[0].equalsIgnoreCase("save"))) {
                    // we're thinking this is a saved destination name
                    return new TARDISTravelSave(plugin).action(player, args, id, tardis.getPreset());
                }
                if (args.length == 2 && args[0].equalsIgnoreCase("area")) {
                    // we're thinking this is admin defined area name
                    return new TARDISTravelArea(plugin).action(player, args, id, tardis.getPreset());
                }
                if (!TARDISPermission.hasPermission(player, "tardis.timetravel.location")) {
                    TARDISMessage.send(player, "TRAVEL_NO_PERM_COORDS");
                    return true;
                }
                // coords of some sort
                return new TARDISTravelCoords(plugin).action(player, args, id);
            }
            return false;
        } else {
            TARDISMessage.send(sender, "CMD_PLAYER");
            return true;
        }
    }
}
