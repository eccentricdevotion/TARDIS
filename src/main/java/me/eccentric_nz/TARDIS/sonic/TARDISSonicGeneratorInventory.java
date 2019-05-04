/*
 * Copyright (C) 2018 eccentric_nz
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
        // \u00a7 = § (ChatColor code)
        // mark I
        ItemStack markone = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta markone_im = markone.getItemMeta();
        markone_im.setDisplayName(ChatColor.DARK_GRAY + "Sonic Screwdriver");
        markone_im.setLore(Collections.singletonList("Mark I"));
        markone.setItemMeta(markone_im);
        // mark II
        ItemStack marktwo = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta marktwo_im = marktwo.getItemMeta();
        marktwo_im.setDisplayName(ChatColor.YELLOW + "Sonic Screwdriver");
        marktwo_im.setLore(Collections.singletonList("Mark II"));
        marktwo.setItemMeta(marktwo_im);
        // mark III
        ItemStack markthree = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta markthree_im = markthree.getItemMeta();
        markthree_im.setDisplayName(ChatColor.DARK_PURPLE + "Sonic Screwdriver");
        markthree_im.setLore(Collections.singletonList("Mark III"));
        markthree.setItemMeta(markthree_im);
        // mark IV
        ItemStack markfour = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta markfour_im = markfour.getItemMeta();
        markfour_im.setDisplayName(ChatColor.GRAY + "Sonic Screwdriver");
        markfour_im.setLore(Collections.singletonList("Mark IV"));
        markfour.setItemMeta(markfour_im);
        // mcgann
        ItemStack mcgann = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta mcgann_im = mcgann.getItemMeta();
        mcgann_im.setDisplayName(ChatColor.BLUE + "Sonic Screwdriver");
        mcgann_im.setLore(Collections.singletonList("Eighth Doctor"));
        mcgann.setItemMeta(mcgann_im);
        // eccelston
        ItemStack eccelston = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta eccelston_im = eccelston.getItemMeta();
        eccelston_im.setDisplayName(ChatColor.GREEN + "Sonic Screwdriver");
        eccelston_im.setLore(Collections.singletonList("Ninth Doctor"));
        eccelston.setItemMeta(eccelston_im);
        // eccelston open
        ItemStack eccelston_open = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta eccelston_open_im = eccelston_open.getItemMeta();
        eccelston_open_im.setDisplayName(ChatColor.DARK_GREEN + "Sonic Screwdriver");
        eccelston_open_im.setLore(Collections.singletonList("Ninth Doctor Open"));
        eccelston_open.setItemMeta(eccelston_open_im);
        // tennant
        ItemStack tennant = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta tennant_im = tennant.getItemMeta();
        tennant_im.setDisplayName(ChatColor.AQUA + "Sonic Screwdriver");
        tennant_im.setLore(Collections.singletonList("Tenth Doctor"));
        tennant.setItemMeta(tennant_im);
        // tennant open
        ItemStack tennant_open = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta tennant_open_im = tennant_open.getItemMeta();
        tennant_open_im.setDisplayName(ChatColor.DARK_AQUA + "Sonic Screwdriver");
        tennant_open_im.setLore(Collections.singletonList("Tenth Doctor Open"));
        tennant_open.setItemMeta(tennant_open_im);
        // smith
        ItemStack smith = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta smith_im = smith.getItemMeta();
        smith_im.setDisplayName("Sonic Screwdriver");
        smith_im.setLore(Collections.singletonList("Eleventh Doctor"));
        smith.setItemMeta(smith_im);
        // smith open
        ItemStack smith_open = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta smith_open_im = smith_open.getItemMeta();
        smith_open_im.setDisplayName(ChatColor.LIGHT_PURPLE + "Sonic Screwdriver");
        smith_open_im.setLore(Collections.singletonList("Eleventh Doctor Open"));
        smith_open.setItemMeta(smith_open_im);
        // hurt
        ItemStack hurt = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta hurt_im = hurt.getItemMeta();
        hurt_im.setDisplayName(ChatColor.DARK_RED + "Sonic Screwdriver");
        hurt_im.setLore(Collections.singletonList("War Doctor"));
        hurt.setItemMeta(hurt_im);
        // master
        ItemStack master = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta master_im = master.getItemMeta();
        master_im.setDisplayName(ChatColor.DARK_BLUE + "Sonic Screwdriver");
        master_im.setLore(Collections.singletonList("Master"));
        master.setItemMeta(master_im);
        // sarah jane
        ItemStack sarahjane = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta sarahjane_im = sarahjane.getItemMeta();
        sarahjane_im.setDisplayName(ChatColor.RED + "Sonic Screwdriver");
        sarahjane_im.setLore(Collections.singletonList("Sarah Jane"));
        sarahjane.setItemMeta(sarahjane_im);
        // river song
        ItemStack song = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta song_im = song.getItemMeta();
        song_im.setDisplayName(ChatColor.GOLD + "Sonic Screwdriver");
        song_im.setLore(Collections.singletonList("River Song"));
        song.setItemMeta(song_im);
        // twelfth doctor (peter capaldi)
        ItemStack peter = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta capaldi = peter.getItemMeta();
        capaldi.setDisplayName(ChatColor.UNDERLINE + "Sonic Screwdriver");
        capaldi.setLore(Collections.singletonList("Twelfth Doctor"));
        peter.setItemMeta(capaldi);
        // thirteenth doctor (jodie whittaker)
        ItemStack whittaker = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta jodie = whittaker.getItemMeta();
        jodie.setDisplayName(ChatColor.BLACK + "Sonic Screwdriver");
        jodie.setLore(Collections.singletonList("Thirteenth Doctor"));
        whittaker.setItemMeta(jodie);
        // info 1/3
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Instructions (1/3)");
        List<String> lore = Arrays.asList("Select your Sonic Screwdriver", "type from the top two rows.", "Click on the upgrades you", "want to add to the sonic.");
        info_im.setLore(lore);
        info.setItemMeta(info_im);
        // info 2/3
        ItemStack info1 = new ItemStack(Material.BOOK, 1);
        ItemMeta info1_im = info.getItemMeta();
        info1_im.setDisplayName("Instructions (2/3)");
        List<String> lore1 = Arrays.asList("You can reset the upgrades", "by clicking the 'Standard' button.", "The Artron cost for the", "sonic is shown bottom left.");
        info1_im.setLore(lore1);
        info1.setItemMeta(info1_im);
        // info 3/3
        ItemStack info2 = new ItemStack(Material.BOOK, 1);
        ItemMeta info2_im = info.getItemMeta();
        info2_im.setDisplayName("Instructions (3/3)");
        List<String> lore2 = Arrays.asList("The final sonic result", "is shown in the middle", "of the bottom row.");
        info2_im.setLore(lore2);
        info2.setItemMeta(info2_im);
        // standard sonic
        ItemStack sta = new ItemStack(Material.BOWL, 1);
        ItemMeta dard = sta.getItemMeta();
        dard.setDisplayName("Standard Sonic");
        sta.setItemMeta(dard);
        // bio-scanner uprgrade
        ItemStack bio = null;
        if (player.hasPermission("tardis.sonic.bio")) {
            bio = new ItemStack(Material.BOWL, 1);
            ItemMeta scan = bio.getItemMeta();
            scan.setDisplayName("Bio-scanner Upgrade");
            bio.setItemMeta(scan);
        }
        // diamond disruptor upgrade
        ItemStack dis = null;
        if (player.hasPermission("tardis.sonic.diamond")) {
            dis = new ItemStack(Material.BOWL, 1);
            ItemMeta rupt = dis.getItemMeta();
            rupt.setDisplayName("Diamond Upgrade");
            dis.setItemMeta(rupt);
        }
        // emerald environment upgrade
        ItemStack eme = null;
        if (player.hasPermission("tardis.sonic.emerald")) {
            eme = new ItemStack(Material.BOWL, 1);
            ItemMeta rald = eme.getItemMeta();
            rald.setDisplayName("Emerald Upgrade");
            eme.setItemMeta(rald);
        }
        // redstone activator upgrade
        ItemStack red = null;
        if (player.hasPermission("tardis.sonic.redstone")) {
            red = new ItemStack(Material.BOWL, 1);
            ItemMeta stone = red.getItemMeta();
            stone.setDisplayName("Redstone Upgrade");
            red.setItemMeta(stone);
        }
        // painter upgrade
        ItemStack pai = null;
        if (player.hasPermission("tardis.sonic.paint")) {
            pai = new ItemStack(Material.BOWL, 1);
            ItemMeta nter = pai.getItemMeta();
            nter.setDisplayName("Painter Upgrade");
            pai.setItemMeta(nter);
        }
        // ignite upgrade
        ItemStack ign = null;
        if (player.hasPermission("tardis.sonic.ignite")) {
            ign = new ItemStack(Material.BOWL, 1);
            ItemMeta ite = ign.getItemMeta();
            ite.setDisplayName("Ignite Upgrade");
            ign.setItemMeta(ite);
        }
        // arrow upgrade
        ItemStack arr = null;
        if (player.hasPermission("tardis.sonic.arrow")) {
            arr = new ItemStack(Material.BOWL, 1);
            ItemMeta ita = ign.getItemMeta();
            ita.setDisplayName("Pickup Arrows Upgrade");
            arr.setItemMeta(ita);
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close_im.setLore(Arrays.asList("Close the menu without", "saving or generating."));
        close.setItemMeta(close_im);
        // save
        ItemStack save = new ItemStack(Material.BOWL, 1);
        ItemMeta save_im = save.getItemMeta();
        save_im.setDisplayName("Save settings");
        save_im.setLore(Arrays.asList("Click to save the current sonic.", "No item will be generated!"));
        save.setItemMeta(save_im);
        // generate
        ItemStack generate = new ItemStack(Material.BOWL, 1);
        ItemMeta gen_im = generate.getItemMeta();
        gen_im.setDisplayName("Generate Sonic Screwdriver");
        gen_im.setLore(Arrays.asList("Click to generate a sonic", "with the current settings."));
        generate.setItemMeta(gen_im);
        // players preferred sonic
        ItemStack sonic = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta screw = sonic.getItemMeta();
        String dn = (data.getSonicType().equals(ChatColor.RESET)) ? "Sonic Screwdriver" : data.getSonicType() + "Sonic Screwdriver";
        screw.setDisplayName(dn);
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
        // cost
        ItemStack cost = new ItemStack(Material.BOWL, 1);
        ItemMeta cost_im = cost.getItemMeta();
        cost_im.setDisplayName("Artron cost");
        cost_im.setLore(Collections.singletonList("" + artron));
        cost.setItemMeta(cost_im);

        if (upgrades.size() > 0) {
            List<String> finalUps = new ArrayList<>();
            finalUps.add("Upgrades:");
            finalUps.addAll(upgrades);
            screw.setLore(finalUps);
        }
        sonic.setItemMeta(screw);

        return new ItemStack[]{
                markone, marktwo, markthree, markfour, mcgann, eccelston, tennant, smith, hurt,
                whittaker, master, sarahjane, song, null, eccelston_open, tennant_open, smith_open, peter,
                null, null, null, null, null, null, null, null, null,
                sta, null, bio, dis, eme, red, pai, ign, arr,
                null, null, info, info1, info2, null, null, save, generate,
                cost, null, null, null, sonic, null, null, null, close
        };
    }

    public ItemStack[] getGenerator() {
        return generator;
    }
}
