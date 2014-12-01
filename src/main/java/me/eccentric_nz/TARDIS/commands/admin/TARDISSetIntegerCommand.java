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
package me.eccentric_nz.TARDIS.commands.admin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSetIntegerCommand {

    private final TARDIS plugin;
    private final List<String> TIPS_SUBS = Arrays.asList("400", "800", "1200", "1600");

    public TARDISSetIntegerCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setConfigInt(CommandSender sender, String[] args, String section) {
        String first = (section.isEmpty()) ? args[0].toLowerCase() : section + "." + args[0].toLowerCase();
        String a = args[1];
        if (args[0].toLowerCase().equals("tips_limit") && !TIPS_SUBS.contains(a)) {
            TARDISMessage.send(sender, "ARG_TIPS");
            return false;
        }
        int val;
        try {
            val = Integer.parseInt(a);
        } catch (NumberFormatException nfe) {
            // not a number
            TARDISMessage.send(sender, "ARG_LAST_NUMBER");
            return false;
        }
        if (first.equals("circuits.uses.chameleon_uses")) {
            first = "circuits.uses.chameleon";
        }
        if (first.equals("circuits.uses.invisibility_uses")) {
            first = "circuits.uses.invisibility";
        }
        plugin.getConfig().set(first, val);
        if (first.equals("terminal_step")) {
            // reset the terminal inventory
            plugin.getGeneralKeeper().getButtonListener().items = new TARDISTerminalInventory(plugin).getTerminal();
        }
        plugin.saveConfig();
        TARDISMessage.send(sender, "CONFIG_UPDATED");
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
            TARDISMessage.send(sender, "ARG_LAST_NUMBER");
            return false;
        }
        plugin.getArtronConfig().set(first, val);
        try {
            plugin.getArtronConfig().save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException io) {
            plugin.debug("Could not save artron.yml, " + io);
        }
        TARDISMessage.send(sender, "CONFIG_UPDATED");
        return true;
    }

    public boolean setRandomInt(CommandSender sender, String[] args) {
        String first = args[0];
        String which = args[1];
        if (!which.equalsIgnoreCase("x") || !which.equalsIgnoreCase("z")) {
            TARDISMessage.send(sender, "ARG_DIRECTION");
            return true;
        }
        String a = args[2];
        int val;
        try {
            val = Integer.parseInt(a);
        } catch (NumberFormatException nfe) {
            // not a number
            TARDISMessage.send(sender, "ARG_LAST_NUMBER");
            return false;
        }
        plugin.getConfig().set("travel." + first + "." + which.toLowerCase(), val);
        plugin.saveConfig();
        TARDISMessage.send(sender, "CONFIG_UPDATED");
        return true;
    }
}
