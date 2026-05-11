/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMSave;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMSavesGUI implements InventoryHolder {

    private final TARDIS plugin;
    private final int start, finish;
    private final String uuid;
    private final HashMap<String, Material> blocks = new HashMap<>();
    private final Inventory inventory;

    public TVMSavesGUI(TARDIS plugin, int start, int finish, String uuid) {
        this.plugin = plugin;
        this.start = start;
        this.finish = finish;
        this.uuid = uuid;
        blocks.put("NORMAL", Material.DIRT);
        blocks.put("NETHER", Material.NETHERRACK);
        blocks.put("THE_END", Material.END_STONE);
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("VM Saves", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
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
                ItemStack save = ItemStack.of(blocks.get(s.getEnv()), 1);
                save.setData(DataComponentTypes.CUSTOM_NAME, Component.text(s.getName()));
                save.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                        Component.text("World: " + s.getWorld()),
                        Component.text("x: " + oneDecimal(s.getX())),
                        Component.text("y: " + s.getY()),
                        Component.text("z: " + oneDecimal(s.getZ()))
                )));
                stack[i] = save;
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
            prev.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Previous Page"));
            stack[48] = prev;
        }
        // next screen (only if needed)
        if (finish > 44) {
            ItemStack next = ItemStack.of(Material.ARROW, 1);
            next.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Next page"));
            stack[49] = next;
        }
        // delete
        ItemStack delete = ItemStack.of(Material.BUCKET, 1);
        delete.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Delete"));
        stack[51] = delete;
        // warp
        ItemStack warp = ItemStack.of(Material.BOWL, 1);
        warp.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Enter Vortex"));
        stack[53] = warp;

        return stack;
    }

    private String oneDecimal(double d) {
        return String.format("%f.1", d);
    }
}
