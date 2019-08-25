package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxOrangeEastPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxOrangeNorthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxOrangeSouthPreset;
import me.eccentric_nz.TARDIS.chameleon.coloured.PoliceBoxOrangeWestPreset;

public class PoliceBoxOrangePreset extends TARDISColoured {

    public PoliceBoxOrangePreset() {
        PoliceBoxOrangeEastPreset east = new PoliceBoxOrangeEastPreset();
        setBlueprintDataEast(east.getBlueprintData());
        setStainedDataEast(east.getStainedData());
        setGlassDataEast(east.getGlassData());
        PoliceBoxOrangeNorthPreset north = new PoliceBoxOrangeNorthPreset();
        setBlueprintDataNorth(north.getBlueprintData());
        setStainedDataNorth(north.getStainedData());
        setGlassDataNorth(north.getGlassData());
        PoliceBoxOrangeWestPreset west = new PoliceBoxOrangeWestPreset();
        setBlueprintDataWest(west.getBlueprintData());
        setStainedDataWest(west.getStainedData());
        setGlassDataWest(west.getGlassData());
        PoliceBoxOrangeSouthPreset south = new PoliceBoxOrangeSouthPreset();
        setBlueprintDataSouth(south.getBlueprintData());
        setStainedDataSouth(south.getStainedData());
        setGlassDataSouth(south.getGlassData());
    }
}
