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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISAnvilListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<String, Material> disallow = new HashMap<>();

    /**
     * Prevents renaming TARDIS items
     *
     * @param plugin an instance of the TARDIS plugin
     */
    public TARDISAnvilListener(TARDIS plugin) {
        this.plugin = plugin;
        plugin.getFigura().getShapedRecipes().forEach((key, value) -> disallow.put(key, value.getResult().getType()));
        plugin.getIncomposita().getShapelessRecipes().forEach((key, value) -> disallow.put(key, value.getResult().getType()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(InventoryClickEvent event) {
        Inventory inv = event.getView().getTopInventory();
        if (inv instanceof AnvilInventory) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            // slot 2 = result item slot
            if (slot == 2) {
                ItemStack is = event.getCurrentItem();
                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    ItemStack one = inv.getItem(0);
                    ItemStack two = inv.getItem(1);
                    if (checkRepair(one, two) && im.hasDisplayName() && disallow.containsKey(ComponentUtils.stripColour(im.displayName())) && is.getType() == disallow.get(ComponentUtils.stripColour(im.displayName()))) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_RENAME");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private boolean checkRepair(ItemStack one, ItemStack two) {
        if (two == null) {
            return true;
        }
        if (!one.hasItemMeta() || !two.hasItemMeta()) {
            return true;
        }
        ItemMeta im_one = one.getItemMeta();
        ItemMeta im_two = two.getItemMeta();
        if (!im_one.hasDisplayName() || !im_two.hasDisplayName()) {
            return true;
        }
        String dn_one = ComponentUtils.stripColour(im_one.displayName());
        String dn_two = ComponentUtils.stripColour(im_two.displayName());
        return !dn_one.equals(dn_two);
    }
}
