/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMSave;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMSavesGUI {

    private final TARDIS plugin;
    private final int start, finish;
    private final String uuid;
    private final ItemStack[] gui;
    private final HashMap<String, Material> blocks = new HashMap<>();

    public TVMSavesGUI(TARDIS plugin, int start, int finish, String uuid) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
        blocks.put("NORMAL", Material.DIRT);
        blocks.put("NETHER", Material.NETHERRACK);
        blocks.put("THE_END", Material.END_STONE);
        gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Vortex Manipulator Saves GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get the player's messages
        TVMResultSetSaves rs = new TVMResultSetSaves(plugin, uuid, start, 44);
        if (rs.resultSet()) {
            List<TVMSave> saves = rs.getSaves();
            for (TVMSave s : saves) {
                // save
                ItemStack save = new ItemStack(blocks.get(s.getEnv()), 1);
                ItemMeta warp = save.getItemMeta();
                warp.setDisplayName(s.getName());
                warp.setLore(Arrays.asList("World: " + s.getWorld(), "x: " + oneDecimal(s.getX()), "y: " + s.getY(), "z: " + oneDecimal(s.getZ())));
                save.setItemMeta(warp);
                stack[i] = save;
                i++;
            }
        }

        int n = start / 44 + 1;
        // page number
        ItemStack page = new ItemStack(Material.BOWL, 1);
        ItemMeta num = page.getItemMeta();
        num.setDisplayName("Page " + n);
        num.setItemModel(Bowl.VM_PAGE.getKey());
        page.setItemMeta(num);
        stack[45] = page;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName("Close");
        win.setItemModel(Bowl.CLOSE.getKey());
        close.setItemMeta(win);
        stack[46] = close;
        // previous screen (only if needed)
        if (start > 0) {
            ItemStack prev = new ItemStack(Material.BOWL, 1);
            ItemMeta een = prev.getItemMeta();
            een.setDisplayName("Previous Page");
            een.setItemModel(Bowl.VM_PREV.getKey());
            prev.setItemMeta(een);
            stack[48] = prev;
        }
        // next screen (only if needed)
        if (finish > 44) {
            ItemStack next = new ItemStack(Material.BOWL, 1);
            ItemMeta scr = next.getItemMeta();
            scr.setDisplayName("Next Page");
            scr.setItemModel(Bowl.VM_NEXT.getKey());
            next.setItemMeta(scr);
            stack[49] = next;
        }
        // delete
        ItemStack del = new ItemStack(Material.BOWL, 1);
        ItemMeta ete = del.getItemMeta();
        ete.setDisplayName("Delete");
        ete.setItemModel(Bowl.VM_DELETE.getKey());
        del.setItemMeta(ete);
        stack[51] = del;
        // warp
        ItemStack warp = new ItemStack(Material.BOWL, 1);
        ItemMeta to = warp.getItemMeta();
        to.setDisplayName("Enter Vortex");
        to.setItemModel(Bowl.VM_WARP.getKey());
        warp.setItemMeta(to);
        stack[53] = warp;

        return stack;
    }

    public ItemStack[] getGUI() {
        return gui;
    }

    private String oneDecimal(double d) {
        return String.format("%f.1", d);
    }
}
