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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.arch.TARDISArchCommand;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.dev.AddRegionsCommand;
import me.eccentric_nz.TARDIS.database.tool.Converter;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.maze.MazeBuilder;
import me.eccentric_nz.TARDIS.maze.MazeGenerator;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.update.UpdateTARDISPlugins;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
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
        firstsStr.add("remove_protection");
        firstsStr.add("repair");
        firstsStr.add("revoke");
        firstsStr.add("set_size");
        firstsStr.add("spawn_abandoned");
        firstsStr.add("undisguise");
        firstsStr.add("update_plugin");
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
                            return new AddRegionsCommand(plugin).doCheck(sender);
                        }
                        case "clean" -> {
                            return new CleanEntitiesCommand(plugin).checkAndRemove(sender);
                        }
                        case "condenser" -> {
                            return new CondenserCommand(plugin).set(sender);
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
                        case "maze" -> {
                            if (sender instanceof Player p) {
                                Location l = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
                                MazeGenerator generator = new MazeGenerator();
                                generator.makeMaze();
                                MazeBuilder builder = new MazeBuilder(generator.getMaze(), l);
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
                        case "update_plugin" -> {
                            if (!sender.isOp()) {
                                plugin.getMessenger().message(sender, "You must be a server operator to run this command!");
                                return true;
                            }
                            return new UpdateTARDISPlugins(plugin).fetchFromGitHub(sender);
                        }
                    }
                }
                switch (first) {
                    case "create" -> {
                        if (args.length < 3) {
                            return false;
                        }
                        // args[1] player
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player == null) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                            return true;
                        }
                        // args[2] schematic
                        String seed = args[2].toUpperCase(Locale.ROOT);
                        // args[3] wall
                        String wall = "ORANGE_WOOL";
                        if (args.length > 3) {
                            wall = args[3].toUpperCase(Locale.ROOT);
                        }
                        // args[4] floor
                        String floor = "LIGHT_GRAY_WOOL";
                        if (args.length > 4) {
                            floor = args[4].toUpperCase(Locale.ROOT);
                        }
                        return new CreateTARDISCommand(plugin, wall, floor).buildTARDIS(sender, player, seed);
                    }
                    case "find" -> {
                        int radius = 16;
                        if (args.length > 1) {
                            int parsed = TARDISNumberParsers.parseInt(args[1]);
                            if (parsed > 0) {
                                radius = parsed;
                            }
                        }
                        return new FindHiddenCommand().search(plugin, sender, radius);
                    }
                    case "remove_protection" -> {
                        if (args.length < 3) {
                            return true;
                        }
                        int id = TARDISNumberParsers.parseInt(args[1]);
                        return new RemoveProtectionCommand(plugin).remove(id, args[2]);
                    }
                    case "list" -> {
                        return new ListCommand(plugin).listStuff(sender, "tardises");
                    }
                    case "purge_portals" -> {
                        return new PortalCommand(plugin).clearAll(sender);
                    }
                    case "undisguise" -> {
                        return new UndisguiseCommand(plugin).undisguise((Player) sender);
                    }
                }
                if (args.length < 2) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                    return false;
                }
                switch (first) {
                    case "arch" -> {
                        if (args.length > 2) {
                            Player player = plugin.getServer().getPlayer(args[1]);
                            if (player == null) {
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                                return true;
                            }
                            return new TARDISArchCommand(plugin).force(sender, player);
                        } else {
                            return new TARDISArchCommand(plugin).whois(sender, args[1]);
                        }
                    }
                    case "assemble", "dispersed" -> {
                        return new DispersedCommand(plugin).assemble(sender, args[1]);
                    }
                    case "set_size" -> {
                        // get the player
                        Player p = plugin.getServer().getPlayer(args[1]);
                        if (p == null) { // player must be online
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                            return true;
                        }
                        String type = args[2].toUpperCase(Locale.ROOT);
                        return new SetSizeCommand(plugin).overwrite(sender, p, type);
                    }
                    case "spawn_abandoned" -> {
                        // tardisadmin spawn_abandoned Schematic PRESET COMPASS world x y z
                        if (args.length < 4) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_ARGS");
                            return true;
                        }
                        Location l;
                        if (sender instanceof Player p) {
                            l = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getRelative(BlockFace.UP).getLocation();
                        } else {
                            if (args.length < 8) {
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ABANDONED_ARGS");
                                return true;
                            }
                            World w = TARDISAliasResolver.getWorldFromAlias(args[4]);
                            if (w == null) {
                                plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld", plugin);
                                return true;
                            }
                            int x = TARDISNumberParsers.parseInt(args[5]);
                            int y = TARDISNumberParsers.parseInt(args[6]);
                            int z = TARDISNumberParsers.parseInt(args[7]);
                            if (x == 0 || y == 0 || z == 0) {
                                plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld", plugin);
                                return true;
                            }
                            l = new Location(w, x, y, z);
                        }
                        return new CreateAbandonedCommand(plugin).spawn(sender, args[1], args[2], args[3], l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
                    }
                    case "make_preset" -> {
                        return new MakePresetCommand(plugin).scanBlocks(sender, args[1]);
                    }
                    case "playercount" -> {
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player == null) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_VALID");
                            return true;
                        }
                        return new PlayerCountCommand(plugin).countPlayers(sender, player, args.length == 3 ? TARDISNumberParsers.parseInt(args[2]) : -1);
                    }
                    case "prune" -> {
                        return new PruneCommand(plugin).startPruning(sender, TARDISNumberParsers.parseInt(args[1]));
                    }
                    case "prunelist" -> {
                        return new PruneCommand(plugin).listPrunes(sender, TARDISNumberParsers.parseInt(args[1]));
                    }
                    case "purge" -> {
                        // Look up this player's UUID
                        String uuid = "";
                        if (args[1].toLowerCase(Locale.ROOT).equals("junk")) {
                            uuid = "00000000-aaaa-bbbb-cccc-000000000000";
                        } else {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            if (offlinePlayer.getName() != null) {
                                uuid = offlinePlayer.getUniqueId().toString();
                            }
                        }
                        return new PurgeCommand(plugin).clearAll(sender, args[1], uuid);
                    }
                    case "recharger" -> {
                        return new RechargerCommand(plugin).setRecharger(sender, args[1]);
                    }
                    case "decharge" -> {
                        return new DechargeCommand(plugin).removeChargerStatus(sender, args[1]);
                    }
                    case "disguise" -> {
                        return new DisguiseCommand(plugin).disguise(sender, (Player) sender, "HUSK");
                    }
                    case "enter" -> {
                        int tmp = -1;
                        try {
                            tmp = Integer.parseInt(args[1]);
                            new EnterCommand(plugin).enterTARDIS(sender, tmp);
                        } catch (NumberFormatException nfe) {
                            // do nothing
                        }
                        if (tmp == -1) {
                            // Look up this player's UUID
                            OfflinePlayer olp = plugin.getServer().getOfflinePlayer(args[1]);
                            new EnterCommand(plugin).enterTARDIS(sender, olp.getPlayer());
                        }
                        return true;
                    }
                    case "delete" -> {
                        boolean junk = (args[1].toLowerCase(Locale.ROOT).equals("junk"));
                        int tmp = -1;
                        int abandoned = (args.length > 2 && args[2].equals("abandoned")) ? 1 : 0;
                        try {
                            tmp = Integer.parseInt(args[1]);
                            return new DeleteTARDISCommand(plugin).deleteTARDIS(sender, tmp, abandoned);
                        } catch (NumberFormatException ignored) { } // do nothing
                        if (junk) {
                            return new DeleteTARDISCommand(plugin).deleteJunk(sender);
                        } else {
                            OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[1]);
                            return new DeleteTARDISCommand(plugin).deleteTARDIS(sender, player.getPlayer(), abandoned);
                        }
                    }
                    case "repair" -> {
                        if (args.length < 3) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                            return true;
                        }
                        // Look up this player's UUID
                        OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
                        if (op.getName() == null) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                            return true;
                        }
                        int r = 1;
                        if (args.length == 3) {
                            r = TARDISNumberParsers.parseInt(args[2]);
                        }
                        return new RepairCommand(plugin).setFreeCount(sender, op.getPlayer(), r);
                    }
                    case "revoke" -> {
                        if (args.length < 3) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                            return true;
                        }
                        Player player = plugin.getServer().getPlayer(args[1]);
                        if (player == null) {
                            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                            return true;
                        }
                        return new RevokeCommand(plugin).removePermission(sender, player, args[2]);
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
