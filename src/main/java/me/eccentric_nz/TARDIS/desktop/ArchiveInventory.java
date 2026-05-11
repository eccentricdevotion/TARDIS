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
package me.eccentric_nz.TARDIS.desktop;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIArchive;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisSize;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.schematic.archive.ResultSetArchiveButtons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone through at least twelve redesigns, though
 * the TARDIS revealed that she had archived 30 versions. Once a control room was reconfigured, the TARDIS archived the
 * old design "for neatness". The TARDIS effectively "curated" a museum of control rooms — both those in the Doctor's
 * personal past and future
 *
 * @author eccentric_nz
 */
class ArchiveInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Player player;
    private final Inventory inventory;

    ArchiveInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("TARDIS Archive", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
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
        ItemStack back = ItemStack.of(GUIArchive.BACK.material(), 1);
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Back"));
        stack[17] = back;
        // size
        ItemStack size = ItemStack.of(GUIArchive.SET_SIZE.material(), 1);
        size.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Set size"));
        String s = "SMALL";
        String b = "16 x 16 x 16 blocks";

        // get current console size
        ResultSetTardisSize rss = new ResultSetTardisSize(plugin);
        if (rss.fromUUID(player.getUniqueId().toString())) {
            s = rss.getConsoleSize().toString();
            b = rss.getConsoleSize().getBlocks();
        }
        size.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text(s),
                Component.text(b),
                Component.text("Click to change", NamedTextColor.AQUA)
        )));
        stack[18] = size;
        // scan
        ItemStack scan = ItemStack.of(GUIArchive.SCAN_CONSOLE.material(), 1);
        scan.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Scan console"));
        stack[19] = scan;
        // archive
        ItemStack archive = ItemStack.of(GUIArchive.ARCHIVE_CURRENT_CONSOLE.material(), 1);
        archive.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Archive current console"));
        archive.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("A random name will"),
                Component.text("be generated - use the"),
                Component.text("/tardis archive command"),
                Component.text("to set your own.")
        )));
        stack[20] = archive;
        // templates
        int t = 22;
        for (ConsoleSize c : ConsoleSize.values()) {
            if (!c.equals(ConsoleSize.MASSIVE) && !c.equals(ConsoleSize.WIDE)) {
                ItemStack template = ItemStack.of(GUIArchive.SMALL.material(), 1);
                template.setData(DataComponentTypes.CUSTOM_NAME, Component.text(c.toString()));
                template.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                        Component.text("Cobblestone template"),
                        Component.text(c.getBlocks())
                )));
                stack[t] = template;
                t++;
            }
        }
        // close
        stack[GUIArchive.CLOSE.slot()] = GUIItemFactory.close();

        return stack;
    }
}
