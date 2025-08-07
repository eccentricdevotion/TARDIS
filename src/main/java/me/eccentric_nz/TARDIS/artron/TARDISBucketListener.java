package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISBucketListener implements Listener {

    private final TARDIS plugin;

    public TARDISBucketListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickArtronBucket(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        // prevent filling artron storage cells & capacitors with water / lava
        ItemStack bucket = event.getItem();
        if (bucket == null) {
            return;
        }
        if (bucket.getType() != Material.BUCKET) {
            return;
        }
        Block block = event.getPlayer().getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16);
        Material material = block.getRelative(event.getBlockFace()).getType();
        if (material != Material.LAVA && material != Material.WATER) {
            return;
        }
        ItemMeta im = bucket.getItemMeta();
        if (im.hasDisplayName()) {
            if (ComponentUtils.endsWith(im.displayName(), "Artron Storage Cell") || ComponentUtils.endsWith(im.displayName(), "Artron Capacitor")) {
                event.setCancelled(true);
                plugin.getMessenger().send(event.getPlayer(), TardisModule.TARDIS, "ARTRON_FILL", material.toString());
            }
        }
    }

    @EventHandler
    public void onMilkArtronClick(PlayerInteractAtEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        // prevent filling artron storage cells & capacitors with milk
        ItemStack bucket = event.getPlayer().getInventory().getItemInMainHand();
        if (bucket.getType() != Material.BUCKET) {
            return;
        }
        EntityType entityType = event.getRightClicked().getType();
        if (entityType != EntityType.COW && entityType != EntityType.GOAT && entityType != EntityType.MOOSHROOM) {
            return;
        }
        ItemMeta im = bucket.getItemMeta();
        if (im.hasDisplayName()) {
            if (ComponentUtils.endsWith(im.displayName(), "Artron Storage Cell") || ComponentUtils.endsWith(im.displayName(), "Artron Capacitor")) {
                event.setCancelled(true);
                plugin.getMessenger().send(event.getPlayer(), TardisModule.TARDIS, "ARTRON_FILL", "MILK");
            }
        }
    }
}
