package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxYellowEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxYellowNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxYellowSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxYellowWestPreset;

public class PoliceBoxYellowPreset extends TARDISColoured {

    public PoliceBoxYellowPreset() {
        PoliceBoxYellowEastPreset east = new PoliceBoxYellowEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxYellowNorthPreset north = new PoliceBoxYellowNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxYellowWestPreset west = new PoliceBoxYellowWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxYellowSouthPreset south = new PoliceBoxYellowSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
