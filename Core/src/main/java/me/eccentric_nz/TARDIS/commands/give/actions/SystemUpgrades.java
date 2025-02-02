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
package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeUpdate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SystemUpgrades {

    private final TARDIS plugin;

    public SystemUpgrades(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean give(CommandSender sender, String player, String upgrade) {
        if (!plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "SYS_DISABLED");
            return true;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        if (offlinePlayer.getName() == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_FOUND");
            return true;
        }
        String uuid = offlinePlayer.getUniqueId().toString();
        // get player's TARDIS id
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (!rst.fromUUID(uuid)) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NO_TARDIS");
            return true;
        }
        if (upgrade.equalsIgnoreCase("all")) {
            for (SystemTree st : SystemTree.values()) {
                if (st.getSlot() != -1 && st != SystemTree.UPGRADE_TREE) {
                    new SystemUpgradeUpdate(plugin).set(uuid, rst.getTardisId(), st);
                }
            }
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "SYS_SUCCESS", "full TARDIS");
        } else {
            SystemTree systemTree;
            try {
                systemTree = SystemTree.valueOf(upgrade);
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "SYS_INVALID");
                return true;
            }
            // update system upgrade record
            new SystemUpgradeUpdate(plugin).set(uuid, rst.getTardisId(), systemTree);
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "SYS_SUCCESS", systemTree.getName());
        }
        return true;
    }
}
