/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TVMGUICommon {

    private final TARDISVortexManipulator plugin;

    public TVMGUICommon(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    public void close(Player p) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p.closeInventory();
        }, 1L);
    }

    public int getPageNumber(InventoryView view) {
        ItemStack is = view.getItem(45);
        ItemMeta im = is.getItemMeta();
        String[] split = im.getDisplayName().split(" ");
        int page = TARDISNumberParsers.parseInt(split[1]);
        return page;
    }
}
