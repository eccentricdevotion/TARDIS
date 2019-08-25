package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLimeEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLimeNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLimeSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxLimeWestPreset;

public class PoliceBoxLimePreset extends TARDISColoured {

    public PoliceBoxLimePreset() {
        PoliceBoxLimeEastPreset east = new PoliceBoxLimeEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxLimeNorthPreset north = new PoliceBoxLimeNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxLimeWestPreset west = new PoliceBoxLimeWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxLimeSouthPreset south = new PoliceBoxLimeSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
