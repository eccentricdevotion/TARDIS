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
package me.eccentric_nz.TARDIS.sonic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUISonicGenerator;
import me.eccentric_nz.TARDIS.database.data.Sonic;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
class SonicGeneratorInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Sonic data;
    private final Inventory inventory;

    public SonicGeneratorInventory(TARDIS plugin, Sonic data) {
        this.plugin = plugin;
        this.data = data;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Sonic Generator", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Sonic Generator Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[54];
        for (GUISonicGenerator sonic : GUISonicGenerator.values()) {
            if (sonic.getMaterial() == Material.BLAZE_ROD) {
                ItemStack is = ItemStack.of(Material.BLAZE_ROD, 1);
                is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Sonic Screwdriver"));
                is.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(sonic.getName())).build());
                is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                                .addFloats(sonic.getFloats())
                        .build());
                stack[sonic.getSlot()] = is;
            }
            if (sonic.getMaterial() == Material.BOWL && sonic.getSlot() != 45) {
                ItemStack is = ItemStack.of(Material.BOWL, 1);
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(sonic.getName()));
                if (!sonic.getLore().isEmpty()) {
                    ItemLore.Builder lore = ItemLore.lore();
                    for (String s : sonic.getLore().split("~")) {
                        lore.addLine(Component.text(s));
                    }
                    is.setData(DataComponentTypes.LORE, lore.build());
                }
                stack[sonic.getSlot()] = is;
            }
        }
        // info 1/3
        ItemStack info = ItemStack.of(Material.BOOK, 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Instructions (1/3)"));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Select your Sonic Screwdriver"),
                Component.text("type from the top two rows."),
                Component.text("Click on the upgrades you"),
                Component.text("want to add to the sonic.")
        )));
        stack[38] = info;
        // info 2/3
        ItemStack info1 = ItemStack.of(Material.BOOK, 1);
        info1.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Instructions (2/3)"));
        info1.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("You can reset the upgrades"),
                Component.text("by clicking the 'Standard' button."),
                Component.text("The Artron cost for the"),
                Component.text("sonic is shown bottom left.")
        )));
        stack[39] = info1;
        // info 3/3
        ItemStack info2 = ItemStack.of(Material.BOOK, 1);
        info2.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Instructions (3/3)"));
        info2.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("The final sonic result"),
                Component.text("is shown in the middle"),
                Component.text("of the bottom row.")
        )));
        stack[40] = info2;
        // players preferred sonic
        ItemStack sonic = ItemStack.of(Material.BLAZE_ROD, 1);
        sonic.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Sonic Screwdriver"));
        sonic.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                        .addFloats(data.getModel())
                .build());
        List<Component> upgrades = new ArrayList<>();
        double full = plugin.getArtronConfig().getDouble("full_charge") / 100.0d;
        int artron = (int) (plugin.getArtronConfig().getDouble("sonic_generator.standard") * full);
        if (data.hasBio()) {
            upgrades.add(Component.text("Bio-scanner Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full);
        }
        if (data.hasDiamond()) {
            upgrades.add(Component.text("Diamond Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.diamond") * full);
        }
        if (data.hasEmerald()) {
            upgrades.add(Component.text("Emerald Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.emerald") * full);
        }
        if (data.hasRedstone()) {
            upgrades.add(Component.text("Redstone Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.redstone") * full);
        }
        if (data.hasPainter()) {
            upgrades.add(Component.text("Painter Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.painter") * full);
        }
        if (data.hasIgnite()) {
            upgrades.add(Component.text("Ignite Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.ignite") * full);
        }
        if (data.hasArrow()) {
            upgrades.add(Component.text("Pickup Arrows Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.arrow") * full);
        }
        if (data.hasKnockback()) {
            upgrades.add(Component.text("Knockback Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.knockback") * full);
        }
        if (data.hasBrush()) {
            upgrades.add(Component.text("Brush Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.brush") * full);
        }
        if (data.hasConversion()) {
            upgrades.add(Component.text("Conversion Upgrade"));
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.conversion") * full);
        }
        // cost
        ItemStack cost = ItemStack.of(Material.BOWL, 1);
        cost.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Artron cost"));
        cost.lore(List.of(Component.text(artron)));
        stack[45] = cost;

        if (!upgrades.isEmpty()) {
            List<Component> finalUps = new ArrayList<>();
            finalUps.add(Component.text("Upgrades:"));
            finalUps.addAll(upgrades);
            sonic.setData(DataComponentTypes.LORE, ItemLore.lore(finalUps));
        }
        stack[49] = sonic;

        return stack;
    }
}
