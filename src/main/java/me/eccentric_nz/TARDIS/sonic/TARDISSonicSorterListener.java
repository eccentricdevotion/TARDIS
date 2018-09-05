/*
 * Copyright (c) 2018 Shadow1013GL, Pyr0Byt3, pendo324
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.eccentric_nz.TARDIS.sonic;

import me.crafter.mc.lockettepro.LocketteProAPI;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISTownyChecker;
import nl.rutgerkok.blocklocker.BlockLockerAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.yi.acru.bukkit.Lockette.Lockette;

import java.util.Arrays;

/**
 * Borrowed from the SimpleSort plugin. http://dev.bukkit.org/bukkit-plugins/simplesort/
 *
 * @author Shadow1013GL
 * @author Pyr0Byt3
 * @author pendo324
 */
public class TARDISSonicSorterListener implements Listener {

    private final TARDIS plugin;

    public TARDISSonicSorterListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && player.hasPermission("tardis.sonic.sort")) {
            ItemStack is = player.getInventory().getItemInMainHand();
            if (is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasDisplayName() && ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver")) {
                    Block block = event.getClickedBlock();
                    if (block != null && (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)) {
                        boolean allow = true;
                        // is Lockette on the server?
                        if (plugin.getPM().isPluginEnabled("Lockette")) {
                            if (Lockette.isProtected(block)) {
                                allow = false;
                            }
                        }
                        if (plugin.getPM().isPluginEnabled("LockettePro")) {
                            if (LocketteProAPI.isProtected(block)) {
                                allow = false;
                            }
                        }
                        if (plugin.getPM().isPluginEnabled("BlockLocker")) {
                            if (BlockLockerAPI.isProtected(block)) {
                                allow = false;
                            }
                        }
                        if (plugin.getPM().isPluginEnabled("Towny")) {
                            allow = new TARDISTownyChecker(plugin, true).checkTowny(player, block.getLocation());
                        }
                        if (allow) {
                            Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
                            sortInventory(inventory);
                            TARDISMessage.send(player, "CHEST_SORTED");
                        }
                    }
                }
            }
        }
    }

    public static ItemStack[] sortInventory(ItemStack[] items) {
        int endIndex = items.length;
        for (int i = 0; i < endIndex; i++) {
            ItemStack item1 = items[i];
            if (item1 == null) {
                continue;
            }
            int maxStackSize = item1.getMaxStackSize();
            if (item1.getAmount() <= 0 || maxStackSize == 1) {
                continue;
            }
            if (item1.getAmount() < maxStackSize) {
                int needed = maxStackSize - item1.getAmount();
                for (int j = i + 1; j < endIndex; j++) {
                    ItemStack item2 = items[j];
                    if (item2 == null || item2.getAmount() <= 0 || maxStackSize == 1) {
                        continue;
                    }
                    if (item2.getType() == item1.getType()) {
                        if (item1.getItemMeta() instanceof Damageable) {
                            Damageable d1 = (Damageable) item1.getItemMeta();
                            Damageable d2 = (Damageable) item2.getItemMeta();
                            if (d1.getDamage() == d2.getDamage() && item1.getEnchantments().equals(item2.getEnchantments()) && item1.getItemMeta().equals(item2.getItemMeta())) {
                                if (item2.getAmount() > needed) {
                                    item1.setAmount(maxStackSize);
                                    item2.setAmount(item2.getAmount() - needed);
                                    break;
                                } else {
                                    items[j] = null;
                                    item1.setAmount(item1.getAmount() + item2.getAmount());
                                    needed = maxStackSize - item1.getAmount();
                                }
                            }
                        } else {
                            if (item1.getEnchantments().equals(item2.getEnchantments()) && item1.getItemMeta().equals(item2.getItemMeta())) {
                                if (item2.getAmount() > needed) {
                                    item1.setAmount(maxStackSize);
                                    item2.setAmount(item2.getAmount() - needed);
                                    break;
                                } else {
                                    items[j] = null;
                                    item1.setAmount(item1.getAmount() + item2.getAmount());
                                    needed = maxStackSize - item1.getAmount();
                                }
                            }
                        }
                    }
                }
            }
        }
        Arrays.sort(items, 0, endIndex, new TARDISItemComparator());
        return items;
    }

    public static void sortInventory(Inventory inventory) {
        inventory.setContents(sortInventory(inventory.getContents()));
    }
}
