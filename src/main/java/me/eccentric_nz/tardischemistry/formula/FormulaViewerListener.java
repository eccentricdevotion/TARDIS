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
package me.eccentric_nz.tardischemistry.formula;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.tardischemistry.compound.Compound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FormulaViewerListener extends TARDISMenuListener {

    private final List<UUID> viewers = new ArrayList<>();
    private final TARDIS plugin;

    public FormulaViewerListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onFormulaViewerOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof FormulaViewer) {
            Player player = (Player) event.getPlayer();
            UUID uuid = player.getUniqueId();
            viewers.add(uuid);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFormulaViewerClick(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        InventoryType type = top.getType();
        if (type == InventoryType.CHEST) {
            Player player = (Player) event.getWhoClicked();
            if (viewers.contains(player.getUniqueId())) {
                event.setCancelled(true);
                if (event.getRawSlot() == 26) {
                    // close
                    close(player);
                } else if (event.getRawSlot() != 0) {
                    ItemStack is = event.getCurrentItem();
                    if (is != null && is.hasItemMeta() && Objects.requireNonNull(is.getItemMeta()).hasDisplayName()) {
                        // is it a compound?
                        try {
                            Compound compound = Compound.valueOf(ComponentUtils.stripColour(is.getItemMeta().displayName()).replace(" ", "_"));
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new FormulaViewer(plugin, player).getCompoundFormula(compound), 2L);
                        } catch (IllegalArgumentException e) {
                            // don't know what it is
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFormulaViewerClose(InventoryCloseEvent event) {
        Inventory top = event.getView().getTopInventory();
        InventoryType type = top.getType();
        if (type == InventoryType.CHEST) {
            Player p = (Player) event.getPlayer();
            UUID uuid = p.getUniqueId();
            if (viewers.contains(uuid)) {
                viewers.remove(uuid);
                event.getView().getTopInventory().clear();
                p.updateInventory();
            }
        }
    }
}
