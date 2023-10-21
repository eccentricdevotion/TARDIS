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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.messaging.TARDISChatPaginator;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISSetIntegerCommand {

    private final TARDIS plugin;
    private final List<String> TIPS_SUBS = Arrays.asList("400", "800", "1200", "1600");

    TARDISSetIntegerCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setConfigInt(CommandSender sender, String[] args, String section) {
        String first = (section.isEmpty()) ? args[0].toLowerCase(Locale.ENGLISH) : section + "." + args[0].toLowerCase(Locale.ENGLISH);
        String a = args[1];
        if (args[0].toLowerCase(Locale.ENGLISH).equals("tips_limit") && !TIPS_SUBS.contains(a)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_TIPS");
            return false;
        }
        int val;
        try {
            val = Integer.parseInt(a);
        } catch (NumberFormatException nfe) {
            // not a number
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_LAST_NUMBER");
            return false;
        }
        if (first.equals("circuits.uses.chameleon_uses")) {
            first = "circuits.uses.chameleon";
        }
        if (first.equals("circuits.uses.invisibility_uses")) {
            first = "circuits.uses.invisibility";
        }
        if (first.equals("preferences.chat_width")) {
            TARDISChatPaginator.setGuaranteedNoWrapChatPageWidth(val);
        }
        if (first.equals("preferences.sfx_volume")) {
            TARDISSounds.setVolume(val);
        }
        plugin.getConfig().set(first, val);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", first);
        if (first.equals("allow.force_field")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "RESTART");
        }
        return true;
    }

    public boolean setConfigInt(CommandSender sender, String[] args) {
        String first = args[0];
        String a = args[1];
        int val;
        try {
            val = Integer.parseInt(a);
        } catch (NumberFormatException nfe) {
            // not a number
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_LAST_NUMBER");
            return false;
        }
        plugin.getArtronConfig().set(first, val);
        try {
            plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "artron.yml"));
        } catch (IOException io) {
            plugin.debug("Could not save artron.yml, " + io);
        }
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", first);
        return true;
    }

    public boolean setRandomInt(CommandSender sender, String[] args) {
        String first = args[0];
        String which = args[1];
        if (!which.equalsIgnoreCase("x") || !which.equalsIgnoreCase("z")) {
            plugin.getMessenger().sendColouredCommand(sender, "ARG_DIRECTION", "/tconfig random_circuit [x|z] [distance]", plugin);
            return true;
        }
        String a = args[2];
        int val;
        try {
            val = Integer.parseInt(a);
        } catch (NumberFormatException nfe) {
            // not a number
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_LAST_NUMBER");
            return false;
        }
        plugin.getConfig().set("travel." + first + "." + which.toLowerCase(Locale.ENGLISH), val);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "CONFIG_UPDATED", first);
        return true;
    }
}
