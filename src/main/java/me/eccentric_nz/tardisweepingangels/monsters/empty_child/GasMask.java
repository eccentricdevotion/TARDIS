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
package me.eccentric_nz.tardisweepingangels.monsters.empty_child;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.EmptyChildVariant;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

import java.util.UUID;

public class GasMask implements Listener {

    private final TARDIS plugin;

    public GasMask(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!TARDISWeepingAngels.getEmpty().contains(uuid)) {
            return;
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            PlayerInventory inv = player.getInventory();
            ItemStack helmet = inv.getHelmet();
            if (helmet != null) {
                // move it to the first free slot
                int free_slot = inv.firstEmpty();
                if (free_slot != -1) {
                    inv.setItem(free_slot, helmet);
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), helmet);
                }
            }
            // set helmet to mask
            ItemStack gasmask = ItemStack.of(Material.SUGAR, 1);
            ItemMeta im = gasmask.getItemMeta();
            im.displayName(Component.text("Gas Mask"));
            im.setItemModel(EmptyChildVariant.EMPTY_CHILD_MASK.getKey());
            EquippableComponent component = im.getEquippable();
            component.setCameraOverlay(EmptyChildVariant.EMPTY_CHILD_OVERLAY.getKey());
            im.setEquippable(component);
            gasmask.setItemMeta(im);
            inv.setHelmet(gasmask);
            player.updateInventory();
            // schedule delayed task
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TARDISWeepingAngels.getEmpty().remove(uuid);
                TARDISWeepingAngels.getTimesUp().add(uuid);
            }, 600L);
        }, 5L);
    }

    @EventHandler
    public void onHelmetClick(InventoryClickEvent event) {
        if (event.getInventory().getType().equals(InventoryType.CRAFTING) && event.getRawSlot() == 5) {
            Player player = (Player) event.getWhoClicked();
            if (TARDISWeepingAngels.getEmpty().contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
            if (TARDISWeepingAngels.getTimesUp().contains(player.getUniqueId())) {
                event.setCancelled(true);
                player.getInventory().setHelmet(null);
                player.updateInventory();
                TARDISWeepingAngels.getTimesUp().remove(player.getUniqueId());
            }
        }
    }
}
