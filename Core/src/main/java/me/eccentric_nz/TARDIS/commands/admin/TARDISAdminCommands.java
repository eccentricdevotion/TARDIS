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
import me.eccentric_nz.TARDIS.arch.TARDISArchCommand;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.dev.TARDISAddRegionsCommand;
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

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

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
        firstsStr.add("add_regions");
        firstsStr.add("arch");
        firstsStr.add("assemble");
        firstsStr.add("clean");
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
        firstsStr.add("remove_protection");
        firstsStr.add("repair");
        firstsStr.add("revoke");
        firstsStr.add("set_size");
        firstsStr.add("spawn_abandoned");
        firstsStr.add("undisguise");
        firstsStr.add("update_plugins");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardisadmin then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisadmin")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length == 0) {
                    new TARDISCommandHelper(plugin).getCommand("tardisadmin", sender);
                    return true;
                }
                String first = args[0].toLowerCase(Locale.ROOT);
                if (!firstsStr.contains(first)) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_NOT_VALID");
                    return false;
                }
                if (args.length == 1) {
                    switch (first) {
                        case "add_regions" -> {
                            return new TARDISAddRegionsCommand(plugin).doCheck(sender);
                        }
                        case "clean" -> {
                            return new TARDISCleanEntitiesCommand(plugin).checkAndRemove(sender);
                        }
                        case "condenser" -> {
                            return new TARDISCondenserCommand(plugin).set(sender);
                        }
                        case "convert_database" -> {
                            try {
                                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Converter(plugin, sender));
                                return true;
                            } catch (Exception e) {
                                plugin.getMessenger().message(sender, "Database conversion failed! " + e.getMessage());
                                return true;
                            }
                        }
                        case "update_plugins" -> {
                            if (!sender.isOp()) {
                                plugin.getMessenger().message(sender, "You must be a server operator to run this command!");
                                return true;
                            }
                            return new UpdateTARDISPlugins(plugin).fetchFromJenkins(sender);
                        }
                        case "maze" -> {
                            if (sender instanceof Player p) {
                                Location l = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
                                TARDISMazeGenerator generator = new TARDISMazeGenerator();
                                generator.makeMaze();
                                TARDISMazeBuilder builder = new TARDISMazeBuilder(generator.getMaze(), l);
                                builder.build(false);
                            }
                            return true;
                        }
                        case "mvimport" -> {
                            if (!plugin.getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_ENABLED");
                            }
                            plugin.getMVHelper().importWorlds(sender);
                            return true;
                        }
                    }
                }
                switch (first) {
                    case "create" -> {
                        return new TARDISCreateCommand(plugin).buildTARDIS(sender, args);
                    }
                    case "find" -> {
                        return new TARDISFindHiddenCommand().search(plugin, sender, args);
                    }
                    case "remove_protection" -> {
                        return new TARDISRemoveProtectionCommand(plugin).remove(args);
                    }
                    case "list" -> {
                        return new TARDISListCommand(plugin).listStuff(sender, args);
                    }
                    case "purge_portals" -> {
                        return new TARDISPortalCommand(plugin).clearAll(sender);
                    }
                    case "undisguise" -> {
                        return new TARDISDisguiseCommand(plugin).disguise(sender, args);
                    }
                }
                if (args.length < 2) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                    return false;
                }
                switch (first) {
                    case "arch" -> {
                        if (args.length > 2) {
                            return new TARDISArchCommand(plugin).force(sender, args);
                        } else {
                            return new TARDISArchCommand(plugin).whois(sender, args);
                        }
                    }
                    case "assemble", "dispersed" -> {
                        return new TARDISDispersedCommand(plugin).assemble(sender, args[1]);
                    }
                    case "set_size" -> {
                        return new TARDISSetSizeCommand(plugin).overwrite(sender, args);
                    }
                    case "spawn_abandoned" -> {
                        return new TARDISAbandonedCommand(plugin).spawn(sender, args);
                    }
                    case "make_preset" -> {
                        return new TARDISMakePresetCommand(plugin).scanBlocks(sender, args);
                    }
                    case "playercount" -> {
                        return new TARDISPlayerCountCommand(plugin).countPlayers(sender, args);
                    }
                    case "prune" -> {
                        return new TARDISPruneCommand(plugin).startPruning(sender, args);
                    }
                    case "prunelist" -> {
                        return new TARDISPruneCommand(plugin).listPrunes(sender, args);
                    }
                    case "purge" -> {
                        return new TARDISPurgeCommand(plugin).clearAll(sender, args);
                    }
                    case "recharger" -> {
                        return new TARDISRechargerCommand(plugin).setRecharger(sender, args);
                    }
                    case "decharge" -> {
                        return new TARDISDechargeCommand(plugin).removeChargerStatus(sender, args);
                    }
                    case "disguise" -> {
                        return new TARDISDisguiseCommand(plugin).disguise(sender, args);
                    }
                    case "enter" -> {
                        return new TARDISEnterCommand(plugin).enterTARDIS(sender, args);
                    }
                    case "delete" -> {
                        return new TARDISDeleteCommand(plugin).deleteTARDIS(sender, args);
                    }
                    case "repair" -> {
                        return new TARDISRepairCommand(plugin).setFreeCount(sender, args);
                    }
                    case "revoke" -> {
                        return new TARDISRevokeCommand(plugin).removePermission(sender, args);
                    }
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
