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

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class TARDISLazarusMonstersInventory extends LazarusItems implements InventoryHolder, LazarusGUI {

    private final TARDIS plugin;
    private final Inventory inventory;
    private int maxSlot;

    public TARDISLazarusMonstersInventory(TARDIS plugin) {
        this.plugin = plugin;
        maxSlot = Monster.values().length - 1;
        plugin.debug("maxslot = " + maxSlot);
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
        for (Monster monster : Monster.values()) {
            if (!monster.equals(Monster.TOCLAFANE)) {
                ItemStack mon = ItemStack.of(monster.getMaterial(), 1);
                ItemMeta ster = mon.getItemMeta();
                ster.displayName(Component.text(monster.getName()));
                ster.addItemFlags(ItemFlag.values());
                ster.setAttributeModifiers(Multimaps.forMap(Map.of()));
                mon.setItemMeta(ster);
                stacks[i] = mon;
                i++;
            }
        }
        // add standard buttons
        addItems(plugin, stacks, 8);

        return stacks;
    }

    @Override
    public int getMaxSlot() {
        return maxSlot;
    }
}
