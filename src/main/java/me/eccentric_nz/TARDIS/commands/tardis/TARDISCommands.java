/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISDiskWriterCommand;
import me.eccentric_nz.TARDIS.arch.TARDISArchCommand;
import me.eccentric_nz.TARDIS.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.TARDIS_COMMAND;
import me.eccentric_nz.TARDIS.noteblock.TARDISPlayThemeCommand;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public TARDISCommands(TARDIS plugin) {
        this.plugin = plugin;
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
                new TARDISCommandHelper(plugin).getCommand("", sender);
                return true;
            }
            // the command list - first argument MUST appear here!
            TARDIS_COMMAND tc;
            try {
                tc = TARDIS_COMMAND.valueOf(args[0].toLowerCase(Locale.ENGLISH));
            } catch (IllegalArgumentException e) {
                sender.sendMessage(plugin.getPluginName() + "That command wasn't recognised type " + ChatColor.GREEN + "/tardis help" + ChatColor.RESET + " to see the commands");
                return false;
            }
            if (args[0].equalsIgnoreCase("version")) {
                return new TARDISVersionCommand(plugin).displayVersion(sender);
            }
            if (args[0].equalsIgnoreCase("help")) {
                return new TARDISHelpCommand(plugin).showHelp(sender, args);
            }
            if (player == null) {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return false;
            } else {
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (!rs.fromUUID(player.getUniqueId().toString())) {
                    TARDISMessage.send(player, "NOT_A_TIMELORD");
                    return true;
                }
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(rs.getTardis_id()) && tc.noSiege()) {
                    TARDISMessage.send(player, "SIEGE_NO_CMD");
                    return true;
                }
                if (args[0].equalsIgnoreCase("abandon")) {
                    return new TARDISAbandonCommand(plugin).doAbandon(sender, args.length > 1);
                }
                if (args[0].equalsIgnoreCase("add")) {
                    if (args.length == 1) {
                        return new TARDISAddCompanionCommand(plugin).doAddGUI(player);
                    } else {
                        return new TARDISAddCompanionCommand(plugin).doAdd(player, args);
                    }
                }
                if (args[0].equalsIgnoreCase("arch_time")) {
                    return new TARDISArchCommand(plugin).getTime(player);
                }
                if (args[0].equalsIgnoreCase("arsremove")) {
                    return new TARDISARSRemoveCommand(plugin).resetARS(player);
                }
                if (args[0].equalsIgnoreCase("check_loc")) {
                    return new TARDISCheckLocCommand(plugin).doACheckLocation(player, args);
                }
                if (args[0].equalsIgnoreCase("colourise") || args[0].equalsIgnoreCase("colorize")) {
                    new TARDISColouriseCommand(plugin).updateBeaconGlass(player);
                }
                if (args[0].equalsIgnoreCase("comehere")) {
                    return new TARDISComehereCommand(plugin).doComeHere(player);
                }
                if (args[0].equalsIgnoreCase("cube")) {
                    return new TARDISCubeCommand(plugin).whoHasCube(player);
                }
                if (args[0].equalsIgnoreCase("desktop") || args[0].equalsIgnoreCase("upgrade") || args[0].equalsIgnoreCase("theme")) {
                    return new TARDISUpgradeCommand(plugin).openUpgradeGUI(player);
                }
                if (args[0].equalsIgnoreCase("direction")) {
                    return new TARDISDirectionCommand(plugin).changeDirection(player, args);
                }
                // play tardis theme on noteblocks
                if (args[0].equalsIgnoreCase("egg")) {
                    return new TARDISPlayThemeCommand(plugin).playTheme(player);
                }
                if (args[0].equalsIgnoreCase("eject")) {
                    return new TARDISEjectCommand(plugin).eject(player);
                }
                if (args[0].equalsIgnoreCase("ep1")) {
                    return new TARDISEmergencyProgrammeCommand(plugin).showEP1(player);
                }
                if (args[0].equalsIgnoreCase("erase")) {
                    return new TARDISDiskWriterCommand(plugin).eraseDisk(player);
                }
                if (args[0].equalsIgnoreCase("find")) {
                    return new TARDISFindCommand(plugin).findTARDIS(player, args);
                }
                if (args[0].equalsIgnoreCase("hide")) {
                    return new TARDISHideCommand(plugin).hide(player);
                }
                if (args[0].equalsIgnoreCase("home")) {
                    return new TARDISHomeCommand(plugin).setHome(player, args);
                }
                if (args[0].equalsIgnoreCase("inside")) {
                    return new TARDISInsideCommand(plugin).whosInside(player, args);
                }
                if (args[0].equalsIgnoreCase("jettison")) {
                    return new TARDISJettisonCommand(plugin).startJettison(player, args);
                }
                if (args[0].equalsIgnoreCase("lamps")) {
                    return new TARDISLampsCommand(plugin).addLampBlocks(player);
                }
                if (args[0].equalsIgnoreCase("list")) {
                    return new TARDISListCommand(plugin).doList(player, args);
                }
                if (args[0].equalsIgnoreCase("make_her_blue")) {
                    return new TARDISMakeHerBlueCommand(plugin).show(player);
                }
                if (args[0].equalsIgnoreCase("namekey")) {
                    return new TARDISNameKeyCommand(plugin).nameKey(player, args);
                }
                if (args[0].equalsIgnoreCase("occupy")) {
                    return new TARDISOccupyCommand(plugin).toggleOccupancy(player);
                }
                if (args[0].equalsIgnoreCase("rebuild")) {
                    return new TARDISRebuildCommand(plugin).rebuildPreset(player);
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    return new TARDISRemoveCompanionCommand(plugin).doRemoveCompanion(player, args);
                }
                if (args[0].equalsIgnoreCase("removesave")) {
                    return new TARDISRemoveSavedLocationCommand(plugin).doRemoveSave(player, args);
                }
                if (args[0].equalsIgnoreCase("rescue")) {
                    return new TARDISRescueCommand(plugin).startRescue(player, args);
                }
                if (args[0].equalsIgnoreCase("room")) {
                    return new TARDISRoomCommand(plugin).startRoom(player, args);
                }
                if (args[0].equalsIgnoreCase("save_player")) {
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (heldDiskIsWrong(is, "Player Storage Disk")) {
                        TARDISMessage.send(player, "DISK_HAND_PLAYER");
                        return true;
                    }
                    return new TARDISDiskWriterCommand(plugin).writePlayer(player, args);
                }
                if (args[0].equalsIgnoreCase("secondary")) {
                    return new TARDISSecondaryCommand(plugin).startSecondary(player, args);
                }
                if (args[0].equalsIgnoreCase("section")) {
                    return new TARDISUpdateChatGUI(plugin).showInterface(player, args);
                }
                if (args[0].equalsIgnoreCase("setdest")) {
                    return new TARDISSetDestinationCommand(plugin).doSetDestination(player, args);
                }
                if (args[0].equalsIgnoreCase("tagtheood")) {
                    return new TARDISTagCommand(plugin).getStats(player);
                }
                if (args[0].equalsIgnoreCase("update")) {
                    return new TARDISUpdateCommand(plugin).startUpdate(player, args);
                }
                if (args[0].equalsIgnoreCase("abort")) {
                    return new TARDISAbortCommand(plugin).doAbort(player, args);
                }
                // delete the TARDIS
                if (args[0].equalsIgnoreCase("exterminate")) {
                    return new TARDISExterminateCommand(plugin).doExterminate(player);
                }
                if (args[0].equalsIgnoreCase("save")) {
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
                        if (plugin.getDifficulty().equals(DIFFICULTY.HARD) && heldDiskIsWrong(is, "Save Storage Disk")) {
                            TARDISMessage.send(player, "DISK_HAND_SAVE");
                            return true;
                        }
                        return new TARDISDiskWriterCommand(plugin).writeSave(player, args);
                    } else {
                        return new TARDISSaveLocationCommand(plugin).doSave(player, args);
                    }
                }
            }
        }
        // If the above has happened the function will break and return true. If this hasn't happened then value of false will be returned.
        return false;
    }

    private boolean heldDiskIsWrong(ItemStack is, String dn) {
        boolean complexBool = false;
        if (is == null) {
            complexBool = true;
        } else if (!is.hasItemMeta()) {
            complexBool = true;
        } else if (!is.getItemMeta().hasDisplayName()) {
            complexBool = true;
        } else if (!is.getItemMeta().getDisplayName().equals(dn)) {
            complexBool = true;
        }
        return complexBool;
    }
}
