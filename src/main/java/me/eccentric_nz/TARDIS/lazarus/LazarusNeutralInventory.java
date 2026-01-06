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
package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The Genetic Manipulation Device was invented by Professor Richard Lazarus. The machine would turn anyone inside
 * decades younger, but the process contained one side effect: genes that evolution rejected and left dormant would be
 * unlocked, transforming the human into a giant skeletal scorpion-like beast that fed off the lifeforce of living
 * creatures.
 *
 * @author eccentric_nz
 */
class LazarusNeutralInventory extends LazarusItems implements InventoryHolder, LazarusGUI {

    private final TARDIS plugin;
    private final List<Material> neutral = new ArrayList<>();
    private final Inventory inventory;
    private int maxSlot;

    LazarusNeutralInventory(TARDIS plugin) {
        this.plugin = plugin;
        // maximum number of eggs is 45
        // neutral
        neutral.add(Material.BEE_SPAWN_EGG);
        neutral.add(Material.DOLPHIN_SPAWN_EGG);
        neutral.add(Material.FOX_SPAWN_EGG);
        neutral.add(Material.GOAT_SPAWN_EGG);
        neutral.add(Material.IRON_GOLEM_SPAWN_EGG);
        neutral.add(Material.LLAMA_SPAWN_EGG);
        neutral.add(Material.PANDA_SPAWN_EGG);
        neutral.add(Material.POLAR_BEAR_SPAWN_EGG);
        neutral.add(Material.PUFFERFISH_SPAWN_EGG);
        neutral.add(Material.TRADER_LLAMA_SPAWN_EGG);
        neutral.add(Material.WOLF_SPAWN_EGG);
        maxSlot = neutral.size();
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Genetic Manipulator", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Genetic Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        for (Material m : neutral) {
            ItemStack egg = ItemStack.of(m, 1);
            ItemMeta me = egg.getItemMeta();
            me.displayName(Component.text(m.toString().replace("_SPAWN_EGG", "")));
            egg.setItemMeta(me);
            stacks[i] = egg;
            i++;
        }
        // add standard buttons
        addItems(plugin, stacks, 2);

        return stacks;
    }

    @Override
    public int getMaxSlot() {
        return maxSlot;
    }
}
