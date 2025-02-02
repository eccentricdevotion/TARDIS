/*
 * Copyright (C) 2025 eccentric_nz
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

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.protection.TARDISWorldGuardFlag;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISSetRespectCommand {

    private final TARDIS plugin;
    private final ImmutableList<String> regions = ImmutableList.of("none", "wilderness", "town", "nation");
    private final ImmutableList<String> flags = ImmutableList.copyOf(TARDISWorldGuardFlag.getFLAG_LOOKUP().keySet());

    TARDISSetRespectCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setRegion(CommandSender sender, String[] args) {
        String region = args[1].toLowerCase(Locale.ROOT);
        if (!regions.contains(region)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_TOWNY");
            return false;
        }
        plugin.getConfig().set("preferences.respect_towny", region);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "respect_towny");
        return true;
    }

    boolean setFlag(CommandSender sender, String[] args) {
        String flag = args[1].toLowerCase(Locale.ROOT);
        if (!flags.contains(flag)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_FLAG");
            return false;
        }
        plugin.getConfig().set("preferences.respect_worldguard", flag);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", "respect_worldguard");
        return true;
    }
}
