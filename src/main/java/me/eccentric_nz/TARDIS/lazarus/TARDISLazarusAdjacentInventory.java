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
class TARDISLazarusAdjacentInventory extends LazarusItems implements InventoryHolder, LazarusGUI {

    private final TARDIS plugin;
    private final List<Material> adjacent = new ArrayList<>();
    private final Inventory inventory;
    private int maxSlot;

    TARDISLazarusAdjacentInventory(TARDIS plugin) {
        this.plugin = plugin;
        // maximum number of eggs is 45
        // passive - hostile-adjacent
        adjacent.add(Material.CAMEL_HUSK_SPAWN_EGG);
        adjacent.add(Material.SKELETON_HORSE_SPAWN_EGG);
        adjacent.add(Material.ZOMBIE_HORSE_SPAWN_EGG);

        // neutral - hostile adjacent
        adjacent.add(Material.CAVE_SPIDER_SPAWN_EGG);
        adjacent.add(Material.DROWNED_SPAWN_EGG);
        adjacent.add(Material.ENDERMAN_SPAWN_EGG);
        adjacent.add(Material.PIGLIN_SPAWN_EGG);
        adjacent.add(Material.SPIDER_SPAWN_EGG);
        adjacent.add(Material.ZOMBIE_NAUTILUS_SPAWN_EGG);
        adjacent.add(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG);

        // unused
        /*
        Giant
        Illusioner
        Herobrine
         */
        maxSlot = adjacent.size() + 3;
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
        for (Material m : adjacent) {
            ItemStack egg = ItemStack.of(m, 1);
            ItemMeta me = egg.getItemMeta();
            me.displayName(Component.text(m.toString().replace("_SPAWN_EGG", "")));
            egg.setItemMeta(me);
            stacks[i] = egg;
            i++;
        }
        // giant
        ItemStack gi = ItemStack.of(Material.ZOMBIE_HEAD, 1);
        ItemMeta ant = gi.getItemMeta();
        ant.displayName(Component.text("GIANT"));
        gi.setItemMeta(ant);
        stacks[i] = gi;
        i++;
        // illusioner
        ItemStack ill = ItemStack.of(Material.BOWL, 1);
        ItemMeta usi = ill.getItemMeta();
        usi.displayName(Component.text("ILLUSIONER"));
        ill.setItemMeta(usi);
        stacks[i] = ill;
        i++;
        // herobrine
        ItemStack hero = ItemStack.of(Material.PLAYER_HEAD, 1);
        ItemMeta brine = hero.getItemMeta();
        brine.displayName(Component.text("HEROBRINE"));
        hero.setItemMeta(brine);
        stacks[i] = hero;
        // add standard buttons
        addItems(plugin, stacks, 4);

        return stacks;
    }

    @Override
    public int getMaxSlot() {
        return maxSlot;
    }
}
