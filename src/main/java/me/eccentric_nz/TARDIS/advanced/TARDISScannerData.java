/*
 * Copyright (C) 2019 eccentric_nz
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

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.block.Biome;

/**
 * @author eccentric_nz
 */
public class TARDISScannerData {

    private Location scanLocation;
    private COMPASS tardisDirection;
    private long time;
    private Biome biome;

    public Location getScanLocation() {
        return scanLocation;
    }

    public void setScanLocation(Location scanLocation) {
        this.scanLocation = scanLocation;
    }

    public COMPASS getTardisDirection() {
        return tardisDirection;
    }

    public void setTardisDirection(COMPASS tardisDirection) {
        this.tardisDirection = tardisDirection;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }
}
