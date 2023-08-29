/*
 * Copyright (C) 2023 eccentric_nz
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
import me.eccentric_nz.TARDIS.commands.utils.ArgumentParser;
import me.eccentric_nz.TARDIS.commands.utils.Arguments;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
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
        ArgumentParser parser = new ArgumentParser();
        String command = parser.join(args);
        plugin.debug(command);
        Arguments arguments = parser.parse(command);
        for (String a : arguments.getArguments()) {
            plugin.debug(a);
        }
        String first = arguments.getArguments().get(0);
        // get world name with no periods(.)
        String name = arguments.getArguments().get(1).replace(".", "_");;
        // check the world actually exists!
        if (TARDISAliasResolver.getWorldFromAlias(name) == null) {
            plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld load", plugin);
            return false;
        }
        if (first.equals("include")) {
            plugin.getPlanetsConfig().set("planets." + name + ".time_travel", true);
        } else {
            plugin.getPlanetsConfig().set("planets." + name + ".time_travel", false);
        }
        plugin.savePlanetsConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "time_travel");
        return true;
    }
}
