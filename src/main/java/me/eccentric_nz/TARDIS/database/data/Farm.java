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
package me.eccentric_nz.TARDIS.database.data;

/**
 * @author eccentric_nz
 * <p>
 * Returns a String representation of the room spawn locations in this TARDIS. IThey are in the form of {@code World name:x:y:z}
 */
public record Farm(int tardis_id, String allay, String apiary, String aquarium, String bamboo, String birdcage,
                   String farm, String geode, String happy, String hutch, String igloo, String iistubil, String lava,
                   String mangrove, String nautilus, String pen, String stable, String stall, String village) {

}
