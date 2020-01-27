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

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxMagentaEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxMagentaNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxMagentaSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxMagentaWestPreset;

public class PoliceBoxMagentaPreset extends TARDISColoured {

    public PoliceBoxMagentaPreset() {
        PoliceBoxMagentaEastPreset east = new PoliceBoxMagentaEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxMagentaNorthPreset north = new PoliceBoxMagentaNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxMagentaWestPreset west = new PoliceBoxMagentaWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxMagentaSouthPreset south = new PoliceBoxMagentaSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
