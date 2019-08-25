package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxGrayEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxGrayNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxGraySouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxGrayWestPreset;

public class PoliceBoxGrayPreset extends TARDISColoured {

    public PoliceBoxGrayPreset() {
        PoliceBoxGrayEastPreset east = new PoliceBoxGrayEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxGrayNorthPreset north = new PoliceBoxGrayNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxGrayWestPreset west = new PoliceBoxGrayWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxGraySouthPreset south = new PoliceBoxGraySouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
