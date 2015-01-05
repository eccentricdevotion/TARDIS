/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.artron;

import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISArtronFurnaceListener implements Listener {

    private final TARDIS plugin;
//    private final short cookTime;

    public TARDISArtronFurnaceListener(TARDIS plugin) {
        this.plugin = plugin;
//        this.cookTime = (short) this.plugin.getArtronConfig().getInt("artron_cook_time");
    }

    @EventHandler(ignoreCancelled = true)
    public void onArtronFurnaceBurn(FurnaceBurnEvent event) {
        final Furnace furnace = (Furnace) event.getBlock().getState();
        if (furnace.getInventory().getTitle().equals("TARDIS Artron Furnace")) {
            final ItemStack is = event.getFuel().clone();
            if (is.hasItemMeta()) {
                final ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName() && im.getDisplayName().equals("Artron Storage Cell")) {
                    final List<String> lore = im.getLore();
                    if (!lore.get(1).equals("0")) {
                        int charge_level = plugin.getUtils().parseInt(lore.get(1));
                        // 1 coal = 1600 burn time, and 1 coal = 8 artron, then burn time = charge level * 200
                        // if full_charge = 5000, then burn time = 1,000,000!
                        int burnTime = charge_level * 20;
                        event.setBurnTime(burnTime);
//                        furnace.setCookTime(cookTime);
                        // return an empty cell
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                lore.set(1, "0");
                                im.setLore(lore);
                                is.setItemMeta(im);
                                is.removeEnchantment(Enchantment.DURABILITY);
                                furnace.getInventory().setItem(1, is);
                            }
                        }, 2L);
                    }
                }
            }
        }
    }

//    @EventHandler
//    public void furnaceSmeltEvent(FurnaceSmeltEvent event) {
//        // Setting cookTime after cooking an item (and the fuel is still burning)
//        Furnace furnace = (Furnace) event.getBlock().getState();
//        if (furnace.getInventory().getTitle().equals("TARDIS Artron Furnace")) {
//            furnace.setCookTime(cookTime);
//        }
//    }
//
//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent event) {
//        if (!(event.getInventory() instanceof FurnaceInventory)) {
//            return;
//        }
//        Furnace furnace = (Furnace) (event.getWhoClicked().getTargetBlock(null, 10)).getState();
//        // Setting cookTime when the furnace is empty but already burning
//        if (furnace.getInventory().getTitle().equals("TARDIS Artron Furnace") && (event.getSlot() == 0 || event.getSlot() == 1) // Click in one of the two slots
//                && event.getCursor().getType() != Material.AIR // With an item
//                && furnace.getCookTime() > cookTime) {         // The furnace is not already burning something
//            furnace.setCookTime(cookTime);
//        }
//    }
    @EventHandler(ignoreCancelled = true)
    public void onArtronFurnacePlace(BlockPlaceEvent event) {
        if (!event.getBlock().getType().equals(Material.FURNACE)) {
            return;
        }
        if (!event.getItemInHand().hasItemMeta()) {
            return;
        }
        if (!event.getItemInHand().getItemMeta().hasDisplayName()) {
            return;
        }
        if (!event.getItemInHand().getItemMeta().getDisplayName().equals("TARDIS Artron Furnace")) {
            return;
        }
        plugin.getTardisHelper().nameFurnaceGUI(event.getBlock(), "TARDIS Artron Furnace");
    }

    @EventHandler(ignoreCancelled = true)
    public void onArtronFurnaceBreak(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.FURNACE)) {
            return;
        }
        Furnace furnace = (Furnace) event.getBlock().getState();
        if (furnace.getInventory().getTitle().equals("TARDIS Artron Furnace")) {
            event.setCancelled(true);
            ItemStack is = new ItemStack(Material.FURNACE, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("TARDIS Artron Furnace");
            is.setItemMeta(im);
            Location l = event.getBlock().getLocation();
            event.getBlock().setType(Material.AIR);
            event.getBlock().getWorld().dropItemNaturally(l, is);
        }
    }
}
