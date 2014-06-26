/*
 * Copyright (C) 2014 eccentric_nz
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
            CMDS.send(player, CMDS.COMMANDS.getHelp());
            return true;
        }
        if (args.length == 2) {
            List<String> cmds = new ArrayList<String>();
            for (CMDS c : CMDS.values()) {
                cmds.add(c.toString());
            }
            // check that the second arument is valid
            if (!cmds.contains(args[1].toUpperCase(Locale.ENGLISH))) {
                TARDISMessage.send(player, "HELP_NOT_VALID");
                return true;
            }
            switch (CMDS.fromString(args[1])) {
                case CREATE:
                    CMDS.send(player, CMDS.CREATE.getHelp());
                    break;
                case DELETE:
                    CMDS.send(player, CMDS.DELETE.getHelp());
                    break;
                case TIMETRAVEL:
                    CMDS.send(player, CMDS.TIMETRAVEL.getHelp());
                    break;
                case LIST:
                    CMDS.send(player, CMDS.LIST.getHelp());
                    break;
                case FIND:
                    CMDS.send(player, CMDS.FIND.getHelp());
                    break;
                case SAVE:
                    CMDS.send(player, CMDS.SAVE.getHelp());
                    break;
                case REMOVESAVE:
                    CMDS.send(player, CMDS.REMOVESAVE.getHelp());
                    break;
                case ADD:
                    CMDS.send(player, CMDS.ADD.getHelp());
                    break;
                case TRAVEL:
                    CMDS.send(player, CMDS.TRAVEL.getHelp());
                    break;
                case UPDATE:
                    CMDS.send(player, CMDS.UPDATE.getHelp());
                    break;
                case REBUILD:
                    CMDS.send(player, CMDS.REBUILD.getHelp());
                    break;
                case CHAMELEON:
                    CMDS.send(player, CMDS.CHAMELEON.getHelp());
                    break;
                case SFX:
                    CMDS.send(player, CMDS.SFX.getHelp());
                    break;
                case SETDEST:
                    CMDS.send(player, CMDS.SETDEST.getHelp());
                    break;
                case HOME:
                    CMDS.send(player, CMDS.HOME.getHelp());
                    break;
                case HIDE:
                    CMDS.send(player, CMDS.HIDE.getHelp());
                    break;
                case VERSION:
                    CMDS.send(player, CMDS.HIDE.getHelp());
                    break;
                case ADMIN:
                    CMDS.send(player, CMDS.ADMIN.getHelp());
                    break;
                case AREA:
                    CMDS.send(player, CMDS.AREA.getHelp());
                    break;
                case ROOM:
                    CMDS.send(player, CMDS.ROOM.getHelp());
                    break;
                case ARTRON:
                    CMDS.send(player, CMDS.ARTRON.getHelp());
                    break;
                case BIND:
                    CMDS.send(player, CMDS.BIND.getHelp());
                    break;
                default:
                    CMDS.send(player, CMDS.COMMANDS.getHelp());
            }
        }
        return true;
    }
}
