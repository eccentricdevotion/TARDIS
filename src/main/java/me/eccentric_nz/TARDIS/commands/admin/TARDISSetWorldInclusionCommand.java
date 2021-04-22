/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISSetWorldInclusionCommand {

    private final TARDIS plugin;

    TARDISSetWorldInclusionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setWorldStatus(CommandSender sender, String[] args) {
        String first = args[0];
        // get world name
        int count = args.length;
        StringBuilder buf = new StringBuilder();
        for (int i = 1; i < count; i++) {
            buf.append(args[i]).append(" ");
        }
        String tmp = buf.toString();
        String t = tmp.substring(0, tmp.length() - 1);
        // need to make there are no periods(.) in the text
        String nodots = StringUtils.replace(t, ".", "_");
        // check the world actually exists!
        if (TARDISAliasResolver.getWorldFromAlias(nodots) == null) {
            TARDISMessage.send(sender, "WORLD_NOT_FOUND");
            return false;
        }
        if (first.equals("include")) {
            plugin.getPlanetsConfig().set("planets." + TARDISStringUtils.worldName(nodots) + ".time_travel", true);
        } else {
            plugin.getPlanetsConfig().set("planets." + TARDISStringUtils.worldName(nodots) + ".time_travel", false);
        }
        plugin.savePlanetsConfig();
        TARDISMessage.send(sender, "CONFIG_UPDATED");
        return true;
    }
}
