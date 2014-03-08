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
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
            TARDISMessage.send(player, CMDS.COMMANDS.getHelp());
            return true;
        }
        if (args.length == 2) {
            List<String> cmds = new ArrayList<String>();
            for (CMDS c : CMDS.values()) {
                cmds.add(c.toString());
            }
            // check that the second arument is valid
            if (!cmds.contains(args[1].toUpperCase(Locale.ENGLISH))) {
                TARDISMessage.send(player, plugin.getPluginName() + "That is not a valid help topic!");
                return true;
            }
            switch (CMDS.fromString(args[1])) {
                case CREATE:
                    TARDISMessage.send(player, CMDS.CREATE.getHelp());
                    break;
                case DELETE:
                    TARDISMessage.send(player, CMDS.DELETE.getHelp());
                    break;
                case TIMETRAVEL:
                    TARDISMessage.send(player, CMDS.TIMETRAVEL.getHelp());
                    break;
                case LIST:
                    TARDISMessage.send(player, CMDS.LIST.getHelp());
                    break;
                case FIND:
                    TARDISMessage.send(player, CMDS.FIND.getHelp());
                    break;
                case SAVE:
                    TARDISMessage.send(player, CMDS.SAVE.getHelp());
                    break;
                case REMOVESAVE:
                    TARDISMessage.send(player, CMDS.REMOVESAVE.getHelp());
                    break;
                case ADD:
                    TARDISMessage.send(player, CMDS.ADD.getHelp());
                    break;
                case TRAVEL:
                    TARDISMessage.send(player, CMDS.TRAVEL.getHelp());
                    break;
                case UPDATE:
                    TARDISMessage.send(player, CMDS.UPDATE.getHelp());
                    break;
                case REBUILD:
                    TARDISMessage.send(player, CMDS.REBUILD.getHelp());
                    break;
                case CHAMELEON:
                    TARDISMessage.send(player, CMDS.CHAMELEON.getHelp());
                    break;
                case SFX:
                    TARDISMessage.send(player, CMDS.SFX.getHelp());
                    break;
                case PLATFORM:
                    TARDISMessage.send(player, CMDS.PLATFORM.getHelp());
                    break;
                case SETDEST:
                    TARDISMessage.send(player, CMDS.SETDEST.getHelp());
                    break;
                case HOME:
                    TARDISMessage.send(player, CMDS.HOME.getHelp());
                    break;
                case HIDE:
                    TARDISMessage.send(player, CMDS.HIDE.getHelp());
                    break;
                case VERSION:
                    TARDISMessage.send(player, CMDS.HIDE.getHelp());
                    break;
                case ADMIN:
                    TARDISMessage.send(player, CMDS.ADMIN.getHelp());
                    break;
                case AREA:
                    TARDISMessage.send(player, CMDS.AREA.getHelp());
                    break;
                case ROOM:
                    TARDISMessage.send(player, CMDS.ROOM.getHelp());
                    break;
                case ARTRON:
                    TARDISMessage.send(player, CMDS.ARTRON.getHelp());
                    break;
                case BIND:
                    TARDISMessage.send(player, CMDS.BIND.getHelp());
                    break;
                default:
                    TARDISMessage.send(player, CMDS.COMMANDS.getHelp());
            }
        }
        return true;
    }
}
