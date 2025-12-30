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
package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISInformationSystemCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISInformationSystemCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            return true;
        }
        UUID uuid = p.getUniqueId();
        if (plugin.getTrackerKeeper().getInfoMenu().containsKey(uuid)) {
            if (args[0].equalsIgnoreCase("E")) {
                TARDISInformationSystemListener.exit(p, plugin);
                return true;
            }
            if (args.length == 1) {
                TARDISInformationSystemListener.processInput(p, uuid, args[0], plugin);
            } else {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "TIS_EXIT");
            }
        } else if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
            // open TIS GUI
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    p.openInventory(new TARDISIndexFileInventory(plugin).getInventory()), 2L);
        }
        return true;
    }
}
