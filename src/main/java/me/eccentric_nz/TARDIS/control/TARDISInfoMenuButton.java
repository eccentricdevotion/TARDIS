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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.floodgate.FloodgateIndexFileForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.info.TARDISIndexFileInventory;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.dialog.CategoryDialog;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISInfoMenuButton {

    private final TARDIS plugin;
    private final Player player;

    public TARDISInfoMenuButton(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void clickButton() {
        // get player preference
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
        if (rsp.resultSet() && rsp.isInfoOn()) {
            // open TIS GUI
            if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                new FloodgateIndexFileForm(plugin, player.getUniqueId()).send();
            } else {
                ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                if (rspp.resultSet()) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (rspp.isDialogsOn()) {
                            Audience.audience(player).showDialog(new CategoryDialog().create());
                        } else {
                            player.openInventory(new TARDISIndexFileInventory(plugin).getInventory());
                        }
                    }, 2L);
                }
            }
        } else {
            plugin.getTrackerKeeper().getInfoMenu().put(player.getUniqueId(), TARDISInfoMenu.TIS);
            plugin.getMessenger().messageWithColour(player, "-----------TARDIS Information System-----------", "#FFAA00");
            player.sendMessage(plugin.getLanguage().getString("TIS_INFO", "Type a white letter in chat, or click an info entry to proceed."));
            plugin.getMessenger().sendInfo(player, "> TARDIS ", "M", "anual");
            plugin.getMessenger().sendInfo(player, "> ", "I", "tems");
            plugin.getMessenger().sendInfo(player, "> ", "C", "omponents");
            plugin.getMessenger().sendInfo(player, "> ", "S", "onic Components");
            plugin.getMessenger().sendInfo(player, "> ", "D", "isks");
            plugin.getMessenger().sendInfo(player, "> C", "o", "mmands");
            plugin.getMessenger().sendInfo(player, "> ", "T", "ARDIS Types");
            plugin.getMessenger().sendInfo(player, "> ", "R", "ooms");
            plugin.getMessenger().sendInfo(player, "> Rooms ", "2", "");
            plugin.getMessenger().sendInfo(player, "> ", "F", "ood & Accessories");
            plugin.getMessenger().sendInfo(player, "> ", "P", "lanets");
            plugin.getMessenger().sendInfo(player, "> Mo", "n", "sters");
            plugin.getMessenger().sendInfo(player, "> ", "E", "xit");
        }
    }
}
