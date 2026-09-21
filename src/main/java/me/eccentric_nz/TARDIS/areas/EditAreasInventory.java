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
package me.eccentric_nz.TARDIS.areas;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreaLocations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EditAreasInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final int area_id;
    private final Inventory inventory;

    public EditAreasInventory(TARDIS plugin, int area_id) {
        this.plugin = plugin;
        this.area_id = area_id;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Area Locations", NamedTextColor.DARK_RED));
        this.inventory.setContents(getLocations());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getLocations() {
        ItemStack[] stacks = new ItemStack[54];
        ResultSetAreaLocations rs = new ResultSetAreaLocations(plugin, area_id);
        if (rs.resultSet()) {
            int i = 0;
            for (Location l : rs.getLocations()) {
                if (i < 45) {
                    ItemStack is = ItemStack.of(Material.MAP);
                    is.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Location " + (i + 1)));
                    is.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                            Component.text(l.getWorld().getKey().getKey()),
                            Component.text("x: " + l.getBlockX()),
                            Component.text("y: " + l.getBlockY()),
                            Component.text("z: " + l.getBlockZ()),
                            Component.text("id: " + area_id)
                    )));
                    stacks[i] = is;
                    i++;
                }
            }
        }
        // Info
        ItemStack info = ItemStack.of(Material.BOOK, 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Info"));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("To REMOVE a location"),
                Component.text("select a location map"),
                Component.text("then click the Remove"),
                Component.text("button (bucket)."),
                Component.text("To ADD the location"),
                Component.text("where you are standing"),
                Component.text("click the Add button"),
                Component.text("(nether star).")
        )));
        stacks[45] = info;
        // add
        ItemStack add = ItemStack.of(Material.NETHER_STAR, 1);
        add.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Add"));
        add.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("area_id: " + area_id)).build());
        stacks[48] = add;
        // remove
        ItemStack del = ItemStack.of(Material.BUCKET, 1);
        del.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Remove"));
        stacks[50] = del;
        // close
        stacks[53] = GUIItemFactory.close();
        ;
        return stacks;
    }
}
