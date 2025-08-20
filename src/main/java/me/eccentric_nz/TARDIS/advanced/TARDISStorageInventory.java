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
package me.eccentric_nz.TARDIS.advanced;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.enumeration.GlowstoneCircuit;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.HashMap;

public class TARDISStorageInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISStorageInventory(TARDIS plugin, String title, ItemStack[] contents) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text(title, NamedTextColor.DARK_RED));
        this.inventory.setContents(contents);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
