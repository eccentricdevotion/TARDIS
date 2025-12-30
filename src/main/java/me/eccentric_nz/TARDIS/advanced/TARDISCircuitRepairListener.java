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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISCircuitRepairListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<List<Float>, String> circuits = new HashMap<>();

    public TARDISCircuitRepairListener(TARDIS plugin) {
        this.plugin = plugin;
        circuits.put(CircuitVariant.ARS.getFloats(), "ars");
        circuits.put(CircuitVariant.CHAMELEON.getFloats(), "chameleon");
        circuits.put(CircuitVariant.INPUT.getFloats(), "input");
        circuits.put(CircuitVariant.INVISIBILITY.getFloats(), "invisibility");
        circuits.put(CircuitVariant.MATERIALISATION.getFloats(), "materialisation");
        circuits.put(CircuitVariant.MEMORY.getFloats(), "memory");
        circuits.put(CircuitVariant.RANDOM.getFloats(), "randomiser");
        circuits.put(CircuitVariant.SCANNER.getFloats(), "scanner");
        circuits.put(CircuitVariant.TEMPORAL.getFloats(), "temporal");
        circuits.put(CircuitVariant.TELEPATHIC.getFloats(), "telepathic");
        circuits.put(CircuitVariant.ARS_DAMAGED.getFloats(), "ars");
        circuits.put(CircuitVariant.CHAMELEON_DAMAGED.getFloats(), "chameleon");
        circuits.put(CircuitVariant.INPUT_DAMAGED.getFloats(), "input");
        circuits.put(CircuitVariant.INVISIBILITY_DAMAGED.getFloats(), "invisibility");
        circuits.put(CircuitVariant.MATERIALISATION_DAMAGED.getFloats(), "materialisation");
        circuits.put(CircuitVariant.MEMORY_DAMAGED.getFloats(), "memory");
        circuits.put(CircuitVariant.RANDOM_DAMAGED.getFloats(), "randomiser");
        circuits.put(CircuitVariant.SCANNER_DAMAGED.getFloats(), "scanner");
        circuits.put(CircuitVariant.TEMPORAL_DAMAGED.getFloats(), "temporal");
        circuits.put(CircuitVariant.TELEPATHIC_DAMAGED.getFloats(), "telepathic");
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(InventoryClickEvent event) {
        if (!event.getInventory().getType().equals(InventoryType.ANVIL)) {
            return;
        }
        // which slot?
        if (event.getRawSlot() != 2) {
            return;
        }
        // they clicked the output slot
        AnvilInventory anvil = (AnvilInventory) event.getInventory();
        ItemStack[] items = anvil.getContents();
        ItemStack first = items[0];
        // is it a glowstone dust with item meta?
        if (first == null || !first.getType().equals(Material.GLOWSTONE_DUST) || !first.hasItemMeta() || first.getAmount() != 1) {
            return;
        }
        // get the item meta
        ItemMeta fim = first.getItemMeta();
        if (!fim.hasDisplayName()) {
            return;
        }
        // get the display name
        String dnf = ComponentUtils.stripColour(fim.displayName());
        if (dnf.startsWith("TARDIS") && dnf.endsWith("Circuit") && fim.hasLore()) {
            // get the lore
            List<Component> flore = fim.lore();
            String stripped = ComponentUtils.stripColour(flore.get(1));
            if (stripped.equals("unlimited")) {
                return;
            }
            // get the uses left
            int left = TARDISNumberParsers.parseInt(stripped);
            // get max uses for this circuit
            CustomModelDataComponent component = fim.getCustomModelDataComponent();
            List<Float> floats = (!component.getFloats().isEmpty()) ? component.getFloats() : CircuitVariant.ARS.getFloats();
            int uses = plugin.getConfig().getInt("circuits.uses." + circuits.get(floats));
            // is it used?
            if (left >= uses) {
                return;
            }
            ItemStack two = items[1];
            // is it redstone?
            if (two == null || !two.getType().equals(Material.REDSTONE)) {
                return;
            }
            // how many in the stack?
            int amount = two.getAmount();
            int repair_max = uses - left;
            int repair_to = (amount > repair_max) ? uses : left + amount;
            int remaining = (amount > repair_max) ? amount - repair_max : 0;
            // clone the map
            ItemStack clone = first.clone();
            ItemMeta cim = clone.getItemMeta();
            List<Component> clore = new ArrayList<>();
            clore.add(Component.text("Uses left"));
            clore.add(Component.text(repair_to, NamedTextColor.YELLOW));
            cim.lore(clore);
            clone.setItemMeta(cim);
            // set the item in slot 0 to the new repaired map
            anvil.setItem(0, clone);
            // set the amount in slot 1
            if (remaining > 0) {
                anvil.setItem(1, ItemStack.of(Material.REDSTONE, remaining));
            } else {
                anvil.setItem(1, ItemStack.of(Material.AIR));
            }
        }
    }
}
