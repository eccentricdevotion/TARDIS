/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author eccentric_nz
 */
class TARDISDefaultWorldNameCommand {

    private static final Pattern DOTS = Pattern.compile("\\.");
    private final TARDIS plugin;

    TARDISDefaultWorldNameCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setName(CommandSender sender, String[] args) {
        // get world name
        String t = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        // need to make there are no periods(.) in the text
        String nodots = DOTS.matcher(t).replaceAll("_");
        plugin.getConfig().set("creation.default_world_name", nodots);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "default_world_name");
        return true;
    }
}
