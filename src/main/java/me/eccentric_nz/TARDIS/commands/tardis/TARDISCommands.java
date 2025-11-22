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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISDiskWriterCommand;
import me.eccentric_nz.TARDIS.arch.TARDISArchCommand;
import me.eccentric_nz.TARDIS.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.utils.TARDISAcceptor;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisCommand;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.ComehereAction;
import me.eccentric_nz.TARDIS.travel.ComehereRequest;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.UUID;

/**
 * Command /tardis [arguments].
 * <p>
 * A TARDIS console room or control room is the area which houses the TARDIS' control console, by which the TARDIS was
 * operated.
 *
 * @author eccentric_nz
 */
public class TARDISCommands implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
            TardisCommand tc;
            String first = args[0].toLowerCase(Locale.ROOT);
            try {
                tc = TardisCommand.valueOf(first);
            } catch (IllegalArgumentException e) {
                // is it a tpa or call request?
                if (first.equals("call")) {
                    UUID chatter = player.getUniqueId();
                    if (plugin.getTrackerKeeper().getComehereRequests().containsKey(chatter)) {
                        ComehereRequest request = plugin.getTrackerKeeper().getComehereRequests().get(chatter);
                        new ComehereAction(plugin).doTravel(request);
                        plugin.getTrackerKeeper().getComehereRequests().remove(chatter);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "REQUEST_TIMEOUT");
                    }
                } else if (first.equals("request")) {
                    new TARDISAcceptor(plugin).doRequest(player, true);
                } else {
                    plugin.getMessenger().message(sender, TardisModule.TARDIS, "That command wasn't recognised type '/tardis help' to see the commands");
                    return false;
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("version")) {
                return new TARDISVersionCommand(plugin).displayVersion(sender);
            }
            if (args[0].equalsIgnoreCase("help")) {
                return new TARDISHelpCommand(plugin).showHelp(sender, args);
            }
            if (player == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                return false;
            }
            if (args[0].equalsIgnoreCase("stop_sound")) {
                return new TARDISStopSoundCommand(plugin).mute(player);
            }
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return true;
            }
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(rs.getTardisId()) && tc.noSiege()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CMD");
                return true;
            }
            switch (tc) {
                case abandon -> {
                    return new TARDISAbandonCommand(plugin).doAbandon(sender, args.length > 1);
                }
                case add -> {
                    if (args.length == 1) {
                        return new TARDISAddCompanionCommand(plugin).doAddGUI(player);
                    } else {
                        return new TARDISAddCompanionCommand(plugin).doAdd(player, args);
                    }
                }
                case arch_time -> {
                    return new TARDISArchCommand(plugin).getTime(player);
                }
                case archive -> {
                    return new TARDISArchiveCommand(plugin).zip(player, args);
                }
                case arsremove -> {
                    return new TARDISARSRemoveCommand(plugin).resetARS(player);
                }
                case bell -> {
                    return new TARDISBellCommand(plugin).toggle(rs.getTardisId(), player, args);
                }
                case check_loc -> {
                    return new TARDISCheckLocCommand(plugin).doACheckLocation(player);
                }
                case colourise, colorize -> {
                    return new TARDISColouriseCommand(plugin).updateBeaconGlass(player);
                }
                case comehere -> {
                    return new TARDISComehereCommand(plugin).doComeHere(player);
                }
                case construct -> {
                    return new TARDISConstructCommand(plugin).setLine(player, args);
                }
                case cube -> {
                    return new TARDISCubeCommand(plugin).whoHasCube(player);
                }
                case desktop, upgrade, theme -> {
                    return new TARDISUpgradeCommand(plugin).openUpgradeGUI(player);
                }
                case direction -> {
                    return new TARDISDirectionCommand(plugin).changeDirection(player, args);
                }
                case door -> {
                    return new TARDISDoorCommand(plugin).toggleDoors(player, args);
                }
                case egg -> { // play tardis theme from resource pack
                    return new TARDISPlayThemeCommand(plugin).playTheme(player, args);
                }
                case eject -> {
                    return new TARDISEjectCommand(plugin).eject(player);
                }
                case excite -> {
                    return new TARDISExciteCommand(plugin).excite(player);
                }
                case ep1 -> {
                    return new TARDISEmergencyProgrammeCommand(plugin).showEP1(player);
                }
                case erase -> {
                    return new TARDISDiskWriterCommand(plugin).eraseDisk(player);
                }
                case find -> {
                    return new TARDISFindCommand(plugin).findTARDIS(player);
                }
                case handbrake -> {
                    return new TARDISHandbrakeCommand(plugin).toggle(player, rs.getTardisId(), args, false);
                }
                case hide -> {
                    return new TARDISHideCommand(plugin).hide(player);
                }
                case sethome -> {
                    return new TARDISSetHomeCommand(plugin).setHome(player, args);
                }
                case inside -> {
                    return new TARDISInsideCommand(plugin).whosInside(player);
                }
                case item -> {
                    return new TARDISItemCommand(plugin).update(player, args);
                }
                case jettison -> {
                    return new TARDISJettisonCommand(plugin).startJettison(player, args);
                }
                case lamps -> {
                    return new TARDISLampsCommand(plugin).zip(player, args);
                }
                case list -> {
                    return new TARDISListCommand(plugin).doList(player, args);
                }
                case make_her_blue -> {
                    return new TARDISMakeHerBlueCommand(plugin).show(player);
                }
                case monsters -> {
                    return new TARDISMonstersCommand(plugin).reset(player, rs.getTardisId(), args);
                }
                case namekey -> {
                    return new TARDISNameKeyCommand(plugin).nameKey(player, args);
                }
                case occupy -> {
                    return new TARDISOccupyCommand(plugin).toggleOccupancy(player, args);
                }
                case decommission -> {
                    return new TARDISDecommissionCommand(plugin).withdraw(player, args);
                }
                case rebuild -> {
                    return new TARDISRebuildCommand(plugin).rebuildPreset(player);
                }
                case remove -> {
                    return new TARDISRemoveCompanionCommand(plugin).doRemoveCompanion(player, args);
                }
                case removesave -> {
                    return new TARDISRemoveSavedLocationCommand(plugin).doRemoveSave(player, args);
                }
                case renamesave -> {
                    return new TARDISRenameSavedLocationCommand(plugin).doRenameSave(player, args);
                }
                case reordersave -> {
                    return new TARDISReorderSavedLocationCommand(plugin).doReorderSave(player, args);
                }
                case rescue -> {
                    return new TARDISRescueCommand(plugin).startRescue(player, args);
                }
                case room -> {
                    return new TARDISRoomCommand(plugin).startRoom(player, args);
                }
                case save_player -> {
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (heldDiskIsWrong(is, "Player Storage Disk")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_HAND_PLAYER");
                        return true;
                    }
                    return new TARDISDiskWriterCommand(plugin).writePlayer(player, args);
                }
                case secondary -> {
                    return new TARDISSecondaryCommand(plugin).startSecondary(player, args);
                }
                case section -> {
                    return new TARDISUpdateChatGUI(plugin).showInterface(player, args);
                }
                case setdest -> {
                    return new TARDISSetDestinationCommand(plugin).doSetDestination(player, args);
                }
                case tagtheood -> {
                    return new TARDISTagCommand(plugin).getStats(player);
                }
                case transmat -> {
                    return new TARDISTransmatCommand(plugin).teleportOrProcess(player, args);
                }
                case update -> {
                    return new TARDISUpdateCommand(plugin).startUpdate(player, args);
                }
                case abort -> {
                    return new TARDISAbortCommand(plugin).doAbort(player, args, rs.getTardisId());
                }
                case exterminate -> { // delete the TARDIS
                    boolean messagePlayer = args.length != 2 || !args[1].equals("6z@3=V!Q7*/O_OB^");
                    return new TARDISExterminateCommand(plugin).doExterminate(player, messagePlayer);
                }
                case save -> {
                    if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.SAVES)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Saves");
                        return true;
                    }
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (itemStack.getType().equals(Material.MUSIC_DISC_FAR)) {
                        return new TARDISDiskWriterCommand(plugin).writeSaveToControlDisk(player, args);
                    } else {
                        if (plugin.getConfig().getBoolean("difficulty.disks") && !plugin.getUtils().inGracePeriod(player, true)) {
                            if (plugin.getConfig().getBoolean("difficulty.disk_in_hand_for_write") && heldDiskIsWrong(itemStack, "Save Storage Disk")) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_HAND_SAVE");
                                return true;
                            }
                            return new TARDISDiskWriterCommand(plugin).writeSave(player, args);
                        } else {
                            return new TARDISSaveLocationCommand(plugin).doSave(player, args);
                        }
                    }
                }
                case saveicon -> {
                    return new TARDISSaveIconCommand(plugin).changeIcon(player, args);
                }
                default -> {
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
        } else if (!ComponentUtils.endsWith(is.getItemMeta().displayName(), dn)) {
            complexBool = true;
        }
        return complexBool;
    }
}
