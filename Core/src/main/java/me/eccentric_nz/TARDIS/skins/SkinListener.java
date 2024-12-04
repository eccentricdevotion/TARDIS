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

import java.util.UUID;

public class SkinListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropAreaDisk(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack stack = event.getItemDrop().getItemStack();
        if (SkinUtils.SKINNED.containsKey(uuid) && SkinExtras.MATERIALS.contains(stack.getType())) {
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
        if (!SkinExtras.MATERIALS.contains(stack.getType())) {
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
            if (SkinUtils.SKINNED.containsKey(uuid) && SkinExtras.MATERIALS.contains(current.getType())) {
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
            if (is != null && SkinExtras.MATERIALS.contains(is.getType())) {
                event.setCancelled(isSkinItem(skin.name(), is));
            }
            ItemStack off = event.getOffHandItem();
            if (off != null && SkinExtras.MATERIALS.contains(off.getType())) {
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
