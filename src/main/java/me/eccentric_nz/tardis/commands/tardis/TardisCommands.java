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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisDiskWriterCommand;
import me.eccentric_nz.tardis.arch.TardisArchCommand;
import me.eccentric_nz.tardis.chatgui.TardisChatGuiUpdater;
import me.eccentric_nz.tardis.commands.TardisCommandHelper;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.TardisCommand;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.noteblock.TardisPlayThemeCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

/**
 * Command /tardis [arguments].
 * <p>
 * A tardis console room or control room is the area which houses the tardis' control console, by which the tardis was
 * operated.
 *
 * @author eccentric_nz
 */
public class TardisCommands implements CommandExecutor {

    private final TardisPlugin plugin;

    public TardisCommands(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player typed /tardis then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardis")) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (args.length == 0) {
                new TardisCommandHelper(plugin).getCommand("", sender);
                return true;
            }
            // the command list - first argument MUST appear here!
            TardisCommand tc;
            try {
                tc = TardisCommand.valueOf(args[0].toLowerCase(Locale.ENGLISH));
            } catch (IllegalArgumentException e) {
                sender.sendMessage(plugin.getPluginName() + "That command wasn't recognised type " + ChatColor.GREEN + "/tardis help" + ChatColor.RESET + " to see the commands");
                return false;
            }
            if (args[0].equalsIgnoreCase("version")) {
                return new TardisVersionCommand(plugin).displayVersion(sender);
            }
            if (args[0].equalsIgnoreCase("help")) {
                return new TardisHelpCommand(plugin).showHelp(sender, args);
            }
            if (player == null) {
                TardisMessage.send(sender, "CMD_PLAYER");
                return false;
            }
            ResultSetTardisId rs = new ResultSetTardisId(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TardisMessage.send(player, "NOT_A_TIMELORD");
                return true;
            }
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(rs.getTardisId()) && tc.noSiege()) {
                TardisMessage.send(player, "SIEGE_NO_CMD");
                return true;
            }
            switch (tc) {
                case abandon:
                    return new TardisAbandonCommand(plugin).doAbandon(sender, args.length > 1);
                case add:
                    if (args.length == 1) {
                        return new TardisAddCompanionCommand(plugin).doAddGUI(player);
                    } else {
                        return new TardisAddCompanionCommand(plugin).doAdd(player, args);
                    }
                case arch_time:
                    return new TardisArchCommand(plugin).getTime(player);
                case archive:
                    return new TardisArchiveCommand(plugin).zip(player, args);
                case arsremove:
                    return new TardisArsRemoveCommand(plugin).resetARS(player);
                case bell:
                    return new TardisBellCommand(plugin).toggle(rs.getTardisId(), player, args);
                case check_loc:
                    return new TardisCheckLocCommand(plugin).doACheckLocation(player);
                case colourise:
                case colorize:
                    new TardisColouriseCommand(plugin).updateBeaconGlass(player);
                case comehere:
                    return new TardisComeHereCommand(plugin).doComeHere(player);
                case construct:
                    return new TardisConstructCommand(plugin).setLine(player, args);
                case cube:
                    return new TardisCubeCommand(plugin).whoHasCube(player);
                case desktop:
                case upgrade:
                case theme:
                    return new TardisUpgradeCommand(plugin).openUpgradeGUI(player);
                case direction:
                    return new TardisDirectionCommand(plugin).changeDirection(player, args);
                case door:
                    return new TardisDoorCommand(plugin).toggleDoors(player, args);
                case egg: // play tardis theme on noteblocks
                    return new TardisPlayThemeCommand(plugin).playTheme(player);
                case eject:
                    return new TardisEjectCommand(plugin).eject(player);
                case excite:
                    return new TardisExciteCommand(plugin).excite(player);
                case ep1:
                    return new TardisEmergencyProgrammeCommand(plugin).showEP1(player);
                case erase:
                    return new TardisDiskWriterCommand(plugin).eraseDisk(player);
                case find:
                    return new TardisFindCommand(plugin).findTARDIS(player);
                case handbrake:
                    return new TardisHandbrakeCommand(plugin).toggle(player, rs.getTardisId(), args, false);
                case hide:
                    return new TardisHideCommand(plugin).hide(player);
                case home:
                case sethome:
                    return new TardisHomeCommand(plugin).setHome(player, args);
                case inside:
                    return new TardisInsideCommand(plugin).whoIsInside(player);
                case item:
                    return new TardisItemCommand().update(player, args);
                case jettison:
                    return new TardisJettisonCommand(plugin).startJettison(player, args);
                case lamps:
                    return new TardisLampsCommand(plugin).addLampBlocks(player);
                case list:
                    return new TardisListCommand(plugin).doList(player, args);
                case make_her_blue:
                    return new TardisMakeHerBlueCommand(plugin).show(player);
                case namekey:
                    return new TardisNameKeyCommand(plugin).nameKey(player, args);
                case occupy:
                    return new TardisOccupyCommand(plugin).toggleOccupancy(player);
                case rebuild:
                    return new TardisRebuildCommand(plugin).rebuildPreset(player);
                case remove:
                    return new TardisRemoveCompanionCommand(plugin).doRemoveCompanion(player, args);
                case removesave:
                    return new TardisRemoveSavedLocationCommand(plugin).doRemoveSave(player, args);
                case renamesave:
                    return new TardisRenameSavedLocationCommand(plugin).doRenameSave(player, args);
                case reordersave:
                    return new TardisReorderSavedLocationCommand(plugin).doReorderSave(player, args);
                case rescue:
                    return new TardisRescueCommand(plugin).startRescue(player, args);
                case room:
                    return new TardisRoomCommand(plugin).startRoom(player, args);
                case save_player:
                    ItemStack is = player.getInventory().getItemInMainHand();
                    if (heldDiskIsWrong(is, "Player Storage Disk")) {
                        TardisMessage.send(player, "DISK_HAND_PLAYER");
                        return true;
                    }
                    return new TardisDiskWriterCommand(plugin).writePlayer(player, args);
                case secondary:
                    return new TardisSecondaryCommand(plugin).startSecondary(player, args);
                case section:
                    return new TardisChatGuiUpdater(plugin).showInterface(player, args);
                case setdest:
                    return new TardisSetDestinationCommand(plugin).doSetDestination(player, args);
                case tagtheood:
                    return new TardisTagCommand(plugin).getStats(player);
                case transmat:
                    return new TardisTransmatCommand(plugin).teleportOrProcess(player, args);
                case update:
                    return new TardisUpdateCommand(plugin).startUpdate(player, args);
                case abort:
                    return new TardisAbortCommand(plugin).doAbort(player, args);
                case exterminate: // delete the TARDIS
                    return new TardisExterminateCommand(plugin).doExterminate(player);
                case save:
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if (itemStack.getType().equals(Material.MUSIC_DISC_FAR)) {
                        return new TardisDiskWriterCommand(plugin).writeSaveToControlDisk(player, args);
                    } else {
                        if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
                            if (plugin.getDifficulty().equals(Difficulty.HARD) && heldDiskIsWrong(itemStack, "Save Storage Disk")) {
                                TardisMessage.send(player, "DISK_HAND_SAVE");
                                return true;
                            }
                            return new TardisDiskWriterCommand(plugin).writeSave(player, args);
                        } else {
                            return new TardisSaveLocationCommand(plugin).doSave(player, args);
                        }
                    }
                case saveicon:
                    return new TardisSaveIconCommand(plugin).changeIcon(player, args);
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
        } else if (!Objects.requireNonNull(is.getItemMeta()).hasDisplayName()) {
            complexBool = true;
        } else if (!is.getItemMeta().getDisplayName().equals(dn)) {
            complexBool = true;
        }
        return complexBool;
    }
}
