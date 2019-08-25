package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLightBlueEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLightBlueNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLightBlueSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLightBlueWestPreset;

public class PoliceBoxLightBluePreset extends TARDISColoured {

    public PoliceBoxLightBluePreset() {
        PoliceBoxLightBlueEastPreset east = new PoliceBoxLightBlueEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxLightBlueNorthPreset north = new PoliceBoxLightBlueNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxLightBlueWestPreset west = new PoliceBoxLightBlueWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxLightBlueSouthPreset south = new PoliceBoxLightBlueSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
