package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxGreenEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxGreenNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxGreenSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxGreenWestPreset;

public class PoliceBoxGreenPreset extends TARDISColoured {

    public PoliceBoxGreenPreset() {
        PoliceBoxGreenEastPreset east = new PoliceBoxGreenEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxGreenNorthPreset north = new PoliceBoxGreenNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxGreenWestPreset west = new PoliceBoxGreenWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxGreenSouthPreset south = new PoliceBoxGreenSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
