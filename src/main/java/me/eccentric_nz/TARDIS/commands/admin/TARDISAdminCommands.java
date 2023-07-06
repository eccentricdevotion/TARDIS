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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.arch.TARDISArchCommand;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.database.tool.Converter;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.maze.TARDISMazeBuilder;
import me.eccentric_nz.TARDIS.maze.TARDISMazeGenerator;
import me.eccentric_nz.TARDIS.utility.UpdateTARDISPlugins;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Command /tardisadmin [arguments].
 * <p>
 * The Lord President was the most powerful member of the Time Lord Council and had near absolute authority, and used a
 * link to the Matrix, a vast computer network containing the knowledge and experiences of all past generations of Time
 * Lords, to set Time Lord policy and remain alert to potential threats from lesser civilisations.
 *
 * @author eccentric_nz
 */
public class TARDISAdminCommands implements CommandExecutor {

    private final Set<String> firstsStr = new HashSet<>();

    private final TARDIS plugin;

    public TARDISAdminCommands(TARDIS plugin) {
        this.plugin = plugin;
        // add first arguments
        firstsStr.add("arch");
        firstsStr.add("assemble");
        firstsStr.add("condenser");
        firstsStr.add("convert_database");
        firstsStr.add("create");
        firstsStr.add("decharge");
        firstsStr.add("delete");
        firstsStr.add("disguise");
        firstsStr.add("dispersed");
        firstsStr.add("enter");
        firstsStr.add("find");
        firstsStr.add("list");
        firstsStr.add("make_preset");
        firstsStr.add("maze");
        firstsStr.add("mvimport");
        firstsStr.add("playercount");
        firstsStr.add("prune");
        firstsStr.add("prunelist");
        firstsStr.add("purge");
        firstsStr.add("purge_portals");
        firstsStr.add("recharger");
        firstsStr.add("region_flag");
        firstsStr.add("reload");
        firstsStr.add("repair");
        firstsStr.add("revoke");
        firstsStr.add("set_size");
        firstsStr.add("spawn_abandoned");
        firstsStr.add("undisguise");
        firstsStr.add("update_plugins");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player typed /tardisadmin then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisadmin")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    new TARDISCommandHelper(plugin).getCommand("tardisadmin", sender);
                    return true;
                }
                String first = args[0].toLowerCase(Locale.ENGLISH);
                if (!firstsStr.contains(first)) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_NOT_VALID");
                    return false;
                }
                if (args.length == 1) {
                    if (first.equals("condenser")) {
                        return new TARDISCondenserCommand(plugin).set(sender);
                    }
                    if (first.equals("convert_database")) {
                        try {
                            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Converter(plugin, sender));
                            return true;
                        } catch (Exception e) {
                            plugin.getMessenger().message(sender, "Database conversion failed! " + e.getMessage());
                            return true;
                        }
                    }
                    if (first.equals("update_plugins")) {
                        if (!sender.isOp()) {
                            plugin.getMessenger().message(sender, "You must be a server operator to run this command!");
                            return true;
                        }
                        return new UpdateTARDISPlugins(plugin).fetchFromJenkins(sender);
                    }
                    if (first.equals("maze")) {
                        if (sender instanceof Player p) {
                            Location l = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
                            TARDISMazeGenerator generator = new TARDISMazeGenerator();
                            generator.makeMaze();
                            TARDISMazeBuilder builder = new TARDISMazeBuilder(generator.getMaze(), l);
                            builder.build(false);
                        }
                        return true;
                    }
                    if (first.equals("mvimport")) {
                        if (!plugin.getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_ENABLED");
                        }
                        plugin.getMVHelper().importWorlds(sender);
                        return true;
                    }
                }
                if (first.equals("create")) {
                    return new TARDISCreateCommand(plugin).buildTARDIS(sender, args);
                }
                if (first.equals("find")) {
                    return new TARDISFindHiddenCommand().search(plugin, sender, args);
                }
                if (first.equals("list")) {
                    return new TARDISListCommand(plugin).listStuff(sender, args);
                }
                if (first.equals("purge_portals")) {
                    return new TARDISPortalCommand(plugin).clearAll(sender);
                }
                if (first.equals("undisguise")) {
                    return new TARDISDisguiseCommand(plugin).disguise(sender, args);
                }
                if (args.length < 2) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                    return false;
                }
                if (first.equals("arch")) {
                    if (args.length > 2) {
                        return new TARDISArchCommand(plugin).force(sender, args);
                    } else {
                        return new TARDISArchCommand(plugin).whois(sender, args);
                    }
                }
                if (first.equals("assemble")) {
                    return new TARDISDispersedCommand(plugin).assemble(sender, args[1]);
                }
                if (first.equals("set_size")) {
                    return new TARDISSetSizeCommand(plugin).overwrite(sender, args);
                }
                if (first.equals("spawn_abandoned")) {
                    return new TARDISAbandonedCommand(plugin).spawn(sender, args);
                }
                if (first.equals("make_preset")) {
                    return new TARDISMakePresetCommand(plugin).scanBlocks(sender, args);
                }
                if (first.equals("playercount")) {
                    return new TARDISPlayerCountCommand(plugin).countPlayers(sender, args);
                }
                if (first.equals("prune")) {
                    return new TARDISPruneCommand(plugin).startPruning(sender, args);
                }
                if (first.equals("prunelist")) {
                    return new TARDISPruneCommand(plugin).listPrunes(sender, args);
                }
                if (first.equals("purge")) {
                    return new TARDISPurgeCommand(plugin).clearAll(sender, args);
                }
                if (first.equals("recharger")) {
                    return new TARDISRechargerCommand(plugin).setRecharger(sender, args);
                }
                if (first.equals("decharge")) {
                    return new TARDISDechargeCommand(plugin).removeChargerStatus(sender, args);
                }
                if (first.equals("disguise")) {
                    return new TARDISDisguiseCommand(plugin).disguise(sender, args);
                }
                if (first.equals("enter")) {
                    return new TARDISEnterCommand(plugin).enterTARDIS(sender, args);
                }
                if (first.equals("delete")) {
                    return new TARDISDeleteCommand(plugin).deleteTARDIS(sender, args);
                }
                if (first.equals("repair")) {
                    return new TARDISRepairCommand(plugin).setFreeCount(sender, args);
                }
                if (first.equals("revoke")) {
                    return new TARDISRevokeCommand(plugin).removePermission(sender, args);
                }
                return true;
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_ADMIN");
                return false;
            }
        }
        return false;
    }
}
