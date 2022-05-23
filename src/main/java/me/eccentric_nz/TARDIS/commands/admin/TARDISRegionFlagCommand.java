/*
 * Copyright (C) 2022 eccentric_nz
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
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISRegionFlagCommand {

    private final TARDIS plugin;
    private final List<String> which = Arrays.asList("entry", "exit");

    TARDISRegionFlagCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean toggleEntryExit(CommandSender sender, String[] args) {
        String flag = args[1].toLowerCase(Locale.ENGLISH);
        if (!which.contains(flag)) {
            TARDISMessage.message(sender, "You need to specify which flag type you want to change to - entry or exit.");
            return true;
        }
        if (!plugin.getConfig().getBoolean("creation.default_world")) {
            TARDISMessage.message(sender, "This command only works if you are using a default world for TARDISes.");
            return true;
        }
        String world_name = plugin.getConfig().getString("creation.default_world_name");
        // get all regions for the default world
        List<String> world_regions = plugin.getWorldGuardUtils().getTARDISRegions(TARDISAliasResolver.getWorldFromAlias(world_name));
        world_regions.forEach((region_id) -> {
            if (flag.endsWith("entry")) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " exit -w " + world_name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " entry -w " + world_name + " -g nonmembers deny");
            } else {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " entry -w " + world_name);
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " exit -w " + world_name + " -g everyone deny");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " use -w " + world_name + " allow");
                plugin.getServer().dispatchCommand(plugin.getConsole(), "rg flag " + region_id + " chest-access -w " + world_name);
            }
        });
        return true;
    }
}
