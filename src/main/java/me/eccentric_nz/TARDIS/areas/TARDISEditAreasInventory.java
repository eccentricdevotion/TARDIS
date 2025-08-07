/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreaLocations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISEditAreasInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final int area_id;
    private final Inventory inventory;

    public TARDISEditAreasInventory(TARDIS plugin, int area_id) {
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
                    ItemMeta im = is.getItemMeta();
                    im.displayName(Component.text("Location " + (i + 1)));
                    im.lore(List.of(
                            Component.text(l.getWorld().getName()),
                            Component.text("x: " + l.getBlockX()),
                            Component.text("y: " + l.getBlockY()),
                            Component.text("z: " + l.getBlockZ()),
                            Component.text("id: " + area_id)
                    ));
                    is.setItemMeta(im);
                    stacks[i] = is;
                    i++;
                }
            }
        }
        // Info
        ItemStack info = ItemStack.of(Material.BOOK, 1);
        ItemMeta ii = info.getItemMeta();
        ii.displayName(Component.text("Info"));
        ii.lore(List.of(
                Component.text("To REMOVE a location"),
                Component.text("select a location map"),
                Component.text("then click the Remove"),
                Component.text("button (bucket)."),
                Component.text("To ADD the location"),
                Component.text("where you are standing"),
                Component.text("click the Add button"),
                Component.text("(nether star).")
        ));
        info.setItemMeta(ii);
        stacks[45] = info;
        // add
        ItemStack add = ItemStack.of(Material.NETHER_STAR, 1);
        ItemMeta er = add.getItemMeta();
        er.displayName(Component.text("Add"));
        er.lore(List.of(Component.text("area_id: " + area_id)));
        add.setItemMeta(er);
        stacks[48] = add;
        // remove
        ItemStack del = ItemStack.of(Material.BUCKET, 1);
        ItemMeta dd = del.getItemMeta();
        dd.displayName(Component.text("Remove"));
        del.setItemMeta(dd);
        stacks[50] = del;
        // close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(close_im);
        stacks[53] = close;
        return stacks;
    }
}
