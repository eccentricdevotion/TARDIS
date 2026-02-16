/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.destroyers.TARDISPruner;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.util.FileUtil;

import java.io.File;

/**
 * @author eccentric_nz
 */
public class PruneCommand {

    private final TARDIS plugin;

    public PruneCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean startPruning(CommandSender sender, int days) {
        TARDISPruner pruner = new TARDISPruner(plugin);
        try {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "BACKUP_DB");
            // backup database
            File oldFile = new File(plugin.getDataFolder() + File.separator + "TARDIS.db");
            File newFile = new File(plugin.getDataFolder() + File.separator + "TARDIS_" + System.currentTimeMillis() + ".db");
            // back up the file
            FileUtil.copy(oldFile, newFile);
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PRUNE_START");
            pruner.prune(sender, days);
            return true;
        } catch (NumberFormatException nfe) {
            plugin.debug("Could not convert to integer");
            return false;
        }
    }

    public boolean listPrunes(CommandSender sender, int days) {
        TARDISPruner pruner = new TARDISPruner(plugin);
        pruner.list(sender, days);
        return true;
    }
}
