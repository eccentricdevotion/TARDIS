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
