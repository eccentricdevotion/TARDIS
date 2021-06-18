/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import org.bukkit.Location;

/**
 * @author eccentric_nz
 */
public class TardisScannerData {

    private Location scanLocation;
    private CardinalDirection tardisDirection;
    private long time;
    private String scannedBiome;

    public Location getScanLocation() {
        return scanLocation;
    }

    public void setScanLocation(Location scanLocation) {
        this.scanLocation = scanLocation;
    }

    public CardinalDirection getTardisDirection() {
        return tardisDirection;
    }

    public void setTardisDirection(CardinalDirection tardisDirection) {
        this.tardisDirection = tardisDirection;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getScannedBiome() {
        return scannedBiome;
    }

    public void setScannedBiome(String scannedBiome) {
        this.scannedBiome = scannedBiome;
    }
}
