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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUISonicGenerator;
import me.eccentric_nz.TARDIS.database.data.Sonic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
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
                im.setDisplayName(ChatColor.WHITE + "Sonic Screwdriver");
                im.setLore(List.of(sonic.getName()));
                CustomModelDataComponent component = im.getCustomModelDataComponent();
                component.setFloats(sonic.getFloats());
                im.setCustomModelDataComponent(component);
                is.setItemMeta(im);
                stack[sonic.getSlot()] = is;
            }
            if (sonic.getMaterial() == Material.BOWL && sonic.getSlot()!= 45) {
                ItemStack is = new ItemStack(Material.BOWL, 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(sonic.getName());
                if (!sonic.getLore().isEmpty()) {
                    im.setLore(List.of(sonic.getLore().split("~")));
                }
                is.setItemMeta(im);
                stack[sonic.getSlot()] = is;
            }
        }
        // info 1/3
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Instructions (1/3)");
        List<String> lore = List.of("Select your Sonic Screwdriver", "type from the top two rows.", "Click on the upgrades you", "want to add to the sonic.");
        info_im.setLore(lore);
        info.setItemMeta(info_im);
        stack[38] = info;
        // info 2/3
        ItemStack info1 = new ItemStack(Material.BOOK, 1);
        ItemMeta info1_im = info.getItemMeta();
        info1_im.setDisplayName("Instructions (2/3)");
        List<String> lore1 = List.of("You can reset the upgrades", "by clicking the 'Standard' button.", "The Artron cost for the", "sonic is shown bottom left.");
        info1_im.setLore(lore1);
        info1.setItemMeta(info1_im);
        stack[39] = info1;
        // info 3/3
        ItemStack info2 = new ItemStack(Material.BOOK, 1);
        ItemMeta info2_im = info.getItemMeta();
        info2_im.setDisplayName("Instructions (3/3)");
        List<String> lore2 = List.of("The final sonic result", "is shown in the middle", "of the bottom row.");
        info2_im.setLore(lore2);
        info2.setItemMeta(info2_im);
        stack[40] = info2;
        // players preferred sonic
        ItemStack sonic = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta screw = sonic.getItemMeta();
        screw.setDisplayName("Sonic Screwdriver");
        CustomModelDataComponent scomponent = screw.getCustomModelDataComponent();
        scomponent.setFloats(data.getModel());
        screw.setCustomModelDataComponent(scomponent);
        List<String> upgrades = new ArrayList<>();
        double full = plugin.getArtronConfig().getDouble("full_charge") / 100.0d;
        int artron = (int) (plugin.getArtronConfig().getDouble("sonic_generator.standard") * full);
        if (data.hasBio()) {
            upgrades.add("Bio-scanner Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full);
        }
        if (data.hasDiamond()) {
            upgrades.add("Diamond Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.diamond") * full);
        }
        if (data.hasEmerald()) {
            upgrades.add("Emerald Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.emerald") * full);
        }
        if (data.hasRedstone()) {
            upgrades.add("Redstone Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.redstone") * full);
        }
        if (data.hasPainter()) {
            upgrades.add("Painter Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.painter") * full);
        }
        if (data.hasIgnite()) {
            upgrades.add("Ignite Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.ignite") * full);
        }
        if (data.hasArrow()) {
            upgrades.add("Pickup Arrows Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.arrow") * full);
        }
        if (data.hasKnockback()) {
            upgrades.add("Knockback Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.knockback") * full);
        }
        if (data.hasBrush()) {
            upgrades.add("Brush Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.brush") * full);
        }
        if (data.hasConversion()) {
            upgrades.add("Conversion Upgrade");
            artron += (int) (plugin.getArtronConfig().getDouble("sonic_generator.conversion") * full);
        }
        // cost
        ItemStack cost = new ItemStack(Material.BOWL, 1);
        ItemMeta cost_im = cost.getItemMeta();
        cost_im.setDisplayName("Artron cost");
        cost_im.setLore(List.of("" + artron));
        cost.setItemMeta(cost_im);
        stack[45] = cost;

        if (!upgrades.isEmpty()) {
            List<String> finalUps = new ArrayList<>();
            finalUps.add("Upgrades:");
            finalUps.addAll(upgrades);
            screw.setLore(finalUps);
        }
        sonic.setItemMeta(screw);
        stack[49] = sonic;

        return stack;
    }

    public ItemStack[] getGenerator() {
        return generator;
    }
}
