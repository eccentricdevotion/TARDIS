package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLightGrayEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLightGrayNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLightGraySouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLightGrayWestPreset;

public class PoliceBoxLightGrayPreset extends TARDISColoured {

    public PoliceBoxLightGrayPreset() {
        PoliceBoxLightGrayEastPreset east = new PoliceBoxLightGrayEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxLightGrayNorthPreset north = new PoliceBoxLightGrayNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxLightGrayWestPreset west = new PoliceBoxLightGrayWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxLightGraySouthPreset south = new PoliceBoxLightGraySouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
