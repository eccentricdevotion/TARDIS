/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMMessageGUI {

    private final TARDISVortexManipulator plugin;
    private final int start, finish;
    private final String uuid;
    private final ItemStack[] gui;

    public TVMMessageGUI(TARDISVortexManipulator plugin, int start, int finish, String uuid) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
        gui = getItemStack();
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
                    mess = new ItemStack(Material.BOOK, 1);
                } else {
                    mess = new ItemStack(Material.WRITABLE_BOOK, 1);
                }
                ItemMeta age = mess.getItemMeta();
                age.setDisplayName("#" + (i + start + 1));
                String from = plugin.getServer().getOfflinePlayer(m.getWho()).getName();
                age.setLore(Arrays.asList("From: " + from, "Date: " + m.getDate(), "" + m.getId()));
                mess.setItemMeta(age);
                stack[i] = mess;
                i++;
            }
        }

        int n = start / 44 + 1;
        // page number
        ItemStack page = new ItemStack(Material.BOWL, 1);
        ItemMeta num = page.getItemMeta();
        num.setDisplayName("Page " + n);
        num.setCustomModelData(119);
        page.setItemMeta(num);
        stack[45] = page;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName("Close");
        win.setCustomModelData(1);
        close.setItemMeta(win);
        stack[46] = close;
        // previous screen (only if needed)
        if (start > 0) {
            ItemStack prev = new ItemStack(Material.BOWL, 1);
            ItemMeta een = prev.getItemMeta();
            een.setDisplayName("Previous Page");
            een.setCustomModelData(120);
            prev.setItemMeta(een);
            stack[48] = prev;
        }
        // next screen (only if needed)
        if (finish > 44) {
            ItemStack next = new ItemStack(Material.BOWL, 1);
            ItemMeta scr = next.getItemMeta();
            scr.setDisplayName("Next Page");
            scr.setCustomModelData(116);
            next.setItemMeta(scr);
            stack[49] = next;
        }
        // read
        ItemStack read = new ItemStack(Material.BOWL, 1);
        ItemMeta daer = read.getItemMeta();
        daer.setDisplayName("Read");
        daer.setCustomModelData(121);
        read.setItemMeta(daer);
        stack[51] = read;
        // delete
        ItemStack del = new ItemStack(Material.BOWL, 1);
        ItemMeta ete = del.getItemMeta();
        ete.setDisplayName("Delete");
        ete.setCustomModelData(107);
        del.setItemMeta(ete);
        stack[53] = del;

        return stack;
    }

    public ItemStack[] getGUI() {
        return gui;
    }
}
