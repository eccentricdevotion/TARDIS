package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxPinkEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxPinkNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxPinkSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxPinkWestPreset;

public class PoliceBoxPinkPreset extends TARDISColoured {

    public PoliceBoxPinkPreset() {
        PoliceBoxPinkEastPreset east = new PoliceBoxPinkEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxPinkNorthPreset north = new PoliceBoxPinkNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxPinkWestPreset west = new PoliceBoxPinkWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxPinkSouthPreset south = new PoliceBoxPinkSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
