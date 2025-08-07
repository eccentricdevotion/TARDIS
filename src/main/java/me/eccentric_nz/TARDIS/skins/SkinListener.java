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
package me.eccentric_nz.TARDIS.skins;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class SkinListener implements Listener {

    private final List<Material> MATERIALS = List.of(Material.BRICK, Material.COD, Material.END_STONE, Material.FEATHER, Material.IRON_INGOT, Material.KELP, Material.LEATHER, Material.MANGROVE_PROPAGULE, Material.NETHERITE_SCRAP, Material.NETHER_WART, Material.ORANGE_STAINED_GLASS_PANE, Material.PAINTING, Material.POTATO, Material.PUFFERFISH, Material.ROTTEN_FLESH, Material.SNOWBALL, Material.SUGAR, Material.TORCH, Material.TURTLE_EGG, Material.WHEAT, Material.YELLOW_DYE);

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropAreaDisk(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack stack = event.getItemDrop().getItemStack();
        if (SkinUtils.SKINNED.containsKey(uuid) && MATERIALS.contains(stack.getType())) {
            ItemMeta im = stack.getItemMeta();
            event.setCancelled(im != null && im.hasItemModel());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        ItemStack stack = event.getItem();
        if (stack == null) {
            return;
        }
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!SkinUtils.SKINNED.containsKey(uuid)) {
            return;
        }
        if (!MATERIALS.contains(stack.getType())) {
            return;
        }
        ItemMeta im = stack.getItemMeta();
        if (im == null || !im.hasItemModel()) {
            return;
        }
        if (im.getPersistentDataContainer().has(TARDIS.plugin.getTimeLordUuidKey(), PersistentDataType.BOOLEAN)) {
            SkinUtils.setOrSwapItem(stack, p, EquipmentSlot.HEAD);
            p.getInventory().setItemInMainHand(null);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onClickSkinInventoryItem(InventoryClickEvent event) {
        InventoryType.SlotType slotType = event.getSlotType();
        ItemStack current = event.getCurrentItem();
        if (current != null) {
            UUID uuid = event.getWhoClicked().getUniqueId();
            if (SkinUtils.SKINNED.containsKey(uuid) && MATERIALS.contains(current.getType())) {
                if (slotType == InventoryType.SlotType.ARMOR) {
                    ItemMeta im = current.getItemMeta();
                    event.setCancelled(im != null && im.hasItemModel());
                }
                if (slotType == InventoryType.SlotType.QUICKBAR) {
                    Skin skin = SkinUtils.SKINNED.get(uuid);
                    event.setCancelled(isSkinItem(skin.name(), current));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwapSkinItem(PlayerSwapHandItemsEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (SkinUtils.SKINNED.containsKey(uuid)) {
            Skin skin = SkinUtils.SKINNED.get(uuid);
            ItemStack is = event.getMainHandItem();
            if (is != null && MATERIALS.contains(is.getType())) {
                event.setCancelled(isSkinItem(skin.name(), is));
            }
            ItemStack off = event.getOffHandItem();
            if (off != null && MATERIALS.contains(off.getType())) {
                event.setCancelled(isSkinItem(skin.name(), off));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropSkinItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack stack = event.getItemDrop().getItemStack();
        if (SkinUtils.SKINNED.containsKey(uuid)) {
            Skin skin = SkinUtils.SKINNED.get(uuid);
            event.setCancelled(isSkinItem(skin.name(), stack));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSkinItemPlace(BlockPlaceEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (SkinUtils.SKINNED.containsKey(uuid)) {
            Skin skin = SkinUtils.SKINNED.get(uuid);
            event.setCancelled(isSkinPlaceable(skin.name(), event.getItemInHand()));
        }
    }

    private boolean isSkinItem(String skin, ItemStack is) {
        return (skin.equals("Cyberman") && is.getType() == Material.IRON_INGOT) || (skin.equals("Slitheen") && is.getType() == Material.TURTLE_EGG);
    }

    private boolean isSkinPlaceable(String skin, ItemStack is) {
        return (skin.equals("Angel of Liberty") && is.getType() == Material.TORCH) || (skin.equals("Slitheen") && is.getType() == Material.TURTLE_EGG);
    }
}
