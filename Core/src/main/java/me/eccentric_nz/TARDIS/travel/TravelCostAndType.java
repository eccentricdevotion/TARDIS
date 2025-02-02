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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.enumeration.TravelType;

public class TravelCostAndType {

    private final int cost;
    private final TravelType travelType;

    public TravelCostAndType(int cost, TravelType travelType) {
        this.cost = cost;
        this.travelType = travelType;
    }

    public int getCost() {
        return cost;
    }

    public TravelType getTravelType() {
        return travelType;
    }
}
