package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBlueEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBlueNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBlueSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxBlueWestPreset;

public class PoliceBoxBluePreset extends TARDISColoured {

    public PoliceBoxBluePreset() {
        PoliceBoxBlueEastPreset east = new PoliceBoxBlueEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxBlueNorthPreset north = new PoliceBoxBlueNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxBlueWestPreset west = new PoliceBoxBlueWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxBlueSouthPreset south = new PoliceBoxBlueSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
