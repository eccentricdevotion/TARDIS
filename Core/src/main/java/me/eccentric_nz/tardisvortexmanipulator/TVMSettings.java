/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.tardisvortexmanipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;

/**
 *
 * @author eccentric_nz
 */
public class TVMSettings {

    private final List<Location> blocks = new ArrayList<>();
    private final List<UUID> beaconSetters = new ArrayList<>();
    private final List<UUID> travellers = new ArrayList<>();

    public List<Location> getBlocks() {
        return blocks;
    }

    public List<UUID> getBeaconSetters() {
        return beaconSetters;
    }

    public List<UUID> getTravellers() {
        return travellers;
    }
}
