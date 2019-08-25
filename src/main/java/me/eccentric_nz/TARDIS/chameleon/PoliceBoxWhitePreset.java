package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxWhiteEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxWhiteNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxWhiteSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxWhiteWestPreset;

public class PoliceBoxWhitePreset extends TARDISColoured {

    public PoliceBoxWhitePreset() {
        PoliceBoxWhiteEastPreset east = new PoliceBoxWhiteEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxWhiteNorthPreset north = new PoliceBoxWhiteNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxWhiteWestPreset west = new PoliceBoxWhiteWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxWhiteSouthPreset south = new PoliceBoxWhiteSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
