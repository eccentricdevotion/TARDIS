/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBrownEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBrownNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBrownSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBrownWestPreset;

public class PoliceBoxBrownPreset extends TARDISColoured {

    public PoliceBoxBrownPreset() {
        PoliceBoxBrownEastPreset east = new PoliceBoxBrownEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxBrownNorthPreset north = new PoliceBoxBrownNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxBrownWestPreset west = new PoliceBoxBrownWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxBrownSouthPreset south = new PoliceBoxBrownSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
