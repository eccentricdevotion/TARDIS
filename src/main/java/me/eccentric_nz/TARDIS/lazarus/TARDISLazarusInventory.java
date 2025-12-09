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
import me.eccentric_nz.TARDIS.custommodels.GUIGeneticManipulator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

/**
 * The Genetic Manipulation Device was invented by Professor Richard Lazarus. The machine would turn anyone inside
 * decades younger, but the process contained one side effect: genes that evolution rejected and left dormant would be
 * unlocked, transforming the human into a giant skeletal scorpion-like beast that fed off the lifeforce of living
 * creatures.
 *
 * @author eccentric_nz
 */
class TARDISLazarusInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    TARDISLazarusInventory(TARDIS plugin) {
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
        ItemMeta passiveItemMeta = passive.getItemMeta();
        passiveItemMeta.displayName(Component.text("Passive Mobs"));
        passive.setItemMeta(passiveItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_PASSIVE.slot()] = passive;
        // neutral
        ItemStack neutral = ItemStack.of(GUIGeneticManipulator.BUTTON_NEUTRAL.material(), 1);
        ItemMeta neutralItemMeta = neutral.getItemMeta();
        neutralItemMeta.displayName(Component.text("Neutral Mobs"));
        neutral.setItemMeta(neutralItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_NEUTRAL.slot()] = neutral;
        // hostile
        ItemStack hostile = ItemStack.of(GUIGeneticManipulator.BUTTON_HOSTILE.material(), 1);
        ItemMeta hostileItemMeta = hostile.getItemMeta();
        hostileItemMeta.displayName(Component.text("Hostile Mobs"));
        hostile.setItemMeta(hostileItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_HOSTILE.slot()] = hostile;
        // hostile adjacent
        ItemStack adjacent = ItemStack.of(GUIGeneticManipulator.BUTTON_ADJACENT.material(), 1);
        ItemMeta adjacentItemMeta = adjacent.getItemMeta();
        adjacentItemMeta.displayName(Component.text("Hostile Adjacent Mobs"));
        adjacent.setItemMeta(adjacentItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_ADJACENT.slot()] = adjacent;
        // doctors
        ItemStack doctors = ItemStack.of(GUIGeneticManipulator.BUTTON_DOCTORS.material(), 1);
        ItemMeta doctorsItemMeta = doctors.getItemMeta();
        doctorsItemMeta.displayName(Component.text("Doctors"));
        doctors.setItemMeta(doctorsItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_DOCTORS.slot()] = doctors;
        // companions
        ItemStack companions = ItemStack.of(GUIGeneticManipulator.BUTTON_COMPANIONS.material(), 1);
        ItemMeta companionsItemMeta = companions.getItemMeta();
        companionsItemMeta.displayName(Component.text("Companions"));
        companions.setItemMeta(companionsItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_COMPANIONS.slot()] = companions;
        // characters
        ItemStack characters = ItemStack.of(GUIGeneticManipulator.BUTTON_CHARACTERS.material(), 1);
        ItemMeta charactersItemMeta = characters.getItemMeta();
        charactersItemMeta.displayName(Component.text("Characters"));
        characters.setItemMeta(charactersItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_CHARACTERS.slot()] = characters;
        // TARDISWeepingAngels monsters
        ItemStack monsters = ItemStack.of(GUIGeneticManipulator.BUTTON_TWA.material(), 1);
        ItemMeta monstersItemMeta = monsters.getItemMeta();
        monstersItemMeta.displayName(Component.text("Monsters"));
        monsters.setItemMeta(monstersItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_TWA.slot()] = monsters;
        // master
        ItemStack master = ItemStack.of(GUIGeneticManipulator.BUTTON_MASTER.material(), 1);
        ItemMeta masterItemMeta = master.getItemMeta();
        masterItemMeta.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MASTER", "The Master's reverse polarity button")));
        masterItemMeta.lore(List.of(Component.text(plugin.getLanguage().getString("SET_OFF", "OFF"))));
        CustomModelDataComponent component = masterItemMeta.getCustomModelDataComponent();
        component.setFloats(List.of(152f));
        masterItemMeta.setCustomModelDataComponent(component);
        master.setItemMeta(masterItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_MASTER.slot()] = master;
        // restore
        ItemStack restore = ItemStack.of(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        ItemMeta restoreItemMeta = restore.getItemMeta();
        restoreItemMeta.displayName(Component.text(plugin.getLanguage().getString("BUTTON_RESTORE", "Restore my original genetic material")));
        restore.setItemMeta(restoreItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = restore;
        // cancel
        ItemStack cancel = ItemStack.of(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        ItemMeta cancelItemMeta = cancel.getItemMeta();
        cancelItemMeta.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CANCEL", "Cancel")));
        cancel.setItemMeta(cancelItemMeta);
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = cancel;

        return stacks;
    }
}
