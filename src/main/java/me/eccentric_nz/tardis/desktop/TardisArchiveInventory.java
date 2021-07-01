/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.desktop;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.custommodeldata.GuiArchive;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisSize;
import me.eccentric_nz.tardis.enumeration.ConsoleSize;
import me.eccentric_nz.tardis.schematic.ResultSetArchiveButtons;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone through at least twelve redesigns, though
 * the TARDIS revealed that she had archived 30 versions. Once a control room was reconfigured, the TARDIS archived the
 * old design "for neatness". The TARDIS effectively "curated" a museum of control rooms — both those in the Doctor's
 * personal past and future
 *
 * @author eccentric_nz
 */
class TardisArchiveInventory {

    private final ItemStack[] menu;
    private final TardisPlugin plugin;
    private final Player player;

    TardisArchiveInventory(TardisPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        int i = 0;
        // get archived consoles
        ResultSetArchiveButtons rs = new ResultSetArchiveButtons(plugin, player.getUniqueId().toString());
        if (rs.resultSet()) {
            for (ItemStack is : rs.getButtons()) {
                stack[i] = is;
                i++;
            }
        }
        // back
        ItemStack back = new ItemStack(Material.BOWL, 1);
        ItemMeta back_im = back.getItemMeta();
        assert back_im != null;
        back_im.setDisplayName("Back");
        back_im.setCustomModelData(GuiArchive.BACK.getCustomModelData());
        back.setItemMeta(back_im);
        stack[17] = back;
        // size
        ItemStack size = new ItemStack(Material.BOWL, 1);
        ItemMeta size_im = size.getItemMeta();
        assert size_im != null;
        size_im.setDisplayName("Set size");
        size_im.setCustomModelData(GuiArchive.SET_SIZE.getCustomModelData());
        String s = "SMALL";
        String b = "16 x 16 x 16 blocks";

        // get current console size
        ResultSetTardisSize rss = new ResultSetTardisSize(plugin);
        if (rss.fromUuid(player.getUniqueId().toString())) {
            s = rss.getConsoleSize().toString();
            b = rss.getConsoleSize().getBlocks();
        }
        size_im.setLore(Arrays.asList(s, b, ChatColor.AQUA + "Click to change"));
        size.setItemMeta(size_im);
        stack[18] = size;
        // scan
        ItemStack scan = new ItemStack(Material.BOWL, 1);
        ItemMeta but_im = scan.getItemMeta();
        assert but_im != null;
        but_im.setDisplayName("Scan console");
        but_im.setCustomModelData(GuiArchive.SCAN_CONSOLE.getCustomModelData());
        scan.setItemMeta(but_im);
        stack[19] = scan;
        // archive
        ItemStack arc = new ItemStack(Material.BOWL, 1);
        ItemMeta hive_im = arc.getItemMeta();
        assert hive_im != null;
        hive_im.setDisplayName("Archive current console");
        hive_im.setLore(Arrays.asList("A random name will", "be generated - use the", "/tardis archive command", "to set your own."));
        hive_im.setCustomModelData(GuiArchive.ARCHIVE_CURRENT_CONSOLE.getCustomModelData());
        arc.setItemMeta(hive_im);
        stack[20] = arc;
        // templates
        int t = 22;
        for (ConsoleSize c : ConsoleSize.values()) {
            if (!c.equals(ConsoleSize.MASSIVE)) {
                ItemStack temp = new ItemStack(Material.BOWL, 1);
                ItemMeta late = temp.getItemMeta();
                assert late != null;
                late.setDisplayName(c.toString());
                late.setCustomModelData(GuiArchive.valueOf(c.toString()).getCustomModelData());
                late.setLore(Arrays.asList("Cobblestone template", c.getBlocks()));
                temp.setItemMeta(late);
                stack[t] = temp;
                t++;
            }
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        assert close_im != null;
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GuiArchive.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stack[26] = close;

        return stack;
    }

    ItemStack[] getArchives() {
        return menu;
    }
}
