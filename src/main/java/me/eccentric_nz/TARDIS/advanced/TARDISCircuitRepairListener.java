/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
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
public class TARDISCircuitRepairListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<Integer, String> circuits = new HashMap<>();

    public TARDISCircuitRepairListener(TARDIS plugin) {
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
            AnvilInventory anvil = (AnvilInventory) event.getInventory();
            ItemStack[] items = anvil.getContents();
            ItemStack first = items[0];
            // is it a redstone with item meta?
            if (first != null && first.getType().equals(Material.GLOWSTONE_DUST) && first.hasItemMeta() && first.getAmount() == 1) {
                // get the item meta
                ItemMeta fim = first.getItemMeta();
                if (fim.hasDisplayName() && fim.hasCustomModelData()) {
                    // get the display name
                    String dnf = fim.getDisplayName();
                    if (dnf.startsWith("TARDIS") && dnf.endsWith("Circuit")) {
                        if (fim.hasLore()) {
                            // get the lore
                            List<String> flore = fim.getLore();
                            String stripped = ChatColor.stripColor(flore.get(1));
                            if (!stripped.equals("unlimited")) {
                                // get the uses left
                                int left = TARDISNumberParsers.parseInt(stripped);
                                // get max uses for this circuit
                                int ctm = (fim.hasCustomModelData()) ? fim.getCustomModelData() : 10001963;
                                int uses = plugin.getConfig().getInt("circuits.uses." + circuits.get(ctm));
                                // is it used?
                                if (left < uses) {
                                    ItemStack two = items[1];
                                    // is it redstone?
                                    if (two != null && two.getType().equals(Material.GLOWSTONE_DUST)) {
                                        // how many in the stack?
                                        int amount = two.getAmount();
                                        int repair_max = uses - left;
                                        int repair_to = (amount > repair_max) ? uses : left + amount;
                                        int remaining = (amount > repair_max) ? amount - repair_max : 0;
                                        // clone the map
                                        ItemStack clone = first.clone();
                                        ItemMeta cim = clone.getItemMeta();
                                        List<String> clore = new ArrayList<>();
                                        clore.add("Uses left");
                                        clore.add(ChatColor.YELLOW + "" + repair_to);
                                        cim.setLore(clore);
                                        clone.setItemMeta(cim);
                                        // set the item in slot 0 to the new repaired map
                                        anvil.setItem(0, clone);
                                        // set the amount in slot 1
                                        if (remaining > 0) {
                                            anvil.setItem(1, new ItemStack(Material.REDSTONE, remaining));
                                        } else {
                                            anvil.setItem(1, new ItemStack(Material.AIR));
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
