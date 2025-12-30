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
package me.eccentric_nz.TARDIS.rooms.library;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibrarySorter {

    private final TARDIS plugin;

    public LibrarySorter(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void distribute(Inventory inventory, Location start) {
        List<ItemStack> leftovers = new ArrayList<>();
        for (ItemStack is : inventory.getContents()) {
            if (is == null) {
                continue;
            }
            if (is.getType() == Material.ENCHANTED_BOOK && is.hasItemMeta()) {
                EnchantmentStorageMeta esm = (EnchantmentStorageMeta) is.getItemMeta();
                Map<Enchantment, Integer> enchantments = esm.getStoredEnchants();
                if (!enchantments.isEmpty()) {
                    // store the book on the outside shelves
                    Map.Entry<Enchantment, Integer> entry = enchantments.entrySet().iterator().next();
                    Enchantment enchantment = entry.getKey();
                    int level = entry.getValue();
                    // get the shelf based on the first enchantment
                    EnchantmentShelf shelf = EnchantmentShelf.BY_ENCHANTMENT.get(enchantment);
                    Block block = start.clone().add(shelf.getPosition()).getBlock().getRelative(BlockFace.UP, level);
                    if (block.getType() == Material.CHISELED_BOOKSHELF) {
                        ChiseledBookshelf cbs = (ChiseledBookshelf) block.getState();
                        HashMap<Integer, ItemStack> add = cbs.getInventory().addItem(is);
                        if (!add.isEmpty()) {
                            leftovers.add(is);
                        }
                        inventory.remove(is);
                    }
                }
            } else if (isBook(is.getType())) {
                // store the book in the centre
                EnchantmentShelf shelf;
                switch (is.getType()) {
                    case BOOK -> shelf = EnchantmentShelf.BOOKS;
                    case WRITABLE_BOOK -> shelf = EnchantmentShelf.BOOK_AND_QUILLS;
                    case WRITTEN_BOOK -> shelf = EnchantmentShelf.WRITTEN_BOOKS;
                    // KNOWLEDGE_BOOK
                    default -> shelf = EnchantmentShelf.KNOWLEDGE_BOOKS;
                }
                Block block = start.clone().add(shelf.getPosition()).getBlock().getRelative(BlockFace.UP);
                if (block.getType() == Material.CHISELED_BOOKSHELF) {
                    ChiseledBookshelf cbs = (ChiseledBookshelf) block.getState();
                    int l = 1;
                    while (cbs.getInventory().firstEmpty() == -1 && l < 4) {
                        cbs = (ChiseledBookshelf) block.getRelative(BlockFace.UP, l).getState();
                        l++;
                    }
                    HashMap<Integer, ItemStack> add = cbs.getInventory().addItem(is);
                    if (!add.isEmpty()) {
                        leftovers.add(is);
                    }
                    inventory.remove(is);
                }
            }
        }
        // put any leftovers back
        if (!leftovers.isEmpty()) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                for (ItemStack stack : leftovers) {
                    inventory.addItem(stack);
                }
            }, 2L);
        }
    }

    private boolean isBook(Material material) {
        switch (material) {
            case BOOK, WRITABLE_BOOK, WRITTEN_BOOK, KNOWLEDGE_BOOK -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
