/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command /tardis [arguments].
 *
 * A TARDIS console room or control room is the area which houses the TARDIS'
 * control console, by which the TARDIS was operated.
 *
 * @author eccentric_nz
 */
public class TARDISCommands implements CommandExecutor {

    private final TARDIS plugin;
    private TARDISPluginRespect respect;
    public HashSet<Byte> transparent = new HashSet<Byte>();
    private final List<String> firstArgs = new ArrayList<String>();
    public List<String> roomArgs = new ArrayList<String>();

    public TARDISCommands(TARDIS plugin) {
        this.plugin = plugin;
        // add transparent blocks
        transparent.add((byte) 0); // AIR
        transparent.add((byte) 8); // WATER
        transparent.add((byte) 9); // STATIONARY_WATER
        transparent.add((byte) 31); // LONG_GRASS
        transparent.add((byte) 32); // DEAD_BUSH
        transparent.add((byte) 55); // REDSTONE_WIRE
        transparent.add((byte) 78); // SNOW
        transparent.add((byte) 101); // IRON_FENCE
        transparent.add((byte) 106); // VINE
        // add first arguments
        firstArgs.add("abort");
        firstArgs.add("add");
        firstArgs.add("arsremove");
        firstArgs.add("chameleon");
        firstArgs.add("check_loc");
        firstArgs.add("comehere");
        firstArgs.add("direction");
        firstArgs.add("ep1");
        firstArgs.add("exterminate");
        firstArgs.add("find");
        firstArgs.add("gravity");
        firstArgs.add("help");
        firstArgs.add("hide");
        firstArgs.add("home");
        firstArgs.add("inside");
        firstArgs.add("jettison");
        firstArgs.add("lamps");
        firstArgs.add("list");
        firstArgs.add("namekey");
        firstArgs.add("occupy");
        firstArgs.add("rebuild");
        firstArgs.add("remove");
        firstArgs.add("removesave");
        firstArgs.add("rescue");
        firstArgs.add("room");
        firstArgs.add("save");
        firstArgs.add("secondary");
        firstArgs.add("setdest");
        firstArgs.add("tagtheood");
        firstArgs.add("update");
        firstArgs.add("version");
        // rooms - only add if enabled in the config
        for (String r : plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false)) {
            if (plugin.getRoomsConfig().getBoolean("rooms." + r + ".enabled")) {
                roomArgs.add(r);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        // If the player typed /tardis then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardis")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (args.length == 0) {
                sender.sendMessage(TARDISConstants.COMMANDS.split("\n"));
                return true;
            }
            // the command list - first argument MUST appear here!
            if (!firstArgs.contains(args[0].toLowerCase(Locale.ENGLISH))) {
                sender.sendMessage(plugin.pluginName + "That command wasn't recognised type " + ChatColor.GREEN + "/tardis help" + ChatColor.RESET + " to see the commands");
                return false;
            }
            // delete the TARDIS
            if (args[0].equalsIgnoreCase("exterminate")) {
                return new TARDISExterminateCommand(plugin).doExterminate(sender, player);
            }
            // temporary command to convert old gravity well to new style
            if (args[0].equalsIgnoreCase("gravity")) {
                return new TARDISGravityConverterCommand(plugin).convertGravity(sender, player, args);
            }
            if (args[0].equalsIgnoreCase("version")) {
                return new TARDISVersionCommand(plugin).displayVersion(sender, player);
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                return false;
            } else {
                if (args[0].equalsIgnoreCase("arsremove")) {
                    return new TARDISARSRemoveCommand(plugin).resetARS(player);
                }
                if (args[0].equalsIgnoreCase("lamps")) {
                    return new TARDISLampsCommand(plugin).addLampBlocks(player);
                }
                if (args[0].equalsIgnoreCase("chameleon")) {
                    new TARDISChameleonCommand(plugin).doChameleon(player, args);
                }
                if (args[0].equalsIgnoreCase("rescue")) {
                    return new TARDISRescueCommand(plugin).startRescue(player, args);
                }
                if (args[0].equalsIgnoreCase("room")) {
                    return new TARDISRoomCommand(plugin).startRoom(player, args);
                }
                if (args[0].equalsIgnoreCase("jettison")) {
                    return new TARDISJettisonCommand(plugin).startJettison(player, args);
                }
                if (args[0].equalsIgnoreCase("abort")) {
                    return new TARDISAbortCommand(plugin).doAbort(player, args);
                }
                if (args[0].equalsIgnoreCase("occupy")) {
                    return new TARDISOccupyCommand(plugin).toggleOccupancy(player);
                }
                if (args[0].equalsIgnoreCase("comehere")) {
                    return new TARDISComehereCommand(plugin).doComeHere(player);
                }
                if (args[0].equalsIgnoreCase("check_loc")) {
                    return new TARDISCheckLocCommand(plugin).doACheckLocation(player, args);
                }
                if (args[0].equalsIgnoreCase("inside")) {
                    return new TARDISInsideCommand(plugin).whosInside(player, args);
                }
                if (args[0].equalsIgnoreCase("home")) {
                    return new TARDISHomeCommand(plugin).setHome(player, args);
                }
                if (args[0].equalsIgnoreCase("update")) {
                    return new TARDISUpdateCommand(plugin).startUpdate(player, args);
                }
                if (args[0].equalsIgnoreCase("secondary")) {
                    return new TARDISSecondaryCommand(plugin).startSecondary(player, args);
                }
                if (args[0].equalsIgnoreCase("rebuild")) {
                    return new TARDISRebuildCommand(plugin).rebuildPreset(player, args);
                }
                if (args[0].equalsIgnoreCase("hide")) {
                    return new TARDISHideCommand(plugin).hide(player, args);
                }
                if (args[0].equalsIgnoreCase("list")) {
                    return new TARDISListCommand(plugin).doList(player, args);
                }
                if (args[0].equalsIgnoreCase("find")) {
                    return new TARDISFindCommand(plugin).findTARDIS(player, args);
                }
                if (args[0].equalsIgnoreCase("add")) {
                    return new TARDISAddCompanionCommand(plugin).doAdd(player, args);
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    return new TARDISRemoveCompanionCommand(plugin).doRemoveCompanion(player, args);
                }
                if (args[0].equalsIgnoreCase("save")) {
                    return new TARDISSaveLocationCommand(plugin).doSave(player, args);
                }
                if (args[0].equalsIgnoreCase("removesave")) {
                    return new TARDISRemoveSavedLocationCommand(plugin).doRemoveSave(player, args);
                }
                if (args[0].equalsIgnoreCase("setdest")) {
                    return new TARDISSetDestinationCommand(plugin).doSetDestination(player, args);
                }
                if (args[0].equalsIgnoreCase("direction")) {
                    return new TARDISDirectionCommand(plugin).changeDirection(player, args);
                }
                if (args[0].equalsIgnoreCase("namekey")) {
                    return new TARDISNameKeyCommand(plugin).nameKey(player, args);
                }
                if (args[0].equalsIgnoreCase("help")) {
                    return new TARDISHelpCommand(plugin).showHelp(player, args);
                }
                if (args[0].equalsIgnoreCase("tagtheood")) {
                    return new TARDISTagCommand(plugin).getStats(player);
                }
                if (args[0].equalsIgnoreCase("ep1")) {
                    return new TARDISEmergencyProgrammeCommand(plugin).showEP1(player);
                }
            }
        }
        // If the above has happened the function will break and return true. If this hasn't happened then value of false will be returned.
        return false;
    }
}
