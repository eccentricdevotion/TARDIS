/*
 * Copyright (C) 2013 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.destroyers.TARDISPruner;
import static me.eccentric_nz.TARDIS.files.TARDISConfigConverter.copyFile;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPruneCommand {

    private final TARDIS plugin;

    public TARDISPruneCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean startPruning(CommandSender sender, String[] args) {
        TARDISPruner pruner = new TARDISPruner(plugin);
        if (args[1].equalsIgnoreCase("list") && args.length == 3) {
            sender.sendMessage(plugin.pluginName + "Please use the /tardisadmin prunelist command");
            return true;
        }
        try {
            sender.sendMessage(plugin.pluginName + "Backing up TARDIS database...");
            // backup database
            File oldFile = new File(plugin.getDataFolder() + File.separator + "TARDIS.db");
            File newFile = new File(plugin.getDataFolder() + File.separator + "TARDIS_" + System.currentTimeMillis() + ".db");
            // back up the file
            try {
                copyFile(oldFile, newFile);
            } catch (IOException ex) {
                plugin.debug("Could not backup TARDIS.db! " + ex.getMessage());
                return false;
            }
            int days = Integer.parseInt(args[1]);
            sender.sendMessage(plugin.pluginName + "Starting TARDIS prune...");
            pruner.prune(sender, days);
            return true;
        } catch (NumberFormatException nfe) {
            plugin.debug("Could not convert to integer");
            return false;
        }
    }

    public boolean listPrunes(CommandSender sender, String[] args) {
        int days = plugin.utils.parseInt(args[1]);
        TARDISPruner pruner = new TARDISPruner(plugin);
        pruner.list(sender, days);
        return true;
    }
}
