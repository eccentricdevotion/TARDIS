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
package me.eccentric_nz.TARDIS.upgrades;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.SystemUpgrade;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SystemTreeInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final SystemUpgrade sysData;
    private final Inventory inventory;

    public SystemTreeInventory(TARDIS plugin, SystemUpgrade sysData) {
        this.plugin = plugin;
        this.sysData = sysData;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS System Upgrades", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStacks());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the TARDIS Upgrades GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStacks() {
        ItemStack[] stacks = new ItemStack[54];
        for (SystemTree g : SystemTree.values()) {
            if (g.getSlot() != -1) {
                ItemStack is = ItemStack.of(g.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                if (g.getBranch().equals("branch")) {
                    im.displayName(Component.text(g.getName(), NamedTextColor.GOLD).decorate(TextDecoration.ITALIC));
                } else {
                    im.displayName(Component.text(g.getName()));
                }
                List<Component> lore = new ArrayList<>(g.getLore());
                boolean has = sysData.getUpgrades().get(g);
                if (!has) {
                    String cost;
                    if (g.getBranch().equals("branch")) {
                        cost = plugin.getSystemUpgradesConfig().getString("branch");
                    } else {
                        cost = plugin.getSystemUpgradesConfig().getString(g.getBranch() + "." + g.toString().toLowerCase(Locale.ROOT));
                    }
                    lore.add(Component.text("Cost: " + cost, NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                } else if (g != SystemTree.UPGRADE_TREE) {
                    lore.add(Component.text("Unlocked", NamedTextColor.GREEN).decorate(TextDecoration.ITALIC));
                } else {
                    // add players current Artron level to UPGRADE_TREE
                    lore.add(Component.text("Artron Level: " + sysData.getArtronLevel(), NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                }
                im.lore(lore);
                // does the player have this system upgrade?
                im.setItemModel((has) ? g.getUnlocked() : g.getLocked());
                is.setItemMeta(im);
                stacks[g.getSlot()] = is;
            }
        }
        // left_down
        ItemStack ld = ItemStack.of(SystemTree.LEFT_DOWN.getMaterial(), 1);
        ItemMeta eft = ld.getItemMeta();
        eft.displayName(ComponentUtils.toWhite(""));
        eft.setItemModel(SystemTree.LEFT_DOWN.getLocked());
        ld.setItemMeta(eft);
        stacks[0] = ld;
        // horizontal
        int[] horizontal = new int[]{1, 3, 5, 7};
        ItemStack his = ItemStack.of(SystemTree.H_LINE.getMaterial(), 1);
        ItemMeta him = his.getItemMeta();
        him.displayName(ComponentUtils.toWhite(""));
        him.setItemModel(SystemTree.H_LINE.getLocked());
        his.setItemMeta(him);
        for (int h : horizontal) {
            stacks[h] = his;
        }
        // both_down
        int[] both_down = new int[]{2, 6};
        ItemStack bd = ItemStack.of(SystemTree.BOTH_DOWN.getMaterial(), 1);
        ItemMeta bdim = bd.getItemMeta();
        bdim.displayName(ComponentUtils.toWhite(""));
        bdim.setItemModel(SystemTree.BOTH_DOWN.getLocked());
        bd.setItemMeta(bdim);
        for (int d : both_down) {
            stacks[d] = bd;
        }
        // right_down
        ItemStack rd = ItemStack.of(SystemTree.RIGHT_DOWN.getMaterial(), 1);
        ItemMeta own = rd.getItemMeta();
        own.displayName(ComponentUtils.toWhite(""));
        own.setItemModel(SystemTree.RIGHT_DOWN.getLocked());
        rd.setItemMeta(own);
        stacks[8] = rd;
        // background
        ItemStack is = ItemStack.of(SystemTree.BLANK.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite(""));
        im.setItemModel(SystemTree.BLANK.getLocked());
        is.setItemMeta(im);
        stacks[10] = is;
        // vertical
        ItemStack vert = ItemStack.of(SystemTree.VERTICAL.getMaterial(), 1);
        ItemMeta ical = vert.getItemMeta();
        ical.displayName(ComponentUtils.toWhite(""));
        ical.setItemModel(SystemTree.VERTICAL.getLocked());
        vert.setItemMeta(ical);
        stacks[13] = vert;
        // close
        ItemStack close = ItemStack.of(SystemTree.CLOSE.getMaterial(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close_im.setItemModel(SystemTree.CLOSE.getLocked());
        close.setItemMeta(close_im);
        stacks[45] = close;
        return stacks;
    }
}
