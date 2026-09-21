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
package me.eccentric_nz.TARDIS.chameleon.gui;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetColour;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColourPickerListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public ColourPickerListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof ColourPickerInventory)) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 18 || slot > 53) {
            return;
        }
        InventoryView view = event.getView();
        switch (slot) {
            case 20 -> {
                // red less
                less(view, 10, 0, 0);
                setRed(view);
            }
            case 24 -> {
                // red more
                more(view, 10, 0, 0);
                setRed(view);
            }
            case 29 -> {
                // green less
                less(view, 0, 10, 0);
                setGreen(view);
            }
            case 33 -> {
                // green more
                more(view, 0, 10, 0);
                setGreen(view);
            }
            case 38 -> {
                // blue less
                less(view, 0, 0, 10);
                setBlue(view);
            }
            case 42 -> {
                // blue more
                more(view, 0, 0, 10);
                setBlue(view);
            }
            case 35 -> {
                // get the player's tardis_id
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                if (rst.fromUUID(player.getUniqueId().toString())) {
                    int id = rst.getTardisId();
                    // save the colour
                    Color color = getColour(view);
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("red", color.getRed());
                    set.put("green", color.getGreen());
                    set.put("blue", color.getBlue());
                    // do they have an existing record?
                    ResultSetColour rsc = new ResultSetColour(plugin, id);
                    if (rsc.resultSet()) {
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        plugin.getQueryFactory().doUpdate("colour", set, where);
                    } else {
                        set.put("tardis_id", id);
                        plugin.getQueryFactory().doInsert("colour", set);
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "COLOUR_SET");
                    // damage the circuit if configured
                    DamageUtility.run(plugin, DiskCircuit.CHAMELEON, id, player);
                }
                close(player);
            }
            case 53 -> close(player); // close
            default -> event.setCancelled(true);
        }
    }

    private void less(InventoryView view, int r, int g, int b) {
        ItemStack display = view.getItem(4);
        DyedItemColor meta = display.getData(DataComponentTypes.DYED_COLOR);
        Color color = meta.color();
        int rr = color.getRed() - r;
        if (rr < 0) {
            rr = 0;
        }
        int gg = color.getGreen() - g;
        if (gg < 0) {
            gg = 0;
        }
        int bb = color.getBlue() - b;
        if (bb < 0) {
            bb = 0;
        }
        List<Component> lore = new ArrayList<>(3);
        lore.add(Component.text("Red: " + rr));
        lore.add(Component.text("Green: " + gg));
        lore.add(Component.text("Blue: " + bb));
        display.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        display.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                .color(Color.fromRGB(rr, gg, bb))
                .build());
    }

    private void more(InventoryView view, int r, int g, int b) {
        ItemStack display = view.getItem(4);
        DyedItemColor meta = display.getData(DataComponentTypes.DYED_COLOR);
        Color color = meta.color();
        int rr = color.getRed() + r;
        if (rr > 255) {
            rr = 255;
        }
        int gg = color.getGreen() + g;
        if (gg > 255) {
            gg = 255;
        }
        int bb = color.getBlue() + b;
        if (bb > 255) {
            bb = 255;
        }
        List<Component> lore = new ArrayList<>(3);
        lore.add(Component.text("Red: " + rr));
        lore.add(Component.text("Green: " + gg));
        lore.add(Component.text("Blue: " + bb));
        display.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        display.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                .color(Color.fromRGB(rr, gg, bb))
                .build());
    }

    private void setRed(InventoryView view) {
        Color color = getColour(view);
        ItemStack red = view.getItem(22);
        red.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                .color(Color.fromRGB(color.getRed(), 0, 0))
                .build());
    }

    private void setGreen(InventoryView view) {
        Color color = getColour(view);
        ItemStack green = view.getItem(31);
        green.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                .color(Color.fromRGB(0, color.getGreen(), 0))
                .build());
    }

    private void setBlue(InventoryView view) {
        Color color = getColour(view);
        ItemStack blue = view.getItem(40);
        blue.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                .color(Color.fromRGB(0, 0, color.getBlue()))
                .build());
    }

    private Color getColour(InventoryView view) {
        ItemStack display = view.getItem(4);
        DyedItemColor meta = display.getData(DataComponentTypes.DYED_COLOR);
        return meta.color();
    }
}
