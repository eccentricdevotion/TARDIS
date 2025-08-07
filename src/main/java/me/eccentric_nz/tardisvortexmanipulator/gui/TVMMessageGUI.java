/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMMessageGUI implements InventoryHolder {

    private final TARDIS plugin;
    private final int start, finish;
    private final String uuid;
    private final Inventory inventory;

    public TVMMessageGUI(TARDIS plugin, int start, int finish, String uuid) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("VM Messages", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Vortex Manipulator Messages GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get the player's messages
        TVMResultSetInbox rs = new TVMResultSetInbox(plugin, uuid, start, 44);
        if (rs.resultSet()) {
            List<TVMMessage> messages = rs.getMail();
            for (TVMMessage m : messages) {
                // message
                ItemStack mess;
                if (m.isRead()) {
                    mess = ItemStack.of(Material.BOOK, 1);
                } else {
                    mess = ItemStack.of(Material.WRITABLE_BOOK, 1);
                }
                ItemMeta age = mess.getItemMeta();
                age.displayName(Component.text("#" + (i + start + 1)));
                String from = plugin.getServer().getOfflinePlayer(m.getWho()).getName();
                age.lore(List.of(
                        Component.text("From: " + from),
                        Component.text("Date: " + m.getDate()),
                        Component.text(m.getId())
                ));
                mess.setItemMeta(age);
                stack[i] = mess;
                i++;
            }
        }

        int n = start / 44 + 1;
        // page number
        ItemStack page = ItemStack.of(Material.BOWL, 1);
        ItemMeta num = page.getItemMeta();
        num.displayName(Component.text("Page " + n));
        page.setItemMeta(num);
        stack[45] = page;
        // close
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.displayName(Component.text("Close"));
        close.setItemMeta(win);
        stack[46] = close;
        // previous screen (only if needed)
        if (start > 0) {
            ItemStack prev = ItemStack.of(Material.ARROW, 1);
            ItemMeta een = prev.getItemMeta();
            een.displayName(Component.text("Previous page"));
            prev.setItemMeta(een);
            stack[48] = prev;
        }
        // next screen (only if needed)
        if (finish > 44) {
            ItemStack next = ItemStack.of(Material.ARROW, 1);
            ItemMeta scr = next.getItemMeta();
            scr.displayName(Component.text("Next page"));
            next.setItemMeta(scr);
            stack[49] = next;
        }
        // read
        ItemStack read = ItemStack.of(Material.BOWL, 1);
        ItemMeta daer = read.getItemMeta();
        daer.displayName(Component.text("Read"));
        read.setItemMeta(daer);
        stack[51] = read;
        // delete
        ItemStack del = ItemStack.of(Material.BUCKET, 1);
        ItemMeta ete = del.getItemMeta();
        ete.displayName(Component.text("Delete"));
        del.setItemMeta(ete);
        stack[53] = del;

        return stack;
    }
}
