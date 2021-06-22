/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.advanced;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TardisCircuitRepairListener implements Listener {

    private final TardisPlugin plugin;
    private final HashMap<Integer, String> circuits = new HashMap<>();

    public TardisCircuitRepairListener(TardisPlugin plugin) {
        this.plugin = plugin;
        circuits.put(10001973, "ars");
        circuits.put(10001966, "chameleon");
        circuits.put(10001976, "input");
        circuits.put(10001981, "invisibility");
        circuits.put(10001964, "materialisation");
        circuits.put(10001975, "memory");
        circuits.put(10001980, "randomiser");
        circuits.put(10001977, "scanner");
        circuits.put(10001974, "temporal");
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(InventoryClickEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.ANVIL)) {
            return;
        }
        // which slot?
        int slot = event.getRawSlot();
        if (slot == 2) {
            // they clicked the output slot
            AnvilInventory anvilInventory = (AnvilInventory) event.getInventory();
            ItemStack[] itemStacks = anvilInventory.getContents();
            ItemStack first = itemStacks[0];
            // is it a redstone with item meta?
            if (first != null && first.getType().equals(Material.GLOWSTONE_DUST) && first.hasItemMeta() && first.getAmount() == 1) {
                // get the item meta
                ItemMeta firstItemMeta = first.getItemMeta();
                assert firstItemMeta != null;
                if (firstItemMeta.hasDisplayName() && firstItemMeta.hasCustomModelData()) {
                    // get the display name
                    String firstDisplayName = firstItemMeta.getDisplayName();
                    if (firstDisplayName.startsWith("TARDIS") && firstDisplayName.endsWith("Circuit")) {
                        if (firstItemMeta.hasLore()) {
                            // get the lore
                            List<String> firstLore = firstItemMeta.getLore();
                            assert firstLore != null;
                            String strippedLore = ChatColor.stripColor(firstLore.get(1));
                            if (!strippedLore.equals("unlimited")) {
                                // get the uses left
                                int left = TardisNumberParsers.parseInt(strippedLore);
                                // get max uses for this circuit
                                int customModelData = (firstItemMeta.hasCustomModelData()) ? firstItemMeta.getCustomModelData() : 10001963;
                                int uses = plugin.getConfig().getInt("circuits.uses." + circuits.get(customModelData));
                                // is it used?
                                if (left < uses) {
                                    ItemStack second = itemStacks[1];
                                    // is it redstone?
                                    if (second != null && second.getType().equals(Material.GLOWSTONE_DUST)) {
                                        // how many in the stack?
                                        int amount = second.getAmount();
                                        int repairMax = uses - left;
                                        int repairTo = (amount > repairMax) ? uses : left + amount;
                                        int remaining = (amount > repairMax) ? amount - repairMax : 0;
                                        // clone the map
                                        ItemStack clone = first.clone();
                                        ItemMeta cloneItemMeta = clone.getItemMeta();
                                        List<String> cloneLore = new ArrayList<>();
                                        cloneLore.add("Uses left");
                                        cloneLore.add(ChatColor.YELLOW + "" + repairTo);
                                        assert cloneItemMeta != null;
                                        cloneItemMeta.setLore(cloneLore);
                                        clone.setItemMeta(cloneItemMeta);
                                        // set the item in slot 0 to the new repaired map
                                        anvilInventory.setItem(0, clone);
                                        // set the amount in slot 1
                                        if (remaining > 0) {
                                            anvilInventory.setItem(1, new ItemStack(Material.REDSTONE, remaining));
                                        } else {
                                            anvilInventory.setItem(1, new ItemStack(Material.AIR));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
