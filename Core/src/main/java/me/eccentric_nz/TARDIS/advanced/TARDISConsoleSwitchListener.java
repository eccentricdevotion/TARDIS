/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.travel.save.TARDISSavesPlanetInventory;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * A trip stitch circuit-breaker was a circuit that if was enabled in the psycho-kinetic threshold manipulator of the
 * Daleks had the effect of preventing them from controlling their movement.
 *
 * @author eccentric_nz
 */
public class TARDISConsoleSwitchListener implements Listener {

    private final TARDIS plugin;
    private final List<NamespacedKey> gui_circuits = List.of(
            CircuitVariant.TELEPATHIC.getKey(),
            CircuitVariant.CHAMELEON.getKey(),
            CircuitVariant.ARS.getKey(),
            CircuitVariant.TEMPORAL.getKey(),
            CircuitVariant.MEMORY.getKey(),
            CircuitVariant.INPUT.getKey(),
            CircuitVariant.SCANNER.getKey(),
            CircuitVariant.ARS_DAMAGED.getKey(),
            CircuitVariant.CHAMELEON_DAMAGED.getKey(),
            CircuitVariant.INPUT_DAMAGED.getKey(),
            CircuitVariant.MEMORY_DAMAGED.getKey(),
            CircuitVariant.SCANNER_DAMAGED.getKey(),
            CircuitVariant.TEMPORAL_DAMAGED.getKey(),
            CircuitVariant.TELEPATHIC_DAMAGED.getKey()
    );

    public TARDISConsoleSwitchListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onConsoleInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Console")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        String uuid = player.getUniqueId().toString();
        // check they're in the TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", uuid);
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
        ItemStack item = view.getItem(slot);
        if (item == null || !item.getType().equals(Material.GLOWSTONE_DUST) || !item.hasItemMeta()) {
            return;
        }
        ItemMeta im = item.getItemMeta();
        NamespacedKey cmd = im.getItemModel();
        if (cmd == null || !gui_circuits.contains(cmd)) {
            return;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
            return;
        }
        Tardis tardis = rs.getTardis();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            ItemStack[] stack = null;
            Inventory new_inv = null;
            // Chameleon circuit
            if (cmd == CircuitVariant.CHAMELEON.getKey() || cmd == CircuitVariant.CHAMELEON_DAMAGED.getKey()) {
                new_inv = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
                stack = new TARDISChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset(), tardis.getItemPreset()).getMenu();
            }
            // ARS circuit
            if (cmd == CircuitVariant.ARS.getKey() || cmd == CircuitVariant.ARS_DAMAGED.getKey()) {
                new_inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Architectural Reconfiguration");
                stack = new TARDISARSInventory(plugin, player).getARS();
            }
            // Telepathic circuit
            if (cmd == CircuitVariant.TELEPATHIC.getKey() || cmd == CircuitVariant.TELEPATHIC_DAMAGED.getKey()) {
                new_inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Telepathic Circuit");
                stack = new TARDISTelepathicInventory(plugin, player).getButtons();
            }
            // Temporal circuit
            if (cmd == CircuitVariant.TEMPORAL.getKey() || cmd == CircuitVariant.TEMPORAL_DAMAGED.getKey()) {
                new_inv = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Temporal Locator");
                stack = new TARDISTemporalLocatorInventory(plugin).getTemporal();
            }
            // Memory circuit (saves/areas)
            if (cmd == CircuitVariant.MEMORY.getKey() || cmd == CircuitVariant.MEMORY_DAMAGED.getKey()) {
                if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(uuid, SystemTree.SAVES)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Saves");
                    return;
                }
                new_inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Dimension Map");
                stack = new TARDISSavesPlanetInventory(plugin, tardis.getTardisId(), player).getPlanets();
            }
            // Input circuit (terminal)
            if (cmd == CircuitVariant.INPUT.getKey() || cmd == CircuitVariant.INPUT_DAMAGED.getKey()) {
                new_inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Destination Terminal");
                stack = new TARDISTerminalInventory(plugin).getTerminal();
            }
            // scanner circuit
            else {
                new TARDISScanner(plugin).scan(tardis.getTardisId(), player, tardis.getRenderer(), tardis.getArtronLevel());
            }
            // close inventory
            player.closeInventory();
            if (new_inv != null && stack != null) {
                // open new inventory
                new_inv.setContents(stack);
                player.openInventory(new_inv);
            }
        }, 1L);
    }
}
