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
class TARDISLazarusPassiveInventory extends LazarusItems implements InventoryHolder, LazarusGUI {

    private final TARDIS plugin;
    private final List<Material> passive = new ArrayList<>();
    private final Inventory inventory;
    private int maxSlot;

    TARDISLazarusPassiveInventory(TARDIS plugin) {
        this.plugin = plugin;
        // maximum number of eggs is 45
        // passive
        passive.add(Material.ALLAY_SPAWN_EGG);
        passive.add(Material.ARMADILLO_SPAWN_EGG);
        passive.add(Material.AXOLOTL_SPAWN_EGG);
        passive.add(Material.BAT_SPAWN_EGG);
        passive.add(Material.CAMEL_SPAWN_EGG);
        passive.add(Material.CAT_SPAWN_EGG);
        passive.add(Material.CHICKEN_SPAWN_EGG);
        passive.add(Material.COD_SPAWN_EGG);
        passive.add(Material.COPPER_GOLEM_SPAWN_EGG);
        passive.add(Material.COW_SPAWN_EGG);
        passive.add(Material.DONKEY_SPAWN_EGG);
        passive.add(Material.FROG_SPAWN_EGG);
        passive.add(Material.GLOW_SQUID_SPAWN_EGG);
        passive.add(Material.HAPPY_GHAST_SPAWN_EGG);
        passive.add(Material.HORSE_SPAWN_EGG);
        passive.add(Material.MOOSHROOM_SPAWN_EGG);
        passive.add(Material.MULE_SPAWN_EGG);
        passive.add(Material.NAUTILUS_SPAWN_EGG);
        passive.add(Material.OCELOT_SPAWN_EGG);
        passive.add(Material.PARROT_SPAWN_EGG);
        passive.add(Material.PIG_SPAWN_EGG);
        passive.add(Material.RABBIT_SPAWN_EGG);
        passive.add(Material.SALMON_SPAWN_EGG);
        passive.add(Material.SHEEP_SPAWN_EGG);
        passive.add(Material.SNIFFER_SPAWN_EGG);
        passive.add(Material.SNOW_GOLEM_SPAWN_EGG);
        passive.add(Material.SQUID_SPAWN_EGG);
        passive.add(Material.STRIDER_SPAWN_EGG);
        passive.add(Material.TADPOLE_SPAWN_EGG);
        passive.add(Material.TROPICAL_FISH_SPAWN_EGG);
        passive.add(Material.TURTLE_SPAWN_EGG);
        passive.add(Material.VILLAGER_SPAWN_EGG);
        passive.add(Material.WANDERING_TRADER_SPAWN_EGG);
        maxSlot = passive.size();
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
        for (Material m : passive) {
            ItemStack egg = ItemStack.of(m, 1);
            ItemMeta me = egg.getItemMeta();
            if (m == Material.MOOSHROOM_SPAWN_EGG) {
                me.displayName(Component.text("MUSHROOM_COW"));
            } else {
                me.displayName(Component.text(m.toString().replace("_SPAWN_EGG", "")));
            }
            egg.setItemMeta(me);
            stacks[i] = egg;
            i++;
        }
        // add standard buttons
        addItems(plugin, stacks, 1);

        return stacks;
    }

    @Override
    public int getMaxSlot() {
        return maxSlot;
    }
}
