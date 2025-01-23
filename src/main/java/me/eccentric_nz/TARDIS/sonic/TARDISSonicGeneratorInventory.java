/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUISonicGenerator;
import me.eccentric_nz.TARDIS.database.data.Sonic;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISSonicGeneratorInventory {

    private final TARDIS plugin;
    private final Sonic data;
    private final Player player;
    private final ItemStack[] generator;

    public TARDISSonicGeneratorInventory(TARDIS plugin, Sonic data, Player player) {
        this.plugin = plugin;
        this.data = data;
        this.player = player;
        generator = getItemStack();
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
                ItemStack is = new ItemStack(Material.BLAZE_ROD, 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text(NamedTextColor.WHITE + "Sonic Screwdriver"));
                im.lore(List.of(Component.text(sonic.getName())));
                im.setItemModel(sonic.getModel());
                is.setItemMeta(im);
                stack[sonic.getSlot()] = is;
            }
            if (sonic.getMaterial() == Material.BOWL && sonic.getSlot() != 45) {
                ItemStack is = new ItemStack(Material.BOWL, 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text(sonic.getName()));
                if (!sonic.getLore().isEmpty()) {
                    List<TextComponent> lore = new ArrayList<>();
                    for (String s : sonic.getLore().split("~")) {
                        lore.add(Component.text(s));
                    }
                    im.lore(lore);
                }
                im.setItemModel(sonic.getModel());
                is.setItemMeta(im);
                stack[sonic.getSlot()] = is;
            }
        }
        // info 1/3
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.displayName(Component.text("Instructions (1/3)"));
        List<TextComponent> lore = List.of(Component.text("Select your Sonic Screwdriver"), Component.text("type from the top two rows."), Component.text("Click on the upgrades you"), Component.text("want to add to the sonic."));
        info_im.lore(lore);
        info_im.setItemModel(GUISonicGenerator.INSTRUCTIONS_1_OF_3.getModel());
        info.setItemMeta(info_im);
        stack[38] = info;
        // info 2/3
        ItemStack info1 = new ItemStack(Material.BOOK, 1);
        ItemMeta info1_im = info.getItemMeta();
        info1_im.displayName(Component.text("Instructions (2/3)"));
        List<TextComponent> lore1 = List.of(Component.text("You can reset the upgrades"), Component.text("by clicking the 'Standard' button."), Component.text("The Artron cost for the"), Component.text("sonic is shown bottom left."));
        info1_im.lore(lore1);
        info1_im.setItemModel(GUISonicGenerator.INSTRUCTIONS_2_OF_3.getModel());
        info1.setItemMeta(info1_im);
        stack[39] = info1;
        // info 3/3
        ItemStack info2 = new ItemStack(Material.BOOK, 1);
        ItemMeta info2_im = info.getItemMeta();
        info2_im.displayName(Component.text("Instructions (3/3)"));
        List<TextComponent> lore2 = List.of(Component.text("The final sonic result"), Component.text("is shown in the middle"), Component.text("of the bottom row."));
        info2_im.lore(lore2);
        info2_im.setItemModel(GUISonicGenerator.INSTRUCTIONS_3_OF_3.getModel());
        info2.setItemMeta(info2_im);
        stack[40] = info2;
        // players preferred sonic
        ItemStack sonic = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta screw = sonic.getItemMeta();
        screw.displayName(Component.text("Sonic Screwdriver"));
        screw.setItemModel(data.getModel());
        List<TextComponent> upgrades = new ArrayList<>();
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
        ItemStack cost = new ItemStack(Material.BOWL, 1);
        ItemMeta cost_im = cost.getItemMeta();
        cost_im.displayName(Component.text("Artron cost"));
        cost_im.lore(List.of(Component.text(artron)));
        cost_im.setItemModel(GUISonicGenerator.ARTRON_COST.getModel());
        cost.setItemMeta(cost_im);
        stack[45] = cost;

        if (!upgrades.isEmpty()) {
            List<TextComponent> finalUps = new ArrayList<>();
            finalUps.add(Component.text("Upgrades:"));
            finalUps.addAll(upgrades);
            screw.lore(finalUps);
        }
        sonic.setItemMeta(screw);
        stack[49] = sonic;

        return stack;
    }

    public ItemStack[] getGenerator() {
        return generator;
    }
}
