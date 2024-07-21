/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.rooms.eye;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TARDISSpaceHelmetListener implements Listener {

    public static boolean isSpaceHelmet(ItemStack helmet) {
        if (helmet == null || helmet.getType() != Material.GLASS || !helmet.hasItemMeta()) {
            return false;
        }
        ItemMeta im = helmet.getItemMeta();
        if (!im.hasDisplayName() || !im.hasCustomModelData() || !im.hasMaxStackSize()) {
            return false;
        }
        return im.getDisplayName().equals("TARDIS Space Helmet") && im.getCustomModelData() == 5 && im.getMaxStackSize() == 1;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpaceHelmetInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (isSpaceHelmet(is) && player.getInventory().getHelmet() == null) {
            player.getInventory().setHelmet(is);
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }
    }
}
