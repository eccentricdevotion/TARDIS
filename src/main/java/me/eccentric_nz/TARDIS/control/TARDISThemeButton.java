/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.desktop.TARDISThemeInventory;
import me.eccentric_nz.tardis.desktop.TARDISUpgradeData;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author eccentric_nz
 */
public class TARDISThemeButton {

    private final TARDISPlugin plugin;
    private final Player player;
    private final Schematic current_console;
    private final int level;
    private final int id;

    public TARDISThemeButton(TARDISPlugin plugin, Player player, Schematic current_console, int level, int id) {
        this.plugin = plugin;
        this.player = player;
        this.current_console = current_console;
        this.level = level;
        this.id = id;
    }

    public void clickButton() {
        // check player is in own tardis
        int p_tid = getTardisId(player.getUniqueId().toString());
        if (p_tid != id) {
            TARDISMessage.send(player, "UPGRADE_OWN");
            return;
        }
        // get player's current console
        TARDISUpgradeData tud = new TARDISUpgradeData();
        tud.setPrevious(current_console);
        tud.setLevel(level);
        plugin.getTrackerKeeper().getUpgrades().put(player.getUniqueId(), tud);
        // open the upgrade menu
        ItemStack[] consoles = new TARDISThemeInventory(plugin, player, current_console.getPermission(), level).getMenu();
        Inventory upg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "tardis Upgrade Menu");
        upg.setContents(consoles);
        player.openInventory(upg);
    }

    private int getTardisId(String uuid) {
        int tid = 0;
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(uuid)) {
            tid = rs.getTardisId();
        }
        return tid;
    }
}
