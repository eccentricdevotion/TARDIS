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
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISHelpCommand {

    private final TARDIS plugin;

    public TARDISHelpCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean showHelp(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(TARDISConstants.COMMANDS.split("\n"));
            return true;
        }
        if (args.length == 2) {
            List<String> cmds = new ArrayList<String>();
            for (TARDISConstants.CMDS c : TARDISConstants.CMDS.values()) {
                cmds.add(c.toString());
            }
            // check that the second arument is valid
            if (!cmds.contains(args[1].toUpperCase(Locale.ENGLISH))) {
                player.sendMessage(plugin.pluginName + "That is not a valid help topic!");
                return true;
            }
            switch (TARDISConstants.fromString(args[1])) {
                case CREATE:
                    player.sendMessage(TARDISConstants.COMMAND_CREATE.split("\n"));
                    break;
                case DELETE:
                    player.sendMessage(TARDISConstants.COMMAND_DELETE.split("\n"));
                    break;
                case TIMETRAVEL:
                    player.sendMessage(TARDISConstants.COMMAND_TIMETRAVEL.split("\n"));
                    break;
                case LIST:
                    player.sendMessage(TARDISConstants.COMMAND_LIST.split("\n"));
                    break;
                case FIND:
                    player.sendMessage(TARDISConstants.COMMAND_FIND.split("\n"));
                    break;
                case SAVE:
                    player.sendMessage(TARDISConstants.COMMAND_SAVE.split("\n"));
                    break;
                case REMOVESAVE:
                    player.sendMessage(TARDISConstants.COMMAND_REMOVESAVE.split("\n"));
                    break;
                case ADD:
                    player.sendMessage(TARDISConstants.COMMAND_ADD.split("\n"));
                    break;
                case TRAVEL:
                    player.sendMessage(TARDISConstants.COMMAND_TRAVEL.split("\n"));
                    break;
                case UPDATE:
                    player.sendMessage(TARDISConstants.COMMAND_UPDATE.split("\n"));
                    break;
                case REBUILD:
                    player.sendMessage(TARDISConstants.COMMAND_REBUILD.split("\n"));
                    break;
                case CHAMELEON:
                    player.sendMessage(TARDISConstants.COMMAND_CHAMELEON.split("\n"));
                    break;
                case SFX:
                    player.sendMessage(TARDISConstants.COMMAND_SFX.split("\n"));
                    break;
                case PLATFORM:
                    player.sendMessage(TARDISConstants.COMMAND_PLATFORM.split("\n"));
                    break;
                case SETDEST:
                    player.sendMessage(TARDISConstants.COMMAND_SETDEST.split("\n"));
                    break;
                case HOME:
                    player.sendMessage(TARDISConstants.COMMAND_HOME.split("\n"));
                    break;
                case HIDE:
                    player.sendMessage(TARDISConstants.COMMAND_HIDE.split("\n"));
                    break;
                case VERSION:
                    player.sendMessage(TARDISConstants.COMMAND_HIDE.split("\n"));
                    break;
                case ADMIN:
                    player.sendMessage(TARDISConstants.COMMAND_ADMIN.split("\n"));
                    break;
                case AREA:
                    player.sendMessage(TARDISConstants.COMMAND_AREA.split("\n"));
                    break;
                case ROOM:
                    player.sendMessage(TARDISConstants.COMMAND_ROOM.split("\n"));
                    break;
                case ARTRON:
                    player.sendMessage(TARDISConstants.COMMAND_ARTRON.split("\n"));
                    break;
                case BIND:
                    player.sendMessage(TARDISConstants.COMMAND_BIND.split("\n"));
                    break;
                default:
                    player.sendMessage(TARDISConstants.COMMANDS.split("\n"));
            }
        }
        return true;
    }
}
