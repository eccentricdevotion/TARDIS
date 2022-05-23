/*
 * Copyright (C) 2022 eccentric_nz
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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.GUISonicGenerator;
import me.eccentric_nz.TARDIS.database.data.Sonic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                ItemStack is = new ItemStack(sonic.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(sonic.getChatColor() + "Sonic Screwdriver");
                im.setLore(Collections.singletonList(sonic.getName()));
                im.setCustomModelData(sonic.getCustomModelData());
                is.setItemMeta(im);
                stack[sonic.getSlot()] = is;
            }
        }
        // \u00a7 = § (ChatColor code)
        // info 1/3
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Instructions (1/3)");
        List<String> lore = Arrays.asList("Select your Sonic Screwdriver", "type from the top two rows.", "Click on the upgrades you", "want to add to the sonic.");
        info_im.setLore(lore);
        info_im.setCustomModelData(GUISonicGenerator.INSTRUCTIONS_1_OF_3.getCustomModelData());
        info.setItemMeta(info_im);
        stack[38] = info;
        // info 2/3
        ItemStack info1 = new ItemStack(Material.BOOK, 1);
        ItemMeta info1_im = info.getItemMeta();
        info1_im.setDisplayName("Instructions (2/3)");
        List<String> lore1 = Arrays.asList("You can reset the upgrades", "by clicking the 'Standard' button.", "The Artron cost for the", "sonic is shown bottom left.");
        info1_im.setLore(lore1);
        info1_im.setCustomModelData(GUISonicGenerator.INSTRUCTIONS_2_OF_3.getCustomModelData());
        info1.setItemMeta(info1_im);
        stack[39] = info1;
        // info 3/3
        ItemStack info2 = new ItemStack(Material.BOOK, 1);
        ItemMeta info2_im = info.getItemMeta();
        info2_im.setDisplayName("Instructions (3/3)");
        List<String> lore2 = Arrays.asList("The final sonic result", "is shown in the middle", "of the bottom row.");
        info2_im.setLore(lore2);
        info2_im.setCustomModelData(GUISonicGenerator.INSTRUCTIONS_3_OF_3.getCustomModelData());
        info2.setItemMeta(info2_im);
        stack[40] = info2;
        // standard sonic
        ItemStack sta = new ItemStack(Material.BOWL, 1);
        ItemMeta dard = sta.getItemMeta();
        dard.setDisplayName("Standard Sonic");
        dard.setCustomModelData(GUISonicGenerator.STANDARD_SONIC.getCustomModelData());
        sta.setItemMeta(dard);
        stack[27] = sta;
        // knockback upgrade
        if (TARDISPermission.hasPermission(player, "tardis.sonic.knockback")) {
            ItemStack knock = new ItemStack(Material.BOWL, 1);
            ItemMeta back = knock.getItemMeta();
            back.setDisplayName("Knockback Upgrade");
            back.setCustomModelData(GUISonicGenerator.KNOCKBACK_UPGRADE.getCustomModelData());
            knock.setItemMeta(back);
            stack[28] = knock;
        }
        // bio-scanner upgrade
        if (TARDISPermission.hasPermission(player, "tardis.sonic.bio")) {
            ItemStack bio = new ItemStack(Material.BOWL, 1);
            ItemMeta scan = bio.getItemMeta();
            scan.setDisplayName("Bio-scanner Upgrade");
            scan.setCustomModelData(GUISonicGenerator.BIO_SCANNER_UPGRADE.getCustomModelData());
            bio.setItemMeta(scan);
            stack[29] = bio;
        }
        // diamond disruptor upgrade
        if (TARDISPermission.hasPermission(player, "tardis.sonic.diamond")) {
            ItemStack dis = new ItemStack(Material.BOWL, 1);
            ItemMeta rupt = dis.getItemMeta();
            rupt.setDisplayName("Diamond Upgrade");
            rupt.setCustomModelData(GUISonicGenerator.DIAMOND_UPGRADE.getCustomModelData());
            dis.setItemMeta(rupt);
            stack[30] = dis;
        }
        // emerald environment upgrade
        if (TARDISPermission.hasPermission(player, "tardis.sonic.emerald")) {
            ItemStack eme = new ItemStack(Material.BOWL, 1);
            ItemMeta rald = eme.getItemMeta();
            rald.setDisplayName("Emerald Upgrade");
            rald.setCustomModelData(GUISonicGenerator.EMERALD_UPGRADE.getCustomModelData());
            eme.setItemMeta(rald);
            stack[31] = eme;
        }
        // redstone activator upgrade
        if (TARDISPermission.hasPermission(player, "tardis.sonic.redstone")) {
            ItemStack red = new ItemStack(Material.BOWL, 1);
            ItemMeta stone = red.getItemMeta();
            stone.setDisplayName("Redstone Upgrade");
            stone.setCustomModelData(GUISonicGenerator.REDSTONE_UPGRADE.getCustomModelData());
            red.setItemMeta(stone);
            stack[32] = red;
        }
        // painter upgrade
        if (TARDISPermission.hasPermission(player, "tardis.sonic.paint")) {
            ItemStack pai = new ItemStack(Material.BOWL, 1);
            ItemMeta nter = pai.getItemMeta();
            nter.setDisplayName("Painter Upgrade");
            nter.setCustomModelData(GUISonicGenerator.PAINTER_UPGRADE.getCustomModelData());
            pai.setItemMeta(nter);
            stack[33] = pai;
        }
        // ignite upgrade
        if (TARDISPermission.hasPermission(player, "tardis.sonic.ignite")) {
            ItemStack ign = new ItemStack(Material.BOWL, 1);
            ItemMeta ite = ign.getItemMeta();
            ite.setDisplayName("Ignite Upgrade");
            ite.setCustomModelData(GUISonicGenerator.IGNITE_UPGRADE.getCustomModelData());
            ign.setItemMeta(ite);
            stack[34] = ign;
        }
        // arrow upgrade
        if (TARDISPermission.hasPermission(player, "tardis.sonic.arrow")) {
            ItemStack arr = new ItemStack(Material.BOWL, 1);
            ItemMeta ow = arr.getItemMeta();
            ow.setDisplayName("Pickup Arrows Upgrade");
            ow.setCustomModelData(GUISonicGenerator.PICKUP_ARROWS_UPGRADE.getCustomModelData());
            arr.setItemMeta(ow);
            stack[35] = arr;
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setLore(Arrays.asList("Close the menu without", "saving or generating."));
        close_im.setCustomModelData(GUISonicGenerator.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stack[53] = close;
        // save
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta save_im = save.getItemMeta();
        save_im.setDisplayName("Save settings");
        save_im.setLore(Arrays.asList("Click to save the current sonic.", "No item will be generated!"));
        save_im.setCustomModelData(GUISonicGenerator.SAVE_SETTINGS.getCustomModelData());
        save.setItemMeta(save_im);
        stack[43] = save;
        // generate
        ItemStack generate = new ItemStack(Material.BOWL, 1);
        ItemMeta gen_im = generate.getItemMeta();
        gen_im.setDisplayName("Generate Sonic Screwdriver");
        gen_im.setLore(Arrays.asList("Click to generate a sonic", "with the current settings."));
        gen_im.setCustomModelData(GUISonicGenerator.GENERATE_SONIC_SCREWDRIVER.getCustomModelData());
        generate.setItemMeta(gen_im);
        stack[44] = generate;
        // players preferred sonic
        ItemStack sonic = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta screw = sonic.getItemMeta();
        String dn = (data.getSonicType().equals(ChatColor.RESET)) ? "Sonic Screwdriver" : data.getSonicType() + "Sonic Screwdriver";
        screw.setDisplayName(dn);
        screw.setCustomModelData(data.getCustomModelData());
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
        // cost
        ItemStack cost = new ItemStack(Material.BOWL, 1);
        ItemMeta cost_im = cost.getItemMeta();
        cost_im.setDisplayName("Artron cost");
        cost_im.setLore(Collections.singletonList("" + artron));
        cost_im.setCustomModelData(GUISonicGenerator.ARTRON_COST.getCustomModelData());
        cost.setItemMeta(cost_im);
        stack[45] = cost;

        if (upgrades.size() > 0) {
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
