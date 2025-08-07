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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.gui.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.console.telepathic.TARDISTelepathicInventory;
import me.eccentric_nz.TARDIS.control.TARDISScanner;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.travel.save.TARDISSavesPlanetInventory;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A trip stitch circuit-breaker is a circuit that if enabled in the psycho-kinetic threshold manipulator of the
 * Daleks has the effect of preventing them from controlling their movement.
 *
 * @author eccentric_nz
 */
public class TARDISConsoleSwitchListener implements Listener {

    private final TARDIS plugin;
    private final List<String> gui_circuits = List.of(
            "TARDIS Telepathic Circuit",
            "TARDIS Chameleon Circuit",
            "TARDIS ARS Circuit",
            "TARDIS Temporal Circuit",
            "TARDIS Memory Circuit",
            "TARDIS Input Circuit",
            "TARDIS Scanner Circuit"
    );

    public TARDISConsoleSwitchListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onConsoleInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof TARDISAdvancedConsoleInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        // check they're in the TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            event.setCancelled(true);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
        }
        if (!event.getClick().equals(ClickType.SHIFT_RIGHT)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= 18) {
            return;
        }
        ItemStack item = event.getView().getItem(slot);
        if (item == null || !item.getType().equals(Material.GLOWSTONE_DUST) || !item.hasItemMeta()) {
            return;
        }
        ItemMeta im = item.getItemMeta();
        if (!im.hasDisplayName()) {
            return;
        }
        String dn = ComponentUtils.stripColour(im.displayName());
        if (!gui_circuits.contains(dn)) {
            return;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
            return;
        }
        Tardis tardis = rs.getTardis();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            InventoryHolder holder = null;
            // Chameleon circuit
            if (dn.contains("Chameleon")) {
                holder = new TARDISChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset(), tardis.getItemPreset());
            }
            // ARS circuit
            if (dn.contains("ARS")) {
                holder = new TARDISARSInventory(plugin, player);
            }
            // Telepathic circuit
            if (dn.contains("Telepathic")) {
                holder = new TARDISTelepathicInventory(plugin, player);
            }
            // Temporal circuit
            if (dn.contains("Temporal")) {
                holder = new TARDISTemporalLocatorInventory(plugin);
            }
            // Memory circuit (saves/areas)
            if (dn.contains("Memory")) {
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid.toString(), SystemTree.SAVES)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Saves");
                    return;
                }
                holder = new TARDISSavesPlanetInventory(plugin, tardis.getTardisId(), player);
            }
            // Input circuit (terminal)
            if (dn.contains("Input")) {
                holder = new TARDISTerminalInventory(plugin);
            }
            // scanner circuit
            else {
                new TARDISScanner(plugin).scan(tardis.getTardisId(), player, tardis.getRenderer(), tardis.getArtronLevel());
            }
            // close inventory
            player.closeInventory();
            if (holder != null) {
                // open new inventory
                player.openInventory(holder.getInventory());
            }
        }, 1L);
    }
}
