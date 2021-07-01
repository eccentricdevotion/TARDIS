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
package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

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
        // get world name with no periods(.)
        String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace(".", "_");
        // check the world actually exists!
        if (TARDISAliasResolver.getWorldFromAlias(name) == null) {
            TARDISMessage.send(sender, "WORLD_NOT_FOUND");
            return false;
        }
        if (first.equals("include")) {
            plugin.getPlanetsConfig().set("planets." + name + ".time_travel", true);
        } else {
            plugin.getPlanetsConfig().set("planets." + name + ".time_travel", false);
        }
        plugin.savePlanetsConfig();
        TARDISMessage.send(sender, "CONFIG_UPDATED");
        return true;
    }
}
