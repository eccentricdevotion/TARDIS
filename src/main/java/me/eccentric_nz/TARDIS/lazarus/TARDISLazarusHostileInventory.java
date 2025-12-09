/*
 * Copyright (C) 2025 eccentric_nz
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
class TARDISLazarusHostileInventory extends LazarusItems implements InventoryHolder, LazarusGUI {

    private final TARDIS plugin;
    private final List<Material> hostile = new ArrayList<>();
    private final Inventory inventory;
    private int maxSlot;

    TARDISLazarusHostileInventory(TARDIS plugin) {
        this.plugin = plugin;
        // hostile
        hostile.add(Material.BLAZE_SPAWN_EGG);
        hostile.add(Material.BOGGED_SPAWN_EGG);
        hostile.add(Material.BREEZE_SPAWN_EGG);
        hostile.add(Material.CREAKING_SPAWN_EGG);
        hostile.add(Material.CREEPER_SPAWN_EGG);
        hostile.add(Material.ELDER_GUARDIAN_SPAWN_EGG);
        hostile.add(Material.ENDERMITE_SPAWN_EGG);
        hostile.add(Material.EVOKER_SPAWN_EGG);
        hostile.add(Material.GHAST_SPAWN_EGG);
        hostile.add(Material.GUARDIAN_SPAWN_EGG);
        hostile.add(Material.HOGLIN_SPAWN_EGG);
        hostile.add(Material.HUSK_SPAWN_EGG);
        hostile.add(Material.MAGMA_CUBE_SPAWN_EGG);
        hostile.add(Material.PARCHED_SPAWN_EGG);
        hostile.add(Material.PHANTOM_SPAWN_EGG);
        hostile.add(Material.PIGLIN_BRUTE_SPAWN_EGG);
        hostile.add(Material.PILLAGER_SPAWN_EGG);
        hostile.add(Material.RAVAGER_SPAWN_EGG);
        hostile.add(Material.SHULKER_SPAWN_EGG);
        hostile.add(Material.SILVERFISH_SPAWN_EGG);
        hostile.add(Material.SKELETON_SPAWN_EGG);
        hostile.add(Material.SLIME_SPAWN_EGG);
        hostile.add(Material.STRAY_SPAWN_EGG);
        hostile.add(Material.VEX_SPAWN_EGG);
        hostile.add(Material.VINDICATOR_SPAWN_EGG);
        hostile.add(Material.WARDEN_SPAWN_EGG);
        hostile.add(Material.WITCH_SPAWN_EGG);
        hostile.add(Material.WITHER_SKELETON_SPAWN_EGG);
        hostile.add(Material.ZOGLIN_SPAWN_EGG);
        hostile.add(Material.ZOMBIE_SPAWN_EGG);
        hostile.add(Material.ZOMBIE_VILLAGER_SPAWN_EGG);
        // boss
        hostile.add(Material.ENDER_DRAGON_SPAWN_EGG);
        hostile.add(Material.WITHER_SPAWN_EGG);
        maxSlot = hostile.size();
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
        // hostile & boss
        for (Material m : hostile) {
            ItemStack egg = ItemStack.of(m, 1);
            ItemMeta me = egg.getItemMeta();
            me.displayName(Component.text(m.toString().replace("_SPAWN_EGG", "")));
            egg.setItemMeta(me);
            stacks[i] = egg;
            i++;
        }
        // add standard buttons
        addItems(plugin, stacks, 3);

        return stacks;
    }

    @Override
    public int getMaxSlot() {
        return maxSlot;
    }
}
