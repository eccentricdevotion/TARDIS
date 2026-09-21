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
package me.eccentric_nz.TARDIS.rooms.eye;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIArtronStorage;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronStorage;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EyeStorage implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public EyeStorage(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 9, Component.text("Artron Capacitor Storage", NamedTextColor.DARK_RED));
        this.inventory.setContents(getGUI(id));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getGUI(int id) {
        ItemStack[] stacks = new ItemStack[9];
        // info
        ItemStack info = ItemStack.of(GUIArtronStorage.INFO.material(), 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Info"));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Increase the Artron storage"),
                Component.text("capacity by placing"),
                Component.text("up to 5 Artron Capacitors"),
                Component.text("in the slots to the right.")
        )));
        stacks[GUIArtronStorage.INFO.slot()] = info;
        // right arrow
        ItemStack r_arrow = ItemStack.of(GUIArtronStorage.ARROW_RIGHT.material(), 1);
        r_arrow.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(""));
        stacks[GUIArtronStorage.ARROW_RIGHT.slot()] = r_arrow;
        ResultSetArtronStorage rs = new ResultSetArtronStorage(plugin);
        if (rs.fromID(id)) {
            int count = rs.getCapacitorCount();
            int damaged = rs.getDamageCount();
            // capacitors
            for (int i = 2; i < 2 + count; i++) {
                ItemStack is = ItemStack.of(Material.BUCKET, 1);
                is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(i > (2 + count) - damaged ? "Damaged Artron Capacitor" : "Artron Capacitor"));
                stacks[i] = is;
            }
        }
        // left arrow
        ItemStack l_arrow = ItemStack.of(GUIArtronStorage.ARROW_LEFT.material(), 1);
        l_arrow.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(" "));
        stacks[GUIArtronStorage.ARROW_LEFT.slot()] = l_arrow;
        // close
        stacks[GUIArtronStorage.CLOSE.slot()] = GUIItemFactory.close();
        return stacks;
    }
}
