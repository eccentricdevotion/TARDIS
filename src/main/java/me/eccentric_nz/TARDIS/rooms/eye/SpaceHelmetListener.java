/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.utility.ComponentUtils;
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
public class SpaceHelmetListener implements Listener {

    public static boolean isSpaceHelmet(ItemStack helmet) {
        if (helmet == null || helmet.getType() != Material.GLASS || !helmet.hasItemMeta()) {
            return false;
        }
        ItemMeta im = helmet.getItemMeta();
        if (!im.hasDisplayName() || !im.hasMaxStackSize()) {
            return false;
        }
        return ComponentUtils.endsWith(im.displayName(), "TARDIS Space Helmet") && im.getMaxStackSize() == 1;
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
            player.getInventory().setItemInMainHand(ItemStack.of(Material.AIR));
        }
    }
}
