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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ArtronCondenserInventory implements InventoryHolder {

    private final String title;
    private final Location location;
    private final Inventory inventory;

    public ArtronCondenserInventory(TARDIS plugin, InventoryHolder holder, String title, Location location) {
        this.title = title;
        this.location = location;
        ItemStack[] stacks = holder.getInventory().getContents();
        this.inventory = plugin.getServer().createInventory(this, stacks.length, Component.text(title, NamedTextColor.DARK_RED));
        this.inventory.setContents(stacks);
        holder.getInventory().clear();
    }

    public String getTitle() {
        return title;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
