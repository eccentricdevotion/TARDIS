package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBlackEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBlackNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBlackSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBlackWestPreset;

public class PoliceBoxBlackPreset extends TARDISColoured {

    public PoliceBoxBlackPreset() {
        PoliceBoxBlackEastPreset east = new PoliceBoxBlackEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxBlackNorthPreset north = new PoliceBoxBlackNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxBlackWestPreset west = new PoliceBoxBlackWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxBlackSouthPreset south = new PoliceBoxBlackSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
