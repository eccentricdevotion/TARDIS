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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Perception filters had the effect of directing attention away from the object or its bearer, rendering them
 * unnoticeable. The Doctor was able to construct a perception filter around three keys to the TARDIS, activated when
 * they were worn around the neck on a chain.
 *
 * @author eccentric_nz
 */
public class TARDISPerceptionFilterListener implements Listener {

    private final TARDIS plugin;
    private final Material material;

    public TARDISPerceptionFilterListener(TARDIS plugin) {
        this.plugin = plugin;
        Material material;
        try {
            material = Material.valueOf(plugin.getConfig().getString("preferences.key"));
        } catch (IllegalArgumentException e) {
            material = Material.GOLD_NUGGET;
        }
        this.material = material;
    }

    @EventHandler
    public void onPerceptionFilterClick(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if ((is.getType().equals(Material.OMINOUS_TRIAL_KEY) || is.getType().equals(material)) && event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName() && im.getDisplayName().endsWith("Perception Filter")) {
                    if (TARDISPermission.hasPermission(player, "tardis.filter")) {
                        ItemStack chestPlate = player.getInventory().getChestplate();
                        if (chestPlate == null) {
                            // equip the chest slot with the perception filter
                            player.getInventory().setChestplate(is);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                player.getInventory().setItemInMainHand(null);
                                player.updateInventory();
                                // make the player invisible
                                plugin.getFilter().addPerceptionFilter(player);
                            }, 1L);
                        } else {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "FILTER");
                        }
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPerceptionFilterRemove(InventoryClickEvent event) {
        if (event.getInventory().getType().equals(InventoryType.CRAFTING)) {
            int slot = event.getRawSlot();
            if (slot == 6) {
                ItemStack is = event.getCurrentItem();
                if (is != null) {
                    if (is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName() && im.getDisplayName().endsWith("Perception Filter")) {
                            if (event.getAction().equals(InventoryAction.PICKUP_ALL) || event.getAction().equals(InventoryAction.PLACE_ALL)) {
                                plugin.getFilter().removePerceptionFilter((Player) event.getWhoClicked());
                            }
                        }
                    }
                }
            }
        }
    }
}
