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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.desktop.PluginThemeInventory;
import me.eccentric_nz.TARDIS.desktop.UpgradeData;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateDestinationTerminalForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISThemeButton {

    private final TARDIS plugin;
    private final Player player;
    private final Schematic current_console;
    private final int level;
    private final int id;

    public TARDISThemeButton(TARDIS plugin, Player player, Schematic current_console, int level, int id) {
        this.plugin = plugin;
        this.player = player;
        this.current_console = current_console;
        this.level = level;
        this.id = id;
    }

    public static int getTardisId(String uuid) {
        int tid = 0;
        ResultSetTardisID rs = new ResultSetTardisID(TARDIS.plugin);
        if (rs.fromUUID(uuid)) {
            tid = rs.getTardisId();
        }
        return tid;
    }

    public void clickButton() {
        // check player is in own TARDIS
        UUID uuid = player.getUniqueId();
        int p_tid = getTardisId(uuid.toString());
        if (p_tid != id) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "UPGRADE_OWN");
            return;
        }
        // check they are not growing rooms
        if (plugin.getTrackerKeeper().getIsGrowingRooms().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_UPGRADE_WHILE_GROWING");
            return;
        }
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.DESKTOP_THEME)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Desktop Theme");
            return;
        }
        // get player's current console
        UpgradeData tud = new UpgradeData();
        tud.setPrevious(current_console);
        tud.setLevel(level);
        plugin.getTrackerKeeper().getUpgrades().put(uuid, tud);
        // open the upgrade menu
        if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(uuid)) {
            new FloodgateDestinationTerminalForm(plugin, uuid).send();
        } else {
            player.openInventory(new PluginThemeInventory(plugin, player, current_console.getPermission(), level).getInventory());
        }
    }
}
