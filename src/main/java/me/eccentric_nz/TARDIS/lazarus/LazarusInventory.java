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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIGeneticManipulator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * The Genetic Manipulation Device was invented by Professor Richard Lazarus. The machine would turn anyone inside
 * decades younger, but the process contained one side effect: genes that evolution rejected and left dormant would be
 * unlocked, transforming the human into a giant skeletal scorpion-like beast that fed off the lifeforce of living
 * creatures.
 *
 * @author eccentric_nz
 */
class LazarusInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    LazarusInventory(TARDIS plugin) {
        this.plugin = plugin;
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
        // passive
        ItemStack passive = ItemStack.of(GUIGeneticManipulator.BUTTON_PASSIVE.material(), 1);
        passive.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Passive Mobs"));
        stacks[GUIGeneticManipulator.BUTTON_PASSIVE.slot()] = passive;
        // neutral
        ItemStack neutral = ItemStack.of(GUIGeneticManipulator.BUTTON_NEUTRAL.material(), 1);
        neutral.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Neutral Mobs"));
        stacks[GUIGeneticManipulator.BUTTON_NEUTRAL.slot()] = neutral;
        // hostile
        ItemStack hostile = ItemStack.of(GUIGeneticManipulator.BUTTON_HOSTILE.material(), 1);
        hostile.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Hostile Mobs"));
        stacks[GUIGeneticManipulator.BUTTON_HOSTILE.slot()] = hostile;
        // hostile adjacent
        ItemStack adjacent = ItemStack.of(GUIGeneticManipulator.BUTTON_ADJACENT.material(), 1);
        adjacent.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Hostile Adjacent Mobs"));
        stacks[GUIGeneticManipulator.BUTTON_ADJACENT.slot()] = adjacent;
        // doctors
        ItemStack doctors = ItemStack.of(GUIGeneticManipulator.BUTTON_DOCTORS.material(), 1);
        doctors.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Doctors"));
        stacks[GUIGeneticManipulator.BUTTON_DOCTORS.slot()] = doctors;
        // companions
        ItemStack companions = ItemStack.of(GUIGeneticManipulator.BUTTON_COMPANIONS.material(), 1);
        companions.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Companions"));
        stacks[GUIGeneticManipulator.BUTTON_COMPANIONS.slot()] = companions;
        // characters
        ItemStack characters = ItemStack.of(GUIGeneticManipulator.BUTTON_CHARACTERS.material(), 1);
        characters.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Characters"));
        stacks[GUIGeneticManipulator.BUTTON_CHARACTERS.slot()] = characters;
        // TARDISWeepingAngels monsters
        ItemStack monsters = ItemStack.of(GUIGeneticManipulator.BUTTON_TWA.material(), 1);
        monsters.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Monsters"));
        stacks[GUIGeneticManipulator.BUTTON_TWA.slot()] = monsters;
        // master
        ItemStack master = ItemStack.of(GUIGeneticManipulator.BUTTON_MASTER.material(), 1);
        master.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MASTER", "The Master's reverse polarity button")));
        master.lore(List.of(Component.text(plugin.getLanguage().getString("SET_OFF", "OFF"))));
        master.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(List.of(152f))
                .build());
        stacks[GUIGeneticManipulator.BUTTON_MASTER.slot()] = master;
        // restore
        ItemStack restore = ItemStack.of(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        restore.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_RESTORE", "Restore my original genetic material")));
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = restore;
        // cancel
        ItemStack cancel = ItemStack.of(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        cancel.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_CANCEL", "Cancel")));
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = cancel;

        return stacks;
    }
}
