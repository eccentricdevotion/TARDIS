/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
 * Perception filters had the effect of directing attention away from the object
 * or its bearer, rendering them unnoticeable. The Doctor was able to construct
 * a perception filter around three keys to the TARDIS, activated when they were
 * worn around the neck on a chain.
 *
 * @author eccentric_nz
 */
public class TARDISPerceptionFilterListener implements Listener {

    private final TARDIS plugin;
    private final Material filter;

    public TARDISPerceptionFilterListener(TARDIS plugin) {
        this.plugin = plugin;
        this.filter = Material.valueOf(plugin.getRecipesConfig().getString("shaped.Perception Filter.result"));
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPerceptionFilterClick(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        final Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType().equals(filter) && event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName() && im.getDisplayName().equals("Perception Filter")) {
                    if (player.hasPermission("tardis.filter")) {
                        ItemStack chestplate = player.getInventory().getChestplate();
                        if (chestplate == null) {
                            // equip the chest slot with the perception filter
                            player.getInventory().setChestplate(is);
                            player.updateInventory();
                            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                            // make the player invisible
                            plugin.getFilter().addPerceptionFilter(player);
                        } else {
                            TARDISMessage.send(player, "FILTER");
                        }
                    } else {
                        TARDISMessage.send(player, "NO_PERMS");
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPerceptioFilterRemove(InventoryClickEvent event) {
        if (event.getInventory().getType().equals(InventoryType.CRAFTING)) {
            int slot = event.getRawSlot();
            if (slot >= 0 && slot == 6) {
                ItemStack is = event.getCurrentItem();
                if (is != null) {
                    if (is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName() && im.getDisplayName().equals("Perception Filter")) {
                            if (event.getAction().equals(InventoryAction.PICKUP_ALL)) {
                                plugin.getFilter().removePerceptionFilter((Player) event.getWhoClicked());
                            }
                        }
                    }
                }
            }
        }
    }
}
