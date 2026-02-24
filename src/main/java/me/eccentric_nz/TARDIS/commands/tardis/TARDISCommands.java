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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DiskWriterCommand;
import me.eccentric_nz.TARDIS.arch.TARDISArchCommand;
import me.eccentric_nz.TARDIS.chatGUI.UpdateChatGUI;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.tardis.update.UpdateCommand;
import me.eccentric_nz.TARDIS.commands.utils.RescueAcceptor;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisCommand;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.ComehereAction;
import me.eccentric_nz.TARDIS.travel.ComehereRequest;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
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
                    new RescueAcceptor(plugin).doRequest(player, true);
                } else {
                    plugin.getMessenger().message(sender, TardisModule.TARDIS, "That command wasn't recognised type '/tardis help' to see the commands");
                    return false;
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("version")) {
                return new VersionCommand(plugin).displayVersion(sender);
            }
            if (args[0].equalsIgnoreCase("help")) {
                return new HelpCommand(plugin).showHelp(sender,
                        args.length > 1 ? args[1] : "",
                        args.length > 2 ? args[2] : ""
                );
            }
            if (player == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
                return false;
            }
            if (args[0].equalsIgnoreCase("stop_sound")) {
                return new StopSoundCommand(plugin).mute(player);
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
                    return new AbandonCommand(plugin).doAbandon(sender, args.length > 1);
                }
                case add -> {
                    if (args.length == 1) {
                        return new AddCompanionCommand(plugin).doAddGUI(player);
                    } else {
                        return new AddCompanionCommand(plugin).doAdd(player, args[1]);
                    }
                }
                case arch_time -> {
                    return new TARDISArchCommand(plugin).getTime(player);
                }
                case archive -> {
                    return new ArchiveCommand(plugin).zip(player, args);
                }
                case arsremove -> {
                    return new ARSRemoveCommand(plugin).resetARS(player);
                }
                case bell -> {
                    return new BellCommand(plugin).toggle(rs.getTardisId(), player, args.length > 1 ? args[1] : "");
                }
                case check_loc -> {
                    return new CheckLocationCommand(plugin).doACheckLocation(player);
                }
                case colourise, colorize -> {
                    return new ColouriseCommand(plugin).updateBeaconGlass(player);
                }
                case comehere -> {
                    return new ComehereCommand(plugin).doComeHere(player);
                }
                case construct -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return true;
                    }
                    return new ConstructCommand(plugin).setLine(player, TARDISNumberParsers.parseInt(args[1]), args[2]);
                }
                case cube -> {
                    return new SiegeCubeCommand(plugin).whoHasCube(player);
                }
                case desktop, upgrade, theme -> {
                    return new UpgradeCommand(plugin).openUpgradeGUI(player);
                }
                case direction -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DIRECTION_NEED");
                        return true;
                    }
                    return new DirectionCommand(plugin).changeDirection(player, args[1]);
                }
                case door -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new DoorCommand(plugin).toggleDoors(player, args[1].equalsIgnoreCase("close"));
                }
                case egg -> { // play tardis theme from resource pack
                    return new ThemeMusicCommand(plugin).play(player, args.length > 1 ? args[1] : "");
                }
                case eject -> {
                    return new EjectCommand(plugin).eject(player);
                }
                case excite -> {
                    return new ExciteCommand(plugin).excite(player);
                }
                case ep1 -> {
                    return new EmergencyProgrammeOneCommand(plugin).showEP1(player);
                }
                case erase -> {
                    return new DiskWriterCommand(plugin).eraseDisk(player);
                }
                case find -> {
                    return new FindCommand(plugin).findTARDIS(player);
                }
                case handbrake -> {
                    return new HandbrakeCommand(plugin).toggle(player, rs.getTardisId(), args[1], false);
                }
                case hide -> {
                    return new HideCommand(plugin).hide(player);
                }
                case sethome -> {
                    return new SetHomeCommand(plugin).setHome(
                            player,
                            args.length > 1 ? args[1] : "",
                            args.length > 2 ? args[2] : ""
                    );
                }
                case inside -> {
                    return new InsideCommand(plugin).whosInside(player);
                }
                case item -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return true;
                    }
                    return new ItemCommand(plugin).update(player, args[1]);
                }
                case jettison -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new JettisonCommand(plugin).startJettison(player, args[1]);
                }
                case lamps -> {
                    if (args.length < 2) {
                        return false;
                    }
                    return new LampsCommand(plugin).zip(player, args);
                }
                case list -> {
                    return new ListCommand(plugin).doList(player, args[1]);
                }
                case make_her_blue -> {
                    return new MakeHerBlueCommand(plugin).show(player);
                }
                case monsters -> {
                    return new MonstersCommand(plugin).reset(player, rs.getTardisId(), args[1]);
                }
                case namekey -> {
                    int count = args.length;
                    if (count < 2) {
                        return false;
                    }
                    String newName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                    return new NameKeyCommand(plugin).nameKey(player, newName);
                }
                case occupy -> {
                    return new OccupyCommand(plugin).toggleOccupancy(player);
                }
                case decommission -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new DecommissionCommand(plugin).withdraw(player, args[1]);
                }
                case rebuild -> {
                    return new RebuildCommand(plugin).rebuildPreset(player);
                }
                case remove -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new RemoveCompanionCommand(plugin).doRemoveCompanion(player, args[1]);
                }
                case removesave -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new RemoveSavedLocationCommand(plugin).doRemoveSave(player, args[1]);
                }
                case renamesave -> {
                    if (args.length < 3) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new RenameSavedLocationCommand(plugin).doRenameSave(player, args[1], args[2]);
                }
                case reordersave -> {
                    if (args.length < 3) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new ReorderSavedLocationCommand(plugin).doReorderSave(player, args[1], TARDISNumberParsers.parseInt(args[2]));
                }
                case rescue -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("accept")) {
                        new RescueAcceptor(plugin).doRequest(player, false);
                        return true;
                    }
                    return new RescueCommand(plugin).startRescue(player, plugin.getServer().getPlayer(args[1]));
                }
                case room -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new RoomCommand(plugin).startRoom(player, args[1]);
                }
                case save_player -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (TardisUtility.heldDiskIsWrong(is, "Player Storage Disk")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_HAND_PLAYER");
                        return true;
                    }
                    return new DiskWriterCommand(plugin).writePlayer(player, plugin.getServer().getPlayer(args[1]));
                }
                case secondary -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new SecondaryCommand(plugin).startSecondary(player, args[1]);
                }
                case section -> {
                    return new UpdateChatGUI(plugin).showInterface(player, args.length > 1 ? args[1] : "");
                }
                case setdest -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new SetDestinationCommand(plugin).doSetDestination(player, args[1]);
                }
                case tagtheood -> {
                    return new TagCommand(plugin).getStats(player);
                }
                case transmat -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new TransmatCommand(plugin).teleportOrProcess(player, args);
                }
                case update -> {
                    return new UpdateCommand(plugin).startUpdate(player, args);
                }
                case abort -> {
                    return new AbortCommand(plugin).doAbort(player, args, rs.getTardisId());
                }
                case exterminate -> { // delete the TARDIS
                    boolean messagePlayer = args.length != 2 || !args[1].equals("6z@3=V!Q7*/O_OB^");
                    return new ExterminateCommand(plugin).doExterminate(player, messagePlayer);
                }
                case save -> {
                    if (args.length < 2) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.SAVES)) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Saves");
                        return true;
                    }
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (itemStack.getType().equals(Material.MUSIC_DISC_FAR)) {
                        return new DiskWriterCommand(plugin).writeSaveToControlDisk(player, args[1]);
                    } else {
                        if (plugin.getConfig().getBoolean("difficulty.disks") && !plugin.getUtils().inGracePeriod(player, true)) {
                            if (plugin.getConfig().getBoolean("difficulty.disk_in_hand_for_write") && TardisUtility.heldDiskIsWrong(itemStack, "Save Storage Disk")) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "DISK_HAND_SAVE");
                                return true;
                            }
                            return new DiskWriterCommand(plugin).writeSave(player, args[1]);
                        } else {
                            return new SaveLocationCommand(plugin).doSave(player, args[1], args.length == 2);
                        }
                    }
                }
                case saveicon -> {
                    if (args.length < 3) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                        return false;
                    }
                    return new SaveIconCommand(plugin).changeIcon(player, args[1], args[2], false);
                }
                default -> {
                }
            }
        }
        // If the above has happened the function will break and return true. If this hasn't happened then value of false will be returned.
        return false;
    }
}
