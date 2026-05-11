/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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
                mess.setData(DataComponentTypes.CUSTOM_NAME, Component.text("#" + (i + start + 1)));
                String from = plugin.getServer().getOfflinePlayer(m.getWho()).getName();
                mess.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                        Component.text("From: " + from),
                        Component.text("Date: " + m.getDate()),
                        Component.text(m.getId())
                )));
                stack[i] = mess;
                i++;
            }
        }

        int n = start / 44 + 1;
        // page number
        ItemStack page = ItemStack.of(Material.BOWL, 1);
        page.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Page " + n));
        stack[45] = page;
        // close
        stack[46] = GUIItemFactory.close();
        // previous screen (only if needed)
        if (start > 0) {
            ItemStack prev = ItemStack.of(Material.ARROW, 1);
            prev.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Previous page"));
            stack[48] = prev;
        }
        // next screen (only if needed)
        if (finish > 44) {
            ItemStack next = ItemStack.of(Material.ARROW, 1);
            next.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Next page"));
            stack[49] = next;
        }
        // read
        ItemStack read = ItemStack.of(Material.BOWL, 1);
        read.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Read"));
        stack[51] = read;
        // delete
        ItemStack delete = ItemStack.of(Material.BUCKET, 1);
        delete.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Delete"));
        stack[53] = delete;

        return stack;
    }
}
