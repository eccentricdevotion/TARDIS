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
package me.eccentric_nz.tardisshop;

import org.bukkit.Location;

public class TARDISShopItem {

    private final int id;
    private final String item;
    private final Location location;
    private final double cost;

    public TARDISShopItem(int id, String item, Location location, double cost) {
        this.id = id;
        this.item = item;
        this.location = location;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public Location getLocation() {
        return location;
    }

    public double getCost() {
        return cost;
    }
}
