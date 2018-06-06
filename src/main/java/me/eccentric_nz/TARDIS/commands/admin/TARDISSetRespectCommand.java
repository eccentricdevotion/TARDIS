/*
 * Copyright (C) 2018 eccentric_nz
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

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISWorldGuardFlag;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISSetRespectCommand {

    private final TARDIS plugin;
    private final ImmutableList<String> regions = ImmutableList.of("none", "wilderness", "town", "nation");
    private final ImmutableList<String> flags = ImmutableList.copyOf(TARDISWorldGuardFlag.getFLAG_LOOKUP().keySet());

    public TARDISSetRespectCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setRegion(CommandSender sender, String[] args) {
        String region = args[1].toLowerCase(Locale.ENGLISH);
        if (!regions.contains(region)) {
            TARDISMessage.send(sender, "ARG_TOWNY");
            return false;
        }
        plugin.getConfig().set("preferences.respect_towny", region);
        plugin.saveConfig();
        TARDISMessage.send(sender, "CONFIG_UPDATED");
        return true;
    }

    public boolean setFlag(CommandSender sender, String[] args) {
        String flag = args[1].toLowerCase(Locale.ENGLISH);
        if (!flags.contains(flag)) {
            TARDISMessage.send(sender, "ARG_FLAG");
            return false;
        }
        plugin.getConfig().set("preferences.respect_worldguard", flag);
        plugin.saveConfig();
        TARDISMessage.send(sender, "CONFIG_UPDATED");
        return true;
    }
}
