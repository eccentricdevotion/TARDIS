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
import me.eccentric_nz.TARDIS.enumeration.CMDS;
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
            player.sendMessage(CMDS.COMMANDS.getHelp().split("\n"));
            return true;
        }
        if (args.length == 2) {
            List<String> cmds = new ArrayList<String>();
            for (CMDS c : CMDS.values()) {
                cmds.add(c.toString());
            }
            // check that the second arument is valid
            if (!cmds.contains(args[1].toUpperCase(Locale.ENGLISH))) {
                player.sendMessage(plugin.pluginName + "That is not a valid help topic!");
                return true;
            }
            switch (CMDS.fromString(args[1])) {
                case CREATE:
                    player.sendMessage(CMDS.CREATE.getHelp().split("\n"));
                    break;
                case DELETE:
                    player.sendMessage(CMDS.DELETE.getHelp().split("\n"));
                    break;
                case TIMETRAVEL:
                    player.sendMessage(CMDS.TIMETRAVEL.getHelp().split("\n"));
                    break;
                case LIST:
                    player.sendMessage(CMDS.LIST.getHelp().split("\n"));
                    break;
                case FIND:
                    player.sendMessage(CMDS.FIND.getHelp().split("\n"));
                    break;
                case SAVE:
                    player.sendMessage(CMDS.SAVE.getHelp().split("\n"));
                    break;
                case REMOVESAVE:
                    player.sendMessage(CMDS.REMOVESAVE.getHelp().split("\n"));
                    break;
                case ADD:
                    player.sendMessage(CMDS.ADD.getHelp().split("\n"));
                    break;
                case TRAVEL:
                    player.sendMessage(CMDS.TRAVEL.getHelp().split("\n"));
                    break;
                case UPDATE:
                    player.sendMessage(CMDS.UPDATE.getHelp().split("\n"));
                    break;
                case REBUILD:
                    player.sendMessage(CMDS.REBUILD.getHelp().split("\n"));
                    break;
                case CHAMELEON:
                    player.sendMessage(CMDS.CHAMELEON.getHelp().split("\n"));
                    break;
                case SFX:
                    player.sendMessage(CMDS.SFX.getHelp().split("\n"));
                    break;
                case PLATFORM:
                    player.sendMessage(CMDS.PLATFORM.getHelp().split("\n"));
                    break;
                case SETDEST:
                    player.sendMessage(CMDS.SETDEST.getHelp().split("\n"));
                    break;
                case HOME:
                    player.sendMessage(CMDS.HOME.getHelp().split("\n"));
                    break;
                case HIDE:
                    player.sendMessage(CMDS.HIDE.getHelp().split("\n"));
                    break;
                case VERSION:
                    player.sendMessage(CMDS.HIDE.getHelp().split("\n"));
                    break;
                case ADMIN:
                    player.sendMessage(CMDS.ADMIN.getHelp().split("\n"));
                    break;
                case AREA:
                    player.sendMessage(CMDS.AREA.getHelp().split("\n"));
                    break;
                case ROOM:
                    player.sendMessage(CMDS.ROOM.getHelp().split("\n"));
                    break;
                case ARTRON:
                    player.sendMessage(CMDS.ARTRON.getHelp().split("\n"));
                    break;
                case BIND:
                    player.sendMessage(CMDS.BIND.getHelp().split("\n"));
                    break;
                default:
                    player.sendMessage(CMDS.COMMANDS.getHelp().split("\n"));
            }
        }
        return true;
    }
}
