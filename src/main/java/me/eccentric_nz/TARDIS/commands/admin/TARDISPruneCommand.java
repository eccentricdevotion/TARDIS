/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.destroyers.TARDISPruner;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.command.CommandSender;
import org.bukkit.util.FileUtil;

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
            TARDISMessage.send(sender, "PRUNE_INFO");
            return true;
        }
        try {
            TARDISMessage.send(sender, "BACKUP_DB");
            // backup database
            File oldFile = new File(plugin.getDataFolder() + File.separator + "TARDIS.db");
            File newFile = new File(plugin.getDataFolder() + File.separator + "TARDIS_" + System.currentTimeMillis() + ".db");
            // back up the file
            FileUtil.copy(oldFile, newFile);
            int days = Integer.parseInt(args[1]);
            TARDISMessage.send(sender, "PRUNE_START");
            pruner.prune(sender, days);
            return true;
        } catch (NumberFormatException nfe) {
            plugin.debug("Could not convert to integer");
            return false;
        }
    }

    public boolean listPrunes(CommandSender sender, String[] args) {
        int days = TARDISNumberParsers.parseInt(args[1]);
        TARDISPruner pruner = new TARDISPruner(plugin);
        pruner.list(sender, days);
        return true;
    }
}
